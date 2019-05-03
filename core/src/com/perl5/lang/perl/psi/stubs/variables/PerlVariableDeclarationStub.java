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

package com.perl5.lang.perl.psi.stubs.variables;

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
import org.jetbrains.annotations.Nullable;

public class PerlVariableDeclarationStub extends StubBase<PerlVariableDeclarationElement> implements PerlVariableDeclaration {
  @NotNull
  private final String myPackageName;
  @NotNull
  private final String myVariableName;
  @NotNull
  private final PerlVariableType myVariableType;
  @Nullable
  private final PerlVariableAnnotations myPerlVariableAnnotations;
  @NotNull
  private final PerlValue myDeclaredValue;

  public PerlVariableDeclarationStub(
    StubElement parent,
    @NotNull IStubElementType elementType,
    @NotNull String packageName,
    @NotNull String variableName,
    @NotNull PerlValue declaredValue,
    @NotNull PerlVariableType variableType,
    @Nullable PerlVariableAnnotations variableAnnotations
  ) {
    super(parent, elementType);
    myPackageName = packageName;
    myVariableName = variableName;
    myDeclaredValue = declaredValue;
    myVariableType = variableType;
    myPerlVariableAnnotations = variableAnnotations;
  }

  @NotNull
  @Override
  public String getExplicitNamespaceName() {
    return myPackageName;
  }

  @NotNull
  @Override
  public String getNamespaceName() {
    return getExplicitNamespaceName();
  }

  @NotNull
  @Override
  public String getVariableName() {
    return myVariableName;
  }

  @Override
  @NotNull
  public PerlValue getDeclaredValue() {
    return myDeclaredValue;
  }

  @NotNull
  @Override
  public PerlVariableType getActualType() {
    return myVariableType;
  }

  @Nullable
  @Override
  public PerlVariableAnnotations getVariableAnnotations() {
    return myPerlVariableAnnotations;
  }

  public StubIndexKey<String, PerlVariableDeclarationElement> getIndexKey() {
    if (myVariableType == PerlVariableType.ARRAY) {
      return PerlVariablesStubIndex.KEY_ARRAY;
    }
    else if (myVariableType == PerlVariableType.SCALAR) {
      return PerlVariablesStubIndex.KEY_SCALAR;
    }
    else if (myVariableType == PerlVariableType.HASH) {
      return PerlVariablesStubIndex.KEY_HASH;
    }
    throw new RuntimeException("Don't have key for " + myVariableType);
  }

  @Override
  public String toString() {
    return super.toString() + "\n" +
           "\tNamespace name: " + myPackageName + "\n" +
           "\tVariable name: " + myVariableName + "\n" +
           "\tVariable type: " + myVariableType + "\n" +
           "\tDeclared value: " + myDeclaredValue + "\n" +
           "\tAnnotations: " + myPerlVariableAnnotations
      ;
  }
}
