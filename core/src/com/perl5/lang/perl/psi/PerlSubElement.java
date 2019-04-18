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

package com.perl5.lang.perl.psi;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.util.Processor;
import com.intellij.util.containers.Queue;
import com.perl5.lang.perl.psi.mro.PerlMro;
import com.perl5.lang.perl.psi.properties.PerlIdentifierOwner;
import com.perl5.lang.perl.util.PerlPackageUtil;
import com.perl5.lang.perl.util.PerlSubUtil;
import gnu.trove.THashSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface PerlSubElement extends PerlSub, PsiElement, PerlIdentifierOwner {
  @Nullable
  default PerlSubElement getDirectSuperMethod() {
    if (!isMethod()) {
      return null;
    }

    Collection<PsiElement> resolveTargets = PerlMro.resolveSub(
      getProject(), getResolveScope(),
      getNamespaceName(),
      getSubName(),
      true
    );

    for (PsiElement resolveTarget : resolveTargets) {
      if (resolveTarget instanceof PerlSubElement) {
        return (PerlSubElement)resolveTarget;
      }
    }
    return null;
  }

  @NotNull
  default PerlSubElement getTopmostSuperMethod() {
    Set<String> classRecursion = new THashSet<>();

    PerlSubElement run = this;
    while (true) {
      String packageName = run.getNamespaceName();
      if (StringUtil.isEmpty(packageName) || classRecursion.contains(packageName)) {
        return run;
      }
      classRecursion.add(packageName);
      PerlSubElement newRun = run.getDirectSuperMethod();
      if (newRun == null) {
        return run;
      }
      run = newRun;
    }
  }

  default List<PerlSubDefinitionElement> getDirectOverridingSubs() {
    List<PerlSubDefinitionElement> result = new ArrayList<>();
    processDirectOverridingSubs(result::add);
    return result;
  }

  default boolean isBuiltIn() {
    return false;
  }

  default boolean processDirectOverridingSubs(@NotNull Processor<PerlSubDefinitionElement> processor) {
    String packageName = getNamespaceName();
    String subName = getSubName();
    if (packageName == null || subName == null) {
      return true;
    }
    Set<String> recursionSet = new THashSet<>();
    Project project = getProject();
    Queue<String> packagesToProcess = new Queue<>(5);
    packagesToProcess.addLast(packageName);

    while (!packagesToProcess.isEmpty()) {
      packageName = packagesToProcess.pullFirst();
      NAMESPACE:
      for (PerlNamespaceDefinitionElement childNamespace : PerlPackageUtil.getChildNamespaces(project, packageName)) {
        String childNamespaceName = childNamespace.getNamespaceName();
        if (recursionSet.add(childNamespaceName)) {
          for (PerlSubDefinitionElement subDefinition : PerlSubUtil.getSubDefinitionsInPackage(project, childNamespaceName)) {
            if (subName.equals(subDefinition.getSubName()) && subDefinition.getDirectSuperMethod() == this) {
              processor.process(subDefinition);
              continue NAMESPACE;
            }
          }
          packagesToProcess.addLast(childNamespaceName);
        }
      }
    }
    return true;
  }
}
