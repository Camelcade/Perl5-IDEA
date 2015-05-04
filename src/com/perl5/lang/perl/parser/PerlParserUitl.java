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
		boolean r = PerlParser.block(b, l);
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
	 * Making a PERL_PACKAGE item, collapsing barewords with ::
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

	public static boolean parseNoStatement(PsiBuilder b, int l )
	{
		PerlCodeBlockStateChange c = parseUseParameters(b,l);
		if( c == null )
			return false;

		getCurrentBlockState(b).no(c);
		return true;
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
			r = parseBarewordPackage(b,l);
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


	///////////////////////// old thing
/*
	public static boolean parseCallArguments(PsiBuilder b, int l)
	{
		if( b.getTokenType() == PERL_LBRACE )
		{
			PsiBuilder.Marker m = b.mark();
			boolean r = PerlParser.block(b, l);
			if( r )
			{
				IElementType nextTokenType = b.getTokenType(); // @todo actually this depends on signature, check in annotator
				if(
					nextTokenType == PERL_SEMI
					|| nextTokenType == PERL_COMMA
				)
				{
					m.rollbackTo();
				}
				else
				{
					m.drop();
					return true;
				}
			}
			else
			{
				m.rollbackTo();
			}

		}
		return false;
	}

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

	public static boolean parsePackageMethodSuper(PsiBuilder b, int l ) {

		return
			"SUPER".equals(b.getTokenText()) && b.lookAhead(1) == PERL_DEPACKAGE
			&& parsePackageFunctionCall(b,l);
	}

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
*/

}
