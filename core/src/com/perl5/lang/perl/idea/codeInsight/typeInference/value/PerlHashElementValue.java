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
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Map;

import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValues.UNKNOWN_VALUE;
import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValuesManager.HASH_ELEMENT_VALUE;

public final class PerlHashElementValue extends PerlParametrizedOperationValue {
  private PerlHashElementValue(@NotNull PerlValue baseValue,
                               @NotNull PerlValue parameter) {
    super(baseValue, parameter);
  }

  PerlHashElementValue(@NotNull StubInputStream dataStream) throws IOException {
    super(dataStream);
  }

  @Override
  protected int getSerializationId() {
    return HASH_ELEMENT_VALUE;
  }

  @NotNull
  @Override
  protected PerlValue computeResolve(@NotNull PsiElement contextElement,
                                     @NotNull PerlValue resolvedHashValue,
                                     @NotNull PerlValue resolvedKeyValue,
                                     @NotNull Map<PerlValue, PerlValue> substitutions) {
    if (resolvedHashValue instanceof PerlHashValue) {
      return ((PerlHashValue)resolvedHashValue).get(resolvedKeyValue);
    }
    else if (resolvedHashValue instanceof PerlDeferredHashValue) {
      return ((PerlDeferredHashValue)resolvedHashValue).tryGet(resolvedKeyValue);
    }
    return UNKNOWN_VALUE;
  }

  public PerlValue getHash() {
    return getBaseValue();
  }

  @NotNull
  public PerlValue getKey() {
    return getParameter();
  }

  @Override
  public String toString() {
    return "HashItem: " + getBaseValue() + "{" + getParameter() + "}";
  }

  @NotNull
  public static PerlValue create(@NotNull PerlValue hashValue, @NotNull PerlValue key) {
    if (hashValue.isEmpty() || key.isEmpty()) {
      return UNKNOWN_VALUE;
    }

    return hashValue.convert(hash -> {
      if (hash instanceof PerlHashValue) {
        return key.convert(((PerlHashValue)hash)::get);
      }
      else if (hash instanceof PerlDeferredHashValue) {
        PerlValue element = key.convertStrict(((PerlDeferredHashValue)hash)::tryGet);
        if (!element.isEmpty()) {
          return element;
        }
      }

      return new PerlHashElementValue(hash, key);
    });
  }
}
