/*
 * Copyright 2015-2019 Alexandr Evstigneev
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

package com.perl5.lang.pod.idea.editor;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorModificationUtil;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.editor.highlighter.HighlighterIterator;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.TokenType;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.util.ObjectUtils;
import com.perl5.lang.perl.idea.editor.smartkeys.PerlTypedHandlerDelegate;
import com.perl5.lang.perl.psi.utils.PerlEditorUtils;
import com.perl5.lang.pod.lexer.PodElementTypes;
import com.perl5.lang.pod.lexer.PodTokenSets;
import com.perl5.lang.pod.parser.psi.PodElementFactory;
import com.perl5.lang.pod.psi.PsiLinkText;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.perl5.lang.pod.lexer.PodTokenSets.FORMAT_ACCEPTING_COMMANDS;

/**
 * Created by hurricup on 14.04.2016.
 */
public class PodTypedHandler extends PerlTypedHandlerDelegate implements PodElementTypes {
  private static final String POD_COMMANDS = "IBCLEFSXZ";
  private static final TokenSet POD_COMMANDS_TOKENSET = TokenSet.create(
    POD_I,
    POD_B,
    POD_C,
    POD_L,
    POD_E,
    POD_F,
    POD_S,
    POD_X,
    POD_Z
  );

  protected int extendAngles(PsiElement formatterBlock) {
    int insertedChars = 0;
    if (formatterBlock == null) {
      return insertedChars;
    }

    PsiElement openAngles = formatterBlock.getFirstChild();
    openAngles = openAngles == null ? null : openAngles.getNextSibling();

    if (openAngles == null) {
      return insertedChars;
    }

    PsiElement closeAngles = formatterBlock.getLastChild();

    if (closeAngles == null) {
      return insertedChars;
    }

    int currentLength = openAngles.getTextRange().getLength();
    if (currentLength == 1) // need to check spacing
    {
      PsiElement openSpace = openAngles.getNextSibling();
      PsiElement closeSpace = openAngles.getPrevSibling();
      PsiElement space = null;

      if (!(openSpace instanceof PsiWhiteSpace)) {
        space = PodElementFactory.getSpace(formatterBlock.getProject());
        formatterBlock.addAfter(space, openAngles);
        openSpace = null;
        insertedChars++;
      }
      if (!(closeSpace instanceof PsiWhiteSpace) || closeSpace == openSpace) {
        if (space == null) {
          space = PodElementFactory.getSpace(formatterBlock.getProject());
        }
        formatterBlock.addBefore(space, closeAngles);
        insertedChars++;
      }
    }

    assert openAngles instanceof LeafPsiElement : "Got non-leaf elements in open angles: " +
                                                  openAngles.getClass() +
                                                  "@" +
                                                  formatterBlock.getText();
    ((LeafPsiElement)openAngles).replaceWithText(openAngles.getText() + "<");
    assert closeAngles instanceof LeafPsiElement : "Got non-leaf elements in close angles: " +
                                                   closeAngles.getClass() +
                                                   "@" +
                                                   formatterBlock.getText();
    ((LeafPsiElement)closeAngles).replaceWithText(closeAngles.getText() + ">");
    return insertedChars + 2;
  }

  @NotNull
  @Override
  public Result beforeCharTyped(char c,
                                @NotNull Project project,
                                @NotNull Editor editor,
                                @NotNull PsiFile file,
                                @NotNull FileType fileType) {
    CaretModel caretModel = editor.getCaretModel();
    final int offset = caretModel.getOffset();
    final int prevCharOffset = offset - 1;
    if (c == '<') {
      if (offset > 0) {
        PsiElement element = file.findElementAt(prevCharOffset);
        IElementType elementType = PsiUtilCore.getElementType(element);
        CharSequence documentChars = editor.getDocument().getCharsSequence();

        if (elementType == POD_IDENTIFIER && StringUtil.containsChar(POD_COMMANDS, documentChars.charAt(prevCharOffset))) {
            EditorModificationUtil.insertStringAtCaret(editor, ">", false, false, 0);
        }
        else if (elementType == POD_ANGLE_LEFT || POD_COMMANDS_TOKENSET.contains(elementType)) {
          extendAngles(element.getParent());
          caretModel.moveToOffset(offset + 1);
          return Result.STOP;
        }
      }
    }
    else if (c == '>') {    // '>'
      PsiElement element = file.findElementAt(offset);
      IElementType elementType = PsiUtilCore.getElementType(element);

      if (elementType != POD_ANGLE_RIGHT) {
        element = file.findElementAt(prevCharOffset);
        elementType = PsiUtilCore.getElementType(element);
      }

      if (elementType == POD_ANGLE_RIGHT) {
        caretModel.moveToOffset(offset + extendAngles(element.getParent()));
        return Result.STOP;
      }
    }
    return super.beforeCharTyped(c, project, editor, file, fileType);
  }

  @Override
  protected boolean shouldShowPopup(char typedChar, @NotNull Project project, @NotNull Editor editor, @Nullable PsiElement element) {
    IElementType elementType = PsiUtilCore.getElementType(element);
    ASTNode elementNode = ObjectUtils.doIfNotNull(element, PsiElement::getNode);
    CharSequence elementChars = ObjectUtils.doIfNotNull(elementNode, ASTNode::getChars);
    PsiElement elementParent = ObjectUtils.doIfNotNull(element, PsiElement::getParent);
    IElementType parentType = PsiUtilCore.getElementType(elementParent);

    return typedChar == '=' && isCommandPosition(editor, elementType, elementChars) ||
           typedChar == ':' &&
           elementNode != null &&
           PerlEditorUtils.isPreviousToken(editor, elementNode.getStartOffset(), FORMAT_ACCEPTING_COMMANDS) ||
           typedChar == ' ' && PodTokenSets.POD_COMMANDS_TOKENSET.contains(elementType) ||
           typedChar == '<' && elementType == POD_IDENTIFIER && StringUtil.equals("L", elementChars) ||
           typedChar == '|' && parentType == LINK_NAME ||
           typedChar == '/' && (parentType == LINK_NAME ||
                                elementType == POD_ANGLE_LEFT && parentType == POD_FORMAT_LINK ||
                                elementType == POD_PIPE && element.getPrevSibling() instanceof PsiLinkText)
      ;
  }

  /**
   * @param elementType  type of element before caret
   * @param elementChars chars of element before caret
   * @return true iff editor is at command position: beginning of the line after 2+ new lines or at beginning of the file
   */
  private static boolean isCommandPosition(@NotNull Editor editor,
                                           @Nullable IElementType elementType,
                                           @Nullable CharSequence elementChars) {
    int offset = editor.getCaretModel().getOffset();
    if (offset == 0 || elementChars == null || elementType == null) {
      return true;
    }
    if (elementType != TokenType.WHITE_SPACE || elementChars.length() != 1 || elementChars.charAt(0) != '\n') {
      return false;
    }
    HighlighterIterator iterator = ((EditorEx)editor).getHighlighter().createIterator(offset - 1);
    while (!iterator.atEnd()) {
      IElementType tokenType = iterator.getTokenType();
      if (tokenType == POD_NEWLINE) {
        return true;
      }
      if (tokenType != TokenType.WHITE_SPACE) {
        return false;
      }
      iterator.retreat();
    }

    return true;
  }
}
