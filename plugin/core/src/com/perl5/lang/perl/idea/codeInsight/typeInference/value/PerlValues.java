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

import com.intellij.openapi.util.AtomicNotNullLazyValue;

public class PerlValues {
  public static final PerlValue FIRST_ELEMENT_INDEX_VALUE = PerlScalarValue.create(0);
  public static final PerlValue LAST_ELEMENT_INDEX_VALUE = PerlScalarValue.create(-1);

  /**
   * Represents plain {@code undef}
   */
  public static final PerlUndefValue UNDEF_VALUE = PerlUndefValue.INSTANCE;
  /**
   * Represents arguments value that will be passed to the sub on resolve
   */
  public static final PerlArgumentsValue ARGUMENTS_VALUE = PerlArgumentsValue.INSTANCE;
  /**
   * Represents unknown value - means we can't compute it. like get hash element from scalar.
   */
  public static final PerlUnknownValue UNKNOWN_VALUE = PerlUnknownValue.INSTANCE;
  public static final AtomicNotNullLazyValue<PerlValue> UNKNOWN_VALUE_PROVIDER = AtomicNotNullLazyValue.createValue(() -> UNKNOWN_VALUE);
  /**
   * Handy predefined value, that return first argument
   */
  public static final PerlValue RETURN_FIRST_ARGUMENT_VALUE = PerlValuesManager.intern(
    PerlArrayElementValue.create(ARGUMENTS_VALUE, FIRST_ELEMENT_INDEX_VALUE));

  private PerlValues() {
  }
}
