package com.perl5.lang.lexer.elements;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 19.04.2015.
 */
public class PerlGlob extends PerlVariable
{
	public PerlGlob(PerlVariableScope scope, boolean isBuiltIn) {
		super("PERL_GLOB", scope, isBuiltIn);
	}
}
