/*
 * Copyright 2016 Alexandr Evstigneev
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

package com.perl5.lang.ea;

import com.intellij.lang.PsiBuilder;
import com.perl5.PerlBundle;
import com.perl5.lang.ea.psi.PerlExternalAnnotationsElementTypes;
import com.perl5.lang.perl.parser.PerlParserImpl;
import com.perl5.lang.perl.parser.PerlParserUtil;

/**
 * Created by hurricup on 06.08.2016.
 */
public class PerlExternalAnnotationsParser extends PerlParserImpl implements PerlExternalAnnotationsElementTypes
{
	@Override
	public boolean parseFileContents(PsiBuilder b, int l)
	{
		while (!b.eof())
		{
			parseDelcarationLikeStatement(b, l);
		}
		return true;
	}

	private void parseDelcarationLikeStatement(PsiBuilder b, int l)
	{
		if (b.getTokenType() == RESERVED_SUB)
		{
			PsiBuilder.Marker marker = b.mark();
			b.advanceLexer();

			if (PerlParserUtil.parseSubDefinitionName(b, l))
			{
				PerlParserUtil.consumeToken(b, SEMICOLON);
				marker.done(PSEUDO_DECLARATION);
			}
			else
			{
				marker.drop();
				b.mark().error(PerlBundle.message("parser.identifier.expected"));
			}
		}
		recoverDeclarationLikeStatement(b, l);
	}

	private void recoverDeclarationLikeStatement(PsiBuilder b, int l)
	{
		while (!b.eof() && b.getTokenType() != RESERVED_SUB)
		{
			PsiBuilder.Marker mark = b.mark();
			b.advanceLexer();
			mark.error(PerlBundle.message("parser.sub.expected"));
		}
	}
}
