package com.perl5.lang.perl.parser;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by hurricup on 04.05.2015.
 */
public class PerlCodeBlockState implements Cloneable
{
	protected HashMap<String,Boolean> features;

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

	public boolean isFeatureEnabled(String featureName)
	{
		return isFeatureValid(featureName) && features.get(featureName);
	}

	public boolean isFeatureValid(String featureName)
	{
		return features.get(featureName) != null;
	}

}
