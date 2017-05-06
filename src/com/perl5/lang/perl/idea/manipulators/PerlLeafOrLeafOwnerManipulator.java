/*
 * Copyright 2016 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.manipulators;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.AbstractElementManipulator;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.LeafElement;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 07.11.2016.
 */
public class PerlLeafOrLeafOwnerManipulator extends AbstractElementManipulator<PsiElement> {
  @Override
  public PsiElement handleContentChange(@NotNull PsiElement element, @NotNull TextRange range, String newContent)
    throws IncorrectOperationException {
    PsiElement modifyableLeaf;
    if (element instanceof LeafPsiElement) {
      modifyableLeaf = element;
    }
    else {
      modifyableLeaf = element.getFirstChild();
      assert modifyableLeaf instanceof LeafPsiElement;
      assert element.getTextRange().equals(modifyableLeaf.getTextRange());
    }

    LeafElement newElement = ((LeafPsiElement)modifyableLeaf).replaceWithText(range.replace(modifyableLeaf.getText(), newContent));
    return element == modifyableLeaf ? newElement.getPsi() : newElement.getTreeParent().getPsi();
  }
}
