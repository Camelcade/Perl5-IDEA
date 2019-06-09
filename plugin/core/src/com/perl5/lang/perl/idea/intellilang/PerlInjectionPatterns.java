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

package com.perl5.lang.perl.idea.intellilang;

import com.intellij.patterns.InitialPatternCondition;
import com.intellij.psi.ElementManipulators;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.util.ProcessingContext;
import com.perl5.lang.perl.psi.PerlCompositeElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Pattern;

public class PerlInjectionPatterns {

  @NotNull
  public static PerlElementPatterns.Capture<? super PsiLanguageInjectionHost> perlString(@NotNull String regexp) {
    final Pattern pattern = Pattern.compile(regexp, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

    return new PerlElementPatterns.Capture<>(new InitialPatternCondition<PsiLanguageInjectionHost>(PsiLanguageInjectionHost.class) {
      @Override
      public boolean accepts(@Nullable Object o, ProcessingContext context) {
        if (!super.accepts(o, context) || !(o instanceof PerlCompositeElement)) {
          return false;
        }
        String valueText = ElementManipulators.getValueText((PsiElement)o);
        return pattern.matcher(valueText).find();
      }
    });
  }
}
