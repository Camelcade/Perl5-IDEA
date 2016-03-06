/*
 * Copyright 2016 Alexandr Evstigneev
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
import com.perl5.lang.htmlmason.elementType.HTMLMasonElementTypes;
import com.perl5.lang.htmlmason.parser.psi.HTMLMasonRecursiveVisitor;
import com.perl5.lang.perl.idea.folding.PerlFoldingBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by hurricup on 06.03.2016.
 */
public class HTMLMasonFoldingBuilder extends PerlFoldingBuilder implements HTMLMasonElementTypes
{
	protected static final TokenSet COMMENT_EXCLUDED_TOKENS = TokenSet.orSet(
			PerlFoldingBuilder.COMMENT_EXCLUDED_TOKENS,
			TokenSet.create(
					HTML_MASON_BLOCK_OPENER,
					HTML_MASON_BLOCK_CLOSER,
					HTML_MASON_LINE_OPENER
			));

	@NotNull
	@Override
	public FoldingDescriptor[] buildFoldRegions(@NotNull PsiElement root, @NotNull Document document, boolean quick)
	{
		List<FoldingDescriptor> masonDescriptors = new ArrayList<FoldingDescriptor>(Arrays.asList(super.buildFoldRegions(root, document, quick)));
		root.accept(new HTMLMasonFoldingRegionsCollector(document, masonDescriptors));
		return masonDescriptors.toArray(new FoldingDescriptor[masonDescriptors.size()]);
	}

	@Nullable
	@Override
	public String getPlaceholderText(@NotNull ASTNode node, @NotNull TextRange range)
	{
		IElementType tokenType = node.getElementType();
		if (tokenType == HTML_MASON_ABSTRACT_BLOCK)
		{
			return PH_CODE_BLOCK;
		}
		else if (tokenType == HTML_MASON_ATTR_BLOCK)
		{
			return "attributes...";
		}
		else if (tokenType == HTML_MASON_ARGS_BLOCK)
		{
			return "arguments...";
		}
		else if (tokenType == HTML_MASON_TEXT_BLOCK)
		{
			return "{text block}";
		}
		return super.getPlaceholderText(node, range);
	}

	@NotNull
	@Override
	protected TokenSet getCommentExcludedTokens()
	{
		return COMMENT_EXCLUDED_TOKENS;
	}

	@Nullable
	@Override
	protected IElementType getTemplateBlockElementType()
	{
		return HTML_MASON_TEMPLATE_BLOCK_HTML;
	}

	public static class HTMLMasonFoldingRegionsCollector extends HTMLMasonRecursiveVisitor
	{
		protected final Document myDocument;
		protected List<FoldingDescriptor> myDescriptors;

		public HTMLMasonFoldingRegionsCollector(@NotNull Document document, @NotNull List<FoldingDescriptor> result)
		{
			myDocument = document;
			myDescriptors = result;
		}

		@Override
		public void visitElement(PsiElement element)
		{
			IElementType elementType = element.getNode().getElementType();

			if (elementType == HTML_MASON_ABSTRACT_BLOCK || elementType == HTML_MASON_ARGS_BLOCK || elementType == HTML_MASON_ATTR_BLOCK || elementType == HTML_MASON_TEXT_BLOCK)
			{
				addDescriptorFor(myDescriptors, myDocument, element, 0, 0, 0);
			}

			super.visitElement(element);
		}
	}

}
