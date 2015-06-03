package com.perl5.lang.perl.psi.utils;

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
