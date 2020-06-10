/*
 * Copyright 2015-2020 Alexandr Evstigneev
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

abstract class PerlListValue extends PerlValue {
  @NotNull
  private final List<PerlValue> myElements;

  protected PerlListValue(@NotNull List<PerlValue> elements) {
    myElements = elements.isEmpty() ? Collections.emptyList() : Collections.unmodifiableList(elements);
  }

  protected PerlListValue(@NotNull PerlValueDeserializer deserializer) throws IOException {
    super(deserializer);
    myElements = deserializer.readValuesList();
  }

  @Override
  protected void serializeData(@NotNull PerlValueSerializer serializer) throws IOException {
    serializer.writeValuesList(myElements);
  }

  @Override
  protected boolean computeIsDeterministic() {
    return isDeterministic(getElements());
  }

  @NotNull
  @Override
  final PerlValue computeResolve(@NotNull PerlValueResolver resolver) {
    return computeResolve(resolver, ContainerUtil.map(myElements, resolver::resolve));
  }

  @NotNull
  protected abstract PerlValue computeResolve(@NotNull PerlValueResolver resolver,
                                              @NotNull List<PerlValue> resolvedElements);

  @NotNull
  @Override
  protected final PerlContextType getContextType() {
    return PerlContextType.LIST;
  }

  @NotNull
  public final List<PerlValue> getElements() {
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
  static boolean isDeterministic(@NotNull Collection<PerlValue> values) {
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

    public Self addPsiElements(@NotNull List<PsiElement> psiElements) {
      psiElements.forEach(it -> addElement(PerlValuesManager.from(it)));
      //noinspection unchecked
      return (Self)this;
    }

    public Self addElements(@NotNull List<PerlValue> elements) {
      elements.forEach(this::addElement);
      //noinspection unchecked
      return (Self)this;
    }

    public Self addElement(@NotNull PerlValue element) {
      if (element instanceof PerlArrayValue) {
        myElements.addAll(((PerlArrayValue)element).getElements());
      }
      else if (element instanceof PerlHashValue) {
        myElements.addAll(((PerlHashValue)element).getElements());
      }
      else {
        myElements.add(element);
      }
      //noinspection unchecked
      return (Self)this;
    }
  }
}
