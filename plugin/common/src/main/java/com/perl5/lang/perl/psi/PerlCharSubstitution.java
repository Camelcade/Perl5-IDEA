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

package com.perl5.lang.perl.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

public interface PerlCharSubstitution extends PsiElement {
  /**
   * @return string of characters represented by this substitution if can be represented by java or empty string.
   * @see Character#isIdentifierIgnorable(int)
   */
  @NotNull
  String getNonIgnorableChars();

  /**
   * @return char code point for this substitution or -1 if it's wrong
   */
  int getCodePoint();
}
