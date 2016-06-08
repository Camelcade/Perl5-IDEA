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

import com.intellij.lang.LighterASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.WhitespacesBinders;
import com.intellij.lang.parser.GeneratedParserUtilBase;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.perl5.PerlBundle;
import com.perl5.lang.tt2.TemplateToolkitParserDefinition;
import com.perl5.lang.tt2.elementTypes.TemplateToolkitElementTypes;
import com.perl5.lang.tt2.lexer.TemplateToolkitSyntaxElements;

/**
 * Created by hurricup on 05.06.2016.
 */
@SuppressWarnings("Duplicates")
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

	public static boolean parseDirective(PsiBuilder b, int l)
	{
		IElementType tokenType = b.getTokenType();
		boolean r = false;
		LighterASTNode latestDoneMarker = null;
		PsiBuilder.Marker outerMarer = b.mark();

		if (tokenType == TT2_OPEN_TAG)
		{
			if (b.rawLookup(1) == TT2_MINUS)
			{
				b.advanceLexer(); // chomp
			}
			b.advanceLexer();

			if (TemplateToolkitParser.directive_expr(b, l))
			{
				latestDoneMarker = b.getLatestDoneMarker();
			}

			PsiBuilder.Marker m = null;
			while (!b.eof())
			{
				tokenType = b.getTokenType();
				if (tokenType == TT2_CLOSE_TAG || tokenType == TT2_MINUS && b.rawLookup(1) == TT2_CLOSE_TAG)
				{
					break;
				}
				if (m == null)
				{
					m = b.mark();
				}
				b.advanceLexer();
			}

			if (m != null)
			{
				m.error(PerlBundle.message("ttk2.unexpected.token"));
			}

			consumeToken(b, TT2_MINUS);
			consumeToken(b, TT2_CLOSE_TAG);

			r = true;
		}
		else if (tokenType == TT2_OUTLINE_TAG)
		{
			b.advanceLexer();

			if (TemplateToolkitParser.directive_expr(b, l))
			{
				latestDoneMarker = b.getLatestDoneMarker();
			}

			PsiBuilder.Marker m = null;

			while (!b.eof())
			{
				if (b.getTokenType() == TT2_HARD_NEWLINE)
				{
					break;
				}
				if (m == null)
				{
					m = b.mark();
				}
				b.advanceLexer();
			}

			if (m != null)
			{
				m.error("ttk2.unexpected.token");
			}
			// parseHardNewLine(b, l); // fixme this breaks lastMarker mechanism, need to figure out something
			if (b.getTokenType() == TT2_HARD_NEWLINE)
			{
				b.remapCurrentToken(TokenType.NEW_LINE_INDENT); // this is irreversable change, so not sure it's a good idea
				b.advanceLexer();
			}

			r = true;
		}

		if (latestDoneMarker != null)
		{
			tokenType = latestDoneMarker.getTokenType();
			if (tokenType == BLOCK_DIRECTIVE_EXPR)
			{
				parseBlockContent(b, l);
				outerMarer.done(NAMED_BLOCK);
				outerMarer = null;
			}
			else if (tokenType == ANON_BLOCK_DIRECTIVE_EXPR)
			{
				parseBlockContent(b, l);
				outerMarer.done(ANON_BLOCK);
				outerMarer = null;
			}
			else if (tokenType == WRAPPER_DIRECTIVE_EXPR)
			{
				parseBlockContent(b, l);
				outerMarer.done(WRAPPER_BLOCK);
				outerMarer = null;
			}
			else if (tokenType == IF_DIRECTIVE_EXPR)
			{
				PsiBuilder.Marker branchMarker = outerMarer;
				outerMarer = outerMarer.precede();
				parseIfSequence(b, l, branchMarker, IF_BRANCH);
				outerMarer.done(IF_BLOCK);
				outerMarer = null;
			}
			else if (tokenType == UNLESS_DIRECTIVE_EXPR)
			{
				PsiBuilder.Marker branchMarker = outerMarer;
				outerMarer = outerMarer.precede();
				parseIfSequence(b, l, branchMarker, UNLESS_BRANCH);
				outerMarer.done(UNLESS_BLOCK);
				outerMarer = null;
			}
		}

		if (outerMarer != null)
		{
			outerMarer.drop();
		}

		return r;
	}

	public static boolean parseBlockContent(PsiBuilder b, int l)
	{
		while (!b.eof() && TemplateToolkitParser.element(b, l))
		{
			LighterASTNode latestDoneMarker = b.getLatestDoneMarker();
			if (latestDoneMarker != null && latestDoneMarker.getTokenType() == END_DIRECTIVE_EXPR)
			{
				break;
			}
		}
		return true;
	}

	public static boolean parseIfSequence(PsiBuilder b, int l, PsiBuilder.Marker branchMarker, IElementType branchTokenType)
	{
		while (!b.eof())
		{
			PsiBuilder.Marker currentMarker = b.mark();
			if (TemplateToolkitParser.element(b, l))
			{
				LighterASTNode latestDoneMarker = b.getLatestDoneMarker();
				if (latestDoneMarker != null)
				{
					if (latestDoneMarker.getTokenType() == END_DIRECTIVE_EXPR)
					{
						branchMarker.doneBefore(branchTokenType, currentMarker);
						branchMarker.setCustomEdgeTokenBinders(WhitespacesBinders.GREEDY_LEFT_BINDER, WhitespacesBinders.GREEDY_RIGHT_BINDER);
						currentMarker.drop();
						branchMarker = null;
						break;
					}
					else if (latestDoneMarker.getTokenType() == ELSIF_DIRECTIVE_EXPR)
					{
						branchMarker.doneBefore(branchTokenType, currentMarker);
						branchMarker.setCustomEdgeTokenBinders(WhitespacesBinders.GREEDY_LEFT_BINDER, WhitespacesBinders.GREEDY_RIGHT_BINDER);
						branchMarker = currentMarker.precede();
						branchTokenType = ELSIF_BRANCH;
					}
					else if (latestDoneMarker.getTokenType() == ELSE_DIRECTIVE_EXPR)
					{
						branchMarker.doneBefore(branchTokenType, currentMarker);
						branchMarker.setCustomEdgeTokenBinders(WhitespacesBinders.GREEDY_LEFT_BINDER, WhitespacesBinders.GREEDY_RIGHT_BINDER);
						branchMarker = currentMarker.precede();
						branchTokenType = ELSE_BRANCH;
					}
				}
			}
			currentMarker.drop();
		}

		if (branchMarker != null)
		{
			branchMarker.done(branchTokenType);
			branchMarker.setCustomEdgeTokenBinders(WhitespacesBinders.GREEDY_LEFT_BINDER, WhitespacesBinders.GREEDY_RIGHT_BINDER);
		}

		return true;
	}

}
