/*
 * Copyright 2015-2023 Alexandr Evstigneev
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
import com.intellij.psi.stubs.StubIndexKey;
import com.intellij.util.Processor;
import com.perl5.lang.perl.psi.PerlVariable;
import com.perl5.lang.perl.psi.PerlVariableDeclarationElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


public final class PerlVariableUtil {
  private PerlVariableUtil() {
  }

  /**
   * Compares two variables by actual type, packageName and name. They can be from different declarations
   * builds AST
   */
  public static boolean equals(@Nullable PerlVariable v1, @Nullable PerlVariable v2) {
    if (Objects.equals(v1, v2)) {
      return true;
    }
    assert v1 != null && v2 != null;
    return v1.getActualType().equals(v2.getActualType()) &&
           Objects.equals(v1.getExplicitNamespaceName(), v2.getExplicitNamespaceName()) &&
           Objects.equals(v1.getName(), v2.getName());
  }

  /**
   * Processes variables indexed with {@code indexKey}
   *
   * @param onePerName if true, only one variable per name is going to the processor
   */
  public static boolean processGlobalVariables(@NotNull StubIndexKey<String, PerlVariableDeclarationElement> indexKey,
                                               @NotNull Project project,
                                               @NotNull GlobalSearchScope scope,
                                               @NotNull Processor<PerlVariableDeclarationElement> processor,
                                               @NotNull String textKey,
                                               boolean onePerName) {
    Set<String> uniqueNames = onePerName ? new HashSet<>() : null;
    return StubIndex.getInstance().processElements(indexKey, textKey, project, scope, PerlVariableDeclarationElement.class, element -> {
      ProgressManager.checkCanceled();
      if (!onePerName || uniqueNames.add(element.getCanonicalName())) {
        return processor.process(element);
      }
      return true;
    });
  }

  /**
   * Processes all variables indexed with {@code indexKey}
   *
   * @param processAll if false, only one entry per name going to be processed. May be need when filling completion
   */
  public static boolean processGlobalVariables(@NotNull StubIndexKey<String, PerlVariableDeclarationElement> indexKey,
                                               @NotNull Project project,
                                               @NotNull GlobalSearchScope scope,
                                               @NotNull Processor<PerlVariableDeclarationElement> processor,
                                               boolean processAll) {
    for (String variableName : PerlStubUtil.getAllKeys(indexKey, scope)) {
      if (!processGlobalVariables(indexKey, project, scope, processor, variableName, !processAll)) {
        return false;
      }
    }
    return true;
  }
}
