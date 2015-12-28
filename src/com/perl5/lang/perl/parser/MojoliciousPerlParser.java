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
import com.perl5.lang.mojolicious.MojoliciousPerlElementTypes;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 28.12.2015.
 */
public class MojoliciousPerlParser extends PerlParserImpl implements MojoliciousPerlElementTypes, PerlElementTypes
{
	public static final TokenSet BAD_CAHARACTER_FORBIDDEN_TOKENS = TokenSet.orSet(
			PerlParserImpl.BAD_CHARACTER_FORBIDDEN_TOKENS,
			TokenSet.create(
					MOJO_BLOCK_EXPR_CLOSER,
					MOJO_BLOCK_EXPR_NOSPACE_CLOSER,
					MOJO_END
			));
	public static final TokenSet STATEMENT_RECOVERY_TOKENS = TokenSet.orSet(
			PerlParserImpl.STATEMENT_RECOVERY_TOKENS, TokenSet.create(
					MOJO_BLOCK_EXPR_CLOSER,
					MOJO_BLOCK_EXPR_NOSPACE_CLOSER,
					MOJO_END
			));
	public static final TokenSet BLOCK_RECOVERY_TOKENS = TokenSet.orSet(
			PerlParserImpl.BLOCK_RECOVERY_TOKENS, TokenSet.create(
					MOJO_END
			));
	public static final TokenSet CONSUMABLE_SEMI_TOKENS = TokenSet.orSet(
			PerlParserImpl.CONSUMABLE_SEMI_TOKENS, TokenSet.create(
					MOJO_BLOCK_EXPR_CLOSER,
					MOJO_BLOCK_EXPR_NOSPACE_CLOSER,
					MOJO_END
			));

	@Override
	public boolean parseTerm(PsiBuilder b, int l)
	{
		IElementType tokenType = b.getTokenType();

		if (tokenType == MOJO_BEGIN)
		{
			PsiBuilder.Marker subMarker = b.mark();
			b.advanceLexer();
			PsiBuilder.Marker blockMarker = b.mark();

			PerlParserImpl.block_content(b, l);

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
