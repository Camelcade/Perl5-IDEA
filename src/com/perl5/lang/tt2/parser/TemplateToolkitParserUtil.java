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

package com.perl5.lang.tt2.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.parser.GeneratedParserUtilBase;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.tt2.TemplateToolkitParserDefinition;
import com.perl5.lang.tt2.elementTypes.TemplateToolkitElementTypes;
import com.perl5.lang.tt2.lexer.TemplateToolkitSyntaxElements;

/**
 * Created by hurricup on 05.06.2016.
 */
public class TemplateToolkitParserUtil extends GeneratedParserUtilBase implements TemplateToolkitElementTypes
{
	public static boolean parseIdentifier(PsiBuilder b, int l)
	{
		if (consumeToken(b, TT2_IDENTIFIER))
		{
			return true;
		}
		else if (TemplateToolkitSyntaxElements.POSSIBLE_IDENTIFIERS.contains(b.getTokenType()))
		{
			PsiBuilder.Marker m = b.mark();
			b.advanceLexer();
			m.collapse(TT2_IDENTIFIER);
			return true;
		}
		return false;
	}

	@SuppressWarnings("Duplicates")
	public static boolean parseHardNewLine(PsiBuilder b, int l)
	{
		if (b.getTokenType() == TT2_HARD_NEWLINE)
		{
			PsiBuilder.Marker m = b.mark();
			b.advanceLexer();
			m.collapse(TokenType.NEW_LINE_INDENT);
			return true;
		}
		return false;
	}

	public static boolean parseHashKey(PsiBuilder b, int l)
	{
		PsiBuilder.Marker m = b.mark();
		if (TemplateToolkitParser.keyword_or_identifier_term(b, l))
		{
			m.collapse(TT2_STRING_CONTENT);
			m.precede().done(SQ_STRING_EXPR);
			return true;
		}

		return false;
	}

	public static boolean parseDirictiveExpr(PsiBuilder b, int l)
	{
		boolean r = TemplateToolkitParser.directive_real_expr(b, l);

		return r;
	}

	private static boolean isEndMarker(IElementType tokenType)
	{
		return tokenType == TT2_HARD_NEWLINE || tokenType == TT2_CLOSE_TAG;

	}

	public static boolean parseFileAsString(PsiBuilder b, int l)
	{
		if (b.eof())
		{
			return false;
		}

		IElementType tokenType = b.getTokenType();
		if (isEndMarker(tokenType))
		{
			return false;
		}

		PsiBuilder.Marker m = b.mark();
		while (!b.eof())
		{
			tokenType = b.rawLookup(1);
			if (isEndMarker(tokenType) || TemplateToolkitParserDefinition.WHITE_SPACES.contains(tokenType))
			{
				b.advanceLexer();
				break;
			}
			b.advanceLexer();
		}
		m.collapse(TT2_STRING_CONTENT);
		m.precede().done(SQ_STRING_EXPR);
		return true;
	}

	public static boolean parseBlockComment(PsiBuilder b, int l)
	{
		if (b.getTokenType() == TT2_OPEN_TAG && b.rawLookup(1) == TT2_SHARP)
		{
			PsiBuilder.Marker m = b.mark();
			b.advanceLexer();

			PsiBuilder.Marker commentMarker = b.mark();
			while (!b.eof())
			{
				if (b.getTokenType() == TT2_CLOSE_TAG)
				{
					break;
				}
				b.advanceLexer();
			}
			commentMarker.collapse(LINE_COMMENT);
			b.advanceLexer();

			m.done(BLOCK_COMMENT);
			return true;
		}
		return false;
	}

}
