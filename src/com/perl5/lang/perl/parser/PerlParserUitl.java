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
import com.intellij.lang.parser.GeneratedParserUtilBase;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.perl.PerlParserDefinition;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.lexer.PerlLexer;
import com.perl5.lang.perl.psi.utils.PerlBuilder;
import com.perl5.lang.perl.util.PerlPackageUtil;
import com.perl5.lang.perl.util.PerlSubUtil;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by hurricup on 01.05.2015.
 */
public class PerlParserUitl extends GeneratedParserUtilBase implements PerlElementTypes
{

	public static final TokenSet PACKAGE_TOKENS = TokenSet.create(
			PACKAGE_CORE_IDENTIFIER,
			PACKAGE_IDENTIFIER,
			PACKAGE
	);

	// tokens that can be converted to a PACKAGE
	public static final TokenSet CONVERTABLE_PACKAGE_TOKENS = TokenSet.create(
			PACKAGE_IDENTIFIER,
			PACKAGE_CORE_IDENTIFIER
	);


	// tokens that can be converted between each other depending on context
	public static final TokenSet CONVERTABLE_TOKENS = TokenSet.create(
			IDENTIFIER,
			VARIABLE_NAME,
			SUB,
			LABEL
	);


	// tokens, allowed to be $^ names part
	public static final TokenSet CONTROL_VARIABLE_NAMES = TokenSet.create(
			LEFT_BRACKET,
			RIGHT_BRACKET
	);

	// Following tokens may be a scalar/glob names
	// "\""|"\\"|"!"|"%"|"&"|"'"|"("|")"|"+"|","|"-"|"."|"/"|"0"|";"|"<"|"="|">"|"@"|"["|"]"|"`"|"|"|"~"|"?"|":"|"*"|"["|"^]"|"^["
	public static final TokenSet POSSIBLE_VARIABLE_NAME = TokenSet.orSet(
			CONVERTABLE_TOKENS,
			TokenSet.create(
					QUOTE_DOUBLE,   // suppress in lexer
					QUOTE_SINGLE,   // suppress in lexer
					QUOTE_TICK,     // suppress in lexer
					OPERATOR_REFERENCE,
					OPERATOR_NOT,
					OPERATOR_MOD,
					OPERATOR_BITWISE_AND,
					LEFT_PAREN,
					RIGHT_PAREN,
					OPERATOR_PLUS,
					SIGIL_SCALAR,
					OPERATOR_COMMA,
					OPERATOR_MINUS,
					OPERATOR_CONCAT,
					OPERATOR_DIV,
					SEMICOLON,
					OPERATOR_LT_NUMERIC,
					OPERATOR_ASSIGN,
					OPERATOR_GT_NUMERIC,
					SIGIL_ARRAY,
					LEFT_BRACKET,
					RIGHT_BRACKET,
					OPERATOR_BITWISE_OR,
					OPERATOR_BITWISE_NOT,
					OPERATOR_BITWISE_XOR,
					QUESTION,
					COLON,
					OPERATOR_MUL,
					NUMBER_SIMPLE
			));

	public static final TokenSet POST_SIGILS_SUFFIXES = TokenSet.orSet(
			PACKAGE_TOKENS,
			CONVERTABLE_TOKENS,
			TokenSet.create(
					LEFT_BRACE,
					SIGIL_SCALAR,
					OPERATOR_BITWISE_XOR,
					NUMBER_SIMPLE
			));


	public static final TokenSet PRINT_HANDLE_NEGATE_SUFFIX = TokenSet.orSet(
			TokenSet.create(
					LEFT_BRACE,
					LEFT_BRACKET,
					LEFT_PAREN,
					OPERATOR_LT_NUMERIC
			),
			PerlLexer.OPERATORS_TOKENSET
	);


	public static final TokenSet VARIABLE_ATTRIBUTE_STOP_TOKENS = TokenSet.orSet(
			PerlLexer.OPERATORS_TOKENSET,
			TokenSet.create(
					SEMICOLON
			)
	);

