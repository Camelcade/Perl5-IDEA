package com.perl5.lang.perl.psi.utils;

import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Created by hurricup on 03.06.2015.
 */
public class PerlSubAnnotation
{
	boolean isMethod = false;
	boolean isDeprecated = false;
	boolean isAbstract = false;
	boolean isOverride = false;
	String returns = null;

	public static PerlSubAnnotation deserialize(@NotNull StubInputStream dataStream) throws IOException
	{
		return new PerlSubAnnotation(
				dataStream.readBoolean(),
				dataStream.readBoolean(),
				dataStream.readBoolean(),
				dataStream.readBoolean(),
				dataStream.readName().toString()
		);
	}

	public void serialize(@NotNull StubOutputStream dataStream) throws IOException
	{
		dataStream.writeBoolean(isMethod);
		dataStream.writeBoolean(isDeprecated);
		dataStream.writeBoolean(isAbstract);
		dataStream.writeBoolean(isOverride);
		dataStream.writeName(returns);
	}

	public PerlSubAnnotation(boolean isMethod, boolean isDeprecated, boolean isAbstract, boolean isOverride, String returns)
	{
		this.isMethod = isMethod;
		this.isDeprecated = isDeprecated;
		this.isAbstract = isAbstract;
		this.isOverride = isOverride;
		this.returns = returns;
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
}
