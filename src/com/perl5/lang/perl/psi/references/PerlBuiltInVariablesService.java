/*
 * Copyright 2015-2017 Alexandr Evstigneev
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

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiManager;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.perl5.lang.perl.psi.PerlVariableDeclarationElement;
import com.perl5.lang.perl.psi.impl.PerlBuiltInVariable;
import com.perl5.lang.perl.psi.utils.PerlVariableType;
import com.perl5.lang.perl.util.PerlArrayUtil;
import com.perl5.lang.perl.util.PerlBuiltInScalars;
import com.perl5.lang.perl.util.PerlGlobUtil;
import com.perl5.lang.perl.util.PerlHashUtil;
import gnu.trove.THashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class PerlBuiltInVariablesService {
  private final PsiManager myPsiManager;
  private final Map<String, PerlBuiltInVariable> myScalars = new THashMap<>();
  private final Map<String, PerlBuiltInVariable> myArrays = new THashMap<>();
  private final Map<String, PerlBuiltInVariable> myHashes = new THashMap<>();
  private final Map<String, PerlBuiltInVariable> myGlobs = new THashMap<>();

  public PerlBuiltInVariablesService(Project project) {
    myPsiManager = PsiManager.getInstance(project);

    PerlBuiltInScalars.BUILT_IN.forEach(name -> myScalars.put(name, new PerlBuiltInVariable(myPsiManager, "$" + name)));
    PerlArrayUtil.BUILT_IN.forEach(name -> myArrays.put(name, new PerlBuiltInVariable(myPsiManager, "@" + name)));
    PerlHashUtil.BUILT_IN.forEach(name -> myHashes.put(name, new PerlBuiltInVariable(myPsiManager, "%" + name)));
    PerlGlobUtil.BUILT_IN.forEach(name -> myGlobs.put(name, new PerlBuiltInVariable(myPsiManager, "*" + name)));
  }

  @Nullable
  public PerlBuiltInVariable getScalar(@Nullable String name) {
    return myScalars.get(name);
  }

  @Nullable
  public PerlBuiltInVariable getArray(@Nullable String name) {
    return myArrays.get(name);
  }

  @Nullable
  public PerlBuiltInVariable getHash(@Nullable String name) {
    return myHashes.get(name);
  }

  @Nullable
  public PerlBuiltInVariable getGlob(@Nullable String name) {
    return myHashes.get(name);
  }

  public boolean processScalars(@NotNull PsiScopeProcessor processor) {
    return processVariables(myScalars, processor);
  }

  public boolean processArrays(@NotNull PsiScopeProcessor processor) {
    return processVariables(myArrays, processor);
  }

  public boolean processHashes(@NotNull PsiScopeProcessor processor) {
    return processVariables(myHashes, processor);
  }

  public boolean processVariables(@NotNull PsiScopeProcessor processor) {
    return processScalars(processor) && processArrays(processor) && processHashes(processor);
  }

  public boolean processGlobs(@NotNull PsiScopeProcessor processor) {
    return processVariables(myGlobs, processor);
  }

  @Nullable
  public PerlVariableDeclarationElement getVariableDeclaration(@Nullable PerlVariableType type, @Nullable String variableName) {
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

  private static boolean processVariables(@NotNull Map<String, PerlBuiltInVariable> variableMap, @NotNull PsiScopeProcessor processor) {
    for (PerlBuiltInVariable variable : variableMap.values()) {
      if (!processor.execute(variable, ResolveState.initial())) {
        return false;
      }
    }
    return true;
  }

  @NotNull
  public static PerlBuiltInVariablesService getInstance(@NotNull Project project) {
    return ServiceManager.getService(project, PerlBuiltInVariablesService.class);
  }
}
