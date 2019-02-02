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

package com.perl5.lang.perl.idea.inspections;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiReference;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.impl.PerlFileImpl;
import com.perl5.lang.perl.psi.impl.PsiPerlStatementImpl;
import com.perl5.lang.perl.psi.references.PerlImplicitSubsService;
import com.perl5.lang.perl.psi.utils.PerlElementFactory;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.*;

public class PerlLoopControlInspection extends PerlInspection {
  private static final TokenSet MAP_GREP = TokenSet.create(
    MAP_EXPR, GREP_EXPR
  );

  @NotNull
  @Override
  public PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
    PerlSubDefinitionElement breakDefinition = Objects.requireNonNull(
      PerlImplicitSubsService.getInstance(holder.getProject()).findCoreSub("break"));

    return new PerlVisitor() {
      @Override
      public void visitContinueExpr(@NotNull PsiPerlContinueExpr o) {

        PsiElement position = o;
        boolean isInsideTheLoop = false;
        while (true) {

          PsiElement closestBlockContainer = PerlBlock.getClosestBlockCompoundContainer(position);
          if (closestBlockContainer == null) {
            break;
          }

          IElementType blockContainerElementType = PsiUtilCore.getElementType(closestBlockContainer);
          if (blockContainerElementType == WHEN_COMPOUND || blockContainerElementType == DEFAULT_COMPOUND) {
            return;
          }
          else if (PerlBlock.LOOPS_CONTAINERS.contains(blockContainerElementType)) {
            isInsideTheLoop = true;
          }
          else if (blockContainerElementType == NAMED_BLOCK) {
            break;
          }
          else if (MAP_GREP.contains(blockContainerElementType)) {
            break;
          }
          else if (PerlBlock.BLOCKS_WITH_RETURN_VALUE.contains(blockContainerElementType)) {
            break;
          }
          else if (blockContainerElementType == GIVEN_COMPOUND) {
            break;
          }

          position = closestBlockContainer;
        }

        if (isInsideTheLoop) {
          holder.registerProblem(
            o,
            PerlBundle.message("perl.inspection.loop.control.continue.instead.of.next"),
            new ReplaceWithExpressionQuickFix("next"));
        }
        else {
          problem(o, "perl.inspection.loop.control.continue");
        }
      }

      @Override
      public void visitSubNameElement(@NotNull PerlSubNameElement o) {
        PsiReference reference = o.getReference();
        if (reference == null) {
          return;
        }
        if (reference.resolve() != breakDefinition) {
          return;
        }

        PsiElement methodElement = o.getParent();
        if (!(methodElement instanceof PsiPerlMethod)) {
          return;
        }

        PsiElement callExpr = methodElement.getParent();
        if (!(callExpr instanceof PsiPerlSubCallExpr)) {
          return;
        }

        PsiElement position = o;
        boolean isInsideTheLoop = false;
        while (true) {
          PsiElement closestBlockContainer = PerlBlock.getClosestBlockCompoundContainer(position);
          if (closestBlockContainer == null) {
            break;
          }

          IElementType blockContainerElementType = PsiUtilCore.getElementType(closestBlockContainer);
          if (PerlBlock.LOOPS_CONTAINERS.contains(blockContainerElementType)) {
            isInsideTheLoop = true;
          }
          else if (blockContainerElementType == NAMED_BLOCK) {
            break;
          }
          else if (MAP_GREP.contains(blockContainerElementType)) {
            break;
          }
          else if (PerlBlock.BLOCKS_WITH_RETURN_VALUE.contains(blockContainerElementType)) {
            break;
          }
          else if (blockContainerElementType == GIVEN_COMPOUND) {
            return;
          }

          position = closestBlockContainer;
        }

        if (isInsideTheLoop) {
          holder.registerProblem(
            callExpr,
            PerlBundle.message("perl.inspection.loop.control.break.instead.of.last"),
            new ReplaceWithExpressionQuickFix("last"));
        }
        else {
          problem(o, "perl.inspection.loop.control.break");
        }
      }

      private void problem(@NotNull PsiElement anchor, @NotNull String key, @NotNull String... args) {
        registerProblem(holder, anchor, PerlBundle.message(key, (Object[])args));
      }


      /**
       * Traversing blocks up, trying to figure out if last/next/redo are in right place.
       *
       * @param expr last/next/redo expression
       *
       * @implNote duplicates logic in {@link PerlFlowControlExpr#getTargetScope()}
       */
      @Override
      public void visitPerlFlowControlExpr(PerlFlowControlExpr expr) {
        PsiElement keyword = expr.getFirstChild();
        if (keyword == null) {
          return;
        }
        String keywordText = keyword.getText();

        // checks modifier
        PsiPerlStatementImpl containingStatement = PsiTreeUtil.getParentOfType(expr, PsiPerlStatementImpl.class);
        PsiPerlStatementModifier modifier = containingStatement == null ? null : containingStatement.getModifier();
        if (modifier instanceof PsiPerlForStatementModifier) {
          return;
        }

        // traversing up
        PsiElement position = expr;
        boolean isInsideGiven = false;
        boolean isInsideWhenOrDefault = false;
        while (true) {
          PsiElement closestBlockContainer = PerlBlock.getClosestBlockCompoundContainer(position);

          if (closestBlockContainer == null) {
            break;
          }

          IElementType blockContainerType = PsiUtilCore.getElementType(closestBlockContainer);

          if (PerlBlock.LOOPS_CONTAINERS.contains(blockContainerType)) {
            return;
          }
          else if (blockContainerType == NAMED_BLOCK) {
            problem(expr, "perl.inspection.loop.control.in.named.block", keywordText);
            return;
          }
          else if (MAP_GREP.contains(blockContainerType)) {
            problem(expr, "perl.inspection.loop.control.in.map", keywordText);
            return;
          }
          else if (PerlBlock.BLOCKS_WITH_RETURN_VALUE.contains(blockContainerType)) {
            problem(expr, "perl.inspection.loop.control.in.do", keywordText);
            return;
          }
          else if (blockContainerType == GIVEN_COMPOUND) {
            isInsideGiven = true;
          }
          else if (blockContainerType == WHEN_COMPOUND || blockContainerType == DEFAULT_COMPOUND) {
            isInsideWhenOrDefault = true;
          }
          position = closestBlockContainer;
        }

        if (expr instanceof PsiPerlNextExpr && isInsideWhenOrDefault) {
          holder.registerProblem(
            expr,
            PerlBundle.message("perl.inspection.loop.control.next.instead.of.continue"),
            new ReplaceWithExpressionQuickFix("continue"));
        }
        else if (expr instanceof PsiPerlLastExpr && isInsideGiven) {
          holder.registerProblem(
            expr,
            PerlBundle.message("perl.inspection.loop.control.last.instead.of.break"),
            new ReplaceWithExpressionQuickFix("break"));
        }
        else {
          problem(expr, "perl.inspection.loop.control.outside", keywordText);
        }
      }
    };
  }

  private static class ReplaceWithExpressionQuickFix implements LocalQuickFix {
    @NotNull
    private final String myTargetKeyword;

    public ReplaceWithExpressionQuickFix(@NotNull String targetKeyword) {
      myTargetKeyword = targetKeyword;
    }

    @Nls
    @NotNull
    @Override
    public String getFamilyName() {
      return PerlBundle.message("perl.inspection.loop.control.convert", myTargetKeyword);
    }

    @Override
    public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
      PsiElement targetElement = descriptor.getPsiElement();
      PerlFileImpl file = PerlElementFactory.createFile(targetElement.getProject(), myTargetKeyword);
      PsiElement statement = file.getFirstChild();
      if (statement == null) {
        return;
      }
      PsiElement expression = statement.getFirstChild();
      if (expression != null) {
        targetElement.replace(expression);
      }
    }
  }
}
