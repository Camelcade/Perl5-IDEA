/*
 * Copyright 2015-2019 Alexandr Evstigneev
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
import com.perl5.lang.perl.psi.properties.PerlValuableEntity;
import com.perl5.lang.perl.psi.properties.PerlVariableNameElementContainer;
import com.perl5.lang.perl.psi.utils.PerlVariableType;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;


public interface PerlVariable extends PsiPerlExpr, PerlLexicalScopeMember, PerlValuableEntity, PerlVariableNameElementContainer {

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
  PerlVariableType getActualType();

  /**
   * Looking for lexical declaration of variable
   *
   * @return declare variable or null
   */
  PerlVariableDeclarationElement getLexicalDeclaration();

  /**
   * Looking for global variable declarations sutable for current variable
   *
   * @return list of global declarations
   */
  List<PerlVariableDeclarationElement> getGlobalDeclarations();

  /**
   * Looking for globs, sutable for current variable
   *
   * @return list of globs
   */
  List<PerlGlobVariable> getRelatedGlobs();

  /**
   * Returns line number of element in the document
   *
   * @return list of globs
   */
  int getLineNumber();

  /**
   * Returns variable name
   *
   * @return variable name or null
   */
  String getName();

  /**
   * Checks if this occurance is declaration
   *
   * @return true if it's declaration
   */
  boolean isDeclaration();

  /**
   * @return original or braced name if it's non-single char control variable
   */
  @NotNull
  static String braceName(@NotNull String variableName) {
    if (variableName.startsWith("^") && variableName.length() > 2 && variableName.charAt(1) != '{') {
      return "{" + variableName + "}";
    }
    return variableName;
  }

  /**
   * Semantic of this method is unclear
   */
  @Nullable
  static String adjustName(@Nullable String originalName, boolean forceShortMain) {
    if (originalName == null || !forceShortMain || !originalName.startsWith(PerlPackageUtil.MAIN_NAMESPACE_FULL)) {
      return originalName;
    }
    else {
      return originalName.substring(4);
    }
  }
}
