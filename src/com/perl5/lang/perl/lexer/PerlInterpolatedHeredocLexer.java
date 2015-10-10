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
import com.intellij.psi.tree.IElementType;

import java.io.IOException;

/**
 * Created by hurricup on 16.08.2015.
 */
public class PerlInterpolatedHeredocLexer extends PerlStringLexer
{
	public PerlInterpolatedHeredocLexer(Project project)
	{
		super(project);
	}

	@Override
	public IElementType perlAdvance() throws IOException
	{
		int bufferEnd = getBufferEnd();
		int tokenStart = getTokenEnd();

		if (tokenStart < bufferEnd)
		{
			if (tokenStart == bufferStart)
			{
				setTokenStart(tokenStart);
				setTokenEnd(tokenStart + 1);
				return HEREDOC_PSEUDO_QUOTE;
			}
		}

		return super.perlAdvance();
	}
}
