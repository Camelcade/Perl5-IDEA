package com.perl5.lang.parser.parsing.auxiliary;

import com.perl5.lang.parser.PerlElementTypes;
import com.intellij.lang.PsiBuilder;
import com.perl5.lang.parser.parsing.util.ParserUtils;

/**
 * Created by hurricup on 12.04.2015.
 */

public class Separators implements PerlElementTypes
{
	public static boolean parse(PsiBuilder builder) {
		if (PERL_SEMI.equals(builder.getTokenType()) || PERL_NEWLINE.equals(builder.getTokenType())) { // check for semicolumn and newlilne
			builder.advanceLexer();
			while (ParserUtils.getToken(builder, PERL_NEWLINE) || ParserUtils.getToken(builder, PERL_SEMI)) {
				// Parse newLines
			}
			return true;
		}

		return false;
	}
}
