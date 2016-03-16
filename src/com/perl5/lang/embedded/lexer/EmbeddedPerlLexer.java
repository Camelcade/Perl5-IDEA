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
import com.perl5.lang.embedded.psi.EmbeddedPerlElementTypes;
import com.perl5.lang.perl.lexer.PerlTemplatingLexer;

import java.io.IOException;

/**
 * Created by hurricup on 19.05.2015.
 */
public class EmbeddedPerlLexer extends PerlTemplatingLexer implements EmbeddedPerlElementTypes
{
	// lexical states
	public static final int LEX_HTML_BLOCK = LEX_CUSTOM1;             // template block
	public static final int LEX_PERL_BLOCK = LEX_CUSTOM2;             // template block

	public EmbeddedPerlLexer(Project project)
	{
		super(project);
	}

	public IElementType perlAdvance() throws IOException
	{
		CharSequence buffer = getBuffer();
		int tokenStart = getNextTokenStart();
		int bufferEnd = getBufferEnd();
		int currentState = getCustomState();

		if (bufferEnd == 0 || tokenStart >= bufferEnd)
		{
			return super.perlAdvance();
		}
		else if (currentState == LEX_HTML_BLOCK)
		{
			int offset = tokenStart;
			boolean blockStart = false;

			for (; offset < bufferEnd; offset++)
			{
				if (offset + 1 < bufferEnd && buffer.charAt(offset) == '<' && buffer.charAt(offset + 1) == '?')
				{
					blockStart = true;
					break;
				}
			}

			if (offset > tokenStart)
			{
				pushPreparsedToken(tokenStart, offset, EMBED_TEMPLATE_BLOCK_HTML);
			}

			if (blockStart)
			{
				pushPreparsedToken(offset, offset + 2, EMBED_MARKER_OPEN);
				setCustomState(LEX_PERL_BLOCK);
			}

			assert preparsedTokensList.size() > 0;
			return getPreParsedToken();
		}
		else if (currentState == LEX_PERL_BLOCK)
		{
			if (isCloseToken(buffer, tokenStart))
			{
				setTokenStart(tokenStart);
				setTokenEnd(tokenStart + 2);
				setCustomState(LEX_HTML_BLOCK);
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

	protected boolean isCloseToken(CharSequence buffer, int offset)
	{
		return offset + 1 < getBufferEnd() && buffer.charAt(offset) == '?' && buffer.charAt(offset + 1) == '>';
	}

	@Override
	public int getInitialCustomState()
	{
		return LEX_HTML_BLOCK;
	}
}
