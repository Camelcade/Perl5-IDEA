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
import com.intellij.lang.PsiParser;
import com.intellij.lang.WhitespacesBinders;
import com.intellij.lang.parser.GeneratedParserUtilBase;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.lexer.PerlTokenSets;
import com.perl5.lang.perl.parser.builder.PerlBuilder;
import com.perl5.lang.perl.parser.builder.PerlStringWrapper;
import com.perl5.lang.perl.parser.elementTypes.PerlStringContentTokenType;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 01.05.2015.
 */
public class PerlParserUtil extends GeneratedParserUtilBase implements PerlElementTypes
{
	public static final TokenSet VERSION_TOKENS = TokenSet.create(
			NUMBER,
			NUMBER_SIMPLE,
			NUMBER_VERSION
	);
	public static final TokenSet OPEN_QUOTES = TokenSet.create(
			QUOTE_DOUBLE_OPEN,
			QUOTE_TICK_OPEN,
			QUOTE_SINGLE_OPEN
	);
	public static final TokenSet CLOSE_QUOTES = TokenSet.create(
			QUOTE_DOUBLE_CLOSE,
			QUOTE_TICK_CLOSE,
			QUOTE_SINGLE_CLOSE
	);

	// tokens that can be converted between each other depending on context
	public static TokenSet CONVERTABLE_TOKENS = TokenSet.create(
			IDENTIFIER,
			SUB,
			RESERVED_METHOD,
			RESERVED_FUNC
	);

	public static void addConvertableTokens(IElementType... convertableTokens)
	{
		CONVERTABLE_TOKENS = TokenSet.orSet(CONVERTABLE_TOKENS, TokenSet.create(convertableTokens));
	}

	/**
	 * Wrapper for Builder class in order to implement additional per parser information in PerlBuilder
	 *
	 * @param root        root element
	 * @param builder     psibuilder
	 * @param parser      psiparser
	 * @param extendsSets extends sets
	 * @return PerlBuilder
	 */
	public static PsiBuilder adapt_builder_(IElementType root, PsiBuilder builder, PsiParser parser, TokenSet[] extendsSets)
	{
		ErrorState state = new ErrorState();
		ErrorState.initState(state, builder, root, extendsSets);

		return new PerlBuilder(builder, state, parser);
	}


	/**
	 * Smart parser for ->, makes }->[ optional
	 *
	 * @param b PerlBuilder
	 * @param l parsing level
	 * @return parsing result
	 */
	public static boolean parseArrowSmart(PsiBuilder b, int l)
	{
		IElementType tokenType = b.getTokenType();
		if (b.getTokenType() == OPERATOR_DEREFERENCE)
		{
			return consumeToken(b, OPERATOR_DEREFERENCE);
		}
		else
		{
			assert b instanceof PerlBuilder;
			PerlTokenData prevToken = ((PerlBuilder) b).lookupToken(-1);
			IElementType prevTokenType = prevToken == null ? null : prevToken.getTokenType();

			// optional }->[ or ]->{
			if (
					(prevTokenType == RIGHT_BRACE || prevTokenType == RIGHT_BRACKET)
							&& (tokenType == LEFT_BRACE || tokenType == LEFT_BRACKET || tokenType == LEFT_PAREN)
					)
			{
				return true;
			}
		}

		return false;
	}

	public static boolean parseExpressionLevel(PsiBuilder b, int l, int g)
	{
		return PerlParserImpl.expr(b, l, g);
	}


	public static boolean parseSubPrototype(PsiBuilder b, int l)
	{
		PsiBuilder.Marker m = null;

		IElementType tokenType = b.getTokenType();
		while (!b.eof() && (tokenType != RIGHT_PAREN))
		{
			if (m == null)
			{
				m = b.mark();
			}


			b.advanceLexer();
			tokenType = b.getTokenType();
		}
		if (m != null)
		{
			m.collapse(SUB_PROTOTYPE_TOKEN);
		}

		return true;
	}

