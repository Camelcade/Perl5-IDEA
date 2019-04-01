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
import com.perl5.lang.perl.idea.refactoring.introduce.PerlIntroduceTarget;
import com.perl5.lang.perl.psi.PerlString;
import com.perl5.lang.perl.psi.PerlStringList;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import com.perl5.lang.perl.psi.utils.PerlVariableType;
import com.perl5.lang.perl.util.PerlArrayUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

class PerlStringListTargetsHandler extends PerlSequentialElementTargetHandler {
  static final PerlStringListTargetsHandler INSTANCE = new PerlStringListTargetsHandler();
  private static final Logger LOG = Logger.getInstance(PerlStringListTargetsHandler.class);

  private PerlStringListTargetsHandler() {
  }

  @NotNull
  @Override
  protected PerlVariableType doComputeVariableType(@NotNull PerlIntroduceTarget target) {
    return target.getChildren().size() > 1 ? PerlVariableType.ARRAY : PerlVariableType.SCALAR;
  }

  @NotNull
  @Override
  protected String createTargetExpressionText(@NotNull PerlIntroduceTarget target) {
    if (target.isFullRange()) {
      return super.createTargetExpressionText(target);
    }
    String targetElementsText = target.getChildren().stream().map(PsiElement::getText).collect(Collectors.joining(" "));
    PsiElement targetElement = target.getPlace();

    assert targetElement instanceof PerlStringList : "Got " + target;
    PsiElement openQuoteElement = ((PerlStringList)targetElement).getOpenQuoteElement();
    PsiElement closeQuoteElement = ((PerlStringList)targetElement).getCloseQuoteElement();
    if (openQuoteElement == null) {
      LOG.error("Unable to find close quote in: " + targetElement.getText());
      return "'INTERNAL ERROR: Unable to find close quote for list'";
    }
    String openQuoteText = openQuoteElement.getText();
    String closeQuoteText = closeQuoteElement != null ? closeQuoteElement.getText() :
                            "" + PerlString.getQuoteCloseChar(openQuoteText.charAt(0));
    return StringUtil.containsWhitespaces(targetElementsText) ?
           "qw " + openQuoteText + targetElementsText + closeQuoteText :
           PerlPsiUtil.createSingleQuotedString(targetElementsText);
  }

  @NotNull
  @Override
  protected List<PsiElement> replaceNonTrivialTarget(@NotNull List<PerlIntroduceTarget> occurrences, @NotNull PsiElement replacement) {
    PerlIntroduceTarget baseTarget = Objects.requireNonNull(occurrences.get(0));
    PsiElement baseElement = Objects.requireNonNull(baseTarget.getPlace());
    List<PsiElement> sourceElements = PerlArrayUtil.collectListElements(baseElement);

    List<PsiElement> resultElements = new ArrayList<>();

    for (PerlIntroduceTarget occurrence : occurrences) {
      TextRange occurrenceTextRange = occurrence.getTextRange();
      boolean replaced = false;
      for (Iterator<PsiElement> iterator = sourceElements.iterator(); iterator.hasNext(); ) {
        PsiElement stringElement = iterator.next();
        if (!occurrenceTextRange.contains(stringElement.getTextRange())) {
          if (replaced) {
            break;
          }
          iterator.remove();
          resultElements.add(stringElement);
        }
        else {
          iterator.remove();
          replaced = true;
        }
      }
      resultElements.add(replacement);
    }
    resultElements.addAll(sourceElements);

    return replaceSequenceWithFlatter(baseElement, replacement, resultElements);
  }

}
