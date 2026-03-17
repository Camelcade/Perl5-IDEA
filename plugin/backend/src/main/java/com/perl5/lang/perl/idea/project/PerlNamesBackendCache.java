/*
 * Copyright 2015-2026 Alexandr Evstigneev
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

import com.intellij.ide.lightEdit.LightEdit;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootEvent;
import com.intellij.openapi.roots.ModuleRootListener;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiTreeChangeAdapter;
import com.intellij.psi.PsiTreeChangeEvent;
import com.intellij.psi.impl.source.resolve.ResolveCache;
import com.intellij.util.concurrency.AppExecutorUtil;
import com.intellij.util.messages.MessageBusConnection;
import com.intellij.util.ui.update.MergingUpdateQueue;
import com.intellij.util.ui.update.Update;
import com.perl5.lang.perl.psi.stubs.namespaces.PerlLightNamespaceIndex;
import com.perl5.lang.perl.psi.stubs.namespaces.PerlNamespaceIndex;
import com.perl5.lang.perl.psi.stubs.subsdeclarations.PerlSubDeclarationIndex;
import com.perl5.lang.perl.psi.stubs.subsdefinitions.PerlLightSubDefinitionsIndex;
import com.perl5.lang.perl.psi.stubs.subsdefinitions.PerlSubDefinitionsIndex;
import com.perl5.lang.perl.util.PerlPackageUtilCore;
import com.perl5.lang.perl.util.PerlTimeLogger;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;
import org.jspecify.annotations.NonNull;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;


public class PerlNamesBackendCache implements PerlNamesCache {
  private static final Logger LOG = Logger.getInstance(PerlNamesBackendCache.class);
  private final MergingUpdateQueue myQueue = new MergingUpdateQueue("Perl names cache updater", 1000, true, null, this, null, false);
  private final Project myProject;
  private volatile Set<String> myKnownSubs = Collections.emptySet();
  private volatile Set<String> myKnownNamespaces = Collections.emptySet();
  private final AtomicBoolean myIsDisposed = new AtomicBoolean(false);
  private @Nullable Disposable myTestDisposable = null;
  private final Queue<Function0<Unit>> myPendingCallbacks = new ConcurrentLinkedQueue<>();

  public PerlNamesBackendCache(Project project) {
    myProject = project;
    if (LightEdit.owns(myProject) || project.isDefault()) {
      return;
    }
    MessageBusConnection connection = project.getMessageBus().connect(this);
    connection.subscribe(ModuleRootListener.TOPIC, new ModuleRootListener() {
      @Override
      public void rootsChanged(@NotNull ModuleRootEvent event) {
        queueUpdate();
      }
    });
    connection.subscribe(DumbService.DUMB_MODE, new DumbService.DumbModeListener() {
      @Override
      public void exitDumbMode() {
        queueUpdate();
      }
    });
    PsiManager.getInstance(myProject).addPsiTreeChangeListener(new PsiTreeChangeAdapter() {
      @Override
      public void childAdded(@NotNull PsiTreeChangeEvent event) {
        queueUpdate();
      }

      @Override
      public void childRemoved(@NotNull PsiTreeChangeEvent event) {
        queueUpdate();
      }

      @Override
      public void childReplaced(@NotNull PsiTreeChangeEvent event) {
        queueUpdate();
      }

      @Override
      public void childMoved(@NotNull PsiTreeChangeEvent event) {
        queueUpdate();
      }

      @Override
      public void childrenChanged(@NotNull PsiTreeChangeEvent event) {
        queueUpdate();
      }

      @Override
      public void propertyChanged(@NotNull PsiTreeChangeEvent event) {
        queueUpdate();
      }
    }, this);
  }

  private void queueUpdate() {
    if (!myProject.isDefault()) {
      myQueue.queue(Update.create(this, () -> doUpdateCache(() -> Unit.INSTANCE)));
    }
  }

  private void doUpdateCache(kotlin.jvm.functions.@NonNull Function0<kotlin.Unit> callback) {
    if (myProject.isDefault() || LightEdit.owns(myProject)) {
      return;
    }
    myPendingCallbacks.add(callback);
    var updateNbra = ReadAction.nonBlocking(() -> {
      PerlTimeLogger logger = PerlTimeLogger.create(LOG);
      logger.debug("Starting to update names cache at");

      PerlSubDeclarationIndex subDeclarationIndex = PerlSubDeclarationIndex.getInstance();
      Collection<String> declarationsNames = subDeclarationIndex.getAllNames(myProject);
      Set<String> subsSet = new HashSet<>(declarationsNames);
      logger.debug("Got declarations names: ", declarationsNames.size());
      ProgressManager.checkCanceled();

      PerlSubDefinitionsIndex subDefinitionsIndex = PerlSubDefinitionsIndex.getInstance();
      Collection<String> definitionsNames = subDefinitionsIndex.getAllNames(myProject);
      subsSet.addAll(definitionsNames);
      logger.debug("Got definitions names: ", definitionsNames.size());
      ProgressManager.checkCanceled();

      PerlLightSubDefinitionsIndex lightSubDefinitionsIndex = PerlLightSubDefinitionsIndex.getInstance();
      Collection<String> lightDefinitionsNames = lightSubDefinitionsIndex.getAllNames(myProject);
      subsSet.addAll(lightDefinitionsNames);
      logger.debug("Got light definitions names: ", lightDefinitionsNames.size());
      ProgressManager.checkCanceled();
      var newSubSet = Collections.unmodifiableSet(subsSet);
      if (!newSubSet.equals(myKnownSubs)) {
        myKnownSubs = newSubSet;
        ResolveCache.getInstance(myProject).clearCache(true);
      }

      Set<String> namespacesSet = new HashSet<>(PerlPackageUtilCore.CORE_PACKAGES_ALL);

      PerlNamespaceIndex namespaceIndex = PerlNamespaceIndex.getInstance();
      Collection<String> namespacesNames = namespaceIndex.getAllNames(myProject);
      namespacesSet.addAll(namespacesNames);
      logger.debug("Got namespaces names: ", namespacesNames.size());
      ProgressManager.checkCanceled();

      PerlLightNamespaceIndex lightNamespaceIndex = PerlLightNamespaceIndex.getInstance();
      Collection<String> lightNamespacesNames = lightNamespaceIndex.getAllNames(myProject);
      namespacesSet.addAll(lightNamespacesNames);
      logger.debug("Got light namespaces names: ", lightNamespacesNames.size());
      var newNamespaceSet = Collections.unmodifiableSet(namespacesSet);
      if (!newNamespaceSet.equals(myKnownNamespaces)) {
        myKnownNamespaces = newNamespaceSet;
        ResolveCache.getInstance(myProject).clearCache(true);
      }

      logger.debug("Names cache updated");

        Function0<kotlin.Unit> pending;
        while ((pending = myPendingCallbacks.poll()) != null) {
          pending.invoke();
        }
      //noinspection ReturnOfNull
      return null;
      }).inSmartMode(myProject)
      .expireWith(this)
      .coalesceBy(this);

    if (myTestDisposable != null) {
      updateNbra = updateNbra.expireWith(myTestDisposable);
    }

    updateNbra.submit(AppExecutorUtil.getAppExecutorService());
  }

  @Override
  public boolean isDisposed() {
    return myIsDisposed.get();
  }

  @Override
  public void forceCacheUpdate(kotlin.jvm.functions.@NonNull Function0<kotlin.Unit> callback) {
    var application = ApplicationManager.getApplication();
    if (application.isDispatchThread()) {
      application.executeOnPooledThread(() -> doUpdateCache(callback));
    }
    else {
      doUpdateCache(callback);
    }
  }

  @Override
  @TestOnly
  public void cleanCache() {
    myKnownSubs = Collections.emptySet();
    myKnownNamespaces = Collections.emptySet();
  }

  @Override
  public void dispose() {
    WriteAction.run(() -> myIsDisposed.set(true));
  }

  @Override
  public @NotNull Set<String> getSubsNamesSet() {
    return myKnownSubs;
  }

  @Override
  public @NotNull Set<String> getNamespacesNamesSet() {
    return myKnownNamespaces;
  }

  @TestOnly
  public void setTestDisposable(@NotNull Disposable disposable) {
    myTestDisposable = disposable;
  }
}