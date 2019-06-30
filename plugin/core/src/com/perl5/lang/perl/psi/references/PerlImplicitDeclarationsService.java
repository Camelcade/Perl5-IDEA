/*
 * Copyright 2015-2019 Alexandr Evstigneev
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
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiManager;
import com.intellij.util.Processor;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValue;
import com.perl5.lang.perl.psi.PerlSubDefinitionElement;
import com.perl5.lang.perl.psi.PerlVariableDeclarationElement;
import com.perl5.lang.perl.psi.impl.PerlImplicitSubDefinition;
import com.perl5.lang.perl.psi.impl.PerlImplicitVariableDeclaration;
import com.perl5.lang.perl.psi.properties.PerlPackageMember;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.perl5.lang.perl.util.PerlPackageUtil.CORE_NAMESPACE;
import static com.perl5.lang.perl.util.PerlPackageUtil.NAMESPACE_SEPARATOR;

/**
 * Service for a common tricky-defined entities:<ul>
 * <li>Built-in subs</li>
 * <li>Subs from extensions</li>
 * <li>Global scalars, arrays and hashes defined by modules code</li>
 * </ul>
 */
public class PerlImplicitDeclarationsService {
  private static final Logger LOG = Logger.getInstance(PerlImplicitDeclarationsService.class);
  private final Map<String, PerlImplicitSubDefinition> mySubsMap = new HashMap<>();
  private final Map<String, PerlImplicitVariableDeclaration> myScalarsMap = new HashMap<>();
  private final Map<String, PerlImplicitVariableDeclaration> myArraysMap = new HashMap<>();
  private final Map<String, PerlImplicitVariableDeclaration> myHashesMap = new HashMap<>();
  @NotNull
  private final PsiManager myPsiManager;

  public PerlImplicitDeclarationsService(@NotNull Project project) {
    myPsiManager = PsiManager.getInstance(project);
    PerlImplicitDeclarationsProvider.EP_NAME.extensions().forEach(it -> it.registerDeclarations(this));
  }

  @NotNull
  public PsiManager getPsiManager() {
    return myPsiManager;
  }

  public void registerVariable(@NotNull PerlImplicitVariableDeclaration implicitVariable) {
    switch (implicitVariable.getVariableType()) {
      case SCALAR:
        doRegister(myScalarsMap, implicitVariable);
        break;
      case ARRAY:
        doRegister(myArraysMap, implicitVariable);
        break;
      case HASH:
        doRegister(myHashesMap, implicitVariable);
        break;
      default:
        LOG.warn("Can handle only SCALAR, ARRAY or HASH at the moment, got: " + implicitVariable);
    }
  }

  public void registerSub(@NotNull PerlImplicitSubDefinition subDefinition) {
    doRegister(mySubsMap, subDefinition);
  }

  @NotNull
  public PerlImplicitSubDefinition registerAnonSub(@NotNull String namespaceName,
                                                   @NotNull String baseName,
                                                   @Nullable PerlValue returnValue) {
    PerlImplicitSubDefinition subDefinition = new PerlImplicitSubDefinition(
      myPsiManager, baseName, namespaceName, Collections.emptyList(), returnValue, true);
    doRegister(mySubsMap, subDefinition);
    return subDefinition;
  }

  private static <T extends PerlPackageMember> void doRegister(@NotNull Map<String, T> targetMap, @NotNull T entity) {
    String canonicalName = entity.getCanonicalName();
    LOG.assertTrue(!targetMap.containsKey(canonicalName), "Multiple registrations for: " + entity);
    targetMap.put(canonicalName, entity);
  }

  @Nullable
  public PerlSubDefinitionElement getCoreSub(@Nullable String subName) {
    return getSub(CORE_NAMESPACE, subName);
  }

  @Nullable
  public PerlSubDefinitionElement getSub(@Nullable String packageName, @Nullable String subName) {
    return getSub(packageName + NAMESPACE_SEPARATOR + subName);
  }

  @Contract("null->null")
  @Nullable
  public PerlSubDefinitionElement getSub(@Nullable String canonicalName) {
    return mySubsMap.get(canonicalName);
  }

