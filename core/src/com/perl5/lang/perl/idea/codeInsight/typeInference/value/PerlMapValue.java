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
import com.perl5.PerlBundle;
import com.perl5.lang.perl.psi.utils.PerlContextType;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlUnknownValue.UNKNOWN_VALUE;

public abstract class PerlMapValue extends PerlListValue {
  protected PerlMapValue(@NotNull List<PerlValue> elements) {
    super(elements);
  }

  PerlMapValue(@NotNull StubInputStream dataStream) throws IOException {
    super(dataStream);
  }

  @Override
  public String toString() {
    return "Hash: " + getElements().toString();
  }

  @NotNull
  @Override
  public String getPresentableText() {
    return PerlBundle.message("perl.value.hash.presentable", getValuesPresentableText());
  }

  protected abstract String getValuesPresentableText();

  @NotNull
  public static Builder builder() {
    return new Builder();
  }

  /**
   * @implNote we have two different maps: deterministic and deferred.
   * Deterministic means that all keys and values has scalar context
   */
  static class Builder extends PerlListValue.Builder<Builder> {
    private Builder() {
    }

    PerlValue build() {
      Map<PerlValue, PerlValue> map = new LinkedHashMap<>();
      for (int i = 0; i < myElements.size(); i++) {
        PerlValue key = myElements.get(i);
        if (key.getContextType() != PerlContextType.SCALAR) {
          map = null;
          break;
        }
        i++;
        if (i == myElements.size()) {
          // odd number of elements while still deterministic
          return UNKNOWN_VALUE;
        }
        PerlValue value = myElements.get(i);
        if (value.getContextType() != PerlContextType.SCALAR) {
          map = null;
          break;
        }
        map.put(key, value);
      }

      if (map == null) {
        // fixme we could optimize sequential scalar keys
        return new PerlDeferredHashValue(myElements);
      }
      List<PerlValue> mapElements = new ArrayList<>(map.size() * 2);
      ArrayList<Map.Entry<PerlValue, PerlValue>> entries = new ArrayList<>(map.entrySet());
      entries.forEach(it -> {
        mapElements.add(it.getKey());
        mapElements.add(it.getValue());
      });

      return new PerlHashValue(mapElements);
    }
  }
}
