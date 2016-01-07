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

package com.perl5.lang.mason.lexer;

import com.intellij.openapi.project.Project;
import com.intellij.psi.tree.IElementType;

import java.io.IOException;

/**
 * Created by hurricup on 20.12.2015.
 */
public class MasonDebuggingLexer extends MasonLexer
{
	public MasonDebuggingLexer(Project project)
	{
		super(project);
	}

	@Override
	public IElementType advance() throws IOException
	{
		IElementType tokenType = super.advance();

		System.err.println(String.format("Type: %s Value: %s Range: %d - %d State: %d Preparsed: %d", tokenType, yytext(), getTokenStart(), getTokenEnd(), yystate(), preparsedTokensList.size()));

		return tokenType;
	}

	@Override
	public void reset(CharSequence buf, int start, int end, int initialState)
	{
		System.err.println(String.format("Resetting lexer: Range: %d - %d, State: %d, Perparsed: %d", start, end, initialState, preparsedTokensList.size()));
		super.reset(buf, start, end, initialState);
	}

}
