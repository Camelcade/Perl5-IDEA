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

package com.perl5.lang.perl.idea.sdk.versionManager.plenv;

import com.perl5.PerlBundle;
import com.perl5.lang.perl.idea.sdk.PerlHandlerBean;
import com.perl5.lang.perl.idea.sdk.host.PerlHostData;
import com.perl5.lang.perl.idea.sdk.versionManager.PerlRealVersionManagerHandler;
import com.perl5.lang.perl.idea.sdk.versionManager.PerlVersionManagerAdapter;
import org.jetbrains.annotations.NotNull;

class PlenvHandler extends PerlRealVersionManagerHandler<PlenvData, PlenvHandler> {

  public PlenvHandler(@NotNull PerlHandlerBean bean) {
    super(bean);
  }

  @NotNull
  @Override
  public String getPresentableName() {
    return PerlBundle.message("perl.vm.plenv.presentable.name");
  }

  @NotNull
  @Override
  protected String getExecutableName() {
    return "plenv";
  }

  @NotNull
  @Override
  public PlenvData createData() {
    return new PlenvData(this);
  }

  @Override
  protected PerlVersionManagerAdapter createAdapter(@NotNull String pathToVersionManager, @NotNull PerlHostData hostData) {
    return new PlenvAdapter(pathToVersionManager, hostData);
  }

  @NotNull
  @Override
  protected PlenvData createData(@NotNull PerlVersionManagerAdapter vmAdapter, @NotNull String distributionId) {
    return new PlenvData(vmAdapter.getVersionManagerPath(), distributionId, this);
  }
}
