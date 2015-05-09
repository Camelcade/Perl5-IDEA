package com.perl5.lang.perl.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.lang.parser.GeneratedParserUtilBase;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.perl.exceptions.PerlParsingException;
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
	 * Parses sub definition, marks error areas
	 * @param b	PerlBuilder
	 * @param l	parsing level
	 * @return	parsing result
	 */
	public static boolean parseSubDefinition(PsiBuilder b, int l)
	{
		assert b instanceof PerlBuilder;

		if( b.getTokenType() == PERL_BAREWORD)
		{
			PsiBuilder.Marker mainMarker = b.mark();

			// sub name
			PsiBuilder.Marker m = b.mark();
			((PerlBuilder) b).beginSubDefinition(b.getTokenText());
			b.advanceLexer();
			m.collapse(PERL_FUNCTION);

			// params
			PerlParser.sub_definition_parameters(b,l);

			PsiBuilder.Marker bodyMarker = b.mark();
			// function body
			if( parseBlock(b, l) )
			{
				try{
					((PerlBuilder) b).commitSubDefinition();
					bodyMarker.drop();
					mainMarker.drop();
				}
				catch(PerlParsingException e)
				{
					mainMarker.errorBefore(e.getMessage(), bodyMarker);
				}

				return true;
			}
			else
			{
				mainMarker.rollbackTo();
				return false;
			}
		}

		return false;
	}

	/**
	 * Parses sub definition, marks error areas
	 * @param b	PerlBuilder
	 * @param l	parsing level
	 * @return	parsing result
	 */
	public static boolean parseSubDeclaration(PsiBuilder b, int l)
	{
		assert b instanceof PerlBuilder;

		if( b.getTokenType() == PERL_BAREWORD)
		{
			PsiBuilder.Marker mainMarker = b.mark();

			// sub name
			PsiBuilder.Marker m = b.mark();
			((PerlBuilder) b).beginSubDeclaration(b.getTokenText());
			b.advanceLexer();
			m.collapse(PERL_FUNCTION);

			// params
			PerlParser.sub_declaration_parameters(b, l);

			if( b.getTokenType() == PERL_SEMI)
			{
				try{
					((PerlBuilder) b).commitSubDeclaration();
					mainMarker.drop();
				}
				catch(PerlParsingException e)
				{
					mainMarker.error(e.getMessage());
				}

				return true;
			}
			else
			{
				mainMarker.rollbackTo();
				return false;
			}
		}

		return false;
	}

	/**
	 * Parsing file entry point. Inits codeblock states, makes code
	 * @param b	PerlBuilder
	 * @param l	parsing level
	 * @return	parsing result
	 */
	public static boolean parseFile(PsiBuilder b, int l)
	{
		assert b instanceof PerlBuilder;

		((PerlBuilder) b).initCodeBlockStateStack(); // push default

		PsiBuilder.Marker m = b.mark();
		boolean r = PerlParser.file_items(b, l);

		if(r)
		{
			m.done(BLOCK);
		}
		else
		{
			m.drop();
		}

		((PerlBuilder) b).popCodeBlockState(b.getTokenText());

		return r;
	}

	/**
	 * Parsing blocks
	 * @param b	PerlBuilder
	 * @param l Parser level
	 * @return	Parsing results
	 */
	public static boolean parseBlock(PsiBuilder b, int l)
	{
		assert b instanceof PerlBuilder;

		((PerlBuilder) b).pushCodeBlockState("Entering block"); // push default
		boolean r = PerlParser.block(b, l + 1);
		((PerlBuilder) b).popCodeBlockState(b.getTokenText());

		return r;
	}


	/**
	 * Parser for package contents with states handling
	 * @param b PerlBuilder
	 * @param l level
	 * @return	result of parsing package_plainp
	 */
	public static boolean parsePackageContents(PsiBuilder b, int l)
	{
		assert b instanceof PerlBuilder;

		((PerlBuilder) b).pushCodeBlockState(b.getTokenText());

		PsiBuilder.Marker m = b.mark();

		boolean r = PerlParser.package_plain(b, l);

		if(r)
		{
			m.done(BLOCK);
		}
		else
		{
			m.drop();
		}

		((PerlBuilder) b).popCodeBlockState(b.getTokenText());

		return r;
	}


	/**
	 * Bareword parser. From context we are trying to decide what is the current and (possibly next) bareword is
	 * @param b	PerlBuilder
	 * @param l	Level
	 * @return	Result
	 */
	public static boolean guessBareword(PsiBuilder b, int l )
	{
		if(b.getTokenType() == PERL_BAREWORD )
		{
			assert b instanceof PerlBuilder;

			IElementType nextRawTokenType = b.rawLookup(1);
			PerlTokenData nextToken = ((PerlBuilder) b).getAheadToken(1);
			PerlTokenData nextNextToken = ((PerlBuilder) b).getAheadToken(2);
			String currentTokenText = b.getTokenText();

			PerlTokenData prevToken = ((PerlBuilder) b).getAheadToken(-1);

			PsiBuilder.Marker m = b.mark();


			// it's a parent package method call like $self->SUPER::method
			if( "SUPER".equals(currentTokenText) )
			{
				if( parsePackageFunctionCall(b,l))
				{
					m.drop();
					return true;
				}

			}
			// bareword bareword
			// method package
			else if (nextToken != null && nextToken.getTokenType() == PERL_BAREWORD && nextRawTokenType == TokenType.WHITE_SPACE)
			{
				// print filehandle
				// say filehandle
				// @todo this should be checked with internal handlers list
				if( "print".equals(currentTokenText) || "say".equals(currentTokenText))
				{
					parseBarewordFunction(b,l);
					parseBarewordHandle(b, l);
					m.drop();
					return true;
				}
				// function1 ...
				else if( ((PerlBuilder) b).isKnownFunction(currentTokenText))
				{
					parseBarewordFunction(b,l);
					m.drop();
					return true;
				}
			}
			// just known function
			else if( ((PerlBuilder) b).isKnownFunction(currentTokenText))
			{
				parseBarewordFunction(b,l);
				m.drop();
				return true;
			}
/*
			if( nextToken != null && nextToken.getTokenType() == PERL_LPAREN )
			{
				b.advanceLexer();
				m.collapse(PERL_FUNCTION);
				return true;

			}
			else if( nextToken != null && nextToken.getTokenType() == PERL_DEPACKAGE )
			{
				*/
/* ok, it's a package all right. Can be:
					package
					package::method
					method package::
					package->
					 package
				 *//*


			}
			// check for built ins
			// method package() invocation
			// we should check if methods are defined if strict is enabled, so we need methods in blockstate
			else if (nextToken != null && nextToken.getTokenType() == PERL_BAREWORD && nextRawTokenType == TokenType.WHITE_SPACE)
			{
				PsiBuilder.Marker markFunction = b.mark();
				b.advanceLexer();
				markFunction.collapse(PERL_FUNCTION);

				boolean r = parseBarewordPackage(b, l);

				if (r)
				{
					m.drop();    // we may collapse/done here to some kind of calee
					return true;
				} else
				{
					m.rollbackTo();
				}
			}
			// pac::age->method	-
			// pack::age::method - how to distinct metod from package part?
			// package->method
			// method->method	- in first occurance, how to distinct method from package?
			else if (
					nextToken != null
					&& nextNextToken != null
					&& "->".equals(nextToken.getTokenText())
					&& nextNextToken.getTokenType() == PERL_BAREWORD
			)
			{
				boolean r = parseBarewordPackage(b, l);

				if (r)
				{
					m.drop();    // we may collapse/done here to some kind of calee
					return true;
				} else
				{
					m.rollbackTo();
				}
			}
*/
			// couldn't guess
			b.advanceLexer();
			m.error("Could'n t guess a bareword");

			return true;
		}

		return false;
	}

	/**
	 * Making a PERL_PACKAGE item, collapsing barewords with :: @see guessBareword for more intelligence method
	 * Sets last parsed package for parsing use/no constructs
	 * @param b PerlBuilder
	 * @param l	level
	 * @return	parsing result
	 */
	public static boolean parseBarewordPackage(PsiBuilder b, int l ) {

		if(b.getTokenType() == PERL_BAREWORD )
		{
			PsiBuilder.Marker m = b.mark();
			StringBuilder packageName = new StringBuilder(b.getTokenText());

			while(b.lookAhead(1) == PERL_DEPACKAGE && b.lookAhead(2) == PERL_BAREWORD)
			{
				b.advanceLexer();
				packageName.append(b.getTokenType());
				b.advanceLexer();
				packageName.append(b.getTokenType());
			}

			assert b instanceof PerlBuilder;
			getPackagesTrap(b).capture(packageName.toString());

			b.advanceLexer();
			m.collapse(PERL_PACKAGE);
			return true;
		}

		return false;
	}

	/**
	 * Function marks current bareword as a function
	 * @param b PerlBuilder
	 * @param l parsing level
	 * @return result
	 */
	public static boolean parseBarewordFunction(PsiBuilder b, int l ) {

		if( b.getTokenType() == PERL_BAREWORD )
		{
			PsiBuilder.Marker m = b.mark();
			b.advanceLexer();
			m.collapse(PERL_FUNCTION);

			return true;
		}

		return false;
	}

	/**
	 * Function marks current bareword as a filehandle
	 * @param b PerlBuilder
	 * @param l parsing level
	 * @return result
	 */
	public static boolean parseBarewordHandle(PsiBuilder b, int l ) {

		if( b.getTokenType() == PERL_BAREWORD )
		{
			PsiBuilder.Marker m = b.mark();
			b.advanceLexer();
			m.collapse(PERL_FILEHANDLE);

			return true;
		}

		return false;
	}

	/**
	 * Function parses construct like package::method
	 * @param b PerlBuilder
	 * @param l parsing level
	 * @return parsing result
	 */
	public static boolean parsePackageFunctionCall(PsiBuilder b, int l ) {

		if(b.getTokenType() == PERL_BAREWORD && b.lookAhead(1) == PERL_DEPACKAGE && b.lookAhead(2) == PERL_BAREWORD )
		{
			PsiBuilder.Marker m = b.mark();

			while(
					b.lookAhead(3) == PERL_DEPACKAGE && b.lookAhead(4) == PERL_BAREWORD
					)
			{
				b.advanceLexer();
				b.advanceLexer();
			}

			b.advanceLexer(); // package rest
			b.advanceLexer(); // depackage

			m.collapse(PERL_PACKAGE);

			m = b.mark();
			b.advanceLexer();
			m.collapse(PERL_FUNCTION);

			return true;
		}

		return false;
	}


	/**
	 * Trying to parse:  bare => 'smth' construction
	 * @param b	PerlBuilder
	 * @param l level
	 * @return	result
	 */
	public static boolean parseBarewordString(PsiBuilder b, int l ) {
		// here is the logic when we allows to use barewords as strings
		IElementType tokenType = b.getTokenType();
		if(
			tokenType == PERL_BAREWORD && b.lookAhead(1) == PERL_ARROW_COMMA // BARE =>
			|| tokenType == PERL_STRING_CONTENT
		)
		{
			assert b instanceof PerlBuilder;

			getStringsTrap(b).capture(b.getTokenText());

			PsiBuilder.Marker m = b.mark();
			b.advanceLexer();
			m.collapse(PERL_STRING);

			return true;
		}

		return false;
	}

	public static boolean parseUseStatement(PsiBuilder b, int l )
	{
		PerlCodeBlockStateChange c = parseUseParameters(b,l);
		if( c == null )
			return false;

		getCurrentBlockState(b).use(c);
		return true;
	}

	public static boolean parseRightCallArguments(PsiBuilder b, int l )
	{
		PerlParser.expr(b,l,3); // optional. Standart generator makes it with -1 level
		return true;
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
		boolean isSignatureEnabled  = getCurrentBlockState(b).isSignaturesEnabled();
//		System.out.println("Sub definition parsing, Signatures enabled: "+isSignatureEnabled);
		return false;
	}


	public static boolean parseSubAttributes(PsiBuilder b, int l )
	{
		boolean isSignatureEnabled  = getCurrentBlockState(b).isSignaturesEnabled();
//		System.out.println("Sub declaration parsing, Signatures enabled: "+isSignatureEnabled);
		return false;
	}

	public static boolean parseSubSignature(PsiBuilder b, int l )
	{
		boolean isSignatureEnabled  = getCurrentBlockState(b).isSignaturesEnabled();
//		System.out.println("Sub declaration parsing, Signatures enabled: "+isSignatureEnabled);
		return false;
	}

	public static boolean parseNoStatement(PsiBuilder b, int l )
	{
		PerlCodeBlockStateChange c = parseUseParameters(b,l);
		if( c == null )
			return false;

		getCurrentBlockState(b).no(c);
		return true;
	}

	public static boolean statementSemi(PsiBuilder b, int l)
	{
		IElementType tokenType = b.getTokenType();
		if( tokenType == PERL_SEMI )
		{
			consumeToken(b, PERL_SEMI);
			return true;
		}
		else if( tokenType == PERL_RBRACE)
			return true;
		return false;
	}

	protected static PerlCodeBlockStateChange parseUseParameters(PsiBuilder b, int l)
	{
		assert b instanceof PerlBuilder;

		PsiBuilder.Marker m = b.mark();
		PerlCodeBlockStateChange c = null;

		PerlSyntaxTrap vt = getVersionsTrap(b);
		vt.start();
		boolean r = parseVersion(b,l);
		vt.stop();

		if( r ) // use VERSION
		{
			if( b.getTokenType() == PERL_SEMI )
			{
				c = new PerlCodeBlockStateChange();
				c.perlVersion = vt.getFistCapture();
				m.drop();
			}
			else
				m.rollbackTo();
		}
		else
		{
			PerlSyntaxTrap t = getPackagesTrap(b);
			t.start();
			r = parseBarewordPackage(b, l);
			t.stop();

			if( r ) // use MODULE
			{
				c = new PerlCodeBlockStateChange();
				c.packageName = t.getFistCapture();

				vt.start();
				r = parseVersion(b,l);
				vt.stop();

				if( r ) // use MODULE VERSION
				{
					c.packageVersion = vt.getFistCapture();
				}

				t = getStringsTrap(b);
				t.start();
				r = PerlParser.expr(b,l,-1);
				t.stop();

				if( r ) // use MODULE VERSION ? LIST
				{
					c.packageParams = t.getCaptures();
				}

				if( b.getTokenType() == PERL_SEMI )
					m.drop();
				else
				{
					c = null;
					m.rollbackTo();
				}
			}
			else
			{
				m.rollbackTo();
			}
		}

		return c;
	}


	/**
	 * Trying to parse:  version and replace token type
	 * @param b	PerlBuilder
	 * @param l level
	 * @return	result
	 */
	public static boolean parseVersion(PsiBuilder b, int l ) {
		// here is the logic when we allows to use barewords as strings
		IElementType tokenType = b.getTokenType();

		if(tokenType == PERL_NUMBER_VERSION || tokenType == PERL_NUMBER)
		{
			assert b instanceof PerlBuilder;
			getVersionsTrap(b).capture(b.getTokenText());
			PsiBuilder.Marker m = b.mark();
			b.advanceLexer();
			m.collapse(PERL_VERSION);

			return true;
		}

		return false;
	}

	public static boolean captureStrings(PsiBuilder b, int l, boolean state ) {
		assert b instanceof PerlBuilder;
		PerlSyntaxTrap t = getStringsTrap(b);
		if( state )
			t.start();
		else
			t.stop();
		return true;
	}

	public static boolean capturePackages(PsiBuilder b, int l, boolean state ) {
		assert b instanceof PerlBuilder;
		PerlSyntaxTrap t = getPackagesTrap(b);
		if( state )
			t.start();
		else
			t.stop();
		return true;
	}

	public static boolean captureVersions(PsiBuilder b, int l, boolean state ) {
		assert b instanceof PerlBuilder;
		PerlSyntaxTrap t = getVersionsTrap(b);
		if( state )
			t.start();
		else
			t.stop();
		return true;
	}

	protected static PerlCodeBlockState getCurrentBlockState(PsiBuilder b)
	{
		assert b instanceof PerlBuilder;
		return ((PerlBuilder) b).getCurrentBlockState();
	}

	protected static PerlSyntaxTrap getVersionsTrap(PsiBuilder b)
	{
		return getCurrentBlockState(b).getVersionsTrap();
	}

	protected static PerlSyntaxTrap getPackagesTrap(PsiBuilder b)
	{
		return getCurrentBlockState(b).getPackagesTrap();
	}

	protected static PerlSyntaxTrap getStringsTrap(PsiBuilder b)
	{
		return getCurrentBlockState(b).getStringsTrap();
	}


}
