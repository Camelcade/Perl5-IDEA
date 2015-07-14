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

package com.perl5.lang.perl.idea.refactoring;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.refactoring.rename.RenamePsiElementProcessor;
import com.perl5.lang.perl.psi.PsiPerlHeredocOpener;
import com.perl5.lang.perl.psi.impl.PerlStringContentElementImpl;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * Created by evstigneev on 14.07.2015.
 */
public class PerlRenameHeredocProcessor extends RenamePsiElementProcessor {
    @Override
    public boolean canProcessElement(@NotNull PsiElement element) {
        if (element instanceof PerlStringContentElementImpl) {
            PsiElement parentElement = element.getParent();
            if (parentElement != null) {
                if (parentElement instanceof PsiPerlHeredocOpener)
                    return true;
                else {
                    PsiElement grandParentElement = parentElement.getParent();
                    if (grandParentElement != null && grandParentElement instanceof PsiPerlHeredocOpener)
                        return true;
                }
            }
        }
        return false;
    }

    @NotNull
    @Override
    public Collection<PsiReference> findReferences(PsiElement element) {
        return ReferencesSearch.search(element, GlobalSearchScope.fileScope(element.getContainingFile())).findAll();
    }
}
