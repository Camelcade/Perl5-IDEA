package com.perl5.lang.perl.parser;

import java.util.HashMap;

/**
 * Created by hurricup on 09.05.2015.
 */
public class PerlPackagePragmaFeatures extends PerlPackagePragma
{
	protected HashMap<String,Boolean> features;

	public PerlPackagePragmaFeatures()
	{
		super("feature");
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

	public PerlPackagePragmaFeatures(PerlPackagePragmaFeatures original)
	{
		super(original);
		features = new HashMap<String, Boolean>(original.features);
	}

	/**
	 * Apply positive change to the current pragma object
	 * @param c state change
	 */
	public void use(PerlUseParameters c)
	{
		for (String featureName : c.getPackageParams())
		{
			if (isFeatureValid(featureName))
				features.put(featureName, true);
		}
	}

	/**
	 * Apply positive change to the current pragma object
	 * @param c state change
	 */
	public void no(PerlUseParameters c)
	{
		for (String featureName : c.getPackageParams())
		{
			if (isFeatureValid(featureName))
				features.put(featureName, false);
		}
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
	 * Checks if feature name valid
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

}
