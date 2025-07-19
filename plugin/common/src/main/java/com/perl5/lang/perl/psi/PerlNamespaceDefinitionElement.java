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

package com.perl5.lang.perl.psi;

import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiInvalidElementAccessException;
import com.intellij.psi.PsiNamedElement;
import com.perl5.lang.perl.extensions.packageprocessor.PerlExportDescriptor;
import com.perl5.lang.perl.util.processors.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface PerlNamespaceDefinitionElement extends PerlNamespaceDefinition, PsiNamedElement, NavigationItem {

  @Override
  @NotNull
  @Contract(pure = true)
  Project getProject() throws PsiInvalidElementAccessException;

  default @NotNull List<PerlExportDescriptor> collectImportDescriptors(@NotNull PerlImportsCollector collector) {
    PerlNamespaceDefinitionHandler.instance(this).processExportDescriptors(this, collector);
    return collector.getResult();
  }

  default @NotNull List<PerlExportDescriptor> getImportedSubsDescriptors() {
    return collectImportDescriptors(new PerlSubImportsCollector());
  }

  default @NotNull List<PerlExportDescriptor> getImportedScalarDescriptors() {
    return collectImportDescriptors(new PerlScalarImportsCollector());
  }

  default @NotNull List<PerlExportDescriptor> getImportedArrayDescriptors() {
    return collectImportDescriptors(new PerlArrayImportsCollector());
  }

  default @NotNull List<PerlExportDescriptor> getImportedHashDescriptors() {
    return collectImportDescriptors(new PerlHashImportsCollector());
  }
}
