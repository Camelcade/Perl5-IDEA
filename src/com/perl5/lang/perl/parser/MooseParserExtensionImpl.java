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
import com.intellij.openapi.util.Pair;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.perl.extensions.parser.PerlParserExtension;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.parser.builder.PerlBuilder;
import com.perl5.lang.perl.parser.builder.PerlStringWrapper;
import gnu.trove.THashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by hurricup on 25.11.2015.
 */
public class MooseParserExtensionImpl extends PerlParserExtension implements MooseParserExtension, PerlElementTypes
{
	protected static final PerlStringWrapper attributeWrapper = new PerlStringWrapper(MOOSE_ATTRIBUTE);
	protected static final THashMap<String, IElementType> TOKENS_MAP = new THashMap<String, IElementType>();
	protected static final THashMap<IElementType, IElementType> RESERVED_TO_STATEMENT_MAP = new THashMap<IElementType, IElementType>();
	@SuppressWarnings("unchecked")
	protected static final List<Pair<IElementType, TokenSet>> EXTENSION_SET = Collections.singletonList(
			Pair.create(EXPR, TokenSet.create(MOOSE_ATTRIBUTE))
	);
	protected static TokenSet PARSER_TOKEN_SET;
	protected static TokenSet HIGHLIGHTER_TOKEN_SET;

	static
	{
		// in regular case, these tokens should be created in extension class
		TOKENS_MAP.put("inner", RESERVED_INNER);
		TOKENS_MAP.put("with", RESERVED_WITH);
		TOKENS_MAP.put("extends", RESERVED_EXTENDS);
		TOKENS_MAP.put("meta", RESERVED_META);
		TOKENS_MAP.put("override", RESERVED_OVERRIDE);
		TOKENS_MAP.put("around", RESERVED_AROUND);
		TOKENS_MAP.put("super", RESERVED_SUPER);
		TOKENS_MAP.put("augment", RESERVED_AUGMENT);
		TOKENS_MAP.put("after", RESERVED_AFTER);
		TOKENS_MAP.put("before", RESERVED_BEFORE);
		TOKENS_MAP.put("has", RESERVED_HAS);

		RESERVED_TO_STATEMENT_MAP.put(RESERVED_WITH, MOOSE_STATEMENT_WITH);
		RESERVED_TO_STATEMENT_MAP.put(RESERVED_EXTENDS, MOOSE_STATEMENT_EXTENDS);
		RESERVED_TO_STATEMENT_MAP.put(RESERVED_META, MOOSE_STATEMENT_META);
		RESERVED_TO_STATEMENT_MAP.put(RESERVED_AROUND, MOOSE_STATEMENT_AROUND);
		RESERVED_TO_STATEMENT_MAP.put(RESERVED_AUGMENT, MOOSE_STATEMENT_AUGMENT);
		RESERVED_TO_STATEMENT_MAP.put(RESERVED_AFTER, MOOSE_STATEMENT_AFTER);
		RESERVED_TO_STATEMENT_MAP.put(RESERVED_BEFORE, MOOSE_STATEMENT_BEFORE);
		RESERVED_TO_STATEMENT_MAP.put(RESERVED_HAS, MOOSE_STATEMENT_HAS);

		PARSER_TOKEN_SET = TokenSet.create(RESERVED_TO_STATEMENT_MAP.keySet().toArray(new IElementType[RESERVED_TO_STATEMENT_MAP.keySet().size()]));
		HIGHLIGHTER_TOKEN_SET = TokenSet.create(TOKENS_MAP.values().toArray(new IElementType[TOKENS_MAP.values().size()]));
	}

	public static TokenSet getHighlighterTokenSet()
	{
		return HIGHLIGHTER_TOKEN_SET;
	}

	private static boolean parseOverride(PerlBuilder b, int l)
	{
		return parseAnnotatedSimpleStatement(b, l, RESERVED_OVERRIDE, MOOSE_STATEMENT_OVERRIDE);
	}

	private static boolean parseHas(PerlBuilder b, int l)
	{
		PerlBuilder.Marker m = b.mark();

		if (PerlParserUtil.consumeToken(b, RESERVED_HAS) && parseHasArguments(b, l))
		{
			PerlParserUtil.parseStatementModifier(b, l);
			m.done(MOOSE_STATEMENT_HAS);
			return true;
		}

		m.rollbackTo();
		return false;
	}

