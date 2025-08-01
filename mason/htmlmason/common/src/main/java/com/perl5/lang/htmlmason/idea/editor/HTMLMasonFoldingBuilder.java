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

package com.perl5.lang.htmlmason.idea.editor;

import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.htmlmason.elementType.HTMLMasonElementTypes;
import com.perl5.lang.htmlmason.parser.psi.HTMLMasonRecursiveVisitor;
import com.perl5.lang.perl.idea.folding.PerlFoldingBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.perl5.lang.perl.util.PerlUtil.mutableList;


public class HTMLMasonFoldingBuilder extends AbstractMasonFoldingBuilder {
  protected static final TokenSet COMMENT_EXCLUDED_TOKENS = TokenSet.orSet(
    PerlFoldingBuilder.COMMENT_EXCLUDED_TOKENS,
    TokenSet.create(
      HTMLMasonElementTypes.HTML_MASON_BLOCK_OPENER,
      HTMLMasonElementTypes.HTML_MASON_BLOCK_CLOSER,
      HTMLMasonElementTypes.HTML_MASON_PERL_OPENER,
      HTMLMasonElementTypes.HTML_MASON_PERL_CLOSER,
      HTMLMasonElementTypes.HTML_MASON_LINE_OPENER,
      HTMLMasonElementTypes.HTML_MASON_TEMPLATE_BLOCK_HTML
    ));

  protected static final TokenSet FOLDABLE_TOKENS = TokenSet.create(
    HTMLMasonElementTypes.HTML_MASON_INIT_BLOCK,
    HTMLMasonElementTypes.HTML_MASON_SHARED_BLOCK,
    HTMLMasonElementTypes.HTML_MASON_ONCE_BLOCK,
    HTMLMasonElementTypes.HTML_MASON_CLEANUP_BLOCK,
    HTMLMasonElementTypes.HTML_MASON_ARGS_BLOCK,
    HTMLMasonElementTypes.HTML_MASON_ATTR_BLOCK,
    HTMLMasonElementTypes.HTML_MASON_TEXT_BLOCK,
    HTMLMasonElementTypes.HTML_MASON_FILTER_BLOCK,
    HTMLMasonElementTypes.HTML_MASON_FILTERED_BLOCK
  );

  @Override
  public FoldingDescriptor @NotNull [] buildFoldRegions(@NotNull PsiElement root, @NotNull Document document, boolean quick) {
    List<FoldingDescriptor> masonDescriptors = mutableList(super.buildFoldRegions(root, document, quick));
    root.accept(new HTMLMasonFoldingRegionsCollector(document, masonDescriptors));
    return masonDescriptors.toArray(FoldingDescriptor.EMPTY_ARRAY);
  }

  @Override
  public @Nullable String getPlaceholderText(@NotNull ASTNode node, @NotNull TextRange range) {
    IElementType tokenType = node.getElementType();
    if (tokenType == HTMLMasonElementTypes.HTML_MASON_ATTR_BLOCK) {
      return "/ attributes /";
    }
    else if (tokenType == HTMLMasonElementTypes.HTML_MASON_BLOCK) {
      return PH_CODE_BLOCK;
    }
    else if (tokenType == HTMLMasonElementTypes.HTML_MASON_ARGS_BLOCK) {
      return "/ arguments /";
    }
    else if (tokenType == HTMLMasonElementTypes.HTML_MASON_TEXT_BLOCK) {
      return "/ text block /";
    }
    else if (tokenType == HTMLMasonElementTypes.HTML_MASON_FILTERED_BLOCK) {
      return "/ filtered content /";
    }
    else if (tokenType == HTMLMasonElementTypes.HTML_MASON_FILTER_BLOCK) {
      return "/ content filter /";
    }
    else if (tokenType == HTMLMasonElementTypes.HTML_MASON_ONCE_BLOCK) {
      return "/ top level shared code /";
    }
    else if (tokenType == HTMLMasonElementTypes.HTML_MASON_SHARED_BLOCK) {
      return "/ shared code /";
    }
    else if (tokenType == HTMLMasonElementTypes.HTML_MASON_INIT_BLOCK) {
      return "/ init code /";
    }
    else if (tokenType == HTMLMasonElementTypes.HTML_MASON_CLEANUP_BLOCK) {
      return "/ cleanup code /";
    }
    return super.getPlaceholderText(node, range);
  }

  @Override
  protected @NotNull TokenSet getCommentExcludedTokens() {
    return COMMENT_EXCLUDED_TOKENS;
  }

  public static class HTMLMasonFoldingRegionsCollector extends HTMLMasonRecursiveVisitor {
    protected final Document myDocument;
    protected final List<FoldingDescriptor> myDescriptors;

    public HTMLMasonFoldingRegionsCollector(@NotNull Document document, @NotNull List<FoldingDescriptor> result) {
      myDocument = document;
      myDescriptors = result;
    }

    @Override
    public void visitElement(PsiElement element) {
      IElementType elementType = PsiUtilCore.getElementType(element);

      if (elementType == HTMLMasonElementTypes.HTML_MASON_FILTERED_BLOCK) {
        addDescriptorFor(myDescriptors, myDocument, element, 0, 0, 0, null, false);
      }
      else if (FOLDABLE_TOKENS.contains(elementType)) {
        foldElement(element, myDescriptors, myDocument);
      }

      super.visitElement(element);
    }
  }
}
