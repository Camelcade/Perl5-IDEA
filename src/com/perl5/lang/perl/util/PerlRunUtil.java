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

import com.intellij.codeInsight.daemon.DaemonCodeAnalyzer;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.ExecutionManager;
import com.intellij.execution.Executor;
import com.intellij.execution.executors.DefaultRunExecutor;
import com.intellij.execution.process.ProcessAdapter;
import com.intellij.execution.process.ProcessEvent;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.process.ProcessListener;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.execution.ui.RunContentDescriptor;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.SdkTypeId;
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.roots.ex.ProjectRootManagerEx;
import com.intellij.openapi.util.EmptyRunnable;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.ObjectUtils;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.PerlBundle;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.adapters.CpanAdapter;
import com.perl5.lang.perl.adapters.CpanminusAdapter;
import com.perl5.lang.perl.idea.execution.PerlCommandLine;
import com.perl5.lang.perl.idea.execution.PerlRunConsole;
import com.perl5.lang.perl.idea.project.PerlProjectManager;
import com.perl5.lang.perl.idea.sdk.PerlSdkType;
import com.perl5.lang.perl.idea.sdk.host.PerlHostData;
import com.perl5.lang.perl.idea.sdk.host.os.PerlOsHandler;
import com.perl5.lang.perl.idea.sdk.versionManager.PerlVersionManagerData;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.nio.file.Paths;
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
  private static final String MISSING_MODULE_PREFIX = "(you may need to install the ";
  private static final String MISSING_MODULE_SUFFIX = " module)";
  private static final String LEGACY_MODULE_PREFIX = "Can't locate ";
  private static final String LEGACY_MODULE_SUFFIX = " in @INC";

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
    if (perlSdk == null) {
      perlSdk = PerlProjectManager.getSdk(project);
    }
    if (perlSdk == null) {
      LOG.error("No sdk provided or available in project " + project);
      return null;
    }
    String interpreterPath = perlSdk.getHomePath();
    if (StringUtil.isEmpty(interpreterPath)) {
      LOG.warn("Empty interpreter path in " + perlSdk + " while building command line for " + scriptFile);
      return null;
    }
    PerlCommandLine commandLine = new PerlCommandLine(perlSdk).withProject(project);
    commandLine.setExePath(interpreterPath);
    PerlHostData hostData = PerlHostData.notNullFrom(perlSdk);
    for (VirtualFile libRoot : PerlProjectManager.getInstance(project).getModulesLibraryRoots()) {
      commandLine.addParameter(PERL_I + hostData.getRemotePath(libRoot.getCanonicalPath()));
    }

    commandLine.addParameters(perlParameters);

    if (scriptFile != null) {
      commandLine.addParameter(hostData.getRemotePath(scriptFile.getPath()));
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
    return ObjectUtils.doIfNotNull(
      PerlProjectManager.getSdkWithNotification(project),
      it -> findLibraryScriptWithNotification(it, project, scriptName, libraryName));
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
                                                              @NotNull Project project,
                                                              @NotNull String scriptName,
                                                              @Nullable String libraryName) {
    VirtualFile scriptFile = findScript(sdk, scriptName);
    if (scriptFile != null) {
      return scriptFile;
    }

    if (StringUtil.isEmpty(libraryName)) {
      return null;
    }

    showMissingLibraryNotification(project, sdk, libraryName);

    return null;
  }

  private static void showMissingLibraryNotification(@NotNull Project project, @NotNull Sdk sdk, @NotNull Collection<String> packageNames) {
    if (packageNames.isEmpty()) {
      return;
    }
    if (packageNames.size() == 1) {
      showMissingLibraryNotification(project, sdk, packageNames.iterator().next());
      return;
    }

    Notification notification = new Notification(
      PerlBundle.message("perl.missing.library.notification"),
      PerlBundle.message("perl.missing.library.notification.title", packageNames.size()),
      StringUtil.join(ContainerUtil.sorted(packageNames), ", "),
      NotificationType.ERROR
    );

    AnAction installCpanmAction =
      ReadAction.compute(() -> CpanminusAdapter.createInstallAction(sdk, project, packageNames, notification::expire));
    if (installCpanmAction != null) {
      notification.addAction(installCpanmAction);
    }
    notification.addAction(CpanAdapter.createInstallAction(sdk, project, packageNames, notification::expire));

    Notifications.Bus.notify(notification, project);
  }

  private static void showMissingLibraryNotification(@NotNull Project project, @NotNull Sdk sdk, @NotNull String libraryName) {
    Notification notification = new Notification(
      PerlBundle.message("perl.missing.library.notification"),
      PerlBundle.message("perl.missing.library.notification.title", libraryName),
      PerlBundle.message("perl.missing.library.notification.message", libraryName),
      NotificationType.ERROR
    );

    AnAction installCpanmAction =
      ReadAction
        .compute(() -> CpanminusAdapter.createInstallAction(sdk, project, Collections.singletonList(libraryName), notification::expire));
    if (installCpanmAction != null) {
      notification.addAction(installCpanmAction);
    }
    notification.addAction(CpanAdapter.createInstallAction(sdk, project, Collections.singletonList(libraryName), notification::expire));

    Notifications.Bus.notify(notification, project);
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
   * @apiNote returns virtual file of local file, not remote
   **/
  @Contract("null,_->null; _,null->null")
  @Nullable
  public static VirtualFile findScript(@Nullable Sdk sdk, @Nullable String scriptName) {
    if (sdk == null || StringUtil.isEmpty(scriptName)) {
      return null;
    }
    ApplicationManager.getApplication().assertReadAccessAllowed();
    PerlOsHandler osHandler = PerlOsHandler.notNullFrom(sdk);
    return getBinDirectories(sdk)
      .map(root -> {
        VirtualFile scriptFile = null;
        if (osHandler.isMsWindows()) {
          scriptFile = root.findChild(scriptName + ".bat");
        }
        return scriptFile != null ? scriptFile : root.findChild(scriptName);
      })
      .filter(Objects::nonNull)
      .findFirst().orElse(null);
  }


  /**
   * @return list of perl bin directories where script from library may be located
   **/
  @NotNull
  public static Stream<VirtualFile> getBinDirectories(@Nullable Sdk sdk) {
    if (sdk == null) {
      return Stream.empty();
    }
    ApplicationManager.getApplication().assertReadAccessAllowed();
    SdkTypeId sdkType = sdk.getSdkType();
    if (!(sdkType instanceof PerlSdkType)) {
      throw new IllegalArgumentException("Got non-perl sdk: " + sdk);
    }
    List<VirtualFile> files =
      new ArrayList<>(ContainerUtil.map(sdk.getRootProvider().getFiles(OrderRootType.CLASSES), PerlRunUtil::findLibsBin));

    PerlHostData hostData = PerlHostData.notNullFrom(sdk);
    Path sdkBinDir = Paths.get(hostData.getLocalPath(StringUtil.notNullize(sdk.getHomePath()))).getParent();
    if (sdkBinDir != null) {
      files.add(VfsUtil.findFile(sdkBinDir, false));
    }
    PerlVersionManagerData.notNullFrom(sdk).getBinDirsPath().forEach(it -> files.add(VfsUtil.findFile(hostData.getLocalPath(it), false)));
    return files.stream().filter(Objects::nonNull).distinct();
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
    Path binPath = findLibsBin(Paths.get(libraryRoot.getPath()));
    return binPath == null ? null : VfsUtil.findFile(binPath, false);
  }

  /**
   * Finds a bin dir for a library root path
   *
   * @return bin root path or null if not found
   * @implSpec for now we are traversing tree up to {@code lib} dir and resolving {@code ../bin}
   */
  @Nullable
  public static Path findLibsBin(@Nullable Path libraryRoot) {
    if (libraryRoot == null) {
      return null;
    }
    Path fileName = libraryRoot.getFileName();
    if (fileName == null) {
      return null;
    }
    if ("lib".equals(fileName.toString())) {
      return libraryRoot.resolve("../bin");
    }
    return findLibsBin(libraryRoot.getParent());
  }

  /**
   * Requests perl path using introspection variable $^X: {@code perl -le print $^X}
   *
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

  public static void runInConsole(@NotNull PerlCommandLine perlCommandLine) {

    ApplicationManager.getApplication().assertIsDispatchThread();
    Executor runExecutor = DefaultRunExecutor.getRunExecutorInstance();
    Project project = perlCommandLine.getNonNullEffectiveProject();
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
      ObjectUtils.notNull(perlCommandLine.getConsoleIcon(), PerlIcons.PERL_LANGUAGE_ICON)
    );

    ExecutionManager.getInstance(project).getContentManager().showRunContent(runExecutor, runContentDescriptor);
    if (processHandler != null) {
      consoleView.attachToProcess(processHandler);
      processHandler.startNotify();
    }
    consoleView.addCloseAction(runExecutor, runContentDescriptor);
  }

  public static void addMissingPackageListener(@NotNull ProcessHandler handler,
                                               @NotNull PerlCommandLine commandLine) {
    ProcessListener listener = createMissingPackageListener(commandLine.getEffectiveProject(), commandLine.getEffectiveSdk());
    if (listener != null) {
      handler.addProcessListener(listener);
    }
  }

  /**
   * Creates a listener watching process output and showing notifications about missing libraries
   */
  @Nullable
  private static ProcessListener createMissingPackageListener(@Nullable Project project, @Nullable Sdk sdk) {
    if (project == null) {
      return null;
    }

    if (sdk == null) {
      sdk = PerlProjectManager.getSdk(project);
      if (sdk == null) {
        return null;
      }
    }

    Sdk finalSdk = sdk;
    Set<String> missingPackages = ContainerUtil.newHashSet();

    return new ProcessAdapter() {
      @Override
      public void onTextAvailable(@NotNull ProcessEvent event, @NotNull Key outputType) {
        String text = event.getText();
        if (StringUtil.isEmpty(text)) {
          return;
        }
        int keyOffset = text.indexOf(MISSING_MODULE_PREFIX);
        if (keyOffset == -1) {
          checkLegacyPrefix(text);
          return;
        }
        int startOffset = keyOffset + MISSING_MODULE_PREFIX.length();
        int endOffset = text.indexOf(MISSING_MODULE_SUFFIX, startOffset);
        if (endOffset == -1) {
          return;
        }
        processPackage(text.substring(startOffset, endOffset));
      }

      private void checkLegacyPrefix(@NotNull String text) {
        int keyOffset = text.indexOf(LEGACY_MODULE_PREFIX);
        if (keyOffset == -1) {
          return;
        }
        int startOffset = keyOffset + LEGACY_MODULE_PREFIX.length();
        int endOffset = text.indexOf(LEGACY_MODULE_SUFFIX, startOffset);
        if (endOffset == -1) {
          return;
        }
        processPackage(PerlPackageUtil.getPackageNameByPath(text.substring(startOffset, endOffset)));
      }

      private void processPackage(@Nullable String packageName) {
        if (StringUtil.isNotEmpty(packageName) && missingPackages.add(packageName)) {
          showMissingLibraryNotification(project, finalSdk, missingPackages);
        }
      }
    };
  }


  /**
   * Sets {@code newText} to the progress indicator if available.
   *
   * @return old indicator text
   */
  @Nullable
  public static String setProgressText(@Nullable String newText) {
    ProgressIndicator indicator = ProgressManager.getInstance().getProgressIndicator();
    if (indicator != null) {
      String oldText = indicator.getText();
      indicator.setText(newText);
      return oldText;
    }
    return null;
  }

  /**
   * Sets {@code newText} to the progress indicator secondary text, if indicator is available
   *
   * @return old indicator text
   */
  @Nullable
  public static String setProgressText2(@Nullable String newText) {
    ProgressIndicator indicator = ProgressManager.getInstance().getProgressIndicator();
    if (indicator != null) {
      String oldText = indicator.getText2();
      indicator.setText2(newText);
      return oldText;
    }
    return null;
  }

  public static void refreshSdkDirs(@Nullable Project project) {
    refreshSdkDirs(PerlProjectManager.getSdk(project), project);
  }

  /**
   * Asynchronously refreshes directories of sdk. Need to be invoked after installations
   */
  public static void refreshSdkDirs(@Nullable Sdk sdk, @Nullable Project project) {
    if (sdk == null) {
      return;
    }

    new Task.Backgroundable(project, PerlBundle.message("perl.progress.refreshing.interpreter.information"), false) {
      @Override
      public void run(@NotNull ProgressIndicator indicator) {
        PerlSdkType.INSTANCE.setupSdkPaths(sdk);
        WriteAction.runAndWait(
          () -> ProjectRootManagerEx.getInstanceEx(myProject).makeRootsChange(EmptyRunnable.getInstance(), false, true));
        if (project != null) {
          DaemonCodeAnalyzer.getInstance(project).restart();
        }
      }
    }.queue();
  }
}
