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

import com.intellij.lang.LighterASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.lang.WhitespacesBinders;
import com.intellij.lang.parser.GeneratedParserUtilBase;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.perl.PerlParserDefinition;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.lexer.PerlLexer;
import com.perl5.lang.perl.lexer.PerlLexerUtil;
import com.perl5.lang.perl.parser.builder.PerlBuilder;
import com.perl5.lang.perl.parser.builder.PerlStringWrapper;
import com.perl5.lang.perl.parser.elementTypes.PerlStringContentTokenType;
import com.perl5.lang.perl.util.PerlPackageUtil;
import com.perl5.lang.perl.util.PerlSubUtil;
import org.jetbrains.annotations.NotNull;

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
			NUMBER_SIMPLE,
			OPERATOR_X
	);
	public static final TokenSet PRINT_HANDLE_NEGATE_SUFFIX = TokenSet.orSet(
			TokenSet.create(
					LEFT_BRACE,
					LEFT_BRACKET,
//					LEFT_PAREN,		// can be print STDERR ($a);
					OPERATOR_LT_NUMERIC
			),
			TokenSet.andNot(
					PerlLexer.OPERATORS_TOKENSET,
					TokenSet.create(
							OPERATOR_NOT    // can be print STDERR !$value;
					)
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
	private static final PerlStringWrapper constantWrapper = new PerlStringWrapper(CONSTANT_NAME);
	public static TokenSet LIGHT_CONTAINERS = TokenSet.create(
			HEREDOC,
			HEREDOC_QQ,
			HEREDOC_QX
	);
	// tokens that can be converted between each other depending on context
	public static TokenSet CONVERTABLE_TOKENS = TokenSet.create(
			IDENTIFIER,
			VARIABLE_NAME,
			SUB,
			LABEL,
			RESERVED_METHOD,
			RESERVED_FUNC
	);
	public static TokenSet POST_SIGILS_SUFFIXES = TokenSet.orSet(
			PACKAGE_TOKENS,
			CONVERTABLE_TOKENS,
			TokenSet.create(
					LEFT_BRACE,
					SIGIL_SCALAR,
					NUMBER_SIMPLE
			));

	public static void addConvertableTokens(IElementType... convertableTokens)
	{
		CONVERTABLE_TOKENS = TokenSet.orSet(CONVERTABLE_TOKENS, TokenSet.create(convertableTokens));
		POST_SIGILS_SUFFIXES = TokenSet.orSet(POST_SIGILS_SUFFIXES, CONVERTABLE_TOKENS);
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

		PerlBuilder perlBuilder = new PerlBuilder(builder, state, parser);

		if (root == PARSABLE_STRING_USE_VARS)
		{
			perlBuilder.setUseVarsContent(true);
		}
		return perlBuilder;
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
				return true;
		}

		return false;
	}

	public static boolean parseExpressionLevel(PsiBuilder b, int l, int g)
	{
		return PerlParserImpl.expr(b, l, g);
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
	public static boolean parseListExpression(PsiBuilder b, int l)
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
		{
			// todo we should check current namespace here

			String tokenText = b.getTokenText();
			if (!PerlSubUtil.BUILT_IN_UNARY.contains(tokenText)) // not unary
			{
				boolean r = PerlParserImpl.method(b, l);

				if (r && !PerlSubUtil.BUILT_IN_ARGUMENTLESS.contains(tokenText)) // not argumentless
				{
					PerlParserImpl.call_arguments(b, l);
				}
				return r;
			}
		}
		else if (PACKAGE_TOKENS.contains(tokenType) && CONVERTABLE_TOKENS.contains(nextTokenType) && b.lookAhead(2) != LEFT_PAREN)
		{
			String packageName = b.getTokenText();
			String subName = ((PerlBuilder) b).lookupToken(1).getTokenText();
			//noinspection ConstantConditions
			if (!PerlSubUtil.isUnary(packageName, subName))    // not unary
			{
				boolean r = PerlParserImpl.method(b, l);

				//noinspection ConstantConditions
				if (r && !PerlSubUtil.isArgumentless(packageName, subName)) // not argumentless
				{
					PerlParserImpl.call_arguments(b, l);
				}
				return r;
			}
		}

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

	public static boolean convertBracedString(PsiBuilder b, int l)
	{

		if (CONVERTABLE_TOKENS.contains(b.getTokenType()) && b.lookAhead(1) == RIGHT_BRACE)
		{
			// fixme shouldn't we add string_sq here?
			PsiBuilder.Marker m = b.mark();
			b.advanceLexer();
			m.collapse(STRING_CONTENT);
			m.precede().done(STRING_BARE);
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
		}
		else if (PACKAGE_TOKENS.contains(currentTokenType))
		{
			PsiBuilder.Marker m = b.mark();
			b.advanceLexer();
			m.collapse(PACKAGE);
			return true;
		}

		return false;
	}

	/**
	 * Check for token and converts it to another
	 *
	 * @param b               PerlBuilder
	 * @param l               parsing level
	 * @param targetTokenType token type to convert to
	 * @param sourceTokenType token to check for
	 * @return checking level
	 */
	public static boolean checkAndConvertToken(PsiBuilder b, int l, IElementType targetTokenType, IElementType sourceTokenType)
	{
		return checkAndCollapseToken(b, l, targetTokenType, sourceTokenType);
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
		}
		else if (PACKAGE_TOKENS.contains(tokenType) && CONVERTABLE_TOKENS.contains(b.lookAhead(1)))
		{
			PsiBuilder.Marker m = b.mark();
			b.advanceLexer();
			b.advanceLexer();
			m.collapse(PACKAGE);
			return true;
		}
		else if (
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
		assert b instanceof PerlBuilder;

		if (
				CONVERTABLE_TOKENS.contains(currentTokenType)                // it's identifier
						&& !printHandleNegation(b, l)        // no negation tokens,
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
	 * Checks if next tokens negates filehandle in print statements
	 *
	 * @param b PerlBuilder
	 * @param l parsing level
	 * @return check result
	 */
	public static boolean printHandleNegation(PsiBuilder b, int l)
	{
		return
				PRINT_HANDLE_NEGATE_SUFFIX.contains(b.lookAhead(1))
						|| b.rawLookup(1) == LEFT_PAREN;
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
			String potentialSubName = canonicalPackageName + PerlPackageUtil.PACKAGE_SEPARATOR + nextTokenData.getTokenText();

			if (
					nextNextTokenType == LEFT_PAREN                        // Package::Identifier( - what can it be?
							|| ((PerlBuilder) b).isKnownSub(potentialSubName)       // we know this sub
							|| !((PerlBuilder) b).isKnownPackage(potentialSubName)) // we don't know such package
				return convertPackageIdentifier(b, l) && convertIdentifier(b, l, ((PerlBuilder) b).popSubElementType());
			else
				return false;
		}
		// 	method
		else if (CONVERTABLE_TOKENS.contains(currentTokenType))
		{
			PerlTokenData prevTokenData = ((PerlBuilder) b).lookupToken(-1);

			// ->sub
			if (prevTokenData != null && prevTokenData.getTokenType() == OPERATOR_DEREFERENCE)
				return convertIdentifier(b, l, ((PerlBuilder) b).popSubElementType());
				// may be
				// 	method Foo::
				//	method Foo::Bar
				//  method Foo::othermethod
			else if (PACKAGE_TOKENS.contains(nextTokenType))
			{
				IElementType nextNextTokenType = b.lookAhead(2);

				// sub Foo::->method
				if (nextNextTokenType == OPERATOR_DEREFERENCE)
					return convertIdentifier(b, l, ((PerlBuilder) b).popSubElementType());
					// identifier Package::identifier
				else if (CONVERTABLE_TOKENS.contains(nextNextTokenType))
				{

					// identifier Package::identifier->
					if (b.lookAhead(3) == OPERATOR_DEREFERENCE)
						return convertIdentifier(b, l, ((PerlBuilder) b).popSubElementType());

					PerlTokenData nextTokenData = ((PerlBuilder) b).lookupToken(1);
					PerlTokenData nextNextTokenData = ((PerlBuilder) b).lookupToken(2);

					String packageOrSub = PerlPackageUtil.getCanonicalPackageName(nextTokenData.getTokenText()) + PerlPackageUtil.PACKAGE_SEPARATOR + nextNextTokenData.getTokenText();

					if (((PerlBuilder) b).isKnownSub(packageOrSub))
						return convertIdentifier(b, l, ((PerlBuilder) b).popSubElementType());
					else if (((PerlBuilder) b).isKnownPackage(packageOrSub))
						return convertIdentifier(b, l, ((PerlBuilder) b).popSubElementType()) && mergePackageName(b, l);
					return convertIdentifier(b, l, ((PerlBuilder) b).popSubElementType());
				}
				else
					// it's method Package::
					return convertIdentifier(b, l, ((PerlBuilder) b).popSubElementType()) && convertPackageIdentifier(b, l);
			}
			// may be
			// 	method Foo
			else if (CONVERTABLE_TOKENS.contains(nextTokenType) && b.lookAhead(2) != OPERATOR_DEREFERENCE)
			{
				PerlTokenData nextTokenData = ((PerlBuilder) b).lookupToken(1);

				String potentialSubName = nextTokenData.getTokenText() + PerlPackageUtil.PACKAGE_SEPARATOR + b.getTokenText();
				if (((PerlBuilder) b).isKnownSub(potentialSubName))
					return convertIdentifier(b, l, ((PerlBuilder) b).popSubElementType()) && convertIdentifier(b, l, PACKAGE);
				else
					return convertIdentifier(b, l, ((PerlBuilder) b).popSubElementType());
			}
			// KnownPackage->
			else if (nextTokenType == OPERATOR_DEREFERENCE && ((PerlBuilder) b).isKnownPackage(b.getTokenText()))
				return false;
				// it's just sub
			else
				return convertIdentifier(b, l, ((PerlBuilder) b).popSubElementType());
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
		if (PerlLexer.LABEL_TOKENSET.contains(b.getTokenType()) && b.lookAhead(1) == COLON)
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

		// checking for scalar cast
		if (currentTokenType == SIGIL_SCALAR && (
				POST_SIGILS_SUFFIXES.contains(b.lookAhead(1))
						|| b.rawLookup(1) == OPERATOR_BITWISE_XOR    // fixme this requires more love in lexer see parseCappedVariableName
		))
		{
			return false;
		}

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
		else if (canBeVariableName(currentTokenType, (PerlBuilder) b))
		{
			if (currentTokenType == OPERATOR_BITWISE_XOR && CONTROL_VARIABLE_NAMES.contains(nextTokenType)) // branch for $^]
			{
				m = b.mark();
				b.advanceLexer();
				b.advanceLexer();
				m.collapse(VARIABLE_NAME);
			}
			else
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
					return convertVariableName(b);
				}
				else if (nextTokenType == RIGHT_BRACE)
				{
					b.advanceLexer();
					return true;
				}
			}
			// ${var}
			else if (canBeVariableName(currentTokenType, (PerlBuilder) b) && nextTokenType == RIGHT_BRACE)
			{
				return convertVariableName(b);
			}
		}

		return false;
	}

	protected static boolean canBeVariableName(IElementType elementType, PerlBuilder b)
	{
		return CONVERTABLE_TOKENS.contains(elementType) || b.isSpecialVariableNamesAllowed() && SPECIAL_VARIABLE_NAME.contains(elementType);
	}

	protected static boolean convertVariableName(PsiBuilder b)
	{
		PsiBuilder.Marker mv = b.mark();
		b.advanceLexer();
		mv.collapse(VARIABLE_NAME);
		b.advanceLexer();
		return true;
	}

	protected static boolean isOperatorToken(PsiBuilder b, int l)
	{
		return PerlLexer.OPERATORS_TOKENSET.contains(b.getTokenType());
	}

	public static boolean parseAmbiguousSigil(PsiBuilder b, int l, IElementType sigilTokenType, IElementType targetTokenType)
	{
		IElementType tokenType = b.getTokenType();
		if (tokenType == targetTokenType)
		{
			b.advanceLexer();
			return true;
		}
		else if (tokenType == sigilTokenType)
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
		IElementType currentTokenType = b.getTokenType();

		return !(
				currentTokenType == null ||                                                                                 // got end of file
						!((PerlBuilder) b).getPerlParser().getStatementRecoveryConsumableTokenSet().contains(currentTokenType)
		);
	}

	/**
	 * Parsing hash index, counting braces, smart error on empty expr
	 *
	 * @param b PerlBuilder
	 * @param l parsing level
	 * @return parsing result
	 */
	public static boolean parseHashIndex(PsiBuilder b, int l)
	{
		if (consumeToken(b, LEFT_BRACE))
		{
			boolean r = convertBracedString(b, l);
			if (!r) r = PerlParserImpl.expr(b, l, -1);

			if (!r && b.getTokenType() == RIGHT_BRACE)
			{
				r = true;
				b.mark().error("Empty hash index");    // fixme this must be done via inspection or annotator
			}

			return r && consumeToken(b, RIGHT_BRACE);
		}
		return false;
	}

	/**
	 * Parsing array index, smart error on empty expr
	 *
	 * @param b PerlBuilder
	 * @param l parsing level
	 * @return parsing result
	 */
	public static boolean parseArrayIndex(PsiBuilder b, int l)
	{
		if (consumeToken(b, LEFT_BRACKET))
		{
			boolean r = false;
			assert b instanceof PerlBuilder;
			if (((PerlBuilder) b).isRegex())    // we could take it from ErrorState stack. I guess...
			{
				r = PerlParserImpl.interpolated_constructs(b, l);    // we can't parse an expression here, cause it's pinned inside
				if (!r)
					r = PerlParserImpl.number_constant(b, l);    // little hack for plain number. Basically we need to use expr here with pin checking
			}
			else
			{
				r = PerlParserImpl.expr(b, l + 1, -1);

				if (!r && b.getTokenType() == RIGHT_BRACKET)
				{
					r = true;
					b.mark().error("Empty array index");    // fixme this must be done via inspection or annotator
				}
			}
			return r && consumeToken(b, RIGHT_BRACKET);
		}
		return false;
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
				if (!PerlParserImpl.expr(b, l, 4))    // looks like an end
					break;
			}
			else
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
		if (PerlParserImpl.parenthesised_expr(b, l))
		{
			if (PerlParserImpl.array_index(b, l))
			{
				m.done(ANON_ARRAY_ELEMENT);
			}
			else
			{
				m.drop();
			}
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
		if (PerlParserImpl.array_primitive(b, l))
		{
			assert b instanceof PerlBuilder;

			b.getTokenType();
			IElementType lastRawTokenType = b.rawLookup(-1);

			if (
					!((PerlBuilder) b).isInterpolated()
							|| lastRawTokenType != TokenType.WHITE_SPACE
					)
			{

				if (PerlParserImpl.array_index(b, l))
				{
					m.done(ARRAY_ARRAY_SLICE);
				}
				else if (PerlParserImpl.hash_index(b, l))
				{
					m.done(ARRAY_HASH_SLICE);
				}
				else
				{
					m.drop();
				}
			}
			else
			{
				m.drop();
			}

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
		if (PerlParserImpl.scalar_primitive(b, l))
		{
			assert b instanceof PerlBuilder;

			b.getTokenType();
			IElementType lastRawTokenType = b.rawLookup(-1);

			if (
					!((PerlBuilder) b).isInterpolated()
							|| lastRawTokenType != TokenType.WHITE_SPACE
					)
			{

				if (PerlParserImpl.array_index(b, l))
				{
					m.done(SCALAR_ARRAY_ELEMENT);
				}
				else if (PerlParserImpl.hash_index(b, l))
				{
					m.done(SCALAR_HASH_ELEMENT);
				}
				else
				{
					m.drop();
				}
			}
			else
			{
				m.drop();
			}
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
		if (PerlParserImpl.glob_primitive(b, l))
		{
			if (PerlParserImpl.hash_index(b, l))
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

		if (b.getTokenType() == QUOTE_SINGLE)
		{ // b.rawLookup(-1) == LEFT_BRACE &&
			PsiBuilder.Marker m = b.mark();
			b.advanceLexer();
			m.collapse(QUOTE_SINGLE_OPEN);

			m = b.mark();

			while ((tokenType = b.getTokenType()) != null)
			{
				if (CLOSE_QUOTES.contains(tokenType))    // reached end of outer string
				{
					m.drop();
					return false;
				}
				else if (tokenType == QUOTE_SINGLE)    // reached end of inner string
				{
					m.collapse(STRING_CONTENT);
					m = b.mark();
					b.advanceLexer();
					m.collapse(QUOTE_SINGLE_CLOSE);
					return true;
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
	 * @param b              PerlPraser
	 * @param l              paring level
	 * @param quoteTokenType opening quote type (atm only QQ)
	 * @return parsing result
	 */
	public static boolean parseNetstedInterpolatedString(PsiBuilder b, int l, IElementType quoteTokenType)
	{
		assert b instanceof PerlBuilder;

		IElementType tokenType = b.getTokenType();

		if (((PerlBuilder) b).getExtraStopQuote() != quoteTokenType && tokenType == quoteTokenType)
		{
			PsiBuilder.Marker m = b.mark();
			b.advanceLexer();
			if (quoteTokenType == QUOTE_DOUBLE)
			{
				m.collapse(QUOTE_DOUBLE_OPEN);
			}
			else if (quoteTokenType == QUOTE_TICK)
			{
				m.collapse(QUOTE_TICK_OPEN);
			}
			else
			{
				throw new RuntimeException("Unknown open quote for token " + quoteTokenType);
			}

			IElementType currentStopQuote = ((PerlBuilder) b).setExtraStopQuote(quoteTokenType);

			parseInterpolatedStringContent(b, l);

			((PerlBuilder) b).setExtraStopQuote(currentStopQuote);

			if ((b.getTokenType()) == quoteTokenType)
			{
				m = b.mark();
				b.advanceLexer();
				if (quoteTokenType == QUOTE_DOUBLE)
				{
					m.collapse(QUOTE_DOUBLE_CLOSE);
				}
				else if (quoteTokenType == QUOTE_TICK)
				{
					m.collapse(QUOTE_TICK_CLOSE);
				}
				else
				{
					throw new RuntimeException("Unknown open quote for token " + quoteTokenType);
				}
				return true;
			}
			return false;
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

		boolean isStopOnNumericGt = ((PerlBuilder) b).isStopOnNumericGt();
		IElementType extraStopQuote = ((PerlBuilder) b).getExtraStopQuote();

		if (tokenType != null
				&& !(isStopOnNumericGt && tokenType == OPERATOR_GT_NUMERIC)    // stop bare glob
				&& !(!isStopOnNumericGt && CLOSE_QUOTES.contains(tokenType))    // stop on close quote
				&& tokenType != extraStopQuote
				)
		{
			IElementType targetToken = PerlLexerUtil.remapSQToken(tokenType);

			PsiBuilder.Marker m = b.mark();

			if (targetToken == STRING_PACKAGE && b.rawLookup(1) == IDENTIFIER)    // we suppose it's Foo::Bar
			{
				b.advanceLexer();
			}
			b.advanceLexer();

//			m.drop();
			m.collapse(targetToken);

			PerlStringWrapper stringWrapper = ((PerlBuilder) b).getStringWrapper();
			if (stringWrapper != null && targetToken == STRING_IDENTIFIER && stringWrapper.canProcess())
			{
				stringWrapper.wrapMarker(m);
			}

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
			while (!b.eof() && !REGEX_MERGE_STOP_TOKENS.contains(tokenType = b.getTokenType()))
			{
				b.advanceLexer();
			}
//			m.drop();
			m.collapse(REGEX_TOKEN);
			return true;
		}
		return false;
	}

	/**
	 * Converts everything till $, @ or close brace to regex tokens; Spaces and newlines must be escaped
	 *
	 * @param b PerlBuilder
	 * @param l parsing level
	 * @return parsing result
	 */
	public static boolean convertRegexTokenEx(PsiBuilder b, int l)
	{

		IElementType tokenType = b.getTokenType();
		if (!REGEX_BLOCK_CLOSER.contains(tokenType))
		{
			PsiBuilder.Marker m = b.mark();
			b.advanceLexer();

			IElementType prevRawTokenType;

			// reduces nodes number
			while (!b.eof()
					&& !REGEX_MERGE_STOP_TOKENS.contains(tokenType = b.getTokenType())
					&& (prevRawTokenType = b.rawLookup(-1)) != TokenType.WHITE_SPACE
					&& prevRawTokenType != TokenType.NEW_LINE_INDENT)
			{
				b.advanceLexer();

			}
//			m.drop();
			m.collapse(REGEX_TOKEN);
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
		boolean r = PerlParserImpl.expr(b, l, -1);
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
		boolean currentUseVarsState = ((PerlBuilder) b).setUseVarsContent(true);

		boolean r = PerlParserImpl.expr(b, l, -1);

		((PerlBuilder) b).setReparseSQString(currentReparseState);
		((PerlBuilder) b).setUseVarsContent(currentUseVarsState);

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

		boolean r = PerlParserImpl.string_sq_parsed(b, l);

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

		if (((PerlBuilder) b).isUseVarsContent())
		{
			PsiBuilder.Marker m = b.mark();
			boolean r = PerlParserImpl.use_vars_interpolated_constructs(b, l);

			if (r)
			{
				LighterASTNode lastMarker = b.getLatestDoneMarker();

				if (lastMarker != null)
				{
					IElementType elementType = lastMarker.getTokenType();

					if (elementType == SCALAR_VARIABLE || elementType == ARRAY_VARIABLE || elementType == HASH_VARIABLE)
					{
						m.done(VARIABLE_DECLARATION_WRAPPER);
						return true;
					}
				}
			}
			m.drop();
			return r;
		}
		else
		{
			return PerlParserImpl.interpolated_constructs(b, l);
		}
	}

	public static boolean parseFileHandleAsString(PsiBuilder b, int l)
	{
		assert b instanceof PerlBuilder;
		((PerlBuilder) b).setStopOnNumericGt(true);
		boolean r = parseInterpolatedStringContent(b, l);
		((PerlBuilder) b).setStopOnNumericGt(false);
		return r;
	}

	/**
	 * Parsing interpolated string contents
	 *
	 * @param b PerlBuilder
	 * @param l parsing level
	 * @return parsing result
	 */
	public static boolean parseInterpolatedStringContent(PsiBuilder b, int l)
	{
		assert b instanceof PerlBuilder;
		boolean currentState = ((PerlBuilder) b).setIsInterpolated(true);
		boolean r = PerlParserImpl.string_content_qq(b, l);
		((PerlBuilder) b).setIsInterpolated(currentState);
		return r;
	}

	/**
	 * Setting flag of regex contents and parse regex
	 *
	 * @param b          PerlBuilder
	 * @param l          parsing level
	 * @param isExtended marks extended regex to use other content processor
	 * @return parsing result
	 */
	public static boolean parseRegexContent(PsiBuilder b, int l, boolean isExtended)
	{
		assert b instanceof PerlBuilder;
		boolean currentState = ((PerlBuilder) b).setIsRegex(true);
		boolean r = isExtended ? PerlParserImpl.perl_regex_ex_items(b, l) : PerlParserImpl.perl_regex_items(b, l);
		((PerlBuilder) b).setIsRegex(currentState);
		return r;
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
	 * Checking if it's angle or LT after block
	 *
	 * @param b PerlBuildfer
	 * @param l parsing level
	 * @return parsing result
	 */
	public static boolean checkFileReadToken(PsiBuilder b, int l)
	{
		IElementType tokenType = b.getTokenType();
		if (tokenType == LEFT_ANGLE)
		{
			b.advanceLexer();
			return true;
		}
		if (tokenType == OPERATOR_LT_NUMERIC && b.getLatestDoneMarker() != null && b.getLatestDoneMarker().getTokenType() == BLOCK)
		{
			PsiBuilder.Marker m = b.mark();
			b.advanceLexer();
			m.collapse(LEFT_ANGLE);
			return true;
		}

		return false;
	}

	/**
	 * Parsing braced cast content. For interpolated strings and regex we should disable appropriate flags to remove limitations
	 *
	 * @param b PerlBuilder
	 * @param l parsing level
	 * @return result
	 */
	public static boolean parseBracedCastContent(PsiBuilder b, int l)
	{
		assert b instanceof PerlBuilder;

		boolean oldInterpolatedState = ((PerlBuilder) b).setIsInterpolated(false);
		boolean oldRegexState = ((PerlBuilder) b).setIsRegex(false);

		boolean r = PerlParserImpl.block_content(b, l);

		((PerlBuilder) b).setIsInterpolated(oldInterpolatedState);
		((PerlBuilder) b).setIsRegex(oldRegexState);

		return r;
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
	 * Parsing sub name, any identifier might be
	 *
	 * @param b Perlbuilder
	 * @param l parsing level
	 * @return parsing result
	 */
	public static boolean parseSubDefinitionName(PsiBuilder b, int l)
	{
		IElementType tokenType = b.getTokenType();
		if (CONVERTABLE_TOKENS.contains(tokenType) || PerlLexer.RESERVED_TOKENSET.contains(tokenType))
		{
			PsiBuilder.Marker m = b.mark();
			b.advanceLexer();
			m.collapse(SUB);
			return true;
		}
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
			if (stringWrapper == null || !stringWrapper.canProcess() || tokenType != STRING_IDENTIFIER)
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

	public static boolean parseConstantDefinition(PsiBuilder b, int l)
	{
		assert b instanceof PerlBuilder;
		PerlStringWrapper oldValue = ((PerlBuilder) b).setStringWrapper(constantWrapper);
		boolean r = PerlParserImpl.string(b, l);
		((PerlBuilder) b).setStringWrapper(oldValue);
		return r;
	}

	public static boolean parsePrintArguments(PsiBuilder b, int l)
	{
		PsiBuilder.Marker m = b.mark();
		if (PerlParserImpl.print_arguments_content(b, l))
		{
			m.done(CALL_ARGUMENTS);
			return true;
		}
		else
		{
			m.rollbackTo();
			return false;
		}
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

	public static boolean parseEmptyString(PsiBuilder b, int l)
	{
		IElementType tokenType = b.getTokenType();
		IElementType nextTokenType = b.rawLookup(1);
		if (tokenType == QUOTE_DOUBLE_OPEN && nextTokenType == QUOTE_DOUBLE_CLOSE ||
				tokenType == QUOTE_SINGLE_OPEN && nextTokenType == QUOTE_SINGLE_CLOSE ||
				tokenType == QUOTE_TICK_OPEN && nextTokenType == QUOTE_TICK_CLOSE
				)
		{
			PsiBuilder.Marker m = b.mark();
			b.advanceLexer();
			b.mark().collapse(STRING_CONTENT);
			b.advanceLexer();

			if (tokenType == QUOTE_DOUBLE_OPEN)
			{
				m.done(STRING_DQ);
			}
			else if (tokenType == QUOTE_SINGLE_OPEN)
			{
				m.done(STRING_SQ);
			}
			else
			{
				m.done(STRING_XQ);
			}
			return true;
		}
		return false;
	}

}
