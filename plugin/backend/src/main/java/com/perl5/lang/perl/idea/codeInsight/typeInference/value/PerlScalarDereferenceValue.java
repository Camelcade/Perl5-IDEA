/*
 * Copyright 2015-2024 Alexandr Evstigneev
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
import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValuesManager.SCALAR_DEREFERENCE_ID;

public class PerlScalarDereferenceValue extends PerlOperationValue {
  private PerlScalarDereferenceValue(@NotNull PerlValue referenceValue) {
    super(referenceValue);
    if (referenceValue.isDeterministic()) {
      LOG.error("Deterministic values should be resolved in-place: " + referenceValue);
    }
  }

  PerlScalarDereferenceValue(@NotNull PerlValueDeserializer deserializer) throws IOException {
    super(deserializer);
  }

  @Override
  protected @NotNull PerlValue computeResolve(@NotNull PerlValue resolvedReference, @NotNull PerlValueResolver resolver) {
    return doComputeStrictResolve(resolvedReference);
  }

  private static PerlValue doComputeStrictResolve(@NotNull PerlValue referenceValue) {
    return ObjectUtils.notNull(doComputeResolve(referenceValue), UNKNOWN_VALUE);
  }

  private static @Nullable PerlValue doComputeResolve(@NotNull PerlValue referenceValue) {
    if (referenceValue instanceof PerlReferenceValue perlReferenceValue) {
      PerlValue referenceTarget = perlReferenceValue.getTarget();
      if (referenceTarget.getContextType() == PerlContextType.SCALAR) {
        return referenceTarget;
      }
    }
    return null;
  }

  @Override
  protected int getSerializationId() {
    return SCALAR_DEREFERENCE_ID;
  }

  @Override
  public String toString() {
    return "ScalarDeref: " + getBaseValue();
  }

  public static @NotNull PerlValue create(@NotNull PerlValue referenceValue) {
    if (referenceValue.isDeterministic()) {
      return PerlValuesBuilder.convert(referenceValue, PerlScalarDereferenceValue::doComputeStrictResolve);
    }

    PerlValue resolvedValue = doComputeResolve(referenceValue);
    return resolvedValue != null ? resolvedValue : new PerlScalarDereferenceValue(referenceValue);
  }
}
