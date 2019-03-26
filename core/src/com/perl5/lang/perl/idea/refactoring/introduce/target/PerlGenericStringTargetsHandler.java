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
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.lang.perl.idea.refactoring.introduce.PerlIntroduceTarget;
import com.perl5.lang.perl.lexer.PerlTokenSets;
import com.perl5.lang.perl.psi.PerlString;
import com.perl5.lang.perl.psi.PsiPerlStringBare;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static com.perl5.lang.perl.lexer.PerlTokenSets.STRING_CONTENT_TOKENSET;

/**
 * Compute target for quoted entities(perl strings).
 */
abstract class PerlGenericStringTargetsHandler extends PerlIntroduceTargetsHandler {
  private static final Logger LOG = Logger.getInstance(PerlGenericStringTargetsHandler.class);

  protected PerlGenericStringTargetsHandler() {
  }

  /**
   * @return children of the {@code element} if it's handled by this collector
   */
  @NotNull
  protected abstract List<PsiElement> getChildren(@NotNull PsiElement element);

  @Contract("null->false")
  protected abstract boolean isApplicable(@Nullable PsiElement element);

  @NotNull
  @Override
  protected List<PerlIntroduceTarget> computeTargetsAtCaret(@NotNull PsiElement element, int caretOffset) {
    if (!isApplicable(element)) {
      LOG.error("Incorrect element passed to collector: " + element + " " + element.getClass());
      return Collections.emptyList();
    }

    List<PsiElement> allChildren = getChildren(element);

    // fixme this is bad for here-doc, but should never be here. Anyway - refactor
    if (allChildren.isEmpty()) {
      return Collections.singletonList(PerlIntroduceTarget.create(element));
    }

    int lastOffsetInParent = Objects.requireNonNull(ContainerUtil.getLastItem(allChildren)).getTextRangeInParent().getEndOffset();
    List<PerlIntroduceTarget> result = new ArrayList<>();
    int firstStringElementStartOffsetInParent = allChildren.get(0).getStartOffsetInParent();

    for (PsiElement stringRun : allChildren) {
      IElementType stringRunElementType = PsiUtilCore.getElementType(stringRun);
      TextRange stringRunTextRange = stringRun.getTextRange();
      if (stringRunTextRange.contains(caretOffset) || stringRunTextRange.getStartOffset() > caretOffset) {
        if (STRING_CONTENT_TOKENSET.contains(stringRunElementType)) {
          String stringRunText = stringRun.getText();
          boolean isLastWhiteSpace = true;
          for (int i = 0; i < stringRunText.length(); i++) {
            boolean isCurrentWhiteSpace = Character.isWhitespace(stringRunText.charAt(i));
            int substringEndOffsetInParent = stringRun.getStartOffsetInParent() + i;
            if (isLastWhiteSpace != isCurrentWhiteSpace && isCurrentWhiteSpace &&
                substringEndOffsetInParent + stringRunTextRange.getStartOffset() > caretOffset) {
              result.add(PerlIntroduceTarget.create(element, firstStringElementStartOffsetInParent,
                                                    substringEndOffsetInParent));
            }
            isLastWhiteSpace = isCurrentWhiteSpace;
          }
          if (!isLastWhiteSpace) {
            int stringLastOffsetInParent = stringRun.getStartOffsetInParent() + stringRunText.length();
            if (stringLastOffsetInParent != lastOffsetInParent) {
              result.add(PerlIntroduceTarget.create(
                element, firstStringElementStartOffsetInParent, stringLastOffsetInParent));
            }
          }
        }
        else {
          result.add(PerlIntroduceTarget.create(element, allChildren.get(0), stringRun));
        }
      }
    }
    if (shouldAddElementAsTarget()) {
      result.add(PerlIntroduceTarget.create(element));
    }
    return result;
  }

  /**
   * @return true iff element itself should be added as a target at caret if partial ranges collected
   */
  protected abstract boolean shouldAddElementAsTarget();

