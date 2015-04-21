package com.perl5.lang.perl.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.lexer.elements.PerlArray;
import com.perl5.lang.perl.lexer.elements.PerlGlob;
import com.perl5.lang.perl.lexer.elements.PerlHash;
import com.perl5.lang.perl.lexer.elements.PerlScalar;

/**
 * Created by hurricup on 19.04.2015.
 */
public abstract class PerlLexerProto implements FlexLexer, PerlTokenTypes
{
	public abstract CharSequence yytext();

	protected IElementType checkBuiltInScalar()
	{
		return PerlScalar.BUILT_IN.contains(yytext().toString())
				? PERL_BUILTIN_VARIABLE_SCALAR
				: PERL_VARIABLE_SCALAR;
	}
	protected IElementType checkBuiltInArray()
	{
		// here we need to check built-in scalars
		return PerlArray.BUILT_IN.contains(yytext().toString())
				? PERL_BUILTIN_VARIABLE_ARRAY
				: PERL_VARIABLE_ARRAY;
	}
	protected IElementType checkBuiltInHash()
	{
		// here we need to check built-in scalars
		return PerlHash.BUILT_IN.contains(yytext().toString())
				? PERL_BUILTIN_VARIABLE_HASH
				: PERL_VARIABLE_HASH;
	}
	protected IElementType checkBuiltInGlob()
	{
		// here we need to check built-in scalars
		return PerlGlob.BUILT_IN.contains(yytext().toString())
				? PERL_BUILTIN_VARIABLE_GLOB
				: PERL_VARIABLE_GLOB;
	}
	protected IElementType checkCorePackage()
	{
		return PERL_PACKAGE;
	}

}
