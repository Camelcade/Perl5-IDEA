/*
 * Copyright 2015-2024 Alexandr Evstigneev
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
import com.intellij.psi.ElementManipulators;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.lang.perl.idea.refactoring.introduce.PerlIntroduceTarget;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.impl.PsiPerlStringBareImpl;
import com.perl5.lang.perl.psi.utils.PerlElementFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static com.perl5.lang.perl.parser.PerlElementTypesGenerated.RESERVED_Q;

class PerlStringsTargetsHandler extends PerlGenericStringTargetsHandler {
  static final PerlStringsTargetsHandler INSTANCE = new PerlStringsTargetsHandler();
  private static final String CONCATENATION = " . ";
  private static final Logger LOG = Logger.getInstance(PerlStringsTargetsHandler.class);

  private PerlStringsTargetsHandler() {
  }

  @Override
  protected boolean shouldAddElementAsTarget() {
    return true;
  }

  @Override
  protected @NotNull List<PsiElement> replaceTarget(@NotNull List<PerlIntroduceTarget> occurrences, @NotNull PsiElement replacement) {
    if (occurrences.size() == 1 && occurrences.getFirst().isFullRange()) {
      return super.replaceTarget(occurrences, replacement);
    }

    CharSequence replacementChars = replacement.getNode().getChars();
    assert replacement instanceof PerlVariable : "Got " + replacement;

    PsiElement psiElement = Objects.requireNonNull(occurrences.getFirst().getPlace());
    Set<TextRange> replacementRanges = new HashSet<>();

    PsiElement replacedString = psiElement instanceof PsiPerlStringSq ?
                                replaceWithConcatenation(occurrences, replacementChars, (PsiPerlStringSq)psiElement, replacementRanges) :
                                replaceWithInterpolation(occurrences, replacementChars, psiElement, replacementRanges);
    return ContainerUtil.filter(replacedString.getChildren(), it -> replacementRanges.contains(it.getTextRangeInParent()));
  }

  private @NotNull PsiElement replaceWithConcatenation(@NotNull List<PerlIntroduceTarget> occurrences,
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
      suffix = String.valueOf(PerlString.getQuoteCloseChar(openQuote));
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

  @Override
  protected @NotNull List<PsiElement> getChildren(@NotNull PsiElement element) {
    return ((PerlString)element).getAllChildrenList();
  }

  @Override
  protected boolean isApplicable(@Nullable PsiElement element) {
    return element instanceof PerlString && !(element.getParent() instanceof PerlHeredocOpener);
  }

  @Override
  protected @NotNull List<PerlIntroduceTarget> computeTargetsAtCaret(@NotNull PsiElement element, int caretOffset) {
    if (element instanceof PsiPerlStringBareImpl) {
      return computeBareStringTarget(element);
    }
    return super.computeTargetsAtCaret(element, caretOffset);
  }

  @Override
  protected @NotNull List<PerlIntroduceTarget> computeTargetsFromSelection(@NotNull PsiElement element, @NotNull TextRange selectionRange) {
    if (element.getParent() instanceof PerlHeredocOpener) {
      return Collections.emptyList();
    }

    if (element instanceof PsiPerlStringBare) {
      return computeBareStringTarget(element);
    }

    return super.computeTargetsFromSelection(element, selectionRange);
  }

  private @NotNull List<PerlIntroduceTarget> computeBareStringTarget(@NotNull PsiElement element) {
    PsiElement elementParent = element.getParent();
    return elementParent instanceof PerlStringList ?
           Collections.singletonList(PerlIntroduceTarget.create(elementParent, element, element)) :
           Collections.singletonList(PerlIntroduceTarget.create(element));
  }

  @NotNull
  @Override
  PsiElement createReplacementFromText(@NotNull PsiElement originalElement, @NotNull String text) {
    return PerlElementFactory.createString(originalElement.getProject(), text);
  }
}
