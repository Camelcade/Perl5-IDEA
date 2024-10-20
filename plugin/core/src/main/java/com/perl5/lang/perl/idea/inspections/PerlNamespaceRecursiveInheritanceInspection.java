/*
 * Copyright 2015-2024 Alexandr Evstigneev
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
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.psi.PerlNamespaceDefinitionElement;
import com.perl5.lang.perl.psi.PerlNamespaceDefinitionWithIdentifier;
import com.perl5.lang.perl.psi.PerlVisitor;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;


public class PerlNamespaceRecursiveInheritanceInspection extends PerlInspection {
  @Override
  public @NotNull PsiElementVisitor buildVisitor(final @NotNull ProblemsHolder holder, boolean isOnTheFly) {
    return new PerlVisitor() {

      @Override
      protected boolean shouldVisitLightElements() {
        return true;
      }

      @Override
      public void visitPerlNamespaceDefinitionWithIdentifier(@NotNull PerlNamespaceDefinitionWithIdentifier o) {
        PsiElement nameIdentifier = o.getNameIdentifier();
        if (nameIdentifier == null) {
          return;
        }

        String packageName = o.getNamespaceName();

        if (StringUtil.isEmpty(packageName) || PerlPackageUtil.MAIN_NAMESPACE_NAME.equals(packageName)) {
          return;
        }

        if (hasRecursiveInheritance(o, new HashSet<>())) {
          registerError(holder, o.getContainingFile(),
                        PerlBundle.message("inspection.message.namespace.has.recursive.inheritance", packageName));
          registerError(holder, nameIdentifier, PerlBundle.message("inspection.message.namespace.has.recursive.inheritance", packageName));
        }
      }
    };
  }

  private static boolean hasRecursiveInheritance(@NotNull PerlNamespaceDefinitionElement definition,
                                                 @NotNull Set<? super String> passedWay) {
    passedWay.add(definition.getNamespaceName());
    for (PerlNamespaceDefinitionElement element : definition.getParentNamespaceDefinitions()) {
      if (passedWay.contains(element.getNamespaceName())) {
        return true;
      }
      if (hasRecursiveInheritance(element, new HashSet<>(passedWay))) {
        return true;
      }
    }
    return false;
  }
}
