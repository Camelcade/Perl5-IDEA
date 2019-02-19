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
import com.intellij.psi.PsiNamedElement;
import com.perl5.lang.perl.extensions.packageprocessor.PerlExportDescriptor;
import com.perl5.lang.perl.util.PerlArrayUtil;
import com.perl5.lang.perl.util.PerlHashUtil;
import com.perl5.lang.perl.util.PerlScalarUtil;
import com.perl5.lang.perl.util.PerlSubUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface PerlNamespaceDefinitionElement extends PerlNamespaceDefinition, PsiNamedElement, NavigationItem {
  @NotNull
  default List<PerlExportDescriptor> getImportedSubsDescriptors() {
    return PerlSubUtil.getImportedSubsDescriptors(this);
  }

  @NotNull
  default List<PerlExportDescriptor> getImportedScalarDescriptors() {
    return PerlScalarUtil.getImportedScalarsDescritptors(this);
  }

  @NotNull
  default List<PerlExportDescriptor> getImportedArrayDescriptors() {
    return PerlArrayUtil.getImportedArraysDescriptors(this);
  }

  @NotNull
  default List<PerlExportDescriptor> getImportedHashDescriptors() {
    return PerlHashUtil.getImportedHashesDescriptors(this);
  }
}
