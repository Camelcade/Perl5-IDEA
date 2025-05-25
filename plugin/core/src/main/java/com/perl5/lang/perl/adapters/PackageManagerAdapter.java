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

package com.perl5.lang.perl.adapters;

import com.intellij.execution.process.ProcessAdapter;
import com.intellij.execution.process.ProcessEvent;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.util.AtomicNotNullLazyValue;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.ui.update.MergingUpdateQueue;
import com.intellij.util.ui.update.Update;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.idea.actions.PerlDumbAwareAction;
import com.perl5.lang.perl.idea.execution.PerlCommandLine;
import com.perl5.lang.perl.idea.project.PerlProjectManager;
import com.perl5.lang.perl.idea.sdk.host.PerlHostData;
import com.perl5.lang.perl.util.PerlPluginUtil;
import com.perl5.lang.perl.util.PerlRunUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;
import org.jetbrains.annotations.VisibleForTesting;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public abstract class PackageManagerAdapter {
  public static final @NlsSafe String CPANMINUS_PACKAGE_NAME = "App::cpanminus";

  private static final Logger LOG = Logger.getInstance(PackageManagerAdapter.class);
  private static final AtomicNotNullLazyValue<MergingUpdateQueue> QUEUE_PROVIDER = AtomicNotNullLazyValue.createValue(() ->
                                                                                                                        new MergingUpdateQueue(
                                                                                                                          "perl.installer.queue",
                                                                                                                          300, true, null,
                                                                                                                          PerlPluginUtil.getUnloadAwareDisposable()));

  private final @NotNull Sdk mySdk;

  private final @Nullable Project myProject;

  public PackageManagerAdapter(@NotNull Sdk sdk, @Nullable Project project) {
    mySdk = sdk;
    myProject = project;
  }

  public abstract @NotNull String getPresentableName();

  protected abstract @NotNull String getManagerScriptName();

  protected abstract @NotNull String getManagerPackageName();

  /**
   * @return list of parameters for manager's script to install a package with {@code packageName}
   */
  protected @NotNull List<String> getInstallParameters(@NotNull Collection<String> packageNames, boolean suppressTests) {
    return new ArrayList<>(packageNames);
  }

  /**
   * This method installs packages with given package names using manager's script.
   *
   * @param packageNames  a collection of strings representing package names to install
   * @param callback      a runnable object to execute when installation is complete
   * @param suppressTests true iff tests should be suppressed during installation
   * @apiNote this method starts installation process immediately. If you need a deferred installation (with 300ms) and it is possible that
   * more packages may come for installation, use {@link #queueInstall(Collection, boolean)}
   */
  public void install(@NotNull Collection<String> packageNames, @NotNull Runnable callback, boolean suppressTests) {
    doInstall(packageNames, callback, suppressTests);
  }

  /**
   * Asynchronously installs {@code packageNames} with output in the console
   *
   * @param callback      optional callback to be invoked after installing and paths.
   * @param suppressTests tue iff tests should not run during installation
   */
  private void doInstall(@NotNull Collection<String> packageNames, @Nullable Runnable callback, boolean suppressTests) {
    ApplicationManager.getApplication().assertIsDispatchThread();
    if (myProject != null && myProject.isDisposed()) {
      return;
    }
    VirtualFile script = PerlRunUtil.findLibraryScriptWithNotification(
      getSdk(), getProject(), getManagerScriptName(), getManagerPackageName());
    if (script == null) {
      LOG.warn("Unable to find a script: " +
               "sdk=" + getSdk() +
               "; project=" + getProject() +
               "; scriptName=" + getManagerScriptName() +
               "; packageName=" + getManagerPackageName());
      return;
    }
    String remotePath = PerlHostData.notNullFrom(mySdk).getRemotePath(script.getPath());
    PerlRunUtil.runInConsole(
      new PerlCommandLine(remotePath)
        .withParameters(getInstallParameters(packageNames, suppressTests))
        .withSdk(getSdk())
        .withProject(getProject())
        .withConsoleTitle(PerlBundle.message("perl.cpan.console.installing", StringUtil.join(packageNames, ", ")))
        .withProcessListener(new ProcessAdapter() {
          @Override
          public void processTerminated(@NotNull ProcessEvent event) {
            PerlRunUtil.refreshSdkDirs(mySdk, myProject, () -> {
              if (event.getExitCode() == 0 && callback != null) {
                callback.run();
              }
            });
          }
        })
    );
  }

  /**
   * Queues {@code packageNames} for installation by adding them to the update queue for asynchronous processing.
   * <p>
   * Note that this method only adds the packages to the queue for installation; it does not perform the installation itself.
   * Use {@link #install(Collection, Runnable, boolean)} to actually install the packages.
   *
   * @param packageNames  the collection of package names to be installed
   * @param suppressTests {@code true} if tests should not run during installation; {@code false} otherwise
   * @see #install(Collection, Runnable, boolean)
   */
  public final void queueInstall(@NotNull Collection<String> packageNames, boolean suppressTests) {
    QUEUE_PROVIDER.get().queue(new InstallUpdate(this, packageNames, suppressTests));
  }

  /**
   * Queues the given {@code packageName} for installation by adding it to the update queue for asynchronous processing.
   * <p>
   * Note that this method only adds the package to the queue for installation; it does not perform the installation itself.
   * Use {@link #install(Collection, Runnable, boolean)} to actually install the package.
   *
   * @param packageName the name of the package to be installed
   */
  public final void queueInstall(@NotNull String packageName) {
    queueInstall(Collections.singletonList(packageName), false);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    PackageManagerAdapter adapter = (PackageManagerAdapter)o;

    if (!mySdk.equals(adapter.mySdk)) {
      return false;
    }
    return myProject != null ? myProject.equals(adapter.myProject) : adapter.myProject == null;
  }

  @Override
  public int hashCode() {
    int result = mySdk.hashCode();
    result = 31 * result + (myProject != null ? myProject.hashCode() : 0);
    return result;
  }

  public @NotNull Sdk getSdk() {
    return mySdk;
  }

  private @Nullable Project getProject() {
    return myProject;
  }

  /**
   * Installs {@code libraryNames} into {@code sdk} and invoking a {@code callback} if any
   */
  public static void installModules(@NotNull Sdk sdk,
                                    @Nullable Project project,
                                    @NotNull Collection<String> libraryNames,
                                    @Nullable Runnable actionCallback,
                                    boolean suppressTests) {
    installModules(PackageManagerAdapterFactory.create(sdk, project), libraryNames, actionCallback, suppressTests);
  }

  private static void installModules(@NotNull PackageManagerAdapter adapter,
                                     @NotNull Collection<String> libraryNames,
                                     @Nullable Runnable actionCallback,
                                     boolean suppressTests) {
    adapter.queueInstall(libraryNames, suppressTests);
    if (actionCallback != null) {
      actionCallback.run();
    }
  }

  public static @NotNull AnAction createInstallAction(@NotNull Sdk sdk,
                                                      @Nullable Project project,
                                                      @NotNull Collection<String> libraryNames,
                                                      @Nullable Runnable actionCallback) {
    return createInstallAction(PackageManagerAdapterFactory.create(sdk, project), libraryNames, actionCallback);
  }

  @VisibleForTesting
  public static @NotNull PerlDumbAwareAction createInstallAction(@NotNull PackageManagerAdapter adapter,
                                                                 @NotNull Collection<String> libraryNames,
                                                                 @Nullable Runnable actionCallback) {
    @NotNull String toolName = adapter.getManagerScriptName();
    return new PerlDumbAwareAction(PerlBundle.message("perl.quickfix.install.family", toolName)) {
      @Override
      public void actionPerformed(@NotNull AnActionEvent e) {
        installModules(adapter, libraryNames, actionCallback, true);
      }
    };
  }

  public static void installCpanminus(@Nullable Project project) {
    @Nullable Sdk sdk = PerlProjectManager.getSdk(project);
    if (sdk == null) {
      LOG.debug("No sdk for installation");
      return;
    }
    PackageManagerAdapterFactory.create(sdk, project).queueInstall(CPANMINUS_PACKAGE_NAME);
  }

  private static final class InstallUpdate extends Update {
    private final @NotNull PackageManagerAdapter myAdapter;
    private final boolean mySuppressTests;
    private final @NotNull Set<String> myPackages = new LinkedHashSet<>();

    public InstallUpdate(@NotNull PackageManagerAdapter adapter, @NotNull Collection<String> packageNames, boolean suppressTests) {
      super(Pair.create(adapter, packageNames));
      myAdapter = adapter;
      myPackages.addAll(packageNames);
      mySuppressTests = suppressTests;
    }

    @Override
    public void run() {
      myAdapter.doInstall(myPackages, null, mySuppressTests);
    }

    @Override
    public boolean canEat(@NotNull Update update) {
      if (!(update instanceof InstallUpdate installUpdate)) {
        return super.canEat(update);
      }
      myPackages.addAll(installUpdate.myPackages);
      return true;
    }
  }

  @TestOnly
  public static void waitForAllExecuted() throws TimeoutException {
    QUEUE_PROVIDER.getValue().waitForAllExecuted(2, TimeUnit.MINUTES);
  }
}
