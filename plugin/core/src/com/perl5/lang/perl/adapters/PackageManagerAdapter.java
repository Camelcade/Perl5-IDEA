/*
 * Copyright 2015-2020 Alexandr Evstigneev
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
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.ui.update.MergingUpdateQueue;
import com.intellij.util.ui.update.Update;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.idea.execution.PerlCommandLine;
import com.perl5.lang.perl.idea.sdk.host.PerlHostData;
import com.perl5.lang.perl.util.PerlPluginUtil;
import com.perl5.lang.perl.util.PerlRunUtil;
import com.pty4j.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public abstract class PackageManagerAdapter {
  private static final MergingUpdateQueue QUEUE =
    new MergingUpdateQueue("perl.installer.queue", 300, true, null, PerlPluginUtil.getUnloadAwareDisposable());

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
  protected @NotNull List<String> getInstallParameters(@NotNull Collection<String> packageNames) {
    return new ArrayList<>(packageNames);
  }

  /**
   * Installs a package with {@code packageName} with output in console
   */
  private void doInstall(@NotNull Collection<String> packageNames) {
    ApplicationManager.getApplication().assertIsDispatchThread();
    if (myProject != null && myProject.isDisposed()) {
      return;
    }
    VirtualFile script = PerlRunUtil.findLibraryScriptWithNotification(
      getSdk(), getProject(), getManagerScriptName(), getManagerPackageName());
    if (script == null) {
      return;
    }
    String remotePath = PerlHostData.notNullFrom(mySdk).getRemotePath(script.getPath());
    if (remotePath == null) {
      return;
    }
    PerlRunUtil.runInConsole(
      new PerlCommandLine(remotePath)
        .withParameters(getInstallParameters(packageNames))
        .withSdk(getSdk())
        .withProject(getProject())
        .withConsoleTitle(PerlBundle.message("perl.cpan.console.installing", StringUtil.join(packageNames, ", ")))
        .withProcessListener(new ProcessAdapter() {
          @Override
          public void processTerminated(@NotNull ProcessEvent event) {
            PerlRunUtil.refreshSdkDirs(mySdk, myProject);
          }
        })
    );
  }

  public final void install(@NotNull Collection<String> packageNames) {
    QUEUE.queue(new InstallUpdate(this, packageNames));
  }

  public final void install(@NotNull String packageName) {
    install(Collections.singletonList(packageName));
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
   * Creates an adapter, prefers cpanminus over cpan
   */
  public static @NotNull PackageManagerAdapter create(@NotNull Sdk sdk, @Nullable Project project) {
    return CpanminusAdapter.isAvailable(sdk) ? new CpanminusAdapter(sdk, project) : new CpanAdapter(sdk, project);
  }

  private static final class InstallUpdate extends Update {
    private final @NotNull PackageManagerAdapter myAdapter;

    private final @NotNull Set<String> myPackages = new HashSet<>();

    public InstallUpdate(@NotNull PackageManagerAdapter adapter, @NotNull Collection<String> packageNames) {
      super(Pair.create(adapter, packageNames));
      myAdapter = adapter;
      myPackages.addAll(packageNames);
    }

    @Override
    public void run() {
      myAdapter.doInstall(myPackages);
    }

    @Override
    public boolean canEat(Update update) {
      if (!(update instanceof InstallUpdate)) {
        return super.canEat(update);
      }
      myPackages.addAll(((InstallUpdate)update).myPackages);
      return true;
    }
  }
}
