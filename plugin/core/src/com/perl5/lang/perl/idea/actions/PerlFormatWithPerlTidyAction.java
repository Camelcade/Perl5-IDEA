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
import com.intellij.execution.process.BaseProcessHandler;
import com.intellij.execution.process.ProcessOutput;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.idea.configuration.settings.PerlSharedSettings;
import com.perl5.lang.perl.idea.execution.PerlCommandLine;
import com.perl5.lang.perl.idea.sdk.host.PerlHostData;
import com.perl5.lang.perl.util.PerlActionUtil;
import com.perl5.lang.perl.util.PerlRunUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Objects;

public class PerlFormatWithPerlTidyAction extends PurePerlActionBase {
  private static final Logger LOG = Logger.getInstance(PerlFormatWithPerlTidyAction.class);
  private static final String PACKAGE_NAME = "Perl::Tidy";
  private static final String SCRIPT_NAME = "perltidy";

  public PerlFormatWithPerlTidyAction() {
    getTemplatePresentation().setText(PerlBundle.message("perl.action.reformat.perl.tidy"));
  }

  @NotNull
  private String getGroup() {
    return PerlBundle.message("perl.action.perl.tidy.notification.group");
  }

  @Override
  protected boolean isEnabled(AnActionEvent event) {
    Presentation presentation = event.getPresentation();
    presentation.setText(PerlBundle.message("perl.action.reformat.perl.tidy"));
    if (!super.isEnabled(event)) {
      return false;
    }
    final PsiFile file = PerlActionUtil.getPsiFileFromEvent(event);
    //noinspection ConstantConditions
    if (!file.isWritable()) {
      return false;
    }
    presentation.setText(PerlBundle.message("perl.action.reformat.perl.tidy.specific", file.getName()));
    return true;
  }

  @Nullable
  private PerlCommandLine getPerlTidyCommandLine(@NotNull Project project) throws ExecutionException {
    PerlSharedSettings sharedSettings = PerlSharedSettings.getInstance(project);
    VirtualFile perlTidyScript =
      ReadAction.compute(() -> PerlRunUtil.findLibraryScriptWithNotification(project, SCRIPT_NAME, PACKAGE_NAME));
    if (perlTidyScript == null) {
      return null;
    }
    PerlCommandLine commandLine = PerlRunUtil.getPerlCommandLine(project, perlTidyScript);
    if (commandLine == null) {
      return null;
    }
    commandLine.withParameters("-st", "-se").withWorkDirectory(project.getBasePath());

    if (StringUtil.isNotEmpty(sharedSettings.PERL_TIDY_ARGS)) {
      commandLine.addParameters(StringUtil.split(sharedSettings.PERL_TIDY_ARGS, " "));
    }

    return commandLine;
  }

  @Override
  public void actionPerformed(@NotNull AnActionEvent event) {
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
          indicator.setIndeterminate(true);
          try {
            PerlCommandLine perlTidyCommandLine = getPerlTidyCommandLine(project);
            if (perlTidyCommandLine == null) {
              return;
            }
            BaseProcessHandler processHandler = PerlHostData.createProcessHandler(
              perlTidyCommandLine.withCharset(virtualFile.getCharset()));

            final OutputStream outputStream = Objects.requireNonNull(processHandler.getProcessInput());

            try {
              final byte[] sourceBytes = virtualFile.contentsToByteArray();
              outputStream.write(sourceBytes);
              outputStream.close();
            }
            catch (IOException e) {
              LOG.error(e);
              Notifications.Bus.notify(new Notification(
                getGroup(),
                PerlBundle.message("perl.action.perl.tidy.formatting.error.title"),
                e.getMessage(),
                NotificationType.ERROR
              ));
              return;
            }

            ProcessOutput processOutput = PerlHostData.getOutput(processHandler);
            final List<String> stdoutLines = processOutput.getStdoutLines(false);
            List<String> stderrLines = processOutput.getStderrLines();

            if (stderrLines.isEmpty()) {
              WriteCommandAction.runWriteCommandAction(project, () -> {
                document.setText(StringUtil.join(stdoutLines, "\n"));
                PsiDocumentManager.getInstance(project).commitDocument(document);
              });
            }
            else {
              LOG.warn("Non-empty stderr: " + processOutput.getStderr());
              Notifications.Bus.notify(new Notification(
                getGroup(),
                PerlBundle.message("perl.action.perl.tidy.formatting.error.title"),
                StringUtil.join(stderrLines, "<br>"),
                NotificationType.ERROR
              ));
            }
          }
          catch (ExecutionException e) {
            LOG.error(e);
            Notifications.Bus.notify(new Notification(
              getGroup(),
              PerlBundle.message("perl.action.perl.tidy.running.error.title"),
              e.getMessage(),
              NotificationType.ERROR
            ));
          }
        }
      }.queue();
    }
  }
}
