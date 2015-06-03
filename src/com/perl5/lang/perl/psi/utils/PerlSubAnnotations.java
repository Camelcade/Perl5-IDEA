package com.perl5.lang.perl.psi.utils;

import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import com.intellij.util.io.StringRef;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Created by hurricup on 03.06.2015.
 */
public class PerlSubAnnotations
{
	boolean isMethod = false;
	boolean isDeprecated = false;
	boolean isAbstract = false;
	boolean isOverride = false;
	PerlReturnType returnType = PerlReturnType.VALUE;
	String returns = null;

	public static PerlSubAnnotations deserialize(@NotNull StubInputStream dataStream) throws IOException
	{
		return new PerlSubAnnotations(
				dataStream.readBoolean(),
				dataStream.readBoolean(),
				dataStream.readBoolean(),
				dataStream.readBoolean(),
				dataStream.readName(),
				PerlReturnType.valueOf(dataStream.readName().toString())
		);
	}

	public void serialize(@NotNull StubOutputStream dataStream) throws IOException
	{
		dataStream.writeBoolean(isMethod);
		dataStream.writeBoolean(isDeprecated);
		dataStream.writeBoolean(isAbstract);
		dataStream.writeBoolean(isOverride);
		dataStream.writeName(returns);
		dataStream.writeName(returnType.toString());
	}

	public PerlSubAnnotations()
	{
	}

	public PerlSubAnnotations(boolean isMethod, boolean isDeprecated, boolean isAbstract, boolean isOverride, String returns, PerlReturnType returnType)
	{
		this.isMethod = isMethod;
		this.isDeprecated = isDeprecated;
		this.isAbstract = isAbstract;
		this.isOverride = isOverride;
		this.returns = returns;
		this.returnType = returnType;
	}

	public PerlSubAnnotations(boolean isMethod, boolean isDeprecated, boolean isAbstract, boolean isOverride, StringRef returns, PerlReturnType returnType)
	{
		this(isMethod,isDeprecated,isAbstract,isOverride,returns == null ? null: returns.toString(),returnType);
	}

	public boolean isMethod()
	{
		return isMethod;
	}

	public void setIsMethod(boolean isMethod)
	{
		this.isMethod = isMethod;
	}

	public boolean isDeprecated()
	{
		return isDeprecated;
	}

	public void setIsDeprecated(boolean isDeprecated)
	{
		this.isDeprecated = isDeprecated;
	}

	public boolean isAbstract()
	{
		return isAbstract;
	}

	public void setIsAbstract(boolean isAbstract)
	{
		this.isAbstract = isAbstract;
	}

	public boolean isOverride()
	{
		return isOverride;
	}

	public void setIsOverride(boolean isOverride)
	{
		this.isOverride = isOverride;
	}

	public String getReturns()
	{
		return returns;
	}

	public void setReturns(String returns)
	{
		this.returns = returns;
	}

	public PerlReturnType getReturnType()
	{
		return returnType;
	}

	public void setReturnType(PerlReturnType returnType)
	{
		this.returnType = returnType;
	}
}
