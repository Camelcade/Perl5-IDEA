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
import com.intellij.psi.util.PsiUtilCore;
import org.jetbrains.annotations.NotNull;

import static com.perl5.lang.perl.parser.PerlElementTypesGenerated.COMMENT_BLOCK;

public class PerlPodReparseableTokenType extends PerlReparseableTemplateTokenType {
  public PerlPodReparseableTokenType(@NotNull String debugName) {
    super(debugName);
  }

  @Override
  protected @NotNull TextRange getLexerConfirmationRange(@NotNull ASTNode leaf) {
    ASTNode prevLeaf = TreeUtil.prevLeaf(leaf);
    if(PsiUtilCore.getElementType(prevLeaf) == COMMENT_BLOCK){
      prevLeaf = TreeUtil.prevLeaf(prevLeaf);
    }
    ASTNode nextLeaf = TreeUtil.nextLeaf(leaf);
    int startOffset = prevLeaf == null ? leaf.getStartOffset() : prevLeaf.getStartOffset();
    int endOffset = nextLeaf == null ? leaf.getTextRange().getEndOffset() : nextLeaf.getTextRange().getEndOffset();
    return TextRange.create(startOffset, endOffset);
  }
}
