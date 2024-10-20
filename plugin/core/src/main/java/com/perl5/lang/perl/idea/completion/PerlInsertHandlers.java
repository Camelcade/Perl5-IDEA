/*
 * Copyright 2015-2024 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.completion;

import com.intellij.codeInsight.completion.InsertHandler;
import com.intellij.codeInsight.completion.InsertionContext;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.EditorModificationUtilEx;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.editor.highlighter.HighlighterIterator;
import com.perl5.lang.perl.lexer.PerlTokenSets;
import org.jetbrains.annotations.NotNull;


public class PerlInsertHandlers {
  public static final InsertHandler<LookupElement> ARRAY_ELEMENT_INSERT_HANDLER = new ArrayElementInsertHandler();
  public static final InsertHandler<LookupElement> HASH_ELEMENT_INSERT_HANDLER = new HashElementInsertHandler();

  /**
   * Array element/slice insert handler
   */
  static class ArrayElementInsertHandler implements InsertHandler<LookupElement> {
    @Override
    public void handleInsert(final @NotNull InsertionContext context, @NotNull LookupElement item) {
      adjustCaretForBracedVariableName(context);
      EditorModificationUtilEx.insertStringAtCaret(context.getEditor(), "[]", false, true, 1);
    }
  }

  /**
   * Moves caret one char forward if we've completed braced variable name.
   */
  public static void adjustCaretForBracedVariableName(@NotNull InsertionContext insertionContext) {
    EditorEx editor = (EditorEx)insertionContext.getEditor();
    CaretModel caretModel = editor.getCaretModel();
    int offset = caretModel.getOffset();
    HighlighterIterator iterator = editor.getHighlighter().createIterator(offset);
    if (!iterator.atEnd() && PerlTokenSets.VARIABLE_CLOSE_BRACES.contains(iterator.getTokenType())) {
      caretModel.moveToOffset(offset + 1);
    }
  }

  /**
   * Hash element/slice insert handler
   */
  static class HashElementInsertHandler implements InsertHandler<LookupElement> {
    @Override
    public void handleInsert(final @NotNull InsertionContext context, @NotNull LookupElement item) {
      adjustCaretForBracedVariableName(context);
      EditorModificationUtilEx.insertStringAtCaret(context.getEditor(), "{}", false, true, 1);
    }
  }
}
