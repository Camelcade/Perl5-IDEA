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
import com.perl5.lang.perl.psi.PerlDerefExpression;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;

class PerlDerefOccurrencesCollector extends PerlTargetOccurrencesCollector {
  @NotNull
  private final List<PsiElement> myTargetChildrenToSearch;

  public PerlDerefOccurrencesCollector(@NotNull PerlIntroduceTarget target) {
    super(target);
    PsiElement targetElement = getTarget().getPlace();
    TextRange rangeInTarget = getTarget().getTextRangeInElement();
    myTargetChildrenToSearch = ContainerUtil.filter(targetElement.getChildren(), it -> rangeInTarget.contains(it.getTextRangeInParent()));
  }

  @Override
  protected boolean collectOccurrences(@NotNull PsiElement element) {
    if (!(element instanceof PerlDerefExpression)) {
      return false;
    }

    PsiElement[] elementChildren = element.getChildren();
    if (elementChildren.length < myTargetChildrenToSearch.size()) {
      return false;
    }
    for (int i = 0; i < myTargetChildrenToSearch.size(); i++) {
      if (!PerlPsiUtil.areElementsSame(myTargetChildrenToSearch.get(i), elementChildren[i])) {
        return false;
      }
    }
    addOccurrence(PerlIntroduceTarget.create(
      element, TextRange.create(0, elementChildren[myTargetChildrenToSearch.size() - 1].getTextRangeInParent().getEndOffset())));
    return true;
  }
}
