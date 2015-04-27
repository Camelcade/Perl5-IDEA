package com.perl5.lang.perl;

/**
 * Created by hurricup on 12.04.2015.
 */

import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.files.PerlFileTypeScript;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class PerlTokenType extends IElementType
{
	public PerlTokenType(@NotNull @NonNls String debugName) {
		super(debugName, PerlLanguage.INSTANCE);
	}

	public String toString() {
		return "PerlTokenType." + super.toString();
	}

}

