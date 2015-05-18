package com.perl5.lang.eperl;

import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.PerlLanguage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 18.05.2015.
 */
public class EmbeddedPerlTokenType extends IElementType
{
	public EmbeddedPerlTokenType(@NotNull @NonNls String debugName) {
		super(debugName, EmbeddedPerlLanguage.INSTANCE);
	}

	public String toString() {
		return "EmbeddedPerlTokenType." + super.toString();
	}

}
