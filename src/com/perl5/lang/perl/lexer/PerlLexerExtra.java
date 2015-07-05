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

package com.perl5.lang.perl.lexer;

import com.intellij.openapi.project.DumbService;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.perl.util.PerlGlobUtil;
import com.perl5.lang.perl.util.PerlPackageUtil;
import com.perl5.lang.perl.util.PerlSubUtil;

import java.util.Arrays;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hurricup on 30.06.2015.
 * This file contains my attempt to guess ambigious things like: *, %, <, >, &
 * I've stopped this direction after realized that i need to parse in lexer, cause }%$ may be a mod if it was hash
 * element before brace and may be a hash sigil if it was a block and lexer does not know it
 * At the moment this is a deadend
 */
public class PerlLexerExtra
{
//	protected boolean openedAngle = false;
//
//	public IElementType guessOpenAngle()
//	{
//		if (LEFT_ANGLE_PREFIX.contains(lastSignificantTokenType))
//		{
//			openedAngle = true;
//			return LEFT_ANGLE;
//		}
//		return OPERATOR_LT_NUMERIC;
//	}
//
//
//	public IElementType guessCloseAngle()
//	{
//		if (openedAngle)
//		{
//			openedAngle = false;
//			return RIGHT_ANGLE;
//		}
//		return OPERATOR_GT_NUMERIC;
//	}
//
//	/**
//	 * Here we should decide if it's OPERATOR_MUL or PERL_SIGIL_GLOB
//	 *
//	 * @return proper token type
//	 */
//public IElementType guessMul()
//{
//	Character nextChar = getNextCharacter();
//	String tokenText = yytext().toString();
//
//	if (lastSignificantTokenType == null
//			|| GLOB_SIGIL_PREFIX.contains(lastSignificantTokenType)
//			|| OPERATORS_TOKENSET.contains(lastSignificantTokenType)
//			|| (nextChar != null && (
//			nextChar == '{'                    // *{...
//					|| nextChar == '_'                    // *{...
//					|| nextChar == ':'                // *::..
//					|| Character.isLetter(nextChar)    // *[a-z]...
//	)))
//	{
//		if( tokenText.length() > 1)
//		{
//			pushState();
//			yybegin(LEX_PREPARSED_ITEMS);
//			tokensList.clear();
//			int tokenStart = getTokenStart();
//			tokensList.add(new CustomToken(tokenStart + 1, getTokenEnd(), SIGIL_SCALAR));
//			setTokenEnd(tokenStart+1);
//		}
//		return SIGIL_GLOB;
//	}
//	else
//	{
//		if( tokenText.length() > 1 )
//			yypushback(tokenText.length() - 1);
//
//		return OPERATOR_MUL;
//	}
//}
//
//	/**
//	 * Here we should decide if it's OPERATOR_AMP or PERL_SIGIL_CODE
//	 *
//	 * @return proper token type
//	 */
//	public IElementType guessAmp()
//	{
//		Character nextChar = getNextCharacter();
//		String tokenText = yytext().toString();
//
//		if (lastSignificantTokenType == null
//				|| CODE_SIGIL_PREFIX.contains(lastSignificantTokenType)
//				|| OPERATORS_TOKENSET.contains(lastSignificantTokenType)
//				|| (nextChar != null && (
//				nextChar == '{'                            // &{...
//						|| nextChar == '_'                        // &_...
////						|| nextChar == '$'                // &$... fixme this triggers in wrong places
//						|| nextChar == ':'                // &::..
//						|| Character.isLetter(nextChar)    // &[a-z]...
//		)))
//		{
//			if( tokenText.length() > 1)
//			{
//				pushState();
//				yybegin(LEX_PREPARSED_ITEMS);
//				tokensList.clear();
//				int tokenStart = getTokenStart();
//				tokensList.add(new CustomToken(tokenStart + 1, getTokenEnd(), SIGIL_SCALAR));
//				setTokenEnd(tokenStart+1);
//			}
//			return SIGIL_CODE;
//		}
//		else
//		{
//			if( tokenText.length() > 1 )
//				yypushback(tokenText.length() - 1);
//
//			return OPERATOR_BITWISE_AND;
//		}
//	}
//
//	/**
//	 * Here we should decide if it's OPERATOR_MOD or PERL_SIGIL_HASH
//	 *
//	 * @return proper token type
//	 */
//	public IElementType guessMod()
//	{
//		Character nextChar = getNextCharacter();
//		String tokenText = yytext().toString();
//
//		if (
//				lastSignificantTokenType != VARIABLE_NAME &&
//						(
//								lastSignificantTokenType == null
//										|| HASH_SIGIL_PREFIX.contains(lastSignificantTokenType)
//										|| OPERATORS_TOKENSET.contains(lastSignificantTokenType)
//										|| (nextChar != null && (
//										nextChar == '{'                    // %{...
//												|| nextChar == '_'                // %_...
//												|| nextChar == '$'                // %$...
//												|| nextChar == ':'                // %::..
//												|| Character.isLetter(nextChar)    // %[a-z]...
//								)
//								)
//						)
//				)
//		{
//			if( tokenText.length() > 1)
//			{
//				pushState();
//				yybegin(LEX_PREPARSED_ITEMS);
//				tokensList.clear();
//				int tokenStart = getTokenStart();
//				tokensList.add(new CustomToken(tokenStart + 1, getTokenEnd(), SIGIL_SCALAR));
//				setTokenEnd(tokenStart+1);
//			}
//			return SIGIL_HASH;
//		}
//		else
//		{
//			if( tokenText.length() > 1 )
//				yypushback(tokenText.length() - 1);
//
//			return OPERATOR_MOD;
//		}
//	}

//	@Override
//	public IElementType parseScalarSigil()
//	{
//		String tokenText = yytext().toString();
//		Character nextSignificantCharacter = getNextSignificantCharacter();
//
//		if( tokenText.length() > 1)	// may be sequential sigils or sequential sigils and built in $$
//		{
//			pushState();
//			yybegin(LEX_PREPARSED_ITEMS);
//			tokensList.clear();
//
//			int tokenStart = getTokenStart();
//
//			if( nextSignificantCharacter != null
//					&& (
//					Character.isLetterOrDigit(nextSignificantCharacter)	// $$$$varname
//							|| nextSignificantCharacter.equals('{')	// $$$${varname}
//			)
//					)
//			{
//				// just sigils
//				tokensList.add(new CustomToken(tokenStart + 1, getTokenEnd(), SIGIL_SCALAR));
//			}
//			else
//			{
//				// sigils and $$
//				int tokenEnd = getTokenEnd();
//
//				if( tokenEnd -1 > tokenStart + 1)
//					tokensList.add(new CustomToken(tokenStart + 1, tokenEnd-1, SIGIL_SCALAR));
//				tokensList.add(new CustomToken(tokenEnd-1, tokenEnd, VARIABLE_NAME));
//			}
//			setTokenEnd(tokenStart + 1);
//		}
//		else if( lastSignificantTokenType == LEFT_BRACE && nextSignificantCharacter != null && nextSignificantCharacter.equals('}')) // for ${$}
//			return VARIABLE_NAME;
//
//		return SIGIL_SCALAR;
//	}
//
//	@Override
//	public IElementType parseScalarSigilIndex()
//	{
//		String tokenText = yytext().toString();
//		if( tokenText.length() > 2)
//		{
//			pushState();
//			yybegin(LEX_PREPARSED_ITEMS);
//			tokensList.clear();
//			int tokenStart = getTokenStart();
//			tokensList.add(new CustomToken(tokenStart + 2, getTokenEnd(), SIGIL_SCALAR));
//			setTokenEnd(tokenStart + 2);
//		}
//
//		return SIGIL_SCALAR_INDEX;
//	}
//
//	@Override
//	public IElementType parseArraySigil()
//	{
//		String tokenText = yytext().toString();
//		if( tokenText.length() > 1)
//		{
//			pushState();
//			yybegin(LEX_PREPARSED_ITEMS);
//			tokensList.clear();
//			int tokenStart = getTokenStart();
//			tokensList.add(new CustomToken(tokenStart + 1, getTokenEnd(), SIGIL_SCALAR));
//			setTokenEnd(tokenStart + 1);
//		}
//
//		return SIGIL_ARRAY;
//	}

//	// tokens that preceeds <FH> opening angle
//	public static final TokenSet LEFT_ANGLE_PREFIX = TokenSet.create(
//			RESERVED_PRINT,
//			RESERVED_PRINTF,
//			RESERVED_SAY,
//			RESERVED_WHILE,
//
//			OPERATOR_CMP_NUMERIC,
//			OPERATOR_LE_NUMERIC,
//			OPERATOR_GE_NUMERIC,
//			OPERATOR_EQ_NUMERIC,
//			OPERATOR_NE_NUMERIC,
//			OPERATOR_LT_NUMERIC,
//			OPERATOR_GT_NUMERIC,
//
//			OPERATOR_CMP_STR,
//			OPERATOR_LE_STR,
//			OPERATOR_GE_STR,
//			OPERATOR_EQ_STR,
//			OPERATOR_NE_STR,
//			OPERATOR_LT_STR,
//			OPERATOR_GT_STR,
//
//			OPERATOR_HELLIP,
//			OPERATOR_FLIP_FLOP,
//			OPERATOR_CONCAT,
//
//			OPERATOR_POW,
//
//			OPERATOR_RE,
//			OPERATOR_NOT_RE,
//
//			OPERATOR_HEREDOC,
//			OPERATOR_SHIFT_LEFT,
//			OPERATOR_SHIFT_RIGHT,
//
//			OPERATOR_SMARTMATCH,
//
//			OPERATOR_AND,
//			OPERATOR_OR,
//			OPERATOR_OR_DEFINED,
//			OPERATOR_NOT,
//
//			OPERATOR_ASSIGN,
//			OPERATOR_POW_ASSIGN,
//
//			OPERATOR_PLUS_ASSIGN,
//			OPERATOR_MINUS_ASSIGN,
//			OPERATOR_CONCAT_ASSIGN,
//
//			OPERATOR_MUL_ASSIGN,
//			OPERATOR_DIV_ASSIGN,
//			OPERATOR_MOD_ASSIGN,
//			OPERATOR_X_ASSIGN,
//
//			OPERATOR_BITWISE_AND_ASSIGN,
//			OPERATOR_BITWISE_OR_ASSIGN,
//			OPERATOR_BITWISE_XOR_ASSIGN,
//
//			OPERATOR_SHIFT_LEFT_ASSIGN,
//			OPERATOR_SHIFT_RIGHT_ASSIGN,
//
//			OPERATOR_AND_ASSIGN,
//			OPERATOR_OR_ASSIGN,
//			OPERATOR_OR_DEFINED_ASSIGN,
//
//			OPERATOR_TRENAR_IF,
//			OPERATOR_TRENAR_ELSE,
//
//			OPERATOR_REFERENCE,
//
//			OPERATOR_DIV,
//			OPERATOR_MUL,
//			OPERATOR_MOD,
//			OPERATOR_PLUS,
//			OPERATOR_MINUS,
//
//			OPERATOR_BITWISE_NOT,
//			OPERATOR_BITWISE_AND,
//			OPERATOR_BITWISE_OR,
//			OPERATOR_BITWISE_XOR,
//
//			OPERATOR_AND_LP,
//			OPERATOR_OR_LP,
//			OPERATOR_XOR_LP,
//			OPERATOR_NOT_LP,
//
//			OPERATOR_COMMA,
//			OPERATOR_COMMA_ARROW,
//
//			SEMICOLON,
//			COLON,
//			LEFT_PAREN,
//			LEFT_BRACE,
//			LEFT_BRACKET
//	);


//	// tokens that preceeds code sigil
//	public static final TokenSet CODE_SIGIL_PREFIX = TokenSet.create(
//			SEMICOLON,
//			COLON,
//			LEFT_PAREN,
//			LEFT_BRACE,
//			LEFT_BRACKET
//	);
//
//	// tokens that preceeds hash sigil
//	public static final TokenSet HASH_SIGIL_PREFIX = TokenSet.create(
//			SEMICOLON,
//			COLON,
//			LEFT_PAREN,
//			LEFT_BRACE,
//			LEFT_BRACKET
//	);
//
//	// tokens that preceeds mul operator, negates glob sigil, checked first
//	public static final TokenSet MUL_OPERATOR_PREFIX = TokenSet.create(
//			RIGHT_PAREN,
//			RIGHT_BRACKET
//	);
//
//
//	// tokens that preceeds glob sigil
//	public static final TokenSet GLOB_SIGIL_PREFIX = TokenSet.orSet(
//			TokenSet.create(
//					SEMICOLON,
//					COLON,
//					LEFT_PAREN,
//					LEFT_BRACE,
//					LEFT_BRACKET
//			),
//			OPERATORS_TOKENSET
//	);

//
//	/**
//	 * Guesses if it's package or package::method or package::variable
//	 *
//	 * @return token type or null if re-parsed
//	 */
//	public IElementType guessPackageName()
//	{
//		// check if sigil was before, check if
//		String packageName = yytext().toString();
//
//		String canonicalPackageName = PerlPackageUtil.getCanonicalPackageName(packageName);
//		String subPackageName = "";
//		String subCanonicalPackageName = "";
//		String subPackageTail = "";
//
//		Matcher m = ambigousPackage.matcher(packageName);
//		if (m.matches())
//		{
//			try
//			{
//				subPackageName = m.group(1);
//				subCanonicalPackageName = PerlPackageUtil.getCanonicalPackageName(subPackageName);
//				subPackageTail = m.group(2);
//
//			} catch (Exception e)
//			{
//				e.printStackTrace();
//			}
//		} else
//			throw new RuntimeException("Inappropriate package name " + packageName);
//
//		Character nextSignificantCharacter = getNextSignificantCharacter();
//
//		// lastcontroltokentype here causes @{sub()} to be parsed like array
//		if (SIGILS_TOKENS.contains(lastUnbraceTokenType)
//				&& (lastSignificantTokenType != LEFT_BRACE
//				|| nextSignificantCharacter != null && nextSignificantCharacter.equals('}')
//		)
//				) // 100% variable name with package
//		{
//			tokensList.clear();
//			tokensList.add(new CustomToken(getTokenStart() + subPackageName.length(), getTokenEnd(), VARIABLE_NAME));
//			pushState();
//			yybegin(LEX_PREPARSED_ITEMS);
//			setTokenEnd(getTokenStart() + subPackageName.length());
//		} else if (prePackageTokenTypes.contains(lastSignificantTokenType))    // 100% package
//		{
//			// we shouldn't store used namespaces into the knownPackages
//			if (lastSignificantTokenType == RESERVED_USE || lastSignificantTokenType == RESERVED_REQUIRE)
//				return PACKAGE;
//		} else
//		{
//
//			boolean mayBeSub = KNOWN_SUBS.contains(canonicalPackageName);
//			boolean mayBePackage = KNOWN_PACKAGES.contains(canonicalPackageName);
//
//
//			if (    // 100% package::sub from perl perspective
//					FORCED_PACKAGES.contains(subCanonicalPackageName)    // main/SUPER/CORE
//							|| !mayBePackage    // no such package
//							|| mayBeSub && nextSignificantCharacter != null && nextSignificantCharacter.equals('(') // has sub and package
//					)
//			{
//				tokensList.clear();
//				tokensList.add(new CustomToken(getTokenStart() + subPackageName.length(), getTokenEnd(), SUB));
//				pushState();
//				yybegin(LEX_PREPARSED_ITEMS);
//				setTokenEnd(getTokenStart() + subPackageName.length());
//			}
//		}
//
//		return getPackageTokenType();
//	}
//
//	/**
//	 * Guessing bareword as function or package, if it has been used before
//	 *
//	 * @return token type
//	 */
//	@Override
//	public IElementType guessBareword()
//	{
//		String bareword = yytext().toString();
//		IElementType tokenType = null;
//
//		// todo check for defined labels
//
////		Character nextCharacter = getNextCharacter();
//		Character nextSignificantCharacter = getNextSignificantCharacter();
//
//		boolean isFollowedRightBrace = ((Character) '}').equals(nextSignificantCharacter);
//
//		// ->bareword
//		if (lastSignificantTokenType == OPERATOR_DEREFERENCE)
//			return SUB;
//			// $/@/... bareword
//		else if (SIGILS_TOKENS.contains(lastUnbraceTokenType)
//				&& (lastSignificantTokenType != LEFT_BRACE || isFollowedRightBrace)
//				)
//			return VARIABLE_NAME;
//			// package/use/no/require/... bareword
//		else if (prePackageTokenTypes.contains(lastSignificantTokenType))
//			return getPackageTokenType();
//		else if (lastSignificantTokenType == LEFT_BRACE && isFollowedRightBrace && PRE_BRACED_STRING_TOKENS.contains(lastUnbraceTokenType))
//			return STRING_CONTENT;
//			// BEGIN, INIT, etc
//		else if ((tokenType = blockNames.get(bareword)) != null)
//			return tokenType;
//			// __PACKAGE__, __LINE__ etc
//		else if ((tokenType = tagNames.get(bareword)) != null)
//			return tokenType;
//			// and/not/defined/scalar/...
//		else if ((tokenType = namedOperators.get(bareword)) != null)
//			return tokenType;
//			// filetest [(] bareword (])
//		else if (preHandleTokenTypes.contains(lastUnparenTokenType)
//				&& !((Character) '(').equals(nextSignificantCharacter)
//				&& (lastSignificantTokenType != LEFT_PAREN || ((Character) ')').equals(nextSignificantCharacter))
//				)
//			return getHandleTokenType();
//			// reserved bareword
//		else if ((tokenType = reservedTokenTypes.get(bareword)) != null)
//		{
//			if (tokenType == RESERVED_QW)
//				processQuoteLikeListOpener();
//			else if (tokenType == RESERVED_TR || tokenType == RESERVED_Y)
//				processTransOpener();
//			else if (tokenType == RESERVED_Q || tokenType == RESERVED_QQ || tokenType == RESERVED_QX)
//				processQuoteLikeStringOpener();
//			else if (tokenType == RESERVED_S || tokenType == RESERVED_M || tokenType == RESERVED_QR)
//				processRegexOpener();
//			else if (tokenType == RESERVED_SUB)
//			{
//				pushState();
//				yybegin(LEX_SUB_NAME);
//			}
//
//			return tokenType;
//		}
//		// next/redo/last/goto bareword
//		else if (preLabelTokenTypes.contains(lastUnparenTokenType))
//			return LABEL;
//			// already known subs
//		else if (knownSubs.contains(bareword))
//			return SUB;
//			// built in subs (large list)
//		else if (PerlSubUtil.BUILT_IN.contains(bareword))
//			return getSubTokenType();
//			// previously confirmed handles
//		else if (knownHandles.contains(bareword))
//			return HANDLE;
//			// previously confirmed packages
//		else if (knownPackages.contains(bareword))
//			return PACKAGE;
//			// built in filehandles
//		else if (PerlGlobUtil.BUILT_IN.contains(bareword))
//			return HANDLE;
//			// open/binmode/etc with filehandle
//		else if (preHandleTokens.contains(lastUnparenToken) && preHandleProperSuffix.contains(nextSignificantCharacter))
//			return getHandleTokenType();
//			// print/say/printf HANDLE
//		else if (preHandleTokenTypesPrint.contains(lastUnparenTokenType) && !printHandleNegativeChars.contains(nextSignificantCharacter))
//			return getHandleTokenType();
//			// built in packages (large list)
//		else if (PerlPackageUtil.BUILT_IN.contains(bareword))
//			return getPackageTokenType();
//			// indexed packages
//		else if (myProject != null && !DumbService.isDumb(myProject) && PerlPackageUtil.findNamespaceDefinitions(myProject, bareword).size() > 0)
//			return getPackageTokenType();
//
//		return SUB;
//	}
//
//	/**
//	 * Returns any next character or null if eof
//	 *
//	 * @return character
//	 */
//	private Character getNextCharacter()
//	{
//		int currentPosition = getTokenEnd();
//		int bufferEnd = getBufferEnd();
//		CharSequence buffer = getBuffer();
//		if (currentPosition < bufferEnd)
//			return buffer.charAt(currentPosition);
//		return null;
//	}
//
//	/**
//	 * searching for next non-spcae/newline/comment character
//	 *
//	 * @return next character
//	 */
//	private Character getNextSignificantCharacter()
//	{
//		int currentPosition = getTokenEnd();
//		int bufferEnd = getBufferEnd();
//		CharSequence buffer = getBuffer();
//
//		while (currentPosition < bufferEnd)
//		{
//			char currentChar = buffer.charAt(currentPosition);
//			if (currentChar == '#')
//			{
//				while (currentPosition < bufferEnd)
//				{
//					if (buffer.charAt(currentPosition) == '\n')
//						break;
//					currentPosition++;
//				}
//			} else if (!Character.isWhitespace(currentChar))
//				return currentChar;
//
//			currentPosition++;
//		}
//		return null;
//	}

//	@Override
//	public IElementType getHandleTokenType()
//	{
//		String handleName = yytext().toString();
//		if (!knownHandles.contains(handleName))
//			knownHandles.add(handleName);
//		return HANDLE;
//	}
//
//	/**
//	 * Registers 100% sure package
//	 *
//	 * @return token type
//	 */
//	@Override
//	public IElementType getPackageTokenType()
//	{
//		String packageName = PerlPackageUtil.getCanonicalPackageName(yytext().toString());
//		if (!knownPackages.contains(packageName))
//			knownPackages.add(packageName);
//		return PACKAGE;
//	}
//
//	@Override
//	public IElementType getSubTokenType()
//	{
//		String subName = yytext().toString();
//		if (!knownSubs.contains(subName))
//			knownSubs.add(subName);
//		return SUB;
//	}

//	// Ambiguous package detectors
//	public static final HashSet<String> FORCED_PACKAGES = new HashSet<>(Arrays.asList(
//			"SUPER",
//			"main",
//			"CORE"
//	));


//	// tokens which preceeds labels
//	public static final TokenSet preLabelTokenTypes = TokenSet.create(
//			RESERVED_NEXT,
//			RESERVED_LAST,
//			RESERVED_REDO,
//			RESERVED_GOTO
//	);
//
//	// tokens which preceeds 100% package bareword
//	public static final TokenSet prePackageTokenTypes = TokenSet.create(
//			RESERVED_MY,
//			RESERVED_OUR,
//			RESERVED_STATE,
//			RESERVED_LOCAL,
//
//			RESERVED_PACKAGE,
//			RESERVED_USE,
//			RESERVED_NO,
//			RESERVED_REQUIRE
//	);
//
//	// tokens which preceeds filehandle bareword
//	public static final HashSet<IElementType> preHandleTokenTypes = new HashSet<>(Arrays.asList(
//			OPERATOR_FILETEST
//	));
//
//	public static final TokenSet preHandleTokenTypesPrint = TokenSet.create(
//			RESERVED_PRINT,
//			RESERVED_PRINTF,
//			RESERVED_SAY
//	);
//
//	public static final HashSet<Character> printHandleNegativeChars = new HashSet<>(Arrays.asList(
//			'(',
//			'-', // fixme we suppose it's ->
//			','
//	));
//
//	public static final HashSet<Character> preHandleProperSuffix = new HashSet<>(Arrays.asList(
//			')',
//			',',
//			'=', // fixme we suppose it's =>
//			';'
//	));
//

}
