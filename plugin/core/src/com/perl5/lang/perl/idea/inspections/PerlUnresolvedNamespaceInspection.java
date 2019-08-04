/*
 * Copyright 2015-2019 Alexandr Evstigneev
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
import com.perl5.lang.perl.psi.PerlNamespaceDefinitionWithIdentifier;
import com.perl5.lang.perl.psi.PerlNamespaceElement;
import com.perl5.lang.perl.psi.PerlVisitor;
import com.perl5.lang.perl.psi.PsiPerlRequireExpr;
import com.perl5.lang.perl.psi.impl.PerlUseStatementElementBase;
import org.jetbrains.annotations.NotNull;

/**
 * Check that namespace is defined
 */
public class PerlUnresolvedNamespaceInspection extends PerlInspection {
  @NotNull
  @Override
  public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
    return new PerlVisitor() {
      @Override
      public void visitNamespaceElement(@NotNull PerlNamespaceElement o) {
        PsiElement parent = o.getParent();

        if (parent instanceof PsiPerlRequireExpr ||
            parent instanceof PerlUseStatementElementBase ||
            parent instanceof PerlNamespaceDefinitionWithIdentifier) {
          return;
        }

        // fixme should depend on parent resolving
        if (o.isSUPER()) {
          return;
        }

        if (o.isBuiltin()) {
          return;
        }

        if (o.getNamespaceDefinitions().isEmpty()) {
          registerProblem(holder, o, "Unable to find namespace definition");
        }
      }
    };
  }
}