	// commands, that accepts filehandles as first parameter
	public static final HashSet<String> PRE_HANDLE_OPS = new HashSet<>(Arrays.asList(
			"opendir",
			"chdir",
			"telldir",
			"seekdir",
			"rewinddir",
			"readdir",
			"closedir",

			"sysopen",
			"syswrite",
			"sysseek",
			"sysread",

			"open",
			"close",
			"read",
			"write",
			"stat",
			"ioctl",
			"fcntl",
			"lstat",
			"truncate",
			"tell",
			"select",
			"seek",
			"getc",
			"flock",
			"fileno",
			"eof",
			"eof",
			"binmode"
	));


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
		} else
		{
			assert b instanceof PerlBuilder;
			PerlTokenData prevToken = ((PerlBuilder) b).lookupToken(-1);
			IElementType prevTokenType = prevToken == null ? null : prevToken.getTokenType();

			// optional }->[ or ]->{
			if (
					(prevTokenType == RIGHT_BRACE || prevTokenType == RIGHT_BRACKET)
							&& (tokenType == LEFT_BRACE || tokenType == LEFT_BRACKET || tokenType == LEFT_PAREN)
					)
				return true;
		}

		return false;
	}


	public static boolean parseExpressionLevel(PsiBuilder b, int l, int g)
	{
		return PerlParser.expr(b, l, g);
	}

	/**
	 * Named unary operators
	 *
	 * @param b PerlBuilder
	 * @param l parsing level
	 * @return parsing result
	 */
	public static boolean isUnaryOperator(PsiBuilder b, int l)
	{
		assert b instanceof PerlBuilder;
		PerlTokenData prevTokenData = ((PerlBuilder) b).lookupToken(-1);

		if (prevTokenData != null && prevTokenData.getTokenType() == OPERATOR_DEREFERENCE)
			return false;

		IElementType tokenType = b.getTokenType();
		IElementType nextTokenType = b.lookAhead(1);

		if (CONVERTABLE_TOKENS.contains(tokenType) && nextTokenType != LEFT_PAREN && !PACKAGE_TOKENS.contains(nextTokenType))
			// todo we should check current namespace here
			return PerlSubUtil.BUILT_IN_UNARY.contains(b.getTokenText());
		else if (PACKAGE_TOKENS.contains(tokenType) && CONVERTABLE_TOKENS.contains(SUB) && b.lookAhead(2) != LEFT_PAREN)
			return PerlSubUtil.isUnary(b.getTokenText(), ((PerlBuilder) b).lookupToken(1).getTokenText());

		return false;
	}

	/**
	 * Named list operators
	 *
	 * @param b PerlBuilder
	 * @param l Parsing level
	 * @return parsing result
	 */
	public static boolean isListOperator(PsiBuilder b, int l)
	{
		PerlTokenData prevTokenData = ((PerlBuilder) b).lookupToken(-1);

		if (prevTokenData != null && prevTokenData.getTokenType() == OPERATOR_DEREFERENCE)
			return false;

		IElementType tokenType = b.getTokenType();
		IElementType nextTokenType = b.lookAhead(1);

		if (CONVERTABLE_TOKENS.contains(tokenType) && nextTokenType != LEFT_PAREN && !PACKAGE_TOKENS.contains(nextTokenType))
			// todo we should check current namespace here
			return !PerlSubUtil.BUILT_IN_UNARY.contains(b.getTokenText());
		else if (PACKAGE_TOKENS.contains(tokenType) && CONVERTABLE_TOKENS.contains(nextTokenType) && b.lookAhead(2) != LEFT_PAREN)
			return !PerlSubUtil.isUnary(b.getTokenText(), ((PerlBuilder) b).lookupToken(1).getTokenText());

		return false;
	}


	public static boolean parseSubPrototype(PsiBuilder b, int l)
	{
		PsiBuilder.Marker m = null;

		IElementType tokenType = b.getTokenType();
		while (!b.eof() && (tokenType != RIGHT_PAREN))
		{
			if (m == null)
				m = b.mark();


			b.advanceLexer();
			tokenType = b.getTokenType();
		}
		if (m != null)
			m.collapse(SUB_PROTOTYPE_TOKEN);

		return true;
	}

	// @todo this is really raw
	public static boolean parseSubAttributes(PsiBuilder b, int l)
	{

		PsiBuilder.Marker m = null;
		IElementType tokenType = b.getTokenType();
		while (!b.eof() && tokenType != LEFT_BRACE && tokenType != SEMICOLON)
		{
			if (m == null)
				m = b.mark();
			b.advanceLexer();
			tokenType = b.getTokenType();
		}
		if (m != null)
			m.collapse(SUB_ATTRIBUTE);

		return true;
	}

	// @todo this is really raw
	public static boolean parseVariableAttributes(PsiBuilder b, int l)
	{

		PsiBuilder.Marker m = null;
		while (!b.eof() && !VARIABLE_ATTRIBUTE_STOP_TOKENS.contains(b.getTokenType()))
		{
			if (m == null)
				m = b.mark();
			b.advanceLexer();
		}
		if (m != null)
			m.collapse(VAR_ATTRIBUTE);

		return true;
	}

	// todo implement
	public static boolean parseSubSignature(PsiBuilder b, int l)
	{
		return false;
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
		IElementType tokenType = b.getTokenType();
		if (tokenType == SEMICOLON)
			return consumeToken(b, SEMICOLON);
		else if (tokenType == RIGHT_BRACE || tokenType == REGEX_QUOTE_CLOSE)
			return true;
		else if (b.eof()) // eof
			return true;

		b.mark().error("Semicolon expected");

		return true;
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
		} else if (CONVERTABLE_TOKENS.contains(currentTokenType))
		{
			PsiBuilder.Marker m = b.mark();
			b.advanceLexer();
			m.collapse(tokenType);
			return true;
		}

		return false;
	}

	/**
	 * Completes namespace, invoked when we are 100% sure that PACKAGE_IDENTIFIER is a package
	 *
	 * @param b Perlbuilder
	 * @param l parsing level
	 * @return parsing result
	 */
	public static boolean convertPackageIdentifier(PsiBuilder b, int l)
	{
		IElementType currentTokenType = b.getTokenType();
		if (currentTokenType == PACKAGE)
		{
			b.advanceLexer();
			return true;
		} else if (CONVERTABLE_PACKAGE_TOKENS.contains(currentTokenType))
		{
			PsiBuilder.Marker m = b.mark();
			b.advanceLexer();
			m.collapse(PACKAGE);
			return true;
		}

		return false;
	}

	/**
	 * Merges sequence [package] identifier to a package
	 *
	 * @param b PerlBuilder
	 * @param l parsing level
	 * @return result
	 */
	public static boolean mergePackageName(PsiBuilder b, int l)
	{
		IElementType tokenType = b.getTokenType();

		if (tokenType == PACKAGE)
		{
			b.advanceLexer();
			return true;
		} else if (
				CONVERTABLE_PACKAGE_TOKENS.contains(tokenType) && CONVERTABLE_TOKENS.contains(b.lookAhead(1))
				)
		{
			PsiBuilder.Marker m = b.mark();
			b.advanceLexer();
			b.advanceLexer();
			m.collapse(PACKAGE);
			return true;
		} else if (
				CONVERTABLE_PACKAGE_TOKENS.contains(tokenType)  // explicit package name, like Foo::->method()
						|| CONVERTABLE_TOKENS.contains(tokenType) // single word package
				)
		{
			PsiBuilder.Marker m = b.mark();
			b.advanceLexer();
			m.collapse(PACKAGE);
			return true;
		}

		return false;
	}

	public static boolean mergeRequirePackageName(PsiBuilder b, int l)
	{
		if (CONVERTABLE_TOKENS.contains(b.getTokenType()) && b.lookAhead(1) == LEFT_PAREN)
			return false;
		return mergePackageName(b, l);
	}

	/**
	 * Joining several regex tokens into one to lighten PSI tree. Temporary solution, until regex parsing is implemented
	 *
	 * @param b PerlBuilder
	 * @param l parsing level
	 * @return parsing result
	 */
	public static boolean joinRegexTokens(PsiBuilder b, int l)
	{
		if (b.getTokenType() == REGEX_TOKEN)
		{
			PsiBuilder.Marker m = b.mark();

			while (b.getTokenType() == REGEX_TOKEN)
				b.advanceLexer();

			m.collapse(REGEX_TOKEN);
			return true;
		}
		return false;
	}


	/**
	 * parser for print/say/printf filehandle
	 *
	 * @param b PerlBuilder
	 * @param l parsing level
	 * @return parsing result
	 */
	public static boolean parsePrintHandle(PsiBuilder b, int l)
	{
		IElementType currentTokenType = b.getTokenType();
		IElementType nextTokenType = b.lookAhead(1);
		assert b instanceof PerlBuilder;

		if (
				CONVERTABLE_TOKENS.contains(currentTokenType)                // it's identifier
						&& !PRINT_HANDLE_NEGATE_SUFFIX.contains(nextTokenType)        // no negation tokens
						&& !PerlSubUtil.BUILT_IN.contains(b.getTokenText())         // it's not built in. crude, probably we should check any known sub
				)
		{
			PsiBuilder.Marker m = b.mark();
			b.advanceLexer();
			m.collapse(HANDLE);
			return true;
		}

		return false;
	}

	/**
	 * Checks and parses bareword filehandle for <FH> operations
	 *
	 * @param b PerlBuilder
	 * @param l parsing level
	 * @return parsing result
	 */
	public static boolean parseReadHandle(PsiBuilder b, int l)
	{
		IElementType currentTokenType = b.getTokenType();
		IElementType nextTokenType = b.lookAhead(1);

		if (CONVERTABLE_TOKENS.contains(currentTokenType) && nextTokenType == OPERATOR_GT_NUMERIC)
		{
			PsiBuilder.Marker m = b.mark();
			b.advanceLexer();
			m.collapse(HANDLE);
			return true;
		}

		return false;
	}

	/**
	 * Parses invocable method
	 * As input we may have:
	 * PACKAGE_IDENTIFIER IDENTIFIER	Foo::sub
	 * IDENTIFIER PACKAGE_IDENTIFIER	sub Foo::
	 * IDENTIFIER IDENTIFIER			sub Foo
	 *
	 * @param b PerlBuilder
	 * @param l parsing level
	 * @return parsing result
	 */
	public static boolean parseMethod(PsiBuilder b, int l)
	{
		IElementType currentTokenType = b.getTokenType();
		IElementType nextTokenType = b.lookAhead(1);

		assert b instanceof PerlBuilder;

		// can be
		// 	Foo::method
		//  Foo::Bar
		if (PACKAGE_TOKENS.contains(currentTokenType) && CONVERTABLE_TOKENS.contains(nextTokenType))
		{
			PerlTokenData nextTokenData = ((PerlBuilder) b).lookupToken(1);
			PerlTokenData nextNextTokenData = ((PerlBuilder) b).lookupToken(2);

			IElementType nextNextTokenType = nextNextTokenData == null ? null : nextNextTokenData.getTokenType();

			String canonicalPackageName = PerlPackageUtil.getCanonicalPackageName(b.getTokenText());
			String potentialSubName = canonicalPackageName + "::" + nextTokenData.getTokenText();

			if (((PerlBuilder) b).isKnownSub(potentialSubName) || nextNextTokenType == LEFT_PAREN || ((PerlBuilder) b).isKnownPackage(canonicalPackageName))
				return convertPackageIdentifier(b, l) && convertIdentifier(b, l, SUB);
			else
				return mergePackageName(b, l);
		}
		// 	method [Foo]
		else if (CONVERTABLE_TOKENS.contains(currentTokenType))
		{
			PerlTokenData prevTokenData = ((PerlBuilder) b).lookupToken(-1);

			// ->sub
			if (prevTokenData != null && prevTokenData.getTokenType() == OPERATOR_DEREFERENCE)
				return convertIdentifier(b, l, SUB);
				// may be
				// 	method Foo::
				//	method Foo::Bar
				//  method Foo::othermethod
			else if (PACKAGE_TOKENS.contains(nextTokenType))
			{
				IElementType nextNextTokenType = b.lookAhead(2);

				if (CONVERTABLE_TOKENS.contains(nextNextTokenType))
				{
					PerlTokenData nextTokenData = ((PerlBuilder) b).lookupToken(1);
					PerlTokenData nextNextTokenData = ((PerlBuilder) b).lookupToken(2);

					String packageOrSub = PerlPackageUtil.getCanonicalPackageName(nextTokenData.getTokenText()) + "::" + nextNextTokenData.getTokenText();

					if (((PerlBuilder) b).isKnownSub(packageOrSub))
						return convertIdentifier(b, l, SUB);
					else if (((PerlBuilder) b).isKnownPackage(packageOrSub))
						return convertIdentifier(b, l, SUB) && mergePackageName(b, l);
					return convertIdentifier(b, l, SUB);
				} else
					// it's method Package::
					return convertIdentifier(b, l, SUB) && convertPackageIdentifier(b, l);
			}
			// may be
			// 	method Foo
			else if (CONVERTABLE_TOKENS.contains(nextTokenType))
			{
				PerlTokenData nextTokenData = ((PerlBuilder) b).lookupToken(1);

				String potentialSubName = nextTokenData.getTokenText() + "::" + b.getTokenText();
				if (((PerlBuilder) b).isKnownSub(potentialSubName))
					return convertIdentifier(b, l, SUB) && convertIdentifier(b, l, PACKAGE);
				else
					return convertIdentifier(b, l, SUB);
			} else // it's just sub
				return convertIdentifier(b, l, SUB);
		}

		return false;
	}


	/**
	 * Parses {IDENTIFIER}
	 *
	 * @param b PerlBuilder
	 * @param l parsing level
	 * @return parsing result
	 */
	public static boolean parseBracedString(PsiBuilder b, int l)
	{
		if (consumeToken(b, LEFT_BRACE)
				&& CONVERTABLE_TOKENS.contains(b.getTokenType())
				&& b.lookAhead(1) == RIGHT_BRACE
				)
		{
			PsiBuilder.Marker m = b.mark();
			b.advanceLexer();
			m.collapse(STRING_CONTENT);
			b.advanceLexer();
			return true;
		}
		return false;
	}

	/**
	 * Parsing label declaration LABEL:
	 *
	 * @param b PerlBuilder
	 * @param l parsing level
	 * @return parsing result
	 */
	public static boolean parseLabelDeclaration(PsiBuilder b, int l)
	{
		if (CONVERTABLE_TOKENS.contains(b.getTokenType()) && b.lookAhead(1) == COLON)
		{
			PsiBuilder.Marker m = b.mark();
			b.advanceLexer();
			m.collapse(LABEL);
			b.advanceLexer();
			return true;
		}
		return false;
	}

	/**
	 * Parses tokens as variables name; replaces:
	 *
	 * @param b PerlBuilder
	 * @param l parsing level
	 * @return parsing result
	 */
	public static boolean parseVariableName(PsiBuilder b, int l)
	{
		IElementType currentTokenType = b.getTokenType();
		IElementType nextTokenType = b.rawLookup(1);

		PsiBuilder.Marker m = null;

		while (currentTokenType == SIGIL_SCALAR                                // sigil is here
				&& POST_SIGILS_SUFFIXES.contains(nextTokenType)            // next can be variable name
				)
		{
			if (m == null)
				m = b.mark();
			b.advanceLexer();
			currentTokenType = nextTokenType;
			nextTokenType = b.rawLookup(1);
		}

		if (m != null)
			m.collapse(SCALAR_SIGILS);

		// $package::
		// $package::var
		if (PACKAGE_TOKENS.contains(currentTokenType))
		{
			PsiBuilder.Marker mp = b.mark();
			b.advanceLexer();
			mp.collapse(PACKAGE);
			if (CONVERTABLE_TOKENS.contains(nextTokenType))
			{
				PsiBuilder.Marker mv = b.mark();
				b.advanceLexer();
				mv.collapse(VARIABLE_NAME);
			}
			return true;
		}
		// $var
		else if (POSSIBLE_VARIABLE_NAME.contains(currentTokenType))
		{
			if (currentTokenType == OPERATOR_BITWISE_XOR && CONTROL_VARIABLE_NAMES.contains(nextTokenType)) // branch for $^]
			{
				m = b.mark();
				b.advanceLexer();
				b.advanceLexer();
				m.collapse(VARIABLE_NAME);
			} else
			{
				PsiBuilder.Marker mv = b.mark();
				b.advanceLexer();
				mv.collapse(VARIABLE_NAME);
			}

			return true;
		}
		// ${var}
		else if (currentTokenType == LEFT_BRACE)
		{
			b.advanceLexer();
			currentTokenType = nextTokenType;
			nextTokenType = b.lookAhead(1);

			// ${package::}
			// ${package::var}
			if (PACKAGE_TOKENS.contains(currentTokenType))
			{
				PsiBuilder.Marker mp = b.mark();
				b.advanceLexer();
				mp.collapse(PACKAGE);
				if (CONVERTABLE_TOKENS.contains(nextTokenType) && b.lookAhead(1) == RIGHT_BRACE)
				{
					PsiBuilder.Marker mv = b.mark();
					b.advanceLexer();
					mv.collapse(VARIABLE_NAME);
					b.advanceLexer();
					return true;
				} else if (nextTokenType == RIGHT_BRACE)
				{
					b.advanceLexer();
					return true;
				}
			}
			// ${var}
			else if (POSSIBLE_VARIABLE_NAME.contains(currentTokenType) && nextTokenType == RIGHT_BRACE)
			{
				PsiBuilder.Marker mv = b.mark();
				b.advanceLexer();
				mv.collapse(VARIABLE_NAME);
				b.advanceLexer();
				return true;
			}
		}

		return false;
	}

	public static boolean parseAmbiguousSigil(PsiBuilder b, int l, IElementType sigilTokenType)
	{
		IElementType tokenType = b.getTokenType();
		if (tokenType == sigilTokenType)
		{
			if (PerlParserDefinition.WHITE_SPACE_AND_COMMENTS.contains(b.rawLookup(1))) // space disallowed after *
				return false;

			b.advanceLexer();
			return true;
		}

		return false;
	}


	public static TokenSet UNCONDITIONAL_STATEMENT_RECOVERY_TOKENS = TokenSet.create(
			SEMICOLON,
			RIGHT_BRACE,
			REGEX_QUOTE_CLOSE,

			BLOCK_NAME,

			RESERVED_PACKAGE,
			RESERVED_USE,
			RESERVED_NO,

			RESERVED_IF,
			RESERVED_UNLESS,
			RESERVED_GIVEN,
			RESERVED_WHILE,
			RESERVED_UNTIL,
			RESERVED_WHEN,

			RESERVED_FOREACH,	// may have no opening paren after a keyword
			RESERVED_FOR, 		// may have no opening paren after a keyword
			RESERVED_DEFAULT	// has no opening paren
	);

	public static TokenSet STATEMENT_RECOVERY_SUB_SUFFIX = TokenSet.create(
			IDENTIFIER,
			PACKAGE,
			PACKAGE_IDENTIFIER,
			PACKAGE_CORE_IDENTIFIER
	);

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

		if( !((PerlBuilder) b).isRecoveringStatement())
			((PerlBuilder) b).startRecovery();

		IElementType currentTokenType = b.getTokenType();

//		System.err.println("Checking " + b.getTokenText() + currentTokenType);

		if( currentTokenType == null																					// got end of file
			|| ((PerlBuilder) b).getBracesLevel() == 0 && (																// we are not in braced statement
				currentTokenType == RESERVED_SUB && STATEMENT_RECOVERY_SUB_SUFFIX.contains(b.lookAhead(1))				// got sub definition/declaration
				||  UNCONDITIONAL_STATEMENT_RECOVERY_TOKENS.contains(currentTokenType) 									// got semi, package, end of regex, use
			)
		)
		{
			((PerlBuilder) b).stopRecovery();
			return false;
		}

		if( currentTokenType == LEFT_BRACE)
			((PerlBuilder) b).openBrace();
		else if(currentTokenType == RIGHT_BRACE)
			((PerlBuilder) b).closeBrace();

		return true;
	}
}
