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

package com.perl5.lang.perl.extensions.moose;

import com.perl5.lang.perl.extensions.packageprocessor.PerlExportDescriptor;
import com.perl5.lang.perl.extensions.packageprocessor.impl.BaseStrictWarningsProvidingProcessor;
import com.perl5.lang.perl.psi.impl.PerlUseStatementElement;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.perl5.lang.perl.parser.moose.MooseSyntax.*;
import static com.perl5.lang.perl.util.PerlPackageUtil.*;

public class MooseRoleProcessor extends BaseStrictWarningsProvidingProcessor {
  static final List<PerlExportDescriptor> EXPORTS;

  static {
    List<PerlExportDescriptor> exports = new ArrayList<>();
    exports.add(PerlExportDescriptor.create(PACKAGE_CARP, "confess"));
    exports.add(PerlExportDescriptor.create(PACKAGE_SCALAR_UTIL, "blessed"));
    exports.add(PerlExportDescriptor.create(PACKAGE_CLASS_MOP_MIXIN, MOOSE_KEYWORD_META));
    Arrays.asList(MOOSE_KEYWORD_AFTER, MOOSE_KEYWORD_AROUND, MOOSE_KEYWORD_AUGMENT, MOOSE_KEYWORD_BEFORE, MOOSE_KEYWORD_EXCLUDES,
                  MOOSE_KEYWORD_EXTENDS, MOOSE_KEYWORD_HAS, MOOSE_KEYWORD_INNER, MOOSE_KEYWORD_META, MOOSE_KEYWORD_OVERRIDE,
                  MOOSE_KEYWORD_REQUIRES, MOOSE_KEYWORD_SUPER, MOOSE_KEYWORD_WITH).forEach(
      it -> exports.add(PerlExportDescriptor.create(PACKAGE_MOOSE_ROLE, it)));
    EXPORTS = List.copyOf(exports);
  }

  @Override
  public @NotNull List<PerlExportDescriptor> getImports(@NotNull PerlUseStatementElement useStatement) {
    return EXPORTS;
  }
}
