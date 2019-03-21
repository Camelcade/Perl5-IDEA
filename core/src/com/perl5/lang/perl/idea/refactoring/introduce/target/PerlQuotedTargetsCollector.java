/*
 * Copyright 2015-2018 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.refactoring.introduce.target;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.perl.PerlParserDefinition;
import com.perl5.lang.perl.idea.refactoring.introduce.PerlIntroduceTarget;
import com.perl5.lang.perl.psi.PerlQuoted;
import com.perl5.lang.perl.psi.PsiPerlStringBare;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Compute target for quoted entities(perl strings) by caret position.
 */
class PerlQuotedTargetsCollector extends PerlTargetsCollector {
  public static final PerlTargetsCollector INSTANCE = new PerlQuotedTargetsCollector();
  private static final Logger LOG = Logger.getInstance(PerlQuotedTargetsCollector.class);

  private PerlQuotedTargetsCollector() {
  }

  @NotNull
  @Override
  protected List<PerlIntroduceTarget> computeTargetsAtCaret(@NotNull PsiElement element, int caretOffset) {
    if (!(element instanceof PerlQuoted)) {
      LOG.error("Incorrect element passed to collector: " + element + " " + element.getClass());
      return Collections.emptyList();
    }
    PerlQuoted perlQuotedElement = (PerlQuoted)element;

    PsiElement stringRun = perlQuotedElement.getOpenQuoteElement();
    if (stringRun == null) {
      return Collections.singletonList(PerlIntroduceTarget.create(element));
    }

    List<PerlIntroduceTarget> result = new ArrayList<>();

    PsiElement closeQuote = perlQuotedElement.getCloseQuoteElement();
    PsiElement firstStringElement = stringRun.getNextSibling();
    while ((stringRun = stringRun.getNextSibling()) != null && !stringRun.equals(closeQuote)) {
      IElementType stringRunElementType = PsiUtilCore.getElementType(stringRun);
      if (stringRunElementType == TokenType.WHITE_SPACE) {
        continue;
      }
      TextRange stringRunTextRange = stringRun.getTextRange();
      if (stringRunTextRange.contains(caretOffset) || stringRunTextRange.getStartOffset() > caretOffset) {
        if (PerlParserDefinition.LITERALS.contains(stringRunElementType)) {
          String stringRunText = stringRun.getText();
          boolean isLastWhiteSpace = true;
          for (int i = 0; i < stringRunText.length(); i++) {
            boolean isCurrentWhiteSpace = Character.isWhitespace(stringRunText.charAt(i));
            int substringEndOffsetInParent = stringRun.getStartOffsetInParent() + i;
            if (isLastWhiteSpace != isCurrentWhiteSpace && isCurrentWhiteSpace &&
                substringEndOffsetInParent + stringRunTextRange.getStartOffset() > caretOffset) {
              result.add(PerlIntroduceTarget.create(perlQuotedElement, firstStringElement.getStartOffsetInParent(),
                                                    substringEndOffsetInParent));
            }
            isLastWhiteSpace = isCurrentWhiteSpace;
          }
          if (!isLastWhiteSpace) {
            result.add(PerlIntroduceTarget.create(perlQuotedElement, firstStringElement.getStartOffsetInParent(),
                                                  stringRun.getStartOffsetInParent() + stringRunText.length()));
          }
        }
        else {
          result.add(PerlIntroduceTarget.create(perlQuotedElement, firstStringElement, stringRun));
        }
      }
    }
    result.add(PerlIntroduceTarget.create(element));
    return result;
  }

  @NotNull
  @Override
  protected List<PerlIntroduceTarget> computeTargetsFromSelection(@NotNull PsiElement element, @NotNull TextRange selectionRange) {
    if (!(element instanceof PerlQuoted)) {
      LOG.error("Incorrect element passed to collector: " + element + " " + element.getClass());
      return Collections.emptyList();
    }

    if (element instanceof PsiPerlStringBare) {
      TextRange intersectedRange = selectionRange.intersection(element.getTextRange());
      if (intersectedRange == null) {
        return Collections.emptyList();
      }
      return Collections.singletonList(PerlIntroduceTarget.create(element, intersectedRange.shiftLeft(element.getTextOffset())));
    }

    int selectionStart = selectionRange.getStartOffset();
    int selectionEnd = selectionRange.getEndOffset();
    PsiElement run = ((PerlQuoted)element).getOpenQuoteElement();
    if (run == null) {
      return Collections.emptyList();
    }
    PsiElement closeQuote = ((PerlQuoted)element).getCloseQuoteElement();
    if (selectionRange.contains(run.getTextRange()) && (closeQuote == null || selectionRange.contains(closeQuote.getTextRange()))) {
      return Collections.singletonList(PerlIntroduceTarget.create(element));
    }

    int startOffset = -1;
    int endOffset = -1;
    while ((run = run.getNextSibling()) != null) {
      if (run.equals(closeQuote)) {
        break;
      }
      IElementType runElementType = PsiUtilCore.getElementType(run);
      if (runElementType == TokenType.WHITE_SPACE) {
        continue;
      }
      TextRange runTextRange = run.getTextRange();
      if (runTextRange.getEndOffset() <= selectionStart || runTextRange.getStartOffset() >= selectionEnd) {
        continue;
      }

      if (startOffset < 0) {
        startOffset = run.getStartOffsetInParent();
        if (selectionStart > runTextRange.getStartOffset() && PerlParserDefinition.LITERALS.contains(runElementType)) {
          startOffset += selectionStart - runTextRange.getStartOffset();
        }
      }
      endOffset = run.getStartOffsetInParent() + run.getTextLength();
      if (selectionEnd < runTextRange.getEndOffset() && PerlParserDefinition.LITERALS.contains(runElementType)) {
        endOffset -= runTextRange.getEndOffset() - selectionEnd;
      }
    }
    return startOffset < 0 || endOffset < 0 ? Collections.emptyList() :
           Collections.singletonList(PerlIntroduceTarget.create(element, startOffset, endOffset));
  }
}
