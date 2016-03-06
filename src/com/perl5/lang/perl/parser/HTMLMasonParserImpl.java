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

package com.perl5.lang.perl.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.WhitespacesBinders;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 05.03.2016.
 */
@SuppressWarnings("Duplicates")
public class HTMLMasonParserImpl extends PerlParserImpl implements HTMLMasonParser
{
	public static final TokenSet NAMESPACE_CONTENT_RECOVERY_TOKENS = TokenSet.orSet(
			PerlParserImpl.NAMESPACE_CONTENT_RECOVERY_TOKENS,
			TokenSet.create(
					HTML_MASON_ONCE_CLOSER,
					HTML_MASON_SHARED_CLOSER,
					HTML_MASON_CLEANUP_CLOSER,
					HTML_MASON_INIT_CLOSER,
					HTML_MASON_PERL_CLOSER,
					HTML_MASON_ATTR_CLOSER,
					HTML_MASON_ARGS_CLOSER,
					HTML_MASON_FILTER_CLOSER,
					HTML_MASON_FLAGS_CLOSER,

					HTML_MASON_METHOD_CLOSER,
					HTML_MASON_DEF_CLOSER,
					HTML_MASON_BLOCK_CLOSER
			)
	);
	protected static final TokenSet STATEMENT_RECOVERY_TOKENS = TokenSet.orSet(
			PerlParserImpl.STATEMENT_RECOVERY_TOKENS,
			TokenSet.create(
					HTML_MASON_ONCE_CLOSER,
					HTML_MASON_ATTR_CLOSER,
					HTML_MASON_SHARED_CLOSER,
					HTML_MASON_ARGS_CLOSER,
					HTML_MASON_CLEANUP_CLOSER,
					HTML_MASON_INIT_CLOSER,
					HTML_MASON_PERL_CLOSER,
					HTML_MASON_FILTER_CLOSER,
					HTML_MASON_FLAGS_CLOSER,

					HTML_MASON_METHOD_CLOSER,
					HTML_MASON_DEF_CLOSER
			));
	protected static final TokenSet BLOCK_RECOVERY_TOKENS = TokenSet.orSet(
			PerlParserImpl.BLOCK_RECOVERY_TOKENS, TokenSet.create(
					HTML_MASON_ONCE_CLOSER,
					HTML_MASON_SHARED_CLOSER,
					HTML_MASON_CLEANUP_CLOSER,
					HTML_MASON_INIT_CLOSER,
					HTML_MASON_PERL_CLOSER,
					HTML_MASON_ATTR_CLOSER,
					HTML_MASON_ARGS_CLOSER,
					HTML_MASON_FILTER_CLOSER,
					HTML_MASON_FLAGS_CLOSER,

					HTML_MASON_METHOD_CLOSER,
					HTML_MASON_DEF_CLOSER
			));
	protected static final TokenSet BAD_CHARACTER_FORBIDDEN_TOKENS = TokenSet.orSet(
			PerlParserImpl.BAD_CHARACTER_FORBIDDEN_TOKENS, TokenSet.create(
					HTML_MASON_ONCE_CLOSER,
					HTML_MASON_SHARED_CLOSER,
					HTML_MASON_CLEANUP_CLOSER,
					HTML_MASON_INIT_CLOSER,
					HTML_MASON_PERL_CLOSER,
					HTML_MASON_ATTR_CLOSER,
					HTML_MASON_ARGS_CLOSER,
					HTML_MASON_FILTER_CLOSER,
					HTML_MASON_FLAGS_CLOSER,

					HTML_MASON_METHOD_CLOSER,
					HTML_MASON_DEF_CLOSER
			));

	protected TokenSet UNCONSUMABLE_SEMI_TOKENS = TokenSet.orSet(
			PerlParserImpl.UNCONSUMABLE_SEMI_TOKENS,
			TokenSet.create(
					HTML_MASON_FILTER_CLOSER
			)
	);

