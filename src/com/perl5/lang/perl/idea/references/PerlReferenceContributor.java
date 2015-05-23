package com.perl5.lang.perl.idea.references;

import com.intellij.openapi.util.TextRange;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import com.perl5.lang.perl.psi.impl.PerlHeredocOpenerImpl;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 26.04.2015.
 */
public class PerlReferenceContributor extends PsiReferenceContributor
{
	@Override
	public void registerReferenceProviders(PsiReferenceRegistrar registrar)
	{
		registrar.registerReferenceProvider(
				PlatformPatterns.psiElement(PerlHeredocOpenerImpl.class),
				new PsiReferenceProvider()
				{
					@NotNull
					@Override
					public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context)
					{
//						PsiTreeUtil.collectElements()
//						PsiLiteralExpression literalExpression = (PsiLiteralExpression) element;
//						String text = (String) literalExpression.getValue();
//						if (text != null ) {
//							return new PsiReference[]{new PerlReference(element, new TextRange(1, text.length() + 1))};
//						}
//						return new PsiReference[0];					}
						return null;
					}
				}
		);
	}
}
