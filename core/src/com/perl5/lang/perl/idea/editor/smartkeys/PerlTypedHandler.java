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
import com.intellij.codeInsight.CodeInsightSettings;
import com.intellij.codeInsight.completion.CompletionType;
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
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.perl.idea.codeInsight.Perl5CodeInsightSettings;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.PerlString;
import com.perl5.lang.perl.psi.PsiPerlCommaSequenceExpr;
import com.perl5.lang.perl.psi.PsiPerlHashIndex;
import com.perl5.lang.perl.psi.PsiPerlStringList;
import com.perl5.lang.perl.psi.mixins.PerlStringBareMixin;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.perl5.lang.perl.lexer.PerlTokenSets.*;

/**
 * Created by hurricup on 25.07.2015.
 */
public class PerlTypedHandler extends PerlTypedHandlerDelegate implements PerlElementTypes {
  // these chars are automatically closed by IDEA and we can't control this
  private static final String HANDLED_BY_BRACE_MATCHER = "{([";

  private static final TokenSet AUTO_OPENED_TOKENS = TokenSet.create(
    RESERVED_USE,
    RESERVED_NO,
    RESERVED_PACKAGE,
    ANNOTATION_RETURNS_KEY,
    ANNOTATION_TYPE_KEY
  );


  @NotNull
  @Override
  public Result beforeCharTyped(char c,
                                @NotNull Project project,
                                @NotNull Editor editor,
                                @NotNull PsiFile file,
                                @NotNull FileType fileType) {
    CaretModel caretModel = editor.getCaretModel();
    int currentOffset = caretModel.getOffset();
    Document document = editor.getDocument();
    CharSequence documentSequence = document.getCharsSequence();
    if (currentOffset > documentSequence.length()) {
      return Result.CONTINUE;
    }

    EditorHighlighter highlighter = ((EditorEx)editor).getHighlighter();
    HighlighterIterator iterator = highlighter.createIterator(currentOffset);
    IElementType nextTokenType = iterator.atEnd() ? null : iterator.getTokenType();
    char nextChar = currentOffset == documentSequence.length() ? 0 : documentSequence.charAt(currentOffset);
    if (c == '<' && nextTokenType == QUOTE_DOUBLE_CLOSE && nextChar == '>' &&
        currentOffset > 0 && documentSequence.charAt(currentOffset - 1) == '<' &&
        (currentOffset < 3 || PerlEditorUtil.getPreviousTokenType(highlighter.createIterator(currentOffset - 2)) != RESERVED_QQ)) {
      document.replaceString(currentOffset, currentOffset + 1, "<");
      caretModel.moveToOffset(currentOffset + 1);
      return Result.STOP;
    }

    if (QUOTE_CLOSE_FIRST_ANY.contains(nextTokenType) && c == nextChar) {
      caretModel.moveToOffset(currentOffset + 1);
      return Result.STOP;
    }

    if (c == ':' && nextTokenType == PACKAGE && Perl5CodeInsightSettings.getInstance().AUTO_INSERT_COLON) {
      document.insertString(currentOffset, "::");
      caretModel.moveToOffset(currentOffset + 2);
      AutoPopupController.getInstance(project).scheduleAutoPopup(editor);
      return Result.STOP;
    }

    if (c == ' ') {
      Result result = tryToAddFatComma(editor, file, currentOffset);
      if (result != null) {
        return result;
      }
    }

    return Result.CONTINUE;
  }

