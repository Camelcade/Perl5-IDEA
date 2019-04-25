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

import com.intellij.codeInsight.editorActions.BackspaceHandlerDelegate;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.pod.lexer.PodElementTypes;
import com.perl5.lang.pod.parser.psi.PodFile;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 16.04.2016.
 */
public class PodBackspaceHandler extends BackspaceHandlerDelegate implements PodElementTypes {
  @Override
  public void beforeCharDeleted(char c, @NotNull PsiFile file, @NotNull Editor editor) {

  }

  @Override
  public boolean charDeleted(char c, @NotNull PsiFile file, @NotNull Editor editor) {
    if (file instanceof PodFile) {
      CaretModel caretModel = editor.getCaretModel();
      int offset = caretModel.getOffset();
      PsiElement element = file.findElementAt(offset);
      IElementType elementType = element == null ? null : element.getNode().getElementType();

      if (elementType == POD_ANGLE_RIGHT) {
        PsiElement formatterBlock = element.getParent();
        if (formatterBlock != null) {

          PsiElement openAngles = formatterBlock.getFirstChild();
          openAngles = openAngles == null ? null : openAngles.getNextSibling();

          if (openAngles != null) {
            int openAnglesOffset = openAngles.getTextRange().getStartOffset();

            editor.getDocument().deleteString(openAnglesOffset, openAnglesOffset + 1);
            return true;
          }
        }
      }
      else if (elementType == POD_ANGLE_LEFT) {
        PsiElement formatterBlock = element.getParent();
        if (formatterBlock != null) {

          PsiElement closeAngles = formatterBlock.getLastChild();

          if (closeAngles != null) {
            int closeAnglesOffset = closeAngles.getTextRange().getStartOffset();

            editor.getDocument().deleteString(closeAnglesOffset - 1, closeAnglesOffset);
            return true;
          }
        }
        return true;
      }
    }
    return false;
  }
}
