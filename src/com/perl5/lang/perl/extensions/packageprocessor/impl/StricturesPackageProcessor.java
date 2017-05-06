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

import com.perl5.lang.perl.extensions.packageprocessor.PerlPackageProcessorBase;
import com.perl5.lang.perl.extensions.packageprocessor.PerlStrictProvider;
import com.perl5.lang.perl.extensions.packageprocessor.PerlWarningsProvider;
import com.perl5.lang.perl.internals.PerlStrictMask;
import com.perl5.lang.perl.internals.PerlWarningsMask;
import com.perl5.lang.perl.psi.PerlUseStatement;

/**
 * Created by hurricup on 23.06.2016.
 */
public class StricturesPackageProcessor extends PerlPackageProcessorBase implements
                                                                         PerlWarningsProvider,
                                                                         PerlStrictProvider {
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
}
