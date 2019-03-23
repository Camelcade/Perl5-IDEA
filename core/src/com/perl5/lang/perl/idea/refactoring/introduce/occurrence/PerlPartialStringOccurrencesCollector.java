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
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.perl5.lang.perl.lexer.PerlTokenSets.STRING_CONTENT_TOKENSET;

public class PerlPartialStringOccurrencesCollector extends PerlTargetOccurrencesCollector {
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
    while (run != null) {
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

    PsiElement closeQuote = ((PerlString)element).getCloseQuoteElement();
    PsiElement run = ((PerlString)element).getFirstContentToken();

    if (run == null) {
      return false;
    }
    boolean result = false;
    Object firstChildToFind = myChildrenToFind.get(0);
    boolean startsWithString = firstChildToFind instanceof String;
    int firstChildLenght = startsWithString ? ((String)firstChildToFind).length() : -1;
    boolean isSimpleString = myChildrenToFind.size() == 1;
    int elementStartOffset = element.getTextRange().getStartOffset();

    while (run != null) {
      boolean isRunString = STRING_CONTENT_TOKENSET.contains(PsiUtilCore.getElementType(run));
      int startOffset = -1;

      if (startsWithString && isRunString) {
        String runText = run.getText();
        if (isSimpleString) {
          int i = 0;
          while (true) {
            i = runText.indexOf((String)firstChildToFind, i);
            if (i < 0) {
              break;
            }
            else {
              result = true;
              addOccurrence(PerlIntroduceTarget.create(element, TextRange.from(
                run.getTextRange().getStartOffset() - elementStartOffset, firstChildLenght)));
              i += firstChildLenght;
            }
          }
        }
        else {
          if (StringUtil.endsWith(runText, (String)firstChildToFind)) {
            startOffset = run.getTextRange().getEndOffset() - firstChildLenght;
          }
        }
      }
      else if (!startsWithString && !isRunString) {
        if (PerlPsiUtil.areElementsSame((PsiElement)firstChildToFind, run)) {
          startOffset = run.getTextRange().getStartOffset();
        }
      }

      if (startOffset > 0) {
        for (int i = 1; i < myChildrenToFind.size(); i++) {
          boolean isLast = i == myChildrenToFind.size() - 1;
        }
      }

      run = run.getNextSibling();
    }

    return result;
  }
}
