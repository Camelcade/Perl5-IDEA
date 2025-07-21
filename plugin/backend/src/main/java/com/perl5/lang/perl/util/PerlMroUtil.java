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
import com.intellij.psi.PsiElement;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.Processor;
import com.perl5.lang.perl.psi.PerlCallableElement;
import com.perl5.lang.perl.psi.PerlGlobVariableElement;
import com.perl5.lang.perl.psi.PerlNamespaceDefinitionElement;
import com.perl5.lang.perl.psi.PerlNamespaceDefinitionHandler;
import com.perl5.lang.perl.psi.mro.PerlIsaCache;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public final class PerlMroUtil {
  private PerlMroUtil() {
  }

  /**
   * Resolving method with current MRO;
   *
   * @return collection of first encountered super subs declarations, definitions, constants and typeglobs
   */
  public static @NotNull Collection<PsiElement> collectCallables(@NotNull Project project,
                                                                 @NotNull GlobalSearchScope searchScope,
                                                                 @Nullable String namespaceName,
                                                                 @Nullable String subName,
                                                                 boolean isSuper) {
    if (subName == null) {
      return Collections.emptyList();
    }
    var result = new ArrayList<PsiElement>();
    processCallables(project, searchScope, namespaceName, Collections.singleton(subName), isSuper, result::add, true);
    return result;
  }

  public static boolean processCallables(@NotNull Project project,
                                         @NotNull GlobalSearchScope searchScope,
                                         @Nullable String namespaceName,
                                         @NotNull Set<String> callableNames,
                                         boolean isSuper,
                                         @NotNull Processor<? super PerlCallableElement> processor,
                                         boolean includeAutoload) {
    if (callableNames.isEmpty()) {
      return true;
    }
    if (namespaceName == null) {
      namespaceName = PerlPackageUtilCore.UNIVERSAL_NAMESPACE;
    }
    Collection<String> linearISA = getLinearISA(project, searchScope, namespaceName, isSuper);

    Ref<String> stopFlag = Ref.create();
    for (String currentNamespaceName : linearISA) {
      for (String subName : callableNames) {
        if (StringUtil.isNotEmpty(subName) && !PerlPackageUtil.processCallables(
          project, searchScope, PerlPackageUtilCore.join(currentNamespaceName, subName),
          it -> {
            stopFlag.set("");
            return processor.process(it);
          })) {
          return false;
        }
      }
      if (!stopFlag.isNull()) {
        return true;
      }
    }

    if (!includeAutoload) {
      return true;
    }

    for (String currentNamespaceName : linearISA) {
      if (PerlPackageUtilCore.isUNIVERSAL(currentNamespaceName)) {
        continue;
      }
      if (!PerlPackageUtil.processCallables(project, searchScope,
                                            PerlPackageUtilCore.join(currentNamespaceName, PerlSubUtilCore.SUB_AUTOLOAD), it -> {
        processor.process(it);
        return false;
      })) {
        return false;
      }
    }

    return true;
  }

  /**
   * Returns collection of Sub Definitions of class and it's superclasses according perl's default MRO
   *
   * @param psiElement        anchorElement
   * @param baseNamespaceName base project
   * @param isSuper           flag for SUPER resolutions
   * @return collection of definitions
   */
  public static Collection<PsiElement> getVariants(@NotNull PsiElement psiElement,
                                                   @Nullable String baseNamespaceName,
                                                   boolean isSuper) {
    if (baseNamespaceName == null) {
      return Collections.emptyList();
    }
    Project project = psiElement.getProject();
    Map<String, PsiElement> methods = new HashMap<>();

    GlobalSearchScope searchScope = psiElement.getResolveScope();
    for (String packageName : getLinearISA(project, psiElement.getResolveScope(), baseNamespaceName, isSuper)) {
      PerlSubUtil.processRelatedSubsInPackage(project, searchScope, packageName, it -> {
        methods.putIfAbsent(it.getName(), it);
        return true;
      });
      for (PerlGlobVariableElement globVariable : PerlGlobUtil.getGlobsDefinitionsInNamespace(project, packageName)) {
        if (globVariable.isLeftSideOfAssignment() && !methods.containsKey(globVariable.getName())) {
          methods.putIfAbsent(globVariable.getName(), globVariable);
        }
      }
    }

    return new ArrayList<>(methods.values());
  }

  /**
   * Building linear @ISA list
   *
   * @param packageName current package name
   * @param isSuper     if false - we include current package into the list, true - otherwise
   * @return list of linear @ISA
   */
  public static List<String> getLinearISA(@NotNull Project project,
                                          @NotNull GlobalSearchScope searchScope,
                                          @NotNull String packageName,
                                          boolean isSuper) {
    var isaCache = PerlIsaCache.getInstance(project);
    var result = isaCache.get(packageName);
    if (result == null) {
      HashSet<String> recursionMap = new HashSet<>();
      result = new ArrayList<>();

      recursionMap.add(packageName);
      result.add(packageName);

      getPackageParents(project, searchScope, packageName, recursionMap, result);

      if (!recursionMap.contains(PerlPackageUtilCore.UNIVERSAL_NAMESPACE)) {
        result.add(PerlPackageUtilCore.UNIVERSAL_NAMESPACE);
      }

      result = isaCache.put(packageName, result);
    }
    return isSuper ? result.subList(1, result.size()) : result;
  }

  public static void getPackageParents(@NotNull Project project,
                                       @NotNull GlobalSearchScope searchScope,
                                       @NotNull String packageName,
                                       @NotNull Set<String> recursionMap,
                                       @NotNull List<String> result) {
    // at the moment we are checking all definitions available
    // fixme we should check only those, which are used in current file
    for (PerlNamespaceDefinitionElement namespaceDefinition : PerlNamespaceUtil.getNamespaceDefinitions(project, searchScope,
                                                                                                        packageName)) {
      PerlNamespaceDefinitionHandler.instance(namespaceDefinition).collectLinearISA(namespaceDefinition, recursionMap, result);
    }
  }
}
