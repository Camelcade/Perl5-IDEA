/*
 * Copyright 2015-2019 Alexandr Evstigneev
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

package com.perl5.lang.perl.parser.Class.Accessor.idea.findUsages;

import com.intellij.openapi.application.QueryExecutorBase;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.util.Processor;
import com.perl5.lang.perl.parser.Class.Accessor.psi.impl.PerlClassAccessorMethod;
import org.jetbrains.annotations.NotNull;


public class ClassAccessorReferencesSearcher extends QueryExecutorBase<PsiReference, ReferencesSearch.SearchParameters> {
  public ClassAccessorReferencesSearcher() {
    super(true);
  }

  @Override
  public void processQuery(@NotNull ReferencesSearch.SearchParameters queryParameters, @NotNull Processor<? super PsiReference> consumer) {
    PsiElement element = queryParameters.getElementToSearch();
    if (element instanceof PerlClassAccessorMethod) {
      queryParameters.getOptimizer()
        .searchWord(((PerlClassAccessorMethod)element).getSubName(), queryParameters.getEffectiveSearchScope(), true, element);

      PerlClassAccessorMethod pairedMethod = ((PerlClassAccessorMethod)element).getPairedMethod();
      if (pairedMethod != null) {
        queryParameters.getOptimizer().searchWord(pairedMethod.getSubName(), queryParameters.getEffectiveSearchScope(), true, pairedMethod);
      }
    }
  }
}
