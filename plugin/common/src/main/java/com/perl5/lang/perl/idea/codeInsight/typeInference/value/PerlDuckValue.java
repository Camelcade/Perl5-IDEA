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

import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValues.UNKNOWN_VALUE;

public class PerlDuckValue extends PerlListValue {
  private static final Set<String> GENERIC_NAMES = Set.of("new", "isa", "DOES", "can", "VERSION", "IntellijIdeaRulezzz");

  private PerlDuckValue(@NotNull List<? extends PerlValue> elements) {
    super(elements);
  }

  public static @NotNull PerlValue create(@NotNull List<? extends PerlValue> elements) {
    return elements.isEmpty() ? UNKNOWN_VALUE : new PerlDuckValue(elements);
  }

  @Override
  protected @NotNull PerlValue computeResolve(@NotNull PerlValueResolver resolver, @NotNull List<? extends PerlValue> resolvedElements) {
    return PerlValueResolveService.getInstance(resolver.getProject()).computeResolve(this, resolver, resolvedElements);
  }

  @Override
  public String toString() {
    return "DuckType: " + getElements().stream().map(Object::toString).sorted().toList();
  }

  @Override
  protected boolean computeIsDeterministic() {
    return isEmpty();
  }

  public static final class Builder {
    private final Set<PerlValue> myElements = new HashSet<>();

    private Builder() {
    }

    public void clear() {
      myElements.clear();
    }

    public @NotNull PerlValue build() {
      return isEmpty() ? UNKNOWN_VALUE : new PerlDuckValue(List.copyOf(myElements));
    }

    public @NotNull Builder addElement(@NotNull String callableName) {
      if (!GENERIC_NAMES.contains(callableName)) {
        myElements.add(PerlScalarValue.create(callableName));
      }
      return this;
    }

    public boolean isEmpty() {
      return myElements.isEmpty();
    }
  }

  public static @NotNull Builder builder() {
    return new Builder();
  }
}
