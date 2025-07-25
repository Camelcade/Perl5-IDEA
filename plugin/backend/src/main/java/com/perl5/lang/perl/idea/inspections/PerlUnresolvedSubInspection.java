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
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiReference;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.psi.PerlMethod;
import com.perl5.lang.perl.psi.PerlNamespaceElement;
import com.perl5.lang.perl.psi.PerlSubNameElement;
import com.perl5.lang.perl.psi.PerlVisitor;
import com.perl5.lang.perl.psi.impl.PerlStringContentElementImpl;
import com.perl5.lang.perl.psi.utils.PerlResolveUtilCore;
import org.jetbrains.annotations.NotNull;

import static com.perl5.lang.perl.idea.PerlElementPatterns.EXPORT_ASSIGNED_STRING_CONTENT;


public class PerlUnresolvedSubInspection extends PerlInspection {
  @Override
  public @NotNull PsiElementVisitor buildVisitor(final @NotNull ProblemsHolder holder, boolean isOnTheFly) {
    return new PerlVisitor() {
      @Override
      public void visitStringContentElement(@NotNull PerlStringContentElementImpl o) {
        if (EXPORT_ASSIGNED_STRING_CONTENT.accepts(o)) {
          if (!PerlResolveUtilCore.isResolvable(o.getReference())) {
            registerProblem(holder, o, PerlBundle.message("perl.inspection.no.exported.entity"));
          }
        }
      }

      @Override
      public void visitPerlMethod(@NotNull PerlMethod o) {
        PerlNamespaceElement namespaceElement = o.getNamespaceElement();
        PerlSubNameElement subNameElement = o.getSubNameElement();

        // fixme adjust built in checking to the file; Remove second condition after implementing annotations
        if (subNameElement == null ||
            namespaceElement != null && namespaceElement.isBuiltin() ||
            subNameElement.isBuiltIn()) {
          return;
        }

        for (PsiReference reference : subNameElement.getReferences()) {
          if (PerlResolveUtilCore.isResolvable(reference)) {
            return;
          }
        }
        registerProblem(holder, subNameElement, PerlBundle.message("perl.inspection.no.sub.definition"));
      }
    };
  }
}