	/**
	 * Smart semi checker decides if we need semi here
	 *
	 * @param b Perl builder
	 * @param l Parsing level
	 * @return checking result
	 */
	public static boolean statementSemi(PsiBuilder b, int l)
	{
		return ((PerlBuilder) b).getPerlParser().parseStatementSemi(b, l);
	}

	/**
	 * Replaces identifier as a variable name
	 *
	 * @param b Perlbuilder
	 * @param l parsing level
	 * @return parsing result
	 */
	public static boolean convertIdentifier(PsiBuilder b, int l, IElementType tokenType)
	{
		IElementType currentTokenType = b.getTokenType();
		if (currentTokenType == tokenType)
		{
			b.advanceLexer();
			return true;
		}
		else if (CONVERTABLE_TOKENS.contains(currentTokenType))
		{
			PsiBuilder.Marker m = b.mark();
			b.advanceLexer();
			m.collapse(tokenType);
			return true;
		}

		return false;
	}

	protected static boolean isOperatorToken(PsiBuilder b, int l)
	{
		return PerlTokenSets.OPERATORS_TOKENSET.contains(b.getTokenType());
	}


	/**
	 * Statement recovery function. Should not consume token, only check;
	 *
	 * @param b PerlBuilder
	 * @param l parsing level
	 * @return parsing result
	 */
	public static boolean recoverStatement(PsiBuilder b, int l)
	{
		assert b instanceof PerlBuilder;
		IElementType currentTokenType = b.getTokenType();

		return !(
				currentTokenType == null ||                                                                                 // got end of file
						!((PerlBuilder) b).getPerlParser().getStatementRecoveryConsumableTokenSet().contains(currentTokenType)
		);
	}

	/**
	 * Parses statement modifier and rolls back if it looks like compound
	 *
	 * @param b PerlBuilder
	 * @param l parsing level
	 * @return check result
	 */
	public static boolean parseStatementModifier(PsiBuilder b, int l)
	{
		assert b instanceof PerlBuilder;
		if (((PerlBuilder) b).getPerlParser().parseStatementModifier(b, l))
		{
			return true;
		}

		PsiBuilder.Marker m = b.mark();

		if (PerlParserImpl.statement_modifier(b, l))
		{
			IElementType tokenType = b.getTokenType();
			if (((PerlBuilder) b).getPerlParser().getConsumableSemicolonTokens().contains(tokenType) ||
					((PerlBuilder) b).getPerlParser().getUnconsumableSemicolonTokens().contains(tokenType))    // we accepts only strict modifiers;
			{
				m.drop();
				return true;
			}
			else    // we suppose that it's compound
			{
				m.rollbackTo();
				return false;
			}
		}
		else
		{
			m.drop();
			return false;
		}
	}

	/**
	 * Checks for version token and convert if necessary
	 *
	 * @param b PerlBuilder
	 * @param l parsing level
	 * @return parsing result
	 */
	public static boolean parsePerlVersion(PsiBuilder b, int l)
	{
		if (VERSION_TOKENS.contains(b.getTokenType()))
		{
			PsiBuilder.Marker m = b.mark();
			b.advanceLexer();
			m.collapse(VERSION_ELEMENT);
			return true;
		}
		return false;
	}

	/**
	 * Parses comma sequence with trailing comma support
	 *
	 * @param b PerlBuilder
	 * @param l parsing level
	 * @return parsing result
	 */
	public static boolean parseCommaSequence(PsiBuilder b, int l)
	{
		boolean r = false;
		while (true)
		{
			if (consumeToken(b, COMMA) || consumeToken(b, FAT_COMMA))    // got comma
			{
				r = true;

				// consume sequential commas
				while (true)
				{
					if (!(consumeToken(b, COMMA) || consumeToken(b, FAT_COMMA)))
					{
						break;
					}
				}
				;
				if (!PerlParserImpl.expr(b, l, 4))    // looks like an end
				{
					break;
				}
			}
			else
			{
				break;
			}
		}
		return r;
	}

