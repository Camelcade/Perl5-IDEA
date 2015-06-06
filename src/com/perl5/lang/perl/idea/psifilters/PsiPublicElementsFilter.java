/*
 * Copyright 2015 Alexandr Evstigneev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
