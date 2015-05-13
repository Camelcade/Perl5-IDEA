package com.perl5.lang.perl.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.lang.parser.GeneratedParserUtilBase;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.perl.exceptions.PerlParsingException;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.PerlBuilder;
import com.perl5.lang.perl.util.PerlArrayUtil;
import com.perl5.lang.perl.util.PerlHashUtil;
import com.perl5.lang.perl.util.PerlScalarUtil;

import java.util.ArrayList;

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
	 * Parsing rightward call parameters, using the prototype
	 * @param b PerlBuilder
	 * @param l parsing level
	 * @return parsing result
	 */
	public static boolean parseRightwardCallParameters(PsiBuilder b, int l)
	{
		if( b.getTokenType() != PERL_LPAREN)
		{
			parseCallParameters(b,l);
			return true;
		}
		return false;
	}

	private static boolean parseCallParameters(PsiBuilder b, int l)
	{
		assert b instanceof PerlBuilder;

 		String methodName = ((PerlBuilder) b).getLastCallableMethod();
		String packageName = ((PerlBuilder) b).getLastCallablePackage();

		if( methodName == null)
			// can't happen
			return false;
//			throw new Error("No method captured, smth is wrong");
		else if(packageName == null) // method from unknown package
		{
			parseExpressionLevel(b,l,2);
		}
		else // method and package are known
		{
			parseExpressionLevel(b,l,2);
		}
		return true;
	}

	/**
	 * Resets last callable in biulder for later parsing
	 * @param b PerlBuilder
	 * @param l parsing level
	 * @return always true
	 */
	public static boolean resetLastCallable(PsiBuilder b, int l)
	{
		assert b instanceof PerlBuilder;
		((PerlBuilder) b).setLastCallableMethod(null);
		((PerlBuilder) b).setLastCallablePackage(null);
		return true;
	}

	/**
	 * Parsing file entry point. Inits codeblock states, default namespace
	 * @param b	PerlBuilder
	 * @param l	parsing level
	 * @return	parsing result
	 */
	public static boolean parseFile(PsiBuilder b, int l)
	{
		assert b instanceof PerlBuilder;

		((PerlBuilder) b).initCodeBlockStateStack(); // init states stack
		getCurrentBlockState(b).setNamespace(((PerlBuilder) b).getNamespace("main")); // switch to main namespace

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
	 * Parses perl package definition, creates namespace in builder, procesing content in scope
	 * @param b	PerlBuilder
	 * @param l parsing level
	 * @return parsing result
	 */

	public static boolean parsePerlPackage(PsiBuilder b, int l)
	{
		if( b.getTokenType() == PERL_BAREWORD )
		{
			if( parseBarewordPackage(b,l))
			{
				assert b instanceof PerlBuilder;

				// package name
				PerlPackage namespace = ((PerlBuilder) b).getNamespace(((PerlBuilder) b).getLastParsedPackage());

				// package version
				if( parseVersion(b,l))
				{
					namespace.setVersion(new PerlVersion(((PerlBuilder) b).getLastParsedVersion()));
				}

				// set namespace for following code
				((PerlBuilder) b).pushCodeBlockState(b.getTokenText());
				getCurrentBlockState(b).setNamespace(namespace);

				boolean r;

				// package content
				if( b.getTokenType() == PERL_SEMI )
				{
					consumeToken(b, PERL_SEMI);
					PsiBuilder.Marker m = b.mark();
					r = PerlParser.namespace_content(b, l);
					if( r )
						m.done(BLOCK);
					else
						m.rollbackTo();

				}
				else
					r = PerlParser.block(b,l);

				// @todo we should read vars from here
				// @todo if something bad happened, we must be able to rollback; so we need to be able to merge namespaces
				((PerlBuilder) b).popCodeBlockState(b.getTokenText());
				return r;
			}
		}

		return false;
	}



	/**
	 * Bareword parser. From context we are trying to decide what is the current and (possibly next) bareword is
	 * @param b	PerlBuilder
	 * @param l	Level
	 * @return	Result
	 */
	public static boolean guessBarewordCallable(PsiBuilder b, int l )
	{
		assert b instanceof PerlBuilder;

		// it seems ::method call from main
		if( b.getTokenType() == PERL_DEPACKAGE && isBareword(b.lookAhead(1)))
		{
			PsiBuilder.Marker m = b.mark();
			b.advanceLexer();
			m.done(PERL_PACKAGE);
			return parseBarewordFunction(b,l);
		}

		PerlTokenData prevToken = ((PerlBuilder) b).getAheadToken(-1);
		IElementType prevTokenType = prevToken == null ? null : prevToken.getTokenType();

		IElementType nextTokenType = b.lookAhead(1);
		IElementType tokenType = b.getTokenType();

		// ->method
		if( prevTokenType == PERL_DEREFERENCE && isBareword(tokenType) && parseBarewordFunction(b,l))
		{
			return true;
		}
		else if( tokenType == PERL_BAREWORD )
		{
			if (nextTokenType == PERL_ARROW_COMMA) // string
				return false;
			else  if( "SUPER".equals(b.getTokenText()) && parseBarewordPackageFunctionCall(b,l) )
				return true;
			else if(nextTokenType == PERL_BAREWORD && b.lookAhead(2) == PERL_DEPACKAGE)
				return parseBarewordFunction(b,l) && parseBarewordPackage(b,l);
			else if(nextTokenType == PERL_DEPACKAGE )
			{
				PsiBuilder.Marker m = b.mark();

				if( parseBarewordPackage(b,l) && b.getTokenType() == PERL_DEREFERENCE )
				{
					m.drop();
					return true;
				}

				m.rollbackTo();
				return parseBarewordPackageFunctionCall(b,l);

			}
			else
				return parseBarewordFunction(b,l);
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
				packageName.append(b.getTokenText());
				b.advanceLexer();
				packageName.append(b.getTokenText());
			}

			assert b instanceof PerlBuilder;
			((PerlBuilder) b).setLastParsedPackage(packageName.toString());
			((PerlBuilder) b).setLastCallablePackage(packageName.toString());

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

		if( isBareword(b.getTokenType()) )
		{
			assert b instanceof PerlBuilder;
			((PerlBuilder) b).setLastCallableMethod(b.getTokenText());
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
			assert b instanceof PerlBuilder;

			String handle = b.getTokenText();
			((PerlBuilder) b).setLastParsedHandle(handle);

			PsiBuilder.Marker m = b.mark();
			b.advanceLexer();

			m.collapse(PERL_HANDLE);

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
	public static boolean parseBarewordPackageFunctionCall(PsiBuilder b, int l) {

		assert b instanceof PerlBuilder;
		if(isBareword(b.getTokenType()) && b.lookAhead(1) == PERL_DEPACKAGE && isBareword(b.lookAhead(2)))
		{
			PsiBuilder.Marker m = b.mark();
			StringBuilder packageName = new StringBuilder("");

			while(
					b.lookAhead(3) == PERL_DEPACKAGE && isBareword(b.lookAhead(4))
					)
			{
				packageName.append(b.getTokenText());
				b.advanceLexer();
				packageName.append(b.getTokenText());
				b.advanceLexer();
			}

			packageName.append(b.getTokenText());

			b.advanceLexer(); // package rest
			b.advanceLexer(); // depackage

			m.collapse(PERL_PACKAGE);

			((PerlBuilder) b).setLastCallablePackage(packageName.toString());
			((PerlBuilder) b).setLastCallableMethod(b.getTokenText());

			m = b.mark();
			b.advanceLexer();
			m.collapse(PERL_FUNCTION);

			return true;
		}

		return false;
	}


	/**
	 * Parsing string content
	 * @param b	PerlBuilder
	 * @param l level
	 * @return	result
	 */
	public static boolean parseBarewordString(PsiBuilder b, int l ) {
		// here is the logic when we allows to use barewords as strings
		IElementType tokenType = b.getTokenType();
		assert b instanceof PerlBuilder;

		if(	tokenType == PERL_STRING_CONTENT
			|| tokenType == PERL_BAREWORD && b.lookAhead(1) == PERL_ARROW_COMMA
				) //
		{
			getStringsTrap(b).capture(b.getTokenText());

			PsiBuilder.Marker m = b.mark();
			b.advanceLexer();
			m.collapse(PERL_STRING);

			return true;
		}

		return false;
	}

	/**
	 * This function parses situations like -bareword =>
	 * @param b
	 * @param l
	 * @return
	 */
	public static boolean parseBarewordStringMinus(PsiBuilder b, int l ) {
		IElementType tokenType = b.getTokenType();
		assert b instanceof PerlBuilder;

		if( "-".equals(b.getTokenText()) && b.lookAhead(1) == PERL_BAREWORD )
		{
			PsiBuilder.Marker m = b.mark();
			b.advanceLexer();
			getStringsTrap(b).capture("-" + b.getTokenText());
			b.advanceLexer();
			m.collapse(PERL_STRING);
		}

		return false;
	}

	/**
	 * Smart parser for ->, makes }->[ optional
	 * @param b
	 * @param l
	 * @return
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
			PerlTokenData prevToken = ((PerlBuilder) b).getAheadToken(-1);
			IElementType prevTokenType = prevToken == null ? null : prevToken.getTokenType();

			// optional }->[ or ]->{
			if(
				( prevTokenType == PERL_RBRACE || prevTokenType == PERL_RBRACK )
				&& ( tokenType == PERL_LBRACE || tokenType == PERL_LBRACK )
					)
				return true;
		}

		return false;
	}


	public static boolean parseUseStatement(PsiBuilder b, int l )
	{
		assert b instanceof PerlBuilder;
		PsiBuilder.Marker m = b.mark();

		PerlUseParameters parameters = parseUseParameters(b, l);
		if (parameters == null)
		{
			m.rollbackTo();
			return false;
		}

		PerlCodeBlockState state = getCurrentBlockState(b);
		String packageName = parameters.getPackageName();

		if( packageName != null )
		{
			if( state.isPragma(packageName))
				state.use(parameters);
			else	// regular package load and import
			{
				parameters.setPackageFile(((PerlBuilder) b).getPackageFile(packageName));
				state.getNamespace().importPackage(parameters);
			}
		}
		// @todo find out what to do with use version

		m.drop();
		return true;
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
		boolean isSignatureEnabled  = getCurrentBlockState(b).getFeatures().isSignaturesEnabled();

//		System.out.println("Sub definition parsing, Signatures enabled: "+isSignatureEnabled);

		while( !b.eof() && b.getTokenType() != PERL_RPAREN )
			consumeToken(b, b.getTokenType());

		return true;
	}


	public static boolean parseSubAttributes(PsiBuilder b, int l )
	{
//		boolean isSignatureEnabled  = getCurrentBlockState(b).getFeatures().isSignaturesEnabled();
//		System.out.println("Sub declaration parsing, Signatures enabled: "+isSignatureEnabled);
		return false;
	}

	public static boolean parseSubSignature(PsiBuilder b, int l )
	{
//		boolean isSignatureEnabled  = getCurrentBlockState(b).getFeatures().isSignaturesEnabled();
//		System.out.println("Sub declaration parsing, Signatures enabled: "+isSignatureEnabled);
		return false;
	}

	public static boolean parseNoStatement(PsiBuilder b, int l )
	{
		PerlUseParameters c = parseUseParameters(b,l);
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

		// @todo think what to do here. Currently any statement being finished, even incorrect one
		return false;
	}

	protected static PerlUseParameters parseUseParameters(PsiBuilder b, int l)
	{
		assert b instanceof PerlBuilder;

		PsiBuilder.Marker m = b.mark();
		PerlUseParameters c = null;

		boolean r = parseVersion(b,l);

		if( r ) // use VERSION
		{
			if( b.getTokenType() == PERL_SEMI )
			{
				c = new PerlUseParameters();
				c.setPackageVersion(((PerlBuilder) b).getLastParsedVersion());
				m.drop();
			}
			else
				m.rollbackTo();
		}
		else
		{
			r = parseBarewordPackage(b, l);

			if( r ) // use MODULE
			{
				c = new PerlUseParameters();
				c.setPackageName(((PerlBuilder) b).getLastParsedPackage());

				r = parseVersion(b,l);

				if( r ) // use MODULE VERSION
				{
					c.setPackageVersion(((PerlBuilder) b).getLastParsedVersion());
				}

				PerlSyntaxTrap t = getStringsTrap(b);
				t.start();
				r = PerlParser.expr(b,l,-1);
				t.stop();

				if( r ) // use MODULE VERSION ? LIST
				{
					c.setPackageParams(t.getCaptures());
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
			((PerlBuilder) b).setLastParsedVersion(b.getTokenText());
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

	protected static PerlCodeBlockState getCurrentBlockState(PsiBuilder b)
	{
		assert b instanceof PerlBuilder;
		return ((PerlBuilder) b).getCurrentBlockState();
	}

	protected static boolean isKnownFunction(PsiBuilder b, String name)
	{
		assert b instanceof PerlBuilder;
		return getCurrentBlockState(b).getNamespace().isKnownFunction(name);
	}

	protected static boolean isKnownHandle(PsiBuilder b, String name)
	{
		assert b instanceof PerlBuilder;
		return ((PerlBuilder) b).isKnownHandle(name);
	}

	protected static PerlSyntaxTrap getStringsTrap(PsiBuilder b)
	{
		assert b instanceof PerlBuilder;
		return getCurrentBlockState(b).getStringsTrap();
	}

	public static boolean checkBuiltInHash(PsiBuilder b, int l)
	{
		return b.getTokenType() == PERL_SIGIL_HASH && checkTextByList(b, PerlHashUtil.BUILT_IN);
	}

	public static boolean checkBuiltInArray(PsiBuilder b, int l)
	{
		return b.getTokenType() == PERL_SIGIL_ARRAY && checkTextByList(b, PerlArrayUtil.BUILT_IN);
	}

	public static boolean checkBuiltInScalar(PsiBuilder b, int l)
	{
		return b.getTokenType() == PERL_SIGIL_SCALAR && checkTextByList(b, PerlScalarUtil.BUILT_IN);
	}

	public static boolean checkTextByList(PsiBuilder b, ArrayList<String> list)
	{
		for(String item: list)
			if( consumeToken(b,item))
				return true;

		return false;
	}

	public static boolean isBareword(IElementType tokenType)
	{
		return tokenType == PERL_BAREWORD || tokenType == PERL_KEYWORD || tokenType == PERL_OPERATOR_UNARY;
	}

}
