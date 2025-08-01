/*
 * Copyright 2015-2025 Alexandr Evstigneev
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

public final class PerlValuesBuilder {
  /**
   * @return converts {@code baseValue} with {@code converter}. Handles cases with {@link PerlOneOfValue}
   */
  public static @NotNull PerlValue convert(@NotNull PerlValue baseValue,
                                           @NotNull Function<? super PerlValue, ? extends PerlValue> converter) {
    if (!(baseValue instanceof PerlOneOfValue oneOfValue)) {
      return converter.apply(baseValue);
    }
    ProgressManager.checkCanceled();
    PerlOneOfValue.Builder builder = PerlOneOfValue.builder();
    oneOfValue.forEach(it -> builder.addVariant(converter.apply(it)));
    return builder.build();
  }

  public static @NotNull PerlValue convert(@NotNull PerlValue baseValue, @NotNull PerlValue parameterValue,
                                           @NotNull BiFunction<? super PerlValue, ? super PerlValue, ? extends PerlValue> converter) {
    return convert(baseValue, first -> convert(parameterValue, second -> converter.apply(first, second)));
  }
}
