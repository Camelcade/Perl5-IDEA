/*
 * Copyright 2015-2018 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.sdk.implementation.porters;

import com.perl5.lang.perl.idea.sdk.PerlHandlerBean;
import com.perl5.lang.perl.idea.sdk.host.PerlHostData;
import com.perl5.lang.perl.idea.sdk.implementation.PerlImplementationHandler;
import com.perl5.lang.perl.idea.sdk.versionManager.PerlVersionManagerData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class PerlPortersHandler
  extends PerlImplementationHandler<PerlPortersData, PerlPortersHandler> {

  public PerlPortersHandler(@NotNull PerlHandlerBean bean) {
    super(bean);
  }

  @NotNull
  @Override
  public PerlPortersData createData() {
    return new PerlPortersData(this);
  }

  @Nullable
  @Override
  protected PerlPortersData doCreateData(@Nullable String interpreterPath,
                                         @Nullable PerlHostData hostData,
                                         @Nullable PerlVersionManagerData versionManagerData) {
    return interpreterPath == null || hostData == null || versionManagerData == null ? null : createData();
  }
}
