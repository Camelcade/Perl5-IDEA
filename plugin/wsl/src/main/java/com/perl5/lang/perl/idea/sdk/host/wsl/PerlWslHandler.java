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

package com.perl5.lang.perl.idea.sdk.host.wsl;

import com.intellij.execution.wsl.WSLDistribution;
import com.intellij.execution.wsl.WSLUtil;
import com.intellij.execution.wsl.WslDistributionManager;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.ui.messages.MessagesService;
import com.intellij.util.ArrayUtil;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.idea.sdk.PerlHandlerBean;
import com.perl5.lang.perl.idea.sdk.host.PerlHostWithFileSystemHandler;
import com.perl5.lang.perl.idea.sdk.host.os.PerlOsHandler;
import com.perl5.lang.perl.idea.sdk.host.os.PerlOsHandlers;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

class PerlWslHandler extends PerlHostWithFileSystemHandler<PerlWslData, PerlWslHandler> {
  @SuppressWarnings("NonDefaultConstructor")
  public PerlWslHandler(@NotNull PerlHandlerBean bean) {
    super(bean);
  }

  @Override
  protected @Nullable PerlWslData createDataInteractively() {
    String[] ids =
      ArrayUtil.toStringArray(ContainerUtil.map(WslDistributionManager.getInstance().getInstalledDistributions(), WSLDistribution::getId));
    if (ids.length < 1) {
      return null;
    }
    int[] result = new int[]{-1};
    ApplicationManager.getApplication().invokeAndWait(
      () -> result[0] = MessagesService.getInstance().showChooseDialog(
        null, null,
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

  @Override
  public @NotNull String getMenuItemTitle() {
    return PerlWslBundle.message("perl.host.handler.wsl.menu.title");
  }

  @Override
  public @NotNull String getShortName() {
    return PerlWslBundle.message("perl.host.handler.wsl.short.name");
  }

  @Override
  public boolean isApplicable() {
    return WSLUtil.isSystemCompatible() && !WslDistributionManager.getInstance().getInstalledDistributions().isEmpty();
  }

  @Override
  public @NotNull PerlOsHandler getOsHandler() {
    return PerlOsHandlers.LINUX;
  }

  @Override
  public @NotNull PerlWslData createData() {
    return new PerlWslData(this);
  }

  @Override
  public @Nullable Icon getIcon() {
    return PerlIcons.LINUX_ICON;
  }
}
