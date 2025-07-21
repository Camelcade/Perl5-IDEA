/*
 * Copyright 2015-2025 Alexandr Evstigneev
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

package com.perl5.lang.perl.psi;

import com.perl5.lang.perl.psi.properties.PerlLexicalScopeMember;
import com.perl5.lang.perl.psi.properties.PerlVariableNameElementContainer;
import com.perl5.lang.perl.psi.utils.PerlVariableType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public interface PerlVariable extends PsiPerlExpr, PerlLexicalScopeMember, PerlVariableNameElementContainer {

  /**
   * @return an explicit namespace name or null if there is none
   */
  @Nullable
  String getExplicitNamespaceName();

  /**
   * Guessing actual variable type from context
   *
   * @return variable type
   */
  @NotNull PerlVariableType getActualType();

  @Nullable PerlVariableDeclarationElement getLexicalDeclaration();

  int getLineNumber();

  @Nullable String getName();

  /**
   * Checks if this occurrence is declaration
   *
   * @return true if it's declaration
   */
  boolean isDeclaration();

  /**
   * @return original or braced name if it's non-single char control variable
   */
  static @NotNull String braceName(@NotNull String variableName) {
    if (variableName.startsWith("^") && variableName.length() > 2 && variableName.charAt(1) != '{') {
      return "{" + variableName + "}";
    }
    return variableName;
  }
}
