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

package com.perl5.lang.perl.idea.sdk.host.wsl;

import com.intellij.execution.wsl.WSLDistribution;
import com.intellij.execution.wsl.WSLUtil;
import com.intellij.execution.wsl.WslDistributionManager;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.ui.messages.MessagesService;
import com.intellij.openapi.util.NlsActions.ActionText;
import com.intellij.openapi.util.ThrowableComputable;
import com.intellij.util.ArrayUtil;
import com.intellij.util.TimeoutUtil;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.idea.sdk.PerlHandlerBean;
import com.perl5.lang.perl.idea.sdk.host.PerlHostWithFileSystemHandler;
import com.perl5.lang.perl.idea.sdk.host.os.PerlOsHandler;
import com.perl5.lang.perl.idea.sdk.host.os.PerlOsHandlers;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.concurrent.ExecutionException;

class PerlWslHandler extends PerlHostWithFileSystemHandler<PerlWslData, PerlWslHandler> {
  private static final Logger LOG = Logger.getInstance(PerlWslHandler.class);
  private static final long COMPUTATION_TIMEOUT = 10_000;

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
  public @NotNull @ActionText String getMenuItemTitle() {
    return PerlWslBundle.message("perl.host.handler.wsl.menu.title");
  }

  @Override
  public @NotNull String getShortName() {
    return PerlWslBundle.message("perl.host.handler.wsl.short.name");
  }

  @Override
  public boolean isApplicable() {
    return Boolean.TRUE == computeSafeOnWsl(
      () -> WSLUtil.isSystemCompatible() && !WslDistributionManager.getInstance().getInstalledDistributions().isEmpty());
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

  /**
   * @apiNote Method is intended to avoid the issue with random WSL executions. Some computations require listing of wsl distributions
   * or initialising a mount point. And both involve execution of the external tool in the random context: EDT, ReadAction, Actions update pass.
   * If everything is ok, this should work smoothly. If not - the worse thing that can happen - IDE will lag for up to 5 seconds. Probably
   * we should reduce the {@link #COMPUTATION_TIMEOUT}, but let's hope for the best. We could show notification here about possible WSL issues
   * and make WSL subsystem unavailable for the current session.
   */
  public static <T, E extends Exception> @Nullable T computeSafeOnWsl(@NotNull ThrowableComputable<T, E> computable) throws E {
    var application = ApplicationManager.getApplication();
    if (application.isDispatchThread()) {
      return ProgressManager.getInstance().runProcessWithProgressSynchronously(
        computable, PerlWslBundle.message("perl.host.handler.computing"), false, null);
    }
    else {
      var future = application.executeOnPooledThread(computable::compute);
      try {
        var started = System.currentTimeMillis();
        while (!future.isDone() && System.currentTimeMillis() < started + COMPUTATION_TIMEOUT) {
          TimeoutUtil.sleep(10);
        }
        if (future.isDone()) {
          return future.get();
        }
        LOG.warn(new Throwable(
          "Failed to perform WSL computation in " + COMPUTATION_TIMEOUT + "; probably there is some issue with WSL subsystem"));
        future.cancel(true);
      }
      catch (InterruptedException e) {
        LOG.warn("WSL computation was interrupted", e);
      }
      catch (ExecutionException e) {
        LOG.warn("Error performing WSL computation", e);
      }
      return null;
    }
  }
}
