/*
 * Copyright 2015-2020 Alexandr Evstigneev
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
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import com.perl5.lang.perl.extensions.packageprocessor.PerlExportDescriptor;
import com.perl5.lang.perl.idea.PerlElementPatterns;
import com.perl5.lang.perl.idea.completion.util.PerlVariableCompletionUtil;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.utils.PerlVariableType;
import com.perl5.lang.perl.util.PerlPackageUtil;
import com.perl5.lang.perl.util.processors.PerlNamespaceEntityProcessor;
import org.jetbrains.annotations.NotNull;


public class PerlVariableNameCompletionProvider extends CompletionProvider<CompletionParameters> implements PerlElementPatterns {

  public void addCompletions(@NotNull CompletionParameters parameters,
                             @NotNull ProcessingContext context,
                             @NotNull CompletionResultSet resultSet) {
    PsiElement variableNameElement = parameters.getPosition();

    boolean isDeclaration = VARIABLE_NAME_IN_DECLARATION_PATTERN.accepts(variableNameElement);
    boolean isFullQualified = PerlPackageUtil.isFullQualifiedName(variableNameElement.getText());

    // declaration helper
    if (isDeclaration) {
      PerlVariableCompletionUtil.fillWithUnresolvedVars((PerlVariableNameElement)variableNameElement, resultSet);
    }
    else if (!isFullQualified) {
      PerlVariableCompletionUtil.fillWithLexicalVariables(variableNameElement, resultSet);
      PerlVariableCompletionUtil.fillWithBuiltInVariables(variableNameElement, resultSet);
    }

    // built ins
    if (VARIABLE_NAME_IN_LOCAL_DECLARATION_PATTERN.accepts(variableNameElement)) {
      PerlVariableCompletionUtil.fillWithBuiltInVariables(variableNameElement, resultSet);
    }

    // imports
    if (!isDeclaration && !isFullQualified) {
      fillWithImportedVariables(variableNameElement, resultSet);
    }

    // fqn names
    if (!isDeclaration) {
      PerlVariableCompletionUtil.fillWithFullQualifiedVariables(variableNameElement, resultSet);
    }
  }

  private void fillWithImportedVariables(@NotNull PsiElement variableNameElement, @NotNull CompletionResultSet resultSet) {
    PerlNamespaceDefinitionElement namespaceContainer = PerlPackageUtil.getNamespaceContainerForElement(variableNameElement);

    if (namespaceContainer == null) {
      return;
    }

    String packageName = namespaceContainer.getNamespaceName();

    if (StringUtil.isEmpty(packageName)) // incomplete package definition
    {
      return;
    }

    PerlNamespaceEntityProcessor<PerlExportDescriptor> processor = null;
    PsiElement perlVariable = variableNameElement.getParent();

    if (perlVariable instanceof PsiPerlScalarVariable) {
      processor = (namespaceName, entity) -> {
        LookupElementBuilder lookupElement = null;
        if (entity.isScalar()) {
          lookupElement = PerlVariableCompletionUtil.createVariableLookupElement(entity.getImportedName(), PerlVariableType.SCALAR);
        }
        else if (entity.isArray()) {
          lookupElement = PerlVariableCompletionUtil.createVariableLookupElement(entity.getImportedName(), PerlVariableType.ARRAY);
        }
        else if (entity.isHash()) {
          lookupElement = PerlVariableCompletionUtil.createVariableLookupElement(entity.getImportedName(), PerlVariableType.HASH);
        }

        if (lookupElement != null) {
          resultSet.addElement(lookupElement.withTypeText(entity.getRealPackage(), true));
        }
        return true;
      };
    }
    else if (perlVariable instanceof PsiPerlArrayVariable || perlVariable instanceof PsiPerlArrayIndexVariable) {
      processor = (namespaceName, entity) -> {
        LookupElementBuilder lookupElement = null;
        if (entity.isArray()) {
          lookupElement = PerlVariableCompletionUtil.createArrayElementLookupElement(entity.getImportedName(), PerlVariableType.ARRAY);
        }
        else if (entity.isHash()) {
          lookupElement = PerlVariableCompletionUtil.createHashElementLookupElement(entity.getImportedName(), PerlVariableType.HASH);
        }

        if (lookupElement != null) {
          resultSet.addElement(lookupElement.withTypeText(entity.getRealPackage(), true));
        }
        return true;
      };
    }
    else if (perlVariable instanceof PsiPerlHashVariable) {
      processor = (namespaceName, entity) -> {
        LookupElementBuilder lookupElement = null;
        if (entity.isHash()) {
          lookupElement = PerlVariableCompletionUtil.createVariableLookupElement(entity.getImportedName(), PerlVariableType.HASH);
        }

        if (lookupElement != null) {
          resultSet.addElement(lookupElement.withTypeText(entity.getRealPackage(), true));
        }
        return true;
      };
    }

    if (processor != null) {
      namespaceContainer.processExportDescriptors(processor);
    }
  }
}
