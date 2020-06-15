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

package com.perl5.lang.perl.idea.editor.smartkeys;

import com.intellij.codeInsight.editorActions.BackspaceHandlerDelegate;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.editor.highlighter.EditorHighlighter;
import com.intellij.openapi.editor.highlighter.HighlighterIterator;
import com.intellij.openapi.util.Key;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.idea.codeInsight.Perl5CodeInsightSettings;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

import static com.perl5.lang.perl.lexer.PerlTokenSets.*;

public class PerlBackspaceHandler extends BackspaceHandlerDelegate {
  private static final Key<Supplier<Boolean>> POST_HANDLER = Key.create("perl.post.handler");

  @Override
  public void beforeCharDeleted(char c, @NotNull PsiFile file, @NotNull Editor editor) {
    CaretModel caretModel = editor.getCaretModel();
    int currentOffset = caretModel.getOffset() - 1;
    if (currentOffset < 0) {
      return;
    }
    EditorHighlighter highlighter = ((EditorEx)editor).getHighlighter();
    HighlighterIterator iterator = highlighter.createIterator(currentOffset);
    IElementType tokenToDelete = iterator.atEnd() ? null : iterator.getTokenType();
    if (QUOTE_OPEN_ANY.contains(tokenToDelete)) {
      PerlEditorUtil.moveToNextMeaningfulToken(iterator);
      if (iterator.atEnd()) {
        return;
      }
      IElementType nextTokenType = iterator.getTokenType();
      if (QUOTE_CLOSE_PAIRED.contains(nextTokenType)) {
        int startOffsetToDelete = currentOffset + 1;

        if (currentOffset > 0 && QUOTE_MIDDLE.contains(tokenToDelete)) {
          HighlighterIterator preQuoteIterator =
            PerlEditorUtil.moveToPreviousMeaningfulToken(highlighter.createIterator(currentOffset - 1));
          if (!preQuoteIterator.atEnd() && QUOTE_OPEN_ANY.contains(preQuoteIterator.getTokenType())) {
            startOffsetToDelete = preQuoteIterator.getEnd();
            caretModel.moveToOffset(preQuoteIterator.getEnd());
          }
        }
        else {
          POST_HANDLER.set(editor, () -> true);
        }
        editor.getDocument().deleteString(startOffsetToDelete, iterator.getEnd());
      }
    }
    else if (c == ':' && Perl5CodeInsightSettings.getInstance().AUTO_INSERT_COLON &&
             currentOffset > 0 && PerlTypedHandler.COLON_HANDLING_TOKENS.contains(tokenToDelete)) {
      Document document = editor.getDocument();
      CharSequence documentCharsSequence = document.getCharsSequence();
      if (documentCharsSequence.charAt(currentOffset - 1) == ':') {
        document.deleteString(currentOffset - 1, currentOffset);
      }
      else if (currentOffset + 1 < documentCharsSequence.length() && documentCharsSequence.charAt(currentOffset + 1) == ':') {
        document.deleteString(currentOffset + 1, currentOffset + 2);
      }
    }
  }

  @Override
  public boolean charDeleted(char c, @NotNull PsiFile file, @NotNull Editor editor) {
    Supplier<Boolean> postHandler = POST_HANDLER.get(editor);
    POST_HANDLER.set(editor, null);
    return postHandler != null && postHandler.get();
  }
}
