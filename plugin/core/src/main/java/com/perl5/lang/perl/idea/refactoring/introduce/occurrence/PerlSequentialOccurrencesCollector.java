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

import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.perl.idea.refactoring.introduce.PerlIntroduceTarget;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;

class PerlSequentialOccurrencesCollector extends PerlIntroduceTargetOccurrencesCollector {
  PerlSequentialOccurrencesCollector(@NotNull PerlIntroduceTarget target) {
    super(target);
  }

  @Override
  protected boolean collectOccurrences(@NotNull PsiElement element) {
    PerlIntroduceTarget target = getTarget();
    PsiElement targetElement = target.getPlace();
    if (target.isFullRange() && PerlPsiUtil.areElementsSame(targetElement, element)) {
      addOccurrence(PerlIntroduceTarget.create(element));
      return true;
    }
    if (PsiUtilCore.getElementType(targetElement) != PsiUtilCore.getElementType(element)) {
      return false;
    }

    List<PsiElement> targetChildren = target.getMeaningfulChildrenWithLeafs();
    if (targetChildren.isEmpty()) {
      return false;
    }

    List<PsiElement> elementChildren = PerlPsiUtil.getMeaningfulChildrenWithLeafs(element);

    if (elementChildren.size() < targetChildren.size()) {
      return false;
    }

    PsiElement firstTargetChild = targetChildren.get(0);
    for (int headIndex = 0; headIndex <= elementChildren.size() - targetChildren.size(); headIndex++) {
      if (!PerlPsiUtil.areElementsSame(firstTargetChild, elementChildren.get(headIndex))) {
        continue;
      }

      int offset = 1;
      for (; offset < targetChildren.size(); offset++) {
        if (!PerlPsiUtil.areElementsSame(targetChildren.get(offset), elementChildren.get(headIndex + offset))) {
          offset = -1;
          break;
        }
      }
      if (offset > 0) {
        addOccurrence(PerlIntroduceTarget.create(element, elementChildren.get(headIndex), elementChildren.get(headIndex + offset - 1)));
        headIndex += offset;
      }
    }

    return false;
  }
}
