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

package com.perl5.lang.perl.idea.intellilang;

import com.intellij.lang.Language;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.InjectedLanguagePlaces;
import com.intellij.psi.LanguageInjector;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.perl5.lang.perl.psi.PerlAnnotationInject;
import com.perl5.lang.perl.psi.PerlString;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 30.05.2017.
 */
public class PerlStringLanguageInjector extends AbstractPerlLanguageInjector implements LanguageInjector {
  @Override
  public void getLanguagesToInject(@NotNull PsiLanguageInjectionHost host, @NotNull InjectedLanguagePlaces injectionPlacesRegistrar) {
    if (host instanceof PerlString && host.isValidHost()) {
      // before element
      PerlAnnotationInject injectAnnotation = PerlPsiUtil.getAnyAnnotationByClass(host, PerlAnnotationInject.class);

      if (injectAnnotation != null) {
        String languageMarker = injectAnnotation.getLanguageMarker();
        if (languageMarker != null) {
          Language targetLanguage = LANGUAGE_MAP.get(languageMarker);
          if (targetLanguage != null) {
            TextRange contentRange = ((PerlString)host).getContentTextRangeInParent();
            if (!contentRange.isEmpty()) {
              injectionPlacesRegistrar.addPlace(targetLanguage, contentRange, null, null);
            }
          }
        }
      }
    }
  }
}
