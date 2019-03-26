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

import com.intellij.openapi.util.Pair;
import com.intellij.psi.PsiElement;
import com.perl5.lang.perl.idea.refactoring.introduce.PerlIntroduceTarget;
import com.perl5.lang.perl.psi.impl.PerlHeredocElementImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

import static com.perl5.lang.perl.idea.refactoring.introduce.target.PerlIntroduceTargetsHandler.isTargetableHeredocElement;

class PerlHeredocOccurrencesCollector extends PerlGenericStringsOccurrencesCollector {
  public PerlHeredocOccurrencesCollector(@NotNull PerlIntroduceTarget target) {
    super(target);
  }

  @NotNull
  @Override
  protected Pair<PsiElement, PsiElement> getChildrenRangeToCollect(@NotNull PerlIntroduceTarget target) {
    PsiElement targetElement = target.getPlace();
    if (!isTargetableHeredocElement(targetElement)) {
      throw new RuntimeException("Expected targetable heredoc element, got: " + target);
    }

    return Pair.create(targetElement.getFirstChild(), null);
  }

  @NotNull
  @Override
  protected List<PsiElement> getElementChildren(@Nullable PsiElement element) {
    return !isTargetableHeredocElement(element) ? Collections.emptyList() : ((PerlHeredocElementImpl)element).getAllChildrenList();
  }
}
