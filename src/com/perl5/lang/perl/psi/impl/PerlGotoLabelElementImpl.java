package com.perl5.lang.perl.psi.impl;

import com.intellij.openapi.util.AtomicNotNullLazyValue;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiReference;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.psi.PerlGlobVariable;
import com.perl5.lang.perl.psi.PerlGotoLabelElement;
import com.perl5.lang.perl.psi.PerlVisitor;
import com.perl5.lang.perl.psi.references.PerlGotoLabelReference;
import org.jetbrains.annotations.NotNull;

/**
 * Created by pverma on 12/30/15.
 */
public class PerlGotoLabelElementImpl extends LeafPsiElement implements PerlGotoLabelElement {

    protected AtomicNotNullLazyValue<PsiReference[]> myReferences;

    public PerlGotoLabelElementImpl(@NotNull IElementType type, CharSequence text) {
        super(type, text);
        createMyReferences();
    }

    private void createMyReferences()
    {
        myReferences = new AtomicNotNullLazyValue<PsiReference[]>()
        {
            @NotNull
            @Override
            protected PsiReference[] compute()
            {
                return new PsiReference[]{new PerlGotoLabelReference(PerlGotoLabelElementImpl.this, null)};
            }
        };
    }

    @NotNull
    @Override
    public String getLabel() {
        return this.getText();
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor)
    {
        if (visitor instanceof PerlVisitor) ((PerlVisitor) visitor).visitGotoLabelElement(this);
        else super.accept(visitor);
    }

    @NotNull
    @Override
    public PsiReference[] getReferences()
    {
        return myReferences.getValue();
    }

    @Override
    public PsiReference getReference()
    {
        return myReferences.getValue()[0];
    }

    @Override
    public void clearCaches()
    {
        super.clearCaches();
        createMyReferences();
    }
}
