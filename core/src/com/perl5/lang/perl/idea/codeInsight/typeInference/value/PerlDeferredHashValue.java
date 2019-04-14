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
import com.perl5.lang.perl.psi.utils.PerlContextType;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValues.UNKNOWN_VALUE;

public final class PerlDeferredHashValue extends PerlMapValue {
  public PerlDeferredHashValue(@NotNull List<PerlValue> elements) {
    super(elements);
  }

  public PerlDeferredHashValue(@NotNull StubInputStream dataStream) throws IOException {
    super(dataStream);
  }

  /**
   * @return attempts to find a key in the deferred map by iterating the most recently added scalar values
   */
  @NotNull
  public PerlValue tryGet(@NotNull PerlValue key) {
    if (key.isEmpty()) {
      return UNKNOWN_VALUE;
    }
    List<PerlValue> elements = getElements();
    for (int i = elements.size() - 1; i >= 0; i--) {
      PerlValue value = elements.get(i--);
      if (value.getContextType() != PerlContextType.SCALAR) {
        return UNKNOWN_VALUE;
      }
      PerlValue keyElement = elements.get(i);
      if (keyElement.getContextType() != PerlContextType.SCALAR) {
        return UNKNOWN_VALUE;
      }
      if (key.equals(keyElement)) {
        return value;
      }
    }
    return UNKNOWN_VALUE;
  }

  @Override
  protected int getSerializationId() {
    return PerlValuesManager.DEFERRED_HASH_ID;
  }

  @Override
  protected String getValuesPresentableText() {
    return getElements().stream().map(PerlValue::getPresentableText).collect(Collectors.joining(", "));
  }
}
