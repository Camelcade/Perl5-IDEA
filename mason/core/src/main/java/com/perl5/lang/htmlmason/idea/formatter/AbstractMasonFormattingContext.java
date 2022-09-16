/*
 * Copyright 2015-2021 Alexandr Evstigneev
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

package com.perl5.lang.htmlmason.idea.formatter;

import com.intellij.formatting.FormattingContext;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.idea.formatter.PurePerlFormattingContext;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractMasonFormattingContext extends PurePerlFormattingContext {
  public AbstractMasonFormattingContext(@NotNull FormattingContext formattingContext,
                                        @NotNull TextRange adjustedRange) {
    super(formattingContext, adjustedRange);
  }

  protected abstract IElementType getLineOpenerToken();

  @Override
  public boolean isNewLineForbiddenAt(@NotNull ASTNode node) {
    if (super.isNewLineForbiddenAt(node)) {
      return true;
    }

    PsiElement element = node.getPsi();
    PsiFile file = element.getContainingFile();
    Document document = PsiDocumentManager.getInstance(file.getProject()).getDocument(file);
    if (document != null) {
      int offset = node.getTextRange().getStartOffset();
      int lineNumber = document.getLineNumber(offset);
      int lineStartOffset = document.getLineStartOffset(lineNumber);
      PsiElement firstElement = file.findElementAt(lineStartOffset);

      return firstElement != null && firstElement.getNode().getElementType() == getLineOpenerToken();
    }

    return false;
  }
}
