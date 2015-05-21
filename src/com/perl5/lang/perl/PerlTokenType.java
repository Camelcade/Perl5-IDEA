package com.perl5.lang.perl;

/**
 * Created by hurricup on 12.04.2015.
 */

import com.intellij.lang.StdLanguages;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.ILazyParseableElementType;
import com.intellij.psi.tree.ILightLazyParseableElementType;
import com.intellij.sql.psi.SqlLanguage;
import com.perl5.lang.perl.files.PerlFileTypeScript;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class PerlTokenType extends IElementType
{
	public static final ILazyParseableElementType HEREDOC_SQL = new ILazyParseableElementType("HEREDOC_SQL", PerlLanguage.INSTANCE);

	public PerlTokenType(@NotNull @NonNls String debugName) {
		super(debugName, PerlLanguage.INSTANCE);
	}

	public String toString() {
		return "PerlTokenType." + super.toString();
	}

}

