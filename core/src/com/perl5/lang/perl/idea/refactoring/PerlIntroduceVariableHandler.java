/*
 * Copyright 2015-2018 Alexandr Evstigneev
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

import com.intellij.codeInsight.unwrap.ScopeHighlighter;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Pass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.refactoring.HelpID;
import com.intellij.refactoring.IntroduceTargetChooser;
import com.intellij.refactoring.RefactoringActionHandler;
import com.intellij.refactoring.RefactoringBundle;
import com.intellij.refactoring.util.CommonRefactoringUtil;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.psi.PsiPerlExpr;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.CONDITION_EXPR;
import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.NESTED_CALL;

class PerlIntroduceVariableHandler implements RefactoringActionHandler {
  private static final Logger LOG = Logger.getInstance(PerlIntroduceVariableHandler.class);
  private static final TokenSet UNINTRODUCIBLE_TOKENS = TokenSet.create(
    CONDITION_EXPR, NESTED_CALL
  );

  @Override
  public void invoke(@NotNull Project project, Editor editor, PsiFile file, DataContext dataContext) {
    List<PsiElement> expressions = new ArrayList<>();
    PsiPerlExpr run = PsiTreeUtil.findElementOfClassAtOffset(file, editor.getCaretModel().getOffset(), PsiPerlExpr.class, false);
    // fixme utilize descriptor for range in repeatance stuff: commas, derefs, additions
    while (run != null) {
      if (!UNINTRODUCIBLE_TOKENS.contains(PsiUtilCore.getElementType(run))) {
        expressions.add(run);
      }
      run = PsiTreeUtil.getParentOfType(run, PsiPerlExpr.class);
    }
    if (expressions.isEmpty()) {
      showErrorMessage(project, editor, PerlBundle.message("perl.introduce.no.target"));
      return;
    }

    if (expressions.size() > 1) {
      IntroduceTargetChooser.showChooser(
        editor, expressions, new Pass<PsiElement>() {
          @Override
          public void pass(PsiElement element) {
            LOG.warn("Introducing " + element);
          }
        },
        PsiElement::getText,
        PerlBundle.message("perl.introduce.expressions"),
        ScopeHighlighter.NATURAL_RANGER);
    }
  }

  protected void showErrorMessage(@NotNull Project project, Editor editor, @NotNull String message) {
    CommonRefactoringUtil
      .showErrorHint(project, editor, message, RefactoringBundle.message("introduce.variable.title"), HelpID.INTRODUCE_VARIABLE);
  }

  @Override
  public void invoke(@NotNull Project project, @NotNull PsiElement[] elements, DataContext dataContext) {

  }
}
