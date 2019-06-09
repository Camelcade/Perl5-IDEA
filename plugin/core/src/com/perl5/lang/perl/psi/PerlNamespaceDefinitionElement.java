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

import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiInvalidElementAccessException;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.search.GlobalSearchScope;
import com.perl5.lang.perl.extensions.packageprocessor.PerlExportDescriptor;
import com.perl5.lang.perl.psi.stubs.imports.PerlUseStatementsIndex;
import com.perl5.lang.perl.util.PerlPackageUtil;
import com.perl5.lang.perl.util.processors.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface PerlNamespaceDefinitionElement extends PerlNamespaceDefinition, PsiNamedElement, NavigationItem {
  @NotNull
  @Contract(pure = true)
  Project getProject() throws PsiInvalidElementAccessException;

  default boolean processExportDescriptors(@NotNull PerlNamespaceEntityProcessor<PerlExportDescriptor> processor) {
    String namespaceName = getNamespaceName();
    if (StringUtil.isEmpty(namespaceName)) {
      return true;
    }
    return processExportDescriptors(
      getProject(), GlobalSearchScope.fileScope(getContainingFile().getOriginalFile()), namespaceName, processor);
  }

  @NotNull
  default List<PerlExportDescriptor> collectImportDescriptors(@NotNull PerlImportsCollector collector) {
    processExportDescriptors(collector);
    return collector.getResult();
  }

  @NotNull
  default List<PerlExportDescriptor> getImportedSubsDescriptors() {
    return collectImportDescriptors(new PerlSubImportsCollector());
  }

  @NotNull
  default List<PerlExportDescriptor> getImportedScalarDescriptors() {
    return collectImportDescriptors(new PerlScalarImportsCollector());
  }

  @NotNull
  default List<PerlExportDescriptor> getImportedArrayDescriptors() {
    return collectImportDescriptors(new PerlArrayImportsCollector());
  }

  @NotNull
  default List<PerlExportDescriptor> getImportedHashDescriptors() {
    return collectImportDescriptors(new PerlHashImportsCollector());
  }

  default List<PerlNamespaceDefinitionElement> getParentNamespaceDefinitions() {
    return PerlPackageUtil.collectNamespaceDefinitions(getProject(), getParentNamespacesNames());
  }

  @NotNull
  default List<PerlNamespaceDefinitionElement> getChildNamespaceDefinitions() {
    return PerlPackageUtil.getChildNamespaces(getProject(), getNamespaceName());
  }

  default void getLinearISA(@NotNull Set<String> recursionMap, @NotNull List<String> result) {
    getMro().getLinearISA(getProject(), getParentNamespaceDefinitions(), recursionMap, result);
  }

  @NotNull
  static Set<PerlExportDescriptor> getExportDescriptors(@NotNull Project project,
                                                        @NotNull GlobalSearchScope searchScope,
                                                        @NotNull String namespaceName) {
    Set<PerlExportDescriptor> result = new HashSet<>();
    PerlNamespaceDefinitionElement.processExportDescriptors(
      project, searchScope, namespaceName, (__, it) -> {
        result.add(it);
        return true;
      });
    return result;
  }

  static boolean processExportDescriptors(@NotNull Project project,
                                          @NotNull GlobalSearchScope searchScope,
                                          @NotNull String namespaceName,
                                          @NotNull PerlNamespaceEntityProcessor<? super PerlExportDescriptor> processor) {
    return PerlUseStatementsIndex.processElements(project, searchScope, namespaceName, it -> {
      String packageName = it.getPackageName();

      if (packageName == null) {
        return true;
      }
      for (PerlExportDescriptor entry : it.getPackageProcessor().getImports(it)) {
        if (!processor.process(packageName, entry)) {
          return false;
        }
      }
      return true;
    });
  }
}
