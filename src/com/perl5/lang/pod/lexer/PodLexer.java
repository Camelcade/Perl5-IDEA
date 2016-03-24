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

import com.intellij.psi.tree.IElementType;

import java.io.IOException;
import java.io.Reader;

/**
 * Created by hurricup on 22.03.2016.
 */
@SuppressWarnings("ALL")
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
			yybegin(LEX_COMMAND_WAITING);
		}
	}

	@Override
	public IElementType advance() throws IOException
	{
		int state = yystate();

		if (state == LEX_CAPTURE_BLOCK)
		{
			int tokenStart = getTokenEnd();
			int tokenEnd = getTokenEnd();
			yybegin(YYINITIAL);
			while (true)
			{
				IElementType result = super.advance();
				if (result == null) // eof
				{
					if (tokenEnd > tokenStart)
					{
						setTokenStart(tokenStart);
						setTokenEnd(tokenEnd);
						yybegin(YYINITIAL);
						return POD_FORMATTED_BLOCK;
					}
					else
					{
						return null;
					}
				}
				else if (result == POD_END) // end
				{
					setTokenStart(tokenStart);
					setTokenEnd(tokenEnd);
					yybegin(LEX_COMMAND_WAITING);
					return POD_FORMATTED_BLOCK;
				}
				tokenEnd = getTokenEnd();
			}
		}
		else if (state == LEX_CAPTURE_LINE)
		{
			int tokenStart = getTokenEnd();
			int tokenEnd = getTokenEnd();
			yybegin(YYINITIAL);
			while (true)
			{
				IElementType result = super.advance();
				if (result == null) // eof
				{
					if (tokenEnd > tokenStart)
					{
						setTokenStart(tokenStart);
						setTokenEnd(tokenEnd);
						yybegin(YYINITIAL);
						return POD_FORMATTED_LINE;
					}
					else
					{
						return null;
					}
				}
				else if (result == POD_NEWLINE) // end
				{
					setTokenStart(tokenStart);
					setTokenEnd(tokenEnd);
					yybegin(YYINITIAL);
					return POD_FORMATTED_LINE;
				}
				tokenEnd = getTokenEnd();
			}
		}
		return super.advance();
//		IElementType result = super.advance();
//		System.err.println(String.format("Type: %s Value: %s Range: %d - %d State: %d", result, yytext(), getTokenStart(), getTokenEnd(), yystate()));
//		return result;
	}
}
