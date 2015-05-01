package com.perl5.lang.perl.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.parser.GeneratedParserUtilBase;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.thoughtworks.xstream.mapper.CGLIBMapper;

/**
 * Created by hurricup on 01.05.2015.
 */
public class PerlParserUitl extends GeneratedParserUtilBase implements PerlElementTypes
{
	public static boolean collapsedPackageToken(PsiBuilder b, int l ) {

		if(b.getTokenType() == PERL_BAREWORD )
		{
			PsiBuilder.Marker m = b.mark();

			while(b.lookAhead(1) == PERL_DEPACKAGE && b.lookAhead(2) == PERL_BAREWORD)
			{
				b.advanceLexer();
				b.advanceLexer();
			}
			b.advanceLexer();
			m.collapse(PERL_PACKAGE_BARE);
			return true;
		}

		return false;
	}

	public static boolean parseBarewordString(PsiBuilder b, int l ) {
		// here is the logic when we allows to use barewards as strings
		if(b.getTokenType() == PERL_BAREWORD )
		{
			if(
				b.lookAhead(1) == PERL_ARROW_COMMA // BARE =>
			)
			{
				PsiBuilder.Marker m = b.mark();
				b.advanceLexer();
				m.done(PERL_STRING);
			}

			return true;
		}

		return false;
	}

	public static boolean parseBarewordFunction(PsiBuilder b, int l ) {
		IElementType tokenType = b.getTokenType();

		if( tokenType == PERL_BAREWORD )
		{
			PsiBuilder.Marker m = b.mark();
			b.advanceLexer();
			m.done(PERL_FUNCTION);

			return true;
		}

		return false;
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

			m.collapse(PERL_PACKAGE_BARE);

			m = b.mark();
			b.advanceLexer();
			m.collapse(PERL_FUNCTION);

			return true;
		}

		return false;
	}

}
