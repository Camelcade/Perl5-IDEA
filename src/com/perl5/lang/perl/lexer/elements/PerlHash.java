package com.perl5.lang.perl.lexer.elements;

import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.lexer.PerlTokenTypes;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by hurricup on 19.04.2015.
 */
public class PerlHash
{
	// @todo shouldn't we think about map search
	public static IElementType getHashType(String hash)
	{
		return BUILT_IN.contains(hash)
				? PerlTokenTypes.PERL_VARIABLE_HASH_BUILT_IN
				: PerlTokenTypes.PERL_VARIABLE_HASH;
	}

	public static final ArrayList<String> BUILT_IN = new ArrayList<String>( Arrays.asList(
			"%!",
			"%+",
			"%-",
			"%^H",
			"%ENV",
			"%INC",
			"%OVERLOAD",
			"%SIG"
	));
}
