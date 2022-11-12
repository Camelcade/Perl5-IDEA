/*
 * Copyright 2015-2022 Alexandr Evstigneev
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
import com.intellij.openapi.util.text.StringUtil;
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
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.perl5.lang.perl.psi.stubs.globs.PerlGlobNamespaceStubIndex.KEY_GLOB_NAMESPACE;


public final class PerlGlobUtil implements PerlElementTypes {
  private PerlGlobUtil() {
  }

  public static final Set<String> BUILT_IN = Stream.of(
      Set.of("ARGV", "STDERR", "STDOUT", "ARGVOUT", "STDIN"),
      PerlBuiltInScalars.BUILT_IN,
      //PerlBuiltInSubs.BUILT_IN,
      PerlArrayUtil.BUILT_IN,
      PerlHashUtil.BUILT_IN)
    .flatMap(Set::stream)
    .collect(Collectors.toUnmodifiableSet());

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

  public static Collection<PsiPerlGlobVariable> getGlobsDefinitionsInPackage(@NotNull Project project, @Nullable String namespaceName) {
    return getGlobsDefinitionsInPackage(project, namespaceName, GlobalSearchScope.allScope(project));
  }

  public static Collection<PsiPerlGlobVariable> getGlobsDefinitionsInPackage(@NotNull Project project,
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

  /**
   * Processes all globs names with specific processor
   * fixme: this need to be refactored, duplicates variable logic
   *
   * @param processAll    if false, only one entry per name going to be processed. May be need when filling completion
   * @param namespaceName optional namespace filter
   */
  public static boolean processDefinedGlobs(@NotNull Project project,
                                            @NotNull GlobalSearchScope scope,
                                            @Nullable Predicate<String> namesFilter,
                                            @NotNull Processor<PerlGlobVariableElement> processor,
                                            boolean processAll,
                                            @Nullable String namespaceName) {
    return namespaceName == null
           ? !processDefinedGlobsByNames(project, scope, namesFilter, processor, processAll)
           : processDefinedGlobsByNamespace(project, scope, namesFilter, processor, processAll, namespaceName);
  }

  private static boolean processDefinedGlobsByNamespace(@NotNull Project project,
                                                        @NotNull GlobalSearchScope scope,
                                                        @Nullable Predicate<String> namesFilter,
                                                        @NotNull Processor<PerlGlobVariableElement> processor,
                                                        boolean processAll,
                                                        @NotNull String namespaceName) {
    Set<String> processedNames = processAll ? null : new HashSet<>();
    StubIndex stubIndex = StubIndex.getInstance();
    return stubIndex.processElements(KEY_GLOB_NAMESPACE, namespaceName, project, scope, PsiPerlGlobVariable.class, it -> {
      ProgressManager.checkCanceled();
      String canonicalName = it.getCanonicalName();
      if ((namesFilter == null || namesFilter.test(canonicalName)) && (processAll || processedNames.add(canonicalName))) {
        return processor.process(it);
      }
      return true;
    });
  }

  private static boolean processDefinedGlobsByNames(@NotNull Project project,
                                                    @NotNull GlobalSearchScope scope,
                                                    @Nullable Predicate<String> namesFilter,
                                                    @NotNull Processor<PerlGlobVariableElement> processor,
                                                    boolean processAll) {
    var namesToProcess = new HashSet<String>();
    for (String globName : PerlStubUtil.getAllKeys(PerlGlobStubIndex.KEY_GLOB, scope)) {
      if (StringUtil.isEmpty(globName) || namesFilter != null && !namesFilter.test(globName)) {
        continue;
      }
      namesToProcess.add(globName);
    }

    Set<String> processedNames = processAll ? null : new HashSet<>();
    StubIndex stubIndex = StubIndex.getInstance();
    for (String key : namesToProcess) {
      if (!stubIndex.processElements(PerlGlobStubIndex.KEY_GLOB, key, project, scope, PsiPerlGlobVariable.class, it -> {
        ProgressManager.checkCanceled();
        String canonicalName = it.getCanonicalName();
        if (processAll || processedNames.add(canonicalName)) {
          return processor.process(it);
        }
        return true;
      })) {
        return true;
      }
    }
    return false;
  }
}

