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
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.parser.builder.PerlBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Map;

/**
 * Created by hurricup on 21.12.2015.
 */
public class MasonPerlParserExtensionImpl extends PerlParserExtension implements MasonPerlParserExtension
{
	@Override
	public boolean parse(PerlBuilder b, int l)
	{
		IElementType tokenType = b.getTokenType();

		boolean r = false;
		PsiBuilder.Marker m = b.mark();

		if (tokenType == MASON_BLOCK_OPENER)
		{
			b.advanceLexer();
			if (PerlParser.expr(b, l, -1))
			{
				// parse filter
				if (PerlParserUtil.consumeToken(b, MASON_EXPR_FILTER_PIPE))
				{
					while (b.getTokenType() == IDENTIFIER)
					{
						PsiBuilder.Marker fm = b.mark();
						b.advanceLexer();
						fm.collapse(SUB);
						fm.precede().done(METHOD);

						if (!PerlParserUtil.consumeToken(b, OPERATOR_COMMA))
						{
							break;
						}
					}
				}
			}
			r = PerlParserUtil.consumeToken(b, MASON_BLOCK_CLOSER);
		}
		else if (tokenType == MASON_CLASS_OPENER)
		{
			b.advanceLexer();

			while (!b.eof() && b.getTokenType() != MASON_CLASS_CLOSER)
			{
				PerlParser.file_item(b, l);
			}
			r = PerlParserUtil.consumeToken(b, MASON_CLASS_CLOSER);
		}
		else if (tokenType == MASON_INIT_OPENER)
		{
			b.advanceLexer();

			while (!b.eof() && b.getTokenType() != MASON_INIT_CLOSER)
			{
				PerlParser.file_item(b, l);
			}
			r = PerlParserUtil.consumeToken(b, MASON_INIT_CLOSER);
		}
		else if (tokenType == MASON_PERL_OPENER)
		{
			b.advanceLexer();

			while (!b.eof() && b.getTokenType() != MASON_PERL_CLOSER)
			{
				PerlParser.file_item(b, l);
			}
			r = PerlParserUtil.consumeToken(b, MASON_PERL_CLOSER);
		}
		else if (tokenType == MASON_FLAGS_OPENER)    // fixme need more love here, extends
		{
			b.advanceLexer();

			while (!b.eof() && b.getTokenType() != MASON_FLAGS_CLOSER)
			{
				PerlParser.expr(b, l, -1);
			}
			r = PerlParserUtil.consumeToken(b, MASON_FLAGS_CLOSER);
		}
		else if (tokenType == MASON_DOC_OPENER)
		{
			b.advanceLexer();
			PerlParserUtil.consumeToken(b, COMMENT_BLOCK);
			r = PerlParserUtil.consumeToken(b, MASON_DOC_CLOSER);
		}
		else if (tokenType == MASON_TEXT_OPENER)
		{
			b.advanceLexer();
			PerlParserUtil.consumeToken(b, STRING_CONTENT);
			r = PerlParserUtil.consumeToken(b, MASON_TEXT_CLOSER);
		}

		if (r)
		{
			m.drop();
		}
		else
		{
			m.rollbackTo();
		}

		return r;
	}
}
