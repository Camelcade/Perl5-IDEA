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

package com.perl5.lang.perl.idea.formatter.blocks;

import com.intellij.formatting.*;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.TokenType;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import com.intellij.psi.formatter.FormatterUtil;
import com.intellij.psi.formatter.common.AbstractBlock;
import com.intellij.psi.formatter.common.InjectedLanguageBlockBuilder;
import com.intellij.psi.impl.source.tree.CompositeElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.perl.idea.formatter.PerlIndentProcessor;
import com.perl5.lang.perl.idea.formatter.settings.PerlCodeStyleSettings;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.parser.PerlParserUtil;
import com.perl5.lang.perl.psi.impl.PerlFileImpl;
import com.perl5.lang.perl.psi.impl.PerlHeredocElementImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hurricup on 03.09.2015.
 */
public class PerlFormattingBlock extends AbstractBlock implements PerlElementTypes
{
	/**
	 * Composit elements that should be treated as leaf elements, no children
	 */
	public final static TokenSet LEAF_ELEMENTS = TokenSet.create(
			HEREDOC,
			HEREDOC_QX,
			HEREDOC_QQ,
			STRING_SQ,
			STRING_DQ,
			STRING_XQ,
			POD,
			PerlParserUtil.DUMMY_BLOCK
	);

	/**
	 * Elements that must have LF between them
	 */
	public final static TokenSet LF_ELEMENTS = TokenSet.create(
			LABEL_DECLARATION,
			STATEMENT,
			FOR_COMPOUND,
			FOREACH_COMPOUND,
			WHILE_COMPOUND,
			WHEN_COMPOUND,
			UNTIL_COMPOUND,
			IF_COMPOUND,
			USE_STATEMENT
	);

	public final static TokenSet BLOCK_OPENERS = TokenSet.create(
			LEFT_BRACE,
			LEFT_BRACKET,
			LEFT_PAREN
	);

	public final static TokenSet BLOCK_CLOSERS = TokenSet.create(
			RIGHT_BRACE,
			RIGHT_BRACKET,
			RIGHT_PAREN,

			SEMICOLON
	);

	private final Indent myIndent;
	private final CommonCodeStyleSettings mySettings;
	private final PerlCodeStyleSettings myPerl5Settings;
	private final SpacingBuilder mySpacingBuilder;
	private final Alignment myAlignment;
	private final boolean myIsFirst;
	private final boolean myIsLast;
	private final IElementType myElementType;
	private final InjectedLanguageBlockBuilder myInjectedLanguageBlockBuilder;
	private List<Block> mySubBlocks;

	public PerlFormattingBlock(
			@NotNull ASTNode node,
			@Nullable Wrap wrap,
			@Nullable Alignment alignment,
			@NotNull CommonCodeStyleSettings codeStyleSettings,
			@NotNull PerlCodeStyleSettings perlCodeStyleSettings,
			@NotNull SpacingBuilder spacingBuilder,
			@NotNull InjectedLanguageBlockBuilder injectedLanguageBlockBuilder
	)
	{
		super(node, wrap, alignment);
		mySettings = codeStyleSettings;
		myPerl5Settings = perlCodeStyleSettings;
		mySpacingBuilder = spacingBuilder;
		myAlignment = alignment;
		myIndent = getIndentProcessor().getNodeIndent(node, perlCodeStyleSettings);
		myIsFirst = FormatterUtil.getPreviousNonWhitespaceSibling(node) == null;
		myIsLast = FormatterUtil.getNextNonWhitespaceSibling(node) == null;
		myElementType = node.getElementType();
		myInjectedLanguageBlockBuilder = injectedLanguageBlockBuilder;
	}

	protected static boolean shouldCreateBlockFor(ASTNode node)
	{
		return node.getElementType() != TokenType.WHITE_SPACE && node.getText().length() != 0;
	}

	protected PerlIndentProcessor getIndentProcessor()
	{
		return PerlIndentProcessor.INSTANCE;
	}

