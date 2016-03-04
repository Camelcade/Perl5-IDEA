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

package com.perl5.lang.mojolicious.idea.folding;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.mojolicious.MojoliciousElementTypes;
import com.perl5.lang.perl.idea.folding.PerlFoldingBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 07.01.2016.
 */
public class MojoliciousFoldingBuilder extends PerlFoldingBuilder implements MojoliciousElementTypes
{
	protected static final TokenSet COMMENT_EXCLUDED_TOKENS = TokenSet.orSet(
			PerlFoldingBuilder.COMMENT_EXCLUDED_TOKENS,
			TokenSet.create(
					MOJO_BLOCK_OPENER,
					MOJO_BLOCK_CLOSER,
					MOJO_BLOCK_NOSPACE_CLOSER,

					MOJO_BLOCK_EXPR_OPENER,
					MOJO_BLOCK_EXPR_CLOSER,

					MOJO_BLOCK_EXPR_ESCAPED_OPENER,
					MOJO_BLOCK_EXPR_NOSPACE_CLOSER,

					MOJO_LINE_EXPR_ESCAPED_OPENER,
					MOJO_LINE_EXPR_OPENER,
					MOJO_LINE_OPENER,

					MOJO_BLOCK_OPENER_TAG,
					MOJO_LINE_OPENER_TAG
			));

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
		return MOJO_TEMPLATE_BLOCK_HTML;
	}
}