	/**
	 * This is kinda hack for use/no statements and bareword -options
	 *
	 * @param b PerlBuilder
	 * @param l parsing level
	 * @return parsing result
	 */
	public static boolean parseStringifiedExpression(PsiBuilder b, int l)
	{
		assert b instanceof PerlBuilder;
		boolean oldState = ((PerlBuilder) b).setStringify(true);
		boolean r = PerlParserImpl.expr(b, l, -1);
		((PerlBuilder) b).setStringify(oldState);
		return r;
	}

	/**
	 * Parsing use vars parameters by forsing re-parsing SQ strings as DQ
	 *
	 * @param b PerlBuilder
	 * @param l parsing level
	 * @return result
	 */
	public static boolean parseUseVarsParameters(PsiBuilder b, int l)
	{
		assert b instanceof PerlBuilder;
		((PerlBuilder) b).setUseVarsContent(true);

		boolean r = PerlParserImpl.expr(b, l, -1);

		((PerlBuilder) b).setUseVarsContent(false);

		return r;
	}

	/**
	 * Parses SQ string with optional conversion to the use_vars lp string
	 *
	 * @param b PerlBuilder
	 * @param l parsing level
	 * @return parsing result
	 */
	public static boolean mapUseVars(PsiBuilder b, int l, Parser parser)
	{
		PsiBuilder.Marker m = b.mark();

		boolean r = parser.parse(b, l);

		// fixme prepend last done marker
		if (r)
		{
			m.collapse(PARSABLE_STRING_USE_VARS);
		}
		else
		{
			m.drop();
		}

		return r;
	}

	// fixme replace with looking to upper frames ?
	public static boolean isUseVars(PsiBuilder b, int l)
	{
		return ((PerlBuilder) b).isUseVarsContent();
	}

	/**
	 * Consuming unexpected token
	 *
	 * @param b perlbuilder
	 * @param l parsing level
	 * @return true
	 **/
	public static boolean parseBadCharacters(PsiBuilder b, int l)
	{
		IElementType tokenType = b.getTokenType();

		if (tokenType == null || ((PerlBuilder) b).getPerlParser().getBadCharacterForbiddenTokens().contains(tokenType))
		{
			return false;
		}

		PsiBuilder.Marker m = b.mark();
		b.advanceLexer();

		if (tokenType == TokenType.BAD_CHARACTER)
		{
			while (b.getTokenType() == TokenType.BAD_CHARACTER)
			{
				b.advanceLexer();
			}
			m.error("Unexpected tokens, plugin currently supports only ASCII identifiers");
		}
		else if (tokenType == RIGHT_PAREN)
		{
			m.error("Unopened closing parenthesis");
		}
		else if (tokenType == RIGHT_BRACKET)
		{
			m.error("Unopened closing bracket");
		}
		else
		{
			m.error("Unexpected token");
		}

		return true;
	}

	/**
	 * Checks if anon hash has proper suffix
	 *
	 * @param b PerlBuilder
	 * @param l parsing level
	 * @return chack result.
	 */
	public static boolean validateAnonHashSuffix(PsiBuilder b, int l)
	{
		IElementType tokenType = b.getTokenType();
		if (tokenType == null || ((PerlBuilder) b).getPerlParser().getAnonHashSuffixTokens().contains(tokenType))
		{
			return true;
		}
		else
		{
			PsiBuilder.Marker m = b.mark();
			boolean r = PerlParserImpl.statement_modifier(b, l);
			r = r && (b.getTokenType() != LEFT_BRACE);
			m.rollbackTo();
			return r;
		}
	}

	/**
	 * Checks that we are at identifier with specific name and convert it to the token type
	 *
	 * @param b               PerlBuilder
	 * @param l               parsing level
	 * @param identifierValue text of identifier to check
	 * @param tokenType       target token type
	 * @return check result
	 */
	@Deprecated
	public static boolean checkAndConvertIdentifier(PsiBuilder b, int l, @NotNull String identifierValue, @NotNull IElementType tokenType)
	{
		if (CONVERTABLE_TOKENS.contains(b.getTokenType()) && identifierValue.equals(b.getTokenText()))
		{
			PsiBuilder.Marker m = b.mark();
			b.advanceLexer();
			m.collapse(tokenType);
			return true;
		}
		return false;
	}

