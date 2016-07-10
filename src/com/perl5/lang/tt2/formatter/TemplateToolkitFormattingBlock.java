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

package com.perl5.lang.tt2.formatter;

import com.intellij.formatting.*;
import com.intellij.formatting.templateLanguages.DataLanguageBlockWrapper;
import com.intellij.formatting.templateLanguages.TemplateLanguageBlock;
import com.intellij.formatting.templateLanguages.TemplateLanguageBlockFactory;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.tt2.elementTypes.TemplateToolkitElementTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by hurricup on 10.07.2016.
 */
public class TemplateToolkitFormattingBlock extends TemplateLanguageBlock implements TemplateToolkitElementTypes
{
	// getChildIndent being asked on try-catch, not branches
	private static final TokenSet INDENTED_CONTAINERS_PARENTS = TokenSet.create(
			TRY_CATCH_BLOCK,
			IF_BLOCK,
			UNLESS_BLOCK
	);

	private static final TokenSet INDENTED_CONTAINERS = TokenSet.create(
			IF_BRANCH,
			UNLESS_BRANCH,
			ELSIF_BRANCH,
			ELSE_BRANCH,

			TRY_BRANCH,
			CATCH_BRANCH,
			FINAL_BRANCH
	);

	private static final TokenSet CHILD_INDENTED_CONTAINERS = TokenSet.orSet(
			INDENTED_CONTAINERS,
			INDENTED_CONTAINERS_PARENTS
	);

	public TemplateToolkitFormattingBlock(
			@NotNull TemplateLanguageBlockFactory blockFactory,
			@NotNull CodeStyleSettings settings,
			@NotNull ASTNode node,
			@Nullable List<DataLanguageBlockWrapper> foreignChildren
	)
	{
		super(blockFactory, settings, node, foreignChildren);
	}

	public TemplateToolkitFormattingBlock(
			@NotNull ASTNode node,
			@Nullable Wrap wrap,
			@Nullable Alignment alignment,
			@NotNull TemplateLanguageBlockFactory blockFactory,
			@NotNull CodeStyleSettings settings,
			@Nullable List<DataLanguageBlockWrapper> foreignChildren
	)
	{
		super(node, wrap, alignment, blockFactory, settings, foreignChildren);
	}

	@Override
	protected IElementType getTemplateTextElementType()
	{
		return TT2_HTML;
	}

	@Override
	public Indent getIndent()
	{
//		IElementType elementType = myNode.getElementType();
		boolean isFirst = myNode.getTreePrev() == null;
		ASTNode parentNode = myNode.getTreeParent();
		IElementType parentNodeType = parentNode == null ? null : parentNode.getElementType();

		if (!isFirst && INDENTED_CONTAINERS.contains(parentNodeType))
		{
			return Indent.getNormalIndent();
		}

		return Indent.getNoneIndent();
	}

	@Nullable
	@Override
	protected Indent getChildIndent()
	{
		IElementType elementType = myNode.getElementType();
		if (CHILD_INDENTED_CONTAINERS.contains(elementType))
		{
			return Indent.getNormalIndent();
		}
		return super.getChildIndent();
	}

	@Nullable
	@Override
	public Alignment getAlignment()
	{
		return super.getAlignment();
	}

	@NotNull
	@Override
	public ChildAttributes getChildAttributes(int newChildIndex)
	{
		return super.getChildAttributes(newChildIndex);
	}

	@Override
	public boolean isRequiredRange(TextRange range)
	{
		return false;
	}

	@Override
	public List<DataLanguageBlockWrapper> getForeignChildren()
	{
		return super.getForeignChildren();
	}

	@Override
	protected List<Block> buildChildren()
	{
		return super.buildChildren();
	}
}
