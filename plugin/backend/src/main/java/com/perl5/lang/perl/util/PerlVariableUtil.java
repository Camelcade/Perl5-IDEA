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
import com.intellij.openapi.util.Pair;
import com.intellij.psi.PsiElement;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.stubs.StubIndex;
import com.intellij.psi.stubs.StubIndexKey;
import com.intellij.util.ObjectUtils;
import com.intellij.util.Processor;
import com.perl5.lang.perl.psi.PerlGlobVariableElement;
import com.perl5.lang.perl.psi.PerlVariable;
import com.perl5.lang.perl.psi.PerlVariableDeclaration;
import com.perl5.lang.perl.psi.PerlVariableDeclarationElement;
import com.perl5.lang.perl.psi.impl.PerlImplicitVariableDeclaration;
import com.perl5.lang.perl.psi.stubs.variables.PerlArrayStubIndex;
import com.perl5.lang.perl.psi.stubs.variables.PerlHashStubIndex;
import com.perl5.lang.perl.psi.stubs.variables.PerlScalarStubIndex;
import com.perl5.lang.perl.psi.utils.PerlVariableType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;


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
  public static boolean processGlobalVariables(@NotNull StubIndexKey<? super String, PerlVariableDeclarationElement> indexKey,
                                               @NotNull Project project,
                                               @NotNull GlobalSearchScope scope,
                                               @NotNull Processor<? super PerlVariableDeclarationElement> processor,
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
                                               @NotNull Processor<? super PerlVariableDeclarationElement> processor,
                                               boolean processAll) {
    for (String variableName : PerlStubUtil.getAllKeys(indexKey, scope)) {
      if (!processGlobalVariables(indexKey, project, scope, processor, variableName, !processAll)) {
        return false;
      }
    }
    return true;
  }

  /**
   * Looking for global variable declarations sutable for current variable
   *
   * @return list of global declarations
   */
  public static @NotNull List<PerlVariableDeclarationElement> getGlobalDeclarations(@NotNull PerlVariable perlVariable) {
    if (perlVariable instanceof PerlImplicitVariableDeclaration) {
      return Collections.emptyList();
    }
    String variableName = perlVariable.getName();
    if (variableName == null) {
      return Collections.emptyList();
    }

    List<PerlVariableDeclarationElement> result = new ArrayList<>();
    PerlVariableType myType = perlVariable.getActualType();

    PsiElement parent = perlVariable.getParent(); // wrapper if any
    String namespaceName =
      ObjectUtils.notNull(perlVariable.getExplicitNamespaceName(), PerlPackageUtilCore.getContextNamespaceName(perlVariable));
    String fullQualifiedName = PerlPackageUtilCore.join(namespaceName, variableName);

    Processor<PerlVariableDeclarationElement> variableProcessor = it -> {
      if (!it.equals(parent)) {
        result.add(it);
      }
      return true;
    };
    if (myType == PerlVariableType.SCALAR) {
      PerlScalarUtil.processGlobalScalarsByName(perlVariable.getProject(), perlVariable.getResolveScope(), fullQualifiedName, true,
                                                variableProcessor);
    }
    else if (myType == PerlVariableType.ARRAY) {
      PerlArrayUtil.processGlobalArraysByName(perlVariable.getProject(), perlVariable.getResolveScope(), fullQualifiedName, true,
                                              variableProcessor);
    }
    else if (myType == PerlVariableType.HASH) {
      PerlHashUtil.processGlobalHashesByName(perlVariable.getProject(), perlVariable.getResolveScope(), fullQualifiedName, true,
                                             variableProcessor);
    }

    return result;
  }

  public static Pair<StubIndexKey<String, PerlVariableDeclarationElement>, StubIndexKey<String, PerlVariableDeclarationElement>> getIndexKey(
    @NotNull PerlVariableDeclaration perlVariableDeclaration) {
    var myVariableType = perlVariableDeclaration.getActualType();
    if (myVariableType == PerlVariableType.ARRAY) {
      return PerlArrayStubIndex.ARRAY_KEYS;
    }
    else if (myVariableType == PerlVariableType.SCALAR) {
      return PerlScalarStubIndex.SCALAR_KEYS;
    }
    else if (myVariableType == PerlVariableType.HASH) {
      return PerlHashStubIndex.HASH_KEYS;
    }
    throw new RuntimeException("Don't have key for " + myVariableType);
  }

  public static @NotNull List<PerlGlobVariableElement> getRelatedGlobs(PerlVariable perlVariable) {
    if (perlVariable instanceof PerlImplicitVariableDeclaration) {
      return Collections.emptyList();
    }
    String variableName = perlVariable.getName();
    if (variableName == null) {
      return Collections.emptyList();
    }
    String namespaceName =
      ObjectUtils.notNull(perlVariable.getExplicitNamespaceName(), PerlPackageUtilCore.getContextNamespaceName(perlVariable));
    String fullQualifiedName = PerlPackageUtilCore.join(namespaceName, variableName);

    List<PerlGlobVariableElement> result = new ArrayList<>();
    PerlGlobUtil.processGlobsByName(perlVariable.getProject(), perlVariable.getResolveScope(), fullQualifiedName, result::add, true);
    return result;
  }
}
