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

package com.perl5.lang.perl.idea.completion.providers;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.PlainPrefixMatcher;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import com.perl5.lang.perl.idea.PerlCompletionWeighter;
import com.perl5.lang.perl.idea.completion.util.PerlPackageCompletionUtil;
import com.perl5.lang.perl.psi.PsiPerlMethod;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 24.07.2015.
 */
public class PerlPackageSubCompletionProvider extends CompletionProvider<CompletionParameters> {
  @Override
  protected void addCompletions(
    @NotNull CompletionParameters parameters,
    ProcessingContext context,
    @NotNull CompletionResultSet result) {
    PsiElement method = parameters.getPosition().getParent();
    assert method instanceof PsiPerlMethod : "Expected PsiPerlMethod, got " + method.getClass();

    String explicitNamespace = ((PsiPerlMethod)method).getExplicitPackageName();
    String currentPrefixMatcher = result.getPrefixMatcher().getPrefix();
    String newPrefixMathcer =
      (explicitNamespace == null ? currentPrefixMatcher : (explicitNamespace + PerlPackageUtil.PACKAGE_SEPARATOR) + currentPrefixMatcher);
    result = result.withPrefixMatcher(new PlainPrefixMatcher(newPrefixMathcer));

    if (!((PsiPerlMethod)method).isObjectMethod()) {
      PerlPackageCompletionUtil.fillWithAllPackageNamesWithAutocompletion(parameters.getPosition(), result);
    }
    else {
      if (!StringUtil.equals(PerlPackageUtil.SUPER_PACKAGE_FULL, newPrefixMathcer)) {
        LookupElementBuilder newElement =
          PerlPackageCompletionUtil.getPackageLookupElementWithAutocomplete(method.getProject(), PerlPackageUtil.SUPER_PACKAGE_FULL, null);
        newElement.putUserData(PerlCompletionWeighter.WEIGHT, -1);
        result.addElement(newElement);
      }
    }


    //
    //		final String finalNameFilter = nameFilter;
    //
    //		if (!isObjectMethod)
    //		{
    //			// fixme not dry with PerlPackageNamesCompletionProvider
    //			PerlPackageUtil.processDefinedPackageNames(PerlScopes.getProjectAndLibrariesScope(project), new PerlInternalIndexKeysProcessor()
    //			{
    //				@Override
    //				public boolean process(String s)
    //				{
    //					if (super.process(s))
    //					{
    //						if (finalNameFilter == null)
    //						{
    //							result.addElement(PerlPackageCompletionUtil.getPackageLookupElementWithAutocomplete(project, s));
    //						}
    //						else if (s.startsWith(finalNameFilter))
    //						{
    //							result.addElement(PerlPackageCompletionUtil.getPackageLookupElementWithAutocomplete(project, s.substring(finalNameFilter.length())));
    //						}
    //					}
    //					return true;
    //				}
    //			});
    //		}
  }
}
