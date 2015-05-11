package com.perl5.lang.perl.util;

import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.lexer.PerlElementTypes;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by hurricup on 19.04.2015.
 */
public class PerlGlobUtil implements PerlElementTypes
{
	public static final ArrayList<String> BUILT_IN = new ArrayList<String>( Arrays.asList(
			"*ARGV",
			"*STDERR",
			"*STDOUT",
			"*ARGVOUT",
			"*STDIN"
	));
}
