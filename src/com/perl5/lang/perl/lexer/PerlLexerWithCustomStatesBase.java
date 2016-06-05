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

import com.intellij.openapi.project.Project;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hurricup on 20.12.2015.
 */
public abstract class PerlLexerWithCustomStatesBase extends PerlLexer implements PerlLexerWithCustomStates
{
	private int customState = 0;

	public PerlLexerWithCustomStatesBase(Project project)
	{
		super(project);
	}

	public int getCustomState()
	{
		return customState;
	}

	public void setCustomState(int newState)
	{
		customState = newState;
	}


	/**
	 * Lexes block cut off heading and tailing spaces/newlines and put them in the beginning of preparsed tokens
	 *
	 * @param blockStart                  block start offset
	 * @param blockEnd                    block end offset
	 * @param lastNonspaceCharacterOffset last non space char in the block, should be -1 if no non-whitespace elements found
	 * @param templateElementType         template element type to assign
	 */
	protected void reLexHTMLBLock(int blockStart, int blockEnd, int lastNonspaceCharacterOffset, IElementType templateElementType)
	{
		List<CustomToken> tokens = new ArrayList<CustomToken>();
		int myOffset = lexSpacesInRange(blockStart, blockEnd, tokens);

		// real template
		if (myOffset <= lastNonspaceCharacterOffset)
		{
			tokens.add(new CustomToken(myOffset, lastNonspaceCharacterOffset + 1, templateElementType));
		}

		if (lastNonspaceCharacterOffset > -1)
		{
			lexSpacesInRange(lastNonspaceCharacterOffset + 1, blockEnd, tokens);
		}

		for (int i = tokens.size() - 1; i >= 0; i--)
		{
			unshiftPreparsedToken(tokens.get(i));
		}
	}

	/**
	 * lex spaces and new lines in the given range until first nonspace char
	 *
	 * @param blockStart start offset
	 * @param blockEnd   end offset
	 * @param tokens     result tokens list
	 * @return offset of the blockEnd or first non-space character
	 */
	protected int lexSpacesInRange(int blockStart, int blockEnd, List<CustomToken> tokens)
	{
		int whiteSpaceTokenStart = -1;
		CharSequence buffer = getBuffer();
		while (blockStart < blockEnd)
		{
			char currentChar = buffer.charAt(blockStart);
			if (currentChar == '\n')
			{
				if (whiteSpaceTokenStart != -1)
				{
					tokens.add(new CustomToken(whiteSpaceTokenStart, blockStart, TokenType.WHITE_SPACE));
					whiteSpaceTokenStart = -1;
				}
				tokens.add(new CustomToken(blockStart, blockStart + 1, TokenType.NEW_LINE_INDENT));
			}
			else if (Character.isWhitespace(currentChar))
			{
				if (whiteSpaceTokenStart == -1)
				{
					whiteSpaceTokenStart = blockStart;
				}
			}
			else
			{
				if (whiteSpaceTokenStart != -1)
				{
					tokens.add(new CustomToken(whiteSpaceTokenStart, blockStart, TokenType.WHITE_SPACE));
					whiteSpaceTokenStart = -1;
				}
				break;
			}
			blockStart++;
		}
		if (whiteSpaceTokenStart != -1)
		{
			tokens.add(new CustomToken(whiteSpaceTokenStart, blockStart, TokenType.WHITE_SPACE));
		}
		return blockStart;
	}

}
