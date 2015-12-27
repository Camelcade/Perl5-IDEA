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

package com.perl5.lang.perl.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.extensions.parser.PerlParserExtension;
import com.perl5.lang.perl.parser.builder.PerlBuilder;

/**
 * Created by hurricup on 27.12.2015.
 */
public class MojoliciousPerlParserExtensionImpl extends PerlParserExtension implements MojoliciousPerlParserExtension
{

	@Override
	public boolean parse(PerlBuilder b, int l)
	{
		IElementType tokenType = b.getTokenType();

		if (tokenType == MOJO_BLOCK_EXPR_OPENER || tokenType == MOJO_BLOCK_EXPR_ESCAPED_OPENER)
		{
			b.advanceLexer();

			PsiBuilder.Marker m = b.mark();
			PerlParser.expr(b, l, -1);

			tokenType = b.getTokenType();
			if (tokenType == MOJO_BLOCK_EXPR_CLOSER || tokenType == MOJO_BLOCK_EXPR_NOSPACE_CLOSER)
			{
				m.done(STATEMENT);
				b.advanceLexer();
				return true;
			}
			else
			{
				m.rollbackTo();
			}
		}

		return super.parse(b, l);
	}
}
