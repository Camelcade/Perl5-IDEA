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

package com.perl5.lang.perl.util;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.CompositeElement;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.stubs.StubIndex;
import com.intellij.util.Processor;
import com.perl5.lang.perl.extensions.packageprocessor.PerlExportDescriptor;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.PerlVariableDeclarationElement;
import com.perl5.lang.perl.psi.PsiPerlCommaSequenceExpr;
import com.perl5.lang.perl.psi.PsiPerlParenthesisedExpr;
import com.perl5.lang.perl.psi.stubs.variables.PerlVariablesStubIndex;
import com.perl5.lang.perl.util.processors.PerlArrayImportsCollector;
import com.perl5.lang.perl.util.processors.PerlImportsCollector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Created by hurricup on 19.04.2015.
 */
public class PerlArrayUtil implements PerlElementTypes {
  public static final HashSet<String> BUILT_IN = new HashSet<String>(Arrays.asList(
    "+",
    "-",
    "_",
    "F",
    "ARG",
    "LAST_MATCH_END",
    "ARGV",
    "INC",
    "^CAPTURE",
    "LAST_MATCH_START"
  ));

  public static boolean isBuiltIn(String variable) {
    return BUILT_IN.contains(variable);
  }


  /**
   * Searching project files for global array definitions by specific package and variable name
   *
   * @param project       project to search in
   * @param canonicalName canonical variable name package::name
   * @return Collection of found definitions
   */
  public static Collection<PerlVariableDeclarationElement> getGlobalArrayDefinitions(Project project, String canonicalName) {
    return getGlobalArrayDefinitions(project, canonicalName, GlobalSearchScope.allScope(project));
  }

  public static Collection<PerlVariableDeclarationElement> getGlobalArrayDefinitions(Project project,
                                                                                     String canonicalName,
                                                                                     GlobalSearchScope scope) {
    if (canonicalName == null) {
      return Collections.emptyList();
    }
    return StubIndex.getElements(
      PerlVariablesStubIndex.KEY_ARRAY,
      canonicalName,
      project,
      scope,
      PerlVariableDeclarationElement.class
    );
  }

  /**
   * Returns list of defined global arrays
   *
   * @param project project to search in
   * @return collection of variable canonical names
   */
  public static Collection<String> getDefinedGlobalArrayNames(Project project) {
    return PerlUtil.getIndexKeysWithoutInternals(PerlVariablesStubIndex.KEY_ARRAY, project);
  }

  /**
   * Processes all global arrays names with specific processor
   *
   * @param project   project to search in
   * @param processor string processor for suitable strings
   * @return collection of constants names
   */
  public static boolean processDefinedGlobalArrays(@NotNull Project project,
                                                   @NotNull GlobalSearchScope scope,
                                                   @NotNull Processor<PerlVariableDeclarationElement> processor) {
    return PerlScalarUtil.processDefinedGlobalVariables(PerlVariablesStubIndex.KEY_ARRAY, project, scope, processor);
  }

  /**
   * Returns a map of imported arrays names
   *
   * @param rootElement element to start looking from
   * @return result map
   */
  @NotNull
  public static List<PerlExportDescriptor> getImportedArraysDescriptors(@NotNull PsiElement rootElement) {
    PerlImportsCollector collector = new PerlArrayImportsCollector();
    PerlUtil.processImportedEntities(rootElement, collector);
    return collector.getResult();
  }

  /**
   * Traversing PsiElement for composite elements, expanding lists and collecting all elements to the plain one: ($a, $b, ($c, $d)) -> $a, $b, $c, $d;
   *
   * @param rootElement top-level container
   * @param result      resulting array to fill
   * @return passed or new List of found PsiElements
   */
  public static List<PsiElement> getElementsAsPlainList(@Nullable PsiElement rootElement, @Nullable List<PsiElement> result) {
    if (result == null) {
      result = new ArrayList<>();
    }

    if (rootElement == null) {
      return result;
    }

    if (rootElement instanceof PsiPerlParenthesisedExpr || rootElement instanceof PsiPerlCommaSequenceExpr) {
      for (PsiElement childElement : rootElement.getChildren()) {
        getElementsAsPlainList(childElement, result);
      }
    }
    else if (rootElement.getNode() instanceof CompositeElement) {
      result.add(rootElement);
    }
    return result;
  }
}
