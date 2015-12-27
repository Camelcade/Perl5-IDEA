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

package com.perl5.lang.perl.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.WhitespacesBinders;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.perl.extensions.parser.PerlParserExtension;
import com.perl5.lang.perl.parser.builder.PerlBuilder;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 27.12.2015.
 */
public class MojoliciousPerlParserExtensionImpl extends PerlParserExtension implements MojoliciousPerlParserExtension
{
	protected static final TokenSet BAD_CAHARACTER_FORBIDDEN_TOKENS = TokenSet.create(
			MOJO_END
	);
	protected static final TokenSet STATEMENT_RECOVERY_TOKENS = TokenSet.create(
			MOJO_END
	);
	protected static final TokenSet BLOCK_RECOVERY_TOKENS = TokenSet.create(
			MOJO_END
	);
	protected static final TokenSet CONSUMABLE_SEMI_TOKENS = TokenSet.create(
			MOJO_END
	);

	@Override
	public boolean parseStatement(PerlBuilder b, int l)
	{
		IElementType tokenType = b.getTokenType();

		if (tokenType == MOJO_BLOCK_EXPR_OPENER || tokenType == MOJO_BLOCK_EXPR_ESCAPED_OPENER)
		{
			b.advanceLexer();

			PsiBuilder.Marker m = b.mark();
			PerlParser.expr(b, l, -1);

			tokenType = b.getTokenType();
			if (tokenType == MOJO_BLOCK_EXPR_CLOSER || tokenType == MOJO_BLOCK_EXPR_NOSPACE_CLOSER)
			{
				m.done(STATEMENT);
				b.advanceLexer();
				return true;
			}
			else
			{
				m.rollbackTo();
			}
		}

		return super.parseStatement(b, l);
	}

	@Override
	public boolean parseTerm(PerlBuilder b, int l)
	{
		IElementType tokenType = b.getTokenType();

		if (tokenType == MOJO_BEGIN)
		{
			PsiBuilder.Marker subMarker = b.mark();
			b.advanceLexer();
			PsiBuilder.Marker blockMarker = b.mark();

			PerlParser.block_content(b, l);

			if (b.getTokenType() == MOJO_END)
			{
				blockMarker.done(BLOCK);
				blockMarker.setCustomEdgeTokenBinders(WhitespacesBinders.GREEDY_LEFT_BINDER, WhitespacesBinders.GREEDY_RIGHT_BINDER);
				subMarker.done(ANON_SUB);
				return true;
			}
			else
			{
				blockMarker.drop();
				subMarker.rollbackTo();
			}
		}

		return super.parseTerm(b, l);
	}

	@Nullable
	@Override
	public TokenSet getBadCharacterForbiddenTokens()
	{
		return BAD_CAHARACTER_FORBIDDEN_TOKENS;
	}

	@Nullable
	@Override
	public TokenSet getStatementRecoveryTokens()
	{
		return STATEMENT_RECOVERY_TOKENS;
	}

	@Nullable
	@Override
	public TokenSet getBlockRecoveryTokens()
	{
		return BLOCK_RECOVERY_TOKENS;
	}

	@Nullable
	@Override
	public TokenSet getConsumableSemicolonTokens()
	{
		return CONSUMABLE_SEMI_TOKENS;
	}
}
