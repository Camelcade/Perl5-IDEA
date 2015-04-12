package com.perl5.lang.lexer;

/**
 * Created by hurricup on 12.04.2015.
 */

import com.intellij.psi.tree.IElementType;

public interface PerlTokenTypes
{
	IElementType PERL_BAD_CHARACTER = new PerlElementType("BAD_CHARACTER");
	IElementType PERL_BAD_CHARACTER2 = new PerlElementType("BAD_CHARACTER2");
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
