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
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.utils.PerlBuilder;
import com.perl5.lang.perl.util.PerlSubUtil;

/**
 * Created by hurricup on 01.05.2015.
 */
public class PerlParserUitl extends GeneratedParserUtilBase implements PerlElementTypes
{
	// tokens that can be converted between each other depending on context
	public static final TokenSet CONVERTABLE_TOKENS = TokenSet.create(
			IDENTIFIER,
			VARIABLE_NAME,
			SUB
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
		if (b.lookAhead(-1) == OPERATOR_DEREFERENCE)
			return false;

		IElementType tokenType = b.getTokenType();
		assert b instanceof PerlBuilder;
		IElementType nextTokenType = b.lookAhead(1);

		if (tokenType == SUB && nextTokenType != LEFT_PAREN && nextTokenType != PACKAGE)
			// todo we should check current namespace here
			return PerlSubUtil.BUILT_IN_UNARY.contains(b.getTokenText());
		else if (tokenType == PACKAGE && nextTokenType == SUB && b.lookAhead(2) != LEFT_PAREN)
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
		if (b.lookAhead(-1) == OPERATOR_DEREFERENCE)
			return false;

		IElementType tokenType = b.getTokenType();
		IElementType nextTokenType = b.lookAhead(1);
		assert b instanceof PerlBuilder;

		if (tokenType == SUB && nextTokenType != LEFT_PAREN && nextTokenType != PACKAGE)
			// todo we should check current namespace here
			return !PerlSubUtil.BUILT_IN_UNARY.contains(b.getTokenText());
		else if (tokenType == PACKAGE && nextTokenType == SUB && b.lookAhead(2) != LEFT_PAREN)
			return !PerlSubUtil.isUnary(b.getTokenText(), ((PerlBuilder) b).lookupToken(1).getTokenText());

		return false;
	}

/*
	*/

	/**
	 * Parses function call
	 *
	 * @param b Perlbuilder
	 * @param l parsing level
	 * @return parsing result
	 *//*

	public static boolean parseSubCall(PsiBuilder b, int l)
	{
		// check if it's list or unary
		// check if it's accepts code as first param
		// check if it's rightward or leftward

		assert b instanceof PerlBuilder;

		IElementType tokenType = b.getTokenType();
		String tokenText = b.getTokenText();

		IElementType prevTokenType = b.lookAhead(-1);
		IElementType nextTokenType = b.lookAhead(1);

		// deref makes sub list, perl can't detect this
		boolean isUnary = false;
		boolean requiresCode = false;

		if( prevTokenType != OPERATOR_DEREFERENCE )
		{
			if( tokenType == PACKAGE && nextTokenType == SUB)
			{
				PerlTokenData nextToken = ((PerlBuilder) b).lookupToken(1);
				// package::sub or package::sub() call
				if( "CORE::".equals(tokenText) )
				{
					isUnary = PerlSubUtil.BUILT_IN_UNARY.contains(nextToken.getTokenText());
					requiresCode = PerlSubUtil.BUILT_IN_CODED.contains(nextToken.getTokenText());
				}

				// todo we should check user subs here

			}
			else if( tokenType == SUB )
			{
				// sub or sub() call
				// todo we should check user subs here, and they depends on current namespace

				if( nextTokenType != PACKAGE)
				{
					isUnary = PerlSubUtil.BUILT_IN_UNARY.contains(tokenText);
					requiresCode = PerlSubUtil.BUILT_IN_CODED.contains(tokenText);
				}
				else
				{
					IElementType nextNextTokenType = b.lookAhead(2);
					if( nextNextTokenType != OPERATOR_DEREFERENCE && nextNextTokenType != SUB)
					{
						// this is a fancy: method package call
						// todo check user subs here
					}
					else
					{
						// this is a sequence, like sub package::method or sub package->method
						isUnary = PerlSubUtil.BUILT_IN_UNARY.contains(tokenText);
						requiresCode = PerlSubUtil.BUILT_IN_CODED.contains(tokenText);
					}
				}
			}
		}
		else if( tokenType == SUB  && b.lookAhead(-2) == PACKAGE)
		{
			// package->sub() or package->sub call
			// todo we should check user subs here
		}

		// parsing invocation
		// todo make a hack for sort sub block
		if( PerlParser.callable(b,l))
		{
			PsiBuilder.Marker m = b.mark();

			if( consumeToken(b, LEFT_PAREN )) // leftward
			{
				if( requiresCode )
				{
					PerlParser.block(b, l + 1);
					consumeTokenSmart(b,OPERATOR_COMMA);
					consumeTokenSmart(b,OPERATOR_COMMA_ARROW);
				}
				PerlParser.expr(b,l+1,-1);

				if( consumeToken(b, RIGHT_PAREN))
				{
					m.done(CALL_ARGUMENTS);
					return true;
				}
				else
				{
					m.drop();
					return false;
				}
			}
			else // rightward
			{
				if( requiresCode )
				{
					if( PerlParser.block(b, l + 1))
					{
						if (!isUnary)
						{
							consumeTokenSmart(b, OPERATOR_COMMA);
							consumeTokenSmart(b, OPERATOR_COMMA_ARROW);
							PerlParser.list_expr(b, l + 1);
						}
					}
				}
				else
				{
					// todo need code heuristic here
					if( isUnary )
						PerlParser.scalar_expr(b, l +1);
					else
						PerlParser.list_expr(b,l+1);
				}
				m.done(CALL_ARGUMENTS);
				return true;
			}
		}

		return false;
	}
*/


