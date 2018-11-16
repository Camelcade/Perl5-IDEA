/*
 * Copyright 2015-2017 Alexandr Evstigneev
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

package com.perl5.lang.perl.parser;

import com.intellij.openapi.util.TextRange;
import com.perl5.lang.perl.psi.properties.PerlIdentifierOwner;
import org.jetbrains.annotations.NotNull;

/**
 * Implement this interface to the named element, if you want to calculate your own name range;
 * For example: has '+something' => ...
 * Identifier is '+something' and manipulator knows nothing about Moose and returns TextRange(1,11)
 * But we need to check if + in the beginning and return TextRange(2,11)
 */
public interface PerlIdentifierRangeProvider extends PerlIdentifierOwner {
  @NotNull
  TextRange getRangeInIdentifier();
}
