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
 * Created by hurricup on 13.01.2016.
 */
public class Mason2TemplatingParserImpl extends Mason2ParserImpl
{
	public static final TokenSet NAMESPACE_CONTENT_RECOVERY_TOKENS = TokenSet.orSet(
			PerlParserImpl.NAMESPACE_CONTENT_RECOVERY_TOKENS,
			TokenSet.create(
					MASON_PERL_CLOSER,
					MASON_INIT_CLOSER,
					MASON_CLASS_CLOSER,
					MASON_AFTER_CLOSER,
					MASON_BEFORE_CLOSER,
					MASON_AUGMENT_CLOSER,
					MASON_AROUND_CLOSER,
					MASON_OVERRIDE_CLOSER,
					MASON_FILTER_CLOSER,
					MASON_METHOD_CLOSER,
					MASON_BLOCK_CLOSER
			)
	);

	public static boolean parseMasonMethod(PsiBuilder b, int l, IElementType closeToken, IElementType statementTokenType)
	{
		boolean r = false;

		PsiBuilder.Marker methodMarker = b.mark();
		b.advanceLexer();
		PsiBuilder.Marker subMarker = b.mark();
		if (PerlParserUtil.consumeToken(b, IDENTIFIER))
		{
			subMarker.collapse(SUB);
			PerlParserImpl.method_signature(b, l);
			if (PerlParserUtil.consumeToken(b, MASON_TAG_CLOSER))
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
		return parsePerlBlock(b, l, closeToken, MASON_ABSTRACT_BLOCK);
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

		if (tokenType == MASON_BLOCK_OPENER)
		{
			PsiBuilder.Marker statementMarker = b.mark();
			b.advanceLexer();
			if (PerlParserImpl.expr(b, l, -1))
			{
				// parseStatement filter
				if (PerlParserUtil.consumeToken(b, MASON_EXPR_FILTER_PIPE))
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
			if (r = endOrRecover(b, MASON_BLOCK_CLOSER))
			{
				statementMarker.done(STATEMENT);
			}
		}
		if (tokenType == MASON_CALL_OPENER)
		{
			PsiBuilder.Marker statementMarker = b.mark();
			b.advanceLexer();
			PerlParserImpl.expr(b, l, -1);
			if (r = endOrRecover(b, MASON_CALL_CLOSER))
			{
				statementMarker.done(MASON_CALL_STATEMENT);
			}
		}
		else if (tokenType == MASON_CLASS_OPENER)
		{
			r = parsePerlBlock(b, l, MASON_CLASS_CLOSER);
		}
		else if (tokenType == MASON_INIT_OPENER)
		{
			r = parsePerlBlock(b, l, MASON_INIT_CLOSER);
		}
		else if (tokenType == MASON_PERL_OPENER)
		{
			r = parsePerlBlock(b, l, MASON_PERL_CLOSER);
		}
		else if (tokenType == MASON_FLAGS_OPENER)
		{
			b.advanceLexer();
			PsiBuilder.Marker statementMarker = b.mark();

			while (!b.eof() && b.getTokenType() != MASON_FLAGS_CLOSER)
			{
				if (!PerlParserImpl.expr(b, l, -1))
				{
					break;
				}
			}
			statementMarker.done(MASON_FLAGS_STATEMENT);

			r = endOrRecover(b, MASON_FLAGS_CLOSER);
		}
		else if (tokenType == MASON_DOC_OPENER)
		{
			b.advanceLexer();
			PerlParserUtil.consumeToken(b, COMMENT_BLOCK);
			r = endOrRecover(b, MASON_DOC_CLOSER);
		}
		else if (tokenType == MASON_TEXT_OPENER)
		{
			b.advanceLexer();
			PsiBuilder.Marker stringMarker = b.mark();
			if (PerlParserUtil.consumeToken(b, STRING_CONTENT))
			{
				stringMarker.done(MASON_TEXT_BLOCK);
			}
			else
			{
				stringMarker.drop();
			}
			r = endOrRecover(b, MASON_TEXT_CLOSER);
		}
		else if (tokenType == MASON_METHOD_OPENER)
		{
			r = parseMasonMethod(b, l, MASON_METHOD_CLOSER, MASON_METHOD_DEFINITION);
		}
		else if (tokenType == MASON_FILTER_OPENER)
		{
			r = parseMasonMethod(b, l, MASON_FILTER_CLOSER, MASON_FILTER_DEFINITION);
		}
		else if (tokenType == MASON_OVERRIDE_OPENER)
		{
			r = parseMasonMethod(b, l, MASON_OVERRIDE_CLOSER, MASON_OVERRIDE_DEFINITION);
		}
		else if (SIMPLE_MASON_NAMED_BLOCKS.contains(tokenType)) // simple named blocks
		{
			PsiBuilder.Marker statementMarker = b.mark();
			b.advanceLexer();
			IElementType closeToken = RESERVED_OPENER_TO_CLOSER_MAP.get(tokenType);

			if (PerlParserUtil.convertIdentifier(b, l, MASON_METHOD_MODIFIER_NAME))
			{
				if (PerlParserUtil.consumeToken(b, MASON_TAG_CLOSER))
				{
					PsiBuilder.Marker blockMarker = b.mark();
					PerlParserImpl.block_content(b, l);
					blockMarker.done(BLOCK);
					blockMarker.setCustomEdgeTokenBinders(WhitespacesBinders.GREEDY_LEFT_BINDER, WhitespacesBinders.GREEDY_RIGHT_BINDER);

					if (r = PerlParserUtil.consumeToken(b, closeToken))
					{
						statementMarker.done(RESERVED_TO_STATEMENT_MAP.get(tokenType));
						statementMarker = null;
					}
				}
			}

			if (statementMarker != null)
			{
				statementMarker.drop();
			}

			r = r || recoverToGreedy(b, closeToken, "Error");
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

	@Override
	public boolean parseStatementModifier(PsiBuilder b, int l)
	{
		if (b.getTokenType() == MASON_FILTERED_BLOCK_OPENER)
		{
			return parsePerlBlock(b, l, MASON_FILTERED_BLOCK_CLOSER, MASON_FILTERED_BLOCK);
		}
		return super.parseStatementModifier(b, l);
	}

	@Override
	public boolean parseTerm(PsiBuilder b, int l)
	{
		if (b.getTokenType() == MASON_SELF_POINTER)
		{
			PsiBuilder.Marker m = b.mark();
			b.advanceLexer();
			if (nested_call(b, l))
			{
				m.done(MASON_SIMPLE_DEREF_EXPR);
			}
			else
			{
				m.error("Error parsing filter expression");
			}
			return true;
		}
		return super.parseTerm(b, l);
	}


}
