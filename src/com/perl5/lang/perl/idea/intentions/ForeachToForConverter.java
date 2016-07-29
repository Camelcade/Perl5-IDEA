package com.perl5.lang.perl.idea.intentions;

import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.utils.PerlElementFactory;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

/**
 * Created by bcardoso on 7/28/16.
 */
public class ForeachToForConverter extends PsiElementBaseIntentionAction
{
	@Nls
	@NotNull
	@Override
	public String getText()
	{
		return "Convert foreach to indexed for (Alpha)";
	}

	@Nls
	@NotNull
	@Override
	public String getFamilyName()
	{
		return getText();
	}

	@Override
	public boolean isAvailable(@NotNull Project project, Editor editor, @NotNull PsiElement element)
	{
		if (!element.isWritable()) {
			return false;
		}

		PsiElement parent = element.getParent();
		if (!(parent instanceof PsiPerlForeachCompound)) {
			return false;
		}

		PsiPerlForListStatement forListStatement = ((PsiPerlForeachCompound) parent).getForListStatement();
		return forListStatement != null
				&& forListStatement.getExpr() instanceof PsiPerlVariableDeclarationLexical;
	}

	@Override
	public void invoke(@NotNull Project project, Editor editor, @NotNull PsiElement element) throws IncorrectOperationException
	{
		PsiPerlForeachCompound foreachElement = (PsiPerlForeachCompound) element.getParent();

		PsiPerlForListStatement forListStatement = foreachElement.getForListStatement();
		assert forListStatement != null;

		PsiPerlExpr forExpr = forListStatement.getExpr();
		assert forExpr != null;

		PsiPerlForListEpxr forListEpxr = forListStatement.getForListEpxr();

		PsiPerlBlock block = foreachElement.getBlock();
		assert block != null;

		PsiPerlForCompound indexedFor = PerlElementFactory.createIndexedFor(project, forExpr, forListEpxr, block);
		foreachElement.replace(indexedFor);
	}

	@Override
	public boolean startInWriteAction()
	{
		return true;
	}
}