  public boolean processSubsInPackage(@NotNull String packageName, @NotNull Processor<? super PerlSubDefinitionElement> processor) {
    return processSubs(it -> !packageName.equals(it.getNamespaceName()) || processor.process(it));
  }

  public boolean processSubs(@NotNull String canonicalName, @NotNull Processor<? super PerlSubDefinitionElement> processor) {
    PerlSubDefinitionElement subDefinitionElement = getSub(canonicalName);
    return subDefinitionElement == null || processor.process(subDefinitionElement);
  }

  public boolean processSubs(@NotNull Processor<? super PerlSubDefinitionElement> processor) {
    for (PerlImplicitSubDefinition subDefinition : mySubsMap.values()) {
      ProgressManager.checkCanceled();
      if (!processor.process(subDefinition)) {
        return false;
      }
    }
    return true;
  }

  @Contract("null->null")
  @Nullable
  public PerlVariableDeclarationElement getScalar(@Nullable String canonicalName) {
    return myScalarsMap.get(canonicalName);
  }

  @Contract("null->null")
  @Nullable
  public PerlVariableDeclarationElement getArray(@Nullable String canonicalName) {
    return myArraysMap.get(canonicalName);
  }

  @Contract("null->null")
  @Nullable
  public PerlVariableDeclarationElement getHash(@Nullable String canonicalName) {
    return myHashesMap.get(canonicalName);
  }

  public boolean processScalars(@NotNull String canonicalName, @NotNull Processor<? super PerlVariableDeclarationElement> processor) {
    PerlVariableDeclarationElement scalar = getScalar(canonicalName);
    return scalar == null || processor.process(scalar);
  }

  public boolean processArrays(@NotNull String canonicalName, @NotNull Processor<? super PerlVariableDeclarationElement> processor) {
    PerlVariableDeclarationElement array = getArray(canonicalName);
    return array == null || processor.process(array);
  }

  public boolean processHashes(@NotNull String canonicalName, @NotNull Processor<? super PerlVariableDeclarationElement> processor) {
    PerlVariableDeclarationElement hash = getHash(canonicalName);
    return hash == null || processor.process(hash);
  }

  public boolean processScalarsInPackage(@Nullable String packageName,
                                         @NotNull Processor<? super PerlVariableDeclarationElement> processor) {
    return packageName != null && processScalars(it -> !packageName.equals(it.getNamespaceName()) || processor.process(it));
  }

  public boolean processArraysInPackage(@Nullable String packageName,
                                        @NotNull Processor<? super PerlVariableDeclarationElement> processor) {
    return packageName != null && processArrays(it -> !packageName.equals(it.getNamespaceName()) || processor.process(it));
  }

  public boolean processHashesInPackage(@Nullable String packageName,
                                        @NotNull Processor<? super PerlVariableDeclarationElement> processor) {
    return packageName != null && processHashes(it -> !packageName.equals(it.getNamespaceName()) || processor.process(it));
  }

  public boolean processScalars(@NotNull Processor<? super PerlVariableDeclarationElement> processor) {
    return processVariables(myScalarsMap, processor);
  }

  public boolean processArrays(@NotNull Processor<? super PerlVariableDeclarationElement> processor) {
    return processVariables(myArraysMap, processor);
  }

  public boolean processHashes(@NotNull Processor<? super PerlVariableDeclarationElement> processor) {
    return processVariables(myHashesMap, processor);
  }

  private static boolean processVariables(@NotNull Map<String, PerlImplicitVariableDeclaration> variablesMap,
                                          @NotNull Processor<? super PerlVariableDeclarationElement> processor) {
    for (PerlImplicitVariableDeclaration variableDeclaration : variablesMap.values()) {
      ProgressManager.checkCanceled();
      if (!processor.process(variableDeclaration)) {
        return false;
      }
    }
    return true;
  }

  @NotNull
  public static PerlImplicitDeclarationsService getInstance(@NotNull Project project) {
    return ServiceManager.getService(project, PerlImplicitDeclarationsService.class);
  }
}
