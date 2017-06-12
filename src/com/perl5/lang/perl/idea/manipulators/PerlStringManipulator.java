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

package com.perl5.lang.perl.idea.manipulators;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import com.perl5.lang.perl.lexer.PerlLexer;
import com.perl5.lang.perl.parser.PerlParserUtil;
import com.perl5.lang.perl.psi.PerlString;
import com.perl5.lang.perl.psi.impl.PerlParsableStringWrapperlImpl;
import com.perl5.lang.perl.psi.mixins.PerlStringMixin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 27.02.2016.
 */
public class PerlStringManipulator extends PerlTextContainerManipulator<PerlStringMixin> {
  @Override
  public PerlStringMixin handleContentChange(@NotNull PerlStringMixin element, @NotNull TextRange range, String newContent)
    throws IncorrectOperationException {
    PsiElement openingQuote = getOpeningQuote(element);
    char closeQuote = PerlLexer.getQuoteCloseChar(openingQuote.getText().charAt(0));

    /*
    String currentContent = getNode().getText();

    String newNodeContent =
      currentContent.substring(0, getOpenQuoteOffsetInParent() + 1) +    // opening sequence
      newContent +                                                    // new content
      currentContent.substring(currentContent.length() - 1)            // close quote fixme handle incomplete strings
      ;

    replace(PerlElementFactory.createString(getProject(), newNodeContent));
    */

    return super.handleContentChange(element, range, newContent.replaceAll("(?<!\\\\)" + closeQuote, "\\\\" + closeQuote));
  }

  @NotNull
  @Override
  public TextRange getRangeInElement(@NotNull PerlStringMixin element) {
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

  @Nullable
  private PsiElement getClosingQuote(@NotNull PerlStringMixin element) {
    PsiElement lastChild = getRealString(element).getLastChild();
    if (lastChild != null && PerlParserUtil.CLOSE_QUOTES.contains(lastChild.getNode().getElementType())) {
      return lastChild;
    }
    return null;
  }

  @NotNull
  public PsiElement getOpeningQuote(@NotNull PerlStringMixin element) {
    PsiElement firstChild = getRealString(element).getFirstChild();

    while (firstChild != null) {
      if (PerlParserUtil.OPEN_QUOTES.contains(firstChild.getNode().getElementType())) {
        return firstChild;
      }
      firstChild = firstChild.getNextSibling();
    }
    throw new RuntimeException("Unable to find opening quote in: " + element.getText());
  }

  @NotNull
  protected PerlString getRealString(@NotNull PerlStringMixin element) {
    if (element.getFirstChild() instanceof PerlParsableStringWrapperlImpl) {
      PsiElement realString = element.getFirstChild().getFirstChild();
      assert realString instanceof PerlString : "Got " + realString + " instead of string";
      return (PerlString)realString;
    }
    return element;
  }

}
