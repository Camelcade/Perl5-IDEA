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
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.util.text.CharArrayUtil;
import com.perl5.lang.perl.lexer.PerlTokenSets;
import com.perl5.lang.perl.psi.PerlString;
import com.perl5.lang.perl.psi.impl.PerlStringContentElementImpl;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

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
      return isRegexReplacementContentReparseable(newText, parent, false);
    }
    if (parentType == TR_SEARCHLIST || parentType == TR_REPLACEMENTLIST) {
      return isRegexReplacementContentReparseable(newText, parent, true);
    }
    if (PerlTokenSets.HEREDOC_BODIES_TOKENSET.contains(parentType)) {
      return isHeredocContentsReparseable(newText, parent);
    }
    return false;
  }

  /**
   * fixme this logic may be reused later for {@link PerlHeredocElementType} for now we are missing node there, got only parent
   */
  private boolean isHeredocContentsReparseable(@NotNull CharSequence newText,
                                               @NotNull ASTNode heredocBodyNode) {
    ASTNode heredocTerminator = heredocBodyNode.getTreeNext();
    if (PsiUtilCore.getElementType(heredocTerminator) == TokenType.WHITE_SPACE) {
      heredocTerminator = heredocTerminator.getTreeNext();
    }
    IElementType terminatorType = PsiUtilCore.getElementType(heredocTerminator);
    if (!PerlTokenSets.HEREDOC_ENDS.contains(terminatorType)) {
      return false;
    }
    CharSequence terminatorChars = heredocTerminator.getChars();

    String forbiddenChars = PsiUtilCore.getElementType(heredocBodyNode) == HEREDOC ? null : "$@\\";

    if (terminatorType == HEREDOC_END) {
      // fixme we could use some smart one-pass scan here
      return !StringUtil.startsWith(newText, terminatorChars) &&
             !StringUtil.contains(newText, "\n" + terminatorChars) &&
             (forbiddenChars == null || !containsAnyChar(newText, forbiddenChars));
    }
    else if (terminatorType == HEREDOC_END_INDENTABLE) {
      // fixme we could use some smart one-pass scan here
      int startIndex = CharArrayUtil.shiftForward(newText, 0, " \t");
      CharSequence newTextToAnalyze = newText.subSequence(startIndex, newText.length());
      return !StringUtil.startsWith(newTextToAnalyze, terminatorChars) &&
             (forbiddenChars == null || !containsAnyChar(newText, forbiddenChars)) &&
             !Pattern.compile("\\n\\s*" + terminatorChars).matcher(newTextToAnalyze).find();
    }
    return false;
  }

  private boolean isRegexReplacementContentReparseable(@NotNull CharSequence newText,
                                                       @NotNull ASTNode regexReplacementNode,
                                                       boolean allowSigils) {
    ASTNode regexQuoteNode = regexReplacementNode.getTreePrev();
    IElementType quoteType = PsiUtilCore.getElementType(regexQuoteNode);
    if (quoteType != REGEX_QUOTE_OPEN && quoteType != REGEX_QUOTE) {
      return false;
    }
    return isGenericQuotedStringReparseable(newText, regexQuoteNode.getChars(), allowSigils);
  }

  private boolean isQuotedStringContentReparseable(@NotNull CharSequence newText, @NotNull ASTNode stringNode) {
    ASTNode openQuoteNode = stringNode.findChildByType(PerlTokenSets.OPEN_QUOTES);
    if (openQuoteNode == null) {
      return false;
    }
    return isGenericQuotedStringReparseable(
      newText, openQuoteNode.getChars(), PsiUtilCore.getElementType(stringNode) == STRING_SQ);
  }

  private boolean isGenericQuotedStringReparseable(@NotNull CharSequence newText,
                                                   @NotNull CharSequence openQuoteChars,
                                                   boolean allowSigils) {
    if (openQuoteChars.length() != 1) {
      return false;
    }
    char openQuote = openQuoteChars.charAt(0);
    char closeQuote = PerlString.getQuoteCloseChar(openQuote);
    // fixme this may be more type-dependent.
    String forbiddenChars = allowSigils ? "\\" + openQuote : "$@\\" + openQuote;
    if (openQuote != closeQuote) {
      forbiddenChars += closeQuote;
    }
    return !containsAnyChar(newText, forbiddenChars);
  }

  private boolean containsAnyChar(@NotNull CharSequence newText, String forbiddenChars) {
    for (int i = 0; i < newText.length(); i++) {
      if (forbiddenChars.indexOf(newText.charAt(i)) > -1) {
        return true;
      }
    }
    return false;
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
