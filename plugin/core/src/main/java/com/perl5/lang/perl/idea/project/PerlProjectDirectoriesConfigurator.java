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

package com.perl5.lang.perl.idea.project;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.startup.StartupActivity;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.openapi.vfs.newvfs.BulkFileListener;
import com.intellij.openapi.vfs.newvfs.events.VFileEvent;
import com.intellij.util.ui.update.MergingUpdateQueue;
import com.intellij.util.ui.update.Update;
import com.intellij.workspaceModel.ide.JpsProjectLoadedListener;
import com.perl5.lang.perl.util.PerlPluginUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PerlProjectDirectoriesConfigurator implements StartupActivity {
  private final MergingUpdateQueue myScanningQueue =
    new MergingUpdateQueue("Project configuration", 300, true, null, ApplicationManager.getApplication(), null, true);

  @Override
  public void runActivity(@NotNull Project project) {
    if (project.isDefault()) {
      return;
    }
    var connection = project.getMessageBus().connect(PerlPluginUtil.getUnloadAwareDisposable(project));
    connection.subscribe(
      VirtualFileManager.VFS_CHANGES,
      new BulkFileListener() {
        @Override
        public void after(@NotNull List<? extends @NotNull VFileEvent> events) {
          scheduleUpdateIfNecessary(project, events);
        }
      });
    connection.subscribe(JpsProjectLoadedListener.Companion.getLOADED(),  (JpsProjectLoadedListener)()->queueRescan(project));
  }

  private static void configureContentRoots(@NotNull Project project) {
    if (project.isDefault() || project.isDisposed()) {
      return;
    }
    var collector = new PerlDirectoryInfoCollector(project);
    for (Module module : ModuleManager.getInstance(project).getModules()) {
      for (VirtualFile contentRoot : ModuleRootManager.getInstance(module).getContentRoots()) {
        PerlDirectoryConfigurationProvider.EP_NAME.forEachExtensionSafe(it -> it.configureContentRoot(module, contentRoot, collector));
      }
      collector.commitExcluded(module);
      collector.commitLibRoots(module);
      collector.commitTestRoots(module);
    }
    collector.commitExternalLibRoots(project);
  }

  private void scheduleUpdateIfNecessary(@NotNull Project project, @NotNull List<? extends @NotNull VFileEvent> events) {
    if (isMyEvent(project, events)) {
      queueRescan(project);
    }
  }

  private void queueRescan(@NotNull Project project) {
    if( ApplicationManager.getApplication().isUnitTestMode()){
      return;
    }
    myScanningQueue.queue(new Update(project) {
      @Override
      public void run() {
        configureContentRoots(project);
      }
    });
  }

  private static boolean isMyEvent(@NotNull Project project, @NotNull List<? extends @NotNull VFileEvent> events) {
    var projectFileIndex = ProjectFileIndex.getInstance(project);
    for (VFileEvent vfsEvent : events) {
      var virtualFile = vfsEvent.getFile();
      if( virtualFile != null && virtualFile.isDirectory() && projectFileIndex.isInContent(virtualFile)){
        return true;
      }
    }
    return false;
  }
}