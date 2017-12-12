/*
 * Copyright 2015-2017 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.actions;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.CapturingProcessHandler;
import com.intellij.execution.process.ProcessOutput;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationListener;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.idea.configuration.settings.PerlLocalSettings;
import com.perl5.lang.perl.idea.configuration.settings.PerlSharedSettings;
import com.perl5.lang.perl.idea.configuration.settings.sdk.Perl5SettingsConfigurable;
import com.perl5.lang.perl.util.PerlActionUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.event.HyperlinkEvent;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class PerlFormatWithPerlTidyAction extends PurePerlActionBase {
  public static final String PERL_TIDY_LINUX_NAME = "perltidy";
  public static final String PERL_TIDY_WINDOWS_NAME = PERL_TIDY_LINUX_NAME + ".bat";
  public static final String PERL_TIDY_OS_DEPENDENT_NAME = SystemInfo.isWindows ? PERL_TIDY_WINDOWS_NAME : PERL_TIDY_LINUX_NAME;

  public static final String PERL_TIDY_GROUP = "PERL5_PERL_TIDY";

  @Override
  protected boolean isEnabled(AnActionEvent event) {
    if (!super.isEnabled(event)) {
      return false;
    }
    final PsiFile file = PerlActionUtil.getPsiFileFromEvent(event);
    //noinspection ConstantConditions
    return super.isMyFile(file) && file.isWritable();
  }

  protected GeneralCommandLine getPerlTidyCommandLine(Project project) throws ExecutionException {
    PerlSharedSettings sharedSettings = PerlSharedSettings.getInstance(project);
    PerlLocalSettings localSettings = PerlLocalSettings.getInstance(project);
    String executable = localSettings.PERL_TIDY_PATH;
    if (StringUtil.isEmpty(executable)) {
      throw new ExecutionException("Path to PerlTidy executable must be configured in perl settings");
    }
    GeneralCommandLine commandLine = new GeneralCommandLine(executable, "-st", "-se").withWorkDirectory(project.getBasePath());

    if (StringUtil.isNotEmpty(sharedSettings.PERL_TIDY_ARGS)) {
      commandLine.addParameters(StringUtil.split(sharedSettings.PERL_TIDY_ARGS, " "));
    }

    return commandLine;
  }

  @Override
  public void actionPerformed(AnActionEvent event) {
    if (isEnabled(event)) {
      final PsiFile file = PerlActionUtil.getPsiFileFromEvent(event);

      if (file == null) {
        return;
      }

      final Project project = file.getProject();

      final Document document = file.getViewProvider().getDocument();
      if (document == null) {
        return;
      }

      final VirtualFile virtualFile = file.getVirtualFile();

      if (virtualFile == null) {
        return;
      }

      FileDocumentManager.getInstance().saveDocument(document);

      new Task.Backgroundable(project, PerlBundle.message("perl.tidy.formatting"), false) {
        @Override
        public void run(@NotNull ProgressIndicator indicator) {
          try {
            GeneralCommandLine perlTidyCommandLine = getPerlTidyCommandLine(project);
            final Process process = perlTidyCommandLine.createProcess();
            final OutputStream outputStream = process.getOutputStream();
            ApplicationManager.getApplication().executeOnPooledThread(() -> {
              try {
                final byte[] sourceBytes = virtualFile.contentsToByteArray();
                outputStream.write(sourceBytes);
                outputStream.close();
              }
              catch (IOException e) {
                Notifications.Bus.notify(new Notification(
                  PERL_TIDY_GROUP,
                  "Re-formatting error",
                  e.getMessage(),
                  NotificationType.ERROR
                ));
              }
            });

            final CapturingProcessHandler processHandler = new CapturingProcessHandler(process,
                                                                                       virtualFile.getCharset(),
                                                                                       perlTidyCommandLine.getCommandLineString());
            ProcessOutput processOutput = processHandler.runProcess();

            final List<String> stdoutLines = processOutput.getStdoutLines(false);
            List<String> stderrLines = processOutput.getStderrLines();

            if (stderrLines.isEmpty()) {
              WriteCommandAction.runWriteCommandAction(project, () -> {
                document.setText(StringUtil.join(stdoutLines, "\n"));
                PsiDocumentManager.getInstance(project).commitDocument(document);
              });
            }
            else {
              Notifications.Bus.notify(new Notification(
                PERL_TIDY_GROUP,
                "Perl::Tidy formatting error",
                StringUtil.join(stderrLines, "<br>"),
                NotificationType.ERROR
              ));
            }
          }
          catch (ExecutionException e) {
            Notifications.Bus.notify(new Notification(
              PERL_TIDY_GROUP,
              "Error running Perl::Tidy",
              "Try to specify path to perltidy manually in <a href=\"configure\">Perl5 settings</a>.<br/>" + e.getMessage(),
              NotificationType.ERROR,
              new NotificationListener.Adapter() {
                @Override
                protected void hyperlinkActivated(@NotNull Notification notification, @NotNull HyperlinkEvent e) {
                  Perl5SettingsConfigurable.open(file);
                  notification.expire();
                }
              }
            ));
          }
        }
      }.queue();
    }
  }
}
