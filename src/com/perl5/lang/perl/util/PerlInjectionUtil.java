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

package com.perl5.lang.perl.util;

import com.intellij.lang.injection.InjectedLanguageManager;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Useful methods to work with injections
 */
public final class PerlInjectionUtil {
  private PerlInjectionUtil() {
  }

  /**
   * @return true if element is not null and injected with something
   */
  @Contract("null -> false")
  public static boolean hasInjections(@Nullable PsiElement element) {
    if (element == null) {
      return false;
    }
    List<Pair<PsiElement, TextRange>> injectedPsiFiles =
      InjectedLanguageManager.getInstance(element.getProject()).getInjectedPsiFiles(element);
    return injectedPsiFiles != null && !injectedPsiFiles.isEmpty();
  }
}
