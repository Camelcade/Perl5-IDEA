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

package com.perl5.lang.perl.lexer;

import com.intellij.psi.tree.IElementType;

import java.io.IOException;

/**
 * Created by hurricup on 16.08.2015.
 */
public class PerlHeredocLexer extends PerlStringLexer
{
	final protected String myType;

	public PerlHeredocLexer(String myType)
	{
		this.myType = myType;
	}

	@Override
	public IElementType perlAdvance() throws IOException
	{
		int tokenStart = getTokenStart();
		int tokenEnd = getTokenEnd();
		int bufferEnd = getBufferEnd();
		CharSequence buffer = getBuffer();

		if (tokenStart == 0 && getBufferEnd() > 0 && tokenEnd == 0)
		{
			if ("HEREDOC".equals(myType) && getBufferEnd() > 1)
				preparsedTokensList.add(new CustomToken(1, getBufferEnd(), STRING_CONTENT));

			setTokenStart(0);
			setTokenEnd(1);
			return HEREDOC_PSEUDO_QUOTE;
		} else if (tokenEnd == 1 && bufferEnd > 1 && Character.isWhitespace(buffer.charAt(tokenEnd)))
		{
			// hack for leading spaces
			setTokenStart(tokenEnd);
			while (tokenEnd < bufferEnd && Character.isWhitespace(buffer.charAt(tokenEnd)))
				tokenEnd++;
			setTokenEnd(tokenEnd);
			return STRING_CONTENT;
		}


		return super.perlAdvance();
	}
}
