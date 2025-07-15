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

import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.RecursionManager;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.util.ObjectUtils;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.serialization.PerlCallValueBackendHelper;
import com.perl5.lang.perl.psi.PerlSubElement;
import com.perl5.lang.perl.psi.utils.PerlContextType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValues.UNKNOWN_VALUE;

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
    Set<String> subNames = resolvedSubNameValue.getSubNames();
    if (subNames.isEmpty()) {
      return UNKNOWN_VALUE;
    }

    Set<String> namespaceNames = PerlCallValueBackendHelper.get(this).computeNamespaceNames(resolvedNamespaceValue);
    if (namespaceNames.isEmpty()) {
      return UNKNOWN_VALUE;
    }

    List<PerlValue> resolvedArguments = computeResolvedArguments(resolvedNamespaceValue, resolver);
    PerlValue argumentsValue = PerlArrayValue.builder().addElements(resolvedArguments).build();

    PerlOneOfValue.Builder builder = PerlOneOfValue.builder();
    boolean[] hasTargets = new boolean[]{false};
    RecursionManager.doPreventingRecursion(
      Pair.create(resolver.getResolveScope(), this), true, () -> {
        PerlCallValueBackendHelper.get(this)
          .processCallTargets(this, resolver.getProject(), resolver.getResolveScope(), resolver.getContextFile(), namespaceNames, subNames,
                              it -> {
          hasTargets[0] = true;
          if (it instanceof PerlSubElement subElement) {
            builder.addVariant(new PerlSubValueResolver(it, argumentsValue).resolve(subElement.getReturnValue()));
            }
            return true;
          });
        return null;
      });

    addFallbackTargets(namespaceNames, subNames, resolvedArguments, hasTargets[0], builder, resolvedNamespaceValue, resolver);

    return builder.build();
  }

  /**
   * Computes a fallback value. This method should handle two cases:
   * - invisible/complex constructor, where we can't compute a proper return value
   * - incorrectly lexed namespace FQNs, where {@code Foo::Bar} was lexed and parsed as {@code Foo::Bar()}
   *
   * @param hasTarget true iff we has processed a real target of this call
   */
  protected abstract void addFallbackTargets(@NotNull Set<String> namespaceNames,
                                             @NotNull Set<String> subNames,
                                             @NotNull List<PerlValue> resolvedArguments,
                                             boolean hasTarget,
                                             @NotNull PerlOneOfValue.Builder builder,
                                             @NotNull PerlValue resolvedNamespaceValue,
                                             @NotNull PerlValueResolver resolver);

  /**
   * @return a list of arguments that passed to the call, resolved in the context of {@code contextElement}
   */
  protected @NotNull List<PerlValue> computeResolvedArguments(@NotNull PerlValue resolvedNamespaceValue,
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
