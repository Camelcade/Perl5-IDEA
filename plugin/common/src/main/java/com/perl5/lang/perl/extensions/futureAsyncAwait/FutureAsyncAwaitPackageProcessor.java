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

package com.perl5.lang.perl.extensions.futureAsyncAwait;

import com.perl5.lang.perl.extensions.packageprocessor.PerlPackageOptionsProvider;
import com.perl5.lang.perl.extensions.packageprocessor.PerlPackageProcessorBase;
import com.perl5.lang.perl.psi.impl.PerlUseStatementElement;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FutureAsyncAwaitPackageProcessor extends PerlPackageProcessorBase implements PerlPackageOptionsProvider {
  /**
   * These are actually keywords, but to avoid writing custom code for documentation/completion, we treat them here as exported methods
   * and lexer handles the rest.
   */
  private static final List<String> METHODS = List.of("async", "await");

  @Override
  public void addExports(@NotNull PerlUseStatementElement useStatement,
                         @NotNull Set<? super String> export,
                         @NotNull Set<? super String> exportOk) {
    export.addAll(METHODS);
    exportOk.addAll(METHODS);
  }

  @Override
  public @NotNull Map<String, String> getOptions() {
    return Collections.emptyMap();
  }

  @Override
  public @NotNull Map<String, String> getOptionsBundles() {
    return Collections.emptyMap();
  }
}
