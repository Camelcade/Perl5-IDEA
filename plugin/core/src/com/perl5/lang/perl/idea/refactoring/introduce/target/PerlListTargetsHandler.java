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
import com.perl5.lang.perl.idea.refactoring.introduce.PerlIntroduceTarget;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import com.perl5.lang.perl.util.PerlArrayUtil;
import org.jetbrains.annotations.NotNull;

import java.util.*;

class PerlListTargetsHandler extends PerlSequentialElementTargetHandler {
  private static final Logger LOG = Logger.getInstance(PerlListTargetsHandler.class);
  static final PerlListTargetsHandler INSTANCE = new PerlListTargetsHandler();

  private PerlListTargetsHandler() {
  }

  @NotNull
  @Override
  protected String createTargetExpressionText(@NotNull PerlIntroduceTarget target) {
    String baseText = super.createTargetExpressionText(target);
    List<PsiElement> childrenInRange = target.getChildren();
    return childrenInRange.size() < 2 ? baseText : "(" + baseText + ")";
  }

  @NotNull
  @Override
  protected List<PsiElement> replaceNonTrivialTarget(@NotNull List<PerlIntroduceTarget> occurrences, @NotNull PsiElement replacement) {
    PerlIntroduceTarget baseTarget = Objects.requireNonNull(occurrences.get(0));
    PsiElement baseElement = Objects.requireNonNull(baseTarget.getPlace());
    List<PsiElement> sourceElements = PerlArrayUtil.collectListElements(baseElement);

    PsiElement occurrencePlace = occurrences.get(0).getPlace();
    if (occurrencePlace == null) {
      reportEmptyPlace();
      return Collections.emptyList();
    }

    boolean sameParentReplacement = true;
    List<PsiElement> resultElements = new ArrayList<>();
    Map<PerlIntroduceTarget, List<PsiElement>> replacementsMap = new HashMap<>();

    for (PerlIntroduceTarget occurrence : occurrences) {
      TextRange occurrenceTextRange = occurrence.getTextRange();
      PsiElement parent = null;
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
          PsiElement stringElementParent = stringElement.getParent();
          if (parent == null) {
            parent = stringElementParent;
          }
          sameParentReplacement = sameParentReplacement &&
                                  parent.equals(stringElementParent) &&
                                  !PerlPsiUtil.isInStringList(stringElement);
          replacementsMap.computeIfAbsent(occurrence, __ -> new ArrayList<>()).add(stringElement);
          iterator.remove();
          replaced = true;
        }
      }
      resultElements.add(replacement);
    }
    resultElements.addAll(sourceElements);

    if (!sameParentReplacement) {
      return replaceSequenceWithFlatter(baseElement, replacement, resultElements);
    }

    List<PsiElement> result = new ArrayList<>();
    for (PerlIntroduceTarget occurrence : occurrences) {
      List<PsiElement> childrenInRange = replacementsMap.get(occurrence);

      if (childrenInRange == null || childrenInRange.isEmpty()) {
        LOG.error("Unable to detect children to replace, please report developers with source sample");
        return Collections.emptyList();
      }
      PsiElement firstChildToReplace = childrenInRange.get(0);
      PsiElement localContainerElement = firstChildToReplace.getParent();
      result.add(localContainerElement.addBefore(replacement, firstChildToReplace));
      localContainerElement.deleteChildRange(firstChildToReplace, childrenInRange.get(childrenInRange.size() - 1));
    }
    return result;
  }
}
