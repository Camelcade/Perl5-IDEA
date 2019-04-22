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

package com.perl5.lang.pod.idea.editor;

import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorModificationUtil;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.perl.idea.editor.smartkeys.PerlTypedHandlerDelegate;
import com.perl5.lang.pod.lexer.PodElementTypes;
import com.perl5.lang.pod.parser.psi.PodElementFactory;
import com.perl5.lang.pod.parser.psi.PodFile;
import com.perl5.lang.pod.psi.PsiLinkText;
import org.jetbrains.annotations.NotNull;

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
    if (file instanceof PodFile) {
      if (c == '<') {
        CaretModel caretModel = editor.getCaretModel();
        int offset = caretModel.getOffset() - 1;
        PsiElement element = file.findElementAt(offset);
        IElementType elementType = element == null ? null : element.getNode().getElementType();
        String text = element == null ? null : element.getText();

        if (elementType == POD_IDENTIFIER && text != null) {

          if (text.length() == 1 && StringUtil.containsChar(POD_COMMANDS, text.charAt(0))) {
            EditorModificationUtil.insertStringAtCaret(editor, ">", false, false, 0);
          }
        }
        else if (elementType == POD_ANGLE_LEFT || POD_COMMANDS_TOKENSET.contains(elementType)) {
          extendAngles(element.getParent());
          caretModel.moveToOffset(offset + 2);
          return Result.STOP;
        }
      }
      else if (c == '>')    // '>'
      {
        CaretModel caretModel = editor.getCaretModel();
        int offset = caretModel.getOffset();
        PsiElement element = file.findElementAt(offset);
        IElementType elementType = element == null ? null : element.getNode().getElementType();
        int extraOffset = 0;

        if (elementType != POD_ANGLE_RIGHT) {
          offset--;
          element = file.findElementAt(offset);
          elementType = element == null ? null : element.getNode().getElementType();
          extraOffset++;
        }

        if (elementType == POD_ANGLE_RIGHT) {
          caretModel.moveToOffset(offset + extendAngles(element.getParent()) + extraOffset);
          return Result.STOP;
        }
      }
    }
    return super.beforeCharTyped(c, project, editor, file, fileType);
  }

  @Override
  protected boolean shouldShowPopup(char typedChar, @NotNull Project project, @NotNull Editor editor, @NotNull PsiElement element) {
    IElementType elementType = PsiUtilCore.getElementType(element);
    CharSequence elementChars = element.getNode().getChars();
    PsiElement elementParent = element.getParent();
    IElementType parentType = PsiUtilCore.getElementType(elementParent);

    return typedChar == '<' && elementType == POD_IDENTIFIER && StringUtil.equals("L", elementChars) ||
           typedChar == '|' && parentType == LINK_NAME ||
           typedChar == '/' && (parentType == LINK_NAME ||
                                elementType == POD_ANGLE_LEFT && parentType == POD_FORMAT_LINK ||
                                elementType == POD_PIPE && element.getPrevSibling() instanceof PsiLinkText)
      ;
  }
}
