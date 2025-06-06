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

package com.perl5.lang.mojolicious.idea.editor.smartkeys;

import com.intellij.codeInsight.editorActions.enter.EnterHandlerDelegateAdapter;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorModificationUtilEx;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.util.Ref;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.mojolicious.MojoliciousLanguage;
import com.perl5.lang.mojolicious.psi.MojoliciousFileViewProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.perl5.lang.mojolicious.MojoliciousElementTypes.MOJO_LINE_OPENER;
import static com.perl5.lang.mojolicious.MojoliciousElementTypes.MOJO_OUTER_ELEMENT_TYPE;
import static com.perl5.lang.mojolicious.MojoliciousSyntaxElements.KEYWORD_MOJO_BLOCK_CLOSER;
import static com.perl5.lang.mojolicious.MojoliciousSyntaxElements.KEYWORD_MOJO_LINE_OPENER;


public class MojoliciousEnterHandlerDelegate extends EnterHandlerDelegateAdapter {
  @Override
  public Result preprocessEnter(@NotNull PsiFile file,
                                @NotNull Editor editor,
                                @NotNull Ref<Integer> caretOffset,
                                @NotNull Ref<Integer> caretAdvance,
                                @NotNull DataContext dataContext,
                                @Nullable EditorActionHandler originalHandler) {
    FileViewProvider viewProvider = file.getViewProvider();
    if (viewProvider instanceof MojoliciousFileViewProvider mojoFileViewProvider) {
      if (!(MojoliciousSmartKeysUtil.addCloseMarker(editor, "\n" + KEYWORD_MOJO_BLOCK_CLOSER) ||
            MojoliciousSmartKeysUtil.addEndMarker(editor, "\n% end\n"))) {
        addOutlineMarkerIfNeeded(editor, mojoFileViewProvider, caretOffset.get());
      }
    }
    return Result.Continue;
  }

  protected void addOutlineMarkerIfNeeded(@NotNull Editor editor,
                                          @NotNull MojoliciousFileViewProvider viewProvider,
                                          int offset
  ) {
    PsiElement element = viewProvider.findElementAt(offset, MojoliciousLanguage.INSTANCE);
    while (element instanceof PsiWhiteSpace) {
      if (element.getText().charAt(0) == '\n') {
        return;
      }
      element = element.getNextSibling();
    }

    if (element == null) {
      return;
    }

    Document document = viewProvider.getDocument();
    if (document == null) {
      return;
    }

    int elementLine = document.getLineNumber(offset);
    int lineStart = document.getLineStartOffset(elementLine);
    element = viewProvider.findElementAt(lineStart, MojoliciousLanguage.INSTANCE);

    while (element instanceof PsiWhiteSpace || PsiUtilCore.getElementType(element) == MOJO_OUTER_ELEMENT_TYPE) {
      element = element.getNextSibling();
    }

    IElementType tokenType = PsiUtilCore.getElementType(element);
    if (tokenType == MOJO_LINE_OPENER && element.getNode().getStartOffset() < offset) {
      EditorModificationUtilEx.insertStringAtCaret(editor, KEYWORD_MOJO_LINE_OPENER + " ", false, true);
    }
  }
}
