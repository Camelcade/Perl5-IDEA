// This is a generated file. Not intended for manual editing.
package com.perl5.lang.pod.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.perl5.lang.pod.psi.PodParagraph;
import com.perl5.lang.pod.psi.PodVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.perl5.lang.pod.lexer.PodElementTypes.POD_NEWLINE;
import static com.perl5.lang.pod.lexer.PodElementTypes.POD_TEXT;

public class PodParagraphImpl extends ASTWrapperPsiElement implements PodParagraph
{

	public PodParagraphImpl(ASTNode node)
	{
		super(node);
	}

	public void accept(@NotNull PsiElementVisitor visitor)
	{
		if (visitor instanceof PodVisitor) ((PodVisitor) visitor).visitParagraph(this);
		else super.accept(visitor);
	}

	@Override
	@NotNull
	public PsiElement getPodNewline()
	{
		return findNotNullChildByType(POD_NEWLINE);
	}

	@Override
	@Nullable
	public PsiElement getPodText()
	{
		return findChildByType(POD_TEXT);
	}

}
