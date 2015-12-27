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
import gnu.trove.THashMap;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 21.12.2015.
 */
public class MasonPerlParserExtensionImpl extends PerlParserExtension implements MasonPerlParserExtension
{
	protected static final TokenSet STATEMENT_RECOVERY_TOKENSET = TokenSet.create(
			MASON_CLASS_CLOSER,
			MASON_INIT_CLOSER,
			MASON_PERL_CLOSER,

			MASON_AFTER_CLOSER,
			MASON_BEFORE_CLOSER,
			MASON_AUGMENT_CLOSER,
			MASON_AROUND_CLOSER,

			MASON_METHOD_CLOSER,
			MASON_OVERRIDE_CLOSER,
			MASON_FILTER_CLOSER
	);

	protected static final TokenSet BLOCK_RECOVERY_TOKENSET = TokenSet.create(
			MASON_CLASS_CLOSER,
			MASON_INIT_CLOSER,
			MASON_PERL_CLOSER,

			MASON_AFTER_CLOSER,
			MASON_BEFORE_CLOSER,
			MASON_AUGMENT_CLOSER,
			MASON_AROUND_CLOSER,

			MASON_METHOD_CLOSER,
			MASON_OVERRIDE_CLOSER,
			MASON_FILTER_CLOSER
	);

	protected static final TokenSet BAD_CHARACTER_FORBIDDEN_TOKENS = TokenSet.create(
			MASON_CLASS_CLOSER,
			MASON_INIT_CLOSER,
			MASON_PERL_CLOSER,

			MASON_AFTER_CLOSER,
			MASON_BEFORE_CLOSER,
			MASON_AUGMENT_CLOSER,
			MASON_AROUND_CLOSER,

			MASON_METHOD_CLOSER,
			MASON_OVERRIDE_CLOSER,
			MASON_FILTER_CLOSER
	);

	protected final static TokenSet SIMPLE_MASON_NAMED_BLOCKS;

	protected final static THashMap<IElementType, IElementType> RESERVED_OPENER_TO_CLOSER_MAP = new THashMap<IElementType, IElementType>();
	protected final static THashMap<IElementType, IElementType> RESERVED_TO_STATEMENT_MAP = new THashMap<IElementType, IElementType>();

	static
	{
		RESERVED_TO_STATEMENT_MAP.put(MASON_AROUND_OPENER, MOOSE_STATEMENT_AROUND);
		RESERVED_TO_STATEMENT_MAP.put(MASON_AUGMENT_OPENER, MOOSE_STATEMENT_AUGMENT);
		RESERVED_TO_STATEMENT_MAP.put(MASON_AFTER_OPENER, MOOSE_STATEMENT_AFTER);
		RESERVED_TO_STATEMENT_MAP.put(MASON_BEFORE_OPENER, MOOSE_STATEMENT_BEFORE);

		RESERVED_OPENER_TO_CLOSER_MAP.put(MASON_AROUND_OPENER, MASON_AROUND_CLOSER);
		RESERVED_OPENER_TO_CLOSER_MAP.put(MASON_AUGMENT_OPENER, MASON_AUGMENT_CLOSER);
		RESERVED_OPENER_TO_CLOSER_MAP.put(MASON_AFTER_OPENER, MASON_AFTER_CLOSER);
		RESERVED_OPENER_TO_CLOSER_MAP.put(MASON_BEFORE_OPENER, MASON_BEFORE_CLOSER);

		SIMPLE_MASON_NAMED_BLOCKS = TokenSet.create(
				RESERVED_TO_STATEMENT_MAP.keySet().toArray(new IElementType[RESERVED_TO_STATEMENT_MAP.keySet().size()])
		);
	}

