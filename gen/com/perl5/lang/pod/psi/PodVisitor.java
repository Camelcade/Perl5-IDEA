// This is a generated file. Not intended for manual editing.
package com.perl5.lang.pod.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;

public class PodVisitor extends PsiElementVisitor
{

	public void visitParagraph(@NotNull PodParagraph o)
	{
		visitPsiElement(o);
	}

	public void visitSection(@NotNull PodSection o)
	{
		visitPsiElement(o);
	}

	public void visitPsiElement(@NotNull PsiElement o)
	{
		visitElement(o);
	}

}
