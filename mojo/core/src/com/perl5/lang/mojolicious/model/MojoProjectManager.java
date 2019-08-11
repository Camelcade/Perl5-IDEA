/*
 * Copyright 2015-2019 Alexandr Evstigneev
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
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProcessCanceledException;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.startup.StartupActivity;
import com.intellij.openapi.startup.StartupManager;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.messages.MessageBusConnection;
import com.intellij.util.ui.update.MergingUpdateQueue;
import com.intellij.util.ui.update.Update;
import com.perl5.lang.mojolicious.MojoUtil;
import com.perl5.lang.mojolicious.idea.modules.MojoTemplateMarkSourceRootAction;
import com.perl5.lang.perl.idea.actions.PerlMarkLibrarySourceRootAction;
import com.perl5.lang.perl.psi.PerlNamespaceDefinitionElement;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.perl5.lang.mojolicious.model.MojoProjectListener.MOJO_PROJECT_TOPIC;

public class MojoProjectManager {
  static final Logger LOG = Logger.getInstance(MojoProjectManager.class);
  @NotNull
  private final Project myProject;
  @NotNull
  private final MergingUpdateQueue myUpdateQueue;
  @NotNull
  private final AtomicBoolean myUpdatingModel = new AtomicBoolean(false);
  @NotNull
  private volatile Model myModel = new Model(Collections.emptySet());

  public MojoProjectManager(@NotNull Project project) {
    myProject = project;
    myUpdateQueue = new MergingUpdateQueue("mojo model updating queue", 500, true, null, project, null, false);
    MessageBusConnection connection = myProject.getMessageBus().connect();
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
          if (myProject.isDisposed() || !mojoApp.isValid()) {
            return;
          }
          VirtualFile appRoot = mojoApp.getRoot();
          new PerlMarkLibrarySourceRootAction().markRoot(myProject, appRoot.findChild(PerlPackageUtil.DEFAULT_LIB_DIR));
          new MojoTemplateMarkSourceRootAction().markRoot(myProject, appRoot.findChild(MojoUtil.DEFAULT_TEMPLATES_DIR_NAME));
        });
      }

      @Override
      public void pluginCreated(@NotNull MojoPlugin mojoPlugin) {
        ApplicationManager.getApplication().invokeLater(() -> {
          if (myProject.isDisposed() || !mojoPlugin.isValid()) {
            return;
          }
          new PerlMarkLibrarySourceRootAction().markRoot(myProject, mojoPlugin.getRoot().findChild(PerlPackageUtil.DEFAULT_LIB_DIR));
        });
      }
    });
  }

  /**
   * @return true iff mojo is available for the project: perl is enabled and mojo installed
   */
  public boolean isMojoAvailable() {
    return MojoUtil.isMojoAvailable(myProject);
  }

  @Contract("null->null")
  @Nullable
  public MojoProject getMojoProject(@Nullable VirtualFile root) {
    return myModel.myProjectRoots.get(root);
  }

  @NotNull
  public List<MojoProject> getMojoProjects() {
    return Collections.unmodifiableList(new ArrayList<>(myModel.myProjectRoots.values()));
  }

  /**
   * Queues model update
   */
  public void scheduleUpdate() {
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
        ReadAction.run(() -> {
          if (myProject.isDisposed()) {
            LOG.warn("Project is disposed");
            return;
          }
          if (DumbService.getInstance(myProject).isDumb()) {
            LOG.debug("Dumb mode, rescheduling");
            scheduleUpdate();
          }
          else {
            doUpdateModel();
          }
        });
      }
      catch (ProcessCanceledException e) {
        LOG.debug("Update was cancelled, rescheduling");
        scheduleUpdate();
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

    if (LOG.isDebugEnabled()) {
      LOG.debug("Current projects: " + newProjects);
      LOG.debug("Old projects: " + newProjects);
    }
    MojoProjectListener projectListener = myProject.getMessageBus().syncPublisher(MOJO_PROJECT_TOPIC);
    Collection<MojoProject> removedProjects = ContainerUtil.subtract(oldProjects, newProjects);
    if (LOG.isDebugEnabled() && !removedProjects.isEmpty()) {
      LOG.debug("Projects removed: " + removedProjects);
    }
    removedProjects.forEach(projectListener::projectDeleted);
    myModel = new Model(newProjects);
    Collection<MojoProject> createdProjects = ContainerUtil.subtract(newProjects, oldProjects);
    if (LOG.isDebugEnabled() && !createdProjects.isEmpty()) {
      LOG.debug("Projects created: " + createdProjects);
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
  @NotNull
  private Set<MojoProject> findAllProjects() {
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
  @Nullable
  private VirtualFile findLibContainer(@NotNull PerlNamespaceDefinitionElement namespaceDefinition) {
    VirtualFile namespaceFile = PsiUtilCore.getVirtualFile(namespaceDefinition);
    if (namespaceFile == null) {
      LOG.debug("Namespace without a virtual file");
      return null;
    }
    VirtualFile libDirectory = VfsUtilCore.findContainingDirectory(namespaceFile, PerlPackageUtil.DEFAULT_LIB_DIR);
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

  @NotNull
  public static MojoProjectManager getInstance(@NotNull Project project) {
    return ServiceManager.getService(project, MojoProjectManager.class);
  }

  public static class Starter implements StartupActivity, DumbAware {
    @Override
    public void runActivity(@NotNull Project project) {
      if (project.isDefault()) {
        return;
      }
      StartupManager.getInstance(project).runWhenProjectIsInitialized(() -> {
        ReadAction.run(() -> {
          if (!project.isDisposed()) {
            LOG.debug("Project is initialized");
            getInstance(project).scheduleUpdate();
          }
        });
      });
    }
  }

  private static class Model {
    @NotNull
    private final Map<VirtualFile, MojoProject> myProjectRoots;

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
  public void updateInTestMode() {
    updateModel();
  }
}
