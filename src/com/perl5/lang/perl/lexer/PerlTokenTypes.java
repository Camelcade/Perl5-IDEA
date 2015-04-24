package com.perl5.lang.perl.lexer;

/**
 * Created by hurricup on 12.04.2015.
 */

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.ILazyParseableElementType;
import com.perl5.lang.perl.PerlTokenType;
import com.perl5.lang.perl.lexer.helpers.*;
import com.perl5.lang.pod.PodLanguage;

public interface PerlTokenTypes
{
	/* end import from perly.h */
/*
	IElementType GRAMPROG = new PerlElement("GRAMPROG");
	IElementType GRAMEXPR = new PerlElement("GRAMEXPR");
	IElementType GRAMBLOCK = new PerlElement("GRAMBLOCK");
	IElementType GRAMBARESTMT = new PerlElement("GRAMBARESTMT");
	IElementType GRAMFULLSTMT = new PerlElement("GRAMFULLSTMT");
	IElementType GRAMSTMTSEQ = new PerlElement("GRAMSTMTSEQ");
	IElementType WORD = new PerlElement("WORD");
	IElementType METHOD = new PerlElement("METHOD");
	IElementType FUNCMETH = new PerlElement("FUNCMETH");
	IElementType THING = new PerlElement("THING");
	IElementType PMFUNC = new PerlElement("PMFUNC");
	IElementType PRIVATEREF = new PerlElement("PRIVATEREF");
	IElementType QWLIST = new PerlElement("QWLIST");
	IElementType FUNC0OP = new PerlElement("FUNC0OP");
	IElementType FUNC0SUB = new PerlElement("FUNC0SUB");
	IElementType UNIOPSUB = new PerlElement("UNIOPSUB");
	IElementType LSTOPSUB = new PerlElement("LSTOPSUB");
	IElementType PLUGEXPR = new PerlElement("PLUGEXPR");
	IElementType PLUGSTMT = new PerlElement("PLUGSTMT");
	IElementType LABEL = new PerlElement("LABEL");
	IElementType FORMAT = new PerlElement("FORMAT");
	IElementType SUB = new PerlElement("SUB");
	IElementType ANONSUB = new PerlElement("ANONSUB");
	IElementType PACKAGE = new PerlElement("PACKAGE");
	IElementType USE = new PerlElement("USE");
	IElementType WHILE = new PerlElement("WHILE");
	IElementType UNTIL = new PerlElement("UNTIL");
	IElementType IF = new PerlElement("IF");
	IElementType UNLESS = new PerlElement("UNLESS");
	IElementType ELSE = new PerlElement("ELSE");
	IElementType ELSIF = new PerlElement("ELSIF");
	IElementType CONTINUE = new PerlElement("CONTINUE");
	IElementType FOR = new PerlElement("FOR");
	IElementType GIVEN = new PerlElement("GIVEN");
	IElementType WHEN = new PerlElement("WHEN");
	IElementType DEFAULT = new PerlElement("DEFAULT");
	IElementType LOOPEX = new PerlElement("LOOPEX");
	IElementType DOTDOT = new PerlElement("DOTDOT");
	IElementType YADAYADA = new PerlElement("YADAYADA");
	IElementType FUNC0 = new PerlElement("FUNC0");
	IElementType FUNC1 = new PerlElement("FUNC1");
	IElementType FUNC = new PerlElement("FUNC");
	IElementType UNIOP = new PerlElement("UNIOP");
	IElementType LSTOP = new PerlElement("LSTOP");
	IElementType RELOP = new PerlElement("RELOP");
	IElementType EQOP = new PerlElement("EQOP");
	IElementType MULOP = new PerlElement("MULOP");
	IElementType ADDOP = new PerlElement("ADDOP");
	IElementType DOLSHARP = new PerlElement("DOLSHARP");
	IElementType DO = new PerlElement("DO");
	IElementType HASHBRACK = new PerlElement("HASHBRACK");
	IElementType NOAMP = new PerlElement("NOAMP");
	IElementType LOCAL = new PerlElement("LOCAL");
	IElementType MY = new PerlElement("MY");
	IElementType REQUIRE = new PerlElement("REQUIRE");
	IElementType COLONATTR = new PerlElement("COLONATTR");
	IElementType FORMLBRACK = new PerlElement("FORMLBRACK");
	IElementType FORMRBRACK = new PerlElement("FORMRBRACK");
	IElementType PREC_LOW = new PerlElement("PREC_LOW");
	IElementType DOROP = new PerlElement("DOROP");
	IElementType OROP = new PerlElement("OROP");
	IElementType ANDOP = new PerlElement("ANDOP");
	IElementType NOTOP = new PerlElement("NOTOP");
	IElementType ASSIGNOP = new PerlElement("ASSIGNOP");
	IElementType DORDOR = new PerlElement("DORDOR");
	IElementType OROR = new PerlElement("OROR");
	IElementType ANDAND = new PerlElement("ANDAND");
	IElementType BITOROP = new PerlElement("BITOROP");
	IElementType BITANDOP = new PerlElement("BITANDOP");
	IElementType SHIFTOP = new PerlElement("SHIFTOP");
	IElementType MATCHOP = new PerlElement("MATCHOP");
	IElementType REFGEN = new PerlElement("REFGEN");
	IElementType UMINUS = new PerlElement("UMINUS");
	IElementType POWOP = new PerlElement("POWOP");
	IElementType POSTJOIN = new PerlElement("POSTJOIN");
	IElementType POSTDEC = new PerlElement("POSTDEC");
	IElementType POSTINC = new PerlElement("POSTINC");
	IElementType PREDEC = new PerlElement("PREDEC");
	IElementType PREINC = new PerlElement("PREINC");
	IElementType ARROW = new PerlElement("ARROW");
	*/
/* end of import from perly.h *//*

	/* my experiments with Jlex 	*/
	IElementType PERL_BAD_CHARACTER = new PerlTokenType("BAD_CHARACTER");

