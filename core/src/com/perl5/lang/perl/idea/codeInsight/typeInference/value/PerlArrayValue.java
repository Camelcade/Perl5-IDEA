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

import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import com.perl5.PerlBundle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public final class PerlArrayValue extends PerlValue {
  @NotNull
  private final List<PerlValue> myElements;

  public PerlArrayValue(@NotNull List<PerlValue> elements, @Nullable PerlValue bless) {
    super(bless);
    myElements = Collections.unmodifiableList(new ArrayList<>(elements));
  }

  public PerlArrayValue(@NotNull StubInputStream dataStream) throws IOException {
    super(dataStream);
    int size = dataStream.readVarInt();
    if (size == 0) {
      myElements = Collections.emptyList();
    }
    else {
      List<PerlValue> elements = new ArrayList<>(size);
      for (int i = 0; i < size; i++) {
        elements.add(PerlValuesManager.deserialize(dataStream));
      }
      myElements = Collections.unmodifiableList(elements);
    }
  }

  @Override
  protected int getSerializationId() {
    return PerlValuesManager.ARRAY_ID;
  }

  @Override
  protected void serializeData(@NotNull StubOutputStream dataStream) throws IOException {
    dataStream.writeVarInt(myElements.size());
    for (PerlValue element : myElements) {
      element.serialize(dataStream);
    }
  }

  @NotNull
  @Override
  PerlValue createBlessedCopy(@NotNull PerlValue bless) {
    return new PerlArrayValue(this.myElements, bless);
  }

  @NotNull
  public List<PerlValue> getElements() {
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

    PerlArrayValue value = (PerlArrayValue)o;

    return myElements.equals(value.myElements);
  }

  @Override
  public int computeHashCode() {
    int result = super.hashCode();
    result = 31 * result + myElements.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return myElements.toString();
  }

  @NotNull
  @Override
  public String getPresentableValueText() {
    return PerlBundle.message("perl.value.array.presentable",
                              myElements.stream().map(PerlValue::getPresentableText).collect(Collectors.joining(", ")));
  }
}
