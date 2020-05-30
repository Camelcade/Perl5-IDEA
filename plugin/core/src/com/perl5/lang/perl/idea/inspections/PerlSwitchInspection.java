/*
 * Copyright 2015-2020 Alexandr Evstigneev
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

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiReference;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.lexer.PerlSyntax;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.references.PerlImplicitDeclarationsService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.PropertyKey;

import java.util.Objects;

import static com.perl5.PerlBundle.PATH_TO_BUNDLE;
import static com.perl5.lang.perl.idea.inspections.PerlLoopControlInspection.MAP_GREP;
import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.*;

public class PerlSwitchInspection extends PerlInspection {
  @Override
  public @NotNull
  PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
    PerlSubDefinitionElement breakDefinition = Objects.requireNonNull(
      PerlImplicitDeclarationsService.getInstance(holder.getProject()).getCoreSub(PerlSyntax.BREAK_KEYWORD));

    return new PerlVisitor() {
      private void problem(@NotNull PsiElement anchor,
                           @NotNull @PropertyKey(resourceBundle = PATH_TO_BUNDLE) String key,
                           @NotNull String... args) {
        registerProblem(holder, anchor, PerlBundle.message(key, (Object[])args));
      }

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

      @Override
      public void visitWhenCompound(@NotNull PsiPerlWhenCompound o) {
        if (PerlSwitchTopicalizer.wrapping(o) == null) {
          problem(o, "perl.inspection.switch.when.outside");
        }
        super.visitWhenCompound(o);
      }

      @Override
      public void visitWhenStatementModifier(@NotNull PsiPerlWhenStatementModifier o) {
        if (PerlSwitchTopicalizer.wrapping(o) == null) {
          problem(o, "perl.inspection.switch.when.modifier.outside");
        }
        super.visitWhenStatementModifier(o);
      }

      @Override
      public void visitDefaultCompound(@NotNull PsiPerlDefaultCompound o) {
        if (PerlSwitchTopicalizer.wrapping(o) == null) {
          problem(o, "perl.inspection.switch.default.outside");
        }
        super.visitDefaultCompound(o);
      }
    };
  }
}
