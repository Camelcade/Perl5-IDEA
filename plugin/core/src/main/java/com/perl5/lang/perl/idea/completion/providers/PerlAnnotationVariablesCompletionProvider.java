/*
 * Copyright 2015-2023 Alexandr Evstigneev
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
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import com.perl5.lang.perl.idea.completion.providers.processors.PerlVariableCompletionProcessor;
import com.perl5.lang.perl.idea.completion.providers.processors.PerlVariableCompletionProcessorImpl;
import com.perl5.lang.perl.psi.PerlAnnotationType;
import com.perl5.lang.perl.psi.PerlAnnotationVariableElement;
import com.perl5.lang.perl.psi.PerlVariableDeclarationElement;
import com.perl5.lang.perl.psi.utils.PerlVariableAnnotations;
import org.jetbrains.annotations.NotNull;

import static com.perl5.lang.perl.idea.completion.util.PerlVariableCompletionUtil.processVariableLookupElement;

public class PerlAnnotationVariablesCompletionProvider extends PerlCompletionProvider {
  @Override
  protected void addCompletions(@NotNull CompletionParameters parameters,
                                @NotNull ProcessingContext context,
                                @NotNull CompletionResultSet result) {
    var currentPosition = parameters.getPosition();
    if (currentPosition.getParent() instanceof PerlAnnotationVariableElement annotationVariableElement &&
        annotationVariableElement.getParent() instanceof PerlAnnotationType typeAnnotation) {
      var originalPosition = parameters.getOriginalPosition();
      PerlVariableCompletionProcessor variableCompletionProcessor =
        createProcessor(parameters, result, originalPosition != null ? originalPosition : currentPosition);
      processTargets(typeAnnotation, variableCompletionProcessor, false);
      variableCompletionProcessor.logStatus(getClass());
    }
  }

  static @NotNull PerlVariableCompletionProcessorImpl createProcessor(@NotNull CompletionParameters parameters,
                                                                      @NotNull CompletionResultSet result,
                                                                      @NotNull PsiElement currentPosition) {
    return new PerlVariableCompletionProcessorImpl(
      parameters, result, currentPosition, null, false, true, true);
  }

  static boolean processTargets(@NotNull PerlAnnotationType typeAnnotation,
                                @NotNull PerlVariableCompletionProcessor variableCompletionProcessor,
                                boolean includeSigil) {
    char requiredSigil = includeSigil ? 0 : variableCompletionProcessor.getLeafElement().getText().charAt(0);

    return PerlVariableAnnotations.processPotentialTargets(typeAnnotation, variableDeclaration -> {
      if (!isStrictlyTyped(typeAnnotation, variableDeclaration)) {
        if (includeSigil) {
          return processVariableLookupElement(variableDeclaration, variableDeclaration.getActualType().getSigil(),
                                              variableCompletionProcessor);
        }
        else if (variableDeclaration.getActualType().getSigil() == requiredSigil) {
          return processVariableLookupElement(variableDeclaration, '_', variableCompletionProcessor);
        }
      }
      return variableCompletionProcessor.result();
    });
  }

  private static boolean isStrictlyTyped(@NotNull PerlAnnotationType typeAnnotation,
                                         @NotNull PerlVariableDeclarationElement variableDeclarationElement) {
    return !PerlVariableAnnotations.processAnnotations(variableDeclarationElement,
                                                       new PerlVariableAnnotations.VariableAnnotationProcessor() {
                                                         @Override
                                                         public boolean processType(@NotNull PerlAnnotationType annotationType) {
                                                           return annotationType.equals(typeAnnotation) || annotationType.isWildCard();
                                                         }
                                                       });
  }
}