	IElementType PERL_COMMENT = new PerlTokenType("PERL_COMMENT");
	IElementType PERL_COMMENT_BLOCK = new PerlTokenType("PERL_COMMENT_BLOCK");

	IElementType PERL_POD = new PerlTokenType("PERL_POD");
//	IElementType PERL_POD = new ILazyParseableElementType("PERL_POD", PodLanguage.INSTANCE);

	IElementType PERL_MULTILINE_MARKER = new PerlTokenType("PERL_MULTILINE_MARKER");
	IElementType PERL_MULTILINE_XML = new PerlTokenType("PERL_MULTILINE_XML");
	IElementType PERL_MULTILINE_HTML = new PerlTokenType("PERL_MULTILINE_HTML");

	IElementType PERL_DQ_STRING = new PerlTokenType("PERL_DQ_STRING");
	IElementType PERL_SQ_STRING = new PerlTokenType("PERL_SQ_STRING");
	IElementType PERL_NUMBER = new PerlTokenType("PERL_NUMBER");

	IElementType PERL_STATIC_METHOD_CALL = new PerlTokenType("PERL_STATIC_METHOD_CALL");
	IElementType PERL_INSTANCE_METHOD_CALL = new PerlTokenType("PERL_INSTANCE_METHOD_CALL");

	IElementType PERL_VARIABLE_SCALAR = new PerlTokenType("PERL_SCALAR");
	IElementType PERL_VARIABLE_SCALAR_BUILT_IN = new PerlTokenType("PERL_SCALAR_BUILT_IN");

	IElementType PERL_VARIABLE_ARRAY = new PerlTokenType("PERL_ARRAY");
	IElementType PERL_VARIABLE_ARRAY_BUILT_IN = new PerlTokenType("PERL_ARRAY_BUILT_IN");

	IElementType PERL_VARIABLE_HASH = new PerlTokenType("PERL_HASH");
	IElementType PERL_VARIABLE_HASH_BUILT_IN = new PerlTokenType("PERL_HASH_BUILT_IN");

	IElementType PERL_VARIABLE_GLOB = new PerlTokenType("PERL_GLOB");
	IElementType PERL_VARIABLE_GLOB_BUILT_IN = new PerlTokenType("PERL_GLOB_BUILT_IN");

	IElementType PERL_FUNCTION = new PerlFunction("PERL_FUNCTION");
	IElementType PERL_FUNCTION_BUILT_IN = new PerlFunction("PERL_FUNCTION_BUILT_IN");

	IElementType PERL_PACKAGE = new PerlTokenType("PERL_PACKAGE");
	IElementType PERL_PACKAGE_BUILT_IN = new PerlTokenType("PERL_PACKAGE_BUILT_IN");
	IElementType PERL_PACKAGE_BUILT_IN_PRAGMA = new PerlTokenType("PERL_PACKAGE_BUILT_IN_PRAGMA");
	IElementType PERL_PACKAGE_BUILT_IN_DEPRECATED = new PerlTokenType("PERL_PACKAGE_BUILT_IN_DEPRECATED");

	IElementType PERL_OPERATOR = new PerlTokenType("PERL_OPERATOR");

	IElementType PERL_DEREFERENCE = new PerlTokenType("DEREF");
	IElementType PERL_DEPACKAGE = new PerlTokenType("DEPACKAGE");

	IElementType PERL_COMMA = new PerlTokenType("COMMA");
	IElementType PERL_LBRACE = new PerlTokenType("LBRACE"); 	// {}
	IElementType PERL_RBRACE = new PerlTokenType("RBRACE");
	IElementType PERL_LBRACK = new PerlTokenType("LBRACK");	// []
	IElementType PERL_RBRACK = new PerlTokenType("RBRACK");
	IElementType PERL_LPAREN = new PerlTokenType("LPAREN");	// ()
	IElementType PERL_RPAREN = new PerlTokenType("RPAREN");
	IElementType PERL_LCURLY = new PerlTokenType("BLOCK");
	IElementType PERL_RCURLY = new PerlTokenType("ENDBLOCK");
	IElementType PERL_SEMI = new PerlTokenType("SEMI");

	IElementType PERL_NEWLINE = new PerlTokenType("NEW_LINE");

}
