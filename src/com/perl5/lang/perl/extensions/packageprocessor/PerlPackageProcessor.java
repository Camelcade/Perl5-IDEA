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

package com.perl5.lang.perl.extensions.packageprocessor;

import com.perl5.lang.perl.parser.builder.PerlBuilder;
import com.perl5.lang.perl.psi.PerlUseStatement;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

/**
 * Created by hurricup on 18.08.2015.
 * implement this interface to provide a package processor
 */
public interface PerlPackageProcessor {
  /**
   * Returns true if package is pragma, false otherwise
   *
   * @return result
   */
  boolean isPragma();

  /**
   * Retuns list of imported descriptors
   *
   * @param useStatement use statement psi element
   * @return list of imported descriptors
   */
  @NotNull
  List<PerlExportDescriptor> getImports(@NotNull PerlUseStatement useStatement);


  /**
   * Populates export and exportOk sets with exported names
   *
   * @param useStatement use statement we processing
   * @param export       export set to fill
   * @param exportOk     export_ok set to fill
   */
  void addExports(@NotNull PerlUseStatement useStatement, @NotNull Set<String> export, @NotNull Set<String> exportOk);

  /**
   * Parses use statement parameters. Might be used if you need to put some specific PSI constructs in the parameters,
   * like constant definition, moose attributes or smth.
   * By default uses default parser passed from generated parser.
   * It's safe to assume that builder is at PACKAGE token with appropriate name
   * We are not advancing lexer through PACKAGE and [VERSION], because it may be used in parsing logic. If you
   * don't need them, just invoke {@link com.perl5.lang.perl.parser.PerlParserUtil#passPackageAndVersion(PerlBuilder, int)}
   */
  default boolean parseUseParameters(@NotNull PerlBuilder b, int l) {
    return false;
  }
}
