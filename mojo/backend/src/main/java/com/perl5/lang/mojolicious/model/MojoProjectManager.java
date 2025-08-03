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

package com.perl5.lang.mojolicious.model;

import com.intellij.ide.projectView.ProjectView;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.util.concurrency.Semaphore;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.messages.MessageBusConnection;
import com.intellij.util.ui.update.MergingUpdateQueue;
import com.intellij.util.ui.update.Update;
import com.perl5.lang.mojolicious.MojoUtil;
import com.perl5.lang.mojolicious.idea.modules.MojoTemplateMarkSourceRootAction;
import com.perl5.lang.perl.idea.actions.PerlMarkLibrarySourceRootAction;
import com.perl5.lang.perl.psi.PerlNamespaceDefinitionElement;
import com.perl5.lang.perl.util.PerlPackageUtil;
import com.perl5.lang.perl.util.PerlPackageUtilCore;
import com.perl5.lang.perl.util.PerlPluginUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BooleanSupplier;

import static com.perl5.lang.mojolicious.model.MojoProjectListener.MOJO_PROJECT_TOPIC;

public class MojoProjectManager implements Disposable {
  static final Logger LOG = Logger.getInstance(MojoProjectManager.class);
  private final @NotNull Project myProject;
  private final @NotNull MergingUpdateQueue myUpdateQueue;
  private final @NotNull AtomicBoolean myUpdatingModel = new AtomicBoolean(false);
  private volatile @NotNull Model myModel = new Model(Collections.emptySet());
  private volatile Semaphore myTestSemaphore;

  public MojoProjectManager(@NotNull Project project) {
    myProject = project;
    myUpdateQueue = new MergingUpdateQueue("mojo model updating queue", 500, true, null, this, null, false);
    MessageBusConnection connection = myProject.getMessageBus().connect(this);
    connection.subscribe(DumbService.DUMB_MODE, new DumbService.DumbModeListener() {
      @Override
      public void exitDumbMode() {
        LOG.debug("Exiting dumb mode");
        scheduleUpdate();
      }
    });

    connection.subscribe(MOJO_PROJECT_TOPIC, new MojoProjectListener() {
      @Override
      public void applicationCreated(@NotNull MojoApp mojoApp) {
        ApplicationManager.getApplication().invokeLater(() -> {
          if (PerlPluginUtil.isUnloaded(myProject) || !mojoApp.isValid()) {
            return;
          }
          VirtualFile appRoot = mojoApp.getRoot();
          new PerlMarkLibrarySourceRootAction().markRoot(myProject, appRoot.findChild(PerlPackageUtilCore.DEFAULT_LIB_DIR));
          new MojoTemplateMarkSourceRootAction().markRoot(myProject, appRoot.findChild(MojoUtil.DEFAULT_TEMPLATES_DIR_NAME));
        });
      }

      @Override
      public void pluginCreated(@NotNull MojoPlugin mojoPlugin) {
        ApplicationManager.getApplication().invokeLater(() -> {
          if (myProject.isDisposed() || !mojoPlugin.isValid()) {
            return;
          }
          new PerlMarkLibrarySourceRootAction().markRoot(myProject, mojoPlugin.getRoot().findChild(PerlPackageUtilCore.DEFAULT_LIB_DIR));
        });
      }
    });
  }

  @Override
  public void dispose() {
    // nothing to dispose
  }

  /**
   * @return true iff mojo is available for the project: perl is enabled and mojo installed
   */
  public boolean isMojoAvailable() {
    return MojoUtil.isMojoAvailable(myProject);
  }

  @Contract("null->null")
  public @Nullable MojoProject getMojoProject(@Nullable VirtualFile root) {
    return myModel.myProjectRoots.get(root);
  }

  public @NotNull List<MojoProject> getMojoProjects() {
    return List.copyOf(myModel.myProjectRoots.values());
  }

  /**
   * Queues model update
   */
  void scheduleUpdate() {
    LOG.debug("Scheduling update");
    myUpdateQueue.queue(Update.create(this, this::updateModel));
  }

  /**
   * Attempts to update a model in read action with smart mode. If update fails, re-schedules it
   */
  private void updateModel() {
    LOG.debug("Attempting to update");
    if (!myUpdatingModel.getAndSet(true)) {
      try {
        //noinspection deprecation
        var isFinished = ReadAction.nonBlocking(() -> {
          if (myProject.isDisposed()) {
            LOG.warn("Project is disposed");
            return true;
          }
          if (DumbService.getInstance(myProject).isDumb()) {
            LOG.debug("Dumb mode, rescheduling");
            scheduleUpdate();
            return false;
          }
          else {
            LOG.debug("Performing model update");
            doUpdateModel();
            return true;
          }
        }).executeSynchronously();
        if (myTestSemaphore != null && isFinished) {
          myTestSemaphore.up();
          myTestSemaphore = null;
        }
      }
      finally {
        myUpdatingModel.set(false);
      }
    }
    else {
      LOG.debug("Another update in progress, rescheduling");
      scheduleUpdate();
    }
  }

