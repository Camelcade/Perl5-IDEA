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

package com.perl5.lang.perl.idea.sdk.host.docker;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.PathEnvironmentVariableUtil;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.options.UnnamedConfigurable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.messages.MessagesService;
import com.intellij.openapi.util.NlsActions.ActionText;
import com.intellij.util.ArrayUtil;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.idea.sdk.PerlHandlerBean;
import com.perl5.lang.perl.idea.sdk.host.PerlHostHandler;
import com.perl5.lang.perl.idea.sdk.host.PerlHostWithFileSystemHandler;
import com.perl5.lang.perl.idea.sdk.host.os.PerlOsHandler;
import com.perl5.lang.perl.idea.sdk.host.os.PerlOsHandlers;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;

import javax.swing.*;
import java.util.List;

class PerlDockerHandler extends PerlHostWithFileSystemHandler<PerlDockerData, PerlDockerHandler> {
  private static final Logger LOG = Logger.getInstance(PerlDockerHandler.class);

  @SuppressWarnings("NonDefaultConstructor")
  public PerlDockerHandler(@NotNull PerlHandlerBean bean) {
    super(bean);
  }

  @Override
  protected @Nullable PerlDockerData createDataInteractively() {
    PerlDockerData hostData = createData();
    List<String> images;
    try {
      images = new PerlDockerAdapter(hostData).listImages();
    }
    catch (ExecutionException e) {
      showErrorDialog(e.getMessage());
      LOG.warn("Error listing docker images: " + e.getMessage());
      return null;
    }
    if (images.isEmpty()) {
      showErrorDialog(PerlDockerBundle.message("perl.host.handler.docker.error.list.images.empty"));
      return null;
    }
    String[] imagesArray = ArrayUtil.toStringArray(images);
    int[] resultIndex = new int[]{-1};
    ApplicationManager.getApplication().invokeAndWait(
      () -> resultIndex[0] = MessagesService.getInstance().showChooseDialog(
        null, null,
        PerlDockerBundle.message("perl.host.handler.docker.choose.distro.message"),
        PerlDockerBundle.message("perl.host.handler.docker.choose.distro.title"),
        imagesArray, imagesArray[0], null));
    int index = resultIndex[0];

    if (index < 0 || index >= imagesArray.length) {
      return null;
    }
    hostData.withImageName(images.get(index));
    return hostData;
  }

  private void showErrorDialog(@NotNull String message) {
    ApplicationManager.getApplication().invokeAndWait(() -> Messages.showErrorDialog(
      message,
      PerlDockerBundle.message("perl.host.handler.docker.error.list.images.title")
    ));
  }

  @Override
  public @NotNull @ActionText String getMenuItemTitle() {
    return PerlDockerBundle.message("perl.host.handler.docker.menu.title");
  }

  @Override
  public @NotNull String getShortName() {
    return PerlDockerBundle.message("perl.host.handler.docker.short.name");
  }

  @Override
  public boolean isApplicable() {
    return PathEnvironmentVariableUtil.findInPath(PerlDockerAdapter.DOCKER_EXECUTABLE) != null;
  }

  @Override
  public @Nullable PerlOsHandler getOsHandler() {
    return PerlOsHandlers.LINUX;
  }

  @Override
  public @NotNull PerlDockerData createData() {
    return new PerlDockerData(this);
  }

  @Override
  public @Nullable UnnamedConfigurable getSettingsConfigurable(@NotNull Project project) {
    return new PerlDockerProjectSettingsConfigurable(project);
  }

  @Override
  public @Nullable Icon getIcon() {
    return PerlIcons.DOCKER_ICON;
  }

  @TestOnly
  static @NotNull PerlDockerHandler getInstance() {
    for (PerlHostHandler<?, ?> handler : PerlHostHandler.all()) {
      if (handler instanceof PerlDockerHandler) {
        return (PerlDockerHandler)handler;
      }
    }
    throw new NullPointerException();
  }
}
