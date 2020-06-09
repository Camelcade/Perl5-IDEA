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

import com.intellij.codeInsight.completion.CompletionData;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.patterns.StandardPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.util.ProcessingContext;
import com.perl5.lang.perl.extensions.packageprocessor.PerlExportDescriptor;
import com.perl5.lang.perl.idea.PerlElementPatterns;
import com.perl5.lang.perl.idea.completion.providers.processors.PerlVariableCompletionProcessor;
import com.perl5.lang.perl.idea.completion.providers.processors.PerlVariableCompletionProcessorImpl;
import com.perl5.lang.perl.idea.completion.util.PerlVariableCompletionUtil;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.utils.PerlVariableType;
import com.perl5.lang.perl.util.PerlPackageUtil;
import com.perl5.lang.perl.util.processors.PerlNamespaceEntityProcessor;
import org.jetbrains.annotations.NotNull;

import static com.perl5.lang.perl.lexer.PerlTokenSets.VARIABLE_OPEN_BRACES;


public class PerlVariableNameCompletionProvider extends CompletionProvider<CompletionParameters> implements PerlElementPatterns {

  @Override
  public void addCompletions(@NotNull CompletionParameters parameters,
                             @NotNull ProcessingContext context,
                             @NotNull CompletionResultSet resultSet) {
    PsiElement variableNameElement = parameters.getPosition();

    PsiElement position = parameters.getOriginalPosition();
    if (position instanceof PerlVariableNameElement) {
      resultSet = resultSet.withPrefixMatcher(CompletionData.findPrefixDefault(
        variableNameElement, parameters.getOffset(), StandardPatterns.alwaysFalse()));
    }

    boolean isDeclaration = VARIABLE_NAME_IN_DECLARATION_PATTERN.accepts(variableNameElement);
    boolean isFullQualified = PerlPackageUtil.isFullQualifiedName(variableNameElement.getText());
    boolean hasBraces = VARIABLE_OPEN_BRACES.contains(PsiUtilCore.getElementType(variableNameElement.getPrevSibling()));
    PerlVariableCompletionProcessorImpl variableCompletionProcessor =
      new PerlVariableCompletionProcessorImpl(resultSet, variableNameElement, isFullQualified, hasBraces, isDeclaration);

    // declaration helper
    if (isDeclaration) {
      PerlVariableCompletionUtil.fillWithUnresolvedVars(variableCompletionProcessor);
    }
    else if (!isFullQualified) {
      PerlVariableCompletionUtil.fillWithLexicalVariables(variableCompletionProcessor);
      PerlVariableCompletionUtil.fillWithBuiltInVariables(variableCompletionProcessor);
    }

    // built ins
    if (VARIABLE_NAME_IN_LOCAL_DECLARATION_PATTERN.accepts(variableNameElement)) {
      PerlVariableCompletionUtil.fillWithBuiltInVariables(variableCompletionProcessor);
    }

    // imports
    if (!isDeclaration && !isFullQualified) {
      fillWithImportedVariables(variableCompletionProcessor);
    }

    // fqn names
    if (!isDeclaration) {
      PerlVariableCompletionUtil.fillWithFullQualifiedVariables(variableCompletionProcessor);
    }
    variableCompletionProcessor.logStatus(getClass());
  }

  static void fillWithImportedVariables(@NotNull PerlVariableCompletionProcessor variableCompletionProcessor) {
    PerlNamespaceDefinitionElement namespaceContainer =
      PerlPackageUtil.getNamespaceContainerForElement(variableCompletionProcessor.getLeafElement());

    if (namespaceContainer == null) {
      return;
    }

    String packageName = namespaceContainer.getNamespaceName();

    if (StringUtil.isEmpty(packageName)) // incomplete package definition
    {
      return;
    }

    PerlNamespaceEntityProcessor<PerlExportDescriptor> processor = null;
    PsiElement perlVariable = variableCompletionProcessor.getLeafParentElement();

    if (perlVariable instanceof PsiPerlScalarVariable) {
      processor = (namespaceName, entity) -> {
        LookupElementBuilder lookupElement = null;
        String entityName = entity.getImportedName();
        if (!variableCompletionProcessor.matches(entityName)) {
          return variableCompletionProcessor.result();
        }
        if (entity.isScalar()) {
          lookupElement = PerlVariableCompletionUtil.processVariableLookupElement(entityName, PerlVariableType.SCALAR);
        }
        else if (entity.isArray()) {
          lookupElement = PerlVariableCompletionUtil.processVariableLookupElement(entityName, PerlVariableType.ARRAY);
        }
        else if (entity.isHash()) {
          lookupElement = PerlVariableCompletionUtil.processVariableLookupElement(entityName, PerlVariableType.HASH);
        }

        if (lookupElement != null) {
          return variableCompletionProcessor.process(lookupElement.withTypeText(entity.getRealPackage(), true));
        }
        return variableCompletionProcessor.result();
      };
    }
    else if (perlVariable instanceof PsiPerlArrayVariable || perlVariable instanceof PsiPerlArrayIndexVariable) {
      processor = (namespaceName, entity) -> {
        LookupElementBuilder lookupElement = null;
        String entityName = entity.getImportedName();
        if (!variableCompletionProcessor.matches(entityName)) {
          return variableCompletionProcessor.result();
        }
        if (entity.isArray()) {
          lookupElement = PerlVariableCompletionUtil.createArrayElementLookupElement(entityName, PerlVariableType.ARRAY);
        }
        else if (entity.isHash()) {
          lookupElement = PerlVariableCompletionUtil.processHashElementLookupElement(entityName, PerlVariableType.HASH);
        }

        if (lookupElement != null) {
          return variableCompletionProcessor.process(lookupElement.withTypeText(entity.getRealPackage(), true));
        }
        return variableCompletionProcessor.result();
      };
    }
    else if (perlVariable instanceof PsiPerlHashVariable) {
      processor = (namespaceName, entity) -> {
        LookupElementBuilder lookupElement = null;
        if (entity.isHash()) {
          String entityName = entity.getImportedName();
          if (!variableCompletionProcessor.matches(entityName)) {
            return variableCompletionProcessor.result();
          }
          lookupElement = PerlVariableCompletionUtil.processVariableLookupElement(entityName, PerlVariableType.HASH);
        }

        if (lookupElement != null) {
          return variableCompletionProcessor.process(lookupElement.withTypeText(entity.getRealPackage(), true));
        }
        return variableCompletionProcessor.result();
      };
    }

    if (processor != null) {
      namespaceContainer.processExportDescriptors(processor);
    }
  }
}
