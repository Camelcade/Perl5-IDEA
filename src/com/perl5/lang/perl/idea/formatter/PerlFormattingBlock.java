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
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import com.intellij.psi.formatter.common.AbstractBlock;
import com.perl5.lang.perl.idea.formatter.settings.PerlCodeStyleSettings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by hurricup on 03.09.2015.
 */
public class PerlFormattingBlock extends AbstractBlock
{
	private final Indent myIndent;
	private final CommonCodeStyleSettings mySettings;
	private final PerlCodeStyleSettings myPerl5Settings;
	private final SpacingBuilder mySpacingBuilder;
	private final Alignment myAlignmentStrategy; // AlignmentStrategy


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
		myAlignmentStrategy = alignment;
		myIndent = new PerlIndentProcessor(perlCodeStyleSettings).getChildIndent(node, binaryExpressionIndex);
	}

	@Override
	protected List<Block> buildChildren()
	{
		return null;
	}

	@Nullable
	@Override
	public Spacing getSpacing(Block child1, Block child2)
	{
		return null;
	}

	@Override
	public boolean isLeaf()
	{
		return false;
	}
}
