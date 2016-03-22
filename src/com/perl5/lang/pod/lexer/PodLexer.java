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

package com.perl5.lang.pod.lexer;

import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;

import java.io.IOException;
import java.io.Reader;

/**
 * Created by hurricup on 22.03.2016.
 */
public class PodLexer extends PodLexerGenerated
{
	public PodLexer(Reader in)
	{
		super(in);
	}

	@Override
	public void reset(CharSequence buffer, int start, int end, int initialState)
	{
		super.reset(buffer, start, end, initialState);
		if (start == 0)
		{
			yybegin(LEX_NEWLINE);
		}
	}

	@Override
	public IElementType advance() throws IOException
	{
		IElementType result = super.advance();
		if (result != TokenType.NEW_LINE_INDENT && yystate() == LEX_NEWLINE)
		{
			yybegin(YYINITIAL);
		}
		return result;
	}
}
