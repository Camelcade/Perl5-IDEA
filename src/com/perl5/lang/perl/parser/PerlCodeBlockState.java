package com.perl5.lang.perl.parser;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by hurricup on 04.05.2015.
 */
public class PerlCodeBlockState implements Cloneable
{
	protected HashMap<String,Boolean> features;

	protected PerlSub subDefinition;
	protected PerlSub subDeclaration;

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

	/**
	 * Check if feature enabled by it's name
	 * @param featureName	feature name
	 * @return	status
	 */
	protected boolean isFeatureEnabled(String featureName)
	{
		return isFeatureValid(featureName) && features.get(featureName);
	}

	/**
	 * Checks if feature name valid @todo mark invalid features
	 * @param featureName	feature name
	 * @return	status
	 */
	public boolean isFeatureValid(String featureName)
	{
		return features.get(featureName) != null;
	}

	/**
	 * Checks if signatures enabled in current scope
	 * @return	status
	 */
	public boolean isSignaturesEnabled(){return isFeatureEnabled("signatures");}

	/**
	 * Applying positive change to current state: use ...
	 * @param c state change object
	 */
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
	}

	/**
	 * Applying negative change to the current state: no ...
	 * @param c state change object
	 */
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
	}

	// getters and setters
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

	public PerlSub getSubDefinition()
	{
		return subDefinition;
	}

	public PerlSub getSubDeclaration()
	{
		return subDeclaration;
	}

	public void setSubDefinition(PerlSub subDefinition)
	{
		this.subDefinition = subDefinition;
	}

	public void setSubDeclaration(PerlSub subDeclaration)
	{
		this.subDeclaration = subDeclaration;
	}
}
