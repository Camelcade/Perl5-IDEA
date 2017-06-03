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

package com.perl5.lang.mojolicious.idea.editor.smartkeys;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorModificationUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.mojolicious.MojoliciousElementTypes;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 09.01.2016.
 */
public class MojoliciousSmartKeysUtil implements MojoliciousElementTypes, PerlElementTypes {
  private static final TokenSet CLOSE_TOKENS = TokenSet.create(
    MOJO_BLOCK_CLOSER,
    MOJO_BLOCK_NOSPACE_CLOSER,
    MOJO_BLOCK_EXPR_CLOSER,
    MOJO_BLOCK_EXPR_NOSPACE_CLOSER
  );

  public static boolean addCloseMarker(@NotNull final Editor editor, @NotNull PsiFile file, @NotNull String marker) {
    PsiElement element = file.findElementAt(editor.getCaretModel().getOffset() - 2);
    IElementType elementType = PsiUtilCore.getElementType(element);
    if (elementType == MOJO_BLOCK_OPENER || elementType == MOJO_BLOCK_EXPR_OPENER || elementType == MOJO_BLOCK_EXPR_ESCAPED_OPENER) {
      ASTNode nextSibling = PerlPsiUtil.getNextSignificantSibling(element.getNode());
      if (nextSibling == null || !CLOSE_TOKENS.contains(nextSibling.getElementType())) {
        EditorModificationUtil.insertStringAtCaret(editor, marker, false, false);
        return true;
      }
    }
    return false;
  }

  public static boolean addEndMarker(@NotNull final Editor editor, @NotNull PsiFile file, @NotNull String marker) {
    PsiElement element = file.findElementAt(editor.getCaretModel().getOffset() - 2);
    IElementType elementType = PsiUtilCore.getElementType(element);
    if (elementType == MOJO_BEGIN) {
      ASTNode nextSibling = PerlPsiUtil.getNextSignificantSibling(element.getNode());
      if (nextSibling == null || nextSibling.getElementType() != BLOCK ||
          nextSibling.getTreeNext() == null || nextSibling.getTreeNext().getElementType() != MOJO_END
        ) {
        EditorModificationUtil.insertStringAtCaret(editor, marker, false, false);
        return true;
      }
    }
    return false;
  }
}
