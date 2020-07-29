/*
 * Copyright 2015-2020 Alexandr Evstigneev
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

package com.perl5.lang.perl.parser.elementTypes;

import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.perl.lexer.PerlTokenSets;
import com.perl5.lang.perl.psi.PerlString;
import com.perl5.lang.perl.psi.impl.PerlStringContentElementImpl;
import org.jetbrains.annotations.NotNull;

import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.*;

public class PerlStringContentTokenType extends PerlReparseableTokenType {
  public PerlStringContentTokenType(@NotNull String debugName) {
    super(debugName, PerlStringContentElementImpl.class);
  }

  @Override
  protected boolean isReparseable(@NotNull ASTNode leaf, @NotNull CharSequence newText) {
    if (newText.length() == 0) {
      return false;
    }
    ASTNode parent = leaf.getTreeParent();
    if (parent == null) {
      return false;
    }

    if (PsiUtilCore.getElementType(parent.getTreeParent()) == HEREDOC_OPENER) {
      return false;
    }

    IElementType parentType = PsiUtilCore.getElementType(parent);
    if (parentType == STRING_BARE) {
      return isBareStringContentReparseable(newText);
    }
    if (PerlTokenSets.QUOTED_STRINGS.contains(parentType)) {
      return isQuotedStringContentReparseable(newText, parent);
    }
    if (parentType == REGEX_REPLACEMENT) {
      return isRegexReplacementContentReparseable(newText, parent);
    }
    // fixme heredocs/tr?
    return false;
  }

  private boolean isRegexReplacementContentReparseable(@NotNull CharSequence newText, @NotNull ASTNode regexReplacementNode) {
    ASTNode regexQuoteNode = regexReplacementNode.getTreePrev();
    IElementType quoteType = PsiUtilCore.getElementType(regexQuoteNode);
    if (quoteType != REGEX_QUOTE_OPEN && quoteType != REGEX_QUOTE) {
      return false;
    }
    return isGenericQuotedStringReparseable(newText, regexReplacementNode, regexQuoteNode.getChars());
  }

  private boolean isQuotedStringContentReparseable(@NotNull CharSequence newText, @NotNull ASTNode stringNode) {
    ASTNode openQuoteNode = stringNode.findChildByType(PerlTokenSets.OPEN_QUOTES);
    if (openQuoteNode == null) {
      return false;
    }
    return isGenericQuotedStringReparseable(newText, stringNode, openQuoteNode.getChars());
  }

  private boolean isGenericQuotedStringReparseable(@NotNull CharSequence newText,
                                                   @NotNull ASTNode genericQuotedStringNode,
                                                   @NotNull CharSequence openQuoteChars) {
    if (openQuoteChars.length() != 1) {
      return false;
    }
    char openQuote = openQuoteChars.charAt(0);
    char closeQuote = PerlString.getQuoteCloseChar(openQuote);
    // fixme this may be more type-dependent.
    String forbiddenChars = PsiUtilCore.getElementType(genericQuotedStringNode) == STRING_SQ ? "\\" + openQuote : "$@\\" + openQuote;
    if (openQuote != closeQuote) {
      forbiddenChars += closeQuote;
    }
    for (int i = 0; i < newText.length(); i++) {
      if (forbiddenChars.indexOf(newText.charAt(i)) > -1) {
        return false;
      }
    }
    return true;
  }

  private boolean isBareStringContentReparseable(@NotNull CharSequence newText) {
    if (!Character.isUnicodeIdentifierStart(newText.charAt(0))) {
      return false;
    }

    for (int i = 1; i < newText.length(); i++) {
      char currentChar = newText.charAt(i);
      if (!Character.isUnicodeIdentifierPart(currentChar)) {
        return false;
      }
    }

    return true;
  }
}
