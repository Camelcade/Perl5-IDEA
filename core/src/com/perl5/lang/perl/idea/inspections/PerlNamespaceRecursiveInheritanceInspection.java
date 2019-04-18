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

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.perl5.lang.perl.psi.PerlNamespaceDefinitionElement;
import com.perl5.lang.perl.psi.PerlNamespaceDefinitionWithIdentifier;
import com.perl5.lang.perl.psi.PerlVisitor;
import com.perl5.lang.perl.util.PerlPackageUtil;
import gnu.trove.THashSet;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * Created by hurricup on 16.08.2015.
 */
public class PerlNamespaceRecursiveInheritanceInspection extends PerlInspection {
  @NotNull
  @Override
  public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
    return new PerlVisitor() {

      @Override
      public void visitPerlNamespaceDefinitionWithIdentifier(@NotNull PerlNamespaceDefinitionWithIdentifier o) {
        PsiElement nameIdentifier = o.getNameIdentifier();
        if (nameIdentifier == null) {
          return;
        }

        String packageName = o.getNamespaceName();

        if (PerlPackageUtil.MAIN_NAMESPACE_NAME.equals(packageName)) {
          return;
        }

        if (hasRecursiveInheritance(o, new THashSet<>())) {
          registerError(holder, o.getContainingFile(), "Namespace " + packageName + " has recursive inheritance");
          registerError(holder, nameIdentifier, "Namespace " + packageName + " has recursive inheritance");
        }
      }
    };
  }

  private static boolean hasRecursiveInheritance(@NotNull PerlNamespaceDefinitionElement definition,
                                                 @NotNull Set<String> passedWay) {
    passedWay.add(definition.getNamespaceName());
    for (PerlNamespaceDefinitionElement element : definition.getParentNamespaceDefinitions()) {
      if (passedWay.contains(element.getNamespaceName())) {
        return true;
      }
      if (hasRecursiveInheritance(element, new THashSet<>(passedWay))) {
        return true;
      }
    }
    return false;
  }
}
