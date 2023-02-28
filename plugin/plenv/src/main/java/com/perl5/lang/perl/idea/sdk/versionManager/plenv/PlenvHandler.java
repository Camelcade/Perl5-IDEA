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

package com.perl5.lang.perl.idea.sdk.versionManager.plenv;

import com.intellij.openapi.util.NlsSafe;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.idea.sdk.PerlHandlerBean;
import com.perl5.lang.perl.idea.sdk.host.PerlHostData;
import com.perl5.lang.perl.idea.sdk.versionManager.InstallPerlHandler;
import com.perl5.lang.perl.idea.sdk.versionManager.PerlRealVersionManagerHandler;
import com.perl5.lang.perl.idea.sdk.versionManager.PerlVersionManagerAdapter;
import com.perl5.lang.perl.idea.sdk.versionManager.PerlVersionManagerHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;

import javax.swing.*;
import java.util.Collection;
import java.util.Collections;

class PlenvHandler extends PerlRealVersionManagerHandler<PlenvData, PlenvHandler> {

  private static final @NlsSafe String PLENV = "plenv";
  private static final @NlsSafe String PLENV_DEFAULT_PATH = "~/.plenv/bin/" + PLENV;

  @SuppressWarnings("NonDefaultConstructor")
  public PlenvHandler(@NotNull PerlHandlerBean bean) {
    super(bean);
  }

  @Override
  public @NotNull String getPresentableName() {
    return PLENV;
  }

  @Override
  protected @NotNull String getExecutableName() {
    return PLENV;
  }

  @Override
  protected @NotNull Collection<String> getPossibleVersionManagerPaths() {
    return Collections.singletonList(PLENV_DEFAULT_PATH);
  }

  @Override
  public @NotNull PlenvData createData() {
    return new PlenvData(this);
  }

  @Override
  public @NotNull PerlVersionManagerAdapter createAdapter(@NotNull String pathToVersionManager, @NotNull PerlHostData<?, ?> hostData) {
    return new PlenvAdapter(pathToVersionManager, hostData);
  }

  @Override
  protected @NotNull PlenvData createData(@NotNull PerlVersionManagerAdapter vmAdapter, @NotNull String distributionId) {
    return new PlenvData(vmAdapter.getVersionManagerPath(), distributionId, this);
  }

  @Override
  public @NotNull Icon getIcon() {
    return PerlIcons.PLENV_ICON;
  }

  @Override
  public @Nullable InstallPerlHandler createInstallHandler(@NotNull String pathToVersionManager) {
    return new PlenvInstallPerlHandler(pathToVersionManager, this);
  }

  @TestOnly
  static @NotNull PlenvHandler getInstance() {
    for (PerlVersionManagerHandler<?, ?> handler : PerlVersionManagerHandler.all()) {
      if (handler instanceof PlenvHandler) {
        return (PlenvHandler)handler;
      }
    }
    throw new NullPointerException();
  }
}
