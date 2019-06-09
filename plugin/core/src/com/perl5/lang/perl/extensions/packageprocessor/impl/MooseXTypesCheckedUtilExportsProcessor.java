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

package com.perl5.lang.perl.extensions.packageprocessor.impl;

import com.perl5.lang.perl.extensions.packageprocessor.PerlExportDescriptor;
import com.perl5.lang.perl.extensions.packageprocessor.PerlPackageProcessorBase;
import com.perl5.lang.perl.psi.PerlUseStatement;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.perl5.lang.perl.util.PerlPackageUtil.PACKAGE_MOOSE_X_TYPES_CHECKEDUTILEXPORTS;

public class MooseXTypesCheckedUtilExportsProcessor extends PerlPackageProcessorBase {
  private static List<PerlExportDescriptor> EXPORTS = new ArrayList<>();

  static {
    for (String name : Arrays
      .asList("type", "subtype", "maybe_type", "duck_type", "enum", "coerce", "from", "as", "class_type", "role_type")) {
      EXPORTS.add(PerlExportDescriptor.create(PACKAGE_MOOSE_X_TYPES_CHECKEDUTILEXPORTS, name));
    }
  }

  @NotNull
  @Override
  public List<PerlExportDescriptor> getImports(@NotNull PerlUseStatement useStatement) {
    return EXPORTS;
  }
}
