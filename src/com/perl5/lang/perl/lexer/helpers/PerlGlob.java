package com.perl5.lang.perl.lexer.helpers;

import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.lexer.PerlTokenTypes;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by hurricup on 19.04.2015.
 */
public class PerlGlob
{
	// @todo shouldn't we think about map search
	public static IElementType getGlobType(String glob)
	{
		return BUILT_IN.contains(glob)
				? PerlTokenTypes.PERL_VARIABLE_GLOB_BUILT_IN
				: PerlTokenTypes.PERL_VARIABLE_GLOB;
	}

	public static final ArrayList<String> BUILT_IN = new ArrayList<String>( Arrays.asList(
			"*ARGV",
			"*STDERR",
			"*STDOUT",
			"*ARGVOUT",
			"*STDIN"
	));
}
