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

import java.util.Arrays;
import java.util.List;

public class DataPrinterProcessor extends PerlPackageProcessorBase {
  private static final String DATA_PRINTER = "Data::Printer";
  private static final List<PerlExportDescriptor> EXPORTS = Arrays.asList(
    PerlExportDescriptor.create(DATA_PRINTER, "p"),
    PerlExportDescriptor.create(DATA_PRINTER, "np")
  );

  @NotNull
  @Override
  public List<PerlExportDescriptor> getImports(@NotNull PerlUseStatement useStatement) {
    return EXPORTS;
  }
}
