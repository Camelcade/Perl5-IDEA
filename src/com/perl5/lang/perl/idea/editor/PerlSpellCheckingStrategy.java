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

package com.perl5.lang.perl.idea.editor;

import com.intellij.psi.ElementManipulators;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.psi.impl.source.tree.injected.InjectedLanguageUtil;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.spellchecker.inspections.IdentifierSplitter;
import com.intellij.spellchecker.tokenizer.SpellcheckingStrategy;
import com.intellij.spellchecker.tokenizer.TokenConsumer;
import com.intellij.spellchecker.tokenizer.Tokenizer;
import com.perl5.lang.perl.psi.mixins.PerlStringBareMixin;
import com.perl5.lang.perl.psi.references.PerlTargetElementEvaluatorEx2;
import org.jetbrains.annotations.NotNull;

import static com.perl5.lang.perl.lexer.PerlTokenSets.STRING_CONTENT_TOKENSET;

public class PerlSpellCheckingStrategy extends SpellcheckingStrategy {
  private static final Tokenizer<PsiElement> IDENTIFIER_TOKENIZER = new Tokenizer<PsiElement>() {
    @Override
    public void tokenize(@NotNull PsiElement element, TokenConsumer consumer) {

      consumer.consumeToken(element,
                            ElementManipulators.getValueText(element),
                            true,
                            ElementManipulators.getOffsetInElement(element),
                            ElementManipulators.getValueTextRange(element),
                            IdentifierSplitter.getInstance());
    }
  };

  @NotNull
  @Override
  public Tokenizer getTokenizer(PsiElement element) {
    if (STRING_CONTENT_TOKENSET.contains(PsiUtilCore.getElementType(element))) {
      PsiElement lightNameIdentifierOwner = PerlTargetElementEvaluatorEx2.getLightNameIdentifierOwner(element);
      if (lightNameIdentifierOwner != null) {
        return IDENTIFIER_TOKENIZER;
      }
      ;

      if (element.getParent() instanceof PerlStringBareMixin) {
        return TEXT_TOKENIZER;
      }

      PsiLanguageInjectionHost injectionHost = PsiTreeUtil.getParentOfType(element, PsiLanguageInjectionHost.class);
      if (injectionHost != null && InjectedLanguageUtil.hasInjections(injectionHost)) {
        return EMPTY_TOKENIZER;
      }
      return TEXT_TOKENIZER;
    }

    return super.getTokenizer(element);
  }
}
