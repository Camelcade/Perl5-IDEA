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
 * Created by hurricup on 26.08.2015.
 */
public class PerlQuotedStringLexer extends PerlStringLexer
{
	@Override
	public IElementType advance() throws IOException
	{
		if (getTokenStart() == 0 && getBufferEnd() > 0 && getTokenEnd() == 0)
		{
			setTokenStart(0);
			setTokenEnd(1);
			return QUOTE_DOUBLE_OPEN;
		} else if (getTokenEnd() == getBufferEnd() - 1)
		{
			setTokenStart(getTokenEnd());
			setTokenEnd(getBufferEnd());
			return QUOTE_DOUBLE_CLOSE;
		}

		return super.advance();
	}

}
