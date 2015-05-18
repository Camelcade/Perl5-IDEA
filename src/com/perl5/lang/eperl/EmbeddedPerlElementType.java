package com.perl5.lang.eperl;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 18.05.2015.
 */
public class EmbeddedPerlElementType extends EmbeddedPerlTokenType
{
	public EmbeddedPerlElementType(@NotNull @NonNls String debugName) {
		super(debugName);
	}

	public String toString() {
		return "EmbeddedPerlElementType." + super.toString();
	}

}
