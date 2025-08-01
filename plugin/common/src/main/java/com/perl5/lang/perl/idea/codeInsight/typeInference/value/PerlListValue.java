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

import com.intellij.psi.PsiElement;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.lang.perl.psi.utils.PerlContextType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class PerlListValue extends PerlValue {
  private final @NotNull List<PerlValue> myElements;

  protected PerlListValue(@NotNull List<? extends PerlValue> elements) {
    myElements = elements.isEmpty() ? Collections.emptyList() : Collections.unmodifiableList(elements);
  }

  public final boolean isEmpty() {
    return getElements().isEmpty();
  }

  @Override
  protected boolean computeIsDeterministic() {
    return isDeterministic(getElements());
  }

  @Override
  final @NotNull PerlValue computeResolve(@NotNull PerlValueResolver resolver) {
    return computeResolve(resolver, ContainerUtil.map(myElements, resolver::resolve));
  }

  protected abstract @NotNull PerlValue computeResolve(@NotNull PerlValueResolver resolver,
                                                       @NotNull List<? extends PerlValue> resolvedElements);

  @Override
  protected final @NotNull PerlContextType getContextType() {
    return PerlContextType.LIST;
  }

  public final @NotNull List<PerlValue> getElements() {
    return myElements;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    PerlListValue value = (PerlListValue)o;

    return myElements.equals(value.myElements);
  }

  /**
   * @return true iff all {@code values} are deterministic
   */
  static boolean isDeterministic(@NotNull Collection<? extends PerlValue> values) {
    for (PerlValue value : values) {
      if (!value.isDeterministic()) {
        return false;
      }
    }
    return true;
  }

  @Override
  public int computeHashCode() {
    int result = super.computeHashCode();
    result = 31 * result + myElements.hashCode();
    return result;
  }

  protected abstract static class Builder<Self extends PerlListValue.Builder<?>> {
    protected final List<PerlValue> myElements = new ArrayList<>();

    protected Builder() {
    }

    public Self addPsiElements(@NotNull List<? extends PsiElement> psiElements) {
      psiElements.forEach(it -> addElement(PerlValuesManager.from(it)));
      //noinspection unchecked
      return (Self)this;
    }

    public Self addElements(@NotNull List<? extends PerlValue> elements) {
      elements.forEach(this::addElement);
      //noinspection unchecked
      return (Self)this;
    }

    public Self addElement(@NotNull PerlValue element) {
      if (element instanceof PerlArrayValue arrayValue) {
        myElements.addAll(arrayValue.getElements());
      }
      else if (element instanceof PerlHashValue hashValue) {
        myElements.addAll(hashValue.getElements());
      }
      else {
        myElements.add(element);
      }
      //noinspection unchecked
      return (Self)this;
    }
  }
}
