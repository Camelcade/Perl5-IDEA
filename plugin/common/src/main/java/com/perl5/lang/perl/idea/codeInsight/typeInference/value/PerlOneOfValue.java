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
import com.intellij.util.containers.ContainerUtil;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.psi.utils.PerlContextType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValues.UNKNOWN_VALUE;

public final class PerlOneOfValue extends PerlValue implements Iterable<PerlValue> {
  private static final int MAX_VARIANTS_NUMBER = 1024;

  private final @NotNull Set<PerlValue> myVariants;

  public PerlOneOfValue(@NotNull Set<? extends PerlValue> variants) {
    myVariants = Collections.unmodifiableSet(variants);
  }

  @Override
  protected @Nullable PerlContextType getContextType() {
    if (myVariants.isEmpty()) {
      return null;
    }
    PerlContextType contextType = null;
    boolean first = true;
    for (PerlValue variant : myVariants) {
      if (first) {
        contextType = variant.getContextType();
        first = false;
        continue;
      }
      if (contextType != variant.getContextType()) {
        return null;
      }
    }
    return contextType;
  }

  @Override
  public @NotNull Iterator<PerlValue> iterator() {
    return myVariants.iterator();
  }

  public @NotNull Set<PerlValue> getVariants() {
    return myVariants;
  }

  @Override
  protected boolean computeIsDeterministic() {
    return PerlListValue.isDeterministic(myVariants);
  }

  @Override
  public @NotNull Set<String> getNamespaceNames() {
    if (myVariants.isEmpty()) {
      return Collections.emptySet();
    }
    Set<String> result = new HashSet<>();
    forEach(it -> result.addAll(it.getNamespaceNames()));
    return result;
  }

  @Override
  public @NotNull Set<String> getSubNames() {
    if (myVariants.isEmpty()) {
      return Collections.emptySet();
    }
    Set<String> result = new HashSet<>();
    forEach(it -> result.addAll(it.getSubNames()));
    return result;
  }

  @Override
  public boolean canRepresentNamespace(@Nullable String namespaceName) {
    for (PerlValue variant : myVariants) {
      if (variant.canRepresentNamespace(namespaceName)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean canRepresentSubName(@Nullable String subName) {
    for (PerlValue variant : myVariants) {
      if (variant.canRepresentSubName(subName)) {
        return true;
      }
    }
    return false;
  }

  @NotNull
  @Override
  PerlValue computeResolve(@NotNull PerlValueResolver resolver) {
    return PerlValuesBuilder.convert(this, resolver::resolve);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PerlOneOfValue of = (PerlOneOfValue)o;

    return myVariants.equals(of.myVariants);
  }

  @Override
  protected int computeHashCode() {
    int result = super.computeHashCode();
    result = 31 * result + myVariants.hashCode();
    return result;
  }

  @Override
  public String toString() {
    List<String> variants = new ArrayList<>(ContainerUtil.map(myVariants, PerlValue::toString));
    ContainerUtil.sort(variants);
    return "OneOf: [" + StringUtil.join(variants, ", ") + "]";
  }

  @Override
  public @NotNull String getPresentableText() {
    List<String> variants = new ArrayList<>(ContainerUtil.map(myVariants, PerlValue::getPresentableText));
    ContainerUtil.sort(variants);
    return PerlBundle.message("perl.value.oneof.static.presentable", StringUtil.join(variants, ",\n"));
  }

  public static @NotNull Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private final @NotNull Set<PerlValue> myVariants = new HashSet<>();

    public void clear() {
      myVariants.clear();
    }

    private Builder(@NotNull PsiElement... elements) {
      addVariants(elements);
    }

    public Builder addVariants(@NotNull PsiElement... elements) {
      for (PsiElement element : elements) {
        if (myVariants.size() >= MAX_VARIANTS_NUMBER) {
          break;
        }
        addVariant(element);
      }
      return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public Builder addVariant(@Nullable PsiElement element) {
      return addVariant(PerlValuesManager.from(element));
    }

    public Builder addVariant(@Nullable PerlValue variant) {
      if (variant == null || variant == UNKNOWN_VALUE) {
        return this;
      }

      if (variant instanceof PerlOneOfValue oneOfValue) {
        for (PerlValue value : oneOfValue.myVariants) {
          if (myVariants.size() >= MAX_VARIANTS_NUMBER) {
            break;
          }
          addVariant(value);
        }
      }
      else if (myVariants.size() < MAX_VARIANTS_NUMBER) {
        myVariants.add(variant);
      }
      return this;
    }

    public boolean isEmpty(){
      return myVariants.isEmpty();
    }

    public @NotNull PerlValue build() {
      if (myVariants.isEmpty()) {
        return UNKNOWN_VALUE;
      }
      else if (myVariants.size() == 1) {
        return myVariants.iterator().next();
      }
      else {
        return new PerlOneOfValue(myVariants);
      }
    }
  }
}
