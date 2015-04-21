package com.perl5.lang.pod;

import com.intellij.lang.Language;

/**
 * Created by hurricup on 21.04.2015.
 */
public class PodLanguage extends Language
{

	public static final PodLanguage INSTANCE = new PodLanguage();

	public PodLanguage() {
		super("Plain Old Documentation");
	}

	@Override
	public boolean isCaseSensitive() {
		return true;
	}
}
