package com.perl5.lang.perl.lexer.elements;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 19.04.2015.
 */
public class PerlVariable extends PerlElement implements PerlScopes
{

	private PerlVariableScope variableScope;
	private boolean isBuiltIn;

	public PerlVariable(@NotNull @NonNls String debugName, PerlVariableScope scope, boolean isBuiltIn)
	{
		super(debugName);
		variableScope = scope;
		this.isBuiltIn = isBuiltIn;
	}
}
