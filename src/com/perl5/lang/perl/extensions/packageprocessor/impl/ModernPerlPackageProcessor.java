/*
 * Copyright 2016 Alexandr Evstigneev
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

import com.perl5.lang.perl.extensions.packageprocessor.*;
import com.perl5.lang.perl.internals.PerlFeaturesTable;
import com.perl5.lang.perl.internals.PerlStrictMask;
import com.perl5.lang.perl.internals.PerlWarningsMask;
import com.perl5.lang.perl.psi.PerlUseStatement;
import com.perl5.lang.perl.psi.mro.PerlMroType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by hurricup on 01.06.2016.
 * package processor for Modern::Perl module: http://search.cpan.org/~chromatic/Modern-Perl-1.20150127/lib/Modern/Perl.pm
 */
public class ModernPerlPackageProcessor extends PerlPackageProcessorBase implements
                                                                         PerlStrictProvider,
                                                                         PerlWarningsProvider,
                                                                         PerlMroProvider,
                                                                         PerlPackageLoader,
                                                                         PerlFeaturesProvider {
  private static final List<String> LOADED_PACKAGES = new ArrayList<String>(Arrays.asList(
    "IO::File",
    "IO::Handle"
  ));

  @Override
  public PerlMroType getMroType(PerlUseStatement useStatement) {
    return PerlMroType.C3;
  }

  @Override
  @NotNull
  public List<String> getLoadedPackageNames(PerlUseStatement useStatement) {
    return LOADED_PACKAGES;
  }

  @Override
  public PerlStrictMask getStrictMask(PerlUseStatement useStatement, PerlStrictMask currentMask) {
    // fixme implement modification
    return currentMask.clone();
  }

  @Override
  public PerlWarningsMask getWarningMask(PerlUseStatement useStatement, PerlWarningsMask currentMask) {
    // fixme implement modification
    return currentMask.clone();
  }

  @Override
  public PerlFeaturesTable getFeaturesTable(PerlUseStatement useStatement, PerlFeaturesTable currentFeaturesTable) {
    // fixme implement modification
    return currentFeaturesTable == null ? new PerlFeaturesTable() : currentFeaturesTable.clone();
  }
}