	@NotNull
	@Override
	protected List<Block> buildChildren()
	{
		if (mySubBlocks == null)
		{
			mySubBlocks = buildSubBlocks();
		}

		// fixme what is re-creation for?
		return new ArrayList<Block>(mySubBlocks);
	}

	protected List<Block> buildSubBlocks()
	{
		final List<Block> blocks = new ArrayList<Block>();

		if (!isLeaf())
		{
			IElementType elementType = getElementType();

			Alignment alignment = null; //Alignment.createAlignment();

			if (elementType == COMMA_SEQUENCE_EXPR || elementType == CONSTANTS_BLOCK || elementType == TRENAR_EXPR)
			{
				alignment = Alignment.createAlignment(true);
			}

			Wrap wrap = null;

			if (elementType == COMMA_SEQUENCE_EXPR && !isNewLineForbidden(this))
			{
				wrap = Wrap.createWrap(WrapType.NORMAL, true);
			}

			for (ASTNode child = myNode.getFirstChildNode(); child != null; child = child.getTreeNext())
			{
				if (!shouldCreateBlockFor(child))
				{
					continue;
				}
				blocks.add(createChildBlock(child, wrap, alignment));
			}
		}
		else
		{
			PsiElement element = getNode().getPsi();
			if (element instanceof PerlHeredocElementImpl && ((PerlHeredocElementImpl) element).isValidHost())
			{
				getInjectedLanguageBlockBuilder().addInjectedBlocks(blocks, getNode(), null, null, getIndent());
			}

		}
		return blocks;
	}

	protected PerlFormattingBlock createChildBlock(
			ASTNode child,
			Wrap wrap,
			Alignment alignment
	)
	{
		IElementType childElementType = child.getElementType();
		if (alignment != null && (childElementType == QUESTION || childElementType == COLON || childElementType == FAT_COMMA))
		{
			return createBlock(child, wrap, alignment);
		}
		else if (childElementType == CONSTANT_DEFINITION)    // fixme we should use delegate here, constant wraps regular block
		{
			return new PerlConstantDefinitionFormattingBlock(child, wrap, alignment, getSettings(), getPerl5Settings(), getSpacingBuilder(), myInjectedLanguageBlockBuilder);
		}
		else
		{
			return createBlock(child, wrap, null);
		}
	}

	protected PerlFormattingBlock createBlock(
			@NotNull ASTNode node,
			@Nullable Wrap wrap,
			@Nullable Alignment alignment
	)
	{
		return new PerlFormattingBlock(node, wrap, alignment, getSettings(), getPerl5Settings(), getSpacingBuilder(), myInjectedLanguageBlockBuilder);
	}

	protected CommonCodeStyleSettings getSettings()
	{
		return mySettings;
	}

	protected PerlCodeStyleSettings getPerl5Settings()
	{
		return myPerl5Settings;
	}

	protected SpacingBuilder getSpacingBuilder()
	{
		return mySpacingBuilder;
	}

	@Nullable
	@Override
	public Spacing getSpacing(Block child1, @NotNull Block child2)
	{
		if (child1 instanceof PerlFormattingBlock && child2 instanceof PerlFormattingBlock)
		{
			ASTNode child1Node = ((PerlFormattingBlock) child1).getNode();
			IElementType child1Type = child1Node.getElementType();
			ASTNode child2Node = ((PerlFormattingBlock) child2).getNode();
			IElementType child2Type = child2Node.getElementType();

			// fix for number/concat
			if (child2Type == OPERATOR_CONCAT)
			{
				ASTNode run = child1Node;
				while (run instanceof CompositeElement)
				{
					run = run.getLastChildNode();
				}

				if (run != null)
				{
					IElementType runType = run.getElementType();
					if (runType == NUMBER_SIMPLE || runType == NUMBER && StringUtil.endsWith(run.getText(), "."))
					{
						return Spacing.createSpacing(1, 1, 0, true, 1);
					}
				}
			}

			// LF after opening brace and before closing need to check if here-doc opener is in the line
			if (LF_ELEMENTS.contains(child1Type) && LF_ELEMENTS.contains(child2Type))
			{
				if (!isNewLineForbidden((PerlFormattingBlock) child1))
				{
					return Spacing.createSpacing(0, 0, 1, true, 1);
				}
				else
				{
					return Spacing.createSpacing(1, Integer.MAX_VALUE, 0, true, 1);
				}
			}
			if (isCodeBlock() && !inGrepMapSort() && !blockHasLessChildrenThan(2) &&
					(BLOCK_OPENERS.contains(child1Type) && ((PerlFormattingBlock) child1).isFirst()
							|| BLOCK_CLOSERS.contains(child2Type) && ((PerlFormattingBlock) child2).isLast()
					)
					&& !isNewLineForbidden((PerlFormattingBlock) child1)
					)
			{
				return Spacing.createSpacing(0, 0, 1, true, 1);
			}
		}
		return mySpacingBuilder.getSpacing(this, child1, child2);
	}

