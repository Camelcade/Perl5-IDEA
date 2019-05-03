/*
 * Copyright 2015-2019 Alexandr Evstigneev
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

package com.perl5.lang.pod.idea.codeInsight;

import com.intellij.codeInsight.TargetElementEvaluator;
import com.intellij.codeInsight.TargetElementEvaluatorEx2;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.pod.parser.psi.PodTitledSection;
import org.jetbrains.annotations.Nullable;

public class PodTargetElementEvaluator extends TargetElementEvaluatorEx2 implements TargetElementEvaluator {
  @Nullable
  @Override
  public PsiElement getNamedElement(@Nullable PsiElement element) {
    PodTitledSection parent = PsiTreeUtil.getParentOfType(element, PodTitledSection.class, false);
    if (parent == null) {
      return null;
    }
    PsiElement titleElement = parent.getTitleElement();
    if (titleElement != null && titleElement.getTextRange().contains(element.getTextRange())) {
      return parent;
    }
    return getNamedElement(parent.getParent());
  }
}
