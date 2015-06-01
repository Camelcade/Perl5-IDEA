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
import com.perl5.lang.perl.psi.PerlBuilder;

/**
 * Created by hurricup on 01.05.2015.
 */
public class PerlParserUitl extends GeneratedParserUtilBase implements PerlElementTypes
{
	/**
	 * Wrapper for Builder class in order to implement additional per parser information in PerlBuilder
	 * @param root           	root element
	 * @param builder			psibuilder
	 * @param parser			psiparser
	 * @param extendsSets		extends sets
	 * @return					PerlBuilder
	 */
	public static PsiBuilder adapt_builder_(IElementType root, PsiBuilder builder, PsiParser parser, TokenSet[] extendsSets) {
		ErrorState state = new ErrorState();
		ErrorState.initState(state, builder, root, extendsSets);
		return new PerlBuilder(builder, state, parser);
	}

	/**
	 * Smart parser for ->, makes }->[ optional
	 * @param b PerlBuilder
	 * @param l parsing level
	 * @return parsing result
	 */
	public static boolean parseArrowSmart(PsiBuilder b, int l )
	{
		IElementType tokenType = b.getTokenType();
		if( b.getTokenType() == PERL_DEREFERENCE )
		{
			return consumeToken(b, PERL_DEREFERENCE);
		}
		else
		{
			assert b instanceof PerlBuilder;
			PerlTokenData prevToken = ((PerlBuilder) b).lookupToken(-1);
			IElementType prevTokenType = prevToken == null ? null : prevToken.getTokenType();

			// optional }->[ or ]->{
			if(
				( prevTokenType == PERL_RBRACE || prevTokenType == PERL_RBRACK )
				&& ( tokenType == PERL_LBRACE || tokenType == PERL_LBRACK || tokenType == PERL_LPAREN )
					)
				return true;
		}

		return false;
	}

	// flag of regexp supression for regex nested code
	private static boolean regexSuppressed = false;

	/**
	 * Supresses regex opener and parses expression
	 * @param b PerlBuilder
	 * @param l parsing level
	 * @return parsing result
	 */
	public static boolean parsePerlInRegex(PsiBuilder b, int l )
	{
		regexSuppressed = true;
		boolean r = PerlParser.statement(b, l);
		regexSuppressed = false;
		return r;
	}

	/**
	 * Checks if regex opener been suppressed (we are in //e block)
	 * @param b PerlBuilder
	 * @param l parsing level
	 * @return parsing result
	 */
	public static boolean regexNotSuppressed(PsiBuilder b, int l)
	{
		return !regexSuppressed;
	}

	public static boolean parseExpressionLevel(PsiBuilder b, int l, int g )
	{
		return PerlParser.expr(b,l,g);
	}

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

	public static boolean parseSubPrototype(PsiBuilder b, int l )
	{
//		boolean isSignatureEnabled  = getCurrentBlockState(b).getFeatures().isSignaturesEnabled();

//		System.out.println("Sub definition parsing, Signatures enabled: "+isSignatureEnabled);

		while( !b.eof() && b.getTokenType() != PERL_RPAREN )
			consumeToken(b, b.getTokenType());

		return true;
	}

	// @todo this is really raw
	public static boolean parseSubAttributes(PsiBuilder b, int l )
	{
//		boolean isSignatureEnabled  = getCurrentBlockState(b).getFeatures().isSignaturesEnabled();
//		System.out.println("Sub declaration parsing, Signatures enabled: "+isSignatureEnabled);

		while( !b.eof() && b.getTokenType() != PERL_LBRACE )
		{
			PerlBuilder.Marker m = b.mark();
			b.advanceLexer();
			m.collapse(PERL_FUNCTION_ATTRIBUTE);
		}

		return true;
	}

	public static boolean parseSubSignature(PsiBuilder b, int l )
	{
//		boolean isSignatureEnabled  = getCurrentBlockState(b).getFeatures().isSignaturesEnabled();
//		System.out.println("Sub declaration parsing, Signatures enabled: "+isSignatureEnabled);
		return false;
	}

	/**
	 * Smart semi checker decides if we need semi here
	 * @param b Perl builder
	 * @param l Parsing level
	 * @return checking result
	 */
	public static boolean statementSemi(PsiBuilder b, int l)
	{
		IElementType tokenType = b.getTokenType();
		if( tokenType == PERL_SEMI )
		{
			consumeToken(b, PERL_SEMI);
			return true;
		}
		else if( tokenType == PERL_RBRACE ||  tokenType == PERL_RESERVED || tokenType == PERL_REGEX_QUOTE)
			return true;
		else if(b.eof()) // eof
			return true;

		// @todo think what to do here. Currently any statement being finished, even incorrect one
		return false;
	}

	/**
	 * fixme this is a bad solution. Lexser must return appropriate tokens for keywords, this will speed up things
	 * Checks current token type for parsing by names, when we need to check token type and token name
	 * @param b Perl builder
	 * @param l parsing level
	 * @param tokenType tokentype to check
	 * @return parsing results
	 */
	public static boolean checkTokenType(PsiBuilder b, int l, IElementType tokenType)
	{
		return b.getTokenType() == tokenType;
	}
}
