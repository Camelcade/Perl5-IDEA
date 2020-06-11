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

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface PerlVariableCompletionProcessor extends PerlCompletionProcessor {
  default boolean isFullQualified() {
    return getExplicitNamespaceName() != null;
  }

  /**
   * @return true iff braces are typed by user, e.g. {@code ${<caret>}}
   */
  boolean hasBraces();

  /**
   * @return true iff we are at variable declaration, not usage.
   */
  boolean isDeclaration();

  /**
   * @return true iff we must use short for of main, {@code ::} instead of {@code main::}
   */
  boolean isForceShortMain();

  /**
   * @return true iff we are currently at lexical variables pass, therefore global variables should be used without a namespace
   */
  boolean isLexical();

  /**
   * @return explicit namespace typed by user if any
   */
  @Nullable String getExplicitNamespaceName();

  @Override
  @Contract(pure = true)
  @NotNull PerlVariableCompletionProcessor withPrefixMatcher(@NotNull String prefix);
}
