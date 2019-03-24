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
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.perl.idea.refactoring.introduce.PerlIntroduceTarget;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.DEREF_EXPR;
import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.LP_STRING_QW;

/**
 * Computes introduce targets for expr+ elements: comma sequences, lists, additions, etc
 */
abstract class PerlSequentialElementTargetHandler extends PerlIntroduceTargetsHandler {
  private static final Logger LOG = Logger.getInstance(PerlSequentialElementTargetHandler.class);

  @NotNull
  @Override
  protected List<PerlIntroduceTarget> computeTargetsAtCaret(@NotNull PsiElement element, int caretOffset) {
    PsiElement[] children = element.getChildren();
    if (children.length == 1 && PsiUtilCore.getElementType(children[0]) == LP_STRING_QW) {
      children = children[0].getChildren();
    }
    if (children.length <= 1) {
      return Collections.emptyList();
    }
    List<PerlIntroduceTarget> result = new ArrayList<>();
    PsiElement firstChild = children[0];
    for (PsiElement child : children) {
      TextRange childTextRange = child.getTextRange();
      if (childTextRange.contains(caretOffset) && !firstChild.equals(child) || childTextRange.getStartOffset() > caretOffset) {
        result.add(PerlIntroduceTarget.create(element, firstChild, child));
      }
    }
    return result;
  }

  @NotNull
  @Override
  protected List<PerlIntroduceTarget> computeTargetsFromSelection(@NotNull PsiElement element, @NotNull TextRange selectionRange) {
    PsiElement[] children = element.getChildren();
    if (children.length == 1 && PsiUtilCore.getElementType(children[0]) == LP_STRING_QW) {
      children = children[0].getChildren();
    }
    PsiElement firstChildToInclude =
      PsiUtilCore.getElementType(element) == DEREF_EXPR ? element.getFirstChild() : null;
    PsiElement lastChildToInclude = null;
    for (PsiElement child : children) {
      TextRange childTextRange = child.getTextRange();
      if (!selectionRange.intersectsStrict(childTextRange)) {
        continue;
      }
      if (firstChildToInclude == null) {
        firstChildToInclude = child;
      }
      lastChildToInclude = child;
    }
    if (firstChildToInclude == null || lastChildToInclude == null) {
      return Collections.emptyList();
    }

    return Collections.singletonList(PerlIntroduceTarget.create(element, firstChildToInclude, lastChildToInclude));
  }

  @Nullable
  @Override
  protected PsiElement replaceTarget(@NotNull PerlIntroduceTarget occurrence, @NotNull PsiElement replacement) {
    if (occurrence.isFullRange()) {
      return super.replaceTarget(occurrence, replacement);
    }
    PsiElement occurrencePlace = occurrence.getPlace();
    if (occurrencePlace == null) {
      reportEmptyPlace();
      return null;
    }
    Pair<PsiElement, PsiElement> childrenInRange = getChildrenInRange(occurrencePlace, occurrence.getTextRangeInElement());

    if (childrenInRange.first == null || !childrenInRange.first.isValid()) {
      LOG.error("Unable to detect children to replace, please report developers with source sample");
      return null;
    }

    PsiElement insertedReplacement = occurrencePlace.addBefore(replacement, childrenInRange.first);
    occurrencePlace.deleteChildRange(childrenInRange.first, childrenInRange.second);
    return insertedReplacement;
  }

  /**
   * @return first and last child of {@code element} inside {@code rangeInElement}
   */
  @NotNull
  protected Pair<PsiElement, PsiElement> getChildrenInRange(@NotNull PsiElement element, TextRange rangeInElement) {
    PsiElement firstChild = null;
    PsiElement lastChild = null;

    for (PsiElement child : element.getChildren()) {
      if (!rangeInElement.contains(child.getTextRangeInParent())) {
        continue;
      }
      if (firstChild == null) {
        firstChild = child;
      }
      lastChild = child;
    }
    return Pair.create(firstChild, lastChild);
  }

  @NotNull
  @Override
  protected String createTargetExpressionText(@NotNull PerlIntroduceTarget target) {
    if (target.isFullRange()) {
      return super.createTargetExpressionText(target);
    }
    PsiElement targetPlace = target.getPlace();
    if (target == null) {
      return reportEmptyPlace();
    }
    return target.getTextRangeInElement().subSequence(targetPlace.getText()).toString();
  }
}
