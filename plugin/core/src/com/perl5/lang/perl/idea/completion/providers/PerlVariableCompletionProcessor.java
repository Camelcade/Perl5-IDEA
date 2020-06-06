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
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class PerlVariableCompletionProcessor {
  protected abstract @NotNull CompletionResultSet getResultSet();

  public abstract @NotNull PsiElement getVariableNameElement();

  public abstract @NotNull PsiElement getVariableElement();

  public abstract boolean isFullQualified();

  public abstract boolean hasBraces();

  public abstract boolean isDeclaration();

  public abstract boolean isForceShortMain();

  public @NotNull Project getProject() {
    return getVariableNameElement().getProject();
  }

  /**
   * @return true iff {@code suggestedName} matches current prefix matcher
   */
  @Contract("null->false")
  public final boolean matches(@Nullable String suggestedName) {
    ProgressManager.checkCanceled();
    return StringUtil.isNotEmpty(suggestedName) && getResultSet().getPrefixMatcher().prefixMatches(suggestedName);
  }

  /**
   * @return true iff we should go on, false if we should stop
   */
  /**
   * @return true iff we should go on, false if we should stop
   */
  public final boolean process(@Nullable LookupElementBuilder lookupElement) {
    if (lookupElement != null) {
      addElement(lookupElement);
    }
    return result();
  }

  protected abstract void addElement(@NotNull LookupElementBuilder lookupElement);

  /**
   * @return true iff we should go on, false if we should stop
   */
  public abstract boolean result();

  @Contract(pure = true)
  public abstract @NotNull PerlVariableCompletionProcessor withPrefixMatcher(@NotNull String prefix);
}
