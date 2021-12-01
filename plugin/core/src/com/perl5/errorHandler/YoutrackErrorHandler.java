/*
 * Copyright 2015-2021 Alexandr Evstigneev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.perl5.errorHandler;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.intellij.diagnostic.DiagnosticBundle;
import com.intellij.diagnostic.IdeErrorsDialog;
import com.intellij.ide.BrowserUtil;
import com.intellij.ide.DataManager;
import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationListener;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.application.ApplicationInfo;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.*;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.Consumer;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.util.PerlPluginUtil;
import org.apache.http.Consts;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.intellij.openapi.diagnostic.SubmittedReportInfo.SubmissionStatus.FAILED;
import static com.intellij.openapi.diagnostic.SubmittedReportInfo.SubmissionStatus.NEW_ISSUE;
import static com.intellij.openapi.util.text.StringUtil.isEmpty;
import static com.perl5.errorHandler.YoutrackApi.YoutrackIssue;
import static com.perl5.errorHandler.YoutrackApi.YoutrackIssueResponse;

public class YoutrackErrorHandler extends ErrorReportSubmitter {
  private static final Logger LOGGER = Logger.getInstance(YoutrackErrorHandler.class);
  private static final @NonNls String SERVER_URL = "https://camelcade.myjetbrains.com/youtrack";
  private static final String SERVER_REST_URL = SERVER_URL + "/api";
  private static final String ISSUES_REST_URL = SERVER_REST_URL + "/issues";
  private static final String SERVER_ISSUE_URL = ISSUES_REST_URL + "?fields=idReadable,id";
  private static final String ACCESS_TOKEN = "Bearer perm:YXV0b3JlcG9ydGVy.NjEtMA==.iG2Zu7rMOzfjaGny7gFHyrOf9NEMCA";


  @Override
  public @NotNull String getReportActionText() {
    return PerlBundle.message("perl.issue.report");
  }

  @Override
  public boolean submit(IdeaLoggingEvent @NotNull [] events,
                        @Nullable String additionalInfo,
                        @NotNull Component parentComponent,
                        @NotNull Consumer<? super SubmittedReportInfo> consumer) {
    final DataContext dataContext = DataManager.getInstance().getDataContext(parentComponent);
    final Project project = CommonDataKeys.PROJECT.getData(dataContext);

    Task.Backgroundable task = new Task.Backgroundable(project, DiagnosticBundle.message("title.submitting.error.report")) {
      @Override
      public void run(@NotNull ProgressIndicator indicator) {
        consumer.consume(doSubmit(events, additionalInfo, project));
      }
    };
    task.queue();
    return true;
  }

  private SubmittedReportInfo doSubmit(IdeaLoggingEvent @NotNull [] ideaLoggingEvents,
                                       @Nullable String addInfo,
                                       @Nullable Project project) {
    final IdeaLoggingEvent ideaLoggingEvent = ideaLoggingEvents[0];
    final String throwableText = ideaLoggingEvent.getThrowableText();
    String description = throwableText.substring(0, Math.min(80, throwableText.length()));

    StringBuilder descBuilder = new StringBuilder();

    descBuilder.append("Build: ").append(ApplicationInfo.getInstance().getBuild()).append('\n');
    descBuilder.append("OS: ").append(SystemInfo.OS_NAME).append(" ").append(SystemInfo.OS_ARCH).append(" ").append(SystemInfo.OS_VERSION)
      .append('\n');
    descBuilder.append("Java Vendor: ").append(SystemInfo.JAVA_VENDOR).append('\n');
    descBuilder.append("Java Version: ").append(SystemInfo.JAVA_VERSION).append('\n');
    descBuilder.append("Java Runtime Version: ").append(SystemInfo.JAVA_RUNTIME_VERSION).append('\n');
    descBuilder.append("Perl Plugin Version: ").append(PerlPluginUtil.getPluginVersion()).append('\n');
    descBuilder.append("Description: ").append(StringUtil.notNullize(addInfo, "<none>"));

    List<Attachment> attachments = new ArrayList<>();
    for (IdeaLoggingEvent e : ideaLoggingEvents) {
      descBuilder
        .append("\n").append("Message: ").append(StringUtil.notNullize(e.getMessage(), "none"))
        .append("\n").append("Throwable:\n").append(e.getThrowableText())
        .append("\n")
      ;

      Throwable throwable = e.getThrowable();

      while (throwable != null) {
        if (throwable instanceof ExceptionWithAttachments) {
          ContainerUtil.addAll(attachments, ((ExceptionWithAttachments)throwable).getAttachments());
        }
        throwable = throwable.getCause();
      }
    }

    String issueNumber = submit(description, descBuilder.toString(), attachments);
    LOGGER.info("Error submitted, response: " + issueNumber);

    if (issueNumber == null) {
      return new SubmittedReportInfo(SERVER_ISSUE_URL, "", FAILED);
    }

    final SubmittedReportInfo reportInfo = new SubmittedReportInfo(SERVER_URL + "/issue/" + issueNumber, issueNumber, NEW_ISSUE);

    popupResultInfo(reportInfo, project);

    return reportInfo;
  }

  /**
   * @return human-readable issue number or null if failed to create one
   */
  public @Nullable String submit(@Nullable String desc,
                                 @NotNull String body,
                                 @NotNull List<Attachment> attachments) {
    if (isEmpty(desc)) {
      throw new RuntimeException(DiagnosticBundle.message("error.report.failure.message"));
    }

    var issueResponse = createIssue(desc, body);
    if (issueResponse == null) {
      return null;
    }

    attachFiles(issueResponse, attachments);

    return issueResponse.idReadable;
  }

  private void attachFiles(@NotNull YoutrackIssueResponse issueResponse, @NotNull List<Attachment> attachments) {
    if (attachments.isEmpty()) {
      return;
    }

    MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();

    ContentType contentType = ContentType.create("text/plain", Consts.UTF_8);
    for (Attachment it : attachments) {
      entityBuilder.addBinaryBody("attachments[]", it.getBytes(), contentType, it.getName());
    }

    try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
      HttpPost issuePost = new HttpPost(String.join("/", ISSUES_REST_URL, issueResponse.id, "attachments"));
      issuePost.setEntity(entityBuilder.build());
      issuePost.addHeader("Authorization", ACCESS_TOKEN);

      CloseableHttpResponse response;
      try {
        response = httpClient.execute(issuePost);
      }
      catch (IOException ex) {
        LOGGER.warn("Error attaching files to the issue: " + ex.getMessage() + "; issue id: " + issueResponse.idReadable);
        return;
      }

      var statusLine = response.getStatusLine();
      var responsePayload = EntityUtils.toString(response.getEntity());
      if (statusLine.getStatusCode() != 200) {
        LOGGER.warn("Error attaching files: status=" + statusLine +
                    "; response: " + responsePayload +
                    "; issue id: " + issueResponse.idReadable
        );
      }
    }
    catch (IOException e) {
      LOGGER.warn(e.getMessage());
    }
  }

  private @Nullable YoutrackIssueResponse createIssue(@NotNull String desc, @NotNull String body) {
    try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
      // posting an issue
      var issue = new YoutrackIssue(
        desc.replaceAll("[\r\n]", ""),
        body
      );

      var gson = new Gson();
      var requestContent = gson.toJson(issue);

      HttpPost issuePost = new HttpPost(SERVER_ISSUE_URL);
      issuePost.setEntity(EntityBuilder.create()
                            .setContentType(ContentType.create("application/json", Consts.UTF_8))
                            .setText(requestContent).build());
      issuePost.addHeader("Authorization", ACCESS_TOKEN);

      CloseableHttpResponse response;
      try {
        response = httpClient.execute(issuePost);
      }
      catch (IOException ex) {
        LOGGER.warn("Error posting an issue: " + ex.getMessage() + "; request: " + requestContent);
        return null;
      }

      var statusLine = response.getStatusLine();
      var responsePayload = EntityUtils.toString(response.getEntity());
      if (statusLine.getStatusCode() != 200) {
        LOGGER.warn("Error submitting report: status=" + statusLine +
                    "; response: " + responsePayload +
                    "; request: " + requestContent
        );
        return null;
      }

      try {
        return gson.fromJson(responsePayload, YoutrackIssueResponse.class);
      }
      catch (JsonSyntaxException e) {
        LOGGER.warn("Error decoding server response: " + responsePayload + "; request: " + requestContent);
      }
    }
    catch (IOException e) {
      LOGGER.warn(e);
    }
    return null;
  }

  private static void popupResultInfo(@NotNull SubmittedReportInfo reportInfo, final @Nullable Project project) {
    ApplicationManager.getApplication().invokeLater(() -> {
      StringBuilder text = new StringBuilder("<html>");
      final String url = reportInfo.getStatus() == SubmittedReportInfo.SubmissionStatus.FAILED || reportInfo.getLinkText() == null
                         ? null
                         : reportInfo.getURL();
      IdeErrorsDialog.appendSubmissionInformation(reportInfo, text);
      text.append(".");
      final SubmittedReportInfo.SubmissionStatus status = reportInfo.getStatus();
      if (status == SubmittedReportInfo.SubmissionStatus.NEW_ISSUE) {
        text.append("<br/>").append(DiagnosticBundle.message("error.report.gratitude"));
      }
      else if (status == SubmittedReportInfo.SubmissionStatus.DUPLICATE) {
        text.append("<br/>Possible duplicate report");
      }
      text.append("</html>");
      NotificationType type;
      if (status == SubmittedReportInfo.SubmissionStatus.FAILED) {
        type = NotificationType.ERROR;
      }
      else if (status == SubmittedReportInfo.SubmissionStatus.DUPLICATE) {
        type = NotificationType.WARNING;
      }
      else {
        type = NotificationType.INFORMATION;
      }
      NotificationListener listener = url != null ? (notification, event) -> {
        BrowserUtil.browse(url);
        notification.expire();
      } : null;
      NotificationGroupManager.getInstance().getNotificationGroup("Error Report")
        .createNotification(DiagnosticBundle.message("error.report.title"), text.toString(), type, listener).notify(project);
    });
  }


}