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
import com.intellij.openapi.util.Pair;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.perl.extensions.parser.PerlParserExtension;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.parser.builder.PerlBuilder;
import com.perl5.lang.perl.parser.moose.psi.PerlMooseHasStatementImpl;
import com.perl5.lang.perl.parser.perlswitch.psi.PerlCaseCompoundStatementImpl;
import com.perl5.lang.perl.parser.perlswitch.psi.PerlSwitchCompoundStatementImpl;
import gnu.trove.THashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by hurricup on 15.12.2015.
 */
public class PerlSwitchParserExtensionImpl extends PerlParserExtension implements PerlSwitchParserExtension, PerlElementTypes
{
	protected static final THashMap<String, IElementType> TOKENS_MAP = new THashMap<String, IElementType>();
	protected static TokenSet TOKENS_SET;

	static
	{
		// in regular case, these tokens should be created in extension class
		TOKENS_MAP.put("case", RESERVED_CASE);
		TOKENS_MAP.put("switch", RESERVED_SWITCH);

		TOKENS_SET = TokenSet.create(TOKENS_MAP.values().toArray(new IElementType[TOKENS_MAP.values().size()]));
	}

	@NotNull
	@Override
	public Map<String, IElementType> getReservedTokens()
	{
		return TOKENS_MAP;
	}

	@Nullable
	@Override
	public PsiElement createElement(@NotNull ASTNode node)
	{
		IElementType tokenType = node.getElementType();

		if (tokenType == SWITCH_COMPOUND)
		{
			return new PerlSwitchCompoundStatementImpl(node);
		}
		else if (tokenType == CASE_COMPOUND)
		{
			return new PerlCaseCompoundStatementImpl(node);
		}

		return super.createElement(node);
	}

	@Override
	public boolean parse(PerlBuilder b, int l)
	{
		return false;
	}
}