	@Override
	public boolean parseStatement(PerlBuilder b, int l)
	{
		IElementType tokenType = b.getTokenType();

		boolean r = false;
		PsiBuilder.Marker m = b.mark();

		if (tokenType == MASON_BLOCK_OPENER)
		{
			b.advanceLexer();
			if (PerlParser.expr(b, l, -1))
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
			r = PerlParserUtil.consumeToken(b, MASON_BLOCK_CLOSER);
		}
		else if (tokenType == MASON_CLASS_OPENER)
		{
			b.advanceLexer();

			while (!b.eof() && b.getTokenType() != MASON_CLASS_CLOSER)
			{
				PerlParser.file_item(b, l);
			}
			r = PerlParserUtil.consumeToken(b, MASON_CLASS_CLOSER);
		}
		else if (tokenType == MASON_INIT_OPENER)
		{
			b.advanceLexer();

			while (!b.eof() && b.getTokenType() != MASON_INIT_CLOSER)
			{
				PerlParser.file_item(b, l);
			}
			r = PerlParserUtil.consumeToken(b, MASON_INIT_CLOSER);
		}
		else if (tokenType == MASON_PERL_OPENER)
		{
			b.advanceLexer();

			while (!b.eof() && b.getTokenType() != MASON_PERL_CLOSER)
			{
				PerlParser.file_item(b, l);
			}
			r = PerlParserUtil.consumeToken(b, MASON_PERL_CLOSER);
		}
		else if (tokenType == MASON_FLAGS_OPENER)    // fixme need more love here, extends
		{
			b.advanceLexer();

			while (!b.eof() && b.getTokenType() != MASON_FLAGS_CLOSER)
			{
				PerlParser.expr(b, l, -1);
			}
			r = PerlParserUtil.consumeToken(b, MASON_FLAGS_CLOSER);
		}
		else if (tokenType == MASON_DOC_OPENER)
		{
			b.advanceLexer();
			PerlParserUtil.consumeToken(b, COMMENT_BLOCK);
			r = PerlParserUtil.consumeToken(b, MASON_DOC_CLOSER);
		}
		else if (tokenType == MASON_TEXT_OPENER)
		{
			b.advanceLexer();
			PerlParserUtil.consumeToken(b, STRING_CONTENT);
			r = PerlParserUtil.consumeToken(b, MASON_TEXT_CLOSER);
		}
		else if (SIMPLE_MASON_NAMED_BLOCKS.contains(tokenType)) // simple named blocks
		{
			PsiBuilder.Marker statementMarker = b.mark();
			b.advanceLexer();
			PsiBuilder.Marker methodMarker = b.mark();

			if (PerlParserUtil.convertIdentifier(b, l, SUB))
			{
				methodMarker.done(METHOD);

				if (PerlParserUtil.consumeToken(b, MASON_TAG_CLOSER))
				{
					PsiBuilder.Marker blockMarker = b.mark();
					PerlParser.block_content(b, l);
					blockMarker.done(BLOCK);
					blockMarker.setCustomEdgeTokenBinders(WhitespacesBinders.GREEDY_LEFT_BINDER, WhitespacesBinders.GREEDY_RIGHT_BINDER);

					if (r = PerlParserUtil.consumeToken(b, RESERVED_OPENER_TO_CLOSER_MAP.get(tokenType)))
					{
						statementMarker.done(RESERVED_TO_STATEMENT_MAP.get(tokenType));
					}
				}
			}
		}

		if (r)
		{
			m.drop();
		}
		else
		{
			m.rollbackTo();
		}

		return r;
	}


	@Nullable
	@Override
	public TokenSet getStatementRecoveryTokens()
	{
		return STATEMENT_RECOVERY_TOKENSET;
	}

	@Nullable
	@Override
	public TokenSet getBlockRecoveryTokens()
	{
		return BLOCK_RECOVERY_TOKENSET;
	}

	@Nullable
	@Override
	public TokenSet getBadCharacterForbiddenTokens()
	{
		return BAD_CHARACTER_FORBIDDEN_TOKENS;
	}
}
