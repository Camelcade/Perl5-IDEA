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

package com.perl5.lang.perl.extensions.packageprocessor;

import com.perl5.lang.perl.internals.PerlFeaturesTable;
import com.perl5.lang.perl.psi.impl.PerlUseStatementElement;
import org.jetbrains.annotations.NotNull;

/**
 * Marks a package processor that it can modify %^H (see use feature)
 */
public interface PerlFeaturesProvider {
  /**
   * Modifies outer block's features table.
   *
   * @param useStatement         use statement reference
   * @param currentFeaturesTable features table of outer block
   * @return new features table
   * @implNote this is a stub for future use
   */
  @SuppressWarnings("unused")
  default @NotNull PerlFeaturesTable getFeaturesTable(@NotNull PerlUseStatementElement useStatement,
                                                      @NotNull PerlFeaturesTable currentFeaturesTable) {
    return currentFeaturesTable.clone();
  }
}
