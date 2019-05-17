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

import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.lang.perl.psi.utils.PerlContextType;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.*;

import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValues.UNKNOWN_VALUE;

abstract class PerlListValue extends PerlValue {
  @NotNull
  private final List<PerlValue> myElements;

  protected PerlListValue(@NotNull List<PerlValue> elements) {
    myElements = elements.isEmpty() ? Collections.emptyList() : Collections.unmodifiableList(elements);
  }

  protected PerlListValue(@NotNull StubInputStream dataStream) throws IOException {
    super(dataStream);
    myElements = PerlValuesManager.readList(dataStream);
  }

  @Override
  protected void serializeData(@NotNull StubOutputStream dataStream) throws IOException {
    PerlValuesManager.writeCollection(dataStream, myElements);
  }

  @Override
  final PerlValue computeResolve(@NotNull PsiElement contextElement,
                                 @NotNull Map<PerlValue, PerlValue> substitutions) {
    return computeResolve(contextElement, ContainerUtil.map(myElements, it -> it.resolve(contextElement, substitutions)));
  }

  @NotNull
  protected abstract PerlValue computeResolve(@NotNull PsiElement contextElement,
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

  @NotNull
  @Override
  protected PerlValue createScalarDereference() {
    return UNKNOWN_VALUE;
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

  protected static abstract class Builder<Self extends PerlListValue.Builder> {
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
      myElements.addAll(element.getListRepresentation());
      //noinspection unchecked
      return (Self)this;
    }
  }
}
