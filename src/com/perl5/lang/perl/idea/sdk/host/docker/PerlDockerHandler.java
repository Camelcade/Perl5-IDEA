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

package com.perl5.lang.perl.idea.sdk.host.docker;

import com.intellij.execution.ExecutionException;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.ui.Messages;
import com.intellij.util.ArrayUtil;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.idea.sdk.PerlHandlerBean;
import com.perl5.lang.perl.idea.sdk.host.PerlHostHandler;
import com.perl5.lang.perl.idea.sdk.host.os.PerlOsHandler;
import com.perl5.lang.perl.idea.sdk.host.os.PerlOsHandlers;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

class PerlDockerHandler extends PerlHostHandler<PerlDockerData, PerlDockerHandler> {
  private static final Logger LOG = Logger.getInstance(PerlDockerHandler.class);
  public PerlDockerHandler(@NotNull PerlHandlerBean bean) {
    super(bean);
  }

  @Nullable
  @Override
  protected PerlDockerData createDataInteractively() {
    PerlDockerData hostData = createData();
    List<String> images;
    try {
      images = new PerlDockerAdapter(hostData).listImages();
    }
    catch (ExecutionException e) {
      // fixme notify user?
      LOG.error(e);
      return null;
    }
    if (images.isEmpty()) {
      // fixme notfiy user?
      return null;
    }
    String[] imagesArray = ArrayUtil.toStringArray(images);
    int[] resultIndex = new int[]{-1};
    ApplicationManager.getApplication().invokeAndWait(
      () -> resultIndex[0] = Messages.showChooseDialog(
        PerlBundle.message("perl.host.handler.docker.choose.distro.message"),
        PerlBundle.message("perl.host.handler.docker.choose.distro.title"),
        imagesArray, imagesArray[0], null));
    int index = resultIndex[0];

    if (index < 0 || index >= imagesArray.length) {
      return null;
    }
    hostData.withImageName(images.get(index));
    return hostData;
  }

  @NotNull
  @Override
  public String getMenuItemTitle() {
    return PerlBundle.message("perl.host.handler.docker.menu.title");
  }

  @NotNull
  @Override
  public String getShortName() {
    return PerlBundle.message("perl.host.handler.docker.short.name");
  }

  @Override
  public boolean isApplicable() {
    return true;
  }

  @Nullable
  @Override
  public PerlOsHandler getOsHandler() {
    return PerlOsHandlers.LINUX;
  }

  @NotNull
  @Override
  public PerlDockerData createData() {
    return new PerlDockerData(this);
  }
}
