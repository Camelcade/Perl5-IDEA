/*
 * Copyright 2015-2017 Alexandr Evstigneev
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

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
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
import com.perl5.lang.perl.idea.sdk.host.PerlHostData;
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
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by ELI-HOME on 04-Jun-15.
 */
public class PerlSdkType extends SdkType {
  private static final Logger LOG = Logger.getInstance(PerlSdkType.class);
  public static final String PERL_SDK_TYPE_ID = "Perl5 Interpreter";
  public static final PerlSdkType INSTANCE = new PerlSdkType();


  private PerlSdkType() {
    super(PERL_SDK_TYPE_ID);
  }

  @Override
  public void saveAdditionalData(@NotNull SdkAdditionalData sdkAdditionalData, @NotNull Element element) {
    if (sdkAdditionalData instanceof PerlSdkAdditionalData) {
      ((PerlSdkAdditionalData)sdkAdditionalData).save(element);
    }
  }

  @NotNull
  @Override
  public PerlSdkAdditionalData loadAdditionalData(Element additional) {
    return PerlSdkAdditionalData.load(additional);
  }

  @Override
  public void setupSdkPaths(@NotNull Sdk sdk) {
    if (ApplicationManager.getApplication().isDispatchThread() && !ApplicationManager.getApplication().isHeadlessEnvironment()) {
      throw new RuntimeException("Do not call from EDT, refreshes FS");
    }
    String oldText = PerlRunUtil.setProgressText(PerlBundle.message("perl.progress.refreshing.inc", sdk.getName()));
    LOG.info("Refreshing @INC for " + sdk);
    PerlHostData hostData = PerlHostData.notNullFrom(sdk);

    List<String> pathsToRefresh = ContainerUtil.newArrayList();
    // syncing data if necessary
    List<String> incPaths = computeIncPaths(sdk);
    for (String hostPath : incPaths) {
      pathsToRefresh.add(hostData.syncPath(hostPath));

      File localBindDirectory = hostData.syncPath(PerlRunUtil.findLibsBin(new File(hostPath)));
      if (localBindDirectory != null) {
        pathsToRefresh.add(localBindDirectory.getPath());
      }
    }
    // additional bin dirs from version manager
    PerlVersionManagerData.notNullFrom(sdk).getBinDirsPath().forEach(it -> {
      File localPath = hostData.syncPath(it);
      if (localPath != null) {
        pathsToRefresh.add(localPath.getPath());
      }
    });

    // sdk home path
    File interpreterPath = new File(Objects.requireNonNull(sdk.getHomePath()));
    File localInterpreterDir = hostData.syncPath(interpreterPath.getParentFile());
    if (localInterpreterDir != null) {
      pathsToRefresh.add(localInterpreterDir.getPath());
    }

    List<VirtualFile> filesToRefresh = pathsToRefresh.stream()
      .map(it -> VfsUtil.findFileByIoFile(new File(it), true))
      .filter(Objects::nonNull)
      .collect(Collectors.toList());

    if (!filesToRefresh.isEmpty()) {
      PerlRunUtil.setProgressText(PerlBundle.message("perl.progress.refreshing.filesystem"));
      VfsUtil.markDirtyAndRefresh(false, true, true, filesToRefresh.toArray(VirtualFile.EMPTY_ARRAY));
    }

    hostData.syncHelpers();

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

    ApplicationManager.getApplication().invokeAndWait(sdkModificator::commitChanges);

    PerlRunUtil.setProgressText(oldText);
  }

  @Nullable
  @Override
  public String suggestHomePath() {
    throw new RuntimeException("unsupported");
  }

  @Nullable
  @Override
  public AdditionalDataConfigurable createAdditionalDataConfigurable(@NotNull SdkModel sdkModel,
                                                                     @NotNull SdkModificator sdkModificator) {
    return null;
  }

  @NotNull
  @Override
  public String getPresentableName() {
    return PerlBundle.message("perl.config.interpreter.title");
  }

  @NotNull
  @Override
  public String suggestSdkName(String currentSdkName, String sdkHome) {
    throw new RuntimeException("Should not be invoked");
  }

  @Nullable
  @Override
  public String getVersionString(@NotNull Sdk sdk) {
    return ObjectUtils.doIfNotNull(
      getPerlVersionDescriptor(sdk.getHomePath(), PerlHostData.notNullFrom(sdk), PerlVersionManagerData.notNullFrom(sdk)),
      VersionDescriptor::getVersionString);
  }

  @NotNull
  private static List<String> computeIncPaths(@NotNull Sdk sdk) {
    return ContainerUtil.filter(PerlRunUtil.getOutputFromPerl(sdk, PerlRunUtil.PERL_LE, "print for @INC"), it -> !".".equals(it));
  }

  @Override
  public boolean isValidSdkHome(String sdkHome) {
    throw new RuntimeException("Unsupported");
  }

