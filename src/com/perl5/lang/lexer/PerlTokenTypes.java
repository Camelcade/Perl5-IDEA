package com.perl5.lang.lexer;

/**
 * Created by hurricup on 12.04.2015.
 */

import com.intellij.psi.tree.IElementType;
import com.perl5.lang.lexer.elements.*;

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
	IElementType PERL_BAD_CHARACTER = new PerlElement("BAD_CHARACTER");
	IElementType PERL_COMMENT = new PerlComment();
	IElementType PERL_COMMENT_MULTILINE = new PerlCommentMultiline();
	IElementType PERL_DQ_STRING = new PerlElement("DQ_STRING");
	IElementType PERL_SQ_STRING = new PerlElement("SQ_STRING");
	IElementType PERL_NUMBER = new PerlNumber();

	IElementType PERL_STATIC_METHOD_CALL = new PerlElement("PERL_STATIC_METHOD_CALL");
	IElementType PERL_INSTANCE_METHOD_CALL = new PerlElement("PERL_INSTANCE_METHOD_CALL");
	IElementType PERL_FUNCTION = new PerlElement("PERL_FUNCTION");
	IElementType PERL_SYNTAX = new PerlElement("PERL_SYNTAX");
	IElementType PERL_HANDLE = new PerlElement("PERL_HANDLE");
	IElementType PERL_MISC = new PerlElement("PERL_MISC");

	IElementType PERL_VARIABLE_SCALAR = new PerlScalar(PerlScopes.PerlVariableScope.MY, false);
	IElementType PERL_VARIABLE_ARRAY = new PerlArray(PerlScopes.PerlVariableScope.MY, false);
	IElementType PERL_VARIABLE_HASH = new PerlHash(PerlScopes.PerlVariableScope.MY, false);
	IElementType PERL_VARIABLE_GLOB = new PerlGlob(PerlScopes.PerlVariableScope.MY, false);

	IElementType PERL_BUILTIN_VARIABLE_SCALAR = new PerlScalar(PerlScopes.PerlVariableScope.MY, true);
	IElementType PERL_BUILTIN_VARIABLE_ARRAY = new PerlArray(PerlScopes.PerlVariableScope.MY, true);
	IElementType PERL_BUILTIN_VARIABLE_HASH = new PerlHash(PerlScopes.PerlVariableScope.MY, true);
	IElementType PERL_BUILTIN_VARIABLE_GLOB = new PerlGlob(PerlScopes.PerlVariableScope.MY, true);

	IElementType PERL_PACKAGE = new PerlElement("PERL_PACKAGE");
	IElementType PERL_OPERATOR = new PerlFunctionOperator();

	IElementType PERL_USER_FUNCTION = new PerlElement("PERL_USER_FUNCTION");

	IElementType PERL_DEREFERENCE = new PerlElement("DEREF");
	IElementType PERL_DEPACKAGE = new PerlElement("DEPACKAGE");

	IElementType PERL_COMMA = new PerlElement("COMMA");
	IElementType PERL_LBRACE = new PerlElement("LBRACE"); 	// {}
	IElementType PERL_RBRACE = new PerlElement("RBRACE");
	IElementType PERL_LBRACK = new PerlElement("LBRACK");	// []
	IElementType PERL_RBRACK = new PerlElement("RBRACK");
	IElementType PERL_LPAREN = new PerlElement("LPAREN");	// ()
	IElementType PERL_RPAREN = new PerlElement("RPAREN");
	IElementType PERL_LCURLY = new PerlElement("BLOCK");
	IElementType PERL_RCURLY = new PerlElement("ENDBLOCK");
	IElementType PERL_SEMI = new PerlElement("SEMI");

	IElementType PERL_NEWLINE = new PerlElement("new line");

}
