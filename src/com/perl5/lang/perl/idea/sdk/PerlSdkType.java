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

import com.intellij.openapi.projectRoots.*;
import com.intellij.openapi.projectRoots.impl.PerlSdkTable;
import com.intellij.openapi.projectRoots.impl.ProjectJdkImpl;
import com.intellij.openapi.projectRoots.impl.SdkConfigurationUtil;
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.perl5.PerlBundle;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.idea.sdk.host.PerlHostData;
import com.perl5.lang.perl.idea.sdk.implementation.PerlImplementationData;
import com.perl5.lang.perl.idea.sdk.implementation.PerlImplementationHandler;
import com.perl5.lang.perl.idea.sdk.versionManager.PerlVersionManagerData;
import com.perl5.lang.perl.util.PerlRunUtil;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ELI-HOME on 04-Jun-15.
 */
public class PerlSdkType extends SdkType {
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
    SdkModificator sdkModificator = sdk.getSdkModificator();
    for (String perlLibPath : getINCPaths(sdk)) {
      File libDir = new File(perlLibPath);

      if (libDir.exists() && libDir.isDirectory()) {
        VirtualFile virtualDir = LocalFileSystem.getInstance().findFileByIoFile(libDir);
        if (virtualDir != null) {
          sdkModificator.addRoot(virtualDir, OrderRootType.CLASSES);
        }
      }
    }

    sdkModificator.commitChanges();
  }

  @NotNull
  private static List<String> getINCPaths(@NotNull Sdk sdk) {
    List<String> perlLibPaths = new ArrayList<>();
    for (String path : PerlRunUtil.getOutputFromProgram(
      sdk.getHomePath(),
      "-le",
      "print for @INC"
    )) {
      if (!".".equals(path)) {
        perlLibPaths.add(path);
      }
    }
    return perlLibPaths;
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

  @Nullable
  @Override
  // fixme move this to the hostHandler
  public String suggestHomePath() {
    String perlPath = PerlRunUtil.getPathFromPerl();

    if (perlPath != null) {
      return perlPath;
    }

    if (SystemInfo.isLinux || SystemInfo.isUnix || SystemInfo.isFreeBSD) {
      return "/usr/bin/" + getPerlExecutableName();
    }

    return FileUtil.join(System.getenv("PERL_HOME"), getPerlExecutableName());
  }

  @NotNull
  @Override
  public String suggestSdkName(String currentSdkName, String sdkHome) {
    VersionDescriptor descriptor = getPerlVersionDescriptor(sdkHome);
    return "Perl" + (descriptor == null ? "" : " " + descriptor.version);
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

  @Nullable
  @Override
  public String getVersionString(@NotNull Sdk sdk) {
    String sdkHomePath = sdk.getHomePath();
    if (sdkHomePath == null) {
      return null;
    }
    VersionDescriptor descriptor = getPerlVersionDescriptor(sdkHomePath);
    return descriptor == null ? null : PerlBundle.message("perl.version.string", descriptor.version, descriptor.platform);
  }

  @Nullable
  private VersionDescriptor getPerlVersionDescriptor(@NotNull String sdkHomePath) {
    List<String> versionLines = PerlRunUtil.getOutputFromProgram(sdkHomePath, "-v");

    if (versionLines.isEmpty()) {
      return null;
    }
    return VersionDescriptor.create(versionLines.get(0));
  }


  /**
   * @deprecated use {@link #INSTANCE} instead
   */
  @Deprecated // use INSTANCE instead
  @NotNull
  public static PerlSdkType getInstance() {
    return INSTANCE;
  }

  /**
   * Creates and adds new Perl SDK
   *
   * @param interpreterPath interpreter path
   * @param successCallback optional callback to invoke on success
   */
  public static void createAndAddSdk(@NotNull String interpreterPath,
                                     @NotNull PerlHostData hostData,
                                     @NotNull PerlVersionManagerData versionManagerData,
                                     @Nullable Runnable successCallback) {
    PerlSdkType sdkType = INSTANCE;
    String newSdkName = SdkConfigurationUtil.createUniqueSdkName(
      sdkType, interpreterPath, Arrays.asList(PerlSdkTable.getInstance().getAllJdks()));

    final ProjectJdkImpl newSdk = PerlSdkTable.getInstance().createSdk(newSdkName);
    newSdk.setHomePath(interpreterPath);

    PerlImplementationData<?, ?> implementationData = PerlImplementationHandler.createData(
      interpreterPath, hostData, versionManagerData);

    if (implementationData == null) {
      return;
    }

    newSdk.setSdkAdditionalData(new PerlSdkAdditionalData(hostData, versionManagerData, implementationData));

    sdkType.setupSdkPaths(newSdk);
    // should we check for version string?
    if (successCallback != null) {
      successCallback.run();
    }
    PerlSdkTable.getInstance().addJdk(newSdk);
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
  }
}

