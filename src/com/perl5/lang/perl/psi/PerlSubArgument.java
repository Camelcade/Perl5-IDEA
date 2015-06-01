package com.perl5.lang.perl.psi;

/**
 * Created by hurricup on 01.06.2015.
 */
public class PerlSubArgument
{
	PerlVariableType argumentType;
	String argumentName;
	String variableClass;
	Boolean isOptional;

	public PerlSubArgument(PerlVariableType variableType, String variableName, String variableClass, boolean isOptional)
	{
		this.argumentType = variableType;
		this.argumentName = variableName;
		this.isOptional = isOptional;
		this.variableClass = variableClass;
	}

	public PerlVariableType getArgumentType()
	{
		return argumentType;
	}

	public String getArgumentName()
	{
		return argumentName;
	}

	public Boolean isOptional()
	{
		return isOptional;
	}

	public String getVariableClass()
	{
		return variableClass;
	}

	public String toStringShort()
	{
		return argumentType.getSigil() + argumentName;
	}

}
