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
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.perl.idea.refactoring.introduce.PerlIntroduceTarget;
import com.perl5.lang.perl.psi.PerlString;
import com.perl5.lang.perl.psi.PsiPerlStringBare;
import com.perl5.lang.perl.psi.impl.PsiPerlStringBareImpl;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.perl5.lang.perl.lexer.PerlTokenSets.STRING_CONTENT_TOKENSET;

/**
 * Compute target for quoted entities(perl strings) by caret position.
 */
class PerlStringTargetsCollector extends PerlTargetsCollector {
  public static final PerlTargetsCollector INSTANCE = new PerlStringTargetsCollector();
  private static final Logger LOG = Logger.getInstance(PerlStringTargetsCollector.class);

  private PerlStringTargetsCollector() {
  }

  @NotNull
  @Override
  protected List<PerlIntroduceTarget> computeTargetsAtCaret(@NotNull PsiElement element, int caretOffset) {
    if (!(element instanceof PerlString)) {
      LOG.error("Incorrect element passed to collector: " + element + " " + element.getClass());
      return Collections.emptyList();
    }

    if (element instanceof PsiPerlStringBareImpl) {
      return Collections.singletonList(PerlIntroduceTarget.create(element));
    }

    PerlString perlStringElement = (PerlString)element;
    List<PsiElement> allChildren = perlStringElement.getAllChildrenList();
    if (allChildren.isEmpty()) {
      return Collections.singletonList(PerlIntroduceTarget.create(element));
    }


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
            result.add(PerlIntroduceTarget.create(perlStringElement, firstStringElementStartOffsetInParent,
                                                  stringRun.getStartOffsetInParent() + stringRunText.length()));
          }
        }
        else {
          result.add(PerlIntroduceTarget.create(perlStringElement, allChildren.get(0), stringRun));
        }
      }
      stringRun = stringRun.getNextSibling();
    }
    result.add(PerlIntroduceTarget.create(element));
    return result;
  }

  @NotNull
  @Override
  protected List<PerlIntroduceTarget> computeTargetsFromSelection(@NotNull PsiElement element, @NotNull TextRange selectionRange) {
    if (!(element instanceof PerlString)) {
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

    int startOffset = -1;
    int endOffset = -1;
    for (PsiElement run : allChildrenList) {
      IElementType runElementType = PsiUtilCore.getElementType(run);
      TextRange runTextRange = run.getTextRange();
      if (runTextRange.getEndOffset() <= selectionStart || runTextRange.getStartOffset() >= selectionEnd) {
        continue;
      }

      if (startOffset < 0) {
        startOffset = run.getStartOffsetInParent();
        if (selectionStart > runTextRange.getStartOffset() && STRING_CONTENT_TOKENSET.contains(runElementType)) {
          startOffset += selectionStart - runTextRange.getStartOffset();
        }
      }
      endOffset = run.getStartOffsetInParent() + run.getTextLength();
      if (selectionEnd < runTextRange.getEndOffset() && STRING_CONTENT_TOKENSET.contains(runElementType)) {
        endOffset -= runTextRange.getEndOffset() - selectionEnd;
      }
    }
    return startOffset < 0 || endOffset < 0 ? Collections.emptyList() :
           Collections.singletonList(PerlIntroduceTarget.create(element, startOffset, endOffset));
  }
}
