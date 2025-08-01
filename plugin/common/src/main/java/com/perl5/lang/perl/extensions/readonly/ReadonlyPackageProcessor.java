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

package com.perl5.lang.perl.extensions.readonly;

import com.perl5.lang.perl.extensions.packageprocessor.PerlPackageProcessorBase;
import com.perl5.lang.perl.psi.impl.PerlUseStatementElement;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class ReadonlyPackageProcessor extends PerlPackageProcessorBase {
  private static final List<String> EXPORT = Collections.singletonList("Readonly");
  private static final List<String> EXPORT_OK = Arrays.asList(
    "Scalar", "Array", "Hash", "Scalar1", "Array1", "Hash1"
  );

  @Override
  public void addExports(@NotNull PerlUseStatementElement useStatement,
                         @NotNull Set<? super String> export,
                         @NotNull Set<? super String> exportOk) {
    export.addAll(EXPORT);
    exportOk.addAll(EXPORT_OK);
  }
}