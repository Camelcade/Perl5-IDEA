/*
 * Copyright 2015-2017 Alexandr Evstigneev
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

package com.perl5.lang.htmlmason.parser.psi.references;

import com.intellij.openapi.application.QueryExecutorBase;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.util.Processor;
import com.perl5.lang.htmlmason.HTMLMasonSyntaxElements;
import com.perl5.lang.htmlmason.parser.psi.impl.HTMLMasonFileImpl;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 19.03.2016.
 */
public class HTMLMasonReferencesSearcher extends QueryExecutorBase<PsiReference, ReferencesSearch.SearchParameters>
  implements HTMLMasonSyntaxElements {
  public HTMLMasonReferencesSearcher() {
    super(true);
  }

  @Override
  public void processQuery(@NotNull ReferencesSearch.SearchParameters queryParameters, @NotNull Processor<? super PsiReference> consumer) {
    PsiElement element = queryParameters.getElementToSearch();
    if (element instanceof HTMLMasonFileImpl) {
      queryParameters.getOptimizer().searchWord(COMPONENT_SLUG_SELF, queryParameters.getEffectiveSearchScope(), true, element);
      queryParameters.getOptimizer().searchWord(COMPONENT_SLUG_PARENT, queryParameters.getEffectiveSearchScope(), true, element);
      queryParameters.getOptimizer().searchWord(COMPONENT_SLUG_REQUEST, queryParameters.getEffectiveSearchScope(), true, element);
    }
  }
}
