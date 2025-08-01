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

package com.perl5.lang.mason2.idea.folding;

import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.htmlmason.idea.editor.AbstractMasonFoldingBuilder;
import com.perl5.lang.mason2.elementType.Mason2ElementTypes;
import com.perl5.lang.mason2.psi.Mason2RecursiveVisitor;
import com.perl5.lang.mason2.psi.MasonAbstractBlock;
import com.perl5.lang.mason2.psi.MasonTextBlock;
import com.perl5.lang.perl.idea.folding.PerlFoldingBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.perl5.lang.perl.util.PerlUtil.mutableList;


public class MasonFoldingBuilder extends AbstractMasonFoldingBuilder {
  protected static final TokenSet COMMENT_EXCLUDED_TOKENS = TokenSet.orSet(
    PerlFoldingBuilder.COMMENT_EXCLUDED_TOKENS,
    TokenSet.create(
      Mason2ElementTypes.MASON_BLOCK_OPENER,
      Mason2ElementTypes.MASON_BLOCK_CLOSER,
      Mason2ElementTypes.MASON_LINE_OPENER,
      Mason2ElementTypes.MASON_TEMPLATE_BLOCK_HTML
    ));

  @Override
  public FoldingDescriptor @NotNull [] buildFoldRegions(@NotNull PsiElement root, @NotNull Document document, boolean quick) {
    List<FoldingDescriptor> masonDescriptors = mutableList(super.buildFoldRegions(root, document, quick));
    root.accept(new MasonFoldingRegionsCollector(document, masonDescriptors));
    return masonDescriptors.toArray(FoldingDescriptor.EMPTY_ARRAY);
  }

  @Override
  public @Nullable String getPlaceholderText(@NotNull ASTNode node, @NotNull TextRange range) {
    IElementType tokenType = node.getElementType();
    if (tokenType == Mason2ElementTypes.MASON_ABSTRACT_BLOCK) {
      return PH_CODE_BLOCK;
    }
    else if (tokenType == Mason2ElementTypes.MASON_FILTERED_BLOCK) {
      return "{filtered block}";
    }
    else if (tokenType == Mason2ElementTypes.MASON_TEXT_BLOCK) {
      return "{text block}";
    }
    return super.getPlaceholderText(node, range);
  }

  @Override
  protected @NotNull TokenSet getCommentExcludedTokens() {
    return COMMENT_EXCLUDED_TOKENS;
  }

  public static class MasonFoldingRegionsCollector extends Mason2RecursiveVisitor {
    protected final Document myDocument;
    protected final List<FoldingDescriptor> myDescriptors;

    public MasonFoldingRegionsCollector(@NotNull Document document, @NotNull List<FoldingDescriptor> result) {
      myDocument = document;
      myDescriptors = result;
    }

    @Override
    public void visitMasonAbstractBlock(@NotNull MasonAbstractBlock o) {
      foldElement(o, myDescriptors, myDocument);
      super.visitMasonAbstractBlock(o);
    }

    @Override
    public void visitMasonTextBlock(@NotNull MasonTextBlock o) {
      foldElement(o, myDescriptors, myDocument);
      super.visitMasonTextBlock(o);
    }
  }
}