  /**
   * Performs an update. This method invoked with guaranteed conditions: smart mode, read action and project is alive
   */
  private void doUpdateModel() {
    LOG.debug("Updating model");
    Set<MojoProject> newProjects = findAllProjects();
    Set<MojoProject> oldProjects = myModel.getProjects();
    if (oldProjects.equals(newProjects)) {
      LOG.debug("Model was not changed");
      return;
    }

    LOG.debug("Current projects: ", newProjects);
    LOG.debug("Old projects: ", oldProjects);
    MojoProjectListener projectListener = myProject.getMessageBus().syncPublisher(MOJO_PROJECT_TOPIC);
    Collection<MojoProject> removedProjects = ContainerUtil.subtract(oldProjects, newProjects);
    if (!removedProjects.isEmpty()) {
      LOG.debug("Projects removed: ", removedProjects);
    }
    removedProjects.forEach(projectListener::projectDeleted);
    myModel = new Model(newProjects);
    Collection<MojoProject> createdProjects = ContainerUtil.subtract(newProjects, oldProjects);
    if (!createdProjects.isEmpty()) {
      LOG.debug("Projects created: ", createdProjects);
    }
    createdProjects.forEach(projectListener::projectCreated);
    LOG.debug("Model updated");
    ApplicationManager.getApplication().invokeLater(() -> {
      if (!myProject.isDisposed() && !ApplicationManager.getApplication().isUnitTestMode()) {
        ProjectView.getInstance(myProject).refresh();
      }
    });
  }

  /**
   * @return a set of mojo entities in project
   */
  private @NotNull Set<MojoProject> findAllProjects() {
    if (!MojoUtil.isMojoAvailable(myProject)) {
      LOG.debug("Mojo is not available in project");
      return Collections.emptySet();
    }
    HashSet<MojoProject> result = new HashSet<>();
    List<PerlNamespaceDefinitionElement> applicationClasses = PerlPackageUtil.getChildNamespaces(
      myProject, MojoUtil.MOJO_PACKAGE_NAME, GlobalSearchScope.projectScope(myProject));
    for (PerlNamespaceDefinitionElement namespace : applicationClasses) {
      LOG.debug("Got application class: " + namespace);
      ProgressManager.checkCanceled();
      VirtualFile root = findLibContainer(namespace);
      if (root != null) {
        LOG.debug("App root: " + root);
        result.add(new MojoApp(root));
      }
      else {
        LOG.debug("No app root");
      }
    }

    List<PerlNamespaceDefinitionElement> pluginClasses = PerlPackageUtil.getChildNamespaces(
      myProject, MojoUtil.MOJO_PLUGIN_PACKAGE_NAME, GlobalSearchScope.projectScope(myProject));
    for (PerlNamespaceDefinitionElement namespace : pluginClasses) {
      LOG.debug("Got plugin class: " + namespace);
      ProgressManager.checkCanceled();
      VirtualFile root = findLibContainer(namespace);
      if (root != null) {
        LOG.debug("Plugin root: " + root);
        result.add(new MojoPlugin(root));
      }
      else {
        LOG.debug("No plugin root");
      }
    }
    return result;
  }

  /**
   * @return a directory in project, containing a {@code lib} directory, containing current class or null
   */
  private @Nullable VirtualFile findLibContainer(@NotNull PerlNamespaceDefinitionElement namespaceDefinition) {
    VirtualFile namespaceFile = PsiUtilCore.getVirtualFile(namespaceDefinition);
    if (namespaceFile == null) {
      LOG.debug("Namespace without a virtual file");
      return null;
    }
    VirtualFile libDirectory = VfsUtilCore.findContainingDirectory(namespaceFile, PerlPackageUtilCore.DEFAULT_LIB_DIR);
    if (libDirectory == null) {
      LOG.debug("No containing lib dir found");
      return null;
    }
    VirtualFile root = libDirectory.getParent();
    if (root != null && ProjectFileIndex.getInstance(myProject).isInContent(root)) {
      return root;
    }
    LOG.debug("No root or not in content: " + root);
    return null;
  }

  public static @NotNull MojoProjectManager getInstance(@NotNull Project project) {
    return project.getService(MojoProjectManager.class);
  }

  private static class Model {
    private final @NotNull Map<VirtualFile, MojoProject> myProjectRoots;

    private Model(@NotNull Set<? extends MojoProject> mojoProjects) {
      if (mojoProjects.isEmpty()) {
        myProjectRoots = Collections.emptyMap();
        return;
      }
      Map<VirtualFile, MojoProject> map = new HashMap<>();
      mojoProjects.forEach(it -> map.put(it.getRoot(), it));
      myProjectRoots = Collections.unmodifiableMap(map);
    }

    private Set<MojoProject> getProjects() {
      return myProjectRoots.isEmpty() ? Collections.emptySet() : new HashSet<>(myProjectRoots.values());
    }
  }

  @TestOnly
  public BooleanSupplier updateInTestMode() {
    var semaphore = new Semaphore(1);
    myTestSemaphore = semaphore;
    updateModel();
    return semaphore::isUp;
  }
}
