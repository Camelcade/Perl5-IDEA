/*
 * Copyright 2015-2018 Alexandr Evstigneev
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    PerlValuesManager.writeList(dataStream, myElements);
  }

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
    if (!super.equals(o)) {
      return false;
    }

    PerlListValue value = (PerlListValue)o;

    return myElements.equals(value.myElements);
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
      return addElements(ContainerUtil.map(psiElements, PerlValue::from));
    }

    public Self addElements(@NotNull List<PerlValue> elements) {
      elements.forEach(it -> {
        if (it instanceof PerlArrayValue || it instanceof PerlHashValue) {
          myElements.addAll(((PerlListValue)it).myElements);
        }
        else {
          myElements.add(it);
        }
      });
      //noinspection unchecked
      return (Self)this;
    }
  }
}
