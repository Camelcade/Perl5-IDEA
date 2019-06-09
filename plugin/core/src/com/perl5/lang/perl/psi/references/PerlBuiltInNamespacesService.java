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

package com.perl5.lang.perl.psi.references;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiManager;
import com.intellij.util.Processor;
import com.perl5.lang.perl.psi.PerlNamespaceDefinitionElement;
import com.perl5.lang.perl.psi.impl.PerlBuiltInNamespaceDefinition;
import com.perl5.lang.perl.util.PerlPackageUtil;
import gnu.trove.THashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

import static com.perl5.lang.perl.util.PerlPackageUtil.*;

public class PerlBuiltInNamespacesService {
  private final Map<String, PerlBuiltInNamespaceDefinition> myNamespacesMap = new THashMap<>();

  public PerlBuiltInNamespacesService(@NotNull Project project) {
    PsiManager psiManager = PsiManager.getInstance(project);
    myNamespacesMap.put(MAIN_NAMESPACE_NAME, new PerlBuiltInNamespaceDefinition(psiManager, MAIN_NAMESPACE_NAME));
    myNamespacesMap.put(CORE_NAMESPACE, new PerlBuiltInNamespaceDefinition(psiManager, CORE_NAMESPACE));
    myNamespacesMap.put(CORE_GLOBAL_NAMESPACE, new PerlBuiltInNamespaceDefinition(psiManager, CORE_GLOBAL_NAMESPACE));
    myNamespacesMap.put(NAMESPACE_PACKAGE, new PerlBuiltInNamespaceDefinition(psiManager, NAMESPACE_PACKAGE));
  }

  @Nullable
  public PerlBuiltInNamespaceDefinition getNamespaceDefinition(@Nullable String name) {
    return name == null ? null : myNamespacesMap.get(PerlPackageUtil.getCanonicalName(name));
  }

  public boolean processNamespaces(@NotNull Processor<PerlNamespaceDefinitionElement> processor) {
    for (PerlBuiltInNamespaceDefinition namespaceDefinition : myNamespacesMap.values()) {
      if (!processor.process(namespaceDefinition)) {
        return false;
      }
    }
    return true;
  }

  @NotNull
  public static PerlBuiltInNamespacesService getInstance(@NotNull Project project) {
    return ServiceManager.getService(project, PerlBuiltInNamespacesService.class);
  }
}
