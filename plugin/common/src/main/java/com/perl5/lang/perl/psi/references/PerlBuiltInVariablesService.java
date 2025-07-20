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

package com.perl5.lang.perl.psi.references;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiManager;
import com.intellij.util.Processor;
import com.perl5.lang.perl.psi.PerlVariableDeclarationElement;
import com.perl5.lang.perl.psi.impl.PerlBuiltInVariable;
import com.perl5.lang.perl.psi.utils.PerlVariableType;
import com.perl5.lang.perl.util.PerlArrayUtilCore;
import com.perl5.lang.perl.util.PerlBuiltInScalars;
import com.perl5.lang.perl.util.PerlGlobUtilCore;
import com.perl5.lang.perl.util.PerlHashUtilCore;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PerlBuiltInVariablesService implements Disposable {
  private final PsiManager myPsiManager;
  private final Map<String, PerlBuiltInVariable> myScalars = new HashMap<>();
  private final Map<String, PerlBuiltInVariable> myArrays = new HashMap<>();
  private final Map<String, PerlBuiltInVariable> myHashes = new HashMap<>();
  private final Map<String, PerlBuiltInVariable> myGlobs = new HashMap<>();

  public PerlBuiltInVariablesService(Project project) {
    myPsiManager = PsiManager.getInstance(project);

    for (int i = 1; i <= 20; i++) {
      String variableName = Integer.toString(i);
      myScalars.put(variableName, new PerlBuiltInVariable(myPsiManager, "$" + variableName));
    }
    PerlBuiltInScalars.BUILT_IN.forEach(
      name -> myScalars.put(name, new PerlBuiltInVariable(myPsiManager, PerlVariableType.SCALAR.withSigil(name))));
    PerlArrayUtilCore.BUILT_IN.forEach(
      name -> myArrays.put(name, new PerlBuiltInVariable(myPsiManager, PerlVariableType.ARRAY.withSigil(name))));
    PerlHashUtilCore.BUILT_IN.forEach(
      name -> myHashes.put(name, new PerlBuiltInVariable(myPsiManager, PerlVariableType.HASH.withSigil(name))));
    PerlGlobUtilCore.BUILT_IN.forEach(
      name -> myGlobs.put(name, new PerlBuiltInVariable(myPsiManager, PerlVariableType.GLOB.withSigil(name))));
  }

  public @Nullable PerlBuiltInVariable getScalar(@Nullable String name) {
    return myScalars.get(name);
  }

  public @Nullable PerlBuiltInVariable getArray(@Nullable String name) {
    return myArrays.get(name);
  }

  public @Nullable PerlBuiltInVariable getHash(@Nullable String name) {
    return myHashes.get(name);
  }

  public @Nullable PerlBuiltInVariable getGlob(@Nullable String name) {
    return myHashes.get(name);
  }

  public boolean processScalars(@NotNull Processor<? super PerlVariableDeclarationElement> processor) {
    return processVariables(myScalars, processor);
  }

  public boolean processArrays(@NotNull Processor<? super PerlVariableDeclarationElement> processor) {
    return processVariables(myArrays, processor);
  }

  public boolean processHashes(@NotNull Processor<? super PerlVariableDeclarationElement> processor) {
    return processVariables(myHashes, processor);
  }

  @SuppressWarnings("UnusedReturnValue")
  public boolean processVariables(@NotNull Processor<? super PerlVariableDeclarationElement> processor) {
    return processScalars(processor) && processArrays(processor) && processHashes(processor) && processGlobs(processor);
  }

  public boolean processGlobs(@NotNull Processor<? super PerlVariableDeclarationElement> processor) {
    return processVariables(myGlobs, processor);
  }

  public @Nullable PerlVariableDeclarationElement getVariableDeclaration(@Nullable PerlVariableType type, @Nullable String variableName) {
    if (StringUtil.isEmpty(variableName)) {
      return null;
    }

    if (type == PerlVariableType.SCALAR) {
      return getScalar(variableName);
    }
    else if (type == PerlVariableType.ARRAY) {
      return getArray(variableName);
    }
    else if (type == PerlVariableType.HASH) {
      return getHash(variableName);
    }

    return null;
  }

  private static boolean processVariables(@NotNull Map<String, ? extends PerlBuiltInVariable> variableMap,
                                          @NotNull Processor<? super PerlVariableDeclarationElement> processor) {
    for (PerlBuiltInVariable variable : variableMap.values()) {
      ProgressManager.checkCanceled();
      if (!processor.process(variable)) {
        return false;
      }
    }
    return true;
  }

  public static @NotNull PerlBuiltInVariablesService getInstance(@NotNull Project project) {
    return project.getService(PerlBuiltInVariablesService.class);
  }

  /**
   * @return {@code @_}
   */
  public static @NotNull PerlBuiltInVariable getImplicitArray(@NotNull Project project) {
    return Objects.requireNonNull(getInstance(project).getArray("_"));
  }

  @Override
  public void dispose() {
  }
}
