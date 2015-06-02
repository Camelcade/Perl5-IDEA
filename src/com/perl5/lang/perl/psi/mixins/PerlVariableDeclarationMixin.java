package com.perl5.lang.perl.psi.mixins;

import com.intellij.lang.ASTNode;
import com.perl5.lang.perl.psi.PerlVariableDeclaration;
import com.perl5.lang.perl.psi.PerlNamespaceElement;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 01.06.2015.
 */
public abstract class PerlVariableDeclarationMixin extends PerlLexicalScopeMemberMixin implements PerlVariableDeclaration
{
	public PerlVariableDeclarationMixin(ASTNode node)
	{
		super(node);
	}

	@Nullable
	@Override
	public PerlNamespaceElement getNamespaceElement()
	{
		return findChildByClass(PerlNamespaceElement.class);
	}
}
