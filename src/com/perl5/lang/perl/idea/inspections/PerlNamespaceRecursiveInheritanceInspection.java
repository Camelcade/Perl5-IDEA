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
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.search.GlobalSearchScope;
import com.perl5.lang.perl.psi.PerlNamespaceDefinitionElement;
import com.perl5.lang.perl.psi.PerlNamespaceDefinitionWithIdentifier;
import com.perl5.lang.perl.psi.PerlVisitor;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;

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

        String packageName = o.getPackageName();

        if (PerlPackageUtil.MAIN_PACKAGE.equals(packageName)) {
          return;
        }

        if (hasRecursiveInheritance(o)) {
          registerError(holder, o.getContainingFile(), "Namespace " + packageName + " has recursive inheritance");
          registerError(holder, nameIdentifier, "Namespace " + packageName + " has recursive inheritance");
        }
      }
    };
  }

  public static boolean hasRecursiveInheritance(PerlNamespaceDefinitionElement definition) {
    return hasRecursiveInheritance(definition.getProject(), definition.getPackageName(), new HashSet<String>());
  }

  private static boolean hasRecursiveInheritance(Project project, String packageName, HashSet<String> passedWay) {
    Collection<PerlNamespaceDefinitionElement> definitions =
      PerlPackageUtil.getNamespaceDefinitions(project, packageName, GlobalSearchScope.projectScope(project));
    if (!definitions.isEmpty()) {
      HashSet<PerlNamespaceDefinitionElement> parents = new HashSet<PerlNamespaceDefinitionElement>();
      for (PerlNamespaceDefinitionElement definition : definitions) {
        parents.addAll(definition.getParentNamespaceDefinitions());
      }

      if (!parents.isEmpty()) {
        passedWay.add(packageName);
        for (PerlNamespaceDefinitionElement parent : parents) {
          if (passedWay.contains(parent.getPackageName()) || hasRecursiveInheritance(project, parent.getPackageName(), passedWay)) {
            return true;
          }
        }
      }
    }
    return false;
  }
}
