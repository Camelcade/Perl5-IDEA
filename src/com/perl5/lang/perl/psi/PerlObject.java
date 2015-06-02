package com.perl5.lang.perl.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 01.06.2015.
 */
public interface PerlObject extends PsiElement
{
	// trying to guess namespace for this object. Returns null if can't
	@Nullable
	public String guessNamespace();
}
