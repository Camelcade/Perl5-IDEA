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

package com.perl5.lang.perl.idea.completion.providers.processors;

import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

public class PerlVariableCompletionProcessorImpl extends PerlCompletionProcessorBase implements PerlVariableCompletionProcessor {
  private final boolean myIsFullQualified;
  private final boolean myHasBraces;
  private final boolean myIsDeclaration;

  public PerlVariableCompletionProcessorImpl(@NotNull CompletionResultSet resultSet,
                                             @NotNull PsiElement leafElement,
                                             boolean isFullQualified,
                                             boolean hasBraces,
                                             boolean isDeclaration) {
    super(resultSet, leafElement);
    myIsFullQualified = isFullQualified;
    myHasBraces = hasBraces;
    myIsDeclaration = isDeclaration;
  }

  public PerlVariableCompletionProcessorImpl(@NotNull PerlCompletionProcessor processor,
                                             boolean isFullQualified,
                                             boolean hasBraces,
                                             boolean isDeclaration) {
    super(processor);
    myIsFullQualified = isFullQualified;
    myHasBraces = hasBraces;
    myIsDeclaration = isDeclaration;
  }

  private PerlVariableCompletionProcessorImpl(@NotNull PerlVariableCompletionProcessorImpl original,
                                              @NotNull String newPrefixMatcher) {
    super(original, newPrefixMatcher);
    myIsFullQualified = original.isFullQualified();
    myHasBraces = original.hasBraces();
    myIsDeclaration = original.isDeclaration();
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
  public boolean isForceShortMain() {
    return false;
  }

  @Override
  public @NotNull PerlVariableCompletionProcessor withPrefixMatcher(@NotNull String prefix) {
    return new PerlVariableCompletionProcessorImpl(this, prefix);
  }
}
