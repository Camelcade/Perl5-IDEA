// This is a generated file. Not intended for manual editing.
package com.perl5.lang.pod.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.pod.psi.PodParagraph;
import com.perl5.lang.pod.psi.PodSection;
import com.perl5.lang.pod.psi.PodVisitor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.perl5.lang.pod.lexer.PodElementTypes.POD_TAG;

public class PodSectionImpl extends ASTWrapperPsiElement implements PodSection
{

	public PodSectionImpl(ASTNode node)
	{
		super(node);
	}

	public void accept(@NotNull PsiElementVisitor visitor)
	{
		if (visitor instanceof PodVisitor) ((PodVisitor) visitor).visitSection(this);
		else super.accept(visitor);
	}

	@Override
	@NotNull
	public List<PodParagraph> getParagraphList()
	{
		return PsiTreeUtil.getChildrenOfTypeAsList(this, PodParagraph.class);
	}

	@Override
	@NotNull
	public PsiElement getPodTag()
	{
		return findNotNullChildByType(POD_TAG);
	}

}
