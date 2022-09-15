package com.perl5.lang.perl.intellilang;

import com.intellij.lang.injection.MultiHostInjector;
import com.intellij.lang.injection.MultiHostRegistrar;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.psi.impl.source.tree.PsiCommentImpl;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.perl.idea.intellilang.PerlInjectionMarkersService;
import com.perl5.lang.perl.psi.PerlAnnotationInject;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.COMMENT_BLOCK;
import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.TAG_DATA;

public final class PerlDataLanguageInjector implements MultiHostInjector {
  private static final List<? extends Class<? extends PsiElement>> INJECTABLES = Collections.singletonList(PsiCommentImpl.class);

  @Override
  public void getLanguagesToInject(@NotNull MultiHostRegistrar registrar, @NotNull PsiElement context) {
    if (PsiUtilCore.getElementType(context) != COMMENT_BLOCK) {
      return;
    }
    var prevSibling = context.getPrevSibling();
    if (PsiUtilCore.getElementType(prevSibling) != TAG_DATA) {
      return;
    }
    PerlAnnotationInject injectAnnotation = PerlPsiUtil.getAnyAnnotationByClass(prevSibling, PerlAnnotationInject.class);
    if (injectAnnotation == null) {
      return;
    }

    var languageMarker = injectAnnotation.getLanguageMarker();
    var injectedLanguage = PerlInjectionMarkersService.getInstance(prevSibling.getProject()).getLanguageByMarker(languageMarker);
    if (injectedLanguage == null) {
      return;
    }

    registrar.startInjecting(injectedLanguage);
    registrar.addPlace("", "", (PsiLanguageInjectionHost)context, TextRange.create(0, context.getTextLength()));
    registrar.doneInjecting();
  }

  @Override
  public @NotNull List<? extends Class<? extends PsiElement>> elementsToInjectIn() {
    return INJECTABLES;
  }
}
