package com.perl5.lang.perl.parser;

import com.intellij.lang.PsiBuilder;

public class PerlUseVarsDeclarationsParser extends PerlParserImpl
{
	@Override
	public boolean parseFileContents(PsiBuilder b, int l)
	{
		return PerlParserImpl.use_vars_declarations(b, l);
	}
}
