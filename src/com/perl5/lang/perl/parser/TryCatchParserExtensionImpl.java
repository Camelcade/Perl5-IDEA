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
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.perl.extensions.parser.PerlParserExtension;
import com.perl5.lang.perl.idea.configuration.settings.PerlSharedSettings;
import com.perl5.lang.perl.parser.builder.PerlBuilder;
import gnu.trove.THashMap;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Created by hurricup on 17.04.2016.
 */
public class TryCatchParserExtensionImpl extends PerlParserExtension implements TryCatchParserExtension
{
	protected static final THashMap<String, IElementType> TOKENS_MAP = new THashMap<String, IElementType>();
	protected static TokenSet TOKENS_SET;

	static
	{
		// in regular case, these tokens should be created in extension class
		TOKENS_MAP.put("try", RESERVED_TRY);
		TOKENS_MAP.put("catch", RESERVED_CATCH);

		TOKENS_SET = TokenSet.create(TOKENS_MAP.values().toArray(new IElementType[TOKENS_MAP.values().size()]));
	}

	public static TokenSet getTokensSet()
	{
		return TOKENS_SET;
	}

	public static boolean parseTryStatement(PerlBuilder b, int l)
	{
		if (parseTryCompound(b, l))
		{
			while (true)
			{
				if (!parseCatchCompound(b, l))
					break;
			}
			return true;
		}
		return false;
	}

	public static boolean parseTryCompound(PerlBuilder b, int l)
	{
		if (b.getTokenType() == RESERVED_TRY)
		{
			PsiBuilder.Marker m = b.mark();
			b.advanceLexer();

			if (PerlParserImpl.block(b, l))
			{
				m.done(TRY_COMPOUND);
				return true;
			}
			else
			{
				m.rollbackTo();
			}
		}
		return false;
	}

	public static boolean parseCatchCompound(PerlBuilder b, int l)
	{
		if (b.getTokenType() == RESERVED_CATCH)
		{
			PsiBuilder.Marker m = b.mark();
			b.advanceLexer();

			parseCatchCondition(b, l);

			if (PerlParserImpl.block(b, l))
			{
				m.done(CATCH_COMPOUND);
				return true;
			}
			else
			{
				m.rollbackTo();
			}

		}
		return false;
	}

	public static boolean parseCatchCondition(PerlBuilder b, int l)
	{
		if (b.getTokenType() == LEFT_PAREN)
		{
			PsiBuilder.Marker m = b.mark();
			b.advanceLexer();

			parseCatchConditionDeclarationBody(b, l);

			if (PerlParserUtil.consumeToken(b, RIGHT_PAREN))
			{
				m.done(PARENTHESISED_EXPR);
				return true;
			}
			else
			{
				m.rollbackTo();
			}
		}
		return false;
	}

	public static boolean parseCatchConditionDeclarationBody(PerlBuilder b, int l)
	{
		PsiBuilder.Marker m = b.mark();

		boolean pr = PerlParserUtil.mergePackageName(b, l);
		boolean r = PerlParserUtil.scalarDeclarationWrapper(b, l);

		if (r || pr)
		{
			m.done(CATCH_CONDITION);
		}
		else
		{
			m.rollbackTo();
		}

		if (!r)
		{
			b.mark().error("Exception variable expected");
		}

		PsiBuilder.Marker errorMarker = null;
		while (!b.eof() && PerlParserImpl.recover_parenthesised(b, l))
		{
			if (errorMarker == null)
			{
				errorMarker = b.mark();
			}
			b.advanceLexer();
		}

		if (errorMarker != null)
		{
			errorMarker.error("Close paren expected");
		}

		return r;
	}

	@NotNull
	@Override
	public Map<String, IElementType> getCustomTokensMap()
	{
		return TOKENS_MAP;
	}

	// fixme isn't this too expensive?
	private boolean isEnabled(PerlBuilder b)
	{
		return PerlSharedSettings.getInstance(b.getProject()).PERL_TRY_CATCH_ENABLED;
	}

	@Override
	public boolean parseStatement(PerlBuilder b, int l)
	{
		IElementType tokenType = b.getTokenType();
		if (tokenType == RESERVED_TRY && isEnabled(b))
		{
			return parseTryStatement(b, l);
		}
		else if (tokenType == RESERVED_CATCH && isEnabled(b))
		{
			PsiBuilder.Marker m = b.mark();
			if (parseCatchCompound(b, l))
			{
				m.error("Catch block without try block");
				return true;
			}
			else
			{
				m.rollbackTo();
			}
		}
		return super.parseStatement(b, l);
	}

}
