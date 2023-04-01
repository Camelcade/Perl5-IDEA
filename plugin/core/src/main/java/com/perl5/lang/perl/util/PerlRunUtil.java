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

package com.perl5.lang.perl.util;

import com.intellij.codeInsight.daemon.DaemonCodeAnalyzer;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.executors.DefaultRunExecutor;
import com.intellij.execution.process.ProcessAdapter;
import com.intellij.execution.process.ProcessEvent;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.process.ProcessListener;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.execution.ui.RunContentDescriptor;
import com.intellij.execution.ui.RunContentManager;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.RootsChangeRescanningInfo;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.ex.ProjectRootManagerEx;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.util.EmptyRunnable;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.ObjectUtils;
import com.intellij.util.concurrency.Semaphore;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.PerlBundle;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.adapters.CpanAdapter;
import com.perl5.lang.perl.adapters.CpanminusAdapter;
import com.perl5.lang.perl.idea.actions.PerlDumbAwareAction;
import com.perl5.lang.perl.idea.execution.PerlCommandLine;
import com.perl5.lang.perl.idea.execution.PerlTerminalExecutionConsole;
import com.perl5.lang.perl.idea.project.PerlProjectManager;
import com.perl5.lang.perl.idea.run.PerlRunConsole;
import com.perl5.lang.perl.idea.sdk.PerlSdkType;
import com.perl5.lang.perl.idea.sdk.host.PerlConsoleView;
import com.perl5.lang.perl.idea.sdk.host.PerlHostData;
import com.perl5.lang.perl.idea.sdk.host.os.PerlOsHandler;
import com.perl5.lang.perl.idea.sdk.versionManager.PerlVersionManagerData;
import org.jetbrains.annotations.*;

import java.io.File;
import java.util.*;
import java.util.stream.Stream;


public final class PerlRunUtil {
  public static final String PERL_I = "-I";
  public static final String PERL_LE = "-le";
  public static final String PERL_CTRL_X = "print eval chr(0x24).q{^X}";
  public static final String PERL5OPT = "PERL5OPT";
  private static final Logger LOG = Logger.getInstance(PerlRunUtil.class);
  private static final String MISSING_MODULE_PREFIX = "(you may need to install the ";
  private static final String MISSING_MODULE_SUFFIX = " module)";
  private static final String LEGACY_MODULE_PREFIX = "Can't locate ";
  private static final String LEGACY_MODULE_SUFFIX = " in @INC";
  public static final String BUNDLED_MODULE_NAME = "Bundle::Camelcade";
  private static final List<RunContentDescriptor> TEST_CONSOLE_DESCRIPTORS = new ArrayList<>();
  private static Semaphore ourTestSdkRefreshSemaphore;
  private static Disposable ourTestDisposable;
  // should be synchronized with https://github.com/Camelcade/Bundle-Camelcade/blob/master/dist.ini
  private static final Set<String> BUNDLED_MODULE_PARTS = Collections.unmodifiableSet(ContainerUtil.newHashSet(
    "App::cpanminus",
    "App::Prove::Plugin::PassEnv",
    "B::Deparse",
    "Config",
    "Devel::Cover",
    "Devel::Camelcadedb",
    "Devel::NYTProf",
    "File::Find",
    "JSON",
    "Perl::Critic",
    "Perl::Tidy",
    "TAP::Formatter::Camelcade",
    "Test::Harness"
  ));

  private PerlRunUtil() {
  }

  /**
   * Builds non-patched perl command line for {@code project}'s sdk (without patching by version manager)
   *
   * @return command line if perl support for project or scriptFile is enabled
   */
  public static @Nullable PerlCommandLine getPerlCommandLine(@NotNull Project project,
                                                             @Nullable VirtualFile scriptFile,
                                                             String... perlParameters) {
    return getPerlCommandLine(
      project, PerlProjectManager.getSdk(project, scriptFile), scriptFile, Arrays.asList(perlParameters), Collections.emptyList());
  }

