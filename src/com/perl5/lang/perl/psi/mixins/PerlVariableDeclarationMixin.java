package com.perl5.lang.perl.psi.mixins;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.psi.IPerlVariableDeclaration;
import com.perl5.lang.perl.psi.PerlNamespace;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 01.06.2015.
 */
public abstract class PerlVariableDeclarationMixin extends  PerlLexicalScopeElementMixin implements IPerlVariableDeclaration
{
	public PerlVariableDeclarationMixin(ASTNode node)
	{
		super(node);
	}

	@Nullable
	@Override
	public PerlNamespace getNamespace()
	{
		return findChildByClass(PerlNamespace.class);
	}
}