  @NotNull
  @Override
  protected List<PerlIntroduceTarget> computeTargetsFromSelection(@NotNull PsiElement element, @NotNull TextRange selectionRange) {
    if (!isApplicable(element)) {
      LOG.error("Incorrect element passed to collector: " + element + " " + element.getClass());
      return Collections.emptyList();
    }

    @SuppressWarnings("Duplicates") List<PsiElement> allChildrenList = getChildren(element);
    if (allChildrenList.isEmpty()) {
      return Collections.singletonList(PerlIntroduceTarget.create(element));
    }

    int selectionStart = selectionRange.getStartOffset();
    int selectionEnd = selectionRange.getEndOffset();

    if (allChildrenList.get(0).getTextRange().getStartOffset() >= selectionStart &&
        allChildrenList.get(allChildrenList.size() - 1).getTextRange().getEndOffset() <= selectionEnd) {
      return Collections.singletonList(PerlIntroduceTarget.create(element));
    }

    int elementStartOffset = element.getTextRange().getStartOffset();
    int startOffset = -1;
    int endOffset = -1;
    for (PsiElement run : allChildrenList) {
      IElementType runElementType = PsiUtilCore.getElementType(run);
      TextRange runTextRange = run.getTextRange();
      if (runTextRange.getEndOffset() <= selectionStart || runTextRange.getStartOffset() >= selectionEnd) {
        continue;
      }

      if (startOffset < 0) {
        startOffset = runTextRange.getStartOffset();
        if (selectionStart > startOffset && STRING_CONTENT_TOKENSET.contains(runElementType)) {
          startOffset = selectionStart;
        }
      }
      endOffset = runTextRange.getEndOffset();
      if (selectionEnd < endOffset && STRING_CONTENT_TOKENSET.contains(runElementType)) {
        endOffset = selectionEnd;
      }
    }
    return startOffset < 0 || endOffset < 0 ? Collections.emptyList() :
           Collections.singletonList(PerlIntroduceTarget.create(element, startOffset - elementStartOffset, endOffset - elementStartOffset));
  }

  @NotNull
  @Override
  protected String createTargetExpressionText(@NotNull PerlIntroduceTarget target) {
    PsiElement targetPlace = target.getPlace();
    if (targetPlace instanceof PsiPerlStringBare) {
      return PerlPsiUtil.createSingleQuotedString(targetPlace.getText());
    }
    else if (target.isFullRange()) {
      return super.createTargetExpressionText(target);
    }
    assert targetPlace instanceof PerlString : "Got " + targetPlace;
    char openQuote = ((PerlString)targetPlace).getOpenQuote();
    if (openQuote == 0) {
      LOG.error("Unable to find an open quote in " + target);
      return reportEmptyPlace();
    }

    PsiElement firstChild = targetPlace.getFirstChild();
    String prefix = "";
    if (PerlTokenSets.SIMPLE_QUOTE_OPENERS.contains(PsiUtilCore.getElementType(firstChild))) {
      prefix = firstChild.getText() + " ";
    }

    return prefix + openQuote +
           target.getTextRangeInElement().subSequence(targetPlace.getNode().getChars()).toString() +
           PerlPsiUtil.getQuoteCloseChar(openQuote);
  }

  @NotNull
  PsiElement replaceWithInterpolation(@NotNull List<PerlIntroduceTarget> occurrences,
                                      @NotNull CharSequence replacementText,
                                      @NotNull PsiElement elementToReplace,
                                      @NotNull Set<TextRange> replacementRanges) {
    assert replacementText.length() > 1 : "Got " + replacementText;
    CharSequence safeReplacementText = braceVariableText(replacementText);

    CharSequence sourceText = elementToReplace.getNode().getChars();

    StringBuilder result = new StringBuilder();
    int lastOffset = 0;
    for (PerlIntroduceTarget occurrence : occurrences) {
      TextRange rangeInElement = occurrence.getTextRangeInElement();
      int rangeStartOffset = rangeInElement.getStartOffset();
      if (rangeStartOffset > lastOffset) {
        result.append(sourceText.subSequence(lastOffset, rangeStartOffset));
      }

      int replacementStartOffset = result.length();
      if (Character.isUnicodeIdentifierPart(sourceText.charAt(rangeInElement.getEndOffset()))) {
        result.append(safeReplacementText);
      }
      else {
        result.append(replacementText);
      }
      replacementRanges.add(TextRange.create(replacementStartOffset, result.length()));
      lastOffset = rangeInElement.getEndOffset();
    }
    if (lastOffset < sourceText.length()) {
      result.append(sourceText.subSequence(lastOffset, sourceText.length()));
    }

    PsiElement replacementElement = createReplacementFromText(elementToReplace, result.toString());
    return elementToReplace.replace(replacementElement);
  }

  @NotNull
  abstract PsiElement createReplacementFromText(@NotNull PsiElement originalElement, @NotNull String text);

  @NotNull
  static CharSequence braceVariableText(@NotNull CharSequence unbracedVariableText) {
    return unbracedVariableText.length() < 2 ? unbracedVariableText :
           unbracedVariableText.charAt(0) + "{" + unbracedVariableText.subSequence(1, unbracedVariableText.length()) + "}";
  }

  @NotNull
  static String quoteTextSafelyWithSingleQuotes(@NotNull CharSequence text,
                                                @NotNull String safePrefix,
                                                @NotNull String safeSuffix) {
    return !StringUtil.contains(text, "'") ? "'" + text + "'" : safePrefix + text + safeSuffix;
  }
}
