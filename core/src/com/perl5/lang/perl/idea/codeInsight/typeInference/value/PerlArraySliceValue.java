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

import com.intellij.psi.stubs.StubInputStream;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValues.UNKNOWN_VALUE;
import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValuesManager.ARRAY_SLICE_ID;

public class PerlArraySliceValue extends PerlParametrizedOperationValue {
  PerlArraySliceValue(@NotNull PerlValue arrayValue,
                      @NotNull PerlValue indexesValue) {
    super(arrayValue, indexesValue);
  }

  PerlArraySliceValue(@NotNull StubInputStream dataStream) throws IOException {
    super(dataStream);
  }

  @NotNull
  @Override
  protected PerlValue computeResolve(@NotNull PerlValue resolvedArrayValue,
                                     @NotNull PerlValue resolvedIndexesValue,
                                     @NotNull PerlValueResolver resolver) {
    if (!(resolvedArrayValue instanceof PerlArrayValue)) {
      return UNKNOWN_VALUE;
    }
    PerlArrayValue.Builder builder = PerlArrayValue.builder();
    if (resolvedIndexesValue instanceof PerlArrayValue) {
      ((PerlArrayValue)resolvedIndexesValue).forEach(key -> builder.addElement(((PerlArrayValue)resolvedArrayValue).get(key)));
    }
    else if (resolvedIndexesValue instanceof PerlScalarValue) {
      builder.addElement(((PerlArrayValue)resolvedArrayValue).get(resolvedIndexesValue));
    }
    else {
      return UNKNOWN_VALUE;
    }
    return builder.build();
  }

  @Override
  protected int getSerializationId() {
    return ARRAY_SLICE_ID;
  }

  @Override
  public String toString() {
    return "ArraySlice: " + getBaseValue() + "[" + getParameter() + "]";
  }
}
