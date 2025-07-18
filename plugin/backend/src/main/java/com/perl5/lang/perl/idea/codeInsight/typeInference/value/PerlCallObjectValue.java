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

import com.intellij.util.containers.ContainerUtil;
import com.perl5.PerlBundle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.perl5.lang.perl.util.PerlPackageUtil.DEREFERENCE_OPERATOR;

public final class PerlCallObjectValue extends PerlCallValue {
  private final @Nullable String mySuperContext;

  private PerlCallObjectValue(@NotNull PerlValue namespaceNameValue,
                              @NotNull PerlValue subNameValue,
                              @NotNull List<? extends PerlValue> arguments,
                              @Nullable String superContext) {
    super(namespaceNameValue, subNameValue, arguments);
    mySuperContext = superContext;
  }

  public @Nullable String getSuperContext() {
    return mySuperContext;
  }

  @Override
  protected @NotNull List<PerlValue> computeResolvedArguments(@NotNull PerlValue resolvedNamespaceValue,
                                                              @NotNull PerlValueResolver valueResolver) {
    return ContainerUtil.prepend(super.computeResolvedArguments(resolvedNamespaceValue, valueResolver), resolvedNamespaceValue);
  }

  public boolean isSuper() {
    return mySuperContext != null;
  }

  @Override
  protected void addFallbackTargets(@NotNull Set<String> namespaceNames,
                                    @NotNull Set<String> subNames,
                                    @NotNull List<PerlValue> resolvedArguments,
                                    boolean hasTarget,
                                    @NotNull PerlOneOfValue.Builder builder,
                                    @NotNull PerlValue resolvedNamespaceValue,
                                    @NotNull PerlValueResolver resolver) {
    if (subNames.size() == 1 && "new".equals(subNames.iterator().next())) {
      builder.addVariant(resolvedNamespaceValue);
    }
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof PerlCallObjectValue callObjectValue)) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }

    return Objects.equals(mySuperContext, callObjectValue.mySuperContext);
  }

  @Override
  protected int computeHashCode() {
    int result = super.computeHashCode();
    result = 31 * result + (mySuperContext != null ? mySuperContext.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return getNamespaceNameValue() +
           DEREFERENCE_OPERATOR +
           (isSuper() ? mySuperContext + "::SUPER::" : "") +
           getSubNameValue() +
           getArgumentsAsString();
  }

  @Override
  public @NotNull String getPresentableText() {
    return PerlBundle.message(
      "perl.value.call.object.presentable",
      getNamespaceNameValue().getPresentableText(),
      getSubNameValue().getPresentableText(),
      getPresentableArguments());
  }

  public static @NotNull PerlCallObjectValue create(@NotNull String namespace,
                                                    @NotNull String name,
                                                    @NotNull List<? extends PerlValue> arguments) {
    return create(PerlScalarValue.create(namespace), name, arguments, null);
  }

  public static @NotNull PerlCallObjectValue create(@NotNull PerlValue namespaceNameValue,
                                                    @NotNull String name,
                                                    @NotNull List<? extends PerlValue> arguments,
                                                    @Nullable String superContext) {
    return create(namespaceNameValue, PerlScalarValue.create(name), arguments, superContext);
  }

  public static @NotNull PerlCallObjectValue create(@NotNull PerlValue namespaceNameValue,
                                                    @NotNull PerlValue nameValue,
                                                    @NotNull List<? extends PerlValue> arguments,
                                                    @Nullable String superContext) {
    return PerlValuesManager.intern(new PerlCallObjectValue(namespaceNameValue, nameValue, arguments, superContext));
  }
}
