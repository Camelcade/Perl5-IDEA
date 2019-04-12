/*
 * Copyright 2015-2018 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.codeInsight.typeInference.value;

import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

abstract class PerlParametrizedOperationValue extends PerlOperationValue {
  @NotNull
  private final PerlValue myParameter;

  public PerlParametrizedOperationValue(@NotNull PerlValue baseValue,
                                        @NotNull PerlValue parameter) {
    super(baseValue);
    myParameter = parameter;
  }

  public PerlParametrizedOperationValue(@NotNull StubInputStream dataStream) throws IOException {
    super(dataStream);
    myParameter = PerlValuesManager.readValue(dataStream);
  }

  @Override
  protected void serializeData(@NotNull StubOutputStream dataStream) throws IOException {
    super.serializeData(dataStream);
    myParameter.serialize(dataStream);
  }

  @NotNull
  @Override
  final protected PerlValue computeResolve(@NotNull PsiElement contextElement,
                                           @NotNull PerlValue resolvedBaseValue) {
    return computeResolve(contextElement, resolvedBaseValue, myParameter.resolve(contextElement));
  }

  @NotNull
  protected abstract PerlValue computeResolve(@NotNull PsiElement contextElement,
                                              @NotNull PerlValue resolvedBaseValue,
                                              @NotNull PerlValue resolvedParameter);

  @NotNull
  protected final PerlValue getParameter() {
    return myParameter;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }

    PerlParametrizedOperationValue value = (PerlParametrizedOperationValue)o;

    return myParameter.equals(value.myParameter);
  }

  @Override
  protected int computeHashCode() {
    int result = super.computeHashCode();
    result = 31 * result + myParameter.hashCode();
    return result;
  }
}
