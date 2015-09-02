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

package com.perl5.lang.embedded.lexer;

import com.intellij.openapi.project.Project;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.lexer.PerlLexer;

import java.io.IOException;

/**
 * Created by hurricup on 19.05.2015.
 */
public class EmbeddedPerlLexer extends PerlLexer
{
	private int preHTMLState = YYINITIAL;

	public EmbeddedPerlLexer(Project project)
	{
		super(project);
	}

	@Override
	public void reset(CharSequence buf, int start, int end, int initialState)
	{
		if (start == 0)
			initialState = LEX_HTML_BLOCK;

		super.reset(buf, start, end, initialState);
	}

	public IElementType perlAdvance() throws IOException
	{
		CharSequence buffer = getBuffer();
		int tokenStart = getNextTokenStart();
		int bufferEnd = buffer.length();
		int currentState = yystate();

		if (bufferEnd == 0 || tokenStart >= bufferEnd)
			return super.perlAdvance();
		else
		{
			if (currentState == LEX_HTML_BLOCK)
			{
				setTokenStart(tokenStart);
				if (bufferAtString(buffer, tokenStart, "<?")) // finishing html block
				{
					yybegin(preHTMLState);
					setTokenEnd(tokenStart + 2);
					return EMBED_MARKER_OPEN;
				} else
				{
					// fixme how about end of file?
					int offset = tokenStart;
					for (; offset < bufferEnd; offset++)
						if (bufferAtString(buffer, offset, "<?"))
							break;

					if (offset == bufferEnd)
						yybegin(preHTMLState);
					setTokenEnd(offset);
					return TEMPLATE_BLOCK_HTML;
				}
			} else if (bufferAtString(buffer, tokenStart, "?>"))
			{
				if (tokenStart < bufferEnd - 2)
				{
					preHTMLState = currentState;
					yybegin(LEX_HTML_BLOCK);
				}
				setTokenStart(tokenStart);
				setTokenEnd(tokenStart + 2);
				return EMBED_MARKER_CLOSE;
			}
		}
		return super.perlAdvance();
	}

	@Override
	public boolean isLineCommentEnd(int currentPosition)
	{
		CharSequence buffer = getBuffer();
		return buffer.charAt(currentPosition) == '\n' || bufferAtString(buffer, currentPosition, "?>");
	}
}
