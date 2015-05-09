package com.perl5.lang.perl.parser;

import java.util.ArrayList;

/**
 * Created by hurricup on 04.05.2015.
 * class represents use/no state modifications
 */
public class PerlCodeBlockStateChange
{
	private String perlVersion = null;
	private String packageName = null;
	private String packageVersion = null;
	private ArrayList<String> packageParams = null;

	public String getPerlVersion()
	{
		return perlVersion;
	}

	public void setPerlVersion(String perlVersion)
	{
		this.perlVersion = perlVersion;
	}

	public String getPackageName()
	{
		return packageName;
	}

	public void setPackageName(String packageName)
	{
		this.packageName = packageName;
	}

	public String getPackageVersion()
	{
		return packageVersion;
	}

	public void setPackageVersion(String packageVersion)
	{
		this.packageVersion = packageVersion;
	}

	public ArrayList<String> getPackageParams()
	{
		return packageParams;
	}

	public void setPackageParams(ArrayList<String> packageParams)
	{
		this.packageParams = packageParams;
	}
}
