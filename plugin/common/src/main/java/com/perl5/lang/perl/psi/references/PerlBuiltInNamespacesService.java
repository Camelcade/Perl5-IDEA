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

package com.perl5.lang.perl.psi.references;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiManager;
import com.intellij.util.Processor;
import com.perl5.lang.perl.psi.PerlNamespaceDefinitionElement;
import com.perl5.lang.perl.psi.impl.PerlBuiltInNamespaceDefinition;
import com.perl5.lang.perl.util.PerlPackageUtilCore;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class PerlBuiltInNamespacesService {
  private final Map<String, PerlBuiltInNamespaceDefinition> myNamespacesMap = new HashMap<>();

  public PerlBuiltInNamespacesService(@NotNull Project project) {
    PsiManager psiManager = PsiManager.getInstance(project);
    myNamespacesMap.put(PerlPackageUtilCore.MAIN_NAMESPACE_NAME,
                        new PerlBuiltInNamespaceDefinition(psiManager, PerlPackageUtilCore.MAIN_NAMESPACE_NAME));
    myNamespacesMap.put(PerlPackageUtilCore.CORE_NAMESPACE,
                        new PerlBuiltInNamespaceDefinition(psiManager, PerlPackageUtilCore.CORE_NAMESPACE));
    myNamespacesMap.put(PerlPackageUtilCore.CORE_GLOBAL_NAMESPACE,
                        new PerlBuiltInNamespaceDefinition(psiManager, PerlPackageUtilCore.CORE_GLOBAL_NAMESPACE));
    myNamespacesMap.put(PerlPackageUtilCore.UNIVERSAL_NAMESPACE,
                        new PerlBuiltInNamespaceDefinition(psiManager, PerlPackageUtilCore.UNIVERSAL_NAMESPACE));
  }

  public @Nullable PerlBuiltInNamespaceDefinition getNamespaceDefinition(@Nullable String name) {
    return name == null ? null : myNamespacesMap.get(PerlPackageUtilCore.getCanonicalName(name));
  }

  @SuppressWarnings("UnusedReturnValue")
  public boolean processNamespaces(@NotNull Processor<? super PerlNamespaceDefinitionElement> processor) {
    for (PerlBuiltInNamespaceDefinition namespaceDefinition : myNamespacesMap.values()) {
      if (!processor.process(namespaceDefinition)) {
        return false;
      }
    }
    return true;
  }

  public static @NotNull PerlBuiltInNamespacesService getInstance(@NotNull Project project) {
    return project.getService(PerlBuiltInNamespacesService.class);
  }
}
