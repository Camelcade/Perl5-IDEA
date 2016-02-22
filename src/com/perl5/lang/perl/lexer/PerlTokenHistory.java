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
import com.perl5.lang.perl.PerlParserDefinition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hurricup on 11.10.2015.
 */
public class PerlTokenHistory implements PerlElementTypes
{
	private final List<PerlTokenHistoryElement> myHistory = new ArrayList<PerlTokenHistoryElement>();

	private PerlTokenHistoryElement myLastToken;
	private PerlTokenHistoryElement myLastSignificantToken;
	private PerlTokenHistoryElement myLastUnbracedToken;
	private PerlTokenHistoryElement myLastUnparenToken;

	public void addToken(IElementType tokenType, CharSequence tokenText)
	{
		myHistory.add(myLastToken = new PerlTokenHistoryElement(tokenType, tokenText));
		if (myLastToken.isSignificant())
		{
			myLastSignificantToken = myLastToken;

			if (myLastToken.getTokenType() != LEFT_BRACE)
			{
				myLastUnbracedToken = myLastToken;
			}

			if (myLastToken.getTokenType() != LEFT_PAREN)
			{
				myLastUnparenToken = myLastToken;
			}
		}
	}

	@Nullable
	public PerlTokenHistoryElement getLastToken()
	{
		return myLastToken;
	}

	@Nullable
	public IElementType getLastTokenType()
	{
		return myLastToken == null ? null : myLastToken.getTokenType();
	}

	@Nullable
	public CharSequence getLastTokenText()
	{
		return myLastToken == null ? null : myLastToken.getTokenText();
	}

	@Nullable
	public PerlTokenHistoryElement getLastSignificantToken()
	{
		return myLastSignificantToken;
	}

	@Nullable
	public IElementType getLastSignificantTokenType()
	{
		return myLastSignificantToken == null ? null : myLastSignificantToken.getTokenType();
	}

	@Nullable
	public CharSequence getLastSignificantTokenText()
	{
		return myLastSignificantToken == null ? null : myLastSignificantToken.getTokenText();
	}

	@Nullable
	public PerlTokenHistoryElement getLastUnbracedToken()
	{
		return myLastUnbracedToken;
	}

	@Nullable
	public IElementType getLastUnbracedTokenType()
	{
		return myLastUnbracedToken == null ? null : myLastUnbracedToken.getTokenType();
	}

	@Nullable
	public CharSequence getLastUnbracedTokenText()
	{
		return myLastUnbracedToken == null ? null : myLastUnbracedToken.getTokenText();
	}

	@Nullable
	public PerlTokenHistoryElement getLastUnparenToken()
	{
		return myLastUnparenToken;
	}

	@Nullable
	public IElementType getLastUnparenTokenType()
	{
		return myLastUnparenToken == null ? null : myLastUnparenToken.getTokenType();
	}

	@Nullable
	public CharSequence getLastUnparenTokenText()
	{
		return myLastUnparenToken == null ? null : myLastUnparenToken.getTokenText();
	}

	@Nullable
	public String getLastUnparenTokenTextAsString()
	{
		return myLastUnparenToken == null ? null : myLastUnparenToken.getTokenTextAsString();
	}

	@NotNull
	public List<PerlTokenHistoryElement> getHistory()
	{
		return myHistory;
	}

	@Nullable
	public PerlTokenHistoryElement getPreviousToken(PerlTokenHistoryElement token)
	{
		int index = getHistory().lastIndexOf(token);
		if (index > 0)
		{
			return getHistory().get(index - 1);
		}
		return null;
	}

	public void reset()
	{
		myHistory.clear();
		myLastToken = null;
		myLastSignificantToken = null;
		myLastUnbracedToken = null;
		myLastUnparenToken = null;
	}


	public int size()
	{
		return myHistory.size();
	}

	public static class PerlTokenHistoryElement
	{
		private final IElementType myTokenType;
		private final CharSequence myTokenText;
		private final boolean myIsSignificant;
		private String myTokenString;

		public PerlTokenHistoryElement(IElementType tokenType, CharSequence tokenText)
		{
			myTokenText = tokenText;
			myTokenType = tokenType;
			myIsSignificant = !PerlParserDefinition.WHITE_SPACE_AND_COMMENTS.contains(tokenType);
		}

		public CharSequence getTokenText()
		{
			return myTokenText;
		}

		public String getTokenTextAsString()
		{
			if (myTokenString == null)
				myTokenString = getTokenText().toString();
			return myTokenString;
		}

		public IElementType getTokenType()
		{
			return myTokenType;
		}

		public boolean isSignificant()
		{
			return myIsSignificant;
		}
	}
}
