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
import com.perl5.PerlBundle;
import com.perl5.lang.perl.psi.utils.PerlContextType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

public final class PerlReferenceValue extends PerlOperationValue {

  PerlReferenceValue(@NotNull PerlValue referrent) {
    super(referrent);
  }

  PerlReferenceValue(@NotNull StubInputStream dataStream) throws IOException {
    super(dataStream);
  }

  @Override
  protected int getSerializationId() {
    return PerlValuesManager.REFERENCE_ID;
  }

  @NotNull
  @Override
  protected PerlValue computeResolve(@NotNull PerlValue resolvedTarget, @NotNull PerlValueResolver resolver) {
    // fixme casts dereferences should be here
    return resolvedTarget.equals(getTarget()) ? this : new PerlReferenceValue(resolvedTarget);
  }

  @NotNull
  @Override
  protected PerlValue createScalarDereference() {
    PerlValue value = getBaseValue();
    if (value.getContextType() == PerlContextType.SCALAR) {
      return value;
    }
    return super.createScalarDereference();
  }

  @NotNull
  @Override
  public Set<String> getNamespaceNames() {
    return getTarget() instanceof PerlBlessedValue ? getTarget().getNamespaceNames() : Collections.emptySet();
  }

  @Override
  public boolean canRepresentNamespace(@Nullable String namespaceName) {
    return getTarget() instanceof PerlBlessedValue && getTarget().canRepresentNamespace(namespaceName);
  }

  @Override
  protected boolean computeIsDeterministic() {
    return getBaseValue().isDeterministic();
  }

  @NotNull
  public PerlValue getTarget() {
    return getBaseValue();
  }

  @Override
  public String toString() {
    return "Reference to: " + getBaseValue();
  }

  @NotNull
  @Override
  public String getPresentableText() {
    return PerlBundle.message("perl.value.reference.presentable", getBaseValue().getPresentableText());
  }

  @NotNull
  public static PerlValue create(@NotNull PerlValue referent) {
    return PerlValuesBuilder.convert(referent, it -> referent.isUnknown() ? referent : new PerlReferenceValue(referent));
  }
}
