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
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.formatter.common.InjectedLanguageBlockBuilder;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.tt2.elementTypes.TemplateToolkitElementTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by hurricup on 10.07.2016.
 */
public class TemplateToolkitFormattingBlock extends TemplateLanguageBlock implements TemplateToolkitElementTypes
{
	// fixme return static modifier before release, made for hot swap
	// getChildIndent being asked on try-catch, not branches
	private final TokenSet NORMAL_INDENTED_CONTAINERS_PARENTS = TokenSet.create(
			TRY_CATCH_BLOCK,
			IF_BLOCK,
			UNLESS_BLOCK
	);

	private final TokenSet NORMAL_INDENTED_CONTAINERS_WITH_CLOSE_TAG = TokenSet.create(
			ANON_BLOCK,
			NAMED_BLOCK,

			FILTER_BLOCK,
			FOREACH_BLOCK,
			WHILE_BLOCK
	);

	private final TokenSet NORMAL_INDENTED_CONTAINERS = TokenSet.create(
			IF_BRANCH,
			UNLESS_BRANCH,
			ELSIF_BRANCH,
			ELSE_BRANCH,

			TRY_BRANCH,
			CATCH_BRANCH,
			FINAL_BRANCH,

			DEFAULT_DIRECTIVE,
			SET_DIRECTIVE
	);

	private final TokenSet NORMAL_CHILD_INDENTED_CONTAINERS = TokenSet.orSet(
			NORMAL_INDENTED_CONTAINERS,
			NORMAL_INDENTED_CONTAINERS_WITH_CLOSE_TAG,
			NORMAL_INDENTED_CONTAINERS_PARENTS
	);

	private final TemplateToolkitFormattingModelBuilder myModelBuilder;
	private final SpacingBuilder mySpacingBuilder;
	private final InjectedLanguageBlockBuilder myInjectedLanguageBlockBuilder;

	public TemplateToolkitFormattingBlock(
			@NotNull TemplateToolkitFormattingModelBuilder blockFactory,
			@NotNull CodeStyleSettings settings,
			@NotNull ASTNode node,
			@Nullable List<DataLanguageBlockWrapper> foreignChildren
	)
	{
		this(node, null, null, blockFactory, settings, foreignChildren);
	}

	public TemplateToolkitFormattingBlock(
			@NotNull ASTNode node,
			@Nullable Wrap wrap,
			@Nullable Alignment alignment,
			@NotNull TemplateToolkitFormattingModelBuilder blockFactory,
			@NotNull CodeStyleSettings settings,
			@Nullable List<DataLanguageBlockWrapper> foreignChildren
	)
	{
		super(node, wrap, alignment, blockFactory, settings, foreignChildren);
		myModelBuilder = blockFactory;
		mySpacingBuilder = myModelBuilder.getSpacingBuilder();
		myInjectedLanguageBlockBuilder = myModelBuilder.getInjectedLanguageBlockBuilder();
	}

	@Override
	protected IElementType getTemplateTextElementType()
	{
		return TT2_HTML;
	}

	@Override
	public Indent getIndent()
	{
		IElementType elementType = myNode.getElementType();

		if (elementType == TT2_OUTLINE_TAG)
		{
			return Indent.getAbsoluteNoneIndent();
		}

		boolean isFirst = isFirst();
		ASTNode parentNode = myNode.getTreeParent();
		IElementType parentNodeType = parentNode == null ? null : parentNode.getElementType();

		if (!isFirst && NORMAL_INDENTED_CONTAINERS.contains(parentNodeType)) // branhces and so on
		{
			return Indent.getNormalIndent();
		}
		else if (!isFirst && NORMAL_INDENTED_CONTAINERS_WITH_CLOSE_TAG.contains(parentNodeType)) // blocks
		{
			return isLast() ? Indent.getNoneIndent() : Indent.getNormalIndent();
		}

		return Indent.getNoneIndent();
	}

	/**
	 * Checks that current node is first, controlling optional SET and GET
	 *
	 * @return
	 */
	protected boolean isFirst()
	{
		boolean defaultValue = myNode.getTreePrev() == null;
		if (defaultValue)
		{
			IElementType parentNodeType = PsiUtilCore.getElementType(myNode.getTreeParent());
			if (parentNodeType == SET_DIRECTIVE)
			{
				return PsiUtilCore.getElementType(myNode) == TT2_SET;
			}
			else if (parentNodeType == GET_DIRECTIVE)
			{
				return PsiUtilCore.getElementType(myNode) == TT2_GET;
			}
		}
		return defaultValue;
	}

	/**
	 * Checks if current node is a last node or last block tag in the current parent
	 *
	 * @return check result
	 */
	protected boolean isLast()
	{
		if (myNode.getTreeNext() == null)
		{
			return true;
		}

		if (myNode.getElementType() != TT2_OPEN_TAG)
		{
			return false;
		}

		ASTNode run = myNode.getTreeNext();
		while (true)
		{
			if (run == null)
			{
				return false;
			}

			if (run.getElementType() == TT2_CLOSE_TAG)
			{
				return run.getTreeNext() == null;
			}
			run = run.getTreeNext();
		}
	}


	@Nullable
	@Override
	protected Indent getChildIndent()
	{
		IElementType elementType = myNode.getElementType();
		if (NORMAL_CHILD_INDENTED_CONTAINERS.contains(elementType))
		{
			return Indent.getNormalIndent();
		}
		return super.getChildIndent();
	}

	@Nullable
	@Override
	public Alignment getAlignment()
	{
		// we could use pattern here, but we need to find parent node, so this method is more effective i guess
		IElementType nodeType = myNode.getElementType();
		if (nodeType == TT2_ASSIGN)
		{
			ASTNode parentNode = myNode.getTreeParent();
			IElementType parentNodeType = PsiUtilCore.getElementType(parentNode);

			if (parentNodeType == ASSIGN_EXPR)
			{
				assert parentNode != null;
				ASTNode grandParentNode = parentNode.getTreeParent();
				IElementType grandParentNodeType = PsiUtilCore.getElementType(grandParentNode);
				if (grandParentNodeType == DEFAULT_DIRECTIVE || grandParentNodeType == SET_DIRECTIVE)
				{
					assert grandParentNode != null;
					return myModelBuilder.getAssignAlignment(grandParentNode);
				}
			}
		}

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
		// fixme implement InjectedLanguageBuilder handling
		return super.buildChildren();
	}

	@Nullable
	@Override
	public Spacing getSpacing(@Nullable Block child1, @NotNull Block child2)
	{
		Spacing result = null;

		if (child1 instanceof TemplateToolkitFormattingBlock && child2 instanceof TemplateToolkitFormattingBlock)
		{
			result = mySpacingBuilder.getSpacing(this, child1, child2);
		}

		if (result != null)
		{
			return result;
		}
		return super.getSpacing(child1, child2);
	}
}
