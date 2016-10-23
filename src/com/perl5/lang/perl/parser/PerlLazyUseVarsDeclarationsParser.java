package com.perl5.lang.perl.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;

public class PerlLazyUseVarsDeclarationsParser extends PerlParserImpl
{
	public static final PsiParser INSTANCE = new PerlLazyUseVarsDeclarationsParser();

	@Override
	public boolean parseFileContents(PsiBuilder b, int l)
	{
		return PerlParserImpl.use_vars_declarations(b, l);
	}
}
