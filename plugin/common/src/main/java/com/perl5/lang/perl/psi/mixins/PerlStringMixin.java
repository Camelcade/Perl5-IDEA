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

package com.perl5.lang.perl.psi.mixins;

import com.intellij.lang.ASTNode;
import com.intellij.psi.ElementManipulators;
import com.intellij.psi.LiteralTextEscaper;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.perl.idea.intellilang.PerlStringLiteralEscaper;
import com.perl5.lang.perl.lexer.PerlTokenSets;
import com.perl5.lang.perl.psi.PsiPerlStringSq;
import com.perl5.lang.perl.psi.PsiPerlStringXq;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.perl5.lang.perl.parser.PerlElementTypesGenerated.COMMA_SEQUENCE_EXPR;
import static com.perl5.lang.perl.parser.PerlElementTypesGenerated.HASH_INDEX;


public abstract class PerlStringMixin extends PerlStringBareMixin implements PsiLanguageInjectionHost {
  public PerlStringMixin(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public PsiLanguageInjectionHost updateText(@NotNull String text) {
    return ElementManipulators.handleContentChange(this, text);
  }

  @Override
  public @NotNull LiteralTextEscaper<? extends PsiLanguageInjectionHost> createLiteralTextEscaper() {
    return new PerlStringLiteralEscaper(this);
  }

  @Override
  public boolean isValidHost() {
    PsiElement parent = getParent();
    IElementType parentType = PsiUtilCore.getElementType(parent);
    if (parentType == HASH_INDEX) {
      return false;
    }

    if (parentType == COMMA_SEQUENCE_EXPR) {
      IElementType grandParentType = PsiUtilCore.getElementType(parent.getParent());
      return grandParentType != HASH_INDEX;
    }

    return true;
  }

  @Override
  public @Nullable PsiElement getFirstContentToken() {
    PsiElement openQuote = getOpenQuoteElement();
    if (openQuote == null) {
      return null;
    }
    PsiElement nextSibling = openQuote.getNextSibling();
    if (nextSibling == null || nextSibling.equals(getCloseQuoteElement())) {
      return null;
    }
    return nextSibling;
  }

  // fixme see #1981
  @Override
  public @Nullable PsiElement getOpenQuoteElement() {
    PsiElement run = getFirstChild();

    while (run != null) {
      if (PerlTokenSets.OPEN_QUOTES.contains(PsiUtilCore.getElementType(run))) {
        return run;
      }
      run = run.getNextSibling();
    }
    return null;
  }

  // fixme see #1981
  @Override
  public @Nullable PsiElement getCloseQuoteElement() {
    PsiElement lastChild = getLastChild();
    return lastChild != null && PerlTokenSets.CLOSE_QUOTES.contains(PsiUtilCore.getElementType(lastChild)) ? lastChild : null;
  }

  /**
   * @return true iff content of the string supports only escapes of quotes and escape symbol
   */
  public boolean isRestricted() {
    return this instanceof PsiPerlStringSq || this instanceof PsiPerlStringXq && getOpenQuote() == '\'';
  }
}
