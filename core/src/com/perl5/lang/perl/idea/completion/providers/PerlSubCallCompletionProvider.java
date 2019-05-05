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

package com.perl5.lang.perl.idea.completion.providers;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.util.ProcessingContext;
import com.perl5.lang.perl.extensions.packageprocessor.PerlExportDescriptor;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlCallStaticValue;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlCallValue;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlNamespaceItemProcessor;
import com.perl5.lang.perl.idea.completion.util.PerlSubCompletionUtil;
import com.perl5.lang.perl.psi.PerlGlobVariable;
import com.perl5.lang.perl.psi.PerlSubDeclarationElement;
import com.perl5.lang.perl.psi.PerlSubDefinitionElement;
import com.perl5.lang.perl.psi.PsiPerlMethod;
import org.jetbrains.annotations.NotNull;


public class PerlSubCallCompletionProvider extends CompletionProvider<CompletionParameters> {
  public void addCompletions(@NotNull CompletionParameters parameters,
                             @NotNull ProcessingContext context,
                             @NotNull CompletionResultSet resultSet) {
    PsiElement position = parameters.getPosition();
    PsiElement method = position.getParent();
    assert method instanceof PsiPerlMethod;

    PerlCallValue perlValue = PerlCallValue.from(method);
    if (perlValue == null) {
      return;
    }

    boolean isStatic = perlValue instanceof PerlCallStaticValue;

    perlValue.processTargetNamespaceElements(
      position.getProject(), position.getResolveScope(), position, new PerlNamespaceItemProcessor<PsiNamedElement>() {
        @Override
        public boolean processItem(@NotNull PsiNamedElement element) {
          if (element instanceof PerlSubDefinitionElement &&
              (isStatic && ((PerlSubDefinitionElement)element).isStatic() || ((PerlSubDefinitionElement)element).isMethod())) {
            resultSet.addElement(PerlSubCompletionUtil.getSubDefinitionLookupElement((PerlSubDefinitionElement)element));
          }
          if (element instanceof PerlSubDeclarationElement &&
              (isStatic && ((PerlSubDeclarationElement)element).isStatic() || ((PerlSubDeclarationElement)element).isMethod())) {
            resultSet.addElement(PerlSubCompletionUtil.getSubDeclarationLookupElement((PerlSubDeclarationElement)element));
          }
          if (element instanceof PerlGlobVariable && ((PerlGlobVariable)element).isLeftSideOfAssignment()) {
            if (StringUtil.isNotEmpty(element.getName())) {
              resultSet.addElement(PerlSubCompletionUtil.getGlobLookupElement((PerlGlobVariable)element));
            }
          }
          return true;
        }

        @Override
        public boolean processImportedItem(@NotNull PsiNamedElement element,
                                           @NotNull PerlExportDescriptor exportDescriptor) {
          resultSet.addElement(PerlSubCompletionUtil.getImportedEntityLookupElement(element, exportDescriptor));
          return true;
        }

        @Override
        public boolean processOrphanDescriptor(@NotNull PerlExportDescriptor exportDescriptor) {
          if (exportDescriptor.isSub()) {
            resultSet.addElement(exportDescriptor.getLookupElement());
          }
          return true;
        }
      });
  }
}
