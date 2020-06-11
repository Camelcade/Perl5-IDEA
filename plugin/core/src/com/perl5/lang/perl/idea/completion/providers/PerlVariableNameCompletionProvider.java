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
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.patterns.StandardPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.util.ProcessingContext;
import com.perl5.lang.perl.idea.completion.providers.processors.PerlCompletionProvider;
import com.perl5.lang.perl.idea.completion.providers.processors.PerlVariableCompletionProcessorImpl;
import com.perl5.lang.perl.idea.completion.util.PerlVariableCompletionUtil;
import com.perl5.lang.perl.psi.PerlVariable;
import com.perl5.lang.perl.psi.PerlVariableNameElement;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;

import static com.perl5.lang.perl.idea.PerlElementPatterns.VARIABLE_NAME_IN_DECLARATION_PATTERN;
import static com.perl5.lang.perl.idea.PerlElementPatterns.VARIABLE_NAME_IN_LOCAL_DECLARATION_PATTERN;
import static com.perl5.lang.perl.lexer.PerlTokenSets.VARIABLE_OPEN_BRACES;


public class PerlVariableNameCompletionProvider extends PerlCompletionProvider {

  @Override
  public void addCompletions(@NotNull CompletionParameters parameters,
                             @NotNull ProcessingContext context,
                             @NotNull CompletionResultSet resultSet) {
    PsiElement variableNameElement = parameters.getPosition();

    PsiElement originalElement = parameters.getOriginalPosition();
    String namespaceName = null;
    if (originalElement instanceof PerlVariableNameElement) {
      resultSet = resultSet.withPrefixMatcher(CompletionData.findPrefixDefault(
        variableNameElement, parameters.getOffset(), StandardPatterns.alwaysFalse()));
      PsiElement variable = originalElement.getParent();
      if (variable instanceof PerlVariable) {
        namespaceName = ((PerlVariable)variable).getExplicitNamespaceName();
      }
    }

    boolean isDeclaration = VARIABLE_NAME_IN_DECLARATION_PATTERN.accepts(variableNameElement);
    boolean hasBraces = VARIABLE_OPEN_BRACES.contains(PsiUtilCore.getElementType(variableNameElement.getPrevSibling()));
    PerlVariableCompletionProcessorImpl variableCompletionProcessor =
      new PerlVariableCompletionProcessorImpl(
        withFqnSafeMatcher(resultSet),
        variableNameElement,
        namespaceName, hasBraces,
        isDeclaration,
        false,
        StringUtil.startsWith(variableNameElement.getText(), PerlPackageUtil.NAMESPACE_SEPARATOR));

    // declaration helper
    if (isDeclaration) {
      PerlVariableCompletionUtil.fillWithUnresolvedVars(variableCompletionProcessor);
    }
    else if (!variableCompletionProcessor.isFullQualified()) {
      PerlVariableCompletionUtil.fillWithLexicalVariables(variableCompletionProcessor);
      PerlVariableCompletionUtil.fillWithBuiltInVariables(variableCompletionProcessor);
    }

    // built ins
    if (VARIABLE_NAME_IN_LOCAL_DECLARATION_PATTERN.accepts(variableNameElement)) {
      PerlVariableCompletionUtil.fillWithBuiltInVariables(variableCompletionProcessor);
    }

    // imports
    if (!isDeclaration && !variableCompletionProcessor.isFullQualified()) {
      PerlVariableCompletionUtil.fillWithImportedVariables(variableCompletionProcessor);
    }

    // fqn names
    if (!isDeclaration) {
      PerlVariableCompletionUtil.fillWithFullQualifiedVariables(variableCompletionProcessor);
    }
    variableCompletionProcessor.logStatus(getClass());
  }
}
