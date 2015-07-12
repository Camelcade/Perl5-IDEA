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

/**
 * Created by hurricup on 02.05.2015.
 */

import com.intellij.psi.tree.IElementType;

/**
 * Class for parsed token type, stores start, end and token type
 */
class CustomToken{
	private int tokenStart;
	private int tokenEnd;
	private IElementType tokenType;

	/**
	 * Creates parsed token entry
	 * @param start	token start offset
	 * @param end token end offset
	 * @param type token type
	 */
	public CustomToken( int start, int end, IElementType type )
	{
		tokenStart = start;
		tokenEnd = end;
		tokenType = type;
	}

	public int getTokenStart()
	{
		return tokenStart;
	}

	public int getTokenEnd()
	{
		return tokenEnd;
	}

	public IElementType getTokenType()
	{
		return tokenType;
	}

	public void setTokenType(IElementType tokenType)
	{
		this.tokenType = tokenType;
	}
}
