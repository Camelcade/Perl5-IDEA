/*
 * Copyright 2015-2020 Alexandr Evstigneev
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

import com.perl5.PerlBundle;
import com.perl5.lang.perl.psi.PerlLightElementProvider;
import com.perl5.lang.perl.psi.impl.PerlUseStatementElement;
import com.perl5.lang.perl.psi.impl.PerlUseStatementElementBase;
import com.perl5.lang.perl.psi.stubs.imports.PerlUseStatementStub;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

/**
 * implement this interface to provide a package processor
 */
public interface PerlPackageProcessor extends PerlLightElementProvider<PerlUseStatementElement, PerlUseStatementStub> {
  /**
   * Returns true if package is pragma, false otherwise
   *
   * @return result
   */
  boolean isPragma();

  /**
   * Returns list of imported descriptors
   *
   * @param useStatement use statement psi element
   * @return list of imported descriptors
   */
  @NotNull
  List<PerlExportDescriptor> getImports(@NotNull PerlUseStatementElement useStatement);


  /**
   * Populates export and exportOk sets with exported names
   *
   * @param useStatement use statement we processing
   * @param export       export set to fill
   * @param exportOk     export_ok set to fill
   */
  void addExports(@NotNull PerlUseStatementElement useStatement, @NotNull Set<String> export, @NotNull Set<String> exportOk);

  /**
   * @return text that should be shown in folded block of use arguments
   */
  @Nullable
  default String getArgumentsFoldingText(@NotNull PerlUseStatementElementBase useStatementElement) {
    return PerlBundle.message("perl.fold.ph.text.default");
  }

  /**
   * @return true iff arguments of this use statement should be collapsed by default, e.g. group of constants definitions
   */
  default boolean isFoldedByDefault(@NotNull PerlUseStatementElementBase useStatementElement) {
    return false;
  }
}
