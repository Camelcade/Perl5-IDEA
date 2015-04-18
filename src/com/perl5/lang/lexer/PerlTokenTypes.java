package com.perl5.lang.lexer;

/**
 * Created by hurricup on 12.04.2015.
 */

import com.intellij.psi.tree.IElementType;

public interface PerlTokenTypes
{
	/* end import from perly.h */
	IElementType GRAMPROG = new PerlElementType("GRAMPROG");
	IElementType GRAMEXPR = new PerlElementType("GRAMEXPR");
	IElementType GRAMBLOCK = new PerlElementType("GRAMBLOCK");
	IElementType GRAMBARESTMT = new PerlElementType("GRAMBARESTMT");
	IElementType GRAMFULLSTMT = new PerlElementType("GRAMFULLSTMT");
	IElementType GRAMSTMTSEQ = new PerlElementType("GRAMSTMTSEQ");
	IElementType WORD = new PerlElementType("WORD");
	IElementType METHOD = new PerlElementType("METHOD");
	IElementType FUNCMETH = new PerlElementType("FUNCMETH");
	IElementType THING = new PerlElementType("THING");
	IElementType PMFUNC = new PerlElementType("PMFUNC");
	IElementType PRIVATEREF = new PerlElementType("PRIVATEREF");
	IElementType QWLIST = new PerlElementType("QWLIST");
	IElementType FUNC0OP = new PerlElementType("FUNC0OP");
	IElementType FUNC0SUB = new PerlElementType("FUNC0SUB");
	IElementType UNIOPSUB = new PerlElementType("UNIOPSUB");
	IElementType LSTOPSUB = new PerlElementType("LSTOPSUB");
	IElementType PLUGEXPR = new PerlElementType("PLUGEXPR");
	IElementType PLUGSTMT = new PerlElementType("PLUGSTMT");
	IElementType LABEL = new PerlElementType("LABEL");
	IElementType FORMAT = new PerlElementType("FORMAT");
	IElementType SUB = new PerlElementType("SUB");
	IElementType ANONSUB = new PerlElementType("ANONSUB");
	IElementType PACKAGE = new PerlElementType("PACKAGE");
	IElementType USE = new PerlElementType("USE");
	IElementType WHILE = new PerlElementType("WHILE");
	IElementType UNTIL = new PerlElementType("UNTIL");
	IElementType IF = new PerlElementType("IF");
	IElementType UNLESS = new PerlElementType("UNLESS");
	IElementType ELSE = new PerlElementType("ELSE");
	IElementType ELSIF = new PerlElementType("ELSIF");
	IElementType CONTINUE = new PerlElementType("CONTINUE");
	IElementType FOR = new PerlElementType("FOR");
	IElementType GIVEN = new PerlElementType("GIVEN");
	IElementType WHEN = new PerlElementType("WHEN");
	IElementType DEFAULT = new PerlElementType("DEFAULT");
	IElementType LOOPEX = new PerlElementType("LOOPEX");
	IElementType DOTDOT = new PerlElementType("DOTDOT");
	IElementType YADAYADA = new PerlElementType("YADAYADA");
	IElementType FUNC0 = new PerlElementType("FUNC0");
	IElementType FUNC1 = new PerlElementType("FUNC1");
	IElementType FUNC = new PerlElementType("FUNC");
	IElementType UNIOP = new PerlElementType("UNIOP");
	IElementType LSTOP = new PerlElementType("LSTOP");
	IElementType RELOP = new PerlElementType("RELOP");
	IElementType EQOP = new PerlElementType("EQOP");
	IElementType MULOP = new PerlElementType("MULOP");
	IElementType ADDOP = new PerlElementType("ADDOP");
	IElementType DOLSHARP = new PerlElementType("DOLSHARP");
	IElementType DO = new PerlElementType("DO");
	IElementType HASHBRACK = new PerlElementType("HASHBRACK");
	IElementType NOAMP = new PerlElementType("NOAMP");
	IElementType LOCAL = new PerlElementType("LOCAL");
	IElementType MY = new PerlElementType("MY");
	IElementType REQUIRE = new PerlElementType("REQUIRE");
	IElementType COLONATTR = new PerlElementType("COLONATTR");
	IElementType FORMLBRACK = new PerlElementType("FORMLBRACK");
	IElementType FORMRBRACK = new PerlElementType("FORMRBRACK");
	IElementType PREC_LOW = new PerlElementType("PREC_LOW");
	IElementType DOROP = new PerlElementType("DOROP");
	IElementType OROP = new PerlElementType("OROP");
	IElementType ANDOP = new PerlElementType("ANDOP");
	IElementType NOTOP = new PerlElementType("NOTOP");
	IElementType ASSIGNOP = new PerlElementType("ASSIGNOP");
	IElementType DORDOR = new PerlElementType("DORDOR");
	IElementType OROR = new PerlElementType("OROR");
	IElementType ANDAND = new PerlElementType("ANDAND");
	IElementType BITOROP = new PerlElementType("BITOROP");
	IElementType BITANDOP = new PerlElementType("BITANDOP");
	IElementType SHIFTOP = new PerlElementType("SHIFTOP");
	IElementType MATCHOP = new PerlElementType("MATCHOP");
	IElementType REFGEN = new PerlElementType("REFGEN");
	IElementType UMINUS = new PerlElementType("UMINUS");
	IElementType POWOP = new PerlElementType("POWOP");
	IElementType POSTJOIN = new PerlElementType("POSTJOIN");
	IElementType POSTDEC = new PerlElementType("POSTDEC");
	IElementType POSTINC = new PerlElementType("POSTINC");
	IElementType PREDEC = new PerlElementType("PREDEC");
	IElementType PREINC = new PerlElementType("PREINC");
	IElementType ARROW = new PerlElementType("ARROW");
	/* end of import from perly.h */


	/* my experiments with Jlex 	*/
	IElementType PERL_BAD_CHARACTER = new PerlElementType("BAD_CHARACTER");
	IElementType PERL_COMMENT = new PerlElementType("COMMENT");

	IElementType PERL_FUNCTION = new PerlElementType("PERL_FUNCTION");
	IElementType PERL_SYNTAX = new PerlElementType("PERL_SYNTAX");
	IElementType PERL_HANDLE = new PerlElementType("PERL_HANDLE");
	IElementType PERL_MISC = new PerlElementType("PERL_MISC");
	IElementType PERL_VARIABLE = new PerlElementType("PERL_VARIABLE");
	IElementType PERL_PACKAGE = new PerlElementType("PERL_PACKAGE");
	IElementType PERL_OPERATOR = new PerlElementType("PERL_OPERATOR");


	IElementType PERL_USER_FUNCTION = new PerlElementType("PERL_USER_FUNCTION");
	IElementType PERL_USER_VARIABLE = new PerlElementType("PERL_USER_VARIABLE");

	IElementType PERL_DEREFERENCE = new PerlElementType("DEREF");
	IElementType PERL_COMMA = new PerlElementType("COMMA");
	IElementType PERL_LBRACKET = new PerlElementType("[");
	IElementType PERL_RBRACKET = new PerlElementType("]");
	IElementType PERL_LSQUARE = new PerlElementType("[");
	IElementType PERL_RSQUARE = new PerlElementType("]");
	IElementType PERL_LCURLY = new PerlElementType("BLOCK");
	IElementType PERL_RCURLY = new PerlElementType("ENDBLOCK");
	IElementType PERL_SEMI = new PerlElementType("SEMI");

	IElementType PERL_NEWLINE = new PerlElementType("new line");

}
