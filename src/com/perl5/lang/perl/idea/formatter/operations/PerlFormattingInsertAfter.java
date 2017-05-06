/*
 * Copyright 2015 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.formatter.operations;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 15.11.2015.
 */
public class PerlFormattingInsertAfter implements PerlFormattingOperation {
  private final PsiElement myAnchor;
  private final PsiElement myFromElement;
  private final PsiElement myToElement;

  public PerlFormattingInsertAfter(@NotNull PsiElement element, @NotNull PsiElement anchorElement) {
    this(element, element, anchorElement);
  }

  public PerlFormattingInsertAfter(@NotNull PsiElement fromElement, @NotNull PsiElement toElement, @NotNull PsiElement anchorElement) {
    assert fromElement.getParent() == toElement.getParent() : "PSI elements range must be under same parent";
    assert fromElement.getStartOffsetInParent() <= toElement.getStartOffsetInParent() : "From element must came first";
    myFromElement = fromElement;
    myToElement = toElement;
    myAnchor = anchorElement;
  }

  @Override
  public int apply() {
    if (!myAnchor.isValid())    // seems something happened on the upper level
    {
      return 0;
    }

    int result = 0;
    PsiElement currentElement = myFromElement;

    PsiElement currentAnchor = getMyAnchor();
    PsiElement anchorContainer = currentAnchor.getParent();

    assert anchorContainer != null;
    assert anchorContainer.isValid();

    while (true) {
      result += currentElement.getNode().getTextLength();
      currentAnchor = insertElement(currentElement, currentAnchor, anchorContainer);

      if (currentElement == myToElement || (currentElement = currentElement.getNextSibling()) == null) {
        break;
      }
      assert currentElement.isValid() : "Element become invalid";
    }

    return result;
  }

  protected PsiElement insertElement(@NotNull PsiElement newElement,
                                     @NotNull PsiElement anchorElement,
                                     @NotNull PsiElement anchorContainer) {
    return anchorContainer.addAfter(newElement, anchorElement);
  }

  public PsiElement getMyAnchor() {
    return myAnchor;
  }
}
