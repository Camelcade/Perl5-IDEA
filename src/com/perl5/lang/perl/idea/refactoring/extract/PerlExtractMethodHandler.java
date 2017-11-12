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

package com.perl5.lang.perl.idea.refactoring.extract;

import com.intellij.codeInsight.codeFragment.CodeFragment;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.refactoring.RefactoringActionHandler;
import com.intellij.refactoring.extractMethod.AbstractExtractMethodDialog;
import com.intellij.refactoring.extractMethod.ExtractMethodDecorator;
import com.intellij.refactoring.extractMethod.ExtractMethodValidator;
import com.intellij.refactoring.util.AbstractVariableData;
import com.intellij.util.containers.ArrayListSet;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.fileTypes.PerlFileTypeScript;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Set;

public class PerlExtractMethodHandler implements RefactoringActionHandler {

  @Override
  public void invoke(@NotNull Project project, Editor editor, PsiFile file, DataContext dataContext) {

    SelectionModel selectionModel = editor.getSelectionModel();
    if (!selectionModel.hasSelection()) {
      selectionModel.selectLineAtCaret();
    }

    int selectionStartLine = selectionModel.getSelectionStart();
    int selectionLength = selectionModel.getSelectionEnd() - selectionStartLine;
    String selectedText = selectionModel.getSelectedText();
    if (StringUtil.isEmpty(selectedText)) {
      return;
    }

    PerlExtractMethod perlExtractMethod = new PerlExtractMethod(selectedText);
    Set<String> parameterVariables = ContainerUtil.newHashSet();
    parameterVariables.addAll(perlExtractMethod.getParameters());

    Set<String> returnVariables = new ArrayListSet<>();
    returnVariables.addAll(perlExtractMethod.getInnerVariables());

    // TODO: create PerlCodeFragment that contains my(...) = foo(bar);
    // TODO: figure how to use returnInside
    CodeFragment fragment = new CodeFragment(parameterVariables, returnVariables, false);

    // This populates the parameters and signature
    ExtractMethodDecorator decorator = (methodName, variableData) -> {

      StringBuilder builder = new StringBuilder();
      if (perlExtractMethod.getOuterVariablesModified().size() > 0) {
        builder.append("my (");
        builder.append(StringUtil.join(perlExtractMethod.getOuterVariablesModified(), ","));
        builder.append(") = ");
      }

      boolean first = true;
      builder.append(methodName).append("(");
      for (AbstractVariableData vd : variableData) {
        if (!vd.passAsParameter) continue;
        if (first) {
          first = false;
        }
        else {
          builder.append(", ");
        }
        builder.append(vd.name);
      }
      builder.append(");");

      return builder.toString();
    };

    //This validator should check if name will clash with existing methods
    ExtractMethodValidator validator = new ExtractMethodValidator() {
      // TODO: Check for existing methods
      @Nullable
      @Override
      public String check(String name) {
        return null;
      }
      // TODO: Validate name
      @Override
      public boolean isValidName(String name) {
        return true;
      }
    };

    AbstractExtractMethodDialog dialog = new AbstractExtractMethodDialog(project, "myNewMethod",
                                                                               fragment, validator, decorator,
                                                                               PerlFileTypeScript.INSTANCE);
    dialog.show();

    if (dialog.isOK()) {
      String methodName = dialog.getMethodName();
      AbstractVariableData[] variableData = dialog.getAbstractVariableData();

      String methodSignature = decorator.createMethodSignature(methodName, variableData);

      String method = perlExtractMethod.buildMethod(methodName, variableData, perlExtractMethod.getSelectedCode(), perlExtractMethod.buildMethodReturn());
      this.writeMethod(file, project, selectionStartLine, selectionLength, methodSignature, method);
    }
  }

  @Override
  public void invoke(@NotNull Project project, @NotNull PsiElement[] elements, DataContext dataContext) {
    // Does Nothing
  }

  public void writeMethod(PsiFile file, Project project, int selectionStartLine, int selectionLength, String methodCall, String methodBody) {

    Document document = file.getViewProvider().getDocument();
    if (document == null) return;

    VirtualFile virtualFile = file.getVirtualFile();
    if (virtualFile == null) return;


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
            "PERL5_PERL_REFACTOR",
            "Error Refactoring",
            e.getMessage(),
            NotificationType.ERROR));
        }
      }
    }.queue();
  }
}