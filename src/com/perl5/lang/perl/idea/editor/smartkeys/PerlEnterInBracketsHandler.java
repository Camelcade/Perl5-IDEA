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

import com.intellij.codeInsight.CodeInsightSettings;
import com.intellij.codeInsight.editorActions.enter.EnterHandlerDelegateAdapter;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.editor.highlighter.EditorHighlighter;
import com.intellij.openapi.editor.highlighter.HighlighterIterator;
import com.intellij.openapi.util.Ref;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;

import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.*;

public class PerlEnterInBracketsHandler extends EnterHandlerDelegateAdapter {
  private static final Logger LOG = Logger.getInstance(PerlEnterInBracketsHandler.class);

  @Override
  public Result preprocessEnter(@NotNull PsiFile file,
                                @NotNull Editor editor,
                                @NotNull Ref<Integer> caretOffset,
                                @NotNull Ref<Integer> caretAdvance,
                                @NotNull DataContext dataContext,
                                EditorActionHandler originalHandler) {

    if (!CodeInsightSettings.getInstance().SMART_INDENT_ON_ENTER) {
      return Result.Continue;
    }
    Integer currentOffset = caretOffset.get();
    if (currentOffset < 1) {
      return Result.Continue;
    }

    assert editor instanceof EditorEx;
    EditorHighlighter editorHighlighter = ((EditorEx)editor).getHighlighter();
    Document document = editor.getDocument();
    CharSequence documentChars = document.getCharsSequence();

    HighlighterIterator highlighterIterator = editorHighlighter.createIterator(currentOffset - 1);
    IElementType previousTokenType = PerlEditorUtil.getPreviousTokenType(highlighterIterator);
    if (highlighterIterator.atEnd() ||
        StringUtil.containsLineBreak(documentChars.subSequence(highlighterIterator.getStart(), currentOffset))) {
      return Result.Continue;
    }
    int openTokenStart = highlighterIterator.getStart();

    IElementType nextTokenType = PerlEditorUtil.getNextTokenType(highlighterIterator);
    if (highlighterIterator.atEnd() ||
        StringUtil.containsLineBreak(documentChars.subSequence(currentOffset, highlighterIterator.getEnd()))) {
      return Result.Continue;
    }

    if (previousTokenType == LEFT_BRACKET && nextTokenType == RIGHT_BRACKET) {
      doIndent(file, editor, dataContext, originalHandler, document);
    }
    else if (previousTokenType == QUOTE_SINGLE_OPEN && nextTokenType == QUOTE_SINGLE_CLOSE && openTokenStart > 0) {
      IElementType qwTokenType = PerlEditorUtil.getPreviousTokenType(editorHighlighter.createIterator(openTokenStart - 1));
      if (qwTokenType == RESERVED_QW) {
        doIndent(file, editor, dataContext, originalHandler, document);
      }
    }
    return Result.Continue;
  }

  private void doIndent(@NotNull PsiFile file,
                        @NotNull Editor editor,
                        @NotNull DataContext dataContext,
                        EditorActionHandler originalHandler, Document document) {
    originalHandler.execute(editor, editor.getCaretModel().getCurrentCaret(), dataContext);
    PsiDocumentManager.getInstance(file.getProject()).commitDocument(document);
    try {
      CodeStyleManager.getInstance(file.getProject()).adjustLineIndent(file, editor.getCaretModel().getOffset());
    }
    catch (IncorrectOperationException e) {
      LOG.error(e);
    }
  }
}
