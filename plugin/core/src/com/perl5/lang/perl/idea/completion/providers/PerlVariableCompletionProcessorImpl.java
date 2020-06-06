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

import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.util.registry.Registry;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

public class PerlVariableCompletionProcessorImpl extends PerlVariableCompletionProcessor {
  private final int myLimit = Registry.intValue("ide.completion.variant.limit");

  @NotNull
  private final CompletionResultSet myResultSet;
  @NotNull
  private final PsiElement myVariableNameElement;
  private final boolean myIsFullQualified;
  private final boolean myHasBraces;
  private final boolean myIsDeclaration;
  private int myCounter = 0;

  public PerlVariableCompletionProcessorImpl(@NotNull CompletionResultSet resultSet,
                                             @NotNull PsiElement variableNameElement,
                                             boolean isFullQualified,
                                             boolean hasBraces,
                                             boolean isDeclaration) {
    myResultSet = resultSet;
    myVariableNameElement = variableNameElement;
    myIsFullQualified = isFullQualified;
    myHasBraces = hasBraces;
    myIsDeclaration = isDeclaration;
  }

  @Override
  @NotNull
  protected CompletionResultSet getResultSet() {
    return myResultSet;
  }

  @Override
  @NotNull
  public PsiElement getVariableNameElement() {
    return myVariableNameElement;
  }

  @Override
  @NotNull
  public PsiElement getVariableElement() {
    return myVariableNameElement.getParent();
  }

  @Override
  public boolean isFullQualified() {
    return myIsFullQualified;
  }

  @Override
  public boolean hasBraces() {
    return myHasBraces;
  }

  @Override
  public boolean isDeclaration() {
    return myIsDeclaration;
  }

  @Override
  protected void addElement(@NotNull LookupElementBuilder lookupElement) {
    getResultSet().addElement(lookupElement);
    myCounter++;
  }

  @Override
  public boolean result() {
    return myCounter < myLimit;
  }

  @Override
  public boolean isForceShortMain() {
    return false;
  }

  @Override
  public @NotNull PerlVariableCompletionProcessor withPrefixMatcher(@NotNull String prefix) {
    CompletionResultSet newResultSet = getResultSet().withPrefixMatcher(prefix);
    PerlVariableCompletionProcessorImpl originalProcessor = this;
    return new PerlVariableCompletionProcessorImpl(
      newResultSet, myVariableNameElement, isFullQualified(), hasBraces(), isDeclaration()
    ) {
      @Override
      protected void addElement(@NotNull LookupElementBuilder lookupElement) {
        super.addElement(lookupElement);
        originalProcessor.myCounter++;
      }

      @Override
      public boolean result() {
        return originalProcessor.result();
      }
    };
  }
}
