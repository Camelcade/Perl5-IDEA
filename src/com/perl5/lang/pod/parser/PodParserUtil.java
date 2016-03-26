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

package com.perl5.lang.pod.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.parser.GeneratedParserUtilBase;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.pod.lexer.PodElementTypes;

/**
 * Created by hurricup on 26.03.2016.
 */
public class PodParserUtil extends GeneratedParserUtilBase implements PodElementTypes
{
	public static boolean parseTermParam(PsiBuilder b, int l)
	{
		if (b.getTokenType() == POD_ANGLE_LEFT)
		{
			int openersNumber = 1;

			int possibleOpenersNumber = openersNumber;
			if (b.rawLookup(possibleOpenersNumber) == POD_ANGLE_LEFT) // check for multi-angle opener format
			{
				//noinspection StatementWithEmptyBody
				while (b.rawLookup(++possibleOpenersNumber) == POD_ANGLE_LEFT) ;

				IElementType nextTokenType = b.rawLookup(possibleOpenersNumber);

				if (nextTokenType == TokenType.WHITE_SPACE || nextTokenType == TokenType.NEW_LINE_INDENT)
				{
					openersNumber = possibleOpenersNumber;
				}
			}

			PsiBuilder.Marker m = b.mark();
			for (int i = 0; i < openersNumber; i++)
			{
				b.advanceLexer();
			}
			m.collapse(POD_ANGLE_LEFT);

			m = null;
			while (true)
			{
				if (atCloseAngles(b, openersNumber))
				{
					if (m != null)
					{
						m.done(POD_TERM_PARAM);
					}

					m = b.mark();
					while (openersNumber > 0)
					{
						b.advanceLexer();
						openersNumber--;
					}
					m.collapse(POD_ANGLE_RIGHT);
					break;
				}
				else if (b.getTokenType() == POD_NEWLINE || b.eof())
				{
					if (m == null)
					{
						m = b.mark();
					}
					m.error("Unclosed angles");
					break;
				}

				if (m == null)
				{
					m = b.mark();
				}
				PodParser.pod_term(b, l);
			}

			return true;
		}
		return false;
	}

	private static boolean atCloseAngles(PsiBuilder b, int markersNumber)
	{
		IElementType currentTokenType = b.getTokenType();

		if (currentTokenType == POD_ANGLE_RIGHT)
		{
			if (markersNumber == 1)
			{
				return true;
			}
			else if ((currentTokenType = b.rawLookup(-1)) == TokenType.WHITE_SPACE || currentTokenType == TokenType.NEW_LINE_INDENT)
			{
				for (int i = 1; i < markersNumber; i++)
				{
					if (b.rawLookup(i) != POD_ANGLE_RIGHT)
						return false;
				}
				return true;
			}
		}

		return false;
	}

}
