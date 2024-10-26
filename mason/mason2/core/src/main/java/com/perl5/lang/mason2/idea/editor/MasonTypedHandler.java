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

package com.perl5.lang.mason2.idea.editor;

import com.intellij.codeInsight.editorActions.TypedHandlerDelegate;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorModificationUtilEx;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.xml.XmlTokenType;
import com.perl5.lang.mason2.elementType.Mason2ElementTypes;
import com.perl5.lang.mason2.psi.Mason2TemplatingFileViewProvider;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Map;


public class MasonTypedHandler extends TypedHandlerDelegate implements Mason2ElementTypes, XmlTokenType, PerlElementTypes {
  private static final Map<String, String> SIMPLE_COMPLETION_MAP = Map.of(
    KEYWORD_DOC_OPENER_UNCLOSED, KEYWORD_DOC_CLOSER,
    KEYWORD_CLASS_OPENER_UNCLOSED, KEYWORD_CLASS_CLOSER,
    KEYWORD_INIT_OPENER_UNCLOSED, KEYWORD_INIT_CLOSER,
    KEYWORD_PERL_OPENER_UNCLOSED, KEYWORD_PERL_CLOSER,
    KEYWORD_TEXT_OPENER_UNCLOSED, KEYWORD_TEXT_CLOSER);

  @Override
  public @NotNull Result charTyped(char c, final @NotNull Project project, final @NotNull Editor editor, @NotNull PsiFile file) {
    if (!(file.getViewProvider() instanceof Mason2TemplatingFileViewProvider)) {
      return super.charTyped(c, project, editor, file);
    }
    if (c == '>') {
      handleRightAngle(editor, file);
    }
    else if (c == ' ') {
      handleSpace(editor, file);
    }
    else if (c == '{') {
      handleLeftBrace(editor, file);
    }
    return super.charTyped(c, project, editor, file);
  }

  private void handleLeftBrace(@NotNull Editor editor, @NotNull PsiFile file) {
    int offset = editor.getCaretModel().getOffset();
    PsiElement element = file.findElementAt(offset - 2);
    if (element == null || element.getNode().getElementType() != LEFT_BRACE) {
      return;
    }
    Document document = editor.getDocument();
    int lineNumber = document.getLineNumber(offset - 1);
    PsiElement lineStartElement = file.findElementAt(document.getLineStartOffset(lineNumber));

    if (lineStartElement != null && lineStartElement.getNode().getElementType() == MASON_LINE_OPENER) {
      PsiElement nextElement = file.findElementAt(offset - 1);
      if (nextElement != null && nextElement.getNode().getElementType() == RIGHT_BRACE) {
        EditorModificationUtilEx.insertStringAtCaret(editor, "\n\n% ", false, true, 1);
      }
    }
  }

  private void handleSpace(@NotNull Editor editor, @NotNull PsiFile file) {
    PsiElement element = file.findElementAt(editor.getCaretModel().getOffset() - 2);
    if (element == null) {
      return;
    }
    IElementType elementType = element.getNode().getElementType();
    if (elementType == MASON_BLOCK_OPENER && !isNextElement(element, MASON_BLOCK_CLOSER)) {
      EditorModificationUtilEx.insertStringAtCaret(editor, KEYWORD_BLOCK_CLOSER, false, false);
    }
    else if (elementType == MASON_CALL_OPENER && !isNextElement(element, MASON_CALL_CLOSER)) {
      EditorModificationUtilEx.insertStringAtCaret(editor, KEYWORD_CALL_CLOSER, false, false);
    }
    else if (elementType == MASON_METHOD_OPENER) {
      EditorModificationUtilEx.insertStringAtCaret(editor, ">\n" + KEYWORD_METHOD_CLOSER, false, false);
    }
    else if (elementType == MASON_FILTER_OPENER) {
      EditorModificationUtilEx.insertStringAtCaret(editor, ">\n" + KEYWORD_FILTER_CLOSER, false, false);
    }
    else if (elementType == MASON_OVERRIDE_OPENER) {
      EditorModificationUtilEx.insertStringAtCaret(editor, ">\n" + KEYWORD_OVERRIDE_CLOSER, false, false);
    }
  }

  private void handleRightAngle(@NotNull Editor editor, @NotNull PsiFile file) {
    PsiElement element = file.findElementAt(editor.getCaretModel().getOffset() - 2);
    if (element == null || element.getNode().getElementType() != XML_DATA_CHARACTERS) {
      return;
    }
    String elementText = element.getText();
    String closeTag;
    if ((closeTag = SIMPLE_COMPLETION_MAP.get(elementText)) != null) {
      EditorModificationUtilEx.insertStringAtCaret(editor, closeTag, false, false);
    }
    else if (elementText.equals(KEYWORD_FLAGS_OPENER_UNCLOSED)) {
      EditorModificationUtilEx.insertStringAtCaret(editor, " extends => '' " + KEYWORD_FLAGS_CLOSER, false, true, 13);
    }
  }

  protected boolean isNextElement(PsiElement element, IElementType typeToCheck) {
    PsiElement nextElement = PerlPsiUtil.getNextSignificantSibling(element);
    return nextElement != null && nextElement.getNode().getElementType() == typeToCheck;
  }
}
