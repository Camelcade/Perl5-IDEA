/*
 * Copyright 2015-2025 Alexandr Evstigneev
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

import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.util.ObjectUtils;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.lang.perl.psi.utils.PerlContextType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Represents a method call value
 */
public abstract class PerlCallValue extends PerlParametrizedOperationValue {
  protected final @NotNull List<PerlValue> myArguments;

  protected PerlCallValue(@NotNull PerlValue namespaceNameValue,
                          @NotNull PerlValue subNameValue,
                          @NotNull List<? extends PerlValue> arguments) {
    super(namespaceNameValue, subNameValue);
    myArguments = List.copyOf(arguments);
  }

  @Override
  protected @NotNull PerlValue computeResolve(@NotNull PerlValue resolvedNamespaceValue,
                                              @NotNull PerlValue resolvedSubNameValue,
                                              @NotNull PerlValueResolver resolver) {
    return PerlValueResolveService.getInstance(resolver.getProject())
      .computeResolve(this, resolvedNamespaceValue, resolvedSubNameValue, resolver);
  }

  /**
   * @return a list of arguments that passed to the call, resolved in the context of {@code contextElement}
   */
  public @NotNull List<PerlValue> computeResolvedArguments(@NotNull PerlValue resolvedNamespaceValue,
                                                              @NotNull PerlValueResolver valueResolver) {
    return ContainerUtil.map(myArguments, valueResolver::resolve);
  }

  public final @NotNull List<PerlValue> getArguments() {
    return myArguments;
  }

  @Override
  protected final @NotNull PerlContextType getContextType() {
    return PerlContextType.LIST;
  }

  public @NotNull PerlValue getNamespaceNameValue() {
    return getBaseValue();
  }

  public @NotNull PerlValue getSubNameValue() {
    return getParameter();
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

    PerlCallValue value = (PerlCallValue)o;

    return myArguments.equals(value.myArguments);
  }


  @Override
  protected int computeHashCode() {
    int result = super.computeHashCode();
    result = 31 * result + myArguments.hashCode();
    return result;
  }

  protected final @NotNull String getPresentableArguments() {
    return StringUtil.join(ContainerUtil.map(myArguments, PerlValue::getPresentableText), ", ");
  }

  protected final String getArgumentsAsString() {
    return "(" + StringUtil.join(ContainerUtil.map(myArguments, PerlValue::toString), ", ") + ")";
  }

  @Contract("null->null")
  public static @Nullable PerlCallValue from(@Nullable PsiElement element) {
    return ObjectUtils.tryCast(PerlValuesManager.from(element), PerlCallValue.class);
  }
}
