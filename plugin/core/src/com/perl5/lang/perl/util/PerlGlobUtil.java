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

package com.perl5.lang.perl.util;

import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.stubs.StubIndex;
import com.intellij.util.Processor;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.PerlGlobVariable;
import com.perl5.lang.perl.psi.PsiPerlGlobVariable;
import com.perl5.lang.perl.psi.stubs.globs.PerlGlobsStubIndex;
import gnu.trove.THashSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;


public class PerlGlobUtil implements PerlElementTypes {
  public static final HashSet<String> BUILT_IN = new HashSet<>(Arrays.asList(
    "ARGV",
    "STDERR",
    "STDOUT",
    "ARGVOUT",
    "STDIN"
  ));

  static {
    BUILT_IN.addAll(PerlBuiltInScalars.BUILT_IN);
    //BUILT_IN.addAll(PerlBuiltInSubs.BUILT_IN);
    BUILT_IN.addAll(PerlArrayUtil.BUILT_IN);
    BUILT_IN.addAll(PerlHashUtil.BUILT_IN);
  }

  /**
   * Searching project files for sub definitions by specific package and function name
   *
   * @param project       project to search in
   * @param canonicalName canonical function name package::name
   * @return collection of found definitions
   */
  public static Collection<PsiPerlGlobVariable> getGlobsDefinitions(Project project, String canonicalName) {
    return getGlobsDefinitions(project, canonicalName, GlobalSearchScope.allScope(project));
  }

  public static Collection<PsiPerlGlobVariable> getGlobsDefinitions(Project project, String canonicalName, GlobalSearchScope scope) {
    if (canonicalName == null) {
      return Collections.emptyList();
    }
    return StubIndex.getElements(PerlGlobsStubIndex.KEY, canonicalName, project, scope, PsiPerlGlobVariable.class);
  }

  /**
   * Returns list of known stubbed globs
   *
   * @param project project to search in
   * @return collection of globs names
   */
  public static Collection<String> getDefinedGlobsNames(Project project) {
    return PerlStubUtil.getIndexKeysWithoutInternals(PerlGlobsStubIndex.KEY, project);
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
                                            @NotNull Processor<PerlGlobVariable> processor,
                                            boolean processAll,
                                            @Nullable String namespaceName) {
    List<String> namesToProcess;
    if (namespaceName == null) {
      namesToProcess = new ArrayList<>();
      for (String globName : PerlStubUtil.getAllKeys(PerlGlobsStubIndex.KEY, scope)) {
        if (StringUtil.isEmpty(globName) || namesFilter != null && !namesFilter.test(globName)) {
          continue;
        }
        namesToProcess.add(globName);
      }
    }
    else {
      namesToProcess = Collections.singletonList("*" + namespaceName);
    }

    Set<String> processedNames = processAll ? null : new THashSet<>();
    StubIndex stubIndex = StubIndex.getInstance();
    for (String key : namesToProcess) {
      if (!stubIndex.processElements(PerlGlobsStubIndex.KEY, key, project, scope, PsiPerlGlobVariable.class, it -> {
        ProgressManager.checkCanceled();
        String canonicalName = it.getCanonicalName();
        if ((namesFilter == null || namesFilter.test(canonicalName)) &&
            (processAll || processedNames.add(canonicalName))) {
          return processor.process(it);
        }
        return true;
      })) {
        return false;
      }
    }
    return true;
  }
}

