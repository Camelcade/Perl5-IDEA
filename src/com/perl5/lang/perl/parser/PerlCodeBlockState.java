package com.perl5.lang.perl.parser;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by hurricup on 04.05.2015.
 */
public class PerlCodeBlockState implements Cloneable
{
	protected HashMap<String,Boolean> features;

	protected final PerlSyntaxTrap stringsTrap = new PerlSyntaxTrap();
	protected final PerlSyntaxTrap packagesTrap = new PerlSyntaxTrap();
	protected final PerlSyntaxTrap versionsTrap = new PerlSyntaxTrap();

	public PerlCodeBlockState()
	{
		// set features state
		features = new HashMap<String, Boolean>();
		features.put("say", false);
		features.put("state", false);
		features.put("switch", false);
		features.put("unicode_strings", false);
		features.put("unicode_eval", false);
		features.put("evalbytes", false);
		features.put("current_sub", false);
		features.put("array_base", false);
		features.put("fc", false);
		features.put("lexical_subs", false);
		features.put("signatures", false);
	}

	public PerlCodeBlockState(PerlCodeBlockState original)
	{
		features = new HashMap<String, Boolean>(original.features);
	}

	protected boolean isFeatureEnabled(String featureName)
	{
		return isFeatureValid(featureName) && features.get(featureName);
	}

	public boolean isFeatureValid(String featureName)
	{
		return features.get(featureName) != null;
	}

	public boolean isSignaturesEnabled(){return isFeatureEnabled("signatures");}

	public void use(PerlCodeBlockStateChange c)
	{
		if( "feature".equals(c.packageName))
		{
			for( String featureName: c.packageParams)
			{
				if( isFeatureValid(featureName))
					features.put(featureName, true);
			}
		}
//		System.out.println("Changing state use: ");
//		System.out.println("Perl version: " + c.perlVersion);
//		System.out.println("Package: " + c.packageName);
//		System.out.println("Package version: " + c.packageVersion);
//		if( c.packageParams != null )
//			System.out.println("Package params: " + c.packageParams.toString());
	}

	public void no(PerlCodeBlockStateChange c)
	{
		if( "feature".equals(c.packageName))
		{
			for( String featureName: c.packageParams)
			{
				if( isFeatureValid(featureName))
					features.put(featureName, false);
			}
		}
//		System.out.println("Changing state no:");
//		System.out.println("Perl version: " + c.perlVersion);
//		System.out.println("Package: " + c.packageName);
//		System.out.println("Package version: " + c.packageVersion);
//		if( c.packageParams != null )
//			System.out.println("Package params: " + c.packageParams.toString());
	}

	public PerlSyntaxTrap getStringsTrap()
	{
		return stringsTrap;
	}

	public PerlSyntaxTrap getPackagesTrap()
	{
		return packagesTrap;
	}

	public PerlSyntaxTrap getVersionsTrap()
	{
		return versionsTrap;
	}
}
