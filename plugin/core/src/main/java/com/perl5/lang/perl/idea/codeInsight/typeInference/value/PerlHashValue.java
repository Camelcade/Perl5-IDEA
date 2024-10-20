/*
 * Copyright 2015-2024 Alexandr Evstigneev
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

import com.intellij.util.ObjectUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValues.UNDEF_VALUE;

public class PerlHashValue extends PerlMapValue {
  /**
   * Transient map for faster seek operations
   */
  private @Nullable Map<PerlValue, PerlValue> myMap;

  public PerlHashValue(@NotNull List<PerlValue> elements) {
    super(elements);
  }

  public PerlHashValue(@NotNull PerlValueDeserializer deserializer) throws IOException {
    super(deserializer);
  }

  public @NotNull Map<PerlValue, PerlValue> getMap() {
    if (myMap == null) {
      Map<PerlValue, PerlValue> map = new HashMap<>();
      List<PerlValue> elements = getElements();
      for (int i = 0, size = elements.size(); i < size; i++) {
        map.put(elements.get(i++), elements.get(i));
      }
      return myMap = Collections.unmodifiableMap(map);
    }
    return myMap;
  }

  /**
   * @return a value for the {@code key}
   */
  public @NotNull PerlValue get(@NotNull PerlValue key) {
    return ObjectUtils.notNull(getMap().get(key), UNDEF_VALUE);
  }

  @Override
  protected int getSerializationId() {
    return PerlValuesManager.HASH_ID;
  }

  @Override
  protected String getValuesPresentableText() {
    List<PerlValue> elements = getElements();
    StringBuilder sb = new StringBuilder();
    for (int i = 0, size = elements.size(); i < size; i++) {
      if (!sb.isEmpty()) {
        sb.append(",\n");
      }
      sb.append(elements.get(i++).getPresentableText()).append(" => ").append(elements.get(i));
    }
    return sb.toString();
  }
}
