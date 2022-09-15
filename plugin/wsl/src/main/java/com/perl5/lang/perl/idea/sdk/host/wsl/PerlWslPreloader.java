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
import com.intellij.ide.AppLifecycleListener;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.progress.ProgressManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class PerlWslPreloader implements AppLifecycleListener {

  @Override
  public void appFrameCreated(@NotNull List<String> commandLineArgs) {
    if (ApplicationManager.getApplication().isDispatchThread()) {
      ProgressManager.getInstance().runProcessWithProgressSynchronously(
        this::doInitWslPaths,
        PerlWslBundle.message("progress.title.initializing.wsl.subsystem"), false, null);
    }
    else {
      doInitWslPaths();
    }
  }

  private void doInitWslPaths() {
    if (WSLUtil.isSystemCompatible()) {
      for (WSLDistribution distribution : WslDistributionManager.getInstance().getInstalledDistributions()) {
        distribution.getMntRoot();
      }
    }
  }
}
