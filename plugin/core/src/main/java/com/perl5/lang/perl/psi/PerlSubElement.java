/*
 * Copyright 2015-2022 Alexandr Evstigneev
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
import com.intellij.openapi.util.Ref;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.Processor;
import com.perl5.lang.perl.psi.mro.PerlMro;
import com.perl5.lang.perl.util.PerlPackageUtil;
import com.perl5.lang.perl.util.PerlSubUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public interface PerlSubElement extends PerlSub, PerlCallableElement {
  default @Nullable PerlSubElement getDirectSuperMethod() {
    if (!isMethod()) {
      return null;
    }

    Ref<PerlSubElement> resultRef = Ref.create();
    PerlMro.processCallables(
      getProject(), getResolveScope(), getNamespaceName(), Collections.singleton(getSubName()), true,
      it -> {
        if (it instanceof PerlSubElement subElement) {
          resultRef.set(subElement);
          return false;
        }
        return true;
      },
      true);

    return resultRef.get();
  }

  default @NotNull PerlSubElement getTopmostSuperMethod() {
    Set<String> classRecursion = new HashSet<>();

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
    Set<String> recursionSet = new HashSet<>();
    Project project = getProject();
    Queue<String> packagesToProcess = new ArrayDeque<>(5);
    packagesToProcess.add(packageName);

    while (!packagesToProcess.isEmpty()) {
      packageName = packagesToProcess.poll();
      NAMESPACE:
      for (PerlNamespaceDefinitionElement childNamespace : PerlPackageUtil.getChildNamespaces(project, packageName)) {
        String childNamespaceName = childNamespace.getNamespaceName();
        if (StringUtil.isNotEmpty(childNamespaceName) && recursionSet.add(childNamespaceName)) {
          for (PerlSubDefinitionElement subDefinition : PerlSubUtil.getSubDefinitionsInPackage(project, childNamespaceName)) {
            if (subName.equals(subDefinition.getSubName()) && subDefinition.getDirectSuperMethod() == this) {
              processor.process(subDefinition);
              continue NAMESPACE;
            }
          }
          packagesToProcess.add(childNamespaceName);
        }
      }
    }
    return true;
  }
}
