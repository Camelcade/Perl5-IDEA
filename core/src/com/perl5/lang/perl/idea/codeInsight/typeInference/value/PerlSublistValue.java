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

import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import com.intellij.util.ObjectUtils;
import com.perl5.lang.perl.psi.utils.PerlContextType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.List;

import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValues.UNKNOWN_VALUE;
import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValuesManager.SUBLIST_ID;

public class PerlSublistValue extends PerlOperationValue {
  private final int myStartOffset;
  private final int myEndOffset;

  private PerlSublistValue(@NotNull PerlValue baseValue, int startOffset, int endOffset) {
    super(baseValue);
    myStartOffset = startOffset;
    myEndOffset = endOffset;
    if (baseValue.isDeterministic()) {
      LOG.error("Deterministic values should be computed in-place: " + baseValue);
    }
  }

  PerlSublistValue(@NotNull StubInputStream dataStream) throws IOException {
    super(dataStream);
    myStartOffset = dataStream.readVarInt();
    myEndOffset = dataStream.readVarInt();
  }

  @Override
  protected void serializeData(@NotNull StubOutputStream dataStream) throws IOException {
    super.serializeData(dataStream);
    dataStream.writeVarInt(myStartOffset);
    dataStream.writeVarInt(myEndOffset);
  }

  @NotNull
  @Override
  protected PerlValue computeResolve(@NotNull PerlValue resolvedListValue, @NotNull PerlValueResolver resolver) {
    return computeStrictResolve(resolvedListValue, myStartOffset, myEndOffset);
  }

  @Nullable
  @Override
  protected PerlContextType getContextType() {
    return PerlContextType.LIST;
  }

  @Override
  protected int getSerializationId() {
    return SUBLIST_ID;
  }

  @Override
  public String toString() {
    return "Sublist (" + myStartOffset + ";" + myEndOffset + ") " + getBaseValue();
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

    PerlSublistValue value = (PerlSublistValue)o;

    if (myStartOffset != value.myStartOffset) {
      return false;
    }
    return myEndOffset == value.myEndOffset;
  }

  public int getStartOffset() {
    return myStartOffset;
  }

  public int getEndOffset() {
    return myEndOffset;
  }

  @Override
  protected int computeHashCode() {
    int result = super.computeHashCode();
    result = 31 * result + myStartOffset;
    result = 31 * result + myEndOffset;
    return result;
  }

  @NotNull
  private static PerlValue computeStrictResolve(@NotNull PerlValue listValue, int startOffset, int endOffset) {
    return ObjectUtils.notNull(computeResolve(listValue, startOffset, endOffset), UNKNOWN_VALUE);
  }

  @Nullable
  private static PerlValue computeResolve(@NotNull PerlValue listValue, int startOffset, int endOffset) {
    if (!(listValue instanceof PerlArrayValue)) {
      return null;
    }
    List<PerlValue> elements = ((PerlArrayValue)listValue).getElements();
    int listSize = elements.size();
    if (listSize < startOffset + endOffset) {
      return null;
    }

    for (int i = 0; i < startOffset; i++) {
      PerlValue element = elements.get(i);
      if (element.getContextType() != PerlContextType.SCALAR) {
        return null;
      }
    }

    for (int i = 0; i < endOffset; i++) {
      PerlValue element = elements.get(listSize - 1 - i);
      if (element.getContextType() != PerlContextType.SCALAR) {
        return null;
      }
    }

    return PerlArrayValue.builder().addElements(elements.subList(startOffset, listSize - endOffset)).build();
  }

  @NotNull
  public static PerlValue createShiftValue(@NotNull PerlValue listValue) {
    if (listValue.isDeterministic()) {
      return computeStrictResolve(listValue, 1, 0);
    }

    PerlValue resolvedValue = computeResolve(listValue, 1, 0);
    if (resolvedValue != null) {
      return resolvedValue;
    }

    if (listValue instanceof PerlSublistValue) {
      return new PerlSublistValue(((PerlSublistValue)listValue).getBaseValue(),
                                  ((PerlSublistValue)listValue).getStartOffset() + 1,
                                  ((PerlSublistValue)listValue).getEndOffset());
    }
    return new PerlSublistValue(listValue, 1, 0);
  }

  @NotNull
  public static PerlValue createPopValue(@NotNull PerlValue listValue) {
    if (listValue.isDeterministic()) {
      return computeStrictResolve(listValue, 0, 1);
    }

    PerlValue resolvedValue = computeResolve(listValue, 0, 1);
    if (resolvedValue != null) {
      return resolvedValue;
    }

    if (listValue instanceof PerlSublistValue) {
      return new PerlSublistValue(((PerlSublistValue)listValue).getBaseValue(),
                                  ((PerlSublistValue)listValue).getStartOffset(),
                                  ((PerlSublistValue)listValue).getEndOffset() + 1);
    }
    return new PerlSublistValue(listValue, 0, 1);
  }
}
