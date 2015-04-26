package com.perl5.lang.perl;

/**
 * Created by hurricup on 12.04.2015.
 */

import com.intellij.lang.Language;
import com.perl5.lang.perl.idea.highlighter.PerlSyntaxHighlighter;

public class PerlLanguage extends Language
{
	public static final PerlLanguage INSTANCE = new PerlLanguage();

	public PerlLanguage() {
		super("Perl5");
	}

	@Override
	public boolean isCaseSensitive() {
		return true;
	}
}

