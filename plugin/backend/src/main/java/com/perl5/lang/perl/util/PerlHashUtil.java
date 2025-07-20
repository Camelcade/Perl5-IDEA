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
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.PerlVariableDeclarationElement;
import com.perl5.lang.perl.psi.references.PerlImplicitDeclarationsService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

import static com.perl5.lang.perl.psi.stubs.variables.PerlHashNamespaceStubIndex.KEY_HASH_IN_NAMESPACE;
import static com.perl5.lang.perl.psi.stubs.variables.PerlHashStubIndex.KEY_HASH;


public final class PerlHashUtil implements PerlElementTypes {

  private PerlHashUtil() {
  }

  /**
   * Searching project files for global hash definitions by specific package and variable name
   *
   * @param project       project to search in
   * @param canonicalName canonical variable name package::name
   * @return Collection of found definitions
   */
  public static Collection<PerlVariableDeclarationElement> getGlobalHashDefinitions(Project project, String canonicalName) {
    return getGlobalHashDefinitions(project, canonicalName, GlobalSearchScope.allScope(project));
  }

  public static Collection<PerlVariableDeclarationElement> getGlobalHashDefinitions(@NotNull Project project,
                                                                                    @NotNull String canonicalName,
                                                                                    @NotNull GlobalSearchScope scope) {
    List<PerlVariableDeclarationElement> result = new SmartList<>();
    processGlobalHashesByName(project, scope, canonicalName, true, it -> {
      if (canonicalName.equals(it.getCanonicalName())) {
        result.add(it);
      }
      return true;
    });
    return result;
  }


  /**
   * Returns list of defined global hashes
   *
   * @param project project to search in
   * @return collection of variable canonical names
   */
  @SuppressWarnings("StaticMethodOnlyUsedInOneClass")
  public static Collection<String> getDefinedGlobalHashNames(@NotNull Project project) {
    return PerlStubUtil.getAllKeys(KEY_HASH, GlobalSearchScope.allScope(project));
  }

  @SuppressWarnings("UnusedReturnValue")
  public static boolean processGlobalHashesByName(@NotNull Project project,
                                                  @NotNull GlobalSearchScope scope,
                                                  @NotNull String canonicalName,
                                                  boolean processAll,
                                                  @NotNull Processor<? super PerlVariableDeclarationElement> processor) {
    return PerlImplicitDeclarationsService.getInstance(project).processHashes(canonicalName, processor) &&
           PerlVariableUtil.processGlobalVariables(KEY_HASH, project, scope, processor, canonicalName, processAll);
  }

  @SuppressWarnings("StaticMethodOnlyUsedInOneClass")
  public static boolean processGlobalHashes(@NotNull Project project,
                                            @NotNull GlobalSearchScope scope,
                                            @Nullable String namespaceName,
                                            boolean processAll,
                                            @NotNull Processor<? super PerlVariableDeclarationElement> processor) {
    if( namespaceName == null){
      return PerlImplicitDeclarationsService.getInstance(project).processHashes(processor) &&
             PerlVariableUtil.processGlobalVariables(KEY_HASH, project, scope, processor, processAll);
    }
    return PerlImplicitDeclarationsService.getInstance(project).processHashesInNamespace(namespaceName, processor) &&
           PerlVariableUtil.processGlobalVariables(KEY_HASH_IN_NAMESPACE, project, scope, processor, namespaceName, !processAll);
  }
}
