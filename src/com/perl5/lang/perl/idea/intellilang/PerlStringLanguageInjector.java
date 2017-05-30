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
