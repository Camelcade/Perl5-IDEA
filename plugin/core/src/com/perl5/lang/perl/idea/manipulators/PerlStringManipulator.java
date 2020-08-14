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

package com.perl5.lang.perl.idea.manipulators;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.AbstractElementManipulator;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import com.perl5.lang.perl.psi.PerlString;
import com.perl5.lang.perl.psi.PsiPerlStatement;
import com.perl5.lang.perl.psi.impl.PerlFileImpl;
import com.perl5.lang.perl.psi.mixins.PerlStringMixin;
import com.perl5.lang.perl.psi.utils.PerlElementFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;


public class PerlStringManipulator extends AbstractElementManipulator<PerlStringMixin> {
  private static final Logger LOG = Logger.getInstance(PerlStringManipulator.class);

  @Override
  public PerlStringMixin handleContentChange(@NotNull PerlStringMixin element, @NotNull TextRange range, String decodedContent)
    throws IncorrectOperationException {
    char openQuote = element.getOpenQuote();
    char closeQuote = PerlString.getQuoteCloseChar(openQuote);

    String encodedContent;
    if (element.isRestricted()) {
      String charsToEscape = "" + openQuote + (openQuote != closeQuote ? closeQuote : "");
      encodedContent = encodeSingleQuotedString(decodedContent, charsToEscape);
    }
    else {
      String charsToEscape = "$@\\" + openQuote + (openQuote != closeQuote ? closeQuote : "");
      encodedContent = encodeDoubleQuotedString(decodedContent, charsToEscape, true);
    }

    CharSequence currentElementChars = element.getNode().getChars();
    String newElementText = range.replace(currentElementChars.toString(), encodedContent);
    PerlFileImpl file = PerlElementFactory.createFile(element.getProject(), newElementText);
    PsiElement firstChild = file.getFirstChild();
    if (!(firstChild instanceof PsiPerlStatement)) {
      LOG.error("Statement expected, got: " + firstChild + "; text: '" + newElementText + "'");
      return element;
    }
    PsiElement newStringElement = firstChild.getFirstChild();
    if (!(newStringElement instanceof PerlStringMixin)) {
      LOG.error("String expected, got: " + newStringElement + "; text: '" + newElementText + "'");
      return element;
    }
    return (PerlStringMixin)element.replace(newStringElement);
  }

  public static String encodeSingleQuotedString(@NotNull String decodedContent, @NotNull String charsToEscape) {
    StringBuilder result = new StringBuilder();

    for (int i = 0; i < decodedContent.length(); i++) {
      char currentChar = decodedContent.charAt(i);
      if (currentChar == '\\') {
        var nextChar = i < decodedContent.length() - 1 ? decodedContent.charAt(i + 1) : -1;
        if (nextChar < 0 || nextChar == '\\' || charsToEscape.indexOf(nextChar) >= 0) {
          result.append('\\');
        }
      }
      else if (!charsToEscape.isEmpty() && charsToEscape.indexOf(currentChar) >= 0) {
        result.append('\\');
      }
      result.append(currentChar);
    }

    return result.toString();
  }

  /**
   * fixme we should probably analyze string for substitutions and use the same as used. Now we always using the hex ones
   */
  public static String encodeDoubleQuotedString(@NotNull String decodedContent, @NotNull String charsToEscape, boolean collapseTabs) {
    StringBuilder result = new StringBuilder();

    for (int i = 0; i < decodedContent.length(); i++) {
      char currentChar = decodedContent.charAt(i);
      if (collapseTabs && currentChar == '\n') {
        result.append("\\n");
        continue;
      }
      else if (collapseTabs && currentChar == '\t') {
        result.append("\\t");
        continue;
      }
      else if (collapseTabs && currentChar == '\r') {
        result.append("\\r");
        continue;
      }
      else if (collapseTabs && currentChar == '\f') {
        result.append("\\f");
        continue;
      }
      else if (collapseTabs && currentChar == '\b') {
        result.append("\\b");
        continue;
      }
      else if (collapseTabs && currentChar == (char)11) {
        result.append("\\a");
        continue;
      }
      else if (collapseTabs && currentChar == (char)27) {
        result.append("\\e");
        continue;
      }
      else if (!charsToEscape.isEmpty() && charsToEscape.indexOf(currentChar) >= 0) {
        result.append('\\');
      }
      else if (!plainAllowedChar(currentChar)) {
        int codePoint = Character.codePointAt(decodedContent, i);
        result.append("\\x{").append(Integer.toHexString(codePoint).toUpperCase()).append("}");
        i += Character.charCount(codePoint) - 1;
        continue;
      }
      result.append(currentChar);
    }

    return result.toString();
  }

  private static boolean plainAllowedChar(char c) {
    if (c == ' ' || Character.isLetterOrDigit(c)) {
      return true;
    }
    var type = Character.getType(c);
    return type >= Character.DASH_PUNCTUATION && type <= Character.CURRENCY_SYMBOL ||
           type == Character.INITIAL_QUOTE_PUNCTUATION || type == Character.FINAL_QUOTE_PUNCTUATION;
  }

  @Override
  public @NotNull TextRange getRangeInElement(@NotNull PerlStringMixin element) {
    return new TextRange(getOpenQuoteOffsetInParent(element) + 1, getCloseQuoteOffsetInParent(element));
  }

  protected int getCloseQuoteOffsetInParent(@NotNull PerlStringMixin element) {
    PsiElement closingQuote = getClosingQuote(element);
    ASTNode node = element.getNode();
    if (closingQuote == null) // unclosed string
    {
      return node.getTextLength();
    }
    return closingQuote.getNode().getStartOffset() - node.getStartOffset();
  }

  private int getOpenQuoteOffsetInParent(@NotNull PerlStringMixin element) {
    return getOpeningQuote(element).getNode().getStartOffset() - element.getNode().getStartOffset();
  }

  private @Nullable PsiElement getClosingQuote(@NotNull PerlStringMixin element) {
    return element.getCloseQuoteElement();
  }

  public @NotNull PsiElement getOpeningQuote(@NotNull PerlStringMixin element) {
    return Objects.requireNonNull(element.getOpenQuoteElement(), element.getText());
  }
}
