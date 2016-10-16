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

import com.intellij.lang.parser.GeneratedParserUtilBase;
import com.intellij.lang.parser.GeneratedParserUtilBase.Parser;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.perl.extensions.parser.PerlParserExtension;
import com.perl5.lang.perl.parser.builder.PerlBuilder;
import com.perl5.lang.perl.parser.builder.PerlStringWrapper;
import gnu.trove.THashMap;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Map;

import static com.intellij.lang.parser.GeneratedParserUtilBase.consumeToken;

/**
 * Created by hurricup on 21.01.2016.
 */
public class ClassAccessorParserExtensionImpl extends PerlParserExtension implements ClassAccessorParserExtension
{
	protected static final THashMap<String, IElementType> TOKENS_MAP = new THashMap<String, IElementType>();
	protected static final PerlStringWrapper declarationWrapper = new PerlStringWrapper(CLASS_ACCESSOR_DECLARATION);
	final static GeneratedParserUtilBase.Parser FBP_PARSER = (builder_, level_) -> consumeToken(builder_, RESERVED_FOLLOW_BEST_PRACTICE);
	final static GeneratedParserUtilBase.Parser MK_ACCESSOR_PARSER = (builder_, level_) -> consumeToken(builder_, RESERVED_MK_ACCESSORS);
	final static GeneratedParserUtilBase.Parser MK_RO_ACCESSOR_PARSER = (builder_, level_) -> consumeToken(builder_, RESERVED_MK_RO_ACCESSORS);
	final static GeneratedParserUtilBase.Parser MK_WO_ACCESSOR_PARSER = (builder_, level_) -> consumeToken(builder_, RESERVED_MK_WO_ACCESSORS);
	protected static TokenSet TOKENS_SET;

	static
	{
		TOKENS_MAP.put("follow_best_practice", RESERVED_FOLLOW_BEST_PRACTICE);
		TOKENS_MAP.put("mk_accessors", RESERVED_MK_ACCESSORS);
		TOKENS_MAP.put("mk_ro_accessors", RESERVED_MK_RO_ACCESSORS);
		TOKENS_MAP.put("mk_wo_accessors", RESERVED_MK_WO_ACCESSORS);

		TOKENS_SET = TokenSet.create(TOKENS_MAP.values().toArray(new IElementType[TOKENS_MAP.values().size()]));
	}

	public static TokenSet getTokenSet()
	{
		return TOKENS_SET;
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
		IElementType elementType = b.getTokenType();
		if (elementType == RESERVED_MK_ACCESSORS)
		{
			return parseAccessorDeclarations(b, l, MK_ACCESSOR_PARSER);
		}
		else if (elementType == RESERVED_MK_RO_ACCESSORS)
		{
			return parseAccessorDeclarations(b, l, MK_RO_ACCESSOR_PARSER);
		}
		else if (elementType == RESERVED_MK_WO_ACCESSORS)
		{
			return parseAccessorDeclarations(b, l, MK_WO_ACCESSOR_PARSER);
		}
		else if (elementType == RESERVED_FOLLOW_BEST_PRACTICE)
		{
			b.setNextSubElementType(elementType);
			return PerlParserImpl.nested_call(b, l, FBP_PARSER);
		}

		return super.parseNestedElement(b, l);
	}

	protected boolean parseAccessorDeclarations(PerlBuilder b, int l, Parser subElementParser)
	{
		PerlStringWrapper currentWrapper = b.setStringWrapper(declarationWrapper);
		boolean r = PerlParserImpl.nested_call(b, l, subElementParser);
		b.setStringWrapper(currentWrapper);
		return r;
	}

}
