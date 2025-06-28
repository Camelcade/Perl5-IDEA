/*
 * Copyright 2015-2020 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.refactoring.introduce;

import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.util.Segment;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.refactoring.IntroduceTargetChooser;
import com.intellij.refactoring.introduce.PsiIntroduceTarget;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents part of the composite psi element. E.g. {@code elem1->elem2->elem3->elem4}, or part of the string
 *
 * @see IntroduceTargetChooser.MyIntroduceTarget
 */
public class PerlIntroduceTarget extends PsiIntroduceTarget<PsiElement> {
  private final @NotNull TextRange myTextRangeInElement;


  private PerlIntroduceTarget(@NotNull PsiElement element, @NotNull TextRange textRangeInElement) {
    super(element);
    myTextRangeInElement = textRangeInElement;
  }

  @Override
  public String toString() {
    return ReadAction.compute(() -> isValid() ? getPlace() + ";" + getTextRange() + "; " + render() : "INVALID");
  }

  public @NotNull TextRange getTextRangeInElement() {
    return myTextRangeInElement;
  }

  @Override
  public @NotNull String render() {
    String elementText = ReadAction.compute(()->{
      if( !isValid()){
        return "INVALID";
      }
      return super.render();
    });

    return myTextRangeInElement.getEndOffset() > elementText.length() ?
           elementText : myTextRangeInElement.subSequence(elementText).toString();
  }

  /**
   * @return true iff range covers all the element
   */
  public boolean isFullRange() {
    PsiElement element = getPlace();
    return element != null && myTextRangeInElement.getStartOffset() == 0 && myTextRangeInElement.getEndOffset() == element.getTextLength();
  }

  @Override
  public @NotNull TextRange getTextRange() {
    Segment elementRange = super.getTextRange();
    return TextRange.from(elementRange.getStartOffset() + getTextRangeInElement().getStartOffset(), getTextRangeInElement().getLength());
  }

  /**
   * @return matching children of the {@code target}
   */
  public final @NotNull List<PsiElement> getChildren() {
    PsiElement place = getPlace();
    return place == null ? Collections.emptyList() : getChildrenInRange(place, getTextRangeInElement());
  }

  /**
   * @return matching children of the {@code target}
   */
  public final @NotNull List<PsiElement> getMeaningfulChildrenWithLeafs() {
    PsiElement place = getPlace();
    return place == null ? Collections.emptyList() : PerlPsiUtil.getMeaningfulChildrenWithLeafs(place, getTextRangeInElement());
  }

  public static @NotNull PerlIntroduceTarget create(@NotNull PsiElement element) {
    return new PerlIntroduceTarget(element, TextRange.create(0, element.getTextLength()));
  }

  public static @NotNull PerlIntroduceTarget create(@NotNull PsiElement element, @NotNull PsiElement lastChildToInclude) {
    if (!element.equals(lastChildToInclude.getParent())) {
      throw new RuntimeException("Last child is not a child of an element");
    }
    return new PerlIntroduceTarget(element, TextRange.create(0, lastChildToInclude.getTextRangeInParent().getEndOffset()));
  }

  public static @NotNull PerlIntroduceTarget create(@NotNull PsiElement element, @NotNull TextRange textRangeInElement) {
    if (textRangeInElement.getStartOffset() < 0 || textRangeInElement.getEndOffset() > element.getTextLength()) {
      throw new RuntimeException("Incorrect element range: " +
                                 "element: " + element + "; " +
                                 "elementRange: " + element.getTextRange() + "; " +
                                 "providedRange: " + textRangeInElement + "; "
      );
    }
    return new PerlIntroduceTarget(element, textRangeInElement);
  }

  public static @NotNull PerlIntroduceTarget create(@NotNull PsiElement element,
                                                    @NotNull PsiElement firstDescendantToInclude,
                                                    @NotNull PsiElement lastDescendantToInclude) {
    if (!PsiTreeUtil.isAncestor(element, lastDescendantToInclude, true)) {
      throw new RuntimeException("Last descendant is not under an element: " +
                                 "element: " + element + "; " +
                                 "firstDescendant: " + firstDescendantToInclude + "; " +
                                 "lastDescendant: " + lastDescendantToInclude + "; "
      );
    }
    if (!PsiTreeUtil.isAncestor(element, firstDescendantToInclude, true)) {
      throw new RuntimeException("First descendant is not under an element: " +
                                 "element: " + element + "; " +
                                 "firstDescendant: " + firstDescendantToInclude + "; " +
                                 "lastDescendant: " + lastDescendantToInclude + "; "
      );
    }
    int firstChildStartOffset = firstDescendantToInclude.getTextRange().getStartOffset();
    int lastChildEndOffset = lastDescendantToInclude.getTextRange().getEndOffset();
    if (firstChildStartOffset > lastChildEndOffset) {
      throw new RuntimeException(
        "Inconsistent offsets: " +
        "element: " + element + "; " +
        "firstDescendant: " + firstDescendantToInclude + "; " +
        "lastDescendant: " + lastDescendantToInclude + "; " +
        "element range: " + element.getTextRange() + "; " +
        "firstDescendant range: " + firstDescendantToInclude.getTextRange() + "; " +
        "lastDescendant: range: " + lastDescendantToInclude.getTextRange() + "; "
      );
    }
    return new PerlIntroduceTarget(
      element, TextRange.create(firstChildStartOffset, lastChildEndOffset).shiftLeft(element.getTextRange().getStartOffset()));
  }

  public static @NotNull PerlIntroduceTarget create(@NotNull PsiElement element,
                                                    int startOffsetInParent,
                                                    int endOffsetInParent) {
    if (startOffsetInParent > endOffsetInParent) {
      throw new RuntimeException(
        "Inconsistent offsets: " +
        "element: " + element + "; " +
        "startOffset: " + startOffsetInParent + "; " +
        "endOffset: " + endOffsetInParent + "; "
      );
    }
    return new PerlIntroduceTarget(element, TextRange.create(startOffsetInParent, endOffsetInParent));
  }

  /**
   * @return all children of {@code element} inside {@code rangeInElement}
   */
  private static @NotNull List<PsiElement> getChildrenInRange(@NotNull PsiElement element, TextRange rangeInElement) {
    List<PsiElement> result = new ArrayList<>();
    TextRange rangeInDocument = rangeInElement.shiftRight(element.getNode().getStartOffset());
    for (PsiElement child : PerlPsiUtil.getCompositeChildrenUnwrappingLazy(element)) {
      if (rangeInDocument.contains(child.getTextRange())) {
        result.add(child);
      }
    }
    return result;
  }
}
