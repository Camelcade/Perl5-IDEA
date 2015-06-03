package com.perl5.lang.perl.psi.utils;

import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

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

	public static PerlSubArgument deserialize(@NotNull StubInputStream dataStream) throws IOException
	{
		PerlVariableType argumentType = PerlVariableType.valueOf(dataStream.readName().toString());
		String argumentName = dataStream.readName().toString();
		String variableClass = dataStream.readName().toString();
		boolean isOptional = dataStream.readBoolean();
		return new PerlSubArgument(argumentType,argumentName,variableClass,isOptional);
	}

	public void serialize(@NotNull StubOutputStream dataStream) throws IOException
	{
		dataStream.writeName(argumentType.toString());
		dataStream.writeName(argumentName);
		dataStream.writeName(variableClass);
		dataStream.writeBoolean(isOptional);
	}

}
