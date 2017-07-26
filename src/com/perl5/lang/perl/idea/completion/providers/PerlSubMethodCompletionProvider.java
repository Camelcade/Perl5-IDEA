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
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import com.perl5.lang.perl.idea.completion.util.PerlSubCompletionUtil;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.mro.PerlMro;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 25.07.2015.
 */
public class PerlSubMethodCompletionProvider extends CompletionProvider<CompletionParameters> {
  public void addCompletions(@NotNull CompletionParameters parameters,
                             ProcessingContext context,
                             @NotNull CompletionResultSet resultSet) {

    PsiElement method = parameters.getPosition().getParent();
    assert method instanceof PsiPerlMethod;

    String packageName = ((PsiPerlMethod)method).getPackageName();
    PerlNamespaceElement namespaceElement = ((PsiPerlMethod)method).getNamespaceElement();
    boolean isSuper = namespaceElement != null && namespaceElement.isSUPER();
    if (isSuper) {
      packageName = PerlPackageUtil.getContextPackageName(method);
    }

    //				System.out.println("Autocomplete for " + packageName);
    if (packageName == null) {
      return;
    }

    // fixme
    for (PsiElement element : PerlMro.getVariants(method, packageName, isSuper)) {
      if (element instanceof PerlSubDefinitionElement && ((PerlSubDefinitionElement)element).isMethod()) {
        resultSet.addElement(PerlSubCompletionUtil.getSubDefinitionLookupElement((PerlSubDefinitionElement)element));
      }
      else if (element instanceof PerlSubDeclarationElement && ((PerlSubDeclarationElement)element).isMethod()) {
        resultSet.addElement(PerlSubCompletionUtil.getSubDeclarationLookupElement((PerlSubDeclarationElement)element));
      }
      else if (element instanceof PerlGlobVariable && ((PerlGlobVariable)element).getName() != null) {
        resultSet.addElement(PerlSubCompletionUtil.getGlobLookupElement((PerlGlobVariable)element));
      }
    }
  }
}
