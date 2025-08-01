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

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PerlDefaultArgumentValue extends PerlParametrizedOperationValue {
  private final int myArgumentIndex;

  private PerlDefaultArgumentValue(@NotNull PerlValue baseValue,
                                   @NotNull PerlValue defaultValue,
                                   int argumentIndex) {
    super(baseValue, defaultValue);
    myArgumentIndex = argumentIndex;
  }

  public final int getArgumentIndex() {
    return myArgumentIndex;
  }

  @Override
  protected @NotNull PerlValue computeResolve(@NotNull PerlValue resolvedMainValue,
                                              @NotNull PerlValue resolvedDefaultValue,
                                              @NotNull PerlValueResolver resolver) {
    if (resolver instanceof PerlSubValueResolver) {
      PerlValue resolvedArguments = resolver.resolve(PerlValues.ARGUMENTS_VALUE);
      if (!(resolvedArguments instanceof PerlArrayValue arrayValue)) {
        return resolvedDefaultValue;
      }
      List<PerlValue> argumentElements = arrayValue.getElements();
      return argumentElements.size() <= myArgumentIndex ? resolvedDefaultValue : resolvedMainValue;
    }
    else {
      return resolvedMainValue.isUnknown() ? resolvedDefaultValue : resolvedMainValue;
    }
  }

  @Override
  public String toString() {
    return getBaseValue() + "(with default " + getParameter() + ")";
  }

  public static PerlValue create(@NotNull PerlValue mainValue, @NotNull PerlValue fallbackValue, int argumentIndex) {
    if (fallbackValue.isUnknown()) {
      return mainValue;
    }
    return PerlValuesManager.intern(new PerlDefaultArgumentValue(mainValue, fallbackValue, argumentIndex));
  }
}