	public static boolean parseMasonNamedBlock(PsiBuilder b, int l, IElementType closeToken, IElementType statementTokenType)
	{
		boolean r = false;

		PsiBuilder.Marker methodMarker = b.mark();
		b.advanceLexer();
		PsiBuilder.Marker subMarker = b.mark();
		if (PerlParserUtil.consumeToken(b, IDENTIFIER))
		{
			subMarker.collapse(SUB);
			if (PerlParserUtil.consumeToken(b, HTML_MASON_TAG_CLOSER))
			{
				PsiBuilder.Marker blockMarker = b.mark();
				PerlParserImpl.block_content(b, l);

				if (b.getTokenType() == closeToken)
				{
					blockMarker.done(BLOCK);
					blockMarker.setCustomEdgeTokenBinders(WhitespacesBinders.GREEDY_LEFT_BINDER, WhitespacesBinders.GREEDY_RIGHT_BINDER);
					b.advanceLexer();
					methodMarker.done(statementTokenType);
					r = true;
				}
			}
		}

		if (!r)
		{
			methodMarker.rollbackTo();
		}

		return r || recoverToGreedy(b, closeToken, "Error");
	}

	protected static boolean endOrRecover(PsiBuilder b, IElementType toElement)
	{
		return endOrRecover(b, toElement, "Error");
	}

	protected static boolean endOrRecover(PsiBuilder b, IElementType toElement, String errorMessage)
	{
		return PerlParserUtil.consumeToken(b, toElement) || recoverToGreedy(b, toElement, errorMessage);
	}

	protected static boolean recoverToGreedy(PsiBuilder b, IElementType toElement, String errorMessage)
	{
		boolean r = recoverTo(b, toElement, errorMessage);
		r = r || PerlParserUtil.consumeToken(b, toElement);
		return r;
	}

	protected static boolean recoverTo(PsiBuilder b, IElementType toElement, String errorMessage)
	{
		// recover bad code
		PsiBuilder.Marker errorMarker = b.mark();
		while (!b.eof() && b.getTokenType() != toElement)
		{
			b.advanceLexer();
			;
		}
		errorMarker.error(errorMessage);
		return b.eof();
	}

	protected static boolean parsePerlBlock(PsiBuilder b, int l, IElementType closeToken)
	{
		return parsePerlBlock(b, l, closeToken, HTML_MASON_ABSTRACT_BLOCK);
	}

	protected static boolean parsePerlBlock(PsiBuilder b, int l, IElementType closeToken, IElementType blockToken)
	{
		b.advanceLexer();
		PsiBuilder.Marker abstractBlockMarker = b.mark();

		while (!b.eof() && b.getTokenType() != closeToken)
		{
			if (!PerlParserImpl.file_item(b, l))
			{
				break;
			}
		}
		abstractBlockMarker.done(blockToken);
		abstractBlockMarker.setCustomEdgeTokenBinders(WhitespacesBinders.GREEDY_LEFT_BINDER, WhitespacesBinders.GREEDY_RIGHT_BINDER);
		return endOrRecover(b, closeToken);
	}

	@NotNull
	@Override
	public TokenSet getUnconsumableSemicolonTokens()
	{
		return UNCONSUMABLE_SEMI_TOKENS;
	}

	@NotNull
	@Override
	public TokenSet getStatementRecoveryTokens()
	{
		return STATEMENT_RECOVERY_TOKENS;
	}

	@NotNull
	@Override
	public TokenSet getBadCharacterForbiddenTokens()
	{
		return BAD_CHARACTER_FORBIDDEN_TOKENS;
	}

	@NotNull
	@Override
	public TokenSet getBlockRecoveryTokens()
	{
		return BLOCK_RECOVERY_TOKENS;
	}

	@NotNull
	@Override
	public TokenSet getNamespaceContentRecoveryTokens()
	{
		return NAMESPACE_CONTENT_RECOVERY_TOKENS;
	}