  public static @Nullable PerlCommandLine getPerlCommandLine(@NotNull Project project,
                                                             @Nullable Sdk perlSdk,
                                                             @Nullable VirtualFile scriptFile,
                                                             @NotNull List<String> perlParameters,
                                                             @NotNull List<String> scriptParameters) {
    if (perlSdk == null) {
      perlSdk = PerlProjectManager.getSdk(project, scriptFile);
    }
    return getPerlCommandLine(project, perlSdk, ObjectUtils.doIfNotNull(scriptFile, VirtualFile::getPath), perlParameters,
                              scriptParameters);
  }

  public static PerlCommandLine getPerlCommandLine(@NotNull Project project,
                                                   @Nullable String localScriptPath) {
    return getPerlCommandLine(project, null, localScriptPath, Collections.emptyList(), Collections.emptyList());
  }

  /**
   * Builds non-patched perl command line (without patching by version manager)
   *
   * @return new perl command line or null if sdk is missing or corrupted
   */
  public static @Nullable PerlCommandLine getPerlCommandLine(@NotNull Project project,
                                                             @Nullable Sdk perlSdk,
                                                             @Nullable String localScriptPath,
                                                             @NotNull List<String> perlParameters,
                                                             @NotNull List<String> scriptParameters) {
    if (perlSdk == null) {
      perlSdk = PerlProjectManager.getSdk(project);
    }
    if (perlSdk == null) {
      LOG.error("No sdk provided or available in project " + project);
      return null;
    }
    String interpreterPath = PerlProjectManager.getInterpreterPath(perlSdk);
    if (StringUtil.isEmpty(interpreterPath)) {
      LOG.warn("Empty interpreter path in " + perlSdk + " while building command line for " + localScriptPath);
      return null;
    }
    PerlCommandLine commandLine = new PerlCommandLine(interpreterPath).withSdk(perlSdk).withProject(project);
    PerlHostData<?, ?> hostData = PerlHostData.notNullFrom(perlSdk);
    commandLine.addParameters(getPerlRunIncludeArguments(hostData, project));

    commandLine.addParameters(perlParameters);

    if (StringUtil.isNotEmpty(localScriptPath)) {
      String remoteScriptPath = hostData.getRemotePath(localScriptPath);
      if (remoteScriptPath != null) {
        commandLine.addParameter(remoteScriptPath);
      }
    }

    commandLine.addParameters(scriptParameters);

    return commandLine;
  }

