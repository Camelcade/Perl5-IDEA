/*
 * Copyright 2015-2017 Alexandr Evstigneev
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

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.AbstractElementManipulator;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 27.02.2016.
 */
public abstract class PerlTextContainerManipulator<T extends PsiElement> extends AbstractElementManipulator<T> {
  @SuppressWarnings("Duplicates")
  @Override
  public T handleContentChange(@NotNull T element, @NotNull TextRange range, String newContent) throws IncorrectOperationException {
    final PsiDocumentManager manager = PsiDocumentManager.getInstance(element.getProject());
    final Document document = manager.getDocument(element.getContainingFile());

    if (document != null) {
      TextRange elementRange = element.getTextRange();
      manager.doPostponedOperationsAndUnblockDocument(document);
      document.replaceString(elementRange.getStartOffset() + range.getStartOffset(), elementRange.getStartOffset() + range.getEndOffset(),
                             newContent);
      manager.commitDocument(document);
    }
    return element;
  }
}
