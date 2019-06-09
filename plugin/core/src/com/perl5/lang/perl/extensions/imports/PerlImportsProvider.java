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

package com.perl5.lang.perl.extensions.imports;

import com.intellij.openapi.extensions.ExtensionPointName;
import com.perl5.lang.perl.extensions.packageprocessor.PerlExportDescriptor;
import com.perl5.lang.perl.psi.PerlNamespaceDefinitionElement;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Extension point that can provide additional import descriptors by {@link com.perl5.lang.perl.psi.PerlNamespaceDefinitionElement}
 * e.g. implicit imports for carton file
 */
public interface PerlImportsProvider {
  ExtensionPointName<PerlImportsProvider> EP_NAME = ExtensionPointName.create("com.perl5.importsProvider");

  @Contract("null->false")
  boolean isApplicable(@Nullable PerlNamespaceDefinitionElement namespaceDefinitionElement);

  @NotNull
  default List<PerlExportDescriptor> getExportDescriptors(PerlNamespaceDefinitionElement namespaceElement) {
    return Collections.emptyList();
  }

  /**
   * Iterates all providers applicable to {@code namespaceDefinitionElement} and passes them to {@code providerProcessor} stops if
   * provider returns false
   */
  static Set<PerlExportDescriptor> getAllExportDescriptors(@Nullable PerlNamespaceDefinitionElement namespaceDefinitionElement) {
    Set<PerlExportDescriptor> result = new HashSet<>();
    for (PerlImportsProvider importsProvider : EP_NAME.getExtensions()) {
      if (importsProvider.isApplicable(namespaceDefinitionElement)) {
        result.addAll(importsProvider.getExportDescriptors(namespaceDefinitionElement));
      }
    }
    return result;
  }
}
