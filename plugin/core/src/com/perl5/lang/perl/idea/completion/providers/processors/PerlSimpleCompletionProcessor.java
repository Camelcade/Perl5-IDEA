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

public class PerlSimpleCompletionProcessor extends PerlCompletionProcessorBase {
  public PerlSimpleCompletionProcessor(@NotNull CompletionResultSet resultSet,
                                       @NotNull PsiElement leafElement) {
    super(resultSet, leafElement);
  }

  private PerlSimpleCompletionProcessor(@NotNull PerlCompletionProcessorBase original,
                                        @NotNull String newPrefixMatcher) {
    super(original, newPrefixMatcher);
  }

  @Override
  public @NotNull PerlSimpleCompletionProcessor withPrefixMatcher(@NotNull String prefix) {
    return new PerlSimpleCompletionProcessor(this, prefix);
  }
}
