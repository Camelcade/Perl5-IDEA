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
import com.perl5.lang.perl.psi.properties.PerlNamespaceElementContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Common interface for my/our/local/state declarations
 */
public interface PerlVariableDeclarationExpr extends PsiPerlExpr, PerlNamespaceElementContainer, PerlLexicalScopeMember {
  /**
   * Returns explicit declared type
   *
   * @return declaration type name or null
   */
  @Nullable
  String getDeclarationType();

  @NotNull
  Collection<PsiPerlArrayVariable> getArrayVariableList();

  @NotNull
  Collection<PsiPerlHashVariable> getHashVariableList();

  @NotNull
  Collection<PsiPerlScalarVariable> getScalarVariableList();

  @NotNull
  List<PsiPerlVariableDeclarationElement> getVariableDeclarationElementList();

  @NotNull
  default List<PerlVariable> getVariables() {
    return getVariableDeclarationElementList().stream()
      .map(PerlVariableDeclarationElement::getVariable)
      .filter(Objects::nonNull)
      .collect(Collectors.toList());
  }
}
