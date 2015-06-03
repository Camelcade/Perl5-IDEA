package com.perl5.lang.perl.lexer;

import com.intellij.psi.tree.IElementType;

import java.util.HashMap;

/**
 * Created by hurricup on 03.06.2015.
 */
public class PerlAnnotations implements PerlElementTypes
{
	public static final HashMap<String,IElementType> TOKEN_TYPES = new HashMap<>();

	static{
		TOKEN_TYPES.put("deprecated", ANNOTATION_DEPRECATED_KEY);
		TOKEN_TYPES.put("abstract", ANNOTATION_ABSTRACT_KEY);
		TOKEN_TYPES.put("override", ANNOTATION_OVERRIDE_KEY);
		TOKEN_TYPES.put("method", ANNOTATION_METHOD_KEY);
		TOKEN_TYPES.put("returns", ANNOTATION_RETURNS_KEY);
	}
}
