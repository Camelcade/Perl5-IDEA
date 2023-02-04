/*
 * Copyright 2015-2023 Alexandr Evstigneev
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
import com.perl5.lang.perl.psi.impl.PerlUseStatementElement;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class ListMoreUtilsProcessor extends PerlPackageProcessorBase {
  private static final Map<String, PerlExportDescriptor> EXPORT_OK_DESCRIPTORS = new HashMap<>();

  static {
    for (String keyword : ListMoreUtilsExports.EXPORT_OK) {
      EXPORT_OK_DESCRIPTORS.put(keyword, PerlExportDescriptor.create("List::MoreUtils::PP", keyword));
    }
  }

  @Override
  public @NotNull List<PerlExportDescriptor> getImports(@NotNull PerlUseStatementElement useStatement) {
    List<String> parameters = useStatement.getImportParameters();
    if (parameters == null) {
      return Collections.emptyList();
    }
    return parameters.stream().distinct().map(EXPORT_OK_DESCRIPTORS::get).filter(Objects::nonNull).collect(Collectors.toList());
  }

  @Override
  public void addExports(@NotNull PerlUseStatementElement useStatement,
                         @NotNull Set<? super String> export,
                         @NotNull Set<? super String> exportOk) {
    super.addExports(useStatement, export, exportOk);
    exportOk.addAll(ListMoreUtilsExports.EXPORT_OK);
  }
}
