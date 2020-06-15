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

package com.perl5.lang.perl.idea.project;

import com.intellij.ide.lightEdit.LightEdit;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProcessCanceledException;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.Processor;
import com.perl5.lang.perl.psi.PerlNamespaceDefinitionElement;
import com.perl5.lang.perl.psi.PerlSubDeclarationElement;
import com.perl5.lang.perl.psi.PerlSubDefinitionElement;
import com.perl5.lang.perl.psi.stubs.namespaces.PerlLightNamespaceIndex;
import com.perl5.lang.perl.psi.stubs.namespaces.PerlNamespaceIndex;
import com.perl5.lang.perl.psi.stubs.subsdeclarations.PerlSubDeclarationIndex;
import com.perl5.lang.perl.psi.stubs.subsdefinitions.PerlLightSubDefinitionsIndex;
import com.perl5.lang.perl.psi.stubs.subsdefinitions.PerlSubDefinitionsIndex;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


public class PerlNamesCache implements Disposable {
  private static final Logger LOG = Logger.getInstance(PerlNamesCache.class);
  private final NamesCacheUpdater myUpdaterRunner = new NamesCacheUpdater();
  private final Project myProject;
  private Set<String> myKnownSubs = Collections.emptySet();
  private Set<String> myKnownNamespaces = Collections.emptySet();
  private final Runnable myCacheUpdaterWorker = new Runnable() {
    @Override
    public void run() {
      if (LightEdit.owns(myProject)) {
        return;
      }
      ReadAction.nonBlocking(() -> {
        long start = System.currentTimeMillis();
        Set<String> subsSet = new HashSet<>();

        GlobalSearchScope scope = GlobalSearchScope.allScope(myProject);
        Processor<PerlSubDeclarationElement> processor = it -> {
          subsSet.add(it.getCanonicalName());
          return false;
        };
        for (String subName : PerlSubDeclarationIndex.getAllNames(myProject)) {
          ProgressManager.checkCanceled();
          PerlSubDeclarationIndex.processSubDeclarations(myProject, subName, scope, processor);
        }

        Processor<PerlSubDefinitionElement> perlSubDefinitionElementProcessor = it -> {
          subsSet.add(it.getCanonicalName());
          return false;
        };
        for (String subName : PerlSubDefinitionsIndex.getAllNames(myProject)) {
          ProgressManager.checkCanceled();
          PerlSubDefinitionsIndex.processSubDefinitions(myProject, subName, scope, perlSubDefinitionElementProcessor);
        }
        for (String subName : PerlLightSubDefinitionsIndex.getAllNames(myProject)) {
          ProgressManager.checkCanceled();
          PerlLightSubDefinitionsIndex.processSubDefinitions(myProject, subName, scope, perlSubDefinitionElementProcessor);
        }
        myKnownSubs = Collections.unmodifiableSet(subsSet);

        Set<String> namespacesSet = new HashSet<>(PerlPackageUtil.CORE_PACKAGES_ALL);

        Processor<PerlNamespaceDefinitionElement> namespaceDefinitionElementProcessor = it -> {
          namespacesSet.add(it.getNamespaceName());
          return false;
        };
        for (String namespaceName : PerlNamespaceIndex.getAllNames(myProject)) {
          ProgressManager.checkCanceled();
          PerlNamespaceIndex.processNamespaces(myProject, namespaceName, scope, namespaceDefinitionElementProcessor);
        }
        for (String namespaceName : PerlLightNamespaceIndex.getAllNames(myProject)) {
          ProgressManager.checkCanceled();
          PerlLightNamespaceIndex.processNamespaces(myProject, namespaceName, scope, namespaceDefinitionElementProcessor);
        }

        myKnownNamespaces = Collections.unmodifiableSet(namespacesSet);

        LOG.debug("Names cache updated in " + (System.currentTimeMillis() - start) + " ms");
      }).inSmartMode(myProject).expireWhen(myProject::isDisposed).executeSynchronously();
    }
  };
  //	long notifyCounter = 0;
  private boolean isNotified = false;

  public PerlNamesCache(Project project) {
    this.myProject = project;
    Application application = ApplicationManager.getApplication();
    if (!application.isUnitTestMode()) {
      application.executeOnPooledThread(myUpdaterRunner);
    }
  }

  public void forceCacheUpdate() {
    myCacheUpdaterWorker.run();
  }

  @Override
  public void dispose() {
    myUpdaterRunner.stopUpdater();
  }

  public Set<String> getSubsNamesSet() {
    myUpdaterRunner.update();
    return myKnownSubs;
  }

  public Set<String> getNamespacesNamesSet() {
    myUpdaterRunner.update();
    return myKnownNamespaces;
  }

  public static @NotNull PerlNamesCache getInstance(@NotNull Project project) {
    return project.getService(PerlNamesCache.class);
  }

  protected class NamesCacheUpdater implements Runnable {
    private static final long TTL = 1000;
    private boolean stopThis = false;
    private long lastUpdate = 0;

    @Override
    public void run() {

      while (!stopThis) {
        try {
          myCacheUpdaterWorker.run();
        }
        catch (ProcessCanceledException ignore) {
        }

        lastUpdate = System.currentTimeMillis();
        isNotified = false;

        synchronized (this) {
          try {
            wait();
          }
          catch (Exception e) {
            break;
          }
        }
      }
    }

    public void update() {
      if (!isNotified && lastUpdate + TTL < System.currentTimeMillis()) {
        synchronized (this) {
          isNotified = true;
          notify();
        }
      }
    }

    public void stopUpdater() {
      stopThis = true;
      synchronized (this) {
        notify();
      }
    }
  }
}