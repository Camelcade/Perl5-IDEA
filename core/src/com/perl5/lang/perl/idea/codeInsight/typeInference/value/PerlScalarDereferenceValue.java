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

package com.perl5.lang.perl.idea.codeInsight.typeInference.value;

import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.StubInputStream;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Map;

import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValues.UNKNOWN_VALUE;
import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValuesManager.SCALAR_DEREFERENCE_ID;

public class PerlScalarDereferenceValue extends PerlOperationValue {
  public PerlScalarDereferenceValue(@NotNull PerlValue referenceValue) {
    super(referenceValue);
  }

  public PerlScalarDereferenceValue(@NotNull StubInputStream dataStream) throws IOException {
    super(dataStream);
  }

  @NotNull
  @Override
  protected PerlValue computeResolve(@NotNull PsiElement contextElement,
                                     @NotNull PerlValue resolvedBaseValue,
                                     @NotNull Map<PerlValue, PerlValue> substitutions) {
    if (resolvedBaseValue instanceof PerlReferenceValue) {
      PerlValue target = ((PerlReferenceValue)resolvedBaseValue).getTarget();
      if (target instanceof PerlScalarValue || target.isUndef() || target instanceof PerlReferenceValue) {
        return target;
      }
    }
    return UNKNOWN_VALUE;
  }

  @Override
  protected int getSerializationId() {
    return SCALAR_DEREFERENCE_ID;
  }

  @Override
  public String toString() {
    return "ScalarDeref: " + getBaseValue();
  }
}
