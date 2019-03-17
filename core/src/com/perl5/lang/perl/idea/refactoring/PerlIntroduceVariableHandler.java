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

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Pass;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.refactoring.HelpID;
import com.intellij.refactoring.IntroduceTargetChooser;
import com.intellij.refactoring.RefactoringActionHandler;
import com.intellij.refactoring.RefactoringBundle;
import com.intellij.refactoring.util.CommonRefactoringUtil;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.PerlParserDefinition;
import com.perl5.lang.perl.psi.PerlQuoted;
import com.perl5.lang.perl.psi.PsiPerlExpr;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.*;

public class PerlIntroduceVariableHandler implements RefactoringActionHandler {
  private static final Logger LOG = Logger.getInstance(PerlIntroduceVariableHandler.class);
  private static final TokenSet UNINTRODUCIBLE_TOKENS = TokenSet.create(
    CONDITION_EXPR, NESTED_CALL
  );
  private static final TokenSet SEQUENTINAL_TOKENS = TokenSet.create(
    COMMA_SEQUENCE_EXPR, DEREF_EXPR,
    ADD_EXPR, MUL_EXPR, SHIFT_EXPR, BITWISE_AND_EXPR, BITWISE_OR_XOR_EXPR, AND_EXPR, OR_EXPR, LP_AND_EXPR, LP_OR_XOR_EXPR
  );

  @Override
  public void invoke(@NotNull Project project, Editor editor, PsiFile file, DataContext dataContext) {
    List<PerlIntroduceTarget> targets = computeIntroduceTargets(editor, file);
    if (targets.isEmpty()) {
      showErrorMessage(project, editor, RefactoringBundle.getCannotRefactorMessage(PerlBundle.message("perl.introduce.no.target")));
      return;
    }

    if (targets.size() > 1) {
      IntroduceTargetChooser.showIntroduceTargetChooser(
        editor,
        targets,
        new Pass<PerlIntroduceTarget>() {
          @Override
          public void pass(PerlIntroduceTarget target) {
            LOG.warn("Introducing " + target.render());
          }
        },
        PerlBundle.message("perl.introduce.expressions"),
        -1);
    }
    else {
      LOG.warn("Introducing " + targets.iterator().next().render());
    }
  }

  /**
   * @return List of possible introduce targets for {@code file} opened in {@code editor}
   */
  @NotNull
  public List<PerlIntroduceTarget> computeIntroduceTargets(Editor editor, PsiFile file) {
    List<PerlIntroduceTarget> targets = new ArrayList<>();
    int caretOffset = editor.getCaretModel().getOffset();
    // fixme choose target by selection
    PsiPerlExpr run = PsiTreeUtil.findElementOfClassAtOffset(file, caretOffset, PsiPerlExpr.class, false);
    while (run != null) {
      IElementType elementType = PsiUtilCore.getElementType(run);
      if (SEQUENTINAL_TOKENS.contains(elementType)) {
        for (PsiElement child : run.getChildren()) {
          TextRange childTextRange = child.getTextRange();
          if (childTextRange.contains(caretOffset) || childTextRange.getStartOffset() > caretOffset) {
            targets.add(PerlIntroduceTarget.create(run, child));
          }
        }
      }
      else if (run instanceof PerlQuoted) {
        PsiElement stringRun = ((PerlQuoted)run).getOpenQuote();
        if (stringRun != null) {
          PsiElement closeQuote = ((PerlQuoted)run).getCloseQuote();
          PsiElement firstStringElement = stringRun.getNextSibling();
          while ((stringRun = stringRun.getNextSibling()) != null && !stringRun.equals(closeQuote)) {
            IElementType stringRunElementType = PsiUtilCore.getElementType(stringRun);
            if (stringRunElementType == TokenType.WHITE_SPACE) {
              continue;
            }
            TextRange stringRunTextRange = stringRun.getTextRange();
            if (stringRunTextRange.contains(caretOffset) || stringRunTextRange.getStartOffset() > caretOffset) {
              if (PerlParserDefinition.LITERALS.contains(stringRunElementType)) {
                String stringRunText = stringRun.getText();
                boolean isLastWhiteSpace = true;
                for (int i = 0; i < stringRunText.length(); i++) {
                  boolean isCurrentWhiteSpace = Character.isWhitespace(stringRunText.charAt(i));
                  int substringEndOffsetInParent = stringRun.getStartOffsetInParent() + i;
                  if (isLastWhiteSpace != isCurrentWhiteSpace && isCurrentWhiteSpace &&
                      substringEndOffsetInParent + stringRunTextRange.getStartOffset() > caretOffset) {
                    targets.add(PerlIntroduceTarget.create(run, firstStringElement.getStartOffsetInParent(),
                                                           substringEndOffsetInParent));
                  }
                  isLastWhiteSpace = isCurrentWhiteSpace;
                }
                if (!isLastWhiteSpace) {
                  targets.add(PerlIntroduceTarget.create(run, firstStringElement.getStartOffsetInParent(),
                                                         stringRun.getStartOffsetInParent() + stringRunText.length()));
                }
              }
              else {
                targets.add(PerlIntroduceTarget.create(run, firstStringElement, stringRun));
              }
            }
          }
        }
        targets.add(PerlIntroduceTarget.create(run));
      }
      else if (!UNINTRODUCIBLE_TOKENS.contains(elementType)) {
        targets.add(PerlIntroduceTarget.create(run));
      }
      run = PsiTreeUtil.getParentOfType(run, PsiPerlExpr.class);
    }
    return targets;
  }

  protected void showErrorMessage(@NotNull Project project, Editor editor, @NotNull String message) {
    CommonRefactoringUtil
      .showErrorHint(project, editor, message, RefactoringBundle.message("introduce.variable.title"), HelpID.INTRODUCE_VARIABLE);
  }

  @Override
  public void invoke(@NotNull Project project, @NotNull PsiElement[] elements, DataContext dataContext) {

  }
}
