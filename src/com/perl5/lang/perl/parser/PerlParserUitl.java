package com.perl5.lang.perl.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.parser.GeneratedParserUtilBase;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.parser.PerlParser;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.thoughtworks.xstream.mapper.CGLIBMapper;

/**
 * Created by hurricup on 01.05.2015.
 */
public class PerlParserUitl extends GeneratedParserUtilBase implements PerlElementTypes
{
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


	public static boolean parseBarewordPackage(PsiBuilder b, int l ) {

		if(b.getTokenType() == PERL_BAREWORD )
		{
			PsiBuilder.Marker m = b.mark();

			while(b.lookAhead(1) == PERL_DEPACKAGE && b.lookAhead(2) == PERL_BAREWORD)
			{
				b.advanceLexer();
				b.advanceLexer();
			}
			b.advanceLexer();
			m.collapse(PERL_PACKAGE);
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
				m.collapse(PERL_STRING);
			}

			return true;
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

}
