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

import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlArgumentsValue.ARGUMENTS_VALUE;

public interface PerlValues {
  PerlValue RETURN_FIRST_ARGUMENT_VALUE = PerlValuesManager.intern(PerlArrayElementValue.create(
    ARGUMENTS_VALUE, PerlScalarValue.create("0")
  ));
}
