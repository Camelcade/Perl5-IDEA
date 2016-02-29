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

package com.perl5.lang.perl.lexer;

import com.intellij.openapi.project.Project;

/**
 * Created by hurricup on 29.02.2016.
 */
public class PerlQQHeredocLexer extends PerlStringLexer
{
	public PerlQQHeredocLexer(Project project)
	{
		super(project);
	}

/*
	@Override
	public void reset(CharSequence buffer, int start, int end, int initialState)
	{
		super.reset(buffer, start, end, initialState);
		System.err.println(String.format("Resetting lexer: Range: %d - %d, State: %d, Perparsed: %d", start, end, initialState, preparsedTokensList.size()));
	}

	@Override
	public IElementType advance() throws IOException
	{
		IElementType tokenType = super.advance();

		System.err.println(String.format("Type: %s Value: %s Range: %d - %d State: %d Preparsed: %d", tokenType, yytext(), getTokenStart(), getTokenEnd(), yystate(), preparsedTokensList.size()));

		return tokenType;
	}
*/
}
