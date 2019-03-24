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

package com.perl5.lang.perl.idea.refactoring.introduce.occurrence;

import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.perl.idea.refactoring.introduce.PerlIntroduceTarget;
import com.perl5.lang.perl.psi.PerlString;
import com.perl5.lang.perl.psi.PsiPerlStringXq;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.perl5.lang.perl.lexer.PerlTokenSets.STRING_CONTENT_TOKENSET;

public class PerlPartialStringOccurrencesCollector extends PerlIntroduceTargetOccurrencesCollector {
  private final List<Object> myChildrenToFind = new ArrayList<>();

  public PerlPartialStringOccurrencesCollector(@NotNull PerlIntroduceTarget target) {
    super(target);
    PsiElement targetElement = target.getPlace();
    TextRange targetTextRange = target.getTextRange();
    if (!(targetElement instanceof PerlString) || target.isFullRange()) {
      throw new RuntimeException("Expected PerlString thing with partial range, got: " + targetElement.getClass() + "; " +
                                 targetElement.getTextRange() + "; " + targetTextRange);
    }

    PsiElement run = ((PerlString)targetElement).getFirstContentToken();
    PsiElement closeQuoteElement = ((PerlString)targetElement).getCloseQuoteElement();
    while (run != null && run != closeQuoteElement) {
      TextRange runTextRange = run.getTextRange().intersection(targetTextRange);
      if (runTextRange != null) {
        if (STRING_CONTENT_TOKENSET.contains(PsiUtilCore.getElementType(run))) {
          myChildrenToFind.add(
            runTextRange.shiftLeft(run.getTextRange().getStartOffset()).subSequence(run.getText()).toString());
        }
        else {
          myChildrenToFind.add(run);
        }
      }

      run = run.getNextSibling();
    }
  }

  @Override
  protected boolean collectOccurrences(@NotNull PsiElement element) {
    if (!(element instanceof PerlString) || myChildrenToFind.isEmpty()) {
      return false;
    }

    List<PsiElement> elementChildren = ((PerlString)element).getAllChildrenList();
    if (elementChildren.isEmpty()) {
      return false;
    }

    boolean result = false;
    Object firstChildToFind = myChildrenToFind.get(0);
    boolean startsWithString = firstChildToFind instanceof String;
    int firstChildLenght = startsWithString ? ((String)firstChildToFind).length() : -1;
    boolean isSimpleString = myChildrenToFind.size() == 1;
    int elementStartOffset = element.getTextRange().getStartOffset();

    for (int index = 0; index <= elementChildren.size() - myChildrenToFind.size(); index++) {
      PsiElement elementChild = elementChildren.get(index);

      boolean isRunString = STRING_CONTENT_TOKENSET.contains(PsiUtilCore.getElementType(elementChild));
      int startOffset = -1;

      if (startsWithString && isRunString) {
        String runText = elementChild.getText();
        if (isSimpleString) {
          int i = 0;
          while (true) {
            i = runText.indexOf((String)firstChildToFind, i);
            if (i < 0) {
              break;
            }
            else {
              result = true;
              addOccurrence(
                index == 0 && elementChildren.size() == 1 && i == 0 && firstChildLenght == runText.length() &&
                !(element instanceof PsiPerlStringXq) ?
                PerlIntroduceTarget.create(element) :
                PerlIntroduceTarget.create(element, TextRange.from(
                  elementChild.getTextRange().getStartOffset() - elementStartOffset + i, firstChildLenght)));
              i += firstChildLenght;
            }
          }
        }
        else {
          if (StringUtil.endsWith(runText, (String)firstChildToFind)) {
            startOffset = elementChild.getTextRange().getEndOffset() - firstChildLenght;
          }
        }
      }
      else if (!startsWithString && !isRunString) {
        if (PerlPsiUtil.areElementsSame((PsiElement)firstChildToFind, elementChild)) {
          startOffset = elementChild.getTextRange().getStartOffset();
        }
      }

      if (startOffset > 0) {
        int endOffset = -1;
        int elementNumber = 1;
        for (; elementNumber < myChildrenToFind.size(); elementNumber++) {
          boolean isLast = elementNumber == myChildrenToFind.size() - 1;
          Object childToFind = myChildrenToFind.get(elementNumber);
          int elementIndex = index + elementNumber;
          if (elementIndex >= elementChildren.size()) {
            endOffset = -1;
            break;
          }
          PsiElement elementToCompare = elementChildren.get(elementIndex);

          TextRange elementToCompareTextRange = elementToCompare.getTextRange();
          if (childToFind instanceof PsiElement) {
            if (!PerlPsiUtil.areElementsSame((PsiElement)childToFind, elementToCompare)) {
              endOffset = -1;
              break;
            }
            endOffset = elementToCompareTextRange.getEndOffset();
          }
          else if (childToFind instanceof String && STRING_CONTENT_TOKENSET.contains(PsiUtilCore.getElementType(elementToCompare))) {
            String textToCompare = elementToCompare.getText();
            if (!isLast) {
              if (!StringUtil.equals((String)childToFind, textToCompare)) {
                endOffset = -1;
                break;
              }
              endOffset = elementToCompareTextRange.getEndOffset();
            }
            else {
              if (StringUtil.startsWith(textToCompare, (String)childToFind)) {
                endOffset = elementToCompareTextRange.getStartOffset() + ((String)childToFind).length();
              }
            }
          }
          else {
            endOffset = -1;
            break;
          }
        }
        if (endOffset > startOffset) {
          addOccurrence(PerlIntroduceTarget.create(element, TextRange.create(startOffset, endOffset).shiftLeft(elementStartOffset)));
          index += elementNumber;
        }
      }
    }

    return result;
  }
}
