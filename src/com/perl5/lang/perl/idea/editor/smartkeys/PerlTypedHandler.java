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

import com.intellij.codeInsight.AutoPopupController;
import com.intellij.codeInsight.editorActions.TypedHandlerDelegate;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorModificationUtil;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.editor.highlighter.EditorHighlighter;
import com.intellij.openapi.editor.highlighter.HighlighterIterator;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.PsiDocumentManagerBase;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.perl.lexer.PerlBaseLexer;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.PsiPerlHashIndex;
import org.jetbrains.annotations.NotNull;

import static com.perl5.lang.perl.lexer.PerlTokenSets.QUOTE_CLOSE_FIRST_ANY;
import static com.perl5.lang.perl.lexer.PerlTokenSets.QUOTE_OPEN_ANY;

/**
 * Created by hurricup on 25.07.2015.
 */
public class PerlTypedHandler extends TypedHandlerDelegate implements PerlElementTypes {
  // these chars are automatically closed by IDEA and we can't control this
  private static final String HANDLED_BY_BRACE_MATCHER = "{([";

  private static final TokenSet DOUBLE_QUOTE_OPENERS = TokenSet.create(
    RESERVED_S,
    RESERVED_TR,
    RESERVED_Y
  );

  @Override
  public Result beforeCharTyped(char c, Project project, Editor editor, PsiFile file, FileType fileType) {
    CaretModel caretModel = editor.getCaretModel();
    int currentOffset = caretModel.getOffset();
    CharSequence documentSequence = editor.getDocument().getCharsSequence();
    if (currentOffset >= documentSequence.length()) {
      return Result.CONTINUE;
    }

    HighlighterIterator iterator = ((EditorEx)editor).getHighlighter().createIterator(currentOffset);
    if (QUOTE_CLOSE_FIRST_ANY.contains(iterator.getTokenType()) && c == documentSequence.charAt(currentOffset)) {
      caretModel.moveToOffset(currentOffset + 1);
      return Result.STOP;
    }

    return Result.CONTINUE;
  }

  @Override
  public Result charTyped(char typedChar, Project project, @NotNull Editor editor, @NotNull PsiFile file) {
    int offset = editor.getCaretModel().getOffset() - 1;
    if (offset < 0) {
      return Result.CONTINUE;
    }
    EditorHighlighter highlighter = ((EditorEx)editor).getHighlighter();
    HighlighterIterator iterator = highlighter.createIterator(offset);
    IElementType elementTokenType = iterator.getTokenType();
    Document document = editor.getDocument();
    if (QUOTE_OPEN_ANY.contains(elementTokenType)) {
      IElementType quotePrefixType = offset > 0 ? PerlEditorUtil.getPreviousTokenType(highlighter.createIterator(offset - 1)) : null;
      CharSequence text = document.getCharsSequence();
      if (offset > text.length() - 1 || text.charAt(offset) != typedChar) {
        return Result.CONTINUE;
      }
      if (elementTokenType == QUOTE_DOUBLE_OPEN || elementTokenType == QUOTE_SINGLE_OPEN) {
        AutoPopupController.getInstance(project).scheduleAutoPopup(editor);
      }
      char openChar = text.charAt(offset);
      char closeChar = PerlBaseLexer.getQuoteCloseChar(openChar);
      iterator.advance();
      IElementType possibleCloseQuoteType = iterator.atEnd() ? null : iterator.getTokenType();
      if (QUOTE_CLOSE_FIRST_ANY.contains(possibleCloseQuoteType) && closeChar == text.charAt(iterator.getStart())) {
        if (DOUBLE_QUOTE_OPENERS.contains(quotePrefixType) && StringUtil.containsChar(HANDLED_BY_BRACE_MATCHER, openChar)) {
          iterator.advance();
          if (iterator.atEnd() || !QUOTE_OPEN_ANY.contains(iterator.getTokenType())) {
            EditorModificationUtil.insertStringAtCaret(editor, Character.toString(closeChar) + openChar, false, false);
          }
        }
        return Result.CONTINUE;
      }

      StringBuilder textToAppend = new StringBuilder();
      textToAppend.append(closeChar);

      if (DOUBLE_QUOTE_OPENERS.contains(quotePrefixType)) {
        textToAppend.append(openChar);
        if (openChar != closeChar) {
          textToAppend.append(closeChar);
        }
      }

      EditorModificationUtil.insertStringAtCaret(editor, textToAppend.toString(), false, false);
    }
    else if (elementTokenType == LEFT_BRACE) {
      PsiDocumentManagerBase.addRunOnCommit(document, () -> {
        PsiElement newElement = file.findElementAt(offset);
        if (PsiUtilCore.getElementType(newElement) == elementTokenType &&
            newElement.getParent() instanceof PsiPerlHashIndex) {
          AutoPopupController.getInstance(project).scheduleAutoPopup(editor);
        }
      });
    }

    return Result.CONTINUE;
  }
}
