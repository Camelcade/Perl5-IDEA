package com.perl5.lang.lexer.elements;

import com.perl5.PerlFileType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 19.04.2015.
 */
public class PerlVariable extends PerlElement
{
	public PerlVariable(@NotNull @NonNls String debugName) {
		super(debugName);
	}
}