  @NotNull
  @Override
  public Result charTyped(char typedChar, @NotNull Project project, @NotNull Editor editor, @NotNull PsiFile file) {
    final int offset = editor.getCaretModel().getOffset() - 1;
    if (offset < 0) {
      return Result.CONTINUE;
    }

    EditorHighlighter highlighter = ((EditorEx)editor).getHighlighter();
    HighlighterIterator iterator = highlighter.createIterator(offset);
    IElementType elementTokenType = iterator.getTokenType();
    Document document = editor.getDocument();
    if (QUOTE_OPEN_ANY.contains(elementTokenType) && CodeInsightSettings.getInstance().AUTOINSERT_PAIR_QUOTE) {
      IElementType quotePrefixType = offset > 0 ? PerlEditorUtil.getPreviousTokenType(highlighter.createIterator(offset - 1)) : null;
      CharSequence text = document.getCharsSequence();
      if (offset > text.length() - 1 || text.charAt(offset) != typedChar) {
        return Result.CONTINUE;
      }
      if (elementTokenType == QUOTE_DOUBLE_OPEN || elementTokenType == QUOTE_SINGLE_OPEN) {
        AutoPopupController.getInstance(project).scheduleAutoPopup(editor);
      }
      char openChar = text.charAt(offset);
      char closeChar = PerlString.getQuoteCloseChar(openChar);
      iterator.advance();
      IElementType possibleCloseQuoteType = iterator.atEnd() ? null : iterator.getTokenType();
      if (QUOTE_CLOSE_FIRST_ANY.contains(possibleCloseQuoteType) && closeChar == text.charAt(iterator.getStart())) {
        if (COMPLEX_QUOTE_OPENERS.contains(quotePrefixType) && StringUtil.containsChar(HANDLED_BY_BRACE_MATCHER, openChar)) {
          iterator.advance();
          if (iterator.atEnd() || !QUOTE_OPEN_ANY.contains(iterator.getTokenType())) {
            EditorModificationUtil.insertStringAtCaret(editor, Character.toString(closeChar) + openChar, false, false);
          }
        }
        else if (SIMPLE_QUOTE_OPENERS.contains(quotePrefixType) &&
                 StringUtil.containsChar(HANDLED_BY_BRACE_MATCHER, openChar) &&
                 !PerlEditorUtil.areMarkersBalanced((EditorEx)editor, offset, openChar)) {
          EditorModificationUtil.insertStringAtCaret(editor, Character.toString(closeChar), false, false);
          return Result.STOP;
        }
        return Result.CONTINUE;
      }

      StringBuilder textToAppend = new StringBuilder();
      textToAppend.append(closeChar);

      if (COMPLEX_QUOTE_OPENERS.contains(quotePrefixType)) {
        textToAppend.append(openChar);
        if (openChar != closeChar) {
          textToAppend.append(closeChar);
        }
      }

      EditorModificationUtil.insertStringAtCaret(editor, textToAppend.toString(), false, false);
    }
    else if (elementTokenType == LEFT_BRACE) {
      AutoPopupController.getInstance(project).scheduleAutoPopup(editor, CompletionType.BASIC, psiFile -> {
        PsiElement newElement = psiFile.findElementAt(offset);
        return PsiUtilCore.getElementType(newElement) == elementTokenType &&
               newElement.getParent() instanceof PsiPerlHashIndex;
      });
    }

    return Result.CONTINUE;
  }

  @Nullable
  private Result tryToAddFatComma(@NotNull Editor editor, @NotNull PsiFile file, int offset) {
    if (!Perl5CodeInsightSettings.getInstance().SMART_COMMA_SEQUENCE_TYPING) {
      return null;
    }

    PsiElement elementAtCaret = file.findElementAt(offset);
    if (!(elementAtCaret instanceof PsiWhiteSpace)) {
      return null;
    }

    PsiElement commaSequence = elementAtCaret.getPrevSibling();
    if (!(commaSequence instanceof PsiPerlCommaSequenceExpr)) {
      return null;
    }

    PsiElement lastChild = commaSequence.getLastChild();
    IElementType lastChildElementType = PsiUtilCore.getElementType(lastChild);
    if (lastChildElementType == COMMA || lastChildElementType == FAT_COMMA) {
      return null;
    }

    PsiElement commaElement = PerlPsiUtil.getPrevSignificantSibling(lastChild);
    if (PsiUtilCore.getElementType(commaElement) != COMMA) {
      return null;
    }

    PsiElement fatCommaElement = PerlPsiUtil.getPrevSignificantSibling(PerlPsiUtil.getPrevSignificantSibling(commaElement));
    if (PsiUtilCore.getElementType(fatCommaElement) != FAT_COMMA) {
      return null;
    }

    int reformatFrom = commaSequence.getNode().getStartOffset();

    Document document = editor.getDocument();
    document.insertString(offset, "=>");
    editor.getCaretModel().moveToOffset(offset + 2);
    Project project = file.getProject();
    PsiDocumentManager.getInstance(project).commitDocument(document);
    CodeStyleManager.getInstance(project).reformatText(file, reformatFrom, offset + 2);
    AutoPopupController.getInstance(project).scheduleAutoPopup(editor);

    return Result.CONTINUE;
  }

  protected boolean shouldShowPopup(char typedChar,
                                    @NotNull Project project,
                                    @NotNull Editor editor,
                                    @NotNull PsiElement element) {
    IElementType elementType = PsiUtilCore.getElementType(element);
    return typedChar == '^' && PRE_VARIABLE_NAME_TOKENS.contains(elementType) ||
           typedChar == '>' && elementType == OPERATOR_MINUS ||
           typedChar == ':' && elementType == COLON ||
           typedChar == ' ' && (AUTO_OPENED_TOKENS.contains(elementType) ||
                                element.getParent() instanceof PerlStringBareMixin &&
                                element.getParent().getParent() instanceof PsiPerlStringList) ||
           typedChar == '{' && SIGILS.contains(elementType) ||
           StringUtil.containsChar("$@%#", typedChar);
  }
}
