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
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.perl5.PerlBundle;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.util.PerlRunUtil;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ELI-HOME on 04-Jun-15.
 */
public class PerlSdkType extends SdkType {
  public static final String PERL_SDK_TYPE_ID = "Perl5 Interpreter";

  public static final Pattern perlVersionStringPattern = Pattern.compile("\\(([^)]+?)\\) built for (.+)");

  public PerlSdkType() {
    super(PERL_SDK_TYPE_ID);
  }

  @Override
  public void saveAdditionalData(@NotNull SdkAdditionalData sdkAdditionalData, @NotNull Element element) {

  }

  @Override
  public void setupSdkPaths(@NotNull Sdk sdk) {
    SdkModificator sdkModificator = sdk.getSdkModificator();

    for (String perlLibPath : getINCPaths(sdk.getHomePath())) {
      File libDir = new File(perlLibPath);

      if (libDir.exists() && libDir.isDirectory()) {
        VirtualFile virtualDir = LocalFileSystem.getInstance().findFileByIoFile(libDir);
        if (virtualDir != null) {
          sdkModificator.addRoot(virtualDir, OrderRootType.SOURCES);
          sdkModificator.addRoot(virtualDir, OrderRootType.CLASSES);
        }
      }
    }

    sdkModificator.commitChanges();
  }

  public List<String> getINCPaths(String sdkHomePath) {
    String executablePath = getExecutablePath(sdkHomePath);
    List<String> perlLibPaths = new ArrayList<>();
    for (String path : PerlRunUtil.getDataFromProgram(
      executablePath,
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
  public String suggestHomePath() {
    String perlPath = PerlRunUtil.getPathFromPerl();

    if (perlPath != null) {
      return perlPath;
    }

    if (SystemInfo.isLinux || SystemInfo.isUnix || SystemInfo.isFreeBSD) {
      return "/usr/bin/";
    }

    return System.getenv("PERL_HOME");
  }

  @Override
  public String suggestSdkName(String currentSdkName, String sdkHome) {
    return "Perl " + getPerlVersionString(sdkHome);
  }

  @Override
  public boolean isValidSdkHome(String sdkHome) {
    File f = new File(getExecutablePath(sdkHome));
    return f.exists();
  }

  @NotNull
  public String getExecutablePath(@NotNull String sdkHome) {
    if (!(sdkHome.endsWith("/") && sdkHome.endsWith("\\"))) {
      sdkHome += File.separator;
    }

    if (SystemInfo.isWindows) {
      return sdkHome + "perl.exe";
    }
    else {
      return sdkHome + "perl";
    }
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
    return getPerlVersionString(sdk);
  }

  public String getPerlVersionString(@NotNull Sdk sdk) {
    String sdkHomePath = sdk.getHomePath();
    if (sdkHomePath != null) {
      return getPerlVersionString(sdkHomePath);
    }
    else {
      return null;
    }
  }

  public String getPerlVersionString(@NotNull String sdkHomePath) {
    List<String> versionLines = PerlRunUtil.getDataFromProgram(getExecutablePath(sdkHomePath), "-v");

    if (!versionLines.isEmpty()) {
      Matcher m = perlVersionStringPattern.matcher(versionLines.get(0));

      if (m.find()) {
        return m.group(1) + " (" + m.group(2) + ")";
      }

      return "Unknown version, please report a bug";
    }

    return "missing executable";
  }

  @NotNull
  public static PerlSdkType getInstance() {
    return SdkType.findInstance(PerlSdkType.class);
  }
}
