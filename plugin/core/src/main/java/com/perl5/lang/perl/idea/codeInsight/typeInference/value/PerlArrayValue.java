/*
 * Copyright 2015-2022 Alexandr Evstigneev
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

import com.intellij.util.SmartList;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.psi.utils.PerlContextType;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValues.UNDEF_VALUE;
import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValues.UNKNOWN_VALUE;

public final class PerlArrayValue extends PerlListValue implements Iterable<PerlValue> {
  public static final PerlArrayValue EMPTY_ARRAY = PerlValuesManager.intern(new PerlArrayValue(Collections.emptyList()));

  private PerlArrayValue(@NotNull List<PerlValue> elements) {
    super(elements);
  }

  PerlArrayValue(@NotNull PerlValueDeserializer deserializer) throws IOException {
    super(deserializer);
  }

  @Override
  public @NotNull Iterator<PerlValue> iterator() {
    return getElements().iterator();
  }

  public @NotNull PerlValue get(@NotNull PerlValue indexValue) {
    if (!(indexValue instanceof PerlScalarValue) || !isDeterministic()) {
      return UNKNOWN_VALUE;
    }
    int index;
    try {
      index = Integer.valueOf(((PerlScalarValue)indexValue).getValue());
    }
    catch (NumberFormatException ignore) {
      return UNKNOWN_VALUE;
    }

    return doComputeGet(index < 0 ? getElements().size() - 1 : 0,
                        new ElementSearchState(index, index < 0 ? -1 : 0, index < 0 ? -1 : 1)).buildValue();
  }

  /**
   * Attempts to collect all variants of element at {@code targetIndex}. This method is recursive.
   *
   * @param realElementOffset current element offset in {@link #getElements()}
   * @return adjusted search state
   */
  private @NotNull ElementSearchState doComputeGet(int realElementOffset, @NotNull ElementSearchState searchState) {
    List<PerlValue> elements = getElements();

    if (searchState.myStep < 0) {
      for (int i = realElementOffset; i >= 0; i--) {
        if (searchState.isFinished()) {
          return searchState;
        }
        searchState.processValue(elements.get(i));
      }
    }
    else {
      for (int i = realElementOffset; i < elements.size(); i++) {
        if (searchState.isFinished()) {
          return searchState;
        }
        searchState.processValue(elements.get(i));
      }
    }
    return searchState;
  }

  @Override
  protected int getSerializationId() {
    return PerlValuesManager.ARRAY_ID;
  }

  @Override
  public String toString() {
    return "Array: " + getElements();
  }

  @Override
  public @NotNull String getPresentableText() {
    return PerlBundle.message("perl.value.array.presentable",
                              getElements().stream().map(PerlValue::getPresentableText).collect(Collectors.joining(", ")));
  }

  @Override
  protected @NotNull PerlValue computeResolve(@NotNull PerlValueResolver resolver, @NotNull List<PerlValue> resolvedElements) {
    return builder().addElements(resolvedElements).build();
  }

  public static PerlValue create(@NotNull PerlValue ... values){
    if( values.length == 0){
      return EMPTY_ARRAY;
    }
    return PerlValuesManager.intern(new PerlArrayValue(Arrays.asList(values)));
  }

  public static @NotNull Builder builder() {
    return new Builder();
  }

  static class Builder extends PerlListValue.Builder<Builder> {
    private Builder() {
    }

    PerlValue build() {
      if (myElements.isEmpty()) {
        return EMPTY_ARRAY;
      }
      if (myElements.size() == 1) {
        PerlValue firstElement = myElements.get(0);
        if (firstElement.isUnknown()) {
          return UNKNOWN_VALUE;
        }
        // call element exception should be removed after taking call context into account
        if (firstElement.getContextType() == PerlContextType.LIST && !(firstElement instanceof PerlCallValue)) {
          return firstElement;
        }
      }
      return new PerlArrayValue(myElements);
    }
  }

  private static class ElementSearchState {
    private final int myStep;
    private final @NotNull Set<PerlValue> myFoundValues = new HashSet<>();
    private final @NotNull Set<Integer> myReachedOffsets = new HashSet<>();
    private final @NotNull Integer myTargetOffset;

    public ElementSearchState(int targetOffset, int virtualOffset, int step) {
      myTargetOffset = targetOffset;
      myReachedOffsets.add(virtualOffset);
      this.myStep = step;
    }

    private ElementSearchState(@NotNull ElementSearchState state) {
      myReachedOffsets.addAll(state.myReachedOffsets);
      myTargetOffset = state.myTargetOffset;
      myStep = state.myStep;
    }

    public boolean isFinished() {
      return myReachedOffsets.isEmpty();
    }

    public void processValue(@NotNull PerlValue value) {
      if (value.isUnknown()) {
        addValue(UNKNOWN_VALUE);
        myReachedOffsets.clear();
      }
      else if (value.getContextType() == PerlContextType.SCALAR) {
        SmartList<Integer> newOffsets = new SmartList<>();
        for (Integer offset : myReachedOffsets) {
          if (offset.equals(myTargetOffset)) {
            addValue(value);
          }
          else {
            newOffsets.add(offset + myStep);
          }
        }
        myReachedOffsets.clear();
        myReachedOffsets.addAll(newOffsets);
      }
      else if (value instanceof PerlArrayValue) {
        ((PerlArrayValue)value).doComputeGet(0, this);
      }
      else if (value instanceof PerlOneOfValue) {
        List<ElementSearchState> states = new SmartList<>();
        ((PerlOneOfValue)value).forEach(it -> {
          ElementSearchState childState = new ElementSearchState(this);
          childState.processValue(it);
          states.add(childState);
        });
        myReachedOffsets.clear();
        states.forEach(it -> {
          myReachedOffsets.addAll(it.myReachedOffsets);
          myFoundValues.addAll(it.myFoundValues);
        });
      }
    }

    public void addValue(@NotNull PerlValue value) {
      myFoundValues.add(value);
    }

    public @NotNull PerlValue buildValue() {
      if (myFoundValues.size() == 1 && myReachedOffsets.isEmpty()) {
        return myFoundValues.iterator().next();
      }
      if (myFoundValues.isEmpty() && !myReachedOffsets.isEmpty()) {
        return UNDEF_VALUE;
      }

      PerlOneOfValue.Builder resultBuilder = PerlOneOfValue.builder();
      myFoundValues.forEach(resultBuilder::addVariant);
      if (!myReachedOffsets.isEmpty()) {
        resultBuilder.addVariant(UNDEF_VALUE);
      }
      return resultBuilder.build();
    }
  }
}
