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

package com.perl5.lang.perl.util;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Ref;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.stubs.StubIndex;
import com.intellij.util.Processor;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.references.PerlImplicitDeclarationsService;
import com.perl5.lang.perl.psi.stubs.subsdeclarations.PerlSubDeclarationIndex;
import com.perl5.lang.perl.psi.stubs.subsdeclarations.PerlSubDeclarationReverseIndex;
import com.perl5.lang.perl.psi.stubs.subsdefinitions.PerlLightSubDefinitionsIndex;
import com.perl5.lang.perl.psi.stubs.subsdefinitions.PerlLightSubDefinitionsReverseIndex;
import com.perl5.lang.perl.psi.stubs.subsdefinitions.PerlSubDefinitionReverseIndex;
import com.perl5.lang.perl.psi.stubs.subsdefinitions.PerlSubDefinitionsIndex;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;


public final class PerlSubUtil {
  private PerlSubUtil() {
  }

  /**
   * Searching project files for sub definitions by specific package and function name
   *
   * @param project       project to search in
   * @param canonicalName canonical function name package::name
   * @return Collection of found definitions
   */
  public static Collection<PerlSubDefinitionElement> getSubDefinitions(@NotNull Project project,
                                                                       @Nullable String canonicalName) {
    return getSubDefinitions(project, canonicalName, GlobalSearchScope.allScope(project));
  }

  public static Collection<PerlSubDefinitionElement> getSubDefinitions(@NotNull Project project,
                                                                       @Nullable String canonicalName,
                                                                       @NotNull GlobalSearchScope scope) {
    if (canonicalName == null) {
      return Collections.emptyList();
    }
    List<PerlSubDefinitionElement> result = new ArrayList<>();
    processSubDefinitions(project, canonicalName, scope, result::add);
    return result;
  }

  public static Collection<PerlSubDefinitionElement> getSubDefinitionsInPackage(@NotNull Project project,
                                                                                @NotNull String packageName) {
    return getSubDefinitionsInPackage(project, packageName, GlobalSearchScope.allScope(project));
  }

  public static Collection<PerlSubDefinitionElement> getSubDefinitionsInPackage(@NotNull Project project,
                                                                                @NotNull String packageName,
                                                                                @NotNull GlobalSearchScope scope) {
    List<PerlSubDefinitionElement> result = new ArrayList<>();
    processSubDefinitionsInPackage(project, packageName, scope, result::add);
    return result;
  }

  public static boolean processSubDefinitionsInPackage(@NotNull Project project,
                                                       @NotNull String packageName,
                                                       @NotNull GlobalSearchScope scope,
                                                       @NotNull Processor<? super PerlSubDefinitionElement> processor) {
    return PerlImplicitDeclarationsService.getInstance(project).processSubsInPackage(packageName, processor) &&
           PerlSubDefinitionReverseIndex.getInstance().processElements(project, packageName, scope, processor) &&
           PerlLightSubDefinitionsReverseIndex.getInstance().processLightElements(project, packageName, scope, processor);
  }

  public static boolean processSubDeclarationsInPackage(@NotNull Project project,
                                                        @NotNull String packageName,
                                                        @NotNull GlobalSearchScope scope,
                                                        @NotNull Processor<? super PerlSubDeclarationElement> processor) {
    return PerlSubDeclarationReverseIndex.getInstance().processElements(project, packageName, scope, processor);
  }

  public static boolean processSubDefinitions(@NotNull Project project,
                                              @NotNull String canonicalName,
                                              @NotNull GlobalSearchScope scope,
                                              @NotNull Processor<? super PerlSubDefinitionElement> processor) {
    return PerlImplicitDeclarationsService.getInstance(project).processSubs(canonicalName, processor) &&
           PerlSubDefinitionsIndex.getInstance().processElements(project, canonicalName, scope, processor) &&
           PerlLightSubDefinitionsIndex.getInstance().processLightElements(project, canonicalName, scope, processor);
  }

  public static boolean processSubDeclarations(@NotNull Project project,
                                               @NotNull String canonicalName,
                                               @NotNull GlobalSearchScope scope,
                                               @NotNull Processor<? super PerlSubDeclarationElement> processor) {
    return PerlSubDeclarationIndex.getInstance().processElements(project, canonicalName, scope, processor);
  }


  /**
   * Returns list of defined subs names
   *
   * @param project project to search in
   * @return collection of sub names
   */
  public static Collection<String> getDefinedSubsNames(Project project) {
    // fixme honor scope
    Collection<String> result = PerlSubDefinitionsIndex.getInstance().getAllNames(project);
    result.addAll(PerlLightSubDefinitionsIndex.getInstance().getAllNames(project));
    return result;
  }

