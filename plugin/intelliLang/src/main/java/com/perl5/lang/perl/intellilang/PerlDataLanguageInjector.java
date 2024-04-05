/*
 * Copyright 2015-2022 Alexandr Evstigneev
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
import com.perl5.lang.perl.psi.utils.PerlAnnotations;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

import static com.perl5.lang.perl.parser.PerlElementTypesGenerated.COMMENT_BLOCK;
import static com.perl5.lang.perl.parser.PerlElementTypesGenerated.TAG_DATA;

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
    PerlAnnotationInject injectAnnotation = PerlAnnotations.getAnyAnnotationByClass(prevSibling, PerlAnnotationInject.class);
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
