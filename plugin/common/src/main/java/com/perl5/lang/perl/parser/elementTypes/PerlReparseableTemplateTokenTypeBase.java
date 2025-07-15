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

package com.perl5.lang.perl.parser.elementTypes;

import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.TokenType;
import com.intellij.psi.impl.source.tree.TreeUtil;
import com.intellij.psi.util.PsiUtilCore;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class PerlReparseableTemplateTokenTypeBase extends PerlReparseableTemplateTokenType {
  public PerlReparseableTemplateTokenTypeBase(@NotNull String debugName,
                                              @NotNull Class<? extends ASTNode> clazz,
                                              @Nullable Language language) {
    super(debugName, clazz, language);
  }

  @Override
  protected @NotNull TextRange getLexerConfirmationRange(@NotNull ASTNode leaf) {
    ASTNode nextLeaf = TreeUtil.nextLeaf(leaf);
    while (PsiUtilCore.getElementType(nextLeaf) == TokenType.WHITE_SPACE) {
      ASTNode lookAheadLeaf = TreeUtil.nextLeaf(nextLeaf);
      if (lookAheadLeaf == null) {
        break;
      }
      nextLeaf = lookAheadLeaf;
    }
    int endOffset = nextLeaf == null ? leaf.getTextRange().getEndOffset() : nextLeaf.getTextRange().getEndOffset();
    return TextRange.create(leaf.getStartOffset(), endOffset);
  }
}
