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

package com.perl5.lang.perl.psi;

import com.perl5.lang.perl.psi.impl.PerlUseStatementElement;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * Data collected from the perl file's AST-based PSI
 */
public class PerlFileData {
  public static final PerlFileData EMPTY_DATA = new PerlFileData(
    Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), Collections.emptyList());

  private final @NotNull List<PerlNamespaceDefinitionElement> myNamespaces;
  private final @NotNull List<PerlUseStatementElement> myUseStatements;
  private final @NotNull List<PerlVariableDeclarationElement> myGlobalVariables;
  private final @NotNull List<PerlGlobVariable> myTypeGlobs;

  public PerlFileData(@NotNull List<PerlNamespaceDefinitionElement> namespaces,
                      @NotNull List<PerlUseStatementElement> useStatements,
                      @NotNull List<PerlVariableDeclarationElement> globalVariables,
                      @NotNull List<PerlGlobVariable> typeGlobs
  ) {
    myNamespaces = namespaces.isEmpty() ? Collections.emptyList() : List.copyOf(namespaces);
    myUseStatements = useStatements.isEmpty() ? Collections.emptyList() : List.copyOf(useStatements);
    myGlobalVariables = globalVariables.isEmpty() ? Collections.emptyList() : List.copyOf(globalVariables);
    myTypeGlobs = typeGlobs.isEmpty() ? Collections.emptyList() : List.copyOf(typeGlobs);
  }

  public @NotNull List<PerlNamespaceDefinitionElement> getNamespaces() {
    return myNamespaces;
  }

  public @NotNull List<PerlUseStatementElement> getUseStatements() {
    return myUseStatements;
  }

  public @NotNull List<PerlGlobVariable> getTypeGlobs() {
    return myTypeGlobs;
  }

  public @NotNull List<PerlVariableDeclarationElement> getGlobalVariables() {
    return myGlobalVariables;
  }
}
