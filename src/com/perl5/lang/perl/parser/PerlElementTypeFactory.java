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
import com.perl5.lang.perl.PerlElementType;
import com.perl5.lang.perl.PerlTokenType;
import com.perl5.lang.perl.idea.stubs.PerlStubElementTypes;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 25.05.2015.
 */
public class PerlElementTypeFactory
{
	public static IElementType getTokenType(@NotNull String name)
	{
		if (name.equals("HEREDOC_QQ") || name.equals("HEREDOC_QX") || name.equals("HEREDOC"))
			return new PerlHeredocElementType(name);
		if (name.equals("PARSABLE_STRING"))
			return new PerlParsableStringElementType(name);
		return new PerlTokenType(name);
	}

	public static IElementType getElementType(@NotNull String name)
	{
		if (name.equals("SUB_DEFINITION"))
			return PerlStubElementTypes.SUB_DEFINITION;
		else if (name.equals("SUB_DECLARATION"))
			return PerlStubElementTypes.SUB_DECLARATION;
		else if (name.equals("GLOB_VARIABLE"))
			return PerlStubElementTypes.PERL_GLOB;
		else if (name.equals("NAMESPACE_DEFINITION"))
			return PerlStubElementTypes.PERL_NAMESPACE;
		else if (name.equals("SCALAR_VARIABLE"))
			return PerlStubElementTypes.PERL_SCALAR;
		else if (name.equals("ARRAY_VARIABLE"))
			return PerlStubElementTypes.PERL_ARRAY;
		else if (name.equals("ARRAY_INDEX_VARIABLE"))
			return PerlStubElementTypes.PERL_ARRAY_INDEX;
		else if (name.equals("HASH_VARIABLE"))
			return PerlStubElementTypes.PERL_HASH;
		else if (name.equals("CONSTANT_NAME"))
			return PerlStubElementTypes.PERL_CONSTANT;

		else if (name.equals("USE_STATEMENT"))
			return PerlStubElementTypes.PERL_USE_STATEMENT;

		return new PerlElementType(name);
	}
}
