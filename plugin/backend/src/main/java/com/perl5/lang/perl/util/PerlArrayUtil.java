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
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.Processor;
import com.intellij.util.SmartList;
import com.perl5.lang.perl.psi.PerlVariableDeclarationElement;
import com.perl5.lang.perl.psi.references.PerlImplicitDeclarationsService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.perl5.lang.perl.psi.stubs.variables.PerlArrayNamespaceStubIndex.KEY_ARRAY_IN_NAMESPACE;
import static com.perl5.lang.perl.psi.stubs.variables.PerlArrayStubIndex.KEY_ARRAY;


public final class PerlArrayUtil {
  private PerlArrayUtil() {
  }


  /**
   * Searching project files for global array definitions by specific package and variable name
   *
   * @param project       project to search in
   * @param canonicalName canonical variable name package::name
   * @return Collection of found definitions
   */
  public static Collection<PerlVariableDeclarationElement> getGlobalArrayDefinitions(@NotNull Project project,
                                                                                     @NotNull String canonicalName) {
    return getGlobalArrayDefinitions(project, canonicalName, GlobalSearchScope.allScope(project));
  }

  public static @NotNull Collection<PerlVariableDeclarationElement> getGlobalArrayDefinitions(@NotNull Project project,
                                                                                              @Nullable String canonicalName,
                                                                                              @NotNull GlobalSearchScope scope) {
    if (canonicalName == null) {
      return Collections.emptyList();
    }
    List<PerlVariableDeclarationElement> result = new SmartList<>();
    processGlobalArraysByName(project, scope, canonicalName, true, it -> {
      if (canonicalName.equals(it.getCanonicalName())) {
        result.add(it);
      }
      return true;
    });
    return result;
  }

  /**
   * Returns list of defined global arrays
   *
   * @param project project to search in
   * @return collection of variable canonical names
   */
  @SuppressWarnings("StaticMethodOnlyUsedInOneClass")
  public static Collection<String> getDefinedGlobalArrayNames(@NotNull Project project) {
    return PerlStubUtil.getAllKeys(KEY_ARRAY, GlobalSearchScope.allScope(project));
  }

  @SuppressWarnings("UnusedReturnValue")
  public static boolean processGlobalArraysByName(@NotNull Project project,
                                                  @NotNull GlobalSearchScope scope,
                                                  @NotNull String canonicalName,
                                                  boolean processAll,
                                                  @NotNull Processor<? super PerlVariableDeclarationElement> processor) {
    return PerlImplicitDeclarationsService.getInstance(project).processArrays(canonicalName, processor) &&
           PerlVariableUtil.processGlobalVariables(KEY_ARRAY, project, scope, processor, canonicalName, processAll);
  }

  @SuppressWarnings("StaticMethodOnlyUsedInOneClass")
  public static boolean processGlobalArrays(@NotNull Project project,
                                            @NotNull GlobalSearchScope scope,
                                            @Nullable String namespaceName,
                                            boolean processAll,
                                            @NotNull Processor<? super PerlVariableDeclarationElement> processor) {
    if( namespaceName == null){
      return PerlImplicitDeclarationsService.getInstance(project).processArrays( processor) &&
             PerlVariableUtil.processGlobalVariables(KEY_ARRAY, project, scope, processor, processAll);
    }
    return PerlImplicitDeclarationsService.getInstance(project).processArraysInNamespace(namespaceName, processor) &&
           PerlVariableUtil.processGlobalVariables(KEY_ARRAY_IN_NAMESPACE, project, scope, processor, namespaceName, !processAll);
  }
}
