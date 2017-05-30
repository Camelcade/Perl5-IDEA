package com.perl5.lang.perl.idea.intellilang;

import com.intellij.lang.Language;
import com.intellij.lang.injection.MultiHostInjector;
import com.intellij.lang.injection.MultiHostRegistrar;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.perl5.lang.perl.psi.PerlHeredocTerminatorElement;
import com.perl5.lang.perl.psi.impl.PerlHeredocElementImpl;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * Created by hurricup on 30.05.2017.
 */
public class PerlHeredocLanguageInjector extends AbstractPerlLanguageInjector implements MultiHostInjector {
  private static final List<? extends Class<? extends PsiElement>> ELEMENTS_TO_INJECT =
    Collections.singletonList(PerlHeredocElementImpl.class);

  @Override
  public void getLanguagesToInject(@NotNull MultiHostRegistrar registrar, @NotNull PsiElement context) {
    assert context instanceof PerlHeredocElementImpl;
    PerlHeredocTerminatorElement terminator = ((PerlHeredocElementImpl)context).getTerminatorElement();

    if (terminator == null) {
      return;
    }

    String terminatorText = terminator.getText();
    Language mappedLanguage = LANGUAGE_MAP.get(terminatorText);

    if (mappedLanguage == null) {
      return;
    }

    registrar.startInjecting(mappedLanguage);
    registrar.addPlace(null, null, (PerlHeredocElementImpl)context, new TextRange(0, context.getTextLength()));
    registrar.doneInjecting();
  }

  @NotNull
  @Override
  public List<? extends Class<? extends PsiElement>> elementsToInjectIn() {
    return ELEMENTS_TO_INJECT;
  }
}
