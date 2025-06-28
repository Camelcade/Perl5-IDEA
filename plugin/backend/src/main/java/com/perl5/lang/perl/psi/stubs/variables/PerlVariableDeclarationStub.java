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

package com.perl5.lang.perl.psi.stubs.variables;

import com.intellij.openapi.util.Pair;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubBase;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubIndexKey;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValue;
import com.perl5.lang.perl.psi.PerlVariableDeclaration;
import com.perl5.lang.perl.psi.PerlVariableDeclarationElement;
import com.perl5.lang.perl.psi.utils.PerlVariableAnnotations;
import com.perl5.lang.perl.psi.utils.PerlVariableType;
import org.jetbrains.annotations.NotNull;

public class PerlVariableDeclarationStub extends StubBase<PerlVariableDeclarationElement> implements PerlVariableDeclaration {
  private final @NotNull String myNamespaceName;
  private final @NotNull String myVariableName;
  private final @NotNull PerlVariableType myVariableType;
  private final @NotNull PerlVariableAnnotations myPerlVariableAnnotations;
  private final @NotNull PerlValue myDeclaredValue;

  public PerlVariableDeclarationStub(
    StubElement parent,
    @NotNull IStubElementType elementType,
    @NotNull String namespaceName,
    @NotNull String variableName,
    @NotNull PerlValue declaredValue,
    @NotNull PerlVariableType variableType,
    @NotNull PerlVariableAnnotations variableAnnotations
  ) {
    super(parent, elementType);
    myNamespaceName = namespaceName;
    myVariableName = variableName;
    myDeclaredValue = declaredValue;
    myVariableType = variableType;
    myPerlVariableAnnotations = variableAnnotations;
  }

  @Override
  public @NotNull String getExplicitNamespaceName() {
    return myNamespaceName;
  }

  @Override
  public @NotNull String getNamespaceName() {
    return getExplicitNamespaceName();
  }

  @Override
  public @NotNull String getVariableName() {
    return myVariableName;
  }

  @Override
  public @NotNull PerlValue getDeclaredValue() {
    return myDeclaredValue;
  }

  @Override
  public @NotNull PerlVariableType getActualType() {
    return myVariableType;
  }

  @Override
  public @NotNull PerlVariableAnnotations getVariableAnnotations() {
    return myPerlVariableAnnotations;
  }

  public Pair<StubIndexKey<String, PerlVariableDeclarationElement>, StubIndexKey<String, PerlVariableDeclarationElement>> getIndexKey() {
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

  @Override
  public String toString() {
    return super.toString() + "\n" +
           "\tNamespace name: " + myNamespaceName + "\n" +
           "\tVariable name: " + myVariableName + "\n" +
           "\tVariable type: " + myVariableType + "\n" +
           "\tDeclared value: " + myDeclaredValue + "\n" +
           "\tAnnotations: " + myPerlVariableAnnotations
      ;
  }
}
