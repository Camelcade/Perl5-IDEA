/*
 * Copyright 2015-2019 Alexandr Evstigneev
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

import com.intellij.diagnostic.DiagnosticBundle;
import com.intellij.diagnostic.IdeErrorsDialog;
import com.intellij.diagnostic.ReportMessages;
import com.intellij.ide.BrowserUtil;
import com.intellij.ide.DataManager;
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
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import static com.intellij.openapi.diagnostic.SubmittedReportInfo.SubmissionStatus.FAILED;
import static com.intellij.openapi.diagnostic.SubmittedReportInfo.SubmissionStatus.NEW_ISSUE;
import static com.intellij.openapi.util.text.StringUtil.isEmpty;

public class YoutrackErrorHandler extends ErrorReportSubmitter {
  public static final String PROJECT = "CAMELCADE";
  private static final Logger LOGGER = Logger.getInstance(YoutrackErrorHandler.class);
  @NonNls
  private static final String SERVER_URL = "https://camelcade.myjetbrains.com/youtrack/";
  private static final String SERVER_REST_URL = SERVER_URL + "rest/";
  private static final String SERVER_ISSUE_URL = SERVER_REST_URL + "issue";
  private static final String LOGIN_URL = SERVER_REST_URL + "user/login";

  @NotNull
  @Override
  public String getReportActionText() {
    return PerlBundle.message("perl.issue.report");
  }

  @Override
  public boolean submit(@NotNull IdeaLoggingEvent[] events,
                        @Nullable String additionalInfo,
                        @NotNull Component parentComponent,
                        @NotNull Consumer<SubmittedReportInfo> consumer) {
    final DataContext dataContext = DataManager.getInstance().getDataContext(parentComponent);
    final Project project = CommonDataKeys.PROJECT.getData(dataContext);

    Task.Backgroundable task = new Task.Backgroundable(project, DiagnosticBundle.message("title.submitting.error.report")) {
      @Override
      public void run(@NotNull ProgressIndicator indicator) {
        consumer.consume(doSubmit(events, additionalInfo, parentComponent));
      }
    };
    task.queue();
    return true;
  }

  private SubmittedReportInfo doSubmit(IdeaLoggingEvent[] ideaLoggingEvents, String addInfo, Component component) {
    final DataContext dataContext = DataManager.getInstance().getDataContext(component);
    final Project project = CommonDataKeys.PROJECT.getData(dataContext);
    final IdeaLoggingEvent ideaLoggingEvent = ideaLoggingEvents[0];
    final String throwableText = ideaLoggingEvent.getThrowableText();
    String description = throwableText.substring(0, Math.min(Math.max(80, throwableText.length()), 80));

    StringBuilder descBuilder = new StringBuilder();

    descBuilder.append("Build: ").append(ApplicationInfo.getInstance().getBuild()).append('\n');
    descBuilder.append("OS: ").append(SystemInfo.OS_NAME).append(" ").append(SystemInfo.OS_ARCH).append(" ").append(SystemInfo.OS_VERSION)
      .append('\n');
    descBuilder.append("Java Vendor: ").append(SystemInfo.JAVA_VENDOR).append('\n');
    descBuilder.append("Java Version: ").append(SystemInfo.JAVA_VERSION).append('\n');
    descBuilder.append("Java Arch: ").append(SystemInfo.is32Bit ? "32 bit" : "64 bit").append('\n');
    descBuilder.append("Java Runtime Version: ").append(SystemInfo.JAVA_RUNTIME_VERSION).append('\n');
    descBuilder.append("Perl Plugin Version: ").append(PerlPluginUtil.getPluginVersion()).append('\n');
    descBuilder.append("Description: ").append(StringUtil.notNullize(addInfo, "<none>"));

    List<Attachment> attachments = new ArrayList<>();
    for (IdeaLoggingEvent e : ideaLoggingEvents) {
      descBuilder
        .append("\n").append("Message: ").append(e.getMessage())
        .append("\n").append("Throwable: ").append(e.getThrowableText())
        .append("\n")
      ;

      Throwable throwable = e.getThrowable();
      if (throwable instanceof ExceptionWithAttachments) {
        ContainerUtil.addAll(attachments, ((ExceptionWithAttachments)throwable).getAttachments());
      }
    }

    String result = submit(description, descBuilder.toString(), PerlPluginUtil.getPluginVersion(), attachments);
    LOGGER.info("Error submitted, response: " + result);

    if (result == null) {
      return new SubmittedReportInfo(SERVER_ISSUE_URL, "", FAILED);
    }

    String ResultString = null;
    try {
      Pattern regex = Pattern.compile("id=\"([^\"]+)\"", Pattern.DOTALL | Pattern.MULTILINE);
      Matcher regexMatcher = regex.matcher(result);
      if (regexMatcher.find()) {
        ResultString = regexMatcher.group(1);
      }
    }
    catch (PatternSyntaxException ex) {
      // Syntax error in the regular expression
    }

    if (ResultString == null) {
      return new SubmittedReportInfo(SERVER_ISSUE_URL, "", FAILED);
    }


    final SubmittedReportInfo reportInfo = new SubmittedReportInfo(SERVER_URL + "issue/" + ResultString, ResultString, NEW_ISSUE);

    popupResultInfo(reportInfo, project);

    return reportInfo;
  }

  @Nullable
  public String submit(@Nullable String desc,
                       @NotNull String body,
                       @Nullable String affectedVersion,
                       @NotNull List<Attachment> attachments) {
    if (isEmpty(desc)) {
      throw new RuntimeException(DiagnosticBundle.message("error.report.failure.message"));
    }

    CloseableHttpClient httpClient = HttpClients.createDefault();
    HttpPost loginPost = new HttpPost(LOGIN_URL);
    MultipartEntityBuilder builder = MultipartEntityBuilder.create()
      .addTextBody("login", "autoreporter")
      .addTextBody("password", "fdnjhtgjhn");
    loginPost.setEntity(builder.build());
    CloseableHttpResponse response;
    try {
      response = httpClient.execute(loginPost);
    }
    catch (IOException ex) {
      LOGGER.warn("Error logging on: " + ex.getMessage());
      return null;
    }
      LOGGER.info(response.toString());

    // posting an issue

    ContentType encoding = ContentType.create("text/plain", Consts.UTF_8);
    builder = MultipartEntityBuilder.create()
      .addTextBody("project", PROJECT)
      .addTextBody("assignee", "Unassigned")
      .addTextBody("summary", desc.replaceAll("[\r\n]", ""), encoding)
      .addTextBody("description", body, encoding)
      .addTextBody("priority", "4")
      .addTextBody("type", "Exception")
    ;

    if (StringUtil.isNotEmpty(affectedVersion)) {
      builder.addTextBody("affectsVersion", affectedVersion);
    }

    for (Attachment it : attachments) {
      builder.addBinaryBody("attachments[]", it.getBytes(), encoding, it.getName());
    }

    HttpPost issuePost = new HttpPost(SERVER_ISSUE_URL);
    issuePost.setEntity(builder.build());

    try {
      response = httpClient.execute(issuePost);
    }
    catch (IOException ex) {
      LOGGER.warn("Error posting an issue: " + ex.getMessage());
      return null;
    }

    return response.toString();
  }

  private static void popupResultInfo(final SubmittedReportInfo reportInfo, final Project project) {
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
      ReportMessages.GROUP.createNotification(ReportMessages.ERROR_REPORT, text.toString(), type, listener).notify(project);
    });
  }
}