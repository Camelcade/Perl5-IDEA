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
import com.intellij.psi.PsiElement;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.lang.perl.idea.refactoring.introduce.PerlIntroduceTarget;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import com.perl5.lang.perl.util.PerlArrayUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

class PerlListOccurrencesCollector extends PerlIntroduceTargetOccurrencesCollector {
  @NotNull
  private final List<PsiElement> myElementsToSearch;

  PerlListOccurrencesCollector(@NotNull PerlIntroduceTarget target) {
    super(target);
    List<PsiElement> targetChildren = PerlArrayUtil.collectListElements(getTargetElement());
    myElementsToSearch = Collections.unmodifiableList(
      ContainerUtil.filter(targetChildren, it -> getTarget().getTextRange().contains(it.getTextRange())));
    assert !myElementsToSearch.isEmpty() : "Empty elements for " + target;
  }

  @Override
  protected boolean collectOccurrences(@NotNull PsiElement element) {
    List<PsiElement> elementChildren = PerlArrayUtil.collectListElements(element);
    if (elementChildren.isEmpty()) {
      return false;
    }

    if (myElementsToSearch.size() > elementChildren.size()) {
      return false;
    }

    boolean found = false;
    PsiElement firstStringToSearch = myElementsToSearch.get(0);
    for (int startIndex = 0; startIndex <= elementChildren.size() - myElementsToSearch.size(); startIndex++) {
      PsiElement firstChildElement = elementChildren.get(startIndex);
      if (!PerlPsiUtil.areElementsSame(firstStringToSearch, firstChildElement)) {
        continue;
      }
      int offset = 1;
      for (; offset < myElementsToSearch.size(); offset++) {
        if (!PerlPsiUtil.areElementsSame(myElementsToSearch.get(offset), elementChildren.get(startIndex + offset))) {
          offset = -1;
          break;
        }
      }
      if (offset == myElementsToSearch.size()) {
        // matches
        addOccurrence(startIndex == 0 && offset == elementChildren.size() ?
                      PerlIntroduceTarget.create(element) :
                      PerlIntroduceTarget.create(element, TextRange.create(
                        firstChildElement.getTextRange().getStartOffset(),
                        elementChildren.get(startIndex + offset - 1).getTextRange().getEndOffset()
                      ).shiftLeft(element.getTextRange().getStartOffset())));
        startIndex += offset - 1;
        found = true;
      }
    }
    return found;
  }
}
