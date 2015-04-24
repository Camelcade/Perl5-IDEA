package com.perl5.lang.pod;

import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 24.04.2015.
 */
public class PodTokenType extends IElementType
{
	public PodTokenType(@NotNull @NonNls String debugName) {
		super(debugName, PodLanguage.INSTANCE);
	}

	@Override
	public String toString() {
		return "PodTokenType." + super.toString();
	}
}
