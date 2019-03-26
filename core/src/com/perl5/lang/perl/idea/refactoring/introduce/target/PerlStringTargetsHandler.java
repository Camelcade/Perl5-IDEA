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
import com.intellij.psi.ElementManipulators;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.lang.perl.idea.refactoring.introduce.PerlIntroduceTarget;
import com.perl5.lang.perl.lexer.PerlBaseLexer;
import com.perl5.lang.perl.lexer.PerlLexer;
import com.perl5.lang.perl.lexer.PerlTokenSets;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.impl.PsiPerlStringBareImpl;
import com.perl5.lang.perl.psi.utils.PerlElementFactory;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.LP_STRING_QW;
import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.RESERVED_Q;
import static com.perl5.lang.perl.lexer.PerlTokenSets.STRING_CONTENT_TOKENSET;

/**
 * Compute target for quoted entities(perl strings) by caret position.
 */
class PerlStringTargetsHandler extends PerlIntroduceTargetsHandler {
  public static final PerlIntroduceTargetsHandler INSTANCE = new PerlStringTargetsHandler();
  private static final String CONCATENATION = " . ";
  private static final Logger LOG = Logger.getInstance(PerlStringTargetsHandler.class);

  private PerlStringTargetsHandler() {
  }

  @NotNull
  @Override
  protected List<PerlIntroduceTarget> computeTargetsAtCaret(@NotNull PsiElement element, int caretOffset) {
    if (!(element instanceof PerlString)) {
      LOG.error("Incorrect element passed to collector: " + element + " " + element.getClass());
      return Collections.emptyList();
    }

    if (element instanceof PsiPerlStringBareImpl) {
      return computeBareStringTarget(element);
    }

    PerlString perlStringElement = (PerlString)element;
    List<PsiElement> allChildren = perlStringElement.getAllChildrenList();
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
              result.add(PerlIntroduceTarget.create(perlStringElement, firstStringElementStartOffsetInParent,
                                                    substringEndOffsetInParent));
            }
            isLastWhiteSpace = isCurrentWhiteSpace;
          }
          if (!isLastWhiteSpace) {
            int stringLastOffsetInParent = stringRun.getStartOffsetInParent() + stringRunText.length();
            if (stringLastOffsetInParent != lastOffsetInParent) {
              result.add(PerlIntroduceTarget.create(
                perlStringElement, firstStringElementStartOffsetInParent, stringLastOffsetInParent));
            }
          }
        }
        else {
          result.add(PerlIntroduceTarget.create(perlStringElement, allChildren.get(0), stringRun));
        }
      }
    }
    result.add(PerlIntroduceTarget.create(element));
    return result;
  }

  @NotNull
  private List<PerlIntroduceTarget> computeBareStringTarget(@NotNull PsiElement element) {
    PsiElement elementParent = element.getParent();
    if (PsiUtilCore.getElementType(elementParent) == LP_STRING_QW) {
      elementParent = elementParent.getParent();
    }

    return elementParent instanceof PerlStringList ?
           Collections.singletonList(PerlIntroduceTarget.create(elementParent, element, element)) :
           Collections.singletonList(PerlIntroduceTarget.create(element));
  }

  @NotNull
  @Override
  protected List<PerlIntroduceTarget> computeTargetsFromSelection(@NotNull PsiElement element, @NotNull TextRange selectionRange) {
    if (!(element instanceof PerlString)) {
      LOG.error("Incorrect element passed to collector: " + element + " " + element.getClass());
      return Collections.emptyList();
    }

    if (element instanceof PsiPerlStringBare) {
      return computeBareStringTarget(element);
    }

    List<PsiElement> allChildrenList = ((PerlString)element).getAllChildrenList();
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
      return createBarewordQuotedText(targetPlace.getText());
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
           PerlLexer.getQuoteCloseChar(openQuote);
  }

  @NotNull
  @Override
  protected List<PsiElement> replaceTarget(@NotNull List<PerlIntroduceTarget> occurrences, @NotNull PsiElement replacement) {
    if (occurrences.size() == 1 && occurrences.get(0).isFullRange()) {
      return super.replaceTarget(occurrences, replacement);
    }

    CharSequence replacementChars = replacement.getNode().getChars();
    assert replacement instanceof PerlVariable : "Got " + replacement;

    PsiElement psiElement = Objects.requireNonNull(occurrences.get(0).getPlace());
    Set<TextRange> replacementRanges = new HashSet<>();

    PsiElement replacedString = psiElement instanceof PsiPerlStringSq ?
                                replaceWithConcatenation(occurrences, replacementChars, (PsiPerlStringSq)psiElement, replacementRanges) :
                                replaceWithInterpolation(occurrences, replacementChars, psiElement, replacementRanges);
    return ContainerUtil.filter(replacedString.getChildren(), it -> replacementRanges.contains(it.getTextRangeInParent()));
  }

  private PsiElement replaceWithInterpolation(@NotNull List<PerlIntroduceTarget> occurrences,
                                              @NotNull CharSequence replacementText,
                                              @NotNull PsiElement elementToReplace,
                                              @NotNull Set<TextRange> replacementRanges) {
    assert replacementText.length() > 1 : "Got " + replacementText;
    String safeReplacementText = replacementText.charAt(0) + "{" + replacementText.subSequence(1, replacementText.length()) + "}";

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

    PerlString newString = PerlElementFactory.createString(elementToReplace.getProject(), result.toString());
    return elementToReplace.replace(newString);
  }

  private PsiElement replaceWithConcatenation(@NotNull List<PerlIntroduceTarget> occurrences,
                                              @NotNull CharSequence replacementText,
                                              @NotNull PsiPerlStringSq elementToReplace,
                                              @NotNull Set<TextRange> replacementRanges) {
    TextRange valueTextRange = ElementManipulators.getValueTextRange(elementToReplace);
    CharSequence valueText = valueTextRange.subSequence(elementToReplace.getNode().getChars());
    int valueOffset = valueTextRange.getStartOffset();

    String prefix = "'";
    String suffix = "'";
    PsiElement firstChild = elementToReplace.getFirstChild();
    if (PsiUtilCore.getElementType(firstChild) == RESERVED_Q) {
      char openQuote = elementToReplace.getOpenQuote();
      if (openQuote == 0) {
        LOG.error("Can't find open quote in " + elementToReplace.getText());
        return elementToReplace;
      }
      prefix = "q " + openQuote;
      suffix = "" + PerlBaseLexer.getQuoteCloseChar(openQuote);
    }

    StringBuilder result = new StringBuilder();
    int positionInContent = 0;
    for (PerlIntroduceTarget occurrence : occurrences) {
      TextRange rangeInElement = occurrence.getTextRangeInElement();
      TextRange rangeInContent = rangeInElement.shiftLeft(valueOffset);

      if (result.length() > 0) {
        result.append(CONCATENATION);
      }

      int rangeStartOffset = rangeInContent.getStartOffset();
      if (rangeStartOffset > positionInContent) {
        result
          .append(quoteTextSafelyWithSingleQuotes(valueText.subSequence(positionInContent, rangeStartOffset), prefix, suffix))
          .append(CONCATENATION);
      }

      int replacementStartOffset = result.length();
      result.append(replacementText);
      replacementRanges.add(TextRange.create(replacementStartOffset, result.length()));
      positionInContent = rangeInContent.getEndOffset();
    }
    if (positionInContent < valueText.length()) {
      if (result.length() > 0) {
        result.append(CONCATENATION);
      }
      result.append(quoteTextSafelyWithSingleQuotes(valueText.subSequence(positionInContent, valueText.length()), prefix, suffix));
    }

    PsiElement statement = PerlElementFactory.createStatement(elementToReplace.getProject(), result.toString());
    if (statement == null) {
      LOG.error("Unable create a replacement statement from: " + elementToReplace.getText() + "\n text: " + result.toString());
      replacementRanges.clear();
      return elementToReplace;
    }
    return elementToReplace.replace(statement.getFirstChild());
  }

  @NotNull
  private String quoteTextSafelyWithSingleQuotes(@NotNull CharSequence text,
                                                 @NotNull String safePrefix,
                                                 @NotNull String safeSuffix
  ) {
    return !StringUtil.contains(text, "'") ? "'" + text + "'" : safePrefix + text + safeSuffix;
  }

  @NotNull
  static String createBarewordQuotedText(@NotNull String content) {
    return "'" + StringUtil.escapeChar(content, '\'') + "'";
  }
}