	private static boolean parseHasArguments(PerlBuilder b, int l)
	{
		return parseHasArgumentsParenthesised(b, l) || parseHasArgumentsBare(b, l);
	}

	private static boolean parseHasArgumentsParenthesised(PerlBuilder b, int l)
	{
		if (b.getTokenType() == LEFT_PAREN)
		{
			PerlBuilder.Marker m = b.mark();
			if (PerlParserUtil.consumeToken(b, LEFT_PAREN) && parseHasArgumentsBare(b, l) && PerlParserUtil.consumeToken(b, RIGHT_PAREN))
			{
				m.done(CALL_ARGUMENTS);
				return true;
			}
			m.rollbackTo();
		}
		return false;
	}

	private static boolean parseHasArgumentsBare(PerlBuilder b, int l)
	{
		PsiBuilder.Marker m = b.mark();
		if (parseHasArgumentsSequenceContent(b, l))
		{
			m.done(COMMA_SEQUENCE_EXPR);
			return true;
		}
		m.rollbackTo();
		return false;
	}

	private static boolean parseHasArgumentsSequenceContent(PerlBuilder b, int l)
	{
		PsiBuilder.Marker m = b.mark();
		if (parseHasAttributeDefinitions(b, l) &&
				(PerlParserUtil.consumeToken(b, OPERATOR_COMMA) || PerlParserUtil.consumeToken(b, OPERATOR_COMMA_ARROW)) &&
				PerlParserImpl.expr(b, l, -1)
				)
		{
			m.drop();
			return true;
		}
		m.rollbackTo();
		return false;
	}

	private static boolean parseHasAttributeDefinitions(PerlBuilder b, int l)
	{
		PerlStringWrapper currentWrapper = b.setStringWrapper(attributeWrapper);
		boolean r = PerlParserImpl.scalar_expr(b, l - 1);
		b.setStringWrapper(currentWrapper);
		return r;
	}


	private static boolean parseAnnotatedSimpleStatement(PerlBuilder b, int l, IElementType keywordToken, IElementType statementToken)
	{
		PerlBuilder.Marker m = b.mark();

		if (PerlParserUtil.consumeToken(b, keywordToken))
		{
			if (PerlParserImpl.expr(b, l, -1))
			{
				PerlParserUtil.parseStatementModifier(b, l);
				PerlParserUtil.statementSemi(b, l);
				m.done(statementToken);
				return true;
			}
		}

		m.rollbackTo();
		return false;
	}

	private static boolean parseDefault(PerlBuilder b, int l)
	{
		PerlBuilder.Marker m = b.mark();

		IElementType tokenType = b.getTokenType();
		if (PARSER_TOKEN_SET.contains(tokenType))
		{
			b.advanceLexer();
			if (PerlParserImpl.expr(b, l, -1))
			{
				PerlParserUtil.parseStatementModifier(b, l);
				PerlParserUtil.statementSemi(b, l);
				m.done(RESERVED_TO_STATEMENT_MAP.get(tokenType));
				return true;
			}
		}

		m.rollbackTo();
		return false;
	}

	@NotNull
	@Override
	public Map<String, IElementType> getCustomTokensMap()
	{
		return TOKENS_MAP;
	}

	@Override
	public boolean parseStatement(PerlBuilder b, int l)
	{
		return parseOverride(b, l) ||
				parseHas(b, l) ||
				parseDefault(b, l)
				;
	}

	@Override
	public boolean parseTerm(PerlBuilder b, int l)
	{
		return
				parseMooseInvocation(b, l, RESERVED_INNER) ||
						parseMooseInvocation(b, l, RESERVED_SUPER) ||
						super.parseTerm(b, l);
	}

	public boolean parseMooseInvocation(PerlBuilder b, int l, IElementType keyToken)
	{
		if (b.getTokenType() == keyToken)
		{
			b.setNextSubElementType(keyToken);
			return PerlParserImpl.sub_call_expr(b, l);
		}
		return false;
	}


	@Nullable
	@Override
	public List<Pair<IElementType, TokenSet>> getExtensionSets()
	{
		return EXTENSION_SET;
	}
}