	/*
// @todo actually, prototypes and signatures depends on feature in current block; We should do this in parse time
//private sub_declaration_parameters ::=
//    sub_prototype sub_attributes ?
//    | sub_attributes
//
//private sub_definition_parameters ::=
//    sub_attributes ? sub_signature
//    | sub_declaration_parameters
//
//private sub_prototype ::= "(" sub_prototype_element * (";" sub_prototype_element *) ? ")"
//private sub_prototype_element ::=
//        "\\" ( "[" sub_prototype_char + "]" | sub_prototype_char )
//        | sub_prototype_char
//
//private sub_prototype_char ::= "$" | "@" | "+" | "*" | "&"
//
//private sub_attributes ::= 'NYI'
//
//// @todo this requires use feature 'signatures' and no warnings 'experimental:signatures';
//private sub_signature ::= 'NYI'
	* */
	public static boolean parseSubPrototype(PsiBuilder b, int l)
	{
//		boolean isSignatureEnabled  = getCurrentBlockState(b).getFeatures().isSignaturesEnabled();

//		System.out.println("Sub definition parsing, Signatures enabled: "+isSignatureEnabled);

		while (!b.eof() && b.getTokenType() != RIGHT_PAREN)
			consumeToken(b, b.getTokenType());

		return true;
	}

	// @todo this is really raw
	public static boolean parseSubAttributes(PsiBuilder b, int l)
	{
//		boolean isSignatureEnabled  = getCurrentBlockState(b).getFeatures().isSignaturesEnabled();
//		System.out.println("Sub declaration parsing, Signatures enabled: "+isSignatureEnabled);

		while (!b.eof() && b.getTokenType() != LEFT_BRACE)
		{
			PerlBuilder.Marker m = b.mark();
			b.advanceLexer();
			m.collapse(SUB_ATTRIBUTE);
		}

		return true;
	}

	public static boolean parseSubSignature(PsiBuilder b, int l)
	{
//		boolean isSignatureEnabled  = getCurrentBlockState(b).getFeatures().isSignaturesEnabled();
//		System.out.println("Sub declaration parsing, Signatures enabled: "+isSignatureEnabled);
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
		{
			consumeToken(b, SEMICOLON);
			return true;
		} else if (tokenType == RIGHT_BRACE || tokenType == REGEX_QUOTE_CLOSE)
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
			b.remapCurrentToken(tokenType);
			b.advanceLexer();
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
		} else if (currentTokenType == PACKAGE_IDENTIFIER)
		{
			b.remapCurrentToken(PACKAGE);
			b.advanceLexer();
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
		} else if (CONVERTABLE_TOKENS.contains(tokenType))    // single word package
		{
			b.remapCurrentToken(PACKAGE);
			b.advanceLexer();
			return true;
		} else if (
				tokenType == PACKAGE_IDENTIFIER && CONVERTABLE_TOKENS.contains(b.lookAhead(1))
				)
		{
			PsiBuilder.Marker m = b.mark();
			b.advanceLexer();
			b.advanceLexer();
			m.collapse(PACKAGE);
			return true;
		}

		return false;
	}


	/**
	 * Checks current token and convert it if necessary
	 * @param b PerlBuilder
	 * @param l parsing level
	 * @param fromToken possible source token
	 * @param toToken token we want to have
	 * @return parsing result
	 */
	public static boolean checkAndConvertToken(PsiBuilder b, int l, IElementType fromToken, IElementType toToken)
	{
		IElementType tokenType = b.getTokenType();
		if( tokenType == toToken)
		{
			b.advanceLexer();
			return true;
		}
		else if( tokenType == fromToken )
		{
			b.remapCurrentToken(toToken);
			b.advanceLexer();
			return true;
		}
		return false;

	}

	public static boolean sigilCode(PsiBuilder b, int l)
	{
		return checkAndConvertToken(b, l, OPERATOR_BITWISE_AND, SIGIL_CODE);
	}

	public static boolean operatorBitwiseAnd(PsiBuilder b, int l)
	{
		return checkAndConvertToken(b, l, SIGIL_CODE, OPERATOR_BITWISE_AND);
	}

	public static boolean sigilGlob(PsiBuilder b, int l)
	{
		return checkAndConvertToken(b, l, OPERATOR_MUL, SIGIL_GLOB);
	}

	public static boolean operatorMul(PsiBuilder b, int l)
	{
		return checkAndConvertToken(b, l, SIGIL_GLOB, OPERATOR_MUL);
	}

	public static boolean sigilHash(PsiBuilder b, int l)
	{
		return checkAndConvertToken(b, l, OPERATOR_MOD, SIGIL_HASH);
	}

	public static boolean operatorMod(PsiBuilder b, int l)
	{
		return checkAndConvertToken(b, l, SIGIL_HASH, OPERATOR_MOD);
	}


}