  @NotNull
  @Override
  public String adjustSelectedSdkHome(@NotNull String homePath) {
    File file = new File(homePath);
    if (file.isDirectory()) {
      return homePath;
    }
    File parentFile = file.getParentFile();
    return parentFile != null && parentFile.isDirectory() ? parentFile.getPath() : homePath;
  }

  @NotNull
  public String getPerlExecutableName() {
    return SystemInfo.isWindows ? "perl.exe" : "perl";
  }

  @Override
  public Icon getIcon() {
    return PerlIcons.PERL_LANGUAGE_ICON;
  }

  @NotNull
  @Override
  public Icon getIconForAddAction() {
    return getIcon();
  }

  @NotNull
  private static String suggestSdkName(@Nullable VersionDescriptor descriptor,
                                       @NotNull PerlHostData hostData,
                                       @NotNull PerlVersionManagerData versionManagerData) {
    return StringUtil.capitalize(hostData.getShortName()) + ", " +
           StringUtil.capitalize(versionManagerData.getShortName()) + ": " +
           "Perl" + (descriptor == null ? "" : " " + descriptor.version);
  }

  @Contract("null, _,_->null")
  @Nullable
  public static VersionDescriptor getPerlVersionDescriptor(@Nullable String interpreterPath,
                                                           @NotNull PerlHostData hostData,
                                                           @NotNull PerlVersionManagerData versionManagerData) {
    if (StringUtil.isEmpty(interpreterPath)) {
      return null;
    }
    List<String> versionLines = PerlRunUtil.getOutputFromProgram(hostData, versionManagerData, interpreterPath, "-v");

    return versionLines.isEmpty() ? null : VersionDescriptor.create(versionLines.get(0));
  }


  /**
   * @deprecated use {@link #INSTANCE} instead
   */
  @Deprecated // use INSTANCE instead
  @NotNull
  public static PerlSdkType getInstance() {
    return INSTANCE;
  }


  public static void createAndAddSdk(@NotNull String interpreterPath,
                                     @NotNull PerlHostData hostData,
                                     @NotNull PerlVersionManagerData versionManagerData,
                                     @Nullable Consumer<Sdk> sdkConsumer) {
    createSdk(interpreterPath, hostData, versionManagerData, sdk -> {
      PerlSdkTable.getInstance().addJdk(sdk);
      if (sdkConsumer != null) {
        sdkConsumer.accept(sdk);
      }
    });
  }

  /**
   * Creates and adds new Perl SDK
   *
   * @param interpreterPath interpreter path
   * @param sdkConsumer     created sdk consumer
   */
  public static void createSdk(@NotNull String interpreterPath,
                               @NotNull PerlHostData hostData,
                               @NotNull PerlVersionManagerData versionManagerData,
                               @NotNull Consumer<Sdk> sdkConsumer) {
    VersionDescriptor perlVersionDescriptor = PerlSdkType.getPerlVersionDescriptor(interpreterPath, hostData, versionManagerData);
    if (perlVersionDescriptor == null) {
      ApplicationManager.getApplication().invokeLater(
        () -> Messages.showErrorDialog("Failed to fetch perl version, see logs for more details", "Failed to Create Interpreter"));
      return;
    }
    String newSdkName = SdkConfigurationUtil.createUniqueSdkName(
      suggestSdkName(perlVersionDescriptor, hostData, versionManagerData), PerlSdkTable.getInstance().getInterpreters());

    final ProjectJdkImpl newSdk = PerlSdkTable.getInstance().createSdk(newSdkName);
    newSdk.setHomePath(interpreterPath);

    PerlImplementationData<?, ?> implementationData = PerlImplementationHandler.createData(
      interpreterPath, hostData, versionManagerData);

    if (implementationData == null) {
      return;
    }

    newSdk.setVersionString(perlVersionDescriptor == null ? null : perlVersionDescriptor.getVersionString());
    newSdk.setSdkAdditionalData(new PerlSdkAdditionalData(hostData, versionManagerData, implementationData));

    INSTANCE.setupSdkPaths(newSdk);
    sdkConsumer.accept(newSdk);
  }

  private static class VersionDescriptor {
    private static final Pattern perlVersionStringPattern = Pattern.compile("\\(([^)]+?)\\) built for (.+)");

    private String version;
    private String platform;

    @Nullable
    private static VersionDescriptor create(@Nullable String versionString) {
      if (versionString == null) {
        return null;
      }
      Matcher m = perlVersionStringPattern.matcher(versionString);
      if (!m.find()) {
        return null;
      }
      VersionDescriptor result = new VersionDescriptor();
      result.version = m.group(1);
      result.platform = m.group(2);
      return result;
    }

    @NotNull
    public String getVersionString() {
      return PerlBundle.message("perl.version.string", version, platform);
    }
  }
}

