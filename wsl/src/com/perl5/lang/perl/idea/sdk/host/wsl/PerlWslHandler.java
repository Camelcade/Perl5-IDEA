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

package com.perl5.lang.perl.idea.sdk.host.wsl;

import com.intellij.execution.wsl.WSLDistribution;
import com.intellij.execution.wsl.WSLUtil;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.ui.Messages;
import com.intellij.util.ArrayUtil;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.lang.perl.idea.sdk.PerlHandlerBean;
import com.perl5.lang.perl.idea.sdk.host.PerlHostWithFileSystemHandler;
import com.perl5.lang.perl.idea.sdk.host.os.PerlOsHandler;
import com.perl5.lang.perl.idea.sdk.host.os.PerlOsHandlers;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class PerlWslHandler extends PerlHostWithFileSystemHandler<PerlWslData, PerlWslHandler> {
  public PerlWslHandler(@NotNull PerlHandlerBean bean) {
    super(bean);
  }

  @Nullable
  protected PerlWslData createDataInteractively() {
    String[] ids = ArrayUtil.toStringArray(ContainerUtil.map(WSLUtil.getAvailableDistributions(), WSLDistribution::getId));
    if (ids.length < 1) {
      return null;
    }
    int[] result = new int[]{-1};
    ApplicationManager.getApplication().invokeAndWait(
      () -> result[0] = Messages.showChooseDialog(
        PerlWslBundle.message("perl.host.handler.wsl.choose.distro.message"),
        PerlWslBundle.message("perl.host.handler.wsl.choose.distro.title"),
        ids, ids[0], null));
    int index = result[0];

    if (index < 0 || index >= ids.length) {
      return null;
    }

    PerlWslData hostData = createData();
    hostData.setDistributionId(ids[index]);
    return hostData;
  }

  @NotNull
  @Override
  public String getMenuItemTitle() {
    return PerlWslBundle.message("perl.host.handler.wsl.menu.title");
  }

  @NotNull
  @Override
  public String getShortName() {
    return PerlWslBundle.message("perl.host.handler.wsl.short.name");
  }

  @Override
  public boolean isApplicable() {
    return WSLUtil.hasAvailableDistributions();
  }

  @NotNull
  @Override
  public PerlOsHandler getOsHandler() {
    return PerlOsHandlers.LINUX;
  }

  @NotNull
  @Override
  public PerlWslData createData() {
    return new PerlWslData(this);
  }
}
