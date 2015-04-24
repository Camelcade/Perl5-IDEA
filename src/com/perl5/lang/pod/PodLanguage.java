package com.perl5.lang.pod;

import com.intellij.lang.Language;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.perl5.lang.pod.highlighter.PodSyntaxHighlighter;

/**
 * Created by hurricup on 21.04.2015.
 */
public class PodLanguage extends Language
{
	public static final PodLanguage INSTANCE = new PodLanguage();

	public PodLanguage() {
		super("Perl5 POD");
	}

	@Override
	public boolean isCaseSensitive() {
		return true;
	}

}
