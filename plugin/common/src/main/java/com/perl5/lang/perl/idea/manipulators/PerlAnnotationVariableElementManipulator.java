/*
 * Copyright 2015-2025 Alexandr Evstigneev
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
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.util.IncorrectOperationException;
import com.perl5.lang.perl.psi.PerlAnnotationVariableElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PerlAnnotationVariableElementManipulator extends AbstractElementManipulator<PerlAnnotationVariableElement> {
  @Override
  public @Nullable PerlAnnotationVariableElement handleContentChange(@NotNull PerlAnnotationVariableElement element,
                                                                     @NotNull TextRange range,
                                                                     String newContent) throws IncorrectOperationException {
    var leafElement = element.getFirstChild();
    if (!(leafElement instanceof LeafPsiElement leafPsiElement)) {
      return element;
    }
    var newLeafElement = leafPsiElement.replaceWithText(range.replace(element.getText(), newContent));
    var newAnnotationVariableElement = newLeafElement.getTreeParent().getPsi();
    return newAnnotationVariableElement instanceof PerlAnnotationVariableElement annotationVariableElement
           ? annotationVariableElement
           : null;
  }

  @Override
  public @NotNull TextRange getRangeInElement(@NotNull PerlAnnotationVariableElement element) {
    var elementTextLength = element.getTextLength();
    return elementTextLength > 1 ? new TextRange(1, elementTextLength) : TextRange.EMPTY_RANGE;
  }
}
