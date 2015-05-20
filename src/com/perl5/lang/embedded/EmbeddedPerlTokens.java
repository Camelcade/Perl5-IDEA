package com.perl5.lang.embedded;

import com.intellij.lang.StdLanguages;
import com.intellij.psi.templateLanguages.TemplateDataElementType;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.PerlElementType;
import com.perl5.lang.perl.PerlTokenType;
import com.perl5.lang.perl.lexer.PerlElementTypes;

/**
 * Created by hurricup on 19.05.2015.
 */
public interface EmbeddedPerlTokens extends PerlElementTypes
{
	IElementType OUTER_ELEMENT_TYPE = new PerlElementType("OUTER_ELEMENT_TYPE");
	IElementType HTML_TEMPLATE_DATA = new TemplateDataElementType("HTML_TEMPLATE_DATA", EmbeddedPerlLanguage.INSTANCE, TEMPLATE_BLOCK_HTML, OUTER_ELEMENT_TYPE);
}
