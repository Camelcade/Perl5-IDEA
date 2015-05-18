package com.perl5.lang.eperl;

import com.intellij.lang.Language;
import com.intellij.psi.templateLanguages.TemplateLanguage;
import com.perl5.lang.perl.PerlLanguage;
import org.apache.batik.svggen.ImageCacher;

/**
 * Created by hurricup on 18.05.2015.
 */
public class EmbeddedPerlLanguage extends Language implements TemplateLanguage
{
	public static final EmbeddedPerlLanguage INSTANCE = new EmbeddedPerlLanguage();

	private EmbeddedPerlLanguage()
	{
		super("Embedded Perl");
	}
}
