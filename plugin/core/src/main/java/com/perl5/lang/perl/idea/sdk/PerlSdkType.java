/*
 * Copyright 2015-2025 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.sdk;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.*;
import com.intellij.openapi.projectRoots.impl.PerlSdkTable;
import com.intellij.openapi.projectRoots.impl.ProjectJdkImpl;
import com.intellij.openapi.projectRoots.impl.SdkConfigurationUtil;
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.ObjectUtils;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.PerlBundle;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.idea.project.PerlProjectManager;
import com.perl5.lang.perl.idea.sdk.host.PerlHostData;
import com.perl5.lang.perl.idea.sdk.host.PerlHostFileTransfer;
import com.perl5.lang.perl.idea.sdk.implementation.PerlImplementationData;
import com.perl5.lang.perl.idea.sdk.implementation.PerlImplementationHandler;
import com.perl5.lang.perl.idea.sdk.versionManager.PerlVersionManagerData;
import com.perl5.lang.perl.util.PerlRunUtil;
import org.jdom.Element;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;


public class PerlSdkType extends SdkType {
  private static final Logger LOG = Logger.getInstance(PerlSdkType.class);
  public static final String PERL_SDK_TYPE_ID = "Perl5 Interpreter";
  public static final PerlSdkType INSTANCE = new PerlSdkType();


  private PerlSdkType() {
    super(PERL_SDK_TYPE_ID);
  }

  @Override
  public void saveAdditionalData(@NotNull SdkAdditionalData sdkAdditionalData, @NotNull Element element) {
    ((SaveAwareSdkAdditionalData)sdkAdditionalData).save(element);
  }

  @Override
  public @NotNull SdkAdditionalData loadAdditionalData(@NotNull Element additional) {
    return PerlSdkAdditionalData.load(additional);
  }

  @Override
  public void setupSdkPaths(@NotNull Sdk sdk) {
    if (ApplicationManager.getApplication().isDispatchThread() && !ApplicationManager.getApplication().isHeadlessEnvironment()) {
      throw new RuntimeException("Do not call from EDT, refreshes FS");
    }
    String oldText = PerlRunUtil.setProgressText(PerlBundle.message("perl.progress.refreshing.inc", sdk.getName()));
    LOG.info("Refreshing @INC for " + sdk);
    PerlHostData<?, ?> hostData = PerlHostData.notNullFrom(sdk);
    List<String> pathsToRefresh = new ArrayList<>();
    // syncing data if necessary
    List<String> incPaths = computeIncPaths(sdk);
    List<Exception> exceptions = new ArrayList<>();

    try (PerlHostFileTransfer<?> fileTransfer = hostData.getFileTransfer()) {
      Consumer<File> downloader = it -> syncAndCollectException(fileTransfer, it, pathsToRefresh, exceptions, false);
      Consumer<File> binStubber = it -> syncAndCollectException(fileTransfer, it, pathsToRefresh, exceptions, true);
      for (String hostPath : incPaths) {
        downloader.accept(new File(hostPath));
        binStubber.accept(PerlRunUtil.findLibsBin(new File(hostPath)));
      }
      // additional bin dirs from version manager
      PerlVersionManagerData.notNullFrom(sdk).getBinDirsPath().forEach(binStubber);

      // sdk home path
      File interpreterPath = new File(Objects.requireNonNull(PerlProjectManager.getInterpreterPath(sdk)));
      binStubber.accept(interpreterPath.getParentFile());

      List<VirtualFile> filesToRefresh = pathsToRefresh.stream()
        .map(it -> VfsUtil.findFileByIoFile(new File(it), true))
        .filter(Objects::nonNull)
        .toList();

      if (!filesToRefresh.isEmpty()) {
        PerlRunUtil.setProgressText(PerlBundle.message("perl.progress.refreshing.filesystem"));
        VfsUtil.markDirtyAndRefresh(false, true, true, filesToRefresh.toArray(VirtualFile.EMPTY_ARRAY));
      }

      try {
        fileTransfer.syncHelpers();
      }
      catch (IOException e) {
        exceptions.add(e);
      }

      if (!exceptions.isEmpty()) {
        int copiedFiles = filesToRefresh.size();
        int errorsNumber = exceptions.size();
        Notifications.Bus.notify(new Notification(
          PerlBundle.message("perl.sync.notification.group"),
          PerlBundle.message("perl.sync.notification.title"),
          PerlBundle.message("perl.sync.notification.body",
                             copiedFiles, copiedFiles + errorsNumber, errorsNumber,
                             StringUtil.pluralize(PerlBundle.message("perl.sync.notification.pluralize"), errorsNumber)),
          NotificationType.ERROR));
        exceptions.forEach(LOG::warn);
      }
    }
    catch (IOException e) {
      LOG.warn("Error closing transfer for " + sdk, e);
    }

    // updating sdk
    SdkModificator sdkModificator = sdk.getSdkModificator();
    sdkModificator.removeAllRoots();
    for (String hostPath : incPaths) {
      String localPath = hostData.getLocalPath(hostPath);
      if (localPath == null) {
        continue;
      }
      File libDir = new File(localPath);

      if (libDir.exists() && libDir.isDirectory()) {
        VirtualFile virtualDir = VfsUtil.findFileByIoFile(libDir, true);
        if (virtualDir != null) {
          sdkModificator.addRoot(virtualDir, OrderRootType.CLASSES);
        }
      }
    }

    ApplicationManager.getApplication().invokeAndWait(() -> WriteAction.run(sdkModificator::commitChanges));
    PerlRunUtil.setProgressText(oldText);
  }

  /**
   * Copying {@code fileToCopy} using the {@code fileTransfer} and collecting local path of copied file to the {@code pathsToRefresh}. In
   * case exception been thrown by the {@code fileTransfer}, it's collected to the {@code exceptionsThrown}
   *
   * @param if set, only empty stubs with same names going to be created, instead of actual copying the file.
   */
  private static void syncAndCollectException(@NotNull PerlHostFileTransfer<?> fileTransfer,
                                              @Nullable File fileToCopy,
                                              @NotNull List<String> pathsToRefresh,
                                              @NotNull List<Exception> exceptionsThrown,
                                              boolean stubOnly) {
    if (fileToCopy == null) {
      return;
    }
    try {
      if (stubOnly) {
        pathsToRefresh.add(fileTransfer.stubFiles(fileToCopy).getPath());
      }
      else {
        pathsToRefresh.add(fileTransfer.syncFile(fileToCopy).getPath());
      }
    }
    catch (IOException e) {
      exceptionsThrown.add(e);
    }
  }


  @Override
  public @Nullable String suggestHomePath() {
    throw new RuntimeException("unsupported");
  }

  @Override
  public @Nullable AdditionalDataConfigurable createAdditionalDataConfigurable(@NotNull SdkModel sdkModel,
                                                                               @NotNull SdkModificator sdkModificator) {
    return null;
  }

  @Override
  public @NotNull String getPresentableName() {
    return PerlBundle.message("perl.config.interpreter.title");
  }

  @Override
  public @NotNull String suggestSdkName(String currentSdkName, @NotNull String sdkHome) {
    throw new RuntimeException("Should not be invoked");
  }

  @Override
  public @Nullable String getVersionString(@NotNull Sdk sdk) {
    return ObjectUtils.doIfNotNull(
      getPerlVersionDescriptor(PerlProjectManager.getInterpreterPath(sdk),
                               PerlHostData.notNullFrom(sdk),
                               PerlVersionManagerData.notNullFrom(sdk)),
      VersionDescriptor::getVersionString);
  }

  private static @NotNull List<String> computeIncPaths(@NotNull Sdk sdk) {
    return ContainerUtil.filter(PerlRunUtil.getOutputFromPerl(sdk, PerlRunUtil.PERL_LE, "print for @INC"), it -> !".".equals(it));
  }

  @Override
  public boolean isValidSdkHome(@NotNull String sdkHome) {
    throw new RuntimeException("Unsupported");
  }

  @Override
  public @NotNull String adjustSelectedSdkHome(@NotNull String homePath) {
    File file = new File(homePath);
    if (file.isDirectory()) {
      return homePath;
    }
    File parentFile = file.getParentFile();
    return parentFile != null && parentFile.isDirectory() ? parentFile.getPath() : homePath;
  }

  public @NotNull String getPerlExecutableName() {
    return SystemInfo.isWindows ? "perl.exe" : "perl";
  }

  @Override
  public Icon getIcon() {
    return PerlIcons.PERL_LANGUAGE_ICON;
  }

  private static @NotNull String suggestSdkName(@Nullable VersionDescriptor descriptor,
                                                @NotNull PerlHostData<?, ?> hostData,
                                                @NotNull PerlVersionManagerData<?, ?> versionManagerData) {
    return StringUtil.capitalize(hostData.getShortName()) + ", " +
           StringUtil.capitalize(versionManagerData.getShortName()) + ": " +
           "Perl" + (descriptor == null ? "" : " " + descriptor.version);
  }

  @Contract("null, _,_->null")
  public static @Nullable VersionDescriptor getPerlVersionDescriptor(@Nullable String interpreterPath,
                                                                     @NotNull PerlHostData<?, ?> hostData,
                                                                     @NotNull PerlVersionManagerData<?, ?> versionManagerData) {
    if (StringUtil.isEmpty(interpreterPath)) {
      return null;
    }

    return VersionDescriptor.create(PerlRunUtil.getOutputFromProgram(
      hostData, versionManagerData,
      interpreterPath, "-MConfig", "-e", "print q{perl}.chr(10);do {print;print chr(10)} for @Config{qw/version archname/}"));
  }


  /**
   * @deprecated use {@link #INSTANCE} instead
   */
  // use INSTANCE instead
  @Deprecated
  public static @NotNull PerlSdkType getInstance() {
    return INSTANCE;
  }


  public static void createAndAddSdk(@NotNull String interpreterPath,
                                     @NotNull PerlHostData<?, ?> hostData,
                                     @NotNull PerlVersionManagerData<?, ?> versionManagerData,
                                     @Nullable Consumer<Sdk> sdkConsumer,
                                     @Nullable Project project) {
    createSdk(interpreterPath, hostData, versionManagerData, sdk -> {
      PerlSdkTable.getInstance().addJdk(sdk);
      if (sdkConsumer != null) {
        sdkConsumer.accept(sdk);
      }
      PerlRunUtil.refreshSdkDirs(sdk, project);
    });
  }

  /**
   * Creates and adds new Perl SDK
   *
   * @param interpreterPath interpreter path
   * @param sdkConsumer     created sdk consumer
   */
  public static void createSdk(@NotNull String interpreterPath,
                               @NotNull PerlHostData<?, ?> hostData,
                               @NotNull PerlVersionManagerData<?, ?> versionManagerData,
                               @NotNull Consumer<Sdk> sdkConsumer) {
    VersionDescriptor perlVersionDescriptor = PerlSdkType.getPerlVersionDescriptor(interpreterPath, hostData, versionManagerData);
    if (perlVersionDescriptor == null) {
      ApplicationManager.getApplication().invokeLater(
        () -> Messages.showErrorDialog(PerlBundle.message("dialog.message.failed.to.fetch.perl.version.see.logs.for.more.details"),
                                       PerlBundle.message("dialog.title.failed.to.create.interpreter")));
      return;
    }
    String newSdkName = SdkConfigurationUtil.createUniqueSdkName(
      suggestSdkName(perlVersionDescriptor, hostData, versionManagerData), PerlSdkTable.getInstance().getInterpreters());

    final ProjectJdkImpl newSdk = PerlSdkTable.getInstance().createSdk(newSdkName);
    var sdkModificator = newSdk.getSdkModificator();
    sdkModificator.setHomePath(interpreterPath);

    PerlImplementationData<?, ?> implementationData = PerlImplementationHandler.createData(
      interpreterPath, hostData, versionManagerData);

    if (implementationData == null) {
      return;
    }

    sdkModificator.setVersionString(perlVersionDescriptor.getVersionString());
    sdkModificator.setSdkAdditionalData(new PerlSdkAdditionalData(hostData, versionManagerData, implementationData, PerlConfig.empty()));
    ApplicationManager.getApplication().invokeAndWait(() -> WriteAction.run(sdkModificator::commitChanges));
    PerlConfig.init(newSdk);
    sdkConsumer.accept(newSdk);
  }

  private static class VersionDescriptor {
    private String version;
    private String platform;

    private static @Nullable VersionDescriptor create(@NotNull List<String> perlResponse) {
      if (perlResponse.size() != 3 || !StringUtil.equals("perl", perlResponse.get(0))) {
        LOG.warn("Got inappropriate response from perl: " + StringUtil.join(perlResponse, "\n"));
        return null;
      }

      VersionDescriptor result = new VersionDescriptor();
      result.version = perlResponse.get(1);
      result.platform = perlResponse.get(2);
      return result;
    }

    public @NotNull String getVersionString() {
      return PerlBundle.message("perl.version.string", version, platform);
    }
  }
}