	/**
	 * This is a hack, because GrammarKit parses <<someMethod "=" OPERATOR_ASSIGN>> incorrectly
	 * fixme this should be changed to two-face token =/id
	 *
	 * @param b Perl builder
	 * @param l parsing level
	 * @return conversion result
	 */
	@Deprecated
	public static boolean checkAssignIdentifier(PsiBuilder b, int l)
	{
		return consumeToken(b, OPERATOR_ASSIGN) || checkAndConvertIdentifier(b, l, "=", OPERATOR_ASSIGN);
	}

	/**
	 * Parses and wraps declaration of scalar variable; NB: special variable names suppressed
	 *
	 * @param b Perl builder
	 * @param l parsing level
	 * @return check result
	 */
	public static boolean scalarDeclarationWrapper(PsiBuilder b, int l)
	{
		PsiBuilder.Marker m = b.mark();
		boolean r = false;
		assert b instanceof PerlBuilder;
		boolean flagBackup = ((PerlBuilder) b).setSpecialVariableNamesAllowed(false);

		if (PerlParserImpl.scalar_variable(b, l))
		{
			m.done(VARIABLE_DECLARATION_WRAPPER);
			r = true;
		}
		else
		{
			m.drop();
		}
		((PerlBuilder) b).setSpecialVariableNamesAllowed(flagBackup);
		return r;
	}

	/**
	 * Parses and wraps declaration of scalar variable; NB: special variable names suppressed
	 *
	 * @param b Perl builder
	 * @param l parsing level
	 * @return check result
	 */
	public static boolean arrayDeclarationWrapper(PsiBuilder b, int l)
	{
		PsiBuilder.Marker m = b.mark();
		boolean r = false;
		assert b instanceof PerlBuilder;
		boolean flagBackup = ((PerlBuilder) b).setSpecialVariableNamesAllowed(false);

		if (PerlParserImpl.array_variable(b, l))
		{
			m.done(VARIABLE_DECLARATION_WRAPPER);
			r = true;
		}
		else
		{
			m.drop();
		}
		((PerlBuilder) b).setSpecialVariableNamesAllowed(flagBackup);
		return r;
	}

	/**
	 * Parses and wraps declaration of scalar variable; NB: special variable names suppressed
	 *
	 * @param b Perl builder
	 * @param l parsing level
	 * @return check result
	 */
	public static boolean hashDeclarationWrapper(PsiBuilder b, int l)
	{
		PsiBuilder.Marker m = b.mark();
		boolean r = false;
		assert b instanceof PerlBuilder;
		boolean flagBackup = ((PerlBuilder) b).setSpecialVariableNamesAllowed(false);

		if (PerlParserImpl.hash_variable(b, l))
		{
			m.done(VARIABLE_DECLARATION_WRAPPER);
			r = true;
		}
		else
		{
			m.drop();
		}
		((PerlBuilder) b).setSpecialVariableNamesAllowed(flagBackup);
		return r;
	}

	/**
	 * This is a hack to cancel empty signature or ($) signature
	 *
	 * @param b PerlBuilder
	 * @param l parsing level
	 * @return check result
	 */
	public static boolean cancelProtoLikeSignature(PsiBuilder b, int l)
	{
		IElementType currentTokenType = b.getTokenType();
		if (currentTokenType == RIGHT_PAREN || currentTokenType == SIGIL_SCALAR && b.lookAhead(1) == RIGHT_PAREN)
		{
			return false;
		}
		return true;
	}

