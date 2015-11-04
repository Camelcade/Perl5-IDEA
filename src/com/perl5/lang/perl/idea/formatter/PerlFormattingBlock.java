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
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.TokenType;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import com.intellij.psi.formatter.FormatterUtil;
import com.intellij.psi.formatter.common.AbstractBlock;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.perl.idea.formatter.settings.PerlCodeStyleSettings;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.parser.PerlParserUtil;
import com.perl5.lang.perl.psi.impl.PerlHeredocElementImpl;
import com.perl5.lang.perl.psi.references.PerlHeredocReference;
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
			USE_STATEMENT,
			USE_STATEMENT_CONSTANT,
			USE_VARS_STATEMENT
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
	private List<Block> mySubBlocks;

	public PerlFormattingBlock(
			@NotNull ASTNode node,
			@Nullable Wrap wrap,
			@Nullable Alignment alignment,
			@NotNull CommonCodeStyleSettings codeStyleSettings,
			@NotNull PerlCodeStyleSettings perlCodeStyleSettings,
			@NotNull SpacingBuilder spacingBuilder
	)
	{
		super(node, wrap, alignment);
		mySettings = codeStyleSettings;
		myPerl5Settings = perlCodeStyleSettings;
		mySpacingBuilder = spacingBuilder;
		myAlignment = alignment;
		myIndent = new PerlIndentProcessor(perlCodeStyleSettings).getNodeIndent(node);
		myIsFirst = FormatterUtil.getPreviousNonWhitespaceSibling(node) == null;
		myIsLast = FormatterUtil.getNextNonWhitespaceSibling(node) == null;
		myElementType = node.getElementType();
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
		{
			mySubBlocks = buildSubBlocks();
		}

		// fixme what is re-creation for?
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
			blocks.add(createChildBlock(myNode, child, alignment));
		}

		return blocks;
	}

	private PerlFormattingBlock createChildBlock(
			ASTNode parent,
			ASTNode child,
			Alignment alignment
	)
	{
		return new PerlFormattingBlock(child, myWrap, alignment, mySettings, myPerl5Settings, mySpacingBuilder);
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

			// LF after opening brace and before closing need to check if here-doc opener is in the line
			if (LF_ELEMENTS.contains(child1Type) && LF_ELEMENTS.contains(child2Type))
				if (!isHeredocAhead((PerlFormattingBlock) child1))
					return Spacing.createSpacing(0, 0, 1, true, 1);
				else
					return Spacing.createSpacing(1, Integer.MAX_VALUE, 0, true, 1);

			if (isCodeBlock() &&
					(BLOCK_OPENERS.contains(child1Type) && ((PerlFormattingBlock) child1).isFirst()
							|| BLOCK_CLOSERS.contains(child2Type) && ((PerlFormattingBlock) child2).isLast()
					)
					&& !isHeredocAhead((PerlFormattingBlock) child1)
					)
				return Spacing.createSpacing(0, 0, 1, true, 1);
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
	public boolean isHeredocAhead(PerlFormattingBlock block)
	{
		PsiFile file = block.getNode().getPsi().getContainingFile();
		Document document = PsiDocumentManager.getInstance(file.getProject()).getDocument(file);
		if (document != null)
		{
			int endOffset = block.getTextRange().getEndOffset();
			int lineNumber = document.getLineNumber(endOffset);
			int lineEndOffset = document.getLineEndOffset(lineNumber);
			PsiElement lastLineElement = file.findElementAt(lineEndOffset);
			return lastLineElement != null
					&& lastLineElement.getParent() instanceof PerlHeredocElementImpl
					&& PerlHeredocReference.getClosestHeredocOpener(lastLineElement).getTextOffset() < endOffset
					;
		}
		return false;
	}

	@Override
	public boolean isLeaf()
	{
		return myNode.getFirstChildNode() == null || LEAF_ELEMENTS.contains(myNode.getElementType());
	}

	@Override
	public Indent getIndent()
	{
		if (isFirst() || isLast() && isBlockCloser())
			return Indent.getNoneIndent();
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
		if (PerlIndentProcessor.UNINDENTABLE_CONTAINERS.contains(getElementType()))
			return Indent.getNoneIndent();

		return super.getChildIndent();
	}

	@NotNull
	@Override
	public TextRange getTextRange()
	{
		int start = myNode.getStartOffset();
		return new TextRange(start, start + myNode.getText().length());
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

}
