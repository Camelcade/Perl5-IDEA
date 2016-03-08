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

package com.perl5.lang.htmlmason.idea.formatter;

import com.intellij.formatting.Alignment;
import com.intellij.formatting.SpacingBuilder;
import com.intellij.formatting.Wrap;
import com.intellij.lang.ASTNode;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import com.intellij.psi.formatter.common.InjectedLanguageBlockBuilder;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.htmlmason.HTMLMasonElementPatterns;
import com.perl5.lang.htmlmason.elementType.HTMLMasonElementTypes;
import com.perl5.lang.perl.idea.formatter.PerlIndentProcessor;
import com.perl5.lang.perl.idea.formatter.blocks.PerlFormattingBlock;
import com.perl5.lang.perl.idea.formatter.settings.PerlCodeStyleSettings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 08.03.2016.
 */
public class HTMLMasonFormattingBlock extends AbstractMasonFormattingBlock implements HTMLMasonElementTypes, HTMLMasonElementPatterns
{
	public HTMLMasonFormattingBlock(@NotNull ASTNode node,
									@Nullable Wrap wrap,
									@Nullable Alignment alignment,
									@NotNull CommonCodeStyleSettings codeStyleSettings,
									@NotNull PerlCodeStyleSettings perlCodeStyleSettings,
									@NotNull SpacingBuilder spacingBuilder,
									@NotNull InjectedLanguageBlockBuilder injectedLanguageBlockBuilder)
	{
		super(node, wrap, alignment, codeStyleSettings, perlCodeStyleSettings, spacingBuilder, injectedLanguageBlockBuilder);
	}

	@Override
	protected IElementType getLineOpenerToken()
	{
		return HTML_MASON_LINE_OPENER;
	}

	@Override
	protected boolean isNewLineForbidden(PerlFormattingBlock block)
	{
		return super.isNewLineForbidden(block) || ATTR_OR_ARG_ELEMENT_PATTERN.accepts(block.getNode().getPsi());
	}

	@Override
	protected PerlFormattingBlock createBlock(@NotNull ASTNode node, @Nullable Wrap wrap, @Nullable Alignment alignment)
	{
		return new HTMLMasonFormattingBlock(node, wrap, alignment, getSettings(), getPerl5Settings(), getSpacingBuilder(), getInjectedLanguageBlockBuilder());
	}

	@Override
	protected PerlIndentProcessor getIndentProcessor()
	{
		return HTMLMasonIndentProcessor.INSTANCE;
	}

}
