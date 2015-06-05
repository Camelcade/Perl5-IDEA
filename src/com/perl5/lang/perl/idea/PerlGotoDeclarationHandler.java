package com.perl5.lang.perl.idea;

import com.intellij.codeInsight.navigation.actions.GotoDeclarationHandler;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiPolyVariantReference;
import com.intellij.psi.PsiReference;
import com.intellij.psi.ResolveResult;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 * Created by hurricup on 05.06.2015.
 */
public class PerlGotoDeclarationHandler implements GotoDeclarationHandler
{
	@Nullable
	@Override
	public PsiElement[] getGotoDeclarationTargets(@Nullable PsiElement sourceElement, int offset, Editor editor)
	{
		ArrayList<PsiElement> result = new ArrayList<>();
		if (sourceElement != null)
			for (PsiReference reference : sourceElement.getReferences())
				if (reference instanceof PsiPolyVariantReference)
					for (ResolveResult resolveResult : ((PsiPolyVariantReference) reference).multiResolve(false))
						result.add(resolveResult.getElement());
				else
					result.add(reference.resolve());

		return result.toArray(new PsiElement[result.size()]);
	}

	@Nullable
	@Override
	public String getActionText(DataContext context)
	{
		return null;
	}
}
