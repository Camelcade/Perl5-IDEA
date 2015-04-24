package com.perl5.lang.perl.lexer.helpers;

import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.lexer.PerlTokenTypes;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by hurricup on 24.04.2015.
 */
public class PerlPackage
{
	// @todo shouldn't we think about map search
	public static IElementType getPackageType(String packageName)
	{
		return BUILT_IN.contains(packageName)
				? PerlTokenTypes.PERL_PACKAGE_BUILT_IN
				: PerlTokenTypes.PERL_PACKAGE;
	}

	public static final ArrayList<String> BUILT_IN = new ArrayList<String>( Arrays.asList(
			"utf8",
			"strict",
			"Exporter",
			"Scalar::Util",
			"warnings"
	));
}
