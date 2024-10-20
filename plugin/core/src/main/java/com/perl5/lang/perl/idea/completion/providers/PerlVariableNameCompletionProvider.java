/*
 * Copyright 2015-2024 Alexandr Evstigneev
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
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.util.ProcessingContext;
import com.perl5.lang.perl.idea.completion.providers.processors.PerlVariableCompletionProcessorImpl;
import com.perl5.lang.perl.idea.completion.util.PerlPackageCompletionUtil;
import com.perl5.lang.perl.idea.completion.util.PerlVariableCompletionUtil;
import com.perl5.lang.perl.psi.PerlVariable;
import com.perl5.lang.perl.psi.PerlVariableNameElement;
import com.perl5.lang.perl.util.PerlTimeLogger;
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

    String namespaceName = null;
    if (variableNameElement instanceof PerlVariableNameElement) {
      resultSet = resultSet.withPrefixMatcher(getVariableNamePrefix(variableNameElement, parameters.getOffset()));
      PsiElement variable = variableNameElement.getParent();
      if (variable instanceof PerlVariable perlVariable) {
        namespaceName = perlVariable.getExplicitNamespaceName();
      }
    }

    boolean isDeclaration = VARIABLE_NAME_IN_DECLARATION_PATTERN.accepts(variableNameElement);
    boolean hasBraces = VARIABLE_OPEN_BRACES.contains(PsiUtilCore.getElementType(variableNameElement.getPrevSibling()));
    boolean isCapped = StringUtil.startsWithChar(resultSet.getPrefixMatcher().getPrefix(), '^');
    PerlVariableCompletionProcessorImpl variableCompletionProcessor =
      new PerlVariableCompletionProcessorImpl(
        parameters, resultSet,
        variableNameElement,
        namespaceName,
        hasBraces,
        isDeclaration,
        false
      );

    PerlTimeLogger timeLogger = new PerlTimeLogger(LOG);

    // declaration helper
    if (isDeclaration) {
      PerlVariableCompletionUtil.fillWithUnresolvedVars(variableCompletionProcessor);
      timeLogger.debug("Filled with unresolved variables");
    }
    else if (!variableCompletionProcessor.isFullQualified()) {
      if (!isCapped) {
        PerlVariableCompletionUtil.fillWithLexicalVariables(variableCompletionProcessor);
        timeLogger.debug("Filled with lexical variables");
      }
      PerlVariableCompletionUtil.fillWithBuiltInVariables(variableCompletionProcessor);
      timeLogger.debug("Filled with built in variables");
    }

    // built ins
    if (VARIABLE_NAME_IN_LOCAL_DECLARATION_PATTERN.accepts(variableNameElement)) {
      PerlVariableCompletionUtil.fillWithBuiltInVariables(variableCompletionProcessor);
      timeLogger.debug("Filled with built in variables");
    }

    // imports
    if (!isCapped && !isDeclaration && !variableCompletionProcessor.isFullQualified()) {
      PerlVariableCompletionUtil.fillWithImportedVariables(variableCompletionProcessor);
      timeLogger.debug("Filled with imported variables");
    }

    // fqn names
    if (!isCapped && !isDeclaration) {
      PerlPackageCompletionUtil.processAllNamespacesNames(variableCompletionProcessor, true, false);
      timeLogger.debug("Filled with namespace names");
      PerlVariableCompletionUtil.processFullQualifiedVariables(variableCompletionProcessor);
      timeLogger.debug("Filled with full qualified variables");
    }
    variableCompletionProcessor.logStatus(getClass());
  }

  private String getVariableNamePrefix(@NotNull PsiElement insertedElement, int offsetInFile) {
    String substr = insertedElement.getText().substring(0, offsetInFile - insertedElement.getTextRange().getStartOffset());
    if (substr.length() == 0 || Character.isWhitespace(substr.charAt(substr.length() - 1))) {
      return "";
    }

    return substr.trim();
  }
}
