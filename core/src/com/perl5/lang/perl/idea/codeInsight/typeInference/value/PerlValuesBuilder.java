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

import com.intellij.openapi.progress.ProgressManager;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlOneOfValue.builder;
import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValues.UNKNOWN_VALUE;

public final class PerlValuesBuilder {
  /**
   * @return converts {@code baseValue} with {@code converter}. Handles cases with {@link PerlOneOfValue}
   */
  @NotNull
  public static PerlValue convert(@NotNull PerlValue baseValue, Function<PerlValue, PerlValue> converter) {
    if (!(baseValue instanceof PerlOneOfValue)) {
      return converter.apply(baseValue);
    }
    ProgressManager.checkCanceled();
    PerlOneOfValue.Builder builder = builder();
    ((PerlOneOfValue)baseValue).forEach(it -> builder.addVariant(converter.apply(it)));
    return builder.build();
  }

  /**
   * Works the same way as {@link #convert(Function)}, but returns {@link PerlValues#UNKNOWN_VALUE} if
   * converter returned {@code UNKNOWN_VALUE} at least once.
   *
   * @see PerlHashElementValue#create(com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValue, com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValue)
   */
  @NotNull
  public static PerlValue convertStrict(@NotNull PerlValue baseValue, @NotNull Function<PerlValue, PerlValue> converter) {
    if (!(baseValue instanceof PerlOneOfValue)) {
      return converter.apply(baseValue);
    }
    ProgressManager.checkCanceled();
    PerlOneOfValue.Builder builder = builder();
    for (PerlValue variant : (PerlOneOfValue)baseValue) {
      PerlValue convertedValue = converter.apply(variant);
      if (convertedValue.isUnknown()) {
        return UNKNOWN_VALUE;
      }
      builder.addVariant(convertedValue);
    }
    return builder.build();
  }

}
