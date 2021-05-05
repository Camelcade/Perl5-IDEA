/*
 * Copyright 2015-2021 Alexandr Evstigneev
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

package com.perl5.lang.perl.extensions.packageprocessor.impl;

import com.perl5.lang.perl.extensions.packageprocessor.PerlExportDescriptor;
import com.perl5.lang.perl.extensions.packageprocessor.PerlUtfProvider;
import com.perl5.lang.perl.psi.impl.PerlUseStatementElement;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class DancerPackageProcessor extends BaseStrictWarningsProvidingProcessor implements PerlUtfProvider {
  private static final List<PerlExportDescriptor> EXPORT_DESCRIPTORS = new ArrayList<>();

  static {
    for (String keyword : PerlDancerDSL.DSL_KEYWORDS) {
      EXPORT_DESCRIPTORS.add(PerlExportDescriptor.create("Dancer", keyword));
    }
  }

  @Override
  public @NotNull List<PerlExportDescriptor> getImports(@NotNull PerlUseStatementElement useStatement) {
    return getExportDescriptors();
  }

  public List<PerlExportDescriptor> getExportDescriptors() {
    return EXPORT_DESCRIPTORS;
  }
}
