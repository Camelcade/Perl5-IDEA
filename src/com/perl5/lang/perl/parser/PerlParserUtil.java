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
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.perl.PerlParserDefinition;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.lexer.PerlLexer;
import com.perl5.lang.perl.parser.builder.PerlBuilder;
import com.perl5.lang.perl.parser.builder.PerlBuilderLight;
import com.perl5.lang.perl.util.PerlPackageUtil;
import com.perl5.lang.perl.util.PerlSubUtil;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by hurricup on 01.05.2015.
 */
public class PerlParserUtil extends GeneratedParserUtilBase implements PerlElementTypes
{

	// tokens that can be converted to a PACKAGE
	public static final TokenSet PACKAGE_TOKENS = TokenSet.create(
			PACKAGE_CORE_IDENTIFIER,
			PACKAGE_PRAGMA_CONSTANT,
			PACKAGE_PRAGMA_VARS,
			PACKAGE_IDENTIFIER,
			PACKAGE
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

	public static final TokenSet SPECIAL_VARIABLE_NAME = TokenSet.create(
			QUOTE_DOUBLE,
			QUOTE_SINGLE,
			QUOTE_TICK,
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
	);


	// Following tokens may be a scalar/glob names
	// "\""|"\\"|"!"|"%"|"&"|"'"|"("|")"|"+"|","|"-"|"."|"/"|"0"|";"|"<"|"="|">"|"@"|"["|"]"|"`"|"|"|"~"|"?"|":"|"*"|"["|"^]"|"^["
	public static final TokenSet POSSIBLE_VARIABLE_NAME = TokenSet.orSet(
			CONVERTABLE_TOKENS,
			SPECIAL_VARIABLE_NAME
	);

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
					SEMICOLON,
					EMBED_MARKER_SEMICOLON
			)
	);

	// commands, that accepts filehandles as first parameter
	public static final HashSet<String> PRE_HANDLE_OPS = new HashSet<String>(Arrays.asList(
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
	public static final TokenSet VERSION_TOKENS = TokenSet.create(
			NUMBER,
			NUMBER_SIMPLE,
			NUMBER_VERSION
	);
	protected static final TokenSet CLOSE_QUOTES = TokenSet.create(
			QUOTE_DOUBLE_CLOSE,
			QUOTE_TICK_CLOSE,
			QUOTE_SINGLE_CLOSE
	);
	protected static final TokenSet STRING_MERGE_STOP_TOKENS = TokenSet.orSet(
			CLOSE_QUOTES,
			TokenSet.create(
					SIGIL_SCALAR, SIGIL_ARRAY
			));
	protected static final TokenSet REGEX_BLOCK_CLOSER = TokenSet.create(
			REGEX_QUOTE,
			REGEX_QUOTE_CLOSE,
			REGEX_QUOTE_E
	);
	protected static final TokenSet REGEX_MERGE_STOP_TOKENS = TokenSet.orSet(
			REGEX_BLOCK_CLOSER,
			TokenSet.create(
					SIGIL_SCALAR, SIGIL_ARRAY
			));
	public static TokenSet UNCONDITIONAL_STATEMENT_RECOVERY_TOKENS = TokenSet.create(
			SEMICOLON,
			EMBED_MARKER_SEMICOLON,

			RIGHT_BRACE,
			REGEX_QUOTE_CLOSE,

			BLOCK_NAME,

			RESERVED_IF,
			RESERVED_UNLESS,
			RESERVED_GIVEN,
			RESERVED_WHILE,
			RESERVED_UNTIL,
			RESERVED_WHEN,

			RESERVED_FOREACH,    // may have no opening paren after a keyword
			RESERVED_FOR,        // may have no opening paren after a keyword

			RESERVED_PACKAGE,
			RESERVED_USE,
			RESERVED_NO,

			RESERVED_DEFAULT    // has no opening paren
	);
	public static TokenSet STATEMENT_RECOVERY_SUB_SUFFIX = TokenSet.create(
			IDENTIFIER,
			PACKAGE,
			PACKAGE_IDENTIFIER,
			PACKAGE_CORE_IDENTIFIER
	);

	public static TokenSet LIGHT_CONTAINERS = TokenSet.create(
			HEREDOC,
			HEREDOC_QQ,
			HEREDOC_QX
	);

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

//		if (!LIGHT_CONTAINERS.contains(root))
//		{
//		int length = 100;
//			if (length > builder.getOriginalText().length())
//				length = builder.getOriginalText().length();
//			System.err.println("Adapting builder for " + root + " " + builder.getOriginalText().length() + " " + builder.getOriginalText().subSequence(0, length));
//		}
//		else
//			System.err.println("Adapting safe builder for " + root + " " + builder.getOriginalText().length());

		if (root == PARSABLE_STRING_USE_VARS)
		{
			PerlBuilder perlBuilder = new PerlBuilderLight(builder, state, parser);
			perlBuilder.setAllowSigils(true);
			return perlBuilder;
		}
		if (LIGHT_CONTAINERS.contains(root))
			return new PerlBuilderLight(builder, state, parser);
		else
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
		{
			PerlTokenData nextToken = ((PerlBuilder) b).lookupToken(1);
			if (nextToken != null)
				return PerlSubUtil.isUnary(b.getTokenText(), nextToken.getTokenText());
		}

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

		if (CONVERTABLE_TOKENS.contains(tokenType)
				&& nextTokenType != LEFT_PAREN              // not function call
				&& !PACKAGE_TOKENS.contains(nextTokenType)  // not method Package::
				&& !(nextTokenType == IDENTIFIER && ((PerlBuilder) b).isKnownPackage(((PerlBuilder) b).lookupToken(1).getTokenText()))  // not Method Package
				)
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
		while (!b.eof() && tokenType != LEFT_BRACE && tokenType != SEMICOLON && tokenType != EMBED_MARKER_SEMICOLON)
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
		else if (tokenType == EMBED_MARKER_SEMICOLON)
			return consumeToken(b, EMBED_MARKER_SEMICOLON);
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

	public static boolean convertBracedString(PsiBuilder b, int l)
	{

		if (CONVERTABLE_TOKENS.contains(b.getTokenType()) && b.lookAhead(1) == RIGHT_BRACE)
		{
			// fixme shouldn't we add string_sq here?
			PsiBuilder.Marker m = b.mark();
			b.advanceLexer();
			m.collapse(STRING_CONTENT);
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
		} else if (PACKAGE_TOKENS.contains(currentTokenType))
		{
			PsiBuilder.Marker m = b.mark();
			b.advanceLexer();
			m.collapse(PACKAGE);
			return true;
		}

		return false;
	}

	/**
	 * Checks token sequence and collapses it into target token
	 *
	 * @param b                 PerlBuilder
	 * @param l                 parsing level
	 * @param targetTokenType   source token type
	 * @param sequenceTokenType varargs tokens to check
	 * @return result
	 */
	public static boolean checkAndCollapseToken(PsiBuilder b, int l, IElementType targetTokenType, IElementType... sequenceTokenType)
	{
		if (sequenceTokenType.length == 0)
			return false;

		PsiBuilder.Marker m = b.mark();

		for (IElementType tokenType : sequenceTokenType)
			if (b.getTokenType() == tokenType)
				b.advanceLexer();
			else
			{
				m.rollbackTo();
				return false;
			}

		m.collapse(targetTokenType);
		return true;
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
				PACKAGE_TOKENS.contains(tokenType) && CONVERTABLE_TOKENS.contains(b.lookAhead(1))
				)
		{
			PsiBuilder.Marker m = b.mark();
			b.advanceLexer();
			b.advanceLexer();
			m.collapse(PACKAGE);
			return true;
		} else if (
				PACKAGE_TOKENS.contains(tokenType)  // explicit package name, like Foo::->method()
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

			if (
					nextNextTokenType == LEFT_PAREN                        // Package::Identifier( - what can it be?
							|| ((PerlBuilder) b).isKnownSub(potentialSubName)       // we know this sub
							|| !((PerlBuilder) b).isKnownPackage(potentialSubName)) // we don't know such package
				return convertPackageIdentifier(b, l) && convertIdentifier(b, l, SUB);
			else
				return false;
		}
		// 	method
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

				// sub Foo::->method
				if (nextNextTokenType == OPERATOR_DEREFERENCE)
					return convertIdentifier(b, l, SUB);
					// identifier Package::identifier
				else if (CONVERTABLE_TOKENS.contains(nextNextTokenType))
				{

					// identifier Package::identifier->
					if (b.lookAhead(3) == OPERATOR_DEREFERENCE)
						return convertIdentifier(b, l, SUB);

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
			else if (CONVERTABLE_TOKENS.contains(nextTokenType) && b.lookAhead(2) != OPERATOR_DEREFERENCE)
			{
				PerlTokenData nextTokenData = ((PerlBuilder) b).lookupToken(1);

				String potentialSubName = nextTokenData.getTokenText() + "::" + b.getTokenText();
				if (((PerlBuilder) b).isKnownSub(potentialSubName))
					return convertIdentifier(b, l, SUB) && convertIdentifier(b, l, PACKAGE);
				else
					return convertIdentifier(b, l, SUB);
			}
			// KnownPackage->
			else if (nextTokenType == OPERATOR_DEREFERENCE && ((PerlBuilder) b).isKnownPackage(b.getTokenText()))
				return false;
				// it's just sub
			else
				return convertIdentifier(b, l, SUB);
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

		PsiBuilder.Marker m;

//		while (currentTokenType == SIGIL_SCALAR                                // sigil is here
//				&& POST_SIGILS_SUFFIXES.contains(nextTokenType)            // next can be variable name
//				)
//		{
//			if (m == null)
//				m = b.mark();
//			b.advanceLexer();
//			currentTokenType = nextTokenType;
//			nextTokenType = b.rawLookup(1);
//		}
//
//		if (m != null)
//			m.collapse(SCALAR_SIGILS);

		// checking for scalar cast
		if (currentTokenType == SIGIL_SCALAR && POST_SIGILS_SUFFIXES.contains(b.lookAhead(1)))
			return false;

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

	public static boolean parseAmbiguousSigil(PsiBuilder b, int l, IElementType sigilTokenType, IElementType targetTokenType)
	{
		IElementType tokenType = b.getTokenType();
		if (tokenType == sigilTokenType)
		{
			if (PerlParserDefinition.WHITE_SPACE_AND_COMMENTS.contains(b.rawLookup(1)) && b.lookAhead(1) != LEFT_BRACE) // space disallowed after * or % if it's not a cast
				return false;

			PsiBuilder.Marker m = b.mark();
			b.advanceLexer();
			m.collapse(targetTokenType);
			return true;
		}

		return false;
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

		if (!((PerlBuilder) b).isRecoveringStatement())
			((PerlBuilder) b).startRecovery();

		IElementType currentTokenType = b.getTokenType();

//		System.err.println("Checking " + b.getTokenText() + currentTokenType);

		if (currentTokenType == null                                                                                    // got end of file
				|| ((PerlBuilder) b).getBracesLevel() == 0 && (                                                                // we are not in braced statement
				UNCONDITIONAL_STATEMENT_RECOVERY_TOKENS.contains(currentTokenType)                              // got semi, package, end of regex, use, compound or suffix
						|| currentTokenType == RESERVED_SUB && STATEMENT_RECOVERY_SUB_SUFFIX.contains(b.lookAhead(1))   // got sub definition
		)
				)
		{
			((PerlBuilder) b).stopRecovery();
			return false;
		}

		if (currentTokenType == LEFT_BRACE)
			((PerlBuilder) b).openBrace();
		else if (currentTokenType == RIGHT_BRACE)
			((PerlBuilder) b).closeBrace();

		return true;
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
			if (consumeToken(b, OPERATOR_COMMA) || consumeToken(b, OPERATOR_COMMA_ARROW))    // got comma
			{
				r = true;

				// consume sequential commas
				while (true)
				{
					if (!(consumeToken(b, OPERATOR_COMMA) || consumeToken(b, OPERATOR_COMMA_ARROW)))
						break;
				}
				;
				if (!PerlParser.expr(b, l, 4))    // looks like an end
					break;
			} else
				break;
		}
		return r;
	}

	/**
	 * Parses parenthesised list or parenthesised list element. To avoid double parenthesised expression parsing
	 *
	 * @param b PerlBuilder
	 * @param l parsing level
	 * @return parsing result
	 */
	public static boolean parseListOrListElement(PsiBuilder b, int l)
	{
		PsiBuilder.Marker m = b.mark();
		if (PerlParser.parenthesised_expr(b, l))
		{
			if (PerlParser.array_index(b, l))
				m.done(ANON_ARRAY_ELEMENT);
			else
				m.drop();
			return true;
		}
		m.drop();
		return false;
	}


	/**
	 * Parses Array variable or array/hash slice
	 *
	 * @param b PerlBuilder
	 * @param l parsing level
	 * @return parsing result
	 */
	public static boolean parseArrayOrSlice(PsiBuilder b, int l)
	{
		PsiBuilder.Marker m = b.mark();
		if (PerlParser.array_primitive(b, l))
		{
			if (PerlParser.array_index(b, l))
				m.done(ARRAY_ARRAY_SLICE);
			else if (PerlParser.hash_index(b, l))
				m.done(ARRAY_HASH_SLICE);
			else
				m.drop();
			return true;
		}
		m.drop();
		return false;
	}

	/**
	 * Parses scalar variable or hash/array element
	 *
	 * @param b PerlBuilder
	 * @param l parsing level
	 * @return result
	 */
	public static boolean parseScalarOrElement(PsiBuilder b, int l)
	{
		PsiBuilder.Marker m = b.mark();
		if (PerlParser.scalar_primitive(b, l))
		{
			if (PerlParser.array_index(b, l))
				m.done(SCALAR_ARRAY_ELEMENT);
			else if (PerlParser.hash_index(b, l))
				m.done(SCALAR_HASH_ELEMENT);
			else
				m.drop();
			return true;
		}
		m.drop();
		return false;
	}

	/**
	 * Parses typeglob or typeglob with a slot
	 *
	 * @param b PerlBuilder
	 * @param l parsing level
	 * @return result
	 */
	public static boolean parseGlobOrElement(PsiBuilder b, int l)
	{
		PsiBuilder.Marker m = b.mark();
		if (PerlParser.glob_primitive(b, l))
		{
			if (PerlParser.hash_index(b, l))
				m.done(GLOB_SLOT);
			else
				m.drop();
			return true;
		}
		m.drop();
		return false;
	}


	/**
	 * Parses SQ string content. Implemented for interpolation parsing, where 'test' is identifier in quotes
	 *
	 * @param b PerlBuilder
	 * @param l parsing level
	 * @return parsing result
	 */
	public static boolean parseNestedSQString(PsiBuilder b, int l)
	{
		IElementType tokenType;

		if (b.rawLookup(-1) == LEFT_BRACE && consumeToken(b, QUOTE_SINGLE))
		{
			PsiBuilder.Marker m = b.mark();

			while ((tokenType = b.getTokenType()) != null)
			{
				if (CLOSE_QUOTES.contains(tokenType))    // reached end of outer string
				{
					m.drop();
					return false;
				} else if (tokenType == QUOTE_SINGLE)    // reached end of inner string
				{
					m.collapse(STRING_CONTENT);

					if (b.lookAhead(1) == RIGHT_BRACE)
						return consumeToken(b, QUOTE_SINGLE);
				}
				b.advanceLexer();
			}

			m.drop();
		}
		return false;
	}

	/**
	 * Magic for nested string opener
	 *
	 * @param b         PerlPraser
	 * @param l         paring level
	 * @param tokenType opening quote type (atm only QQ)
	 * @return parsing result
	 */
	public static boolean parseNetstedInterpolatedString(PsiBuilder b, int l, IElementType tokenType)
	{
		// [string_content_qq] QUOTE_DOUBLE &RIGHT_BRACE// nested qq string
		if (b.rawLookup(-1) == LEFT_BRACE && consumeToken(b, tokenType))
		{
			assert b instanceof PerlBuilder;
			boolean oldState = ((PerlBuilder) b).getCurrentStringState();
			((PerlBuilder) b).setCurrentStringState(true);

			PerlParser.string_content_qq(b, l);

			((PerlBuilder) b).setCurrentStringState(oldState);

			return (consumeToken(b, tokenType));    // && b.getTokenType() == RIGHT_BRACE  fixme not sure we should check for closing brace here. We could leave this to the outer element
		}

		return false;
	}

	/**
	 * Converting string element to string content if it's not a closing quote
	 *
	 * @param b PerlBuildder
	 * @param l Parsing level
	 * @return parsing result
	 */
	public static boolean convertToStringContent(PsiBuilder b, int l)
	{
		IElementType tokenType = b.getTokenType();
		assert b instanceof PerlBuilder;

		boolean currentStringState = ((PerlBuilder) b).getCurrentStringState();

		if (tokenType != null
				&& !CLOSE_QUOTES.contains(tokenType)
				&& !(currentStringState
				&& (tokenType == QUOTE_DOUBLE || tokenType == QUOTE_TICK)
				&& b.lookAhead(1) == RIGHT_BRACE
		)
				)
		{
			PsiBuilder.Marker m = b.mark();
			b.advanceLexer();

			// reduces nodes number
			while ((tokenType = b.getTokenType()) != null
					&& !STRING_MERGE_STOP_TOKENS.contains(tokenType)
					&& !(
					currentStringState
							&& (tokenType == QUOTE_DOUBLE || tokenType == QUOTE_TICK)
							&& b.lookAhead(1) == RIGHT_BRACE
			)
					)
				b.advanceLexer();

//			m.drop();
			m.collapse(STRING_CONTENT);
			return true;
		}
		return false;
	}


	/**
	 * Converts everything till $, @ or close brace to regex tokens;
	 *
	 * @param b PerlBuilder
	 * @param l parsing level
	 * @return parsing result
	 */
	public static boolean convertRegexToken(PsiBuilder b, int l)
	{

		IElementType tokenType = b.getTokenType();
		if (!REGEX_BLOCK_CLOSER.contains(tokenType))
		{
			PsiBuilder.Marker m = b.mark();
			b.advanceLexer();

			// reduces nodes number
			while (!b.eof() && !REGEX_MERGE_STOP_TOKENS.contains(b.getTokenType()))
				b.advanceLexer();
//			m.drop();
			m.collapse(REGEX_TOKEN);
			return true;
		}
		return false;
	}

	/**
	 * Parses lazy heredoc content
	 *
	 * @param b PerlBuilder
	 * @param l parsing level
	 * @return result
	 */
	public static boolean parseHeredocContent(PsiBuilder b, int l)
	{
		if (b.getTokenType() == HEREDOC_PSEUDO_QUOTE)
		{
			PsiBuilder.Marker m = b.mark();
			b.advanceLexer();
			m.collapse(TokenType.NEW_LINE_INDENT);

			PerlParser.string_content_qq(b, l);
			return true;
		}
		return false;
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
		boolean r = PerlParser.expr(b, l, -1);
		((PerlBuilder) b).setStringify(oldState);
		return r;
	}


	/**
	 * Collapses -bareword to a string if stringify is forced
	 *
	 * @param b PerlBuilder
	 * @param l parsing level
	 * @return result
	 */
	public static boolean parseMinusBareword(PsiBuilder b, int l)
	{
		assert b instanceof PerlBuilder;
		if (((PerlBuilder) b).isStringify()
				&& (b.getTokenType() == OPERATOR_MINUS || b.getTokenType() == OPERATOR_MINUS_MINUS)
				&& b.lookAhead(1) == IDENTIFIER
				)
		{
			PsiBuilder.Marker m = b.mark();
			b.advanceLexer();
			if (!PerlSubUtil.isBuiltIn(b.getTokenText()))
			{
				b.advanceLexer();
				m.collapse(STRING_CONTENT);
				return true;
			}
			m.drop();
		}
		return false;
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
		boolean currentReparseState = ((PerlBuilder) b).setReparseSQString(true);
		boolean currentAllowSigils = ((PerlBuilder) b).setAllowSigils(true);

		boolean r = PerlParser.expr(b, l, -1);

		((PerlBuilder) b).setReparseSQString(currentReparseState);
		((PerlBuilder) b).setAllowSigils(currentAllowSigils);

		return r;
	}

	/**
	 * Parses SQ string depending on reparseSQString flag of PerlBuilder
	 *
	 * @param b PerlBuilder
	 * @param l parsing level
	 * @return parsing result
	 */
	public static boolean parseSQString(PsiBuilder b, int l)
	{
		PsiBuilder.Marker m = b.mark();

		boolean r = PerlParser.string_sq_parsed(b, l);

		if (r && ((PerlBuilder) b).isReparseSQString())
			m.collapse(PARSABLE_STRING_USE_VARS);
		else
			m.drop();

		return r;
	}

	/**
	 * Hack for use vars parameter
	 *
	 * @param b PerlBuilder
	 * @param l parsing level
	 * @return parsing result
	 */
	public static boolean parseInterpolatedConstructs(PsiBuilder b, int l)
	{
		assert b instanceof PerlBuilder;

		if (((PerlBuilder) b).isAllowAllSigils())
			return PerlParser.use_vars_interpolated_constructs(b, l);
		else
			return PerlParser.interpolated_constructs(b, l);
	}

}
