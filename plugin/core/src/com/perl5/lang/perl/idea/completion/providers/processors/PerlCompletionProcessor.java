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
import com.intellij.codeInsight.completion.PrefixMatcher;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface PerlCompletionProcessor {
  @NotNull CompletionResultSet getResultSet();

  @NotNull PsiElement getLeafElement();

  default @NotNull PsiElement getLeafParentElement() {
    return getLeafElement().getParent();
  }

  default @NotNull PsiFile getContainingFile() {
    return getLeafElement().getContainingFile();
  }

  default @NotNull PsiFile getOriginalFile() {
    return getContainingFile().getContainingFile();
  }

  default @NotNull VirtualFile getVirtualFile() {
    return getOriginalFile().getVirtualFile();
  }

  default @NotNull Project getProject() {
    return getLeafElement().getProject();
  }

  /**
   * @return true iff {@code suggestedName} matches current prefix matcher
   */
  @Contract("null->false")
  boolean matches(@Nullable String suggestedName);

  /**
   * @return true iff we should go on, false if we should stop
   */
  boolean process(@Nullable LookupElementBuilder lookupElement);

  /**
   * Should be used for a single custom-created elements. For mass update, please, check texts before creation a lookup
   */
  boolean processSingle(@NotNull LookupElementBuilder lookupElement);

  @Contract(pure = true)
  @NotNull PerlCompletionProcessor withPrefixMatcher(@NotNull String prefix);

  void logStatus(@NotNull Class<?> clazz);

  /**
   * @return true iff we should go on, false if we should stop
   */
  boolean result();

  void addElement(@NotNull LookupElementBuilder lookupElement);

  @NotNull
  default PrefixMatcher getPrefixMatcher() {
    return getResultSet().getPrefixMatcher();
  }

  @NotNull
  default String getPrefix() {
    return getPrefixMatcher().getPrefix();
  }
}
