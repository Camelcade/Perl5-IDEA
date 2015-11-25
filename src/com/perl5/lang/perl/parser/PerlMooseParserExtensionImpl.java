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
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.parser.builder.PerlBuilder;
import gnu.trove.THashMap;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Created by hurricup on 25.11.2015.
 */
public class PerlMooseParserExtensionImpl extends PerlParserExtension implements PerlMooseParserExtension, PerlElementTypes
{

	protected static final THashMap<String, IElementType> TOKENS_MAP = new THashMap<String, IElementType>();
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
			PsiBuilder.Marker m = b.mark();
			b.advanceLexer();
			if (PerlParser.expr(b, l, -1))
			{
				m.done(STATEMENT);
				return true;
			}
			else
			{
				m.drop();
			}
		}

		return false;
	}
}
