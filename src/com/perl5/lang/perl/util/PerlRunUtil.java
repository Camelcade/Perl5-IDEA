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

package com.perl5.lang.perl.util;

import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.util.ExecUtil;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.SdkTypeId;
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.idea.project.PerlProjectManager;
import com.perl5.lang.perl.idea.sdk.PerlSdkType;
import gnu.trove.THashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Created by hurricup on 26.04.2016.
 */
public class PerlRunUtil {
  public static final String PERL5OPT = "PERL5OPT";

  @Nullable
  public static GeneralCommandLine getPerlCommandLine(@NotNull Project project,
                                                      @Nullable VirtualFile scriptFile,
                                                      String... perlParameters) {
    String perlPath = PerlProjectManager.getSdkPath(project, scriptFile);
    return perlPath == null ? null : getPerlCommandLine(project, perlPath, scriptFile, perlParameters);
  }

  /**
   * Creates or appends additionalOpts separated by space to PERL5OPT environment variable
   *
   * @param originalMap    original environment
   * @param additionalOpts options to add
   * @return patched environment
   **/
  @NotNull
  public static Map<String, String> withPerl5Opts(@NotNull Map<String, String> originalMap, @NotNull String... additionalOpts) {
    THashMap<String, String> newMap = new THashMap<>(originalMap);
    String currentOpt = originalMap.get(PERL5OPT);

    ArrayList<String> options = new ArrayList<>();
    if (StringUtil.isNotEmpty(currentOpt)) {
      options.add(currentOpt);
    }
    options.addAll(Arrays.asList(additionalOpts));

    newMap.put(PERL5OPT, StringUtil.join(options, " "));
    return newMap;
  }


  @NotNull
  public static GeneralCommandLine getPerlCommandLine(@NotNull Project project,
                                                      @NotNull String perlDirectory,
                                                      @Nullable VirtualFile scriptFile,
                                                      String... perlParameters) {
    GeneralCommandLine commandLine = new GeneralCommandLine();
    String executablePath = PerlSdkType.INSTANCE.getExecutablePath(perlDirectory);
    commandLine.setExePath(FileUtil.toSystemDependentName(executablePath));
    for (VirtualFile libRoot : PerlProjectManager.getInstance(project).getModulesLibraryRoots()) {
      commandLine.addParameter("-I" + libRoot.getCanonicalPath());
    }

    commandLine.addParameters(perlParameters);

    if (scriptFile != null) {
      commandLine.addParameter(FileUtil.toSystemDependentName(scriptFile.getPath()));
    }
    return commandLine;
  }

  /**
   * Attempts to find a script in project's perl sdk and shows notification to user with suggestion to install a library if
   * script was not found
   *
   * @param project     to get sdk from
   * @param scriptName  script name
   * @param libraryName library to suggest if script was not found; notification won't be shown if lib is null/empty
   * @return script's virtual file if any
   */
  @Nullable
  public static VirtualFile findLibraryScriptWithNotification(@NotNull Project project,
                                                              @NotNull String scriptName,
                                                              @Nullable String libraryName) {
    Sdk sdk = PerlProjectManager.getSdkWithNotification(project);
    if (sdk == null) {
      return null;
    }
    return findLibraryScriptWithNotification(
      sdk,
      scriptName,
      libraryName
    );
  }


  /**
   * Attempts to find a script in project's perl sdk and shows notification to user with suggestion to install a library if
   * script was not found
   *
   * @param sdk         to find script in
   * @param scriptName  script name
   * @param libraryName library to suggest if script was not found, notification won't be shown if lib is null/empty
   * @return script's virtual file if any
   */
  @Nullable
  public static VirtualFile findLibraryScriptWithNotification(@NotNull Sdk sdk,
                                                              @NotNull String scriptName,
                                                              @Nullable String libraryName) {
    VirtualFile scriptFile = findScript(sdk, scriptName);
    if (scriptFile != null) {
      return scriptFile;
    }

    if (StringUtil.isEmpty(libraryName)) {
      return null;
    }

    Notification notification = new Notification(
      PerlBundle.message("perl.missing.library.notification"),
      PerlBundle.message("perl.missing.library.notification.title", libraryName),
      PerlBundle.message("perl.missing.library.notification.message"),
      NotificationType.ERROR
    );
    // fixme add installation action here, see #1645
    Notifications.Bus.notify(notification);

    return null;
  }

  /**
   * Attempts to find a script by name in perl's libraries path
   *
   * @param project    project to get sdk from
   * @param scriptName script name to find
   * @return script's virtual file if available
   **/
  @Nullable
  public static VirtualFile findScript(@Nullable Project project, @Nullable String scriptName) {
    return findScript(PerlProjectManager.getSdk(project), scriptName);
  }

  /**
   * Attempts to find a script by name in perl's libraries path
   *
   * @param sdk        perl sdk to search in
   * @param scriptName script name to find
   * @return script's virtual file if available
   **/
  @Nullable
  public static VirtualFile findScript(@Nullable Sdk sdk, @Nullable String scriptName) {
    if (sdk == null || scriptName == null) {
      return null;
    }
    ApplicationManager.getApplication().assertReadAccessAllowed();
    return getBinDirectories(sdk).stream().map(root -> root.findChild(scriptName)).filter(Objects::nonNull).findFirst().orElse(null);
  }


  /**
   * @return list of perl bin directories where script from library may be located
   **/
  @NotNull
  public static List<VirtualFile> getBinDirectories(@NotNull Sdk sdk) {
    ApplicationManager.getApplication().assertReadAccessAllowed();
    SdkTypeId sdkType = sdk.getSdkType();
    if (!(sdkType instanceof PerlSdkType)) {
      throw new IllegalArgumentException("Got non-perl sdk: " + sdk);
    }
    VirtualFile[] roots = sdk.getRootProvider().getFiles(OrderRootType.CLASSES);
    List<VirtualFile> result = new ArrayList<>();
    for (VirtualFile root : roots) {
      if (root.isValid()) {
        VirtualFile binDir = root.findFileByRelativePath("../bin");
        if (binDir != null && binDir.isValid() && binDir.isDirectory()) {
          result.add(binDir);
        }
      }
    }
    return result;
  }

  @Nullable
  public static String getPathFromPerl() {
    List<String> perlPathLines = getOutputFromProgram("perl", "-le", "print $^X");

    if (perlPathLines.size() == 1) {
      int perlIndex = perlPathLines.get(0).lastIndexOf("perl");
      if (perlIndex > 0) {
        return perlPathLines.get(0).substring(0, perlIndex);
      }
    }
    return null;
  }

  @NotNull
  public static List<String> getOutputFromProgram(String... command) {
    try {
      GeneralCommandLine commandLine = new GeneralCommandLine(command);
      return ExecUtil.execAndGetOutput(commandLine).getStdoutLines();
    }
    catch (Exception e) {
      //			throw new IncorrectOperationException("Error executing external perl, please report to plugin developers: " + e.getMessage());
      return Collections.emptyList();
    }
  }
}
