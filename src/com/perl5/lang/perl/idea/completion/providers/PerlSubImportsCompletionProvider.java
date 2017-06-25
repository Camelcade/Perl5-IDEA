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
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import com.perl5.lang.perl.extensions.packageprocessor.PerlExportDescriptor;
import com.perl5.lang.perl.idea.completion.util.PerlSubCompletionUtil;
import com.perl5.lang.perl.psi.PerlNamespaceDefinitionElement;
import com.perl5.lang.perl.psi.PerlNamespaceElement;
import com.perl5.lang.perl.psi.PsiPerlMethod;
import com.perl5.lang.perl.util.PerlPackageUtil;
import com.perl5.lang.perl.util.PerlSubUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by hurricup on 24.08.2015.
 */
public class PerlSubImportsCompletionProvider extends CompletionProvider<CompletionParameters> {
  public void addCompletions(@NotNull final CompletionParameters parameters,
                             ProcessingContext context,
                             @NotNull final CompletionResultSet resultSet) {
    PsiElement position = parameters.getPosition();

    PsiPerlMethod method = (PsiPerlMethod)position.getParent();

    if (method.isObjectMethod()) {
      return;
    }

    Project project = method.getProject();
    if (!method.hasExplicitNamespace()) {
      PerlNamespaceDefinitionElement namespaceContainer = PerlPackageUtil.getNamespaceContainerForElement(position);
      if (namespaceContainer != null) {
        fillWithNamespaceImports(namespaceContainer, resultSet);
      }
    }
    else {    // not an object method, but has explicit namespace
      PerlNamespaceElement namespaceElement = method.getNamespaceElement();
      if (namespaceElement != null) {
        String targetPackageName = namespaceElement.getCanonicalName();
        if (targetPackageName != null) {
          for (PerlNamespaceDefinitionElement namespaceDefinition : PerlPackageUtil.getNamespaceDefinitions(project, targetPackageName)) {
            fillWithNamespaceImports(namespaceDefinition, resultSet);
          }
        }
      }
    }
  }

  protected static void fillWithNamespaceImports(@NotNull PerlNamespaceDefinitionElement namespaceContainer,
                                                 @NotNull final CompletionResultSet resultSet) {
    for (PerlExportDescriptor exportDescriptor : namespaceContainer.getImportedSubsDescriptors()) {
      List<PsiElement> psiElements =
        PerlSubUtil.collectRelatedItems(exportDescriptor.getTargetCanonicalName(), namespaceContainer.getProject());

      if (psiElements.isEmpty()) // no definition found
      {
        resultSet.addElement(exportDescriptor.getLookupElement());
      }
      else {
        for (PsiElement element : psiElements) {
          resultSet.addElement(PerlSubCompletionUtil.getSmartLookupElement(element));
        }
      }
    }
  }
}
