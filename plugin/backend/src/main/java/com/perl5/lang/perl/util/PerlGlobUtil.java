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

import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.stubs.StubIndex;
import com.intellij.util.Processor;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.PerlGlobVariableElement;
import com.perl5.lang.perl.psi.PsiPerlGlobVariable;
import com.perl5.lang.perl.psi.stubs.globs.PerlGlobStubIndex;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static com.perl5.lang.perl.psi.stubs.globs.PerlGlobNamespaceStubIndex.KEY_GLOB_NAMESPACE;


public final class PerlGlobUtil implements PerlElementTypes {
  private PerlGlobUtil() {
  }

  /**
   * Searching project files for sub definitions by specific package and function name
   *
   * @param project       project to search in
   * @param canonicalName canonical function name package::name
   * @return collection of found definitions
   */
  public static Collection<PsiPerlGlobVariable> getGlobsDefinitions(@NotNull Project project, @Nullable String canonicalName) {
    return getGlobsDefinitions(project, canonicalName, GlobalSearchScope.allScope(project));
  }

  public static Collection<PsiPerlGlobVariable> getGlobsDefinitions(@NotNull Project project,
                                                                    @Nullable String canonicalName,
                                                                    @NotNull GlobalSearchScope scope) {
    if (canonicalName == null) {
      return Collections.emptyList();
    }
    return StubIndex.getElements(PerlGlobStubIndex.KEY_GLOB, canonicalName, project, scope, PsiPerlGlobVariable.class);
  }

  public static @NotNull Collection<PsiPerlGlobVariable> getGlobsDefinitionsInNamespace(@NotNull Project project,
                                                                                        @Nullable String namespaceName) {
    return getGlobsDefinitionsInNamespace(project, namespaceName, GlobalSearchScope.allScope(project));
  }

  public static @NotNull Collection<PsiPerlGlobVariable> getGlobsDefinitionsInNamespace(@NotNull Project project,
                                                                                        @Nullable String namespaceName,
                                                                                        @NotNull GlobalSearchScope scope) {
    if (namespaceName == null) {
      return Collections.emptyList();
    }
    return StubIndex.getElements(KEY_GLOB_NAMESPACE, namespaceName, project, scope, PsiPerlGlobVariable.class);
  }

  /**
   * Returns list of known stubbed globs
   *
   * @param project project to search in
   * @return collection of globs names
   */
  public static Collection<String> getDefinedGlobsNames(Project project) {
    return PerlStubUtil.getAllKeys(PerlGlobStubIndex.KEY_GLOB, GlobalSearchScope.allScope(project));
  }

  @SuppressWarnings("StaticMethodOnlyUsedInOneClass")
  public static boolean processGlobs(@NotNull Project project,
                                     @NotNull GlobalSearchScope scope,
                                     @Nullable String namespaceName,
                                     boolean processAll,
                                     @NotNull Processor<? super PerlGlobVariableElement> processor) {
    Set<String> namespacesToProcess;
    if (namespaceName == null) {
      namespacesToProcess = new HashSet<>(getDefinedGlobsNames(project));
    }
    else {
      namespacesToProcess = Collections.singleton(namespaceName);
    }
    Set<String> processedNames = processAll ? null : new HashSet<>();
    for (String namespace : namespacesToProcess) {
      if (!StubIndex.getInstance().processElements(KEY_GLOB_NAMESPACE, namespace, project, scope, PsiPerlGlobVariable.class, it -> {
        ProgressManager.checkCanceled();
        String canonicalName = it.getCanonicalName();
        if (processAll || processedNames.add(canonicalName)) {
          return processor.process(it);
        }
        return true;
      })){
        return false;
      }
    }
    return true;
  }

  @SuppressWarnings({"StaticMethodOnlyUsedInOneClass", "UnusedReturnValue"})
  public static boolean processGlobsByName(@NotNull Project project,
                                           @NotNull GlobalSearchScope scope,
                                           @NotNull String canonicalName,
                                           @NotNull Processor<? super PerlGlobVariableElement> processor,
                                           boolean processAll) {
    Set<String> processedNames = processAll ? null : new HashSet<>();
    return StubIndex.getInstance().processElements(PerlGlobStubIndex.KEY_GLOB, canonicalName, project, scope, PsiPerlGlobVariable.class, it -> {
      ProgressManager.checkCanceled();
      String globName = it.getCanonicalName();
      if (processAll || processedNames.add(globName)) {
        return processor.process(it);
      }
      return true;
    });
  }
}