  /**
   * Searching project files for sub declarations by specific package and function name
   *
   * @param project       project to search in
   * @param canonicalName canonical function name package::name
   * @return Collection of found definitions
   */
  public static Collection<PerlSubDeclarationElement> getSubDeclarations(Project project, String canonicalName) {
    return getSubDeclarations(project, canonicalName, GlobalSearchScope.allScope(project));
  }

  public static Collection<PerlSubDeclarationElement> getSubDeclarations(Project project, String canonicalName, GlobalSearchScope scope) {
    if (canonicalName == null) {
      return Collections.emptyList();
    }
    return StubIndex.getElements(PerlSubDeclarationIndex.KEY, canonicalName, project, scope, PerlSubDeclarationElement.class);
  }


  public static @NotNull List<PerlSubElement> collectOverridingSubs(PerlSubElement subBase) {
    return collectOverridingSubs(subBase, new HashSet<>());
  }

  public static @NotNull List<PerlSubElement> collectOverridingSubs(@NotNull PerlSubElement subElement,
                                                                    @NotNull Set<? super String> recursionSet) {
    List<PerlSubElement> result;
    result = new ArrayList<>();
    for (PerlSubElement directDescendant : getDirectOverridingSubs(subElement)) {
      String packageName = directDescendant.getNamespaceName();
      if (StringUtil.isNotEmpty(packageName) && !recursionSet.contains(packageName)) {
        recursionSet.add(packageName);
        result.add(directDescendant);
        result.addAll(collectOverridingSubs(directDescendant, recursionSet));
      }
    }

    return result;
  }

  public static boolean processRelatedSubsInPackage(@NotNull Project project,
                                                    @NotNull GlobalSearchScope searchScope,
                                                    @NotNull String packageName,
                                                    @NotNull Processor<? super PerlCallableElement> processor) {
    return processSubDefinitionsInPackage(project, packageName, searchScope, processor) &&
           processSubDeclarationsInPackage(project, packageName, searchScope, processor);
  }

  public static @Nullable PerlSubElement getDirectSuperMethod(@NotNull PerlSubElement subElement) {

    if (!subElement.isMethod()) {
      return null;
    }

    Ref<PerlSubElement> resultRef = Ref.create();
    PerlMroUtil.processCallables(
      subElement.getProject(), subElement.getResolveScope(), subElement.getNamespaceName(), Collections.singleton(subElement.getSubName()),
      true,
      it -> {
        if (it instanceof PerlSubElement subElement1) {
          resultRef.set(subElement1);
          return false;
        }
        return true;
      },
      true);

    return resultRef.get();
  }

  public static @NotNull PerlSubElement getTopmostSuperMethod(PerlSubElement subElement) {
    Set<String> classRecursion = new HashSet<>();

    PerlSubElement run = subElement;
    while (true) {
      String packageName = run.getNamespaceName();
      if (StringUtil.isEmpty(packageName) || classRecursion.contains(packageName)) {
        return run;
      }
      classRecursion.add(packageName);
      PerlSubElement newRun = getDirectSuperMethod(run);
      if (newRun == null) {
        return run;
      }
      run = newRun;
    }
  }

  public static List<PerlSubDefinitionElement> getDirectOverridingSubs(@NotNull PerlSubElement subElement) {
    List<PerlSubDefinitionElement> result = new ArrayList<>();
    processDirectOverridingSubs(subElement, (Processor<? super PerlSubDefinitionElement>)result::add);
    return result;
  }

  @SuppressWarnings({"UnusedReturnValue", "SameReturnValue"})
  public static boolean processDirectOverridingSubs(@NotNull PerlSubElement subElement,
                                                    @NotNull Processor<? super PerlSubDefinitionElement> processor) {
    String packageName = subElement.getNamespaceName();
    String subName = subElement.getSubName();
    if (packageName == null || subName == null) {
      return true;
    }
    Set<String> recursionSet = new HashSet<>();
    Project project = subElement.getProject();
    Queue<String> packagesToProcess = new ArrayDeque<>(5);
    packagesToProcess.add(packageName);

    while (!packagesToProcess.isEmpty()) {
      packageName = packagesToProcess.poll();
      NAMESPACE:
      for (PerlNamespaceDefinitionElement childNamespace : PerlPackageUtil.getChildNamespaces(project, packageName)) {
        String childNamespaceName = childNamespace.getNamespaceName();
        if (StringUtil.isNotEmpty(childNamespaceName) && recursionSet.add(childNamespaceName)) {
          for (PerlSubDefinitionElement subDefinition : PerlSubUtil.getSubDefinitionsInPackage(project, childNamespaceName)) {
            if (subName.equals(subDefinition.getSubName()) && getDirectSuperMethod(subDefinition) == subElement) {
              processor.process(subDefinition);
              continue NAMESPACE;
            }
          }
          packagesToProcess.add(childNamespaceName);
        }
      }
    }
    return true;
  }
}
