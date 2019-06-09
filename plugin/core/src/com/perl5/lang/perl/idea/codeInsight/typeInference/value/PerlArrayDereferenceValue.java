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

import com.intellij.util.ObjectUtils;
import com.perl5.lang.perl.psi.utils.PerlContextType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValues.UNKNOWN_VALUE;
import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValuesManager.ARRAY_DEREFERENCE_ID;

public class PerlArrayDereferenceValue extends PerlOperationValue {
  private PerlArrayDereferenceValue(@NotNull PerlValue referenceValue) {
    super(referenceValue);
    if (referenceValue.isDeterministic()) {
      LOG.error("Deterministic values should be resolved in-place: " + referenceValue);
    }
  }

  PerlArrayDereferenceValue(@NotNull PerlValueDeserializer deserializer) throws IOException {
    super(deserializer);
  }

  @Nullable
  @Override
  protected PerlContextType getContextType() {
    return PerlContextType.LIST;
  }

  @NotNull
  @Override
  protected PerlValue computeResolve(@NotNull PerlValue resolvedReference, @NotNull PerlValueResolver resolver) {
    return doComputeStrictResolve(resolvedReference);
  }

  @Override
  protected int getSerializationId() {
    return ARRAY_DEREFERENCE_ID;
  }

  @Override
  public String toString() {
    return "ArrayDeref: " + getBaseValue();
  }

  private static PerlValue doComputeStrictResolve(@NotNull PerlValue referenceValue) {
    return ObjectUtils.notNull(doComputeResolve(referenceValue), UNKNOWN_VALUE);
  }

  @Nullable
  private static PerlValue doComputeResolve(@NotNull PerlValue referenceValue) {
    if (referenceValue instanceof PerlReferenceValue) {
      PerlValue referenceTarget = ((PerlReferenceValue)referenceValue).getTarget();
      if (referenceTarget instanceof PerlArrayValue) {
        return referenceTarget;
      }
    }
    return null;
  }

  @NotNull
  public static PerlValue create(@NotNull PerlValue referenceValue) {
    if (referenceValue.isDeterministic()) {
      return PerlValuesBuilder.convert(referenceValue, PerlArrayDereferenceValue::doComputeStrictResolve);
    }

    PerlValue resolvedValue = doComputeResolve(referenceValue);
    return resolvedValue != null ? resolvedValue : new PerlArrayDereferenceValue(referenceValue);
  }
}
