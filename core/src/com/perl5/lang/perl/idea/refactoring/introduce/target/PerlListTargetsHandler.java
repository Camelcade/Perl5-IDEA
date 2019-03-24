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

import com.intellij.openapi.util.Pair;
import com.intellij.psi.PsiElement;
import com.perl5.lang.perl.idea.refactoring.introduce.PerlIntroduceTarget;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

class PerlListTargetsHandler extends PerlSequentialElementTargetHandler {
  static final PerlListTargetsHandler INSTANCE = new PerlListTargetsHandler();

  private PerlListTargetsHandler() {
  }

  @NotNull
  @Override
  protected String createTargetExpressionText(@NotNull PerlIntroduceTarget target) {
    String baseText = super.createTargetExpressionText(target);
    Pair<PsiElement, PsiElement> childrenInRange = getChildrenInRange(target.getPlace(), target.getTextRangeInElement());
    return Objects.equals(childrenInRange.first, childrenInRange.second) ? baseText : "(" + baseText + ")";
  }
}
