package com.perl5.lang.xs;

/**
 * Created by hurricup on 21.04.2015.
 */

import com.intellij.lang.Language;

/**
 * Created by hurricup on 21.04.2015.
 */
public class XSLanguage extends Language
{

	public static final XSLanguage INSTANCE = new XSLanguage();

	public XSLanguage() {
		super("Perl extension language");
	}

	@Override
	public boolean isCaseSensitive() {
		return true;
	}
}
