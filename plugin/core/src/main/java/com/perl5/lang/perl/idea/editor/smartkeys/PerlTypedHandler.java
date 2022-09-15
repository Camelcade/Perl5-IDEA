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
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.idea.codeInsight.Perl5CodeInsightSettings;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.impl.PerlHeredocElementImpl;
import com.perl5.lang.perl.psi.impl.PsiPerlPerlRegexImpl;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.intellij.psi.TokenType.WHITE_SPACE;
import static com.perl5.lang.perl.lexer.PerlTokenSets.*;


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


  @Override
  public @NotNull Result beforeCharTyped(char c,
                                         @NotNull Project project,
                                         @NotNull Editor editor,
                                         @NotNull PsiFile file,
                                         @NotNull FileType fileType) {
    if (!isMyFile(file)) {
      return Result.CONTINUE;
    }
    CaretModel caretModel = editor.getCaretModel();
    int currentOffset = caretModel.getOffset();
    Document document = editor.getDocument();
    CharSequence documentSequence = document.getCharsSequence();
    if (currentOffset > documentSequence.length()) {
      return Result.CONTINUE;
    }

    EditorHighlighter highlighter = ((EditorEx)editor).getHighlighter();
    HighlighterIterator nextPositionIterator = highlighter.createIterator(currentOffset);
    IElementType nextTokenType = PerlEditorUtil.getTokenType(nextPositionIterator);
    int nextTokenLength = PerlEditorUtil.getTokenLength(nextPositionIterator);

    HighlighterIterator prevPositionIterator = currentOffset > 0 ? highlighter.createIterator(currentOffset - 1) : null;
    IElementType prevTokenType = PerlEditorUtil.getTokenType(prevPositionIterator);
    int prevTokenLength = PerlEditorUtil.getTokenLength(prevPositionIterator);

    if (c == '<' && prevTokenType == LEFT_ANGLE && prevTokenLength == 1 && nextTokenType == RIGHT_ANGLE && nextTokenLength == 1) {
      document.replaceString(currentOffset, currentOffset + 1, "<");
      caretModel.moveToOffset(currentOffset + 1);
      return Result.STOP;
    }

    char nextChar = currentOffset == documentSequence.length() ? 0 : documentSequence.charAt(currentOffset);
    if (QUOTE_CLOSE_FIRST_ANY.contains(nextTokenType) && c == nextChar) {
      caretModel.moveToOffset(currentOffset + 1);
      return Result.STOP;
    }

    if (c == ':') {
      if (Perl5CodeInsightSettings.getInstance().AUTO_INSERT_COLON && currentOffset > 0 && shouldAddColon(prevPositionIterator, file)) {
        if (documentSequence.charAt(currentOffset - 1) != ':') {
          document.insertString(currentOffset, "::");
          caretModel.moveToOffset(currentOffset + 2);
        }
        AutoPopupController.getInstance(project).scheduleAutoPopup(editor);
        return Result.STOP;
      }
      else if (currentOffset > 1 && documentSequence.charAt(currentOffset - 1) == ':') {
        AutoPopupController.getInstance(project).scheduleAutoPopup(editor);
      }
    }

    if (c == ' ') {
      Result result = tryToAddFatComma(editor, file, currentOffset);
      if (result != null) {
        return result;
      }
    }

    if (c == '>' && PerlEditorUtil.getPreviousTokenType(prevPositionIterator, true) == OPERATOR_DEREFERENCE) {
      AutoPopupController.getInstance(project).scheduleAutoPopup(editor);
      return Result.STOP;
    }

    return Result.CONTINUE;
  }

  static final TokenSet COLON_HANDLING_VARIABLE_TOKENS = TokenSet.orSet(SIGILS, VARIABLE_NAMES);

  /**
   * @return true if we are at proper place for inserting two colons
   */
  private boolean shouldAddColon(@NotNull HighlighterIterator precedingIterator,
                                 @NotNull PsiFile psiFile) {
    IElementType tokenType = precedingIterator.getTokenType();
    if (isPreColonSuffixBase(tokenType, precedingIterator.getStart(), psiFile)) {
      return true;
    }
    else if (tokenType != SUB_NAME) {
      return false;
    }
    precedingIterator.retreat();
    return !precedingIterator.atEnd() && precedingIterator.getTokenType() == QUALIFYING_PACKAGE;
  }

  /**
   * @return {@code true} iff we should add/remove colon after the token starting at {@code elementOffset} in {@code psiFile} with
   * {@code tokenType}.
   */
  public static boolean isPreColonSuffixBase(@Nullable IElementType tokenType,
                                             int elementOffset,
                                             @NotNull PsiFile psiFile) {
    if (PACKAGE_LIKE_TOKENS.contains(tokenType)) {
      return true;
    }
    else if (COLON_HANDLING_VARIABLE_TOKENS.contains(tokenType)) {
      var perlLeafAtOffset = psiFile.getViewProvider().findElementAt(elementOffset, PerlLanguage.INSTANCE);
      var perlVariable = perlLeafAtOffset == null ? null : perlLeafAtOffset.getParent();
      var variableContainer = perlVariable == null ? null : perlVariable.getParent();
      return !(variableContainer instanceof PerlString ||
               variableContainer instanceof PerlHeredocElementImpl ||
               variableContainer instanceof PsiPerlPerlRegexImpl);
    }
    return false;
  }

  private boolean isMyFile(@NotNull PsiFile file) {
    return file.getLanguage().isKindOf(PerlLanguage.INSTANCE);
  }

  @Override
  public @NotNull Result charTyped(char typedChar, @NotNull Project project, @NotNull Editor editor, @NotNull PsiFile file) {
    if (!isMyFile(file)) {
      return Result.CONTINUE;
    }
    final int currentOffset = editor.getCaretModel().getOffset();
    final int offset = currentOffset - 1;
    if (offset < 0) {
      return Result.CONTINUE;
    }

    Perl5CodeInsightSettings perlCodeInsightSettings = Perl5CodeInsightSettings.getInstance();
    EditorHighlighter highlighter = ((EditorEx)editor).getHighlighter();
    HighlighterIterator iterator = highlighter.createIterator(offset);
    IElementType elementTokenType = iterator.getTokenType();
    Document document = editor.getDocument();
    CharSequence text = document.getCharsSequence();
    if (QUOTE_OPEN_ANY.contains(elementTokenType) && CodeInsightSettings.getInstance().AUTOINSERT_PAIR_QUOTE) {
      IElementType quotePrefixType = offset > 0 ? PerlEditorUtil.getPreviousTokenType(highlighter.createIterator(offset - 1), false) : null;
      if (offset > text.length() - 1 || text.charAt(offset) != typedChar) {
        return Result.CONTINUE;
      }
      if (elementTokenType == QUOTE_DOUBLE_OPEN || elementTokenType == QUOTE_SINGLE_OPEN) {
        AutoPopupController.getInstance(project).scheduleAutoPopup(editor);
      }
      char openChar = text.charAt(offset);
      char closeChar = PerlString.getQuoteCloseChar(openChar);
      iterator.advance();
      IElementType possibleCloseQuoteType = PerlEditorUtil.getTokenType(iterator);
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
    else if (elementTokenType == STRING_SPECIAL_HEX && perlCodeInsightSettings.AUTO_BRACE_HEX_SUBSTITUTION ||
             elementTokenType == STRING_SPECIAL_OCT && perlCodeInsightSettings.AUTO_BRACE_OCT_SUBSTITUTION) {
      EditorModificationUtil.insertStringAtCaret(editor, "{}", false, 1);
    }
    else if (elementTokenType == STRING_SPECIAL_UNICODE) {
      EditorModificationUtil.insertStringAtCaret(editor, "{}", false, 1);
      AutoPopupController.getInstance(project).scheduleAutoPopup(editor, CompletionType.BASIC, null);
    }
    else if (elementTokenType == LEFT_BRACE) {
      AutoPopupController.getInstance(project).scheduleAutoPopup(editor, CompletionType.BASIC, psiFile -> {
        PsiElement newElement = psiFile.findElementAt(offset);
        return PsiUtilCore.getElementType(newElement) == elementTokenType &&
               newElement.getParent() instanceof PsiPerlHashIndex;
      });
    }
    else if (typedChar == '=' && !iterator.atEnd()) {
      handleEqualSign(editor, file, offset, iterator, document);
    }
    else if (typedChar == '-' && offset > 0) {
      iterator.retreat();
      if (PerlEditorUtil.getPreviousTokenType(iterator, true) == PACKAGE &&
          (currentOffset == text.length() || text.charAt(currentOffset) != '>')) {
        EditorModificationUtil.insertStringAtCaret(editor, ">");
        AutoPopupController.getInstance(project).scheduleAutoPopup(editor, CompletionType.BASIC, null);
      }
    }

    return Result.CONTINUE;
  }

  private void handleEqualSign(@NotNull Editor editor,
                               @NotNull PsiFile file,
                               int offset,
                               HighlighterIterator iterator,
                               Document document) {
    int previousNonSpaceTokenStart = -1;
    while (true) {
      iterator.retreat();
      if (iterator.atEnd()) {
        return;
      }
      if (iterator.getTokenType() != WHITE_SPACE) {
        previousNonSpaceTokenStart = iterator.getStart();
        break;
      }
    }

    PsiElement elementAtOffset = file.findElementAt(previousNonSpaceTokenStart);
    PsiPerlSignatureContent wrappingSignature = PsiTreeUtil.getParentOfType(elementAtOffset, PsiPerlSignatureContent.class);
    if (wrappingSignature != null) {
      int signatureOffset = wrappingSignature.getNode().getStartOffset();
      EditorModificationUtil.insertStringAtCaret(editor, " ", false, true);
      Project project = file.getProject();
      PsiDocumentManager.getInstance(project).commitDocument(document);
      CodeStyleManager.getInstance(project).reformatText(file, signatureOffset, offset);
      AutoPopupController.getInstance(project).scheduleAutoPopup(editor);
    }
  }

  private @Nullable Result tryToAddFatComma(@NotNull Editor editor, @NotNull PsiFile file, int offset) {
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

  @Override
  protected boolean shouldShowPopup(char typedChar,
                                    @NotNull Project project,
                                    @NotNull Editor editor,
                                    @Nullable PsiElement element) {
    IElementType elementType = PsiUtilCore.getElementType(element);
    return typedChar == '^' && PRE_VARIABLE_NAME_TOKENS.contains(elementType) ||
           typedChar == '>' && elementType == OPERATOR_MINUS ||
           typedChar == ':' && elementType == COLON ||
           typedChar == ' ' && (AUTO_OPENED_TOKENS.contains(elementType) || PerlStringList.isListElement(element)) ||
           typedChar == '{' && (elementType == STRING_SPECIAL_UNICODE || SIGILS.contains(elementType)) ||
           StringUtil.containsChar("$@%#", typedChar);
  }
}
