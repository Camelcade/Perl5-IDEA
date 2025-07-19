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

package com.perl5.lang.perl.util;

import com.intellij.psi.ElementManipulators;
import com.intellij.psi.PsiElement;
import com.perl5.lang.perl.psi.PerlString;
import com.perl5.lang.perl.psi.PerlStringContentElement;
import org.jetbrains.annotations.Nullable;

public final class PerlScalarUtilCore {
  public static final String DEFAULT_SELF_NAME = "self";
  public static final String DEFAULT_SELF_SCALAR_NAME = "$" + DEFAULT_SELF_NAME;

  private PerlScalarUtilCore() {
  }

  /**
   * Extracts value from the string element
   *
   * @param string psi element that may be StringElement or stringcontentElement
   * @return string content or null
   */
  public static @Nullable String getStringContent(@Nullable PsiElement string) {
    return string instanceof PerlString || string instanceof PerlStringContentElement ? ElementManipulators.getValueText(string) : null;
  }
}
