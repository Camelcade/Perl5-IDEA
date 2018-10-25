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

import com.intellij.execution.ExecutionException;
import com.intellij.execution.ExecutionManager;
import com.intellij.execution.Executor;
import com.intellij.execution.executors.DefaultRunExecutor;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.process.ProcessTerminatedListener;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.execution.ui.RunContentDescriptor;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.SdkTypeId;
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.ObjectUtils;
import com.perl5.PerlBundle;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.idea.execution.PerlCommandLine;
import com.perl5.lang.perl.idea.execution.PerlRunConsole;
import com.perl5.lang.perl.idea.project.PerlProjectManager;
import com.perl5.lang.perl.idea.sdk.PerlSdkType;
import com.perl5.lang.perl.idea.sdk.host.PerlHostData;
import com.perl5.lang.perl.idea.sdk.versionManager.PerlVersionManagerData;
import gnu.trove.THashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Stream;

/**
 * Created by hurricup on 26.04.2016.
 */
public class PerlRunUtil {
  public static final String PERL_I = "-I";
  public static final String PERL_LE = "-le";
  public static final String PERL_CTRL_X = "print $^X";
  public static final String PERL5OPT = "PERL5OPT";
  private static final Logger LOG = Logger.getInstance(PerlRunUtil.class);

  /**
   * Builds non-patched perl command line for {@code project}'s sdk (without patching by version manager)
   *
   * @return command line if perl support for project or scriptFile is enabled
   */
  @Nullable
  public static PerlCommandLine getPerlCommandLine(@NotNull Project project,
                                                   @Nullable VirtualFile scriptFile,
                                                   String... perlParameters) {
    return getPerlCommandLine(
      project, PerlProjectManager.getSdk(project, scriptFile), scriptFile, Arrays.asList(perlParameters), Collections.emptyList());
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


  /**
   * Builds non-patched perl command line (without patching by version manager)
   *
   * @return new perl command line or null if sdk is missing or corrupted
   */
  @Nullable
  public static PerlCommandLine getPerlCommandLine(@NotNull Project project,
                                                   @Nullable Sdk perlSdk,
                                                      @Nullable VirtualFile scriptFile,
                                                      @NotNull List<String> perlParameters,
                                                      @NotNull List<String> scriptParameters) {
    String interpreterPath = ObjectUtils.doIfNotNull(perlSdk, Sdk::getHomePath);
    if (StringUtil.isEmpty(interpreterPath)) {
      return null;
    }
    PerlCommandLine commandLine = new PerlCommandLine(perlSdk);
    commandLine.setExePath(FileUtil.toSystemDependentName(interpreterPath));
    for (VirtualFile libRoot : PerlProjectManager.getInstance(project).getModulesLibraryRoots()) {
      commandLine.addParameter(PERL_I + libRoot.getCanonicalPath());
    }

    commandLine.addParameters(perlParameters);

    if (scriptFile != null) {
      commandLine.addParameter(FileUtil.toSystemDependentName(scriptFile.getPath()));
    }

    commandLine.addParameters(scriptParameters);

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
      PerlBundle.message("perl.missing.library.notification.message", libraryName),
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
    ApplicationManager.getApplication().assertReadAccessAllowed();
    return sdk == null || StringUtil.isEmpty(scriptName) ? null : getBinDirectories(sdk)
      .map(root -> root.findChild(scriptName))
      .filter(Objects::nonNull)
      .findFirst().orElse(null);
  }


  /**
   * @return list of perl bin directories where script from library may be located
   **/
  @NotNull
  public static Stream<VirtualFile> getBinDirectories(@NotNull Sdk sdk) {
    ApplicationManager.getApplication().assertReadAccessAllowed();
    SdkTypeId sdkType = sdk.getSdkType();
    if (!(sdkType instanceof PerlSdkType)) {
      throw new IllegalArgumentException("Got non-perl sdk: " + sdk);
    }
    return Arrays.stream(sdk.getRootProvider().getFiles(OrderRootType.CLASSES))
      .map(PerlRunUtil::findLibsBin)
      .filter(Objects::nonNull)
      .distinct();
  }

  /**
   * Finds a bin dir for a library root
   *
   * @return bin root or null if not available
   * @implSpec for now we are traversing tree up to lib dir and resolving {@code ../bin}
   */
  @Nullable
  private static VirtualFile findLibsBin(@Nullable VirtualFile libraryRoot) {
    if (libraryRoot == null || !libraryRoot.isValid()) {
      return null;
    }
    if ("lib".equals(libraryRoot.getName())) {
      return libraryRoot.findFileByRelativePath("../bin");
    }
    return findLibsBin(libraryRoot.getParent());
  }

  /**
   * Requests perl path using introspection variable $^X: {@code perl -le print $^X}
   * @param hostData host to execute command on
   * @return version string or null if response was wrong
   */
  @Nullable
  public static String getPathFromPerl(@NotNull PerlHostData hostData) {
    List<String> perlPathLines = getOutputFromProgram(
      hostData, hostData.getOsHandler().getPerlExecutableName(), PERL_LE, PERL_CTRL_X);
    return perlPathLines.size() == 1 ? perlPathLines.get(0) : null;
  }

  /**
   * Gets stdout from executing a perl command with a given parameters, command represented by {@code parameters}.
   */
  @NotNull
  public static List<String> getOutputFromPerl(@NotNull Sdk perlSdk, @NotNull String... parameters) {
    return getOutputFromProgram(new PerlCommandLine(perlSdk.getHomePath()).withParameters(parameters).withSdk(perlSdk));
  }


  /**
   * Gets stdout from executing a command represented by {@code commands} on the host represented by {@code hostData}
   * Commands are going to be patched with version manager, represented by {@code versionManagerData}
   */
  @NotNull
  public static List<String> getOutputFromProgram(@NotNull PerlHostData hostData,
                                                  @NotNull PerlVersionManagerData versionManagerData,
                                                  @NotNull String... commands) {
    return getOutputFromProgram(new PerlCommandLine(commands).withHostData(hostData).withVersionManagerData(versionManagerData));
  }

  /**
   * Gets stdout from executing a command represented by {@code commands} on the host represented by {@code hostData}
   *
   * @apiNote MUST not be used for executing perl scripts
   */
  @NotNull
  public static List<String> getOutputFromProgram(@NotNull PerlHostData hostData, @NotNull String... commands) {
    return getOutputFromProgram(new PerlCommandLine(commands).withHostData(hostData));
  }

  /**
   * Gets stdout from a {@code commandLine} at host represented by {@code hostData}
   */
  @NotNull
  private static List<String> getOutputFromProgram(@NotNull PerlCommandLine commandLine) {
    try {
      return PerlHostData.execAndGetOutput(commandLine).getStdoutLines();
    }
    catch (Exception e) {
      LOG.warn("Error executing " + commandLine, e);
      return Collections.emptyList();
    }
  }

  public static void runInConsole(@NotNull Project project,
                                  @NotNull PerlCommandLine perlCommandLine) {
    Executor runExecutor = DefaultRunExecutor.getRunExecutorInstance();
    PerlRunConsole consoleView = new PerlRunConsole(project, true);
    ProcessHandler processHandler = null;
    try {
      processHandler = PerlHostData.createConsoleProcessHandler(perlCommandLine);
    }
    catch (ExecutionException e) {
      consoleView.print(e.getMessage(), ConsoleViewContentType.ERROR_OUTPUT);
      LOG.error(e);
    }

    RunContentDescriptor runContentDescriptor = new RunContentDescriptor(
      consoleView,
      processHandler,
      consoleView.buildPanel(),
      ObjectUtils.notNull(perlCommandLine.getConsoleTitle(), perlCommandLine.getCommandLineString()),
      PerlIcons.PERL_LANGUAGE_ICON
    );

    ExecutionManager.getInstance(project).getContentManager().showRunContent(runExecutor, runContentDescriptor);
    if (processHandler != null) {
      consoleView.attachToProcess(processHandler);
      ProcessTerminatedListener.attach(processHandler, project);
      processHandler.startNotify();
    }
    consoleView.addCloseAction(runExecutor, runContentDescriptor);
  }
}
