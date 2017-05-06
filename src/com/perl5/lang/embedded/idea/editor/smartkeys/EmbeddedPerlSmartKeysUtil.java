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

package com.perl5.lang.embedded.idea.editor.smartkeys;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorModificationUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.embedded.idea.completion.EmbeddedPerlPatterns;
import com.perl5.lang.embedded.psi.EmbeddedPerlElementTypes;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 09.01.2016.
 */
public class EmbeddedPerlSmartKeysUtil implements EmbeddedPerlElementTypes, PerlElementTypes {
  public static boolean addCloseMarker(@NotNull final Editor editor, @NotNull PsiFile file, @NotNull String marker) {
    int offset = editor.getCaretModel().getOffset();

    if (offset >= 2) {
      PsiElement element = file.findElementAt(offset - 2);
      if (element != null && element.getNode().getElementType() == EMBED_MARKER_OPEN && !hasCloseMarkerAhead(file, offset)) {
        EditorModificationUtil.insertStringAtCaret(editor, marker, false, false);
      }
    }
    return false;
  }

  public static boolean hasCloseMarkerAhead(@NotNull PsiFile psiFile, int startOffset) {
    int docLength = psiFile.getTextLength();
    while (startOffset < docLength) {
      PsiElement element = psiFile.findElementAt(startOffset);
      if (element == null) {
        return false;
      }
      IElementType tokenType = element.getNode().getElementType();
      if (tokenType == EMBED_MARKER_CLOSE) {
        return true;
      }
      else if (tokenType == EMBED_MARKER_OPEN ||
               tokenType == QUESTION && EmbeddedPerlPatterns.BROKEN_OPEN_MARKER_PATTERN.accepts(element)) {
        return false;
      }
      startOffset = element.getTextOffset() + element.getTextLength();
    }
    return false;
  }
}
