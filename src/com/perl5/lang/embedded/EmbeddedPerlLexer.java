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

package com.perl5.lang.embedded;

import com.intellij.psi.tree.IElementType;
import com.intellij.util.text.ImmutableText;
import com.perl5.lang.perl.lexer.PerlLexer;

import java.io.IOException;

/**
 * Created by hurricup on 19.05.2015.
 */
public class EmbeddedPerlLexer extends PerlLexer
{
	public EmbeddedPerlLexer(java.io.Reader in) {
		super(in);
	}

	@Override
	public void reset(CharSequence buf, int start, int end, int initialState)
	{
		if( start == 0 )
			initialState = LEX_HTML_BLOCK;

		super.reset(buf, start, end, initialState);
	}

	private int preHTMLState = YYINITIAL;

	public IElementType advance() throws IOException
	{
		CharSequence buffer = getBuffer();
		int tokenStart = getNextTokenStart();
		int bufferEnd = buffer.length();

		if( tokenStart < bufferEnd && yystate() == LEX_HTML_BLOCK )
		{
			setTokenStart(tokenStart);
			if(tokenStart < bufferEnd - 1 && buffer.charAt(tokenStart) == '<' && buffer.charAt(tokenStart+1) == '?') // finishing html block
			{
				setState(preHTMLState);
				setTokenEnd(tokenStart + 2);
				return EMBED_MARKER;
			}
			else
			{
				for (int offset = tokenStart; offset < bufferEnd - 3; offset++)
				{
					if (buffer.charAt(offset + 1) == '<' && buffer.charAt(offset + 2) == '?')
					{
						setTokenEnd(offset + 1);
						return TEMPLATE_BLOCK_HTML;
					}
				}
				setState(preHTMLState);
				setTokenEnd(bufferEnd);
				return TEMPLATE_BLOCK_HTML;
			}
		}
		else if( tokenStart < bufferEnd - 2 && buffer.charAt(tokenStart)=='?' && buffer.charAt(tokenStart+1)=='>')
		{
			preHTMLState = yystate();
			yybegin(LEX_HTML_BLOCK);
			setTokenStart(tokenStart);
			setTokenEnd(tokenStart + 2);
			return EMBED_MARKER;
		}
		else if( tokenStart < bufferEnd && buffer.charAt(tokenStart)=='#')
		{
			// comment may end on newline or ?>
			int currentPosition = tokenStart;
			setTokenStart(tokenStart);

			while( currentPosition < bufferEnd )
			{
				if( buffer.charAt(currentPosition) == '\n'
						|| ( currentPosition < bufferEnd - 3 && buffer.charAt(currentPosition + 1)=='?' && buffer.charAt(currentPosition+2)=='>')
				)
				{
					currentPosition++;
					break;
				}
				currentPosition++;
			}
			setTokenEnd(currentPosition);
			return PERL_COMMENT;
		}

		return super.advance();
	}
}