	/**
	 * Parsing simple call arguments, prototype tricks not allowed (like code block without sub
	 *
	 * @param b Perlbuilder
	 * @param l parsing level
	 * @return parsing result
	 */
	public static boolean parseSimpleCallArguments(PsiBuilder b, int l)
	{
		PsiBuilder.Marker m = b.mark();
		if (PerlParserImpl.expr(b, l, -1))
		{
			m.done(CALL_ARGUMENTS);

			return true;
		}
		m.drop();
		return false;
	}

	/**
	 * Parses string and wraps it if necessary
	 * fixme we should control where string is located, and avoid in hash indexes, for example
	 *
	 * @param b PerlBuilder
	 * @param l parsing level
	 * @return parsing result
	 */
	public static boolean parseAndWrapStringContent(PsiBuilder b, int l)
	{
		assert b instanceof PerlBuilder;

		IElementType tokenType = b.getTokenType();

		if (tokenType instanceof PerlStringContentTokenType)
		{
			PerlStringWrapper stringWrapper = ((PerlBuilder) b).getStringWrapper();
			if (stringWrapper == null || !stringWrapper.canProcess() || tokenType != STRING_CONTENT)
			{
				b.advanceLexer();
			}
			else
			{
				stringWrapper.wrapNextToken((PerlBuilder) b);
			}
			return true;
		}
		return false;

	}

	/**
	 * attempting to parse statement using parserExtensions
	 *
	 * @param b PerlBuilder
	 * @param l parsing level
	 * @return parsing result
	 */
	public static boolean parseParserExtensionStatement(PsiBuilder b, int l)
	{
		assert b instanceof PerlBuilder;
		return ((PerlBuilder) b).getPerlParser().parseStatement(b, l);
	}

	/**
	 * attempting to parse term using parserExtensions
	 *
	 * @param b PerlBuilder
	 * @param l parsing level
	 * @return parsing result
	 */
	public static boolean parseParserExtensionTerm(PsiBuilder b, int l)
	{
		assert b instanceof PerlBuilder;
		return ((PerlBuilder) b).getPerlParser().parseTerm(b, l);
	}

	public static boolean parseFileContent(PsiBuilder b, int l)
	{
		assert b instanceof PerlBuilder;
		return ((PerlBuilder) b).getPerlParser().parseFileContents(b, l);
	}

	public static boolean parseNestedElementVariation(PsiBuilder b, int l)
	{
		assert b instanceof PerlBuilder;
		return ((PerlBuilder) b).getPerlParser().parseNestedElementVariation(b, l);
	}

	public static boolean checkSemicolon(PsiBuilder b, int l)
	{
		return ((PerlBuilder) b).getPerlParser().getConsumableSemicolonTokens().contains(b.getTokenType());
	}

	public static boolean parseSemicolon(PsiBuilder b, int l)
	{
		if (checkSemicolon(b, l))
		{
			b.advanceLexer();
			return true;
		}
		return false;
	}

	public static boolean parseNamespaceContent(PsiBuilder b, int l)
	{
		PsiBuilder.Marker m = b.mark();
		if (PerlParserGenerated.real_namespace_content(b, l))
		{
			m.done(NAMESPACE_CONTENT);
			m.setCustomEdgeTokenBinders(WhitespacesBinders.GREEDY_LEFT_BINDER, WhitespacesBinders.GREEDY_RIGHT_BINDER);
			return true;
		}
		m.rollbackTo();
		return false;
	}

	// LEFT_PAREN [call_arguments] RIGHT_PAREN !LEFT_BRACKET
	public static boolean parseLeftwardCallArguments(PsiBuilder b, int l)
	{
		if (b.getTokenType() == LEFT_PAREN)
		{
			PsiBuilder.Marker m = b.mark();
			b.advanceLexer();
			PerlParserImpl.parse_call_arguments(b, l);
			if (b.getTokenType() == RIGHT_PAREN && b.lookAhead(1) != LEFT_BRACKET)
			{
				b.advanceLexer();
				m.done(CALL_ARGUMENTS);
				return true;
			}
			m.rollbackTo();
		}
		return false;
	}

}
