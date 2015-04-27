package com.perl5.lang.perl;

import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.files.PerlFileTypeScript;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 24.04.2015.
 */
public class PerlElementType extends PerlTokenType
{
	public PerlElementType(@NotNull @NonNls String debugName) {
		super(debugName);
	}

	public String toString() {
		return "PerlElementType." + super.toString();
	}
}
