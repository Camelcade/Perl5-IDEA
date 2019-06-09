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

import com.perl5.PerlBundle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Set;

import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValues.UNDEF_VALUE;

public final class PerlReferenceValue extends PerlParametrizedOperationValue {
  public PerlReferenceValue(@NotNull PerlValue reference,
                            @NotNull PerlValue bless) {
    super(reference, bless);
  }

  PerlReferenceValue(@NotNull PerlValueDeserializer deserializer) throws IOException {
    super(deserializer);
  }

  @Override
  protected int getSerializationId() {
    return PerlValuesManager.REFERENCE_ID;
  }

  @NotNull
  @Override
  protected PerlValue computeResolve(@NotNull PerlValue resolvedTarget,
                                     @NotNull PerlValue resolvedBless,
                                     @NotNull PerlValueResolver resolver) {
    // fixme casts dereferences should be here
    return resolvedTarget.equals(getTarget()) && resolvedBless.equals(getBless())
           ? this
           : new PerlReferenceValue(resolvedTarget, resolvedBless);
  }

  @NotNull
  @Override
  public Set<String> getNamespaceNames() {
    return getBless().getNamespaceNames();
  }

  @Override
  public boolean canRepresentNamespace(@Nullable String namespaceName) {
    return getBless().canRepresentNamespace(namespaceName);
  }

  @Override
  protected boolean computeIsDeterministic() {
    return getBaseValue().isDeterministic() && getBless().isDeterministic();
  }

  @NotNull
  public PerlValue getTarget() {
    return getBaseValue();
  }

  @NotNull
  public PerlValue getBless() {
    return getParameter();
  }

  @Override
  public String toString() {
    PerlValue bless = getBless();
    String baseName = "Reference to: " + getTarget();
    return bless.isUndef() ? baseName : baseName + " blessed with " + bless;
  }

  @NotNull
  @Override
  public String getPresentableText() {
    return getBless().isUndef() ? PerlBundle.message("perl.value.reference.presentable", getTarget().getPresentableText()) :
           PerlBundle
             .message("perl.value.reference.blessed.presentable", getTarget().getPresentableText(), getBless().getPresentableText());
  }

  @NotNull
  public static PerlValue create(@NotNull PerlValue referent) {
    return create(referent, UNDEF_VALUE);
  }

  @NotNull
  public static PerlValue create(@NotNull PerlValue referent, @NotNull PerlValue bless) {
    return referent.isUnknown() ? referent : new PerlReferenceValue(referent, bless);
  }
}
