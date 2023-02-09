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

import com.perl5.lang.perl.extensions.packageprocessor.PerlFeaturesProvider;
import com.perl5.lang.perl.extensions.packageprocessor.PerlMroProvider;
import com.perl5.lang.perl.extensions.packageprocessor.PerlPackageLoader;
import com.perl5.lang.perl.internals.PerlFeaturesTable;
import com.perl5.lang.perl.psi.impl.PerlUseStatementElement;
import com.perl5.lang.perl.psi.mro.PerlMroType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * package processor for Modern::Perl module: http://search.cpan.org/~chromatic/Modern-Perl-1.20150127/lib/Modern/Perl.pm
 */
public class ModernPerlPackageProcessor extends BaseStrictWarningsProvidingProcessor implements
                                                                                     PerlMroProvider,
                                                                                     PerlPackageLoader,
                                                                                     PerlFeaturesProvider {
  private static final List<String> LOADED_PACKAGES = List.of(
    "IO::File",
    "IO::Handle"
  );

  @Override
  public @NotNull PerlMroType getMroType(PerlUseStatementElement useStatement) {
    return PerlMroType.C3;
  }

  @Override
  public @NotNull List<String> getLoadedPackageNames(PerlUseStatementElement useStatement) {
    return LOADED_PACKAGES;
  }

  @Override
  public PerlFeaturesTable getFeaturesTable(PerlUseStatementElement useStatement, PerlFeaturesTable currentFeaturesTable) {
    // fixme implement modification
    return currentFeaturesTable == null ? new PerlFeaturesTable() : currentFeaturesTable.clone();
  }
}