	@Override
	public boolean parseStatement(PsiBuilder b, int l)
	{
		IElementType tokenType = b.getTokenType();

		boolean r = false;
		PsiBuilder.Marker m = b.mark();

		if (tokenType == HTML_MASON_BLOCK_OPENER)
		{
			PsiBuilder.Marker statementMarker = b.mark();
			b.advanceLexer();
			if (PerlParserImpl.expr(b, l, -1))
			{
				// parseStatement filter
				if (PerlParserUtil.consumeToken(b, HTML_MASON_EXPR_FILTER_PIPE))
				{
					while (b.getTokenType() == IDENTIFIER)
					{
						PsiBuilder.Marker fm = b.mark();
						b.advanceLexer();
						fm.collapse(SUB);
						fm.precede().done(METHOD);

						if (!PerlParserUtil.consumeToken(b, OPERATOR_COMMA))
						{
							break;
						}
					}
				}
			}
			if (r = endOrRecover(b, HTML_MASON_BLOCK_CLOSER))
			{
				statementMarker.done(STATEMENT);
			}
		}
		if (tokenType == HTML_MASON_CALL_OPENER)
		{
			PsiBuilder.Marker statementMarker = b.mark();
			b.advanceLexer();
			PerlParserImpl.expr(b, l, -1);
			if (r = endOrRecover(b, HTML_MASON_CALL_CLOSER))
			{
				statementMarker.done(HTML_MASON_CALL_STATEMENT);
			}
		}
		else if (tokenType == HTML_MASON_ONCE_OPENER)
		{
			r = parsePerlBlock(b, l, HTML_MASON_ONCE_CLOSER);
		}
		else if (tokenType == HTML_MASON_SHARED_OPENER)
		{
			r = parsePerlBlock(b, l, HTML_MASON_SHARED_CLOSER);
		}
		else if (tokenType == HTML_MASON_CLEANUP_OPENER)
		{
			r = parsePerlBlock(b, l, HTML_MASON_CLEANUP_CLOSER);
		}
		else if (tokenType == HTML_MASON_INIT_OPENER)
		{
			r = parsePerlBlock(b, l, HTML_MASON_INIT_CLOSER);
		}
		else if (tokenType == HTML_MASON_FILTER_OPENER)
		{
			r = parsePerlBlock(b, l, HTML_MASON_FILTER_CLOSER);
		}
		else if (tokenType == HTML_MASON_PERL_OPENER)
		{
			r = parsePerlBlock(b, l, HTML_MASON_PERL_CLOSER);
		}
		else if (tokenType == HTML_MASON_FLAGS_OPENER)
		{
			b.advanceLexer();
			PsiBuilder.Marker statementMarker = b.mark();

			while (!b.eof() && b.getTokenType() != HTML_MASON_FLAGS_CLOSER)
			{
				if (!PerlParserImpl.expr(b, l, -1))
				{
					break;
				}
			}
			statementMarker.done(HTML_MASON_FLAGS_STATEMENT);

			r = endOrRecover(b, HTML_MASON_FLAGS_CLOSER);
		}
		else if (tokenType == HTML_MASON_DOC_OPENER)
		{
			b.advanceLexer();
			PerlParserUtil.consumeToken(b, COMMENT_BLOCK);
			r = endOrRecover(b, HTML_MASON_DOC_CLOSER);
		}
		else if (tokenType == HTML_MASON_TEXT_OPENER)
		{
			b.advanceLexer();
			PsiBuilder.Marker stringMarker = b.mark();
			if (PerlParserUtil.consumeToken(b, STRING_CONTENT))
			{
				stringMarker.done(HTML_MASON_TEXT_BLOCK);
			}
			else
			{
				stringMarker.drop();
			}
			r = endOrRecover(b, HTML_MASON_TEXT_CLOSER);
		}
		else if (tokenType == HTML_MASON_METHOD_OPENER)
		{
			r = parseMasonNamedBlock(b, l, HTML_MASON_METHOD_CLOSER, HTML_MASON_METHOD_DEFINITION);
		}
		else if (tokenType == HTML_MASON_DEF_OPENER)
		{
			r = parseMasonNamedBlock(b, l, HTML_MASON_DEF_CLOSER, HTML_MASON_SUBCOMPONENT_DEFINITION);
		}

		if (r)
		{
			m.drop();
		}
		else
		{
			m.rollbackTo();
		}

		return r || super.parseStatement(b, l);
	}

}
