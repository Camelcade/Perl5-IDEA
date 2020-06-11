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
import org.jetbrains.annotations.Nullable;

public class PerlVariableCompletionProcessorImpl extends PerlCompletionProcessorBase implements PerlVariableCompletionProcessor {
  private final boolean myHasBraces;
  private final boolean myIsDeclaration;
  private final boolean myIsLexical;
  private final boolean myIsForceShortMain;
  private final @Nullable String myExplicitNamespaceName;

  public PerlVariableCompletionProcessorImpl(@NotNull CompletionResultSet resultSet,
                                             @NotNull PsiElement leafElement,
                                             @Nullable String explicitNamespaceName,
                                             boolean hasBraces,
                                             boolean isDeclaration,
                                             boolean isLexical,
                                             boolean isForceShortMain) {
    super(resultSet, leafElement);
    myHasBraces = hasBraces;
    myIsDeclaration = isDeclaration;
    myIsLexical = isLexical;
    myExplicitNamespaceName = explicitNamespaceName;
    myIsForceShortMain = isForceShortMain;
  }

  public PerlVariableCompletionProcessorImpl(@NotNull PerlCompletionProcessor processor,
                                             @Nullable String explicitNamespaceName,
                                             boolean hasBraces,
                                             boolean isDeclaration,
                                             boolean isLexical,
                                             boolean isForceShortMain) {
    super(processor);
    myHasBraces = hasBraces;
    myIsDeclaration = isDeclaration;
    myIsLexical = isLexical;
    myExplicitNamespaceName = explicitNamespaceName;
    myIsForceShortMain = isForceShortMain;
  }

  private PerlVariableCompletionProcessorImpl(@NotNull PerlVariableCompletionProcessorImpl original,
                                              @NotNull String newPrefixMatcher) {
    super(original, newPrefixMatcher);
    myHasBraces = original.hasBraces();
    myIsDeclaration = original.isDeclaration();
    myIsLexical = original.isLexical();
    myExplicitNamespaceName = original.getExplicitNamespaceName();
    myIsForceShortMain = original.isForceShortMain();
  }

  @Override
  public boolean isLexical() {
    return myIsLexical;
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
  public @Nullable String getExplicitNamespaceName() {
    return myExplicitNamespaceName;
  }

  @Override
  public boolean isForceShortMain() {
    return myIsForceShortMain;
  }

  @Override
  public @NotNull PerlVariableCompletionProcessor withPrefixMatcher(@NotNull String prefix) {
    return new PerlVariableCompletionProcessorImpl(this, prefix);
  }
}