  /**
   * @return a list with {@code -I} arguments we need to pass to the Perl to include all in-project library directories and external
   * libraries configured by user
   */
  public static @NotNull List<String> getPerlRunIncludeArguments(@NotNull PerlHostData<?, ?> hostData, @NotNull Project project) {
    var result = new ArrayList<String>();
    var perlProjectManager = PerlProjectManager.getInstance(project);
    for (VirtualFile libRoot : perlProjectManager.getModulesLibraryRoots()) {
      result.add(PERL_I + hostData.getRemotePath(libRoot.getCanonicalPath()));
    }
    for (VirtualFile libRoot : perlProjectManager.getExternalLibraryRoots()) {
      result.add(PERL_I + hostData.getRemotePath(libRoot.getCanonicalPath()));
    }
    return result;
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
  public static @Nullable VirtualFile findLibraryScriptWithNotification(@NotNull Project project,
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
  public static @Nullable VirtualFile findLibraryScriptWithNotification(@NotNull Sdk sdk,
                                                                        @Nullable Project project,
                                                                        @NotNull String scriptName,
                                                                        @Nullable String libraryName) {
    VirtualFile scriptFile = findScript(project, scriptName);
    if (scriptFile != null) {
      return scriptFile;
    }

    if (StringUtil.isEmpty(libraryName)) {
      return null;
    }

    showMissingLibraryNotification(project, sdk, libraryName);

    return null;
  }

  public static void showMissingLibraryNotification(@NotNull Project project, @NotNull Sdk sdk, @NotNull Collection<String> packageNames) {
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
    addInstallActionsAndShow(project, sdk, packageNames, notification);
  }

  /**
   * Adds installation actions to the {@code notification} and shows it in the context of the {@code project}
   */
  public static void addInstallActionsAndShow(@Nullable Project project,
                                              @NotNull Sdk sdk,
                                              @NotNull Collection<String> packagesToInstall,
                                              @NotNull Notification notification) {
    List<AnAction> actions = new ArrayList<>();
    ContainerUtil.addIfNotNull(actions, ReadAction.compute(
      () -> CpanminusAdapter.createInstallAction(sdk, project, packagesToInstall, notification::expire)));
    actions.add(CpanAdapter.createInstallAction(sdk, project, packagesToInstall, notification::expire));
    boolean hasAlternatives = actions.size() > 1;

    if (new HashSet<>(BUNDLED_MODULE_PARTS).removeAll(packagesToInstall)) {
      actions.add(new PerlDumbAwareAction(PerlBundle.message("perl.quickfix.install.module", BUNDLED_MODULE_NAME)) {
        @Override
        public void actionPerformed(@NotNull AnActionEvent e) {
          if (hasAlternatives) {
            notification.expire();
            showMissingLibraryNotification(project, sdk, BUNDLED_MODULE_NAME);
          }
          else {
            CpanAdapter.installModules(sdk, project, packagesToInstall, notification::expire);
          }
        }
      });
    }

    actions.forEach(notification::addAction);
    Notifications.Bus.notify(notification, project);
  }


  private static void showMissingLibraryNotification(@Nullable Project project, @NotNull Sdk sdk, @NotNull String libraryName) {
    Notification notification = new Notification(
      PerlBundle.message("perl.missing.library.notification"),
      PerlBundle.message("perl.missing.library.notification.title", libraryName),
      PerlBundle.message("perl.missing.library.notification.message", libraryName),
      NotificationType.ERROR
    );

    addInstallActionsAndShow(project, sdk, Collections.singletonList(libraryName), notification);
  }

  /**
   * Attempts to find an executable script by name in perl's libraries path
   *
   * @return script's virtual file if available
   * @apiNote returns virtual file of local file, not remote. It finds not a perl script, but executable script. E.g. for windows, it may
   * be a bat script
   **/
  @Contract("null,_->null;_,null->null")
  public static @Nullable VirtualFile findScript(@Nullable Project project, @Nullable String scriptName) {
    var sdk = PerlProjectManager.getSdk(project);
    if (sdk == null || StringUtil.isEmpty(scriptName)) {
      return null;
    }
    ApplicationManager.getApplication().assertReadAccessAllowed();
    PerlOsHandler osHandler = PerlOsHandler.notNullFrom(sdk);
    return getBinDirectories(project)
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
  public static @NotNull Stream<VirtualFile> getBinDirectories(@NotNull Project project) {
    ApplicationManager.getApplication().assertReadAccessAllowed();
    var libraryRoots = PerlProjectManager.getInstance(project).getAllLibraryRoots();
    var files = new ArrayList<>(ContainerUtil.map(libraryRoots, PerlRunUtil::findLibsBin));

    var sdk = PerlProjectManager.getSdk(project);
    if (sdk != null) {
      PerlHostData<?, ?> hostData = PerlHostData.notNullFrom(sdk);
      File localSdkBinDir = hostData.getLocalPath(new File(
        StringUtil.notNullize(PerlProjectManager.getInterpreterPath(sdk))).getParentFile());
      if (localSdkBinDir != null) {
        files.add(VfsUtil.findFileByIoFile(localSdkBinDir, false));
      }
      PerlVersionManagerData.notNullFrom(sdk).getBinDirsPath().forEach(
        it -> ObjectUtils.doIfNotNull(hostData.getLocalPath(it), localPath -> files.add(VfsUtil.findFileByIoFile(localPath, false))));
    }
    return files.stream().filter(Objects::nonNull).distinct();
  }

  /**
   * Finds a bin dir for a library root
   *
   * @return bin root or null if not available
   * @implSpec for now we are traversing tree up to lib dir and resolving {@code ../bin}
   */
  private static @Nullable VirtualFile findLibsBin(@Nullable VirtualFile libraryRoot) {
    if (libraryRoot == null || !libraryRoot.isValid()) {
      return null;
    }
    File binPath = findLibsBin(new File(libraryRoot.getPath()));
    return binPath == null ? null : VfsUtil.findFileByIoFile(binPath, false);
  }

  /**
   * Finds a bin dir for a library root path
   *
   * @return bin root path or null if not found
   * @implSpec for now we are traversing tree up to {@code lib} dir and resolving {@code ../bin}
   */
  public static @Nullable File findLibsBin(@Nullable File libraryRoot) {
    if (libraryRoot == null) {
      return null;
    }
    String fileName = libraryRoot.getName();
    if ("lib".equals(fileName)) {
      return new File(libraryRoot.getParentFile(), "bin");
    }
    return findLibsBin(libraryRoot.getParentFile());
  }

  /**
   * Gets stdout from executing a perl command with a given parameters, command represented by {@code parameters}.
   */
  public static @NotNull List<String> getOutputFromPerl(@NotNull Sdk perlSdk, @NotNull String... parameters) {
    String interpreterPath = PerlProjectManager.getInterpreterPath(perlSdk);
    if (StringUtil.isEmpty(interpreterPath)) {
      LOG.warn("Empty interpreter path from " + perlSdk);
      return Collections.emptyList();
    }
    return getOutputFromProgram(new PerlCommandLine(
      interpreterPath).withParameters(parameters).withSdk(perlSdk));
  }


  /**
   * Gets stdout from executing a command represented by {@code commands} on the host represented by {@code hostData}
   * Commands are going to be patched with version manager, represented by {@code versionManagerData}
   */
  public static @NotNull List<String> getOutputFromProgram(@NotNull PerlHostData<?, ?> hostData,
                                                           @NotNull PerlVersionManagerData<?, ?> versionManagerData,
                                                           @NotNull String... commands) {
    return getOutputFromProgram(new PerlCommandLine(commands).withHostData(hostData).withVersionManagerData(versionManagerData));
  }

  /**
   * Gets stdout from a {@code commandLine} at host represented by {@code hostData}
   */
  private static @NotNull List<String> getOutputFromProgram(@NotNull PerlCommandLine commandLine) {
    try {
      var commandOutput = PerlHostData.execAndGetOutput(commandLine);
      if (commandOutput.getExitCode() != 0) {
        LOG.warn("Non-zero exit code from " + commandLine + "; " + commandOutput);
      }
      return commandOutput.getStdoutLines();
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
    boolean isUnitTestMode = ApplicationManager.getApplication().isUnitTestMode();
    PerlConsoleView consoleView = isUnitTestMode ? new PerlRunConsole(project) : new PerlTerminalExecutionConsole(project);
    consoleView.withHostData(perlCommandLine.getEffectiveHostData());
    ProcessHandler processHandler = null;
    try {
      processHandler = PerlHostData.createConsoleProcessHandler(perlCommandLine.withPty(!isUnitTestMode));
      if (isUnitTestMode) {
        processHandler.addProcessListener(new ProcessAdapter() {
          @Override
          public void onTextAvailable(@NotNull ProcessEvent event, @NotNull Key outputType) {
            LOG.info(outputType + ": " + event.getText());
          }
        });
      }
    }
    catch (ExecutionException e) {
      consoleView.print(e.getMessage(), ConsoleViewContentType.ERROR_OUTPUT);
      LOG.warn(e);
    }

    RunContentDescriptor runContentDescriptor = new RunContentDescriptor(
      consoleView,
      processHandler,
      consoleView.getComponent(),
      ObjectUtils.notNull(perlCommandLine.getConsoleTitle(), perlCommandLine.getCommandLineString()),
      ObjectUtils.notNull(perlCommandLine.getConsoleIcon(), PerlIcons.PERL_LANGUAGE_ICON)
    );

    RunContentManager.getInstance(project).showRunContent(runExecutor, runContentDescriptor);
    if (processHandler != null) {
      consoleView.attachToProcess(processHandler);
      processHandler.startNotify();
      if (ApplicationManager.getApplication().isUnitTestMode()) {
        LOG.assertTrue(ourTestDisposable != null);
        TEST_CONSOLE_DESCRIPTORS.add(runContentDescriptor);
        Disposer.register(ourTestDisposable, runContentDescriptor.getExecutionConsole());
      }
    }
  }

  public static void addMissingPackageListener(@NotNull ProcessHandler handler,
                                               @NotNull PerlCommandLine commandLine) {
    if (!commandLine.isWithMissingPackageListener()) {
      return;
    }
    ProcessListener listener = createMissingPackageListener(commandLine.getEffectiveProject(), commandLine.getEffectiveSdk());
    if (listener != null) {
      handler.addProcessListener(listener);
    }
  }

  /**
   * Creates a listener watching process output and showing notifications about missing libraries
   */
  private static @Nullable ProcessListener createMissingPackageListener(@Nullable Project project, @Nullable Sdk sdk) {
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
    Set<String> missingPackages = new HashSet<>();

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
  public static @Nullable String setProgressText(@Nullable @Nls String newText) {
    ProgressIndicator indicator = ProgressManager.getInstance().getProgressIndicator();
    if (indicator != null) {
      String oldText = indicator.getText();
      indicator.setText(newText);
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
    LOG.debug("Starting to refresh ", sdk, " on ", Thread.currentThread().getName());
    if (ourTestSdkRefreshSemaphore != null) {
      ourTestSdkRefreshSemaphore.down();
    }
    new Task.Backgroundable(project, PerlBundle.message("perl.progress.refreshing.interpreter.information", sdk.getName()), false) {
      @Override
      public void run(@NotNull ProgressIndicator indicator) {
        PerlSdkType.INSTANCE.setupSdkPaths(sdk);
        if (project != null) {
          WriteAction.runAndWait(() -> {
            if (!project.isDisposed()) {
              ProjectRootManagerEx.getInstanceEx(project)
                .makeRootsChange(EmptyRunnable.getInstance(), RootsChangeRescanningInfo.TOTAL_RESCAN);
              DaemonCodeAnalyzer.getInstance(project).restart();
            }
          });
        }
        if (ourTestSdkRefreshSemaphore != null) {
          ourTestSdkRefreshSemaphore.up();
        }
        LOG.debug("Finished to refresh ", sdk, " on ", Thread.currentThread().getName());
      }
    }.queue();
  }

  @TestOnly
  public static @NotNull List<RunContentDescriptor> getTestConsoleDescriptors() {
    return TEST_CONSOLE_DESCRIPTORS;
  }

  @TestOnly
  public static @NotNull Semaphore getSdkRefreshSemaphore() {
    return ourTestSdkRefreshSemaphore;
  }

  @TestOnly
  public static void setUpForTests(@NotNull Disposable testDisposable) {
    ourTestDisposable = testDisposable;
    ourTestSdkRefreshSemaphore = new Semaphore();
    Disposer.register(testDisposable, () -> {
      TEST_CONSOLE_DESCRIPTORS.clear();
      ourTestSdkRefreshSemaphore = null;
      ourTestDisposable = null;
    });
  }

  /**
   * Updates {@code environment} with {@code perlParametersList} packed to {@code PERL5OPT} environment variable to pass it transparently.
   */
  public static void updatePerl5Opt(@NotNull Map<? super String, String> environment, @NotNull List<String> perlParametersList) {
    if (perlParametersList.isEmpty()) {
      return;
    }
    var perlParameters = StringUtil.join(perlParametersList, " ");

    String currentOpt = environment.get(PERL5OPT);
    if (StringUtil.isNotEmpty(currentOpt)) {
      perlParameters = String.join(" ", currentOpt, perlParameters);
    }
    environment.put(PERL5OPT, perlParameters);
  }
}
