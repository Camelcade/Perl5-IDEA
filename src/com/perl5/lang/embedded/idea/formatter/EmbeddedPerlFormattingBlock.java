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

package com.perl5.lang.embedded.idea.formatter;

import com.intellij.formatting.Alignment;
import com.intellij.formatting.Indent;
import com.intellij.formatting.SpacingBuilder;
import com.intellij.formatting.Wrap;
import com.intellij.lang.ASTNode;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import com.perl5.lang.embedded.EmbeddedPerlParserDefinition;
import com.perl5.lang.perl.idea.formatter.blocks.PerlFormattingBlock;
import com.perl5.lang.perl.idea.formatter.settings.PerlCodeStyleSettings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 11.01.2016.
 */
public class EmbeddedPerlFormattingBlock extends PerlFormattingBlock
{
	public EmbeddedPerlFormattingBlock(@NotNull ASTNode node, @Nullable Wrap wrap, @Nullable Alignment alignment, @NotNull CommonCodeStyleSettings codeStyleSettings, @NotNull PerlCodeStyleSettings perlCodeStyleSettings, @NotNull SpacingBuilder spacingBuilder)
	{
		super(node, wrap, alignment, codeStyleSettings, perlCodeStyleSettings, spacingBuilder);
	}

	@Nullable
	@Override
	protected Indent getChildIndent()
	{
		if (getElementType() == EmbeddedPerlParserDefinition.FILE)
		{
			return Indent.getNoneIndent();
		}
		return super.getChildIndent();
	}
}
