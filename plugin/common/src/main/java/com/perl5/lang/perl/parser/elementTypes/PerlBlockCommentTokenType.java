/*
 * Copyright 2015-2025 Alexandr Evstigneev
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
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.impl.source.tree.TreeUtil;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiUtilCore;
import org.jetbrains.annotations.NotNull;

import static com.perl5.lang.perl.parser.PerlElementTypesGenerated.TAG_DATA;
import static com.perl5.lang.perl.parser.PerlElementTypesGenerated.TAG_END;

public class PerlBlockCommentTokenType extends PerlReparseableTokenType {
  public PerlBlockCommentTokenType(@NotNull String debugName) {
    super(debugName);
  }

  @Override
  protected @NotNull TextRange getLexerConfirmationRange(@NotNull ASTNode leaf) {
    ASTNode dataNode = TreeUtil.prevLeaf(leaf);
    IElementType dataNodeType = PsiUtilCore.getElementType(dataNode);
    if (dataNodeType != TAG_DATA && dataNodeType != TAG_END) {
      return TextRange.EMPTY_RANGE;
    }
    ASTNode nextLeaf = TreeUtil.nextLeaf(leaf);
    int endOffset = nextLeaf == null ? leaf.getTextRange().getEndOffset() :
                    nextLeaf.getTextRange().getEndOffset();

    return TextRange.create(dataNode.getStartOffset(), endOffset);
  }
}
