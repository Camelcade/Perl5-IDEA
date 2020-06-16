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
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.Processor;
import com.intellij.util.ui.update.MergingUpdateQueue;
import com.intellij.util.ui.update.Update;
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
  private final MergingUpdateQueue myQueue = new MergingUpdateQueue("Perl names cache updater", 1000, true, null, this, null, false);
  private final Project myProject;
  private volatile Set<String> myKnownSubs = Collections.emptySet();
  private volatile Set<String> myKnownNamespaces = Collections.emptySet();

  public PerlNamesCache(Project project) {
    myProject = project;
  }

  private void queueUpdate() {
    myQueue.queue(Update.create(this, this::doUpdateCache));
  }

  private void doUpdateCache() {
    if (LightEdit.owns(myProject)) {
      return;
    }
    if (DumbService.isDumb(myProject)) {
      queueUpdate();
      return;
    }
    LOG.debug("Strting to update cache");
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
    LOG.debug("Finished updating cache");
  }


  public void forceCacheUpdate() {
    doUpdateCache();
  }

  @Override
  public void dispose() {
  }

  public Set<String> getSubsNamesSet() {
    queueUpdate();
    return myKnownSubs;
  }

  public Set<String> getNamespacesNamesSet() {
    queueUpdate();
    return myKnownNamespaces;
  }

  public static @NotNull PerlNamesCache getInstance(@NotNull Project project) {
    return project.getService(PerlNamesCache.class);
  }
}