/*
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

package com.perl5.lang.perl.idea.refactoring;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.perl5.PerlBundle;
import org.jetbrains.annotations.NotNull;

public class PerlRefactorUtils {
  PsiFile file;
  String selectedCode;
  String methodName;
  int selectionStartLine;
  int selectionLength;
  Project project;
  Document document;
  VirtualFile virtualFile;

  public static final String PERL_REFACTOR_GROUP = "PERL5_PERL_REFACTOR";

  public PerlRefactorUtils(PsiFile file, String selectedCode, String methodName, int selectionStartLine, int selectionLength) {
    this.file = file;
    this.project = file.getProject();
    this.virtualFile = file.getVirtualFile();
    this.document = file.getViewProvider().getDocument();
    this.selectedCode = selectedCode;
    this.methodName = methodName;
    this.selectionStartLine = selectionStartLine;
    this.selectionLength = selectionLength;
  }

  public void writeMethod(String methodCall, String methodBody) {

    if (this.document == null) return;
    if (this.virtualFile == null) return;

      new Task.Backgroundable(project, PerlBundle.message("perl.refactor.running"), false) {
        @Override
        public void run(@NotNull ProgressIndicator indicator) {
          try {
            // TODO: run methodBody through perl tidy
            // TODO: figure out best place to add new method to document. Look how eclipse or padre does it?
            // TODO: update bundle messages

            // Write method call
            WriteCommandAction.runWriteCommandAction(project, () -> {
              document.replaceString(selectionStartLine, selectionStartLine + selectionLength, StringUtil.join("\n",methodCall, "\n"));
              PsiDocumentManager.getInstance(project).commitDocument(document);
            });

            // Write method body
            WriteCommandAction.runWriteCommandAction(project, () -> {
              document.insertString(
                document.getLineEndOffset(
                  document.getLineCount() - 1),
                StringUtil.join(methodBody, "\n")
              );
              PsiDocumentManager.getInstance(project).commitDocument(document);
            });
          }
          catch (Exception e) {
            Notifications.Bus.notify(new Notification(
              PERL_REFACTOR_GROUP,
              "Error Refactoring",
              e.getMessage(),
              NotificationType.ERROR));
          }
        }
      }.queue();
    }
}
