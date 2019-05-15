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

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.StubInputStream;
import com.perl5.PerlBundle;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValues.UNDEF_VALUE;
import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValues.UNKNOWN_VALUE;

public final class PerlArrayValue extends PerlListValue implements Iterable<PerlValue> {
  private static final Logger LOG = Logger.getInstance(PerlArrayValue.class);
  public static final PerlArrayValue EMPTY_ARRAY = PerlValuesManager.intern(new PerlArrayValue(Collections.emptyList()));

  private PerlArrayValue(@NotNull List<PerlValue> elements) {
    super(elements);
  }

  PerlArrayValue(@NotNull StubInputStream dataStream) throws IOException {
    super(dataStream);
  }

  @Override
  protected boolean computeIsDeterministic() {
    return isDeterministic(getElements());
  }

  @NotNull
  @Override
  public Iterator iterator() {
    return getElements().iterator();
  }

  @NotNull
  public PerlValue get(@NotNull PerlValue indexValue) {
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
    List<PerlValue> arrayElements = getElements();
    if (index >= arrayElements.size() || index < -arrayElements.size()) {
      return UNDEF_VALUE;
    }
    return index > -1 ? arrayElements.get(index) : arrayElements.get(arrayElements.size() + index);
  }

  @NotNull
  @Override
  protected PerlValue computeScalarRepresentation() {
    LOG.assertTrue(isDeterministic());
    return PerlScalarValue.create(Integer.toString(getElements().size()));
  }

  @NotNull
  @Override
  public List<PerlValue> getListRepresentation() {
    return getElements();
  }

  @Override
  protected int getSerializationId() {
    return PerlValuesManager.ARRAY_ID;
  }

  @Override
  public String toString() {
    return "Array: " + getElements().toString();
  }

  @NotNull
  @Override
  public String getPresentableText() {
    return PerlBundle.message("perl.value.array.presentable",
                              getElements().stream().map(PerlValue::getPresentableText).collect(Collectors.joining(", ")));
  }

  @NotNull
  @Override
  protected PerlValue computeResolve(@NotNull PsiElement contextElement,
                                     @NotNull List<PerlValue> resolvedElements) {
    return builder().addElements(resolvedElements).build();
  }

  @NotNull
  public static Builder builder() {
    return new Builder();
  }

  static class Builder extends PerlListValue.Builder<Builder> {
    private Builder() {
    }

    PerlArrayValue build() {
      return myElements.isEmpty() ? EMPTY_ARRAY : new PerlArrayValue(myElements);
    }
  }
}
