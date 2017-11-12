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
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.refactoring.RefactoringActionHandler;
import com.intellij.refactoring.extractMethod.AbstractExtractMethodDialog;
import com.intellij.refactoring.extractMethod.ExtractMethodDecorator;
import com.intellij.refactoring.extractMethod.ExtractMethodValidator;
import com.intellij.refactoring.util.AbstractVariableData;
import com.intellij.util.containers.ArrayListSet;
import com.perl5.lang.perl.fileTypes.PerlFileTypeScript;
import com.perl5.lang.perl.idea.refactoring.PerlRefactorUtils;
import com.perl5.lang.perl.idea.refactoring.PerlRefactor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Set;

public class PerlExtractMethodHandler implements RefactoringActionHandler {

  @Override
  public void invoke(@NotNull Project project, Editor editor, PsiFile file, DataContext dataContext) {

    final SelectionModel selectionModel = editor.getSelectionModel();
    if (!selectionModel.hasSelection()) selectionModel.selectLineAtCaret();

    final int selectionStartLine = selectionModel.getSelectionStart();
    final int selectionLength = selectionModel.getSelectionEnd() - selectionStartLine;
    String selectedText = selectionModel.getSelectedText();

    if (selectedText == null || selectedText.isEmpty()) {
      return;
    }

    PerlRefactor perlRefactor = new PerlRefactor(selectedText, "myNewMethod");

    Set<String> parameterVariables = new ArrayListSet<>();
    parameterVariables.addAll(perlRefactor.getParameters());

    Set<String> returnVariables = new ArrayListSet<>();
    returnVariables.addAll(perlRefactor.getInnerVariables());

    // TODO: create PerlCodeFragment that contains my(...) = foo(bar);
    // TODO: figure how to use returnInside
    CodeFragment fragment = new CodeFragment(parameterVariables, returnVariables,false);

    // This populates the parameters and signature
    final ExtractMethodDecorator decorator = (methodName, variableData) -> {

      StringBuilder builder = new StringBuilder();
      boolean first = true;

      if (perlRefactor.getOuterVariablesModified().size() > 0) {
        builder.append("my (");
        for (String ov : perlRefactor.getOuterVariablesModified()) {
          if (first) {
            first = false;
          }
          else {
            builder.append(", ");
          }
          builder.append(ov);
        }
        builder.append(") = ");
        first = true;
      }

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
    final ExtractMethodValidator validator = new ExtractMethodValidator() {
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

    final AbstractExtractMethodDialog dialog = new AbstractExtractMethodDialog(project, "myNewMethod",
                                                                               fragment, validator, decorator,
                                                                               PerlFileTypeScript.INSTANCE);
    dialog.show();

    if (dialog.isOK()) {
      String methodName = dialog.getMethodName();
      AbstractVariableData[] variableData = dialog.getAbstractVariableData();
      final PerlRefactorUtils refactoring = new PerlRefactorUtils(file, selectedText, methodName, selectionStartLine, selectionLength);

      String methodSignature = decorator.createMethodSignature(methodName, variableData);

      String method = perlRefactor.buildMethod(methodName, variableData, perlRefactor.getCodeSelection(), perlRefactor.buildMethodReturn());
      refactoring.writeMethod(methodSignature, method);
    }
  }

  @Override
  public void invoke(@NotNull Project project, @NotNull PsiElement[] elements, DataContext dataContext) {
    // Does Nothing
  }
}
