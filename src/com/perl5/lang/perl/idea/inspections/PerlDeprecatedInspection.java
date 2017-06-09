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

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.utils.PerlResolveUtil;
import org.jetbrains.annotations.NotNull;

/**
 * fixme butify this
 */
public class PerlDeprecatedInspection extends PerlInspection {
  @NotNull
  @Override
  public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
    return new PerlVisitor() {
      @Override
      public void visitSubNameElement(@NotNull PerlSubNameElement o) {
        PsiElement container = o.getParent();

        if (container instanceof PerlSubElement && ((PerlSubElement)container).isDeprecated()) {
          markDeprecated(holder, o, PerlBundle.message("perl.deprecated.sub"));
        }
        else {
          PerlResolveUtil.processElementReferencesResolveResults(targetPair ->
                                                                 {
                                                                   if (targetPair.first instanceof PerlDeprecatable &&
                                                                       ((PerlDeprecatable)targetPair.first).isDeprecated()) {
                                                                     markDeprecated(holder, o, PerlBundle.message("perl.deprecated.sub"));
                                                                     return false;
                                                                   }
                                                                   return true;
                                                                 }, o);
        }
      }

      @Override
      public void visitVariableNameElement(@NotNull PerlVariableNameElement o) {
        PsiElement parent = o.getParent();
        if (parent instanceof PerlVariable) {
          if (((PerlVariable)parent).isDeclaration()) {
            if (((PerlVariable)parent).isDeprecated()) {
              markDeprecated(holder, o, PerlBundle.message("perl.deprecated.variable"));
            }
          }

          PerlResolveUtil.processElementReferencesResolveResults(targetPair ->
                                                                 {
                                                                   String message = null;
                                                                   if (targetPair.first instanceof PerlNamespaceDefinitionElement &&
                                                                       ((PerlNamespaceDefinitionElement)targetPair.first).isDeprecated()) {
                                                                     message = PerlBundle.message("perl.deprecated.namespace");
                                                                   }
                                                                   else if (targetPair.first instanceof PerlVariableDeclarationElement &&
                                                                            ((PerlVariableDeclarationElement)targetPair.first)
                                                                              .isDeprecated()) {
                                                                     message = PerlBundle.message("perl.deprecated.variable");
                                                                   }
                                                                   if (message != null) {
                                                                     holder.registerProblem(targetPair.second, message,
                                                                                            ProblemHighlightType.LIKE_DEPRECATED);
                                                                   }
                                                                   return true;
                                                                 }, o);
        }
      }

      @Override
      public void visitNamespaceElement(@NotNull PerlNamespaceElement o) {
        if (o.isDeprecated()) {
          markDeprecated(holder, o, PerlBundle.message("perl.deprecated.namespace"));
        }
      }

      @Override
      public void visitConstantDefinition(@NotNull PsiPerlConstantDefinition o) {
        if (o.isDeprecated()) {
          PsiElement nameIdentifier = o.getNameIdentifier();
          if (nameIdentifier != null) {
            markDeprecated(holder, nameIdentifier, PerlBundle.message("perl.deprecated.sub"));
          }
        }
      }
    };
  }
}
