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

import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.parser.builder.PerlBuilder;
import gnu.trove.THashMap;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Created by hurricup on 22.11.2015.
 * class must be in com.perl5.lang.perl.parser package to have access to the PerlParser routines
 */
public class PerlMethodSignaturesSimpleParserExtension extends PerlParserExtension implements PerlElementTypes
{
	protected static final THashMap<String, IElementType> TOKENS_MAP = new THashMap<String, IElementType>();

	static
	{
		// in regular case, these tokens should be created in extension class
		TOKENS_MAP.put("method", RESERVED_METHOD);
		TOKENS_MAP.put("func", RESERVED_FUNC);
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
		// in regular case, these methods should
		return PerlParser.method_definition(b, l) || PerlParser.func_definition(b, l);
	}
}
