package com.perl5.lang.perl.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.perl5.lang.perl.psi.PerlFunctionDefinitionNamed;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 26.04.2015.
 */
public class PerlFunctionDefinitionNamedUtil  extends ASTWrapperPsiElement
{
	public PerlFunctionDefinitionNamedUtil( @NotNull ASTNode node){ super(node);}
}
