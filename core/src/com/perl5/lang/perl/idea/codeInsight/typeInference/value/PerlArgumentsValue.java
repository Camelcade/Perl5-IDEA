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

import com.perl5.PerlBundle;
import org.jetbrains.annotations.NotNull;

import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValuesManager.ARGUMENTS_ID;

/**
 * Pseudo-value representing sub input arguments
 */
public class PerlArgumentsValue extends PerlSpecialValue {
  public static final PerlArgumentsValue ARGUMENTS_VALUE = new PerlArgumentsValue();

  private PerlArgumentsValue() {
  }

  @Override
  protected int getSerializationId() {
    return ARGUMENTS_ID;
  }

  @NotNull
  @Override
  protected String getPresentableValueText() {
    return PerlBundle.message("perl.value.sub.arguments");
  }

  @Override
  public String toString() {
    return "SUB_ARGUMENTS";
  }
}
