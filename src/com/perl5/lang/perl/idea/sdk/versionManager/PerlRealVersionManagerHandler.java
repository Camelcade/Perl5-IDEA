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

package com.perl5.lang.perl.idea.sdk.versionManager;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.Ref;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFileSystem;
import com.intellij.util.ArrayUtil;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.idea.sdk.PerlHandlerBean;
import com.perl5.lang.perl.idea.sdk.PerlSdkType;
import com.perl5.lang.perl.idea.sdk.host.PerlHostData;
import com.perl5.lang.perl.idea.sdk.host.PerlHostHandler;
import com.perl5.lang.perl.idea.sdk.host.os.PerlOsHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Consumer;

import static com.perl5.lang.perl.util.PerlRunUtil.PERL_CTRL_X;
import static com.perl5.lang.perl.util.PerlRunUtil.PERL_LE;

/**
 * Real version manager, except the system one
 */
public abstract class PerlRealVersionManagerHandler<Data extends PerlRealVersionManagerData<Data, Handler>, Handler extends PerlRealVersionManagerHandler<Data, Handler>>
  extends PerlVersionManagerHandler<Data, Handler> {
  private static final Logger LOG = Logger.getInstance(PerlRealVersionManagerHandler.class);
  static final String LIB_SEPARATOR = "@";

  public PerlRealVersionManagerHandler(@NotNull PerlHandlerBean bean) {
    super(bean);
  }

  @Override
  public boolean isApplicable(@Nullable PerlOsHandler osHandler) {
    return osHandler == null || !osHandler.isMsWindows();
  }

  @NotNull
  protected abstract Data createData(@NotNull PerlVersionManagerAdapter vmAdapter, @NotNull String distributionId);

  /**
   * @return version manager's executable name;
   */
  @NotNull
  protected abstract String getExecutableName();

  @NotNull
  @Override
  public String getMenuItemTitle() {
    return PerlBundle.message("perl.vm.menu.title", getPresentableName());
  }

  @Override
  public void createSdkInteractively(@NotNull PerlHostHandler<?, ?> hostHandler, @Nullable Consumer<Sdk> sdkConsumer) {
    hostHandler.chooseFileInteractively(
      PerlBundle.message("perl.vm.choose.executable", StringUtil.capitalize(getPresentableName())),
      this::suggestDefaultVersionManagerPath,
      true,
      it -> StringUtil.equals(it, getExecutableName()),
      it -> {
        String fileName = Paths.get(it).getFileName().toString();
        return StringUtil.equals(fileName, getExecutableName())
               ? null
               : PerlBundle.message("perl.vm.wrong.file", fileName, getPresentableName());
      },
      (path, perlHostData) -> createSdkInteractively(path, perlHostData, sdkConsumer));
  }

  /**
   * Suggests a default path for a file chooser or text input
   *
   * @param hostData host we are creating interpreter for
   * @return suggested path
   * @implSpec for now we are trying to find a path in the existing sdks from the same host, supported by the same version manager, or
   * just trying to find the executable file on the host
   */
  @Nullable
  protected File suggestDefaultVersionManagerPath(@NotNull PerlHostData<?, ?> hostData) {
    VirtualFileSystem fileSystem = hostData.getFileSystem();
    if (fileSystem == null) {
      return null;
    }
    return hostData.getHostSdkStream()
      .filter(this::isSameHandler)
      .map(it -> fileSystem.findFileByPath(PerlRealVersionManagerData.notNullFrom(it).getVersionManagerPath()))
      .filter(it -> it != null && it.isValid() && it.exists())
      .map(it -> new File(it.getPath()))
      .findFirst().orElseGet(() -> hostData.findFileByName(getExecutableName()));
  }

  protected abstract PerlVersionManagerAdapter createAdapter(@NotNull String pathToVersionManager, @NotNull PerlHostData hostData);

  private void createSdkInteractively(@Nullable String selectedPath,
                                      @Nullable PerlHostData perlHostData,
                                      @Nullable Consumer<Sdk> sdkConsumer) {
    if (!StringUtil.isNotEmpty(selectedPath) || perlHostData == null) {
      return;
    }
    PerlVersionManagerAdapter vmAdapter = createAdapter(selectedPath, perlHostData);
    List<String> distributions = vmAdapter.getDistributionsList();
    if (distributions == null) {
      return;
    }
    if (distributions.isEmpty()) {
      Messages.showInfoMessage(PerlBundle.message("perl.vm.perlbrew.empty.list.message"),
                               PerlBundle.message("perl.vm.perlbrew.empty.list.title"));
      return;
    }

    String installation;
    if (distributions.size() > 1) {
      Ref<Integer> selectionRef = Ref.create(-1);
      ApplicationManager.getApplication().invokeAndWait(
        () -> selectionRef.set(Messages.showChooseDialog(
          "",
          PerlBundle.message("perl.vm.perlbrew.choose.installation"),
          ArrayUtil.toStringArray(distributions),
          distributions.get(0),
          null)));
      if (selectionRef.get() == -1) {
        return;
      }
      installation = distributions.get(selectionRef.get());
    }
    else {
      installation = distributions.get(0);
    }

    createInterpreter(installation, vmAdapter, sdkConsumer);
  }

  /**
   * @return icon for this version manager
   */
  @NotNull
  public abstract Icon getIcon();

  /**
   * Creates an interpreter for version manager.
   *
   * @param distributionId distribution id
   * @param vmAdapter      adapter representing vm and host data
   * @param sdkConsumer    optional sdk consumer to invoke on success
   */
  public void createInterpreter(@NotNull String distributionId,
                                @NotNull PerlVersionManagerAdapter vmAdapter,
                                @Nullable Consumer<Sdk> sdkConsumer) {
    List<String> perlPath = vmAdapter.execWith(distributionId, "perl", PERL_LE, PERL_CTRL_X);
    if (perlPath == null || perlPath.size() != 1) {
      LOG.warn("Error getting perl location from interpreter. One line with path expected, got:\n" +
               (perlPath == null ? "nothing" : StringUtil.join(perlPath, "\n")));
      Notifications.Bus.notify(new Notification(
        PerlBundle.message("perl.vm.notification.group"),
        PerlBundle.message("perl.vm.error.getting.interpreter.path.title"),
        PerlBundle.message("perl.vm.error.getting.interpreter.path.message"),
        NotificationType.ERROR
      ));
      return;
    }
    PerlRealVersionManagerData versionManagerData = createData(vmAdapter, distributionId);
    PerlSdkType.createAndAddSdk(perlPath.get(0), vmAdapter.getHostData(), versionManagerData, sdkConsumer);
  }
}
