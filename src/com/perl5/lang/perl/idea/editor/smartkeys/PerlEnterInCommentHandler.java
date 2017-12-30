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

package com.perl5.lang.perl.idea.editor.smartkeys;

import com.intellij.codeInsight.editorActions.enter.EnterHandlerDelegateAdapter;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.editor.highlighter.HighlighterIterator;
import com.intellij.openapi.util.Ref;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.PerlLanguage;
import org.jetbrains.annotations.NotNull;

import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.COMMENT_LINE;

public class PerlEnterInCommentHandler extends EnterHandlerDelegateAdapter {
  @Override
  public Result preprocessEnter(@NotNull PsiFile file,
                                @NotNull Editor editor,
                                @NotNull Ref<Integer> caretOffset,
                                @NotNull Ref<Integer> caretAdvance,
                                @NotNull DataContext dataContext,
                                EditorActionHandler originalHandler) {
    if (!file.getLanguage().is(PerlLanguage.INSTANCE)) {
      return Result.Continue;
    }

    int currentOffset = caretOffset.get();

    assert editor instanceof EditorEx;
    HighlighterIterator highlighterIterator = ((EditorEx)editor).getHighlighter().createIterator(currentOffset);
    IElementType currentTokenType = highlighterIterator.getTokenType();
    int currentTokenStart = highlighterIterator.getStart();

    //noinspection ConstantConditions
    if (currentTokenType == COMMENT_LINE && currentOffset > currentTokenStart) {
      Document document = editor.getDocument();

      int lineNumber = document.getLineNumber(currentOffset);
      int lineEnd = document.getLineEndOffset(lineNumber);

      if (lineEnd > currentOffset) {
        document.insertString(currentOffset, "# ");
      }
    }
    return Result.Continue;
  }
}
