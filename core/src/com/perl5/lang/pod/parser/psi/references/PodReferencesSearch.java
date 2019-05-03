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

package com.perl5.lang.pod.parser.psi.references;

import com.intellij.openapi.application.QueryExecutorBase;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.util.Processor;
import com.perl5.lang.pod.idea.completion.PodLinkCompletionProvider;
import com.perl5.lang.pod.parser.psi.PodTitledSection;
import org.jetbrains.annotations.NotNull;

public class PodReferencesSearch extends QueryExecutorBase<PsiReference, ReferencesSearch.SearchParameters> {
  public PodReferencesSearch() {
    super(true);
  }

  @Override
  public void processQuery(@NotNull ReferencesSearch.SearchParameters queryParameters, @NotNull Processor<? super PsiReference> consumer) {
    final PsiElement element = queryParameters.getElementToSearch();
    if (!(element instanceof PodTitledSection)) {
      return;
    }
    final String textTitle = ((PodTitledSection)element).getTitleText();
    if (!StringUtil.isNotEmpty(textTitle)) {
      return;
    }
    String longestWord = "";
    for (String chunk : textTitle.split("[^\\w_]+")) {
      if (chunk.length() > longestWord.length()) {
        longestWord = chunk;
      }
    }
    queryParameters.getOptimizer().searchWord(longestWord, queryParameters.getEffectiveSearchScope(), true, element);
    String escapedTitle = PodLinkCompletionProvider.escapeTitle(textTitle);
    if (!StringUtil.equals(escapedTitle, textTitle)) {
      queryParameters.getOptimizer().searchWord(escapedTitle, queryParameters.getEffectiveSearchScope(), true, element);
    }
  }
}
