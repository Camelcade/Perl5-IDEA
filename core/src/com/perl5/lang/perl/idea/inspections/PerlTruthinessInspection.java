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

package com.perl5.lang.perl.idea.inspections;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Pair;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.impl.PerlFileImpl;
import com.perl5.lang.perl.psi.utils.PerlElementFactory;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.*;

public class PerlTruthinessInspection extends PerlInspection {
  private static final TokenSet AMBIGUOUS_CONDITIONS = TokenSet.create(SCALAR_VARIABLE, SCALAR_CAST_EXPR);

  private static final TokenSet EXCLUDING_CONTAINERS = TokenSet.create(
    FOREACH_COMPOUND, FOR_STATEMENT_MODIFIER, GIVEN_COMPOUND, WHEN_COMPOUND
  );

  @NotNull
  @Override
  public PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
    return new PerlVisitor() {
      @Override
      public void visitConditionExpr(@NotNull PsiPerlConditionExpr o) {
        IElementType containerType = PsiUtilCore.getElementType(o.getParent());
        if (EXCLUDING_CONTAINERS.contains(containerType)) {
          return;
        }

        processExpression(o.getExpr());
      }

      @Override
      public void visitForCondition(@NotNull PsiPerlForCondition o) {
        processExpression(o.getExpr());
      }

      @Override
      public void visitStatementModifier(@NotNull PsiPerlStatementModifier o) {
        IElementType elementType = PsiUtilCore.getElementType(o);
        if (EXCLUDING_CONTAINERS.contains(elementType)) {
          return;
        }
        processExpression(o.getExpr());
      }

      private void processExpression(@Nullable PsiElement expression) {
        PsiElement ambiguousExpression = getAmbiguousExpression(expression);
        if (ambiguousExpression != null) {
          registerProblem(holder, expression, PerlBundle.message("perl.inspection.truthness.warning"),
                          DefinedQuickFix.INSTANCE, ZeroEqualityFix.INSTANCE, EmptyStringEqualityFix.INSTANCE);
        }
      }
    };
  }

  /**
   * @return ambiguous expression inside the {@code expression} if any
   */
  @Contract("null->null")
  @Nullable
  private static PsiElement getAmbiguousExpression(@Nullable PsiElement expression) {
    if (expression == null) {
      return null;
    }
    if (isNegation(expression)) {
      PsiElement[] children = expression.getChildren();
      if (children.length == 1 && !isNegation(children[0])) {
        expression = children[0];
      }
    }
    if (expression instanceof PsiPerlParenthesisedExpr) {
      return getAmbiguousExpression(((PsiPerlParenthesisedExpr)expression).getExpr());
    }
    return AMBIGUOUS_CONDITIONS.contains(PsiUtilCore.getElementType(expression)) ? expression : null;
  }

  /**
   * @return true iff {@code expression} is a negation expression
   */
  private static boolean isNegation(@NotNull PsiElement expression) {
    return expression instanceof PsiPerlLpNotExpr ||
           expression instanceof PsiPerlPrefixUnaryExpr && PsiUtilCore.getElementType(expression.getFirstChild()) == OPERATOR_NOT;
  }

  /**
   * @return a wrapping negation expression if any. Skips parenthesized expressions
   */
  @Contract("null->null")
  @Nullable
  private static PsiElement getParentNegation(@Nullable PsiElement expression) {
    if (expression == null) {
      return null;
    }
    PsiElement parent = expression.getParent();
    if (isNegation(parent)) {
      return parent;
    }
    if (parent instanceof PsiPerlParenthesisedExpr) {
      return getParentNegation(parent);
    }
    return null;
  }

  private abstract static class ReplacementQuickFix implements LocalQuickFix {

    @Override
    public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
      PsiElement psiElement = descriptor.getPsiElement();
      PsiElement ambiguousExpression = getAmbiguousExpression(psiElement);
      if (ambiguousExpression == null) {
        return;
      }

      Pair<PsiElement, PsiElement> replacement = getReplacementAndAnchor(getAmbiguousExpression(ambiguousExpression));
      if (replacement == null || replacement.first == null || replacement.second == null) {
        return;
      }
      replacement.first.replace(replacement.second);
    }

    /**
     * @return pair of element to replace and element to replace with.
     */
    @Contract("null->null")
    @Nullable
    protected abstract Pair<PsiElement, PsiElement> getReplacementAndAnchor(@Nullable PsiElement ambiguousExpression);
  }

  private static class DefinedQuickFix extends ReplacementQuickFix {
    private static final DefinedQuickFix INSTANCE = new DefinedQuickFix();

    @Nls(capitalization = Nls.Capitalization.Sentence)
    @NotNull
    @Override
    public String getFamilyName() {
      return PerlBundle.message("perl.inspection.truthness.add.defined");
    }

    @Override
    protected Pair<PsiElement, PsiElement> getReplacementAndAnchor(@Nullable PsiElement ambiguousExpression) {
      if (ambiguousExpression == null) {
        return null;
      }
      PerlFileImpl file = PerlElementFactory.createFile(ambiguousExpression.getProject(), "defined " + ambiguousExpression.getText());
      return Pair.create(ambiguousExpression, PsiTreeUtil.findChildOfType(file, PsiPerlExpr.class));
    }
  }

  private abstract static class ReplacementWithNegationQuickFix extends ReplacementQuickFix {
    @Override
    protected Pair<PsiElement, PsiElement> getReplacementAndAnchor(@Nullable final PsiElement ambiguousExpression) {
      if (ambiguousExpression == null) {
        return null;
      }
      PsiElement anchor = getParentNegation(ambiguousExpression);
      boolean isNegated = anchor != null;
      if (anchor == null) {
        anchor = ambiguousExpression;
      }

      PerlFileImpl file =
        PerlElementFactory.createFile(ambiguousExpression.getProject(), getReplacementText(ambiguousExpression.getText(), isNegated));
      return Pair.create(anchor, PsiTreeUtil.findChildOfType(file, PsiPerlExpr.class));
    }

    @NotNull
    protected abstract String getReplacementText(@NotNull String baseText, boolean isNegated);
  }

  private static class ZeroEqualityFix extends ReplacementWithNegationQuickFix {
    private static final ZeroEqualityFix INSTANCE = new ZeroEqualityFix();

    @Nls(capitalization = Nls.Capitalization.Sentence)
    @NotNull
    @Override
    public String getFamilyName() {
      return PerlBundle.message("perl.inspection.truthness.add.numeric");
    }

    @NotNull
    @Override
    protected String getReplacementText(@NotNull String baseText, boolean isNegated) {
      return isNegated ? baseText + " != 0" : baseText + " == 0";
    }
  }

  private static final class EmptyStringEqualityFix extends ReplacementWithNegationQuickFix {
    private static final EmptyStringEqualityFix INSTANCE = new EmptyStringEqualityFix();

    @Nls(capitalization = Nls.Capitalization.Sentence)
    @NotNull
    @Override
    public String getFamilyName() {
      return PerlBundle.message("perl.inspection.truthness.add.string");
    }

    @NotNull
    @Override
    protected String getReplacementText(@NotNull String baseText, boolean isNegated) {
      return isNegated ? baseText + " ne ''" : baseText + " eq ''";
    }
  }
}
