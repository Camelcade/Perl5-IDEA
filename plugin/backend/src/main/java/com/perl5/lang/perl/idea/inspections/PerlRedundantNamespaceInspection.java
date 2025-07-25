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

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElementVisitor;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.psi.PerlNamespaceElement;
import com.perl5.lang.perl.psi.PerlVisitor;
import com.perl5.lang.perl.psi.PsiPerlMethod;
import com.perl5.lang.perl.util.PerlPackageUtilCore;
import org.jetbrains.annotations.NotNull;


public class PerlRedundantNamespaceInspection extends PerlInspection {
  @Override
  public @NotNull PsiElementVisitor buildVisitor(final @NotNull ProblemsHolder holder, boolean isOnTheFly) {
    return new PerlVisitor() {
      @Override
      public void visitMethod(@NotNull PsiPerlMethod o) {
        PerlNamespaceElement namespaceElement = o.getNamespaceElement();
        if (namespaceElement != null) {
          String packageName = namespaceElement.getCanonicalName();
          if (StringUtil.isNotEmpty(packageName)) {
            String contextPackageName = PerlPackageUtilCore.getContextNamespaceName(o);
            if (StringUtil.equals(packageName, contextPackageName)) {
              if (!namespaceElement.getTextRange().isEmpty()) {
                holder.registerProblem(
                  namespaceElement,
                  PerlBundle.message("inspection.message.redundant.namespace.qualifier"),
                  ProblemHighlightType.LIKE_UNUSED_SYMBOL
                );
              }
            }
          }
        }

        super.visitMethod(o);
      }
    };
  }
}
