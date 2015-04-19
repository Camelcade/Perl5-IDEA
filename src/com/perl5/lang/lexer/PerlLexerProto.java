package com.perl5.lang.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

/**
 * Created by hurricup on 19.04.2015.
 */
public abstract class PerlLexerProto implements FlexLexer, PerlTokenTypes, PerlBuiltIns
{

/*
  StringBuffer stringBuffer = new StringBuffer();
*/
 /* private IElementType element(int type, Object value) {
    return new IElementType(type, value);
  }*/

	public abstract CharSequence yytext();

	protected IElementType checkBuiltInScalar()
	{
		return PERL_BUILT_IN_SCALARS.contains(yytext().toString())
				? PERL_BUILTIN_VARIABLE_SCALAR
				: PERL_VARIABLE_SCALAR;
	}
	protected IElementType checkBuiltInArray()
	{
		// here we need to check built-in scalars
		return PERL_BUILT_IN_ARRAYS.contains(yytext().toString())
				? PERL_BUILTIN_VARIABLE_ARRAY
				: PERL_VARIABLE_ARRAY;
	}
	protected IElementType checkBuiltInHash()
	{
		// here we need to check built-in scalars
		return PERL_BUILT_IN_HASHES.contains(yytext().toString())
				? PERL_BUILTIN_VARIABLE_HASH
				: PERL_VARIABLE_HASH;
	}
	protected IElementType checkBuiltInGlob()
	{
		// here we need to check built-in scalars
		return PERL_BUILT_IN_GLOBS.contains(yytext().toString())
				? PERL_BUILTIN_VARIABLE_GLOB
				: PERL_VARIABLE_GLOB;
	}
	protected IElementType checkCorePackage()
	{
		return PERL_PACKAGE;
	}
}
