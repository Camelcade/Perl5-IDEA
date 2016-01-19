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

package com.perl5.lang.perl.parser.moose;

import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.parser.elementTypes.PerlElementType;
import com.perl5.lang.perl.parser.elementTypes.PerlTokenType;
import com.perl5.lang.perl.parser.elementTypes.PerlTokenTypeEx;
import com.perl5.lang.perl.parser.moose.psi.impl.PerlMooseInnerKeywordImpl;
import com.perl5.lang.perl.parser.moose.psi.impl.PerlMooseSuperKeywordImpl;
import com.perl5.lang.perl.parser.moose.stubs.attribute.PerlMooseAttributeStubElementType;
import com.perl5.lang.perl.parser.moose.stubs.override.PerlMooseOverrideStubElementType;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 27.12.2015.
 */
public interface MooseElementTypes
{
	IElementType RESERVED_INNER = new PerlTokenTypeEx("inner")
	{
		@NotNull
		@Override
		public ASTNode createLeafNode(CharSequence leafText)
		{
			return new PerlMooseInnerKeywordImpl(this, leafText);
		}
	};
	IElementType RESERVED_SUPER = new PerlTokenTypeEx("super")
	{
		@NotNull
		@Override
		public ASTNode createLeafNode(CharSequence leafText)
		{
			return new PerlMooseSuperKeywordImpl(this, leafText);
		}
	};
	IElementType RESERVED_WITH = new PerlTokenType("with");
	IElementType RESERVED_EXTENDS = new PerlTokenType("extends");
	IElementType RESERVED_META = new PerlTokenType("meta");
	IElementType RESERVED_OVERRIDE = new PerlTokenType("override");
	IElementType RESERVED_AROUND = new PerlTokenType("around");
	IElementType RESERVED_AUGMENT = new PerlTokenType("augment");
	IElementType RESERVED_AFTER = new PerlTokenType("after");
	IElementType RESERVED_BEFORE = new PerlTokenType("before");
	IElementType RESERVED_HAS = new PerlTokenType("has");


	IElementType MOOSE_STATEMENT_INNER = new PerlElementType("MOOSE_STATEMENT_INNER");
	IElementType MOOSE_STATEMENT_WITH = new PerlElementType("MOOSE_STATEMENT_WITH");
	IElementType MOOSE_STATEMENT_EXTENDS = new PerlElementType("MOOSE_STATEMENT_EXTENDS");
	IElementType MOOSE_STATEMENT_META = new PerlElementType("MOOSE_STATEMENT_META");
	IElementType MOOSE_STATEMENT_OVERRIDE = new PerlMooseOverrideStubElementType("MOOSE_STATEMENT_OVERRIDE");
	IElementType MOOSE_STATEMENT_AROUND = new PerlElementType("MOOSE_STATEMENT_AROUND");
	IElementType MOOSE_STATEMENT_SUPER = new PerlElementType("MOOSE_STATEMENT_SUPER");
	IElementType MOOSE_STATEMENT_AUGMENT = new PerlElementType("MOOSE_STATEMENT_AUGMENT");
	IElementType MOOSE_STATEMENT_AFTER = new PerlElementType("MOOSE_STATEMENT_AFTER");
	IElementType MOOSE_STATEMENT_BEFORE = new PerlElementType("MOOSE_STATEMENT_BEFORE");

	IElementType MOOSE_STATEMENT_HAS = new PerlElementType("MOOSE_STATEMENT_HAS");
	IElementType MOOSE_ATTRIBUTE = new PerlMooseAttributeStubElementType("MOOSE_ATTRIBUTE");
}
