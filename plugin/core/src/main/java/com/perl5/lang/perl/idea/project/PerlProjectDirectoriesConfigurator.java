/*
 * Copyright 2015-2024 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.project;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.openapi.vfs.newvfs.BulkFileListener;
import com.intellij.openapi.vfs.newvfs.events.VFileEvent;
import com.intellij.util.concurrency.AppExecutorUtil;
import com.intellij.util.ui.update.MergingUpdateQueue;
import com.intellij.util.ui.update.Update;
import com.intellij.workspaceModel.ide.JpsProjectLoadedListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;
import org.jetbrains.concurrency.CancellablePromise;

import java.util.List;
import java.util.Objects;

@Service(Service.Level.PROJECT)
public final class PerlProjectDirectoriesConfigurator implements Disposable, BulkFileListener, JpsProjectLoadedListener {
  private final MergingUpdateQueue myScanningQueue = new MergingUpdateQueue("Project configuration", 300, true, null, this, null, false);
  private final @NotNull Project myProject;

  public PerlProjectDirectoriesConfigurator(@NotNull Project project) {
    myProject = project;
    var connection = project.getMessageBus().connect(this);
    connection.subscribe(VirtualFileManager.VFS_CHANGES, this);
    connection.subscribe(JpsProjectLoadedListener.Companion.getLOADED(), this);
  }

  @Override
  public void loaded() {
    queueRescan();
  }

  @Override
  public void after(@NotNull List<? extends @NotNull VFileEvent> events) {
    scheduleUpdateIfNecessary(events);
  }

  private static @Nullable CancellablePromise<PerlDirectoryInfoCollector> configureContentRoots(@NotNull Project project) {
    if (project.isDefault()) {
      return null;
    }

    return ReadAction.nonBlocking(() -> {
        var collector = new PerlDirectoryInfoCollector(project);
        for (Module module : ModuleManager.getInstance(project).getModules()) {
          for (VirtualFile contentRoot : ModuleRootManager.getInstance(module).getContentRoots()) {
            PerlDirectoryConfigurationProvider.EP_NAME.forEachExtensionSafe(it -> it.configureContentRoot(module, contentRoot, collector));
          }
        }
        return collector;
      }).expireWhen(project::isDisposed)
      .finishOnUiThread(ModalityState.defaultModalityState(), collector -> WriteAction.run(collector::commit))
      .coalesceBy(project, PerlProjectDirectoriesConfigurator.class)
      .submit(AppExecutorUtil.getAppExecutorService());
  }

  private void scheduleUpdateIfNecessary(@NotNull List<? extends @NotNull VFileEvent> events) {
    if (isMyEvent(events)) {
      queueRescan();
    }
  }

  void queueRescan() {
    if( ApplicationManager.getApplication().isUnitTestMode()){
      return;
    }
    myScanningQueue.queue(new Update(myProject) {
      @Override
      public void run() {
        configureContentRoots(myProject);
      }
    });
  }

  private boolean isMyEvent(@NotNull List<? extends @NotNull VFileEvent> events) {
    var projectFileIndex = ProjectFileIndex.getInstance(myProject);
    for (VFileEvent vfsEvent : events) {
      var virtualFile = vfsEvent.getFile();
      if( virtualFile != null && virtualFile.isDirectory() && projectFileIndex.isInContent(virtualFile)){
        return true;
      }
    }
    return false;
  }

  @SuppressWarnings("MethodOnlyUsedFromInnerClass")
  static @Nullable PerlProjectDirectoriesConfigurator getInstance(@NotNull Project project) {
    return project.isDefault() ? null : project.getService(PerlProjectDirectoriesConfigurator.class);
  }

  @Override
  public void dispose() {
  }

  @TestOnly
  public static @NotNull CancellablePromise<PerlDirectoryInfoCollector> configureRoots(@NotNull Project project){
    return Objects.requireNonNull(configureContentRoots(project));
  }
}