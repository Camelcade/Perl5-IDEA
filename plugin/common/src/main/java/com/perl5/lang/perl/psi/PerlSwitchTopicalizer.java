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
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.psi.properties.PerlCompound;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

/**
 * Compound statement that may be a topicalizer for switch-like statements.
 *
 * @see <a href="https://perldoc.perl.org/perlsyn.html#Switch-Statements">Switch Statements</a>
 */
public interface PerlSwitchTopicalizer extends PerlCompound {

  /**
   * @return closest {@link PerlSwitchTopicalizer} wrapping the {@code psiElement} if any
   */
  @Contract("null->null")
  static @Nullable PerlSwitchTopicalizer wrapping(@Nullable PsiElement psiElement) {
    return PsiTreeUtil.getParentOfType(psiElement, PerlSwitchTopicalizer.class);
  }
}
