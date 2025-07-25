/*
 * Copyright 2015-2025 Alexandr Evstigneev
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
import com.perl5.PerlBundle;
import com.perl5.lang.perl.psi.PerlNamespaceDefinitionWithIdentifier;
import com.perl5.lang.perl.psi.PerlVisitor;
import com.perl5.lang.perl.util.PerlPackageUtilCore;
import org.jetbrains.annotations.NotNull;


public class PerlClashedNamespacesInspection extends PerlInspection {
  @Override
  public @NotNull PsiElementVisitor buildVisitor(final @NotNull ProblemsHolder holder, boolean isOnTheFly) {
    return new PerlVisitor() {

      @Override
      public void visitPerlNamespaceDefinitionWithIdentifier(@NotNull PerlNamespaceDefinitionWithIdentifier o) {
        PsiElement nameIdentifier = o.getNameIdentifier();
        if (nameIdentifier == null) {
          return;
        }


        String packageName = o.getNamespaceName();
        if (PerlPackageUtilCore.MAIN_NAMESPACE_NAME.equals(packageName)) {
          return;
        }

        // fixme we should check that this is not in SDK
        if (PerlPackageUtilCore.isBuiltIn(packageName)) {
          registerProblem(holder, nameIdentifier,
                          PerlBundle.message("inspection.message.namespace.definition.clashes.with.built.in.namespace"));
        }
      }

      @Override
      protected boolean shouldVisitLightElements() {
        return true;
      }
    };
  }
}
