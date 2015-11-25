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

import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.parser.builder.PerlBuilder;
import com.perl5.lang.perl.parser.moose.*;
import gnu.trove.THashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * Created by hurricup on 25.11.2015.
 */
public class PerlMooseParserExtensionImpl extends PerlParserExtension implements PerlMooseParserExtension, PerlElementTypes
{

	protected static final THashMap<String, IElementType> TOKENS_MAP = new THashMap<String, IElementType>();
	protected static final THashMap<IElementType, IElementType> RESERVED_TO_STATEMENT_MAP = new THashMap<IElementType, IElementType>();

	protected static TokenSet MOOSE_TOKEN_SET;

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

		RESERVED_TO_STATEMENT_MAP.put(RESERVED_INNER, MOOSE_STATEMENT_INNER);
		RESERVED_TO_STATEMENT_MAP.put(RESERVED_WITH, MOOSE_STATEMENT_WITH);
		RESERVED_TO_STATEMENT_MAP.put(RESERVED_EXTENDS, MOOSE_STATEMENT_EXTENDS);
		RESERVED_TO_STATEMENT_MAP.put(RESERVED_META, MOOSE_STATEMENT_META);
		RESERVED_TO_STATEMENT_MAP.put(RESERVED_OVERRIDE, MOOSE_STATEMENT_OVERRIDE);
		RESERVED_TO_STATEMENT_MAP.put(RESERVED_AROUND, MOOSE_STATEMENT_AROUND);
		RESERVED_TO_STATEMENT_MAP.put(RESERVED_SUPER, MOOSE_STATEMENT_SUPER);
		RESERVED_TO_STATEMENT_MAP.put(RESERVED_AUGMENT, MOOSE_STATEMENT_AUGMENT);
		RESERVED_TO_STATEMENT_MAP.put(RESERVED_AFTER, MOOSE_STATEMENT_AFTER);
		RESERVED_TO_STATEMENT_MAP.put(RESERVED_BEFORE, MOOSE_STATEMENT_BEFORE);
		RESERVED_TO_STATEMENT_MAP.put(RESERVED_HAS, MOOSE_STATEMENT_HAS);

		MOOSE_TOKEN_SET = TokenSet.create(TOKENS_MAP.values().toArray(new IElementType[TOKENS_MAP.values().size()]));
	}

	@NotNull
	@Override
	public Map<String, IElementType> getReservedTokens()
	{
		return TOKENS_MAP;
	}

	@Override
	public boolean parse(PerlBuilder b, int l)
	{
		// fallback branch
		if (MOOSE_TOKEN_SET.contains(b.getTokenType()))
		{
			IElementType tokenType = b.getTokenType();
			PsiBuilder.Marker m = b.mark();
			b.advanceLexer();
			if (PerlParser.expr(b, l, -1))
			{
				m.done(RESERVED_TO_STATEMENT_MAP.get(tokenType));
				return true;
			}
			else
			{
				m.drop();
			}
		}

		return false;
	}

	@Nullable
	@Override
	public PsiElement createElement(@NotNull ASTNode node)
	{
		IElementType tokenType = node.getElementType();

		if (tokenType == MOOSE_STATEMENT_HAS)
		{
			return new PerlMooseHasStatementImpl(node);
		}
		else if (tokenType == MOOSE_STATEMENT_INNER)
		{
			return new PerlMooseInnerStatementImpl(node);

		}
		else if (tokenType == MOOSE_STATEMENT_WITH)
		{
			return new PerlMooseWithStatementImpl(node);

		}
		else if (tokenType == MOOSE_STATEMENT_EXTENDS)
		{
			return new PerlMooseExtendsStatementImpl(node);

		}
		else if (tokenType == MOOSE_STATEMENT_META)
		{
			return new PerlMooseMetaStatementImpl(node);

		}
		else if (tokenType == MOOSE_STATEMENT_OVERRIDE)
		{
			return new PerlMooseOverrideStatementImpl(node);

		}
		else if (tokenType == MOOSE_STATEMENT_AROUND)
		{
			return new PerlMooseAroundStatementImpl(node);

		}
		else if (tokenType == MOOSE_STATEMENT_SUPER)
		{
			return new PerlMooseSuperStatementImpl(node);

		}
		else if (tokenType == MOOSE_STATEMENT_AUGMENT)
		{
			return new PerlMooseAugmentStatementImpl(node);

		}
		else if (tokenType == MOOSE_STATEMENT_AFTER)
		{
			return new PerlMooseAfterStatementImpl(node);

		}
		else if (tokenType == MOOSE_STATEMENT_BEFORE)
		{
			return new PerlMooseBeforeStatementImpl(node);

		}

		return null;
	}
}
