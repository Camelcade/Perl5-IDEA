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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PerlDelegatingVariableCompletionProcessor extends PerlDelegatingCompletionProcessor<PerlVariableCompletionProcessor>
  implements PerlVariableCompletionProcessor {

  public PerlDelegatingVariableCompletionProcessor(@NotNull PerlVariableCompletionProcessor delegate) {
    super(delegate);
  }

  @Override
  public boolean isFullQualified() {
    return getDelegate().isFullQualified();
  }

  @Override
  public boolean hasBraces() {
    return getDelegate().hasBraces();
  }

  @Override
  public boolean isDeclaration() {
    return getDelegate().isDeclaration();
  }

  @Override
  public boolean isLexical() {
    return getDelegate().isLexical();
  }

  @Override
  public @Nullable String getExplicitNamespaceName() {
    return getDelegate().getExplicitNamespaceName();
  }

  @Override
  public @NotNull PerlVariableCompletionProcessor withPrefixMatcher(@NotNull String prefix) {
    try {
      PerlDelegatingVariableCompletionProcessor clone = (PerlDelegatingVariableCompletionProcessor)clone();
      clone.setDelegate(getDelegate().withPrefixMatcher(prefix));
      return clone;
    }
    catch (CloneNotSupportedException e) {
      throw new RuntimeException(e);
    }
  }
}
