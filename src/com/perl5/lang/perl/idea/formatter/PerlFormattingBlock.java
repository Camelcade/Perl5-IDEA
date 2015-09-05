/*
 * Copyright 2015 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.formatter;

import com.intellij.formatting.*;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.TokenType;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import com.intellij.psi.formatter.common.AbstractBlock;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.perl.idea.formatter.settings.PerlCodeStyleSettings;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hurricup on 03.09.2015.
 */
public class PerlFormattingBlock extends AbstractBlock implements PerlElementTypes
{
	protected final static TokenSet LEAF_ELEMENTS = TokenSet.create(
			HEREDOC,
			HEREDOC_QX,
			HEREDOC_QQ
	);

	private final Indent myIndent;
	private final CommonCodeStyleSettings mySettings;
	private final PerlCodeStyleSettings myPerl5Settings;
	private final SpacingBuilder mySpacingBuilder;
	private final Alignment myAlignment;
	private List<Block> mySubBlocks;


	public PerlFormattingBlock(
			@NotNull ASTNode node,
			@Nullable Wrap wrap,
			@Nullable Alignment alignment,
			@NotNull CommonCodeStyleSettings codeStyleSettings,
			@NotNull PerlCodeStyleSettings perlCodeStyleSettings,
			@NotNull SpacingBuilder spacingBuilder,
			int binaryExpressionIndex
	)
	{
		super(node, wrap, alignment);
		mySettings = codeStyleSettings;
		myPerl5Settings = perlCodeStyleSettings;
		mySpacingBuilder = spacingBuilder;
		myAlignment = alignment;
		myIndent = new PerlIndentProcessor(perlCodeStyleSettings).getChildIndent(node, binaryExpressionIndex);
	}

	private static boolean shouldCreateBlockFor(ASTNode node)
	{
		return node.getElementType() != TokenType.WHITE_SPACE && node.getText().length() != 0;
	}

	@NotNull
	@Override
	protected List<Block> buildChildren()
	{
		if (mySubBlocks == null)
			mySubBlocks = buildSubBlocks();

		return new ArrayList<Block>(mySubBlocks);
	}

	private List<Block> buildSubBlocks()
	{
		final List<Block> blocks = new ArrayList<Block>();
//		System.err.println("Creating sub-blocks for " + myNode);

		Alignment alignment = null;//Alignment.createAlignment();

		for (ASTNode child = myNode.getFirstChildNode(); child != null; child = child.getTreeNext())
		{
			if (!shouldCreateBlockFor(child)) continue;
//			System.err.println("Creating sub-block for " + child);
			blocks.add(createChildBlock(myNode, child, alignment, -1));
		}

		return blocks;
	}

	private PerlFormattingBlock createChildBlock(
			ASTNode parent,
			ASTNode child,
			Alignment alignment,
			int binaryExpressionIndex
	)
	{
		return new PerlFormattingBlock(child, myWrap, alignment, mySettings, myPerl5Settings, mySpacingBuilder, binaryExpressionIndex);
	}

	@Nullable
	@Override
	public Spacing getSpacing(Block child1, @NotNull Block child2)
	{
		return mySpacingBuilder.getSpacing(this, child1, child2);
	}

	@Override
	public boolean isLeaf()
	{
		return myNode.getFirstChildNode() == null || LEAF_ELEMENTS.contains(myNode.getElementType());
	}

	@Override
	public Indent getIndent()
	{
		return myIndent;
	}

	@Nullable
	@Override
	public Alignment getAlignment()
	{
		return super.getAlignment();
	}


	@Nullable
	@Override
	protected Indent getChildIndent()
	{
//		System.err.println("Invoced getchild indent");
		IElementType type = getNode().getElementType();

		if (PerlIndentProcessor.BLOCK_LIKE_TOKENS.contains(type))
			return Indent.getNormalIndent();

		return Indent.getNoneIndent();
	}

	@NotNull
	@Override
	public TextRange getTextRange()
	{
		int start = myNode.getStartOffset();
		return new TextRange(start, start + myNode.getText().length());
	}
}