	/**
	 * Checks if Heredoc is ahead of current block and it's not possible to insert newline
	 * fixme we should cache result here by line number
	 *
	 * @param block block in question
	 * @return check result
	 */
	protected boolean isNewLineForbidden(PerlFormattingBlock block)
	{
		PsiElement element = block.getNode().getPsi();
		PsiFile file = element.getContainingFile();
		assert file instanceof PerlFileImpl;
		return ((PerlFileImpl) file).isNewLineForbiddenAt(element);
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
		IElementType elementType = getElementType();

		if (getIndentProcessor().getUnindentableContainers().contains(elementType))
		{
			return Indent.getNoneIndent();
		}

		if (getIndentProcessor().getBlockLikeContainers().contains(elementType))
		{
			return Indent.getNormalIndent();
		}

		return super.getChildIndent();
	}

	@NotNull
	@Override
	public ChildAttributes getChildAttributes(int newChildIndex)
	{
		return new ChildAttributes(getChildIndent(), getChildAlignment());
	}

	@Nullable
	private Alignment getChildAlignment()
	{

		IElementType elementType = getElementType();
		if (PerlIndentProcessor.COMMA_LIKE_SEQUENCES.contains(elementType))
		{
			return null;
		}

		// this is default algorythm from AbstractBlock#getFirstChildAlignment()
		List<Block> subBlocks = getSubBlocks();
		for (final Block subBlock : subBlocks)
		{
			Alignment alignment = subBlock.getAlignment();
			if (alignment != null)
			{
				return alignment;
			}
		}
		return null;
	}

	public boolean isLast()
	{
		return myIsLast;
	}

	public boolean isFirst()
	{
		return myIsFirst;
	}

	public IElementType getElementType()
	{
		return myElementType;
	}

	public boolean isCodeBlock()
	{
		return getElementType() == BLOCK;
	}

	public boolean isBlockCloser()
	{
		return BLOCK_CLOSERS.contains(getElementType());
	}

	public boolean isBlockOpener()
	{
		return BLOCK_OPENERS.contains(getElementType());
	}

	/**
	 * Check if we are in grep map or sort
	 *
	 * @return check result
	 */
	public boolean inGrepMapSort()
	{
		ASTNode parent = myNode.getTreeParent();
		IElementType parentElementType;
		return parent != null && ((parentElementType = parent.getElementType()) == GREP_EXPR || parentElementType == SORT_EXPR || parentElementType == MAP_EXPR);
	}

	/**
	 * Checks if block contains more than specified number of meaningful children. Spaces and line comments are being ignored
	 *
	 * @return check result
	 */
	public boolean blockHasLessChildrenThan(int maxChildren)
	{
		int counter = -2; // for braces
		ASTNode childNode = myNode.getFirstChildNode();
		while (childNode != null)
		{
			IElementType nodeType = childNode.getElementType();
			if (nodeType != TokenType.WHITE_SPACE && nodeType != COMMENT_LINE && nodeType != SEMICOLON)
			{
				if (++counter >= maxChildren)
				{
					return false;
				}
			}
			childNode = childNode.getTreeNext();
		}
		return true;
	}

	public InjectedLanguageBlockBuilder getInjectedLanguageBlockBuilder()
	{
		return myInjectedLanguageBlockBuilder;
	}
}
