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

package com.perl5.lang.perl.idea.actions;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.process.ProcessOutput;
import com.intellij.ide.actions.OpenFileAction;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.testFramework.LightVirtualFile;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.idea.configuration.settings.PerlSharedSettings;
import com.perl5.lang.perl.idea.execution.PerlCommandLine;
import com.perl5.lang.perl.idea.sdk.host.PerlHostData;
import com.perl5.lang.perl.util.PerlActionUtil;
import com.perl5.lang.perl.util.PerlRunUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;


public class PerlDeparseFileAction extends PurePerlActionBase {
  private static final String PERL_DEPARSE_GROUP = PerlBundle.message("perl.action.deparse.notification.group");

  public PerlDeparseFileAction() {
    super(PerlBundle.message("perl.action.deparse.file"));
  }

  @Override
  protected boolean isEnabled(@NotNull AnActionEvent event) {
    boolean isEnabled = super.isEnabled(event);
    event.getPresentation().setText(
      isEnabled ?
      PerlBundle.message(
        "perl.action.deparse.file.specific",
        Objects.requireNonNull(PerlActionUtil.getPsiFileFromEvent(event)).getName()) :
      PerlBundle.message("perl.action.deparse.file")
    );
    return isEnabled;
  }

  @Override
  public void actionPerformed(@NotNull AnActionEvent event) {
    PsiFile file = PerlActionUtil.getPsiFileFromEvent(event);

    if (file == null) {
      return;
    }

    final Document document = file.getViewProvider().getDocument();
    if (document == null) {
      return;
    }

    final Project project = file.getProject();

    String deparseArgument = "-MO=Deparse";
    PerlSharedSettings perl5Settings = PerlSharedSettings.getInstance(project);
    if (StringUtil.isNotEmpty(perl5Settings.PERL_DEPARSE_ARGUMENTS)) {
      deparseArgument += "," + perl5Settings.PERL_DEPARSE_ARGUMENTS;
    }

    PerlCommandLine commandLine = PerlRunUtil.getPerlCommandLine(project, file.getVirtualFile(), deparseArgument);

    if (commandLine == null) {
      return;
    }
    commandLine.withWorkDirectory(project.getBasePath());
    FileDocumentManager.getInstance().saveDocument(document);
    new Task.Backgroundable(file.getProject(), PerlBundle.message("perl.action.deparsing.progress", file.getName()), true) {
      @Override
      public void run(@NotNull ProgressIndicator indicator) {
        indicator.setIndeterminate(true);
        try {
          ProcessOutput processOutput = PerlHostData.execAndGetOutput(commandLine);
          String deparsed = processOutput.getStdout();
          String error = processOutput.getStderr();

          if (StringUtil.isNotEmpty(error) && !StringUtil.contains(error, "syntax OK")) {
            if (StringUtil.isEmpty(deparsed)) {
              Notifications.Bus.notify(new Notification(
                PERL_DEPARSE_GROUP,
                PerlBundle.message("perl.action.error.notification.title"),
                error.replaceAll("\\n", "<br/>"),
                NotificationType.ERROR
              ));
            }
            else {
              Notifications.Bus.notify(new Notification(
                PERL_DEPARSE_GROUP,
                PerlBundle.message("perl.action.success.notification.title"),
                PerlBundle.message("perl.action.success.notification.message", error.replaceAll("\\n", "<br/>")),
                NotificationType.INFORMATION
              ));
            }
          }

          if (StringUtil.isNotEmpty(deparsed)) {
            ApplicationManager.getApplication().invokeLater(() -> WriteAction.run(() -> {
              VirtualFile newFile = new LightVirtualFile("Deparsed " + file.getName(), file.getFileType(), deparsed);
              OpenFileAction.openFile(newFile, project);
            }));
          }
        }
        catch (ExecutionException e) {
          Notifications.Bus.notify(new Notification(
            PERL_DEPARSE_GROUP,
            PerlBundle.message("perl.execution.error.notification.title"),
            e.getMessage().replaceAll("\\n", "<br/>"),
            NotificationType.ERROR
          ));
        }
      }
    }.queue();
  }
}
