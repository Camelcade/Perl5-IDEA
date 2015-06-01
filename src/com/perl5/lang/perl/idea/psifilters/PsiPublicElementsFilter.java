package com.perl5.lang.perl.idea.psifilters;

import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiElementFilter;
import com.perl5.lang.perl.lexer.PerlElementTypes;

/**
 * creates a filter that returns only psi elements that have a public declaration (like subroutines,variables)
 * fixme unused atm
 */
public class PsiPublicElementsFilter implements PsiElementFilter {

    @Override
    public boolean isAccepted(PsiElement psiElement) {
        if(psiElement.getNode().getElementType().equals(PerlElementTypes.VARIABLE_DECLARATION_GLOBAL)) {
            //found global variable
            return true;
        }
        //otherwise try to find subroutine
        if(isElementFunction(psiElement) && checkSubToken(psiElement)){
            return true;
        }
        if(isElementFunction(psiElement.getParent())&& checkSubToken(psiElement.getParent()) ) {
            return true;
        }
        return false;
    }

    private boolean isElementFunction(PsiElement psiElement) {
        return psiElement != null
                && psiElement.getNode() != null
                && (psiElement.getNode().getElementType().equals(PerlElementTypes.PERL_FUNCTION) || psiElement.getNode().getElementType().equals(PerlElementTypes.PERL_FUNCTION));
    }

    private boolean checkSubToken(PsiElement psiElement) {
        return  psiElement.getPrevSibling() != null
                && psiElement.getPrevSibling().getPrevSibling() != null
                && psiElement.getPrevSibling().getPrevSibling().getText().equals("sub");
    }
}
