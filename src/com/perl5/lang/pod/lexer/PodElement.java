package com.perl5.lang.pod.lexer;

import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.PerlFileTypeScript;
import com.perl5.lang.pod.PodFileType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 21.04.2015.
 */
public class PodElement extends IElementType
{
	protected String debugName = null;

	public PodElement(@NotNull @NonNls String debugName) {
		super(debugName, PodFileType.LANGUAGE);
		this.debugName = debugName;
	}

	public String toString() {
		return "PodTokenType " + super.toString();
	}

}
