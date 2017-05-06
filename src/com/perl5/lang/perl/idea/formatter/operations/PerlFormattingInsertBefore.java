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
public class PerlFormattingInsertBefore extends PerlFormattingInsertAfter {
  public PerlFormattingInsertBefore(@NotNull PsiElement element, @NotNull PsiElement anchorElement) {
    super(element, anchorElement);
  }

  public PerlFormattingInsertBefore(@NotNull PsiElement fromElement, @NotNull PsiElement toElement, @NotNull PsiElement anchorElement) {
    super(fromElement, toElement, anchorElement);
  }

  @Override
  protected PsiElement insertElement(@NotNull PsiElement newElement,
                                     @NotNull PsiElement anchorElement,
                                     @NotNull PsiElement anchorContainer) {
    if (getMyAnchor() == anchorContainer)    // first element being added before
    {
      return anchorContainer.addBefore(newElement, anchorElement);
    }
    else    // the rest if added after
    {
      return super.insertElement(newElement, anchorElement, anchorContainer);
    }
  }
}
