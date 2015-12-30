package com.perl5.lang.perl.psi.references;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.impl.source.resolve.ResolveCache;
import com.perl5.lang.perl.psi.PerlGotoLabelElement;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by pverma on 12/30/15.
 */
public class PerlGotoLabelReference extends PerlReference {

    private static final ResolveCache.Resolver RESOLVER = new ResolveCache.Resolver()
    {
        @Override
        public PsiElement resolve(@NotNull PsiReference psiReference, boolean incompleteCode)
        {
            PsiElement element = psiReference.getElement();
            return PerlPsiUtil.findGotoLabelDeclaration(element.getContainingFile(), ((PerlGotoLabelElement) element).getLabel());
        }
    };

    public PerlGotoLabelReference(@NotNull PsiElement element, TextRange textRange) {
        super(element, textRange);
    }

    @Nullable
    @Override
    public PsiElement resolve() {
        return ResolveCache.getInstance(myElement.getProject()).resolveWithCaching(this, RESOLVER, true, false);
    }
}

