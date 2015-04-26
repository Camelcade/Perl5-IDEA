package com.perl5.lang.perl.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.perl5.lang.perl.psi.PerlNamedElement;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 26.04.2015.
 */
public abstract class PerlNamedElementImpl extends ASTWrapperPsiElement implements PerlNamedElement
{
	public PerlNamedElementImpl(@NotNull ASTNode node){
		super(node);
	}
}
