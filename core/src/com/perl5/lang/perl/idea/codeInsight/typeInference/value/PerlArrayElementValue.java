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
import java.util.List;
import java.util.Map;

import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValues.UNDEF_VALUE;
import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValues.UNKNOWN_VALUE;
import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValuesManager.ARRAY_ELEMENT_ID;

public final class PerlArrayElementValue extends PerlParametrizedOperationValue {
  PerlArrayElementValue(@NotNull PerlValue baseValue, @NotNull PerlValue index) {
    super(baseValue, index);
  }

  PerlArrayElementValue(@NotNull StubInputStream dataStream) throws IOException {
    super(dataStream);
  }

  @NotNull
  @Override
  protected PerlValue computeResolve(@NotNull PsiElement contextElement,
                                     @NotNull PerlValue resolvedArrayValue,
                                     @NotNull PerlValue resolvedIndexValue,
                                     @NotNull Map<PerlValue, PerlValue> substitutions) {
    if (!(resolvedArrayValue instanceof PerlListValue)
        || !(resolvedIndexValue instanceof PerlScalarValue)
        || !resolvedArrayValue.isDeterministic()) {
      return UNKNOWN_VALUE;
    }
    int indexValue;
    try {
      indexValue = Integer.valueOf(((PerlScalarValue)resolvedIndexValue).getValue());
    }
    catch (NumberFormatException ignore) {
      return UNKNOWN_VALUE;
    }
    List<PerlValue> arrayElements = ((PerlListValue)resolvedArrayValue).getElements();
    if (Math.abs(indexValue) >= arrayElements.size()) {
      return UNDEF_VALUE;
    }
    return indexValue > -1 ? arrayElements.get(indexValue) : arrayElements.get(arrayElements.size() + indexValue);
  }

  @Override
  protected int getSerializationId() {
    return ARRAY_ELEMENT_ID;
  }

  public PerlValue getArray() {
    return getBaseValue();
  }

  @NotNull
  public PerlValue getIndex() {
    return getParameter();
  }

  @Override
  public String toString() {
    return "ArrayItem: " + getBaseValue() + "[" + getParameter() + "]";
  }

  @NotNull
  public static PerlValue create(@NotNull PerlValue arrayValue, @NotNull PerlValue indexValue) {
    if (arrayValue.isUnknown() || indexValue.isUnknown()) {
      return UNKNOWN_VALUE;
    }
    return new PerlArrayElementValue(arrayValue, indexValue);
  }
}
