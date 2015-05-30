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
import com.perl5.lang.perl.psi.stubs.PerlStubElementTypes;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 25.05.2015.
 */
public class PerlElementTypeFactory
{
	private PerlElementTypeFactory(){}

	public static IElementType factory(@NotNull String name)
	{
		if( name.equals("SUB_DEFINITION"))
			return PerlStubElementTypes.SUB_DEFINITION;
		else if( name.equals("PERL_GLOB"))
			return PerlStubElementTypes.PERL_GLOB;
		else if( name.equals("NAMESPACE_DEFINITION"))
			return PerlStubElementTypes.PERL_NAMESPACE;
		else if( name.equals("PERL_SCALAR"))
			return PerlStubElementTypes.PERL_SCALAR;
		else if( name.equals("PERL_ARRAY"))
			return PerlStubElementTypes.PERL_ARRAY;
		else if( name.equals("PERL_HASH"))
			return PerlStubElementTypes.PERL_HASH;

		throw new RuntimeException("Unknown element type "+name);
	}
}
