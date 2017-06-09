/*
 * Copyright 2015-2017 Alexandr Evstigneev
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
import com.perl5.lang.perl.psi.properties.PerlPackageMember;
import com.perl5.lang.perl.psi.properties.PerlVariableNameElementContainer;
import com.perl5.lang.perl.psi.utils.PerlVariableType;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by hurricup on 27.05.2015.
 */
public interface PerlVariable extends PsiPerlExpr, PerlLexicalScopeMember, PerlPackageMember, PerlVariableNameElementContainer {
  /**
   * Guesses variable type from definition or context (uses containing perl file to cache types)
   *
   * @return Package name if found
   */
  @Nullable
  String guessVariableType();

  /**
   * Guesses variable type from definition or context, using source
   *
   * @return Package name if found
   */
  @Nullable
  String getVariableTypeHeavy();

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
   * Checks if variable is configured $self
   *
   * @return true of false
   */
  boolean isSelf();

  /**
   * Checks if this occurance is declaration
   *
   * @return true if it's declaration
   */
  boolean isDeclaration();
}
