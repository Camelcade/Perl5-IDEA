/*
 * Copyright 2015-2021 Alexandr Evstigneev
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

package com.perl5.lang.perl.psi.references;

import com.intellij.openapi.application.QueryExecutorBase;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.util.Processor;
import com.perl5.lang.perl.psi.PerlHeredocOpener;
import com.perl5.lang.perl.psi.PerlNamespaceDefinition;
import com.perl5.lang.perl.psi.impl.PerlPolyNamedElement;
import com.perl5.lang.perl.psi.light.PerlDelegatingLightNamedElement;
import com.perl5.lang.perl.psi.light.PerlLightSubDefinitionElement;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static com.perl5.lang.perl.util.PerlPackageUtil.__PACKAGE__;


public class PerlReferencesSearcher extends QueryExecutorBase<PsiReference, ReferencesSearch.SearchParameters> {
  public PerlReferencesSearcher() {
    super(true);
  }

  @Override
  public void processQuery(@NotNull ReferencesSearch.SearchParameters queryParameters, @NotNull Processor<? super PsiReference> consumer) {
    PsiElement element = queryParameters.getElementToSearch();

    if (element instanceof PerlHeredocOpener) {
      String heredocName = ((PerlHeredocOpener)element).getName();
      if ("".equals(heredocName)) {
        queryParameters.getOptimizer().searchWord("\n", queryParameters.getEffectiveSearchScope(), true, element);
      }
    }
    else if (element instanceof PerlLightSubDefinitionElement) {
      PerlPolyNamedElement<?> delegate = ((PerlLightSubDefinitionElement<?>)element).getDelegate();
      var identifyingElement = ((PerlLightSubDefinitionElement<?>)element).getIdentifyingElement();
      for (PerlDelegatingLightNamedElement<?> lightElement : delegate.getLightElements()) {
        if (Objects.equals(identifyingElement, lightElement.getIdentifyingElement())) {
          queryParameters.getOptimizer().searchWord(lightElement.getName(), queryParameters.getEffectiveSearchScope(), true, delegate);
        }
      }
    }
    else if (element instanceof PerlNamespaceDefinition) {
      queryParameters.getOptimizer().searchWord(__PACKAGE__, queryParameters.getEffectiveSearchScope(), true, element);
    }
  }
}
