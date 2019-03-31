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
import com.intellij.util.containers.ContainerUtil;
import com.perl5.lang.perl.idea.refactoring.introduce.PerlIntroduceTarget;
import com.perl5.lang.perl.psi.PsiPerlStringBare;
import com.perl5.lang.perl.psi.utils.PerlElementFactory;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import org.jetbrains.annotations.NotNull;

import java.util.*;

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
    PsiElement lastChild = children[children.length - 1];
    for (PsiElement child : children) {
      TextRange childTextRange = child.getTextRange();
      if (childTextRange.contains(caretOffset) && !firstChild.equals(child) || childTextRange.getStartOffset() > caretOffset) {
        if (child.equals(lastChild)) {
          result.add(PerlIntroduceTarget.create(element));
        }
        else {
          result.add(PerlIntroduceTarget.create(element, firstChild, child));
        }
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

  @NotNull
  @Override
  protected final List<PsiElement> replaceTarget(@NotNull List<PerlIntroduceTarget> occurrences, @NotNull PsiElement replacement) {
    if (occurrences.size() == 1 && occurrences.get(0).isFullRange()) {
      return super.replaceTarget(occurrences, replacement);
    }
    return replaceNonTrivialTarget(occurrences, replacement);
  }

  @NotNull
  protected List<PsiElement> replaceNonTrivialTarget(@NotNull List<PerlIntroduceTarget> occurrences,
                                                     @NotNull PsiElement replacement) {
    PsiElement occurrencePlace = occurrences.get(0).getPlace();
    if (occurrencePlace == null) {
      reportEmptyPlace();
      return Collections.emptyList();
    }

    List<Pair<PsiElement, PsiElement>> elementsRangesToReplace = new ArrayList<>();

    for (PerlIntroduceTarget occurrence : occurrences) {
      List<PsiElement> childrenInRange = occurrence.getChildren();

      if (childrenInRange.isEmpty()) {
        LOG.error("Unable to detect children to replace, please report developers with source sample");
        return Collections.emptyList();
      }
      elementsRangesToReplace.add(Pair.create(childrenInRange.get(0), ContainerUtil.getLastItem(childrenInRange)));
    }

    List<PsiElement> result = new ArrayList<>();
    for (Pair<PsiElement, PsiElement> elementRangeToReplace : elementsRangesToReplace) {
      result.add(occurrencePlace.addBefore(replacement, elementRangeToReplace.first));
      occurrencePlace.deleteChildRange(elementRangeToReplace.first, elementRangeToReplace.second);
    }


    return result;
  }

  @NotNull
  @Override
  protected String createTargetExpressionText(@NotNull PerlIntroduceTarget target) {
    if (target.isFullRange()) {
      return super.createTargetExpressionText(target);
    }
    PsiElement targetPlace = target.getPlace();
    if (targetPlace == null) {
      return reportEmptyPlace();
    }
    return target.getTextRangeInElement().subSequence(targetPlace.getText()).toString();
  }

  /**
   * Builds a comma sequence from {@code sequenceElements} and replaces a {@code baseElement} with it
   *
   * @return list of psiElements representing {@code replacement}
   */
  @NotNull
  final List<PsiElement> replaceSequenceWithFlatter(@NotNull PsiElement baseElement,
                                                    @NotNull PsiElement replacement,
                                                    @NotNull List<PsiElement> sequenceElement) {
    Set<TextRange> replacementsRanges = new HashSet<>();
    StringBuilder sb = new StringBuilder("(");
    for (PsiElement element : sequenceElement) {
      if (sb.length() > 1) {
        sb.append(",");
      }
      int startOffset = sb.length();
      if (element instanceof PsiPerlStringBare) {
        sb.append(PerlPsiUtil.createSingleQuotedString(element.getText()));
      }
      else {
        sb.append(element.getText());
        if (element.equals(replacement)) {
          replacementsRanges.add(TextRange.create(startOffset, sb.length()));
        }
      }
    }
    sb.append(")");

    PsiElement sequenceExpression =
      Objects.requireNonNull(PerlElementFactory.createStatement(baseElement.getProject(), sb.toString())).getFirstChild();
    List<PsiElement> result = new ArrayList<>();
    PsiElement newSequenceExpression = baseElement.replace(sequenceExpression);
    for (PsiElement newSequenceExpressionChild : newSequenceExpression.getChildren()) {
      if (replacementsRanges.contains(newSequenceExpressionChild.getTextRangeInParent())) {
        result.add(newSequenceExpressionChild);
      }
    }
    return result;
  }
}
