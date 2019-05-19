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

import java.util.function.BiFunction;
import java.util.function.Function;

import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlOneOfValue.builder;

public final class PerlValuesBuilder {
  /**
   * @return converts {@code baseValue} with {@code converter}. Handles cases with {@link PerlOneOfValue}
   */
  @NotNull
  public static PerlValue convert(@NotNull PerlValue baseValue, @NotNull Function<PerlValue, PerlValue> converter) {
    if (!(baseValue instanceof PerlOneOfValue)) {
      return converter.apply(baseValue);
    }
    ProgressManager.checkCanceled();
    PerlOneOfValue.Builder builder = builder();
    ((PerlOneOfValue)baseValue).forEach(it -> builder.addVariant(converter.apply(it)));
    return builder.build();
  }

  @NotNull
  public static PerlValue convert(@NotNull PerlValue baseValue, @NotNull PerlValue parameterValue,
                                  @NotNull BiFunction<PerlValue, PerlValue, PerlValue> converter) {
    return convert(baseValue, first -> convert(parameterValue, second -> converter.apply(first, second)));
  }
}
