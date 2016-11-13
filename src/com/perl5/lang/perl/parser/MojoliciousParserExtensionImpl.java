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
import com.intellij.lang.parser.GeneratedParserUtilBase;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.perl.extensions.parser.PerlParserExtension;
import com.perl5.lang.perl.parser.builder.PerlBuilder;
import gnu.trove.THashMap;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Map;

import static com.intellij.lang.parser.GeneratedParserUtilBase.consumeToken;

/**
 * Created by hurricup on 23.04.2016.
 */
public class MojoliciousParserExtensionImpl extends PerlParserExtension implements MojoliciousParserExtension
{
	protected static final THashMap<String, IElementType> TOKENS_MAP = new THashMap<String, IElementType>();
	final static GeneratedParserUtilBase.Parser MOJO_HELPER_PARSER = (builder_, level_) -> consumeToken(builder_, MOJO_HELPER_METHOD);
	final static GeneratedParserUtilBase.Parser HELPER_DECLARATION_PARSER = (b, l) ->
	{
		PsiBuilder.Marker wrapperMarker = b.mark();
		if (PerlParserImpl.optional_expression(b, l))
		{
			wrapperMarker.done(MOJO_HELPER_DECLARATION);
			return true;
		}
		wrapperMarker.rollbackTo();
		return false;
	};
	protected static TokenSet TOKENS_SET;

	static
	{
		TOKENS_MAP.put(KEYWORD_MOJO_HELPER_METHOD, MOJO_HELPER_METHOD);

		TOKENS_SET = TokenSet.create(TOKENS_MAP.values().toArray(new IElementType[TOKENS_MAP.values().size()]));
	}

	public static TokenSet getTokenSet()
	{
		return TOKENS_SET;
	}

	protected static boolean parseHelperDeclaration(PerlBuilder b, int l)
	{
		return PerlParserImpl.nested_call(b, l, MOJO_HELPER_PARSER, HELPER_DECLARATION_PARSER);
	}

	@NotNull
	@Override
	public Map<String, IElementType> getCustomTokensMap()
	{
		return Collections.emptyMap();
	}

	@Override
	public boolean parseNestedElement(PerlBuilder b, int l)
	{
		if (b.getTokenType() == MOJO_HELPER_METHOD)
		{
			return parseHelperDeclaration(b, l);
		}
		return super.parseNestedElement(b, l);
	}
}
