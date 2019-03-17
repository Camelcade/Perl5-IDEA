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

package com.perl5.lang.perl.idea.refactoring;

import com.intellij.openapi.util.Segment;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.refactoring.IntroduceTargetChooser;
import com.intellij.refactoring.introduce.PsiIntroduceTarget;
import org.jetbrains.annotations.NotNull;

/**
 * Represents part of the composite psi element. E.g. {@code elem1->elem2->elem3->elem4}, or part of the string
 *
 * @see IntroduceTargetChooser.MyIntroduceTarget
 */
public class PerlIntroduceTarget extends PsiIntroduceTarget<PsiElement> {
  @NotNull
  private final TextRange myTextRangeInElement;


  private PerlIntroduceTarget(@NotNull PsiElement element, @NotNull TextRange textRangeInElement) {
    super(element);
    myTextRangeInElement = textRangeInElement;
  }

  @NotNull
  public TextRange getTextRangeInElement() {
    return myTextRangeInElement;
  }

  @NotNull
  @Override
  public String render() {
    return myTextRangeInElement.subSequence(super.render()).toString();
  }

  @NotNull
  public TextRange getTextRange() {
    Segment elementRange = super.getTextRange();
    return TextRange.from(elementRange.getStartOffset() + getTextRangeInElement().getStartOffset(), getTextRangeInElement().getLength());
  }

  @NotNull
  public static PerlIntroduceTarget create(@NotNull PsiElement element) {
    return new PerlIntroduceTarget(element, TextRange.create(0, element.getTextLength()));
  }

  @NotNull
  public static PerlIntroduceTarget create(@NotNull PsiElement element, @NotNull PsiElement lastChildToInclude) {
    if (!element.equals(lastChildToInclude.getParent())) {
      throw new RuntimeException("Last child is not a child of an element");
    }
    return new PerlIntroduceTarget(element, TextRange.create(0, lastChildToInclude.getTextRangeInParent().getEndOffset()));
  }

  @NotNull
  public static PerlIntroduceTarget create(@NotNull PsiElement element,
                                           @NotNull PsiElement firstChildToInclude,
                                           @NotNull PsiElement lastChildToInclude) {
    if (!element.equals(lastChildToInclude.getParent())) {
      throw new RuntimeException("Last child is not a child of an element: " +
                                 "element: " + element + "; " +
                                 "firstChild: " + firstChildToInclude + "; " +
                                 "lastChild: " + lastChildToInclude + "; "
      );
    }
    if (!element.equals(firstChildToInclude.getParent())) {
      throw new RuntimeException("First child is not a child of an element: " +
                                 "element: " + element + "; " +
                                 "firstChild: " + firstChildToInclude + "; " +
                                 "lastChild: " + lastChildToInclude + "; "
      );
    }
    int firstChildStartOffset = firstChildToInclude.getTextRangeInParent().getStartOffset();
    int lastChildEndOffset = lastChildToInclude.getTextRangeInParent().getEndOffset();
    if (firstChildStartOffset > lastChildEndOffset) {
      throw new RuntimeException(
        "Inconsistent offsets: " +
        "element: " + element + "; " +
        "firstChild: " + firstChildToInclude + "; " +
        "lastChild: " + lastChildToInclude + "; " +
        "element range: " + element.getTextRange() + "; " +
        "firstChild range: " + firstChildToInclude.getTextRange() + "; " +
        "lastChild: range: " + lastChildToInclude.getTextRange() + "; "
      );
    }
    return new PerlIntroduceTarget(element, TextRange.create(firstChildStartOffset, lastChildEndOffset));
  }

  @NotNull
  public static PerlIntroduceTarget create(@NotNull PsiElement element,
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
}
