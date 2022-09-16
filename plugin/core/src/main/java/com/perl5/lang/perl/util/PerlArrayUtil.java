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

package com.perl5.lang.perl.util;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.CompositeElement;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.util.Processor;
import com.intellij.util.SmartList;
import com.perl5.lang.perl.PerlParserDefinition;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.PerlStringList;
import com.perl5.lang.perl.psi.PerlVariableDeclarationElement;
import com.perl5.lang.perl.psi.PsiPerlCommaSequenceExpr;
import com.perl5.lang.perl.psi.PsiPerlParenthesisedExpr;
import com.perl5.lang.perl.psi.references.PerlImplicitDeclarationsService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static com.perl5.lang.perl.psi.stubs.variables.PerlVariablesStubIndex.KEY_ARRAY;


public class PerlArrayUtil implements PerlElementTypes {
  public static final HashSet<String> BUILT_IN = new HashSet<>(Arrays.asList(
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
                                                                                     @Nullable String canonicalName,
                                                                                     GlobalSearchScope scope) {
    if (canonicalName == null) {
      return Collections.emptyList();
    }
    List<PerlVariableDeclarationElement> result = new SmartList<>();
    processDefinedGlobalArrays(project, scope, it -> {
      if (canonicalName.equals(it.getCanonicalName())) {
        result.add(it);
      }
      return true;
    }, true, null);
    return result;
  }

  /**
   * Returns list of defined global arrays
   *
   * @param project project to search in
   * @return collection of variable canonical names
   */
  public static Collection<String> getDefinedGlobalArrayNames(Project project) {
    return PerlStubUtil.getIndexKeysWithoutInternals(KEY_ARRAY, project);
  }

  /**
   * Processes all global arrays names with specific processor
   *
   * @param processAll    if false, only one entry per name going to be processed. May be need when filling completion
   * @param namespaceName optional namespace filter
   */
  public static boolean processDefinedGlobalArrays(@NotNull Project project,
                                                   @NotNull GlobalSearchScope scope,
                                                   @NotNull Processor<PerlVariableDeclarationElement> processor,
                                                   boolean processAll,
                                                   @Nullable String namespaceName) {
    if (!PerlImplicitDeclarationsService.getInstance(project).processArrays(processor)) {
      return false;
    }
    return namespaceName == null ?
           PerlVariableUtil.processGlobalVariables(KEY_ARRAY, project, scope, processor, processAll) :
           PerlVariableUtil.processGlobalVariables(KEY_ARRAY, project, scope, processor, "*" + namespaceName, !processAll);
  }

  /**
   * Traversing root element, expanding lists and collecting all elements to the plain one:
   * ($a, $b, ($c, $d)), qw/bla bla/ -> $a, $b, $c, $d, bla, bla;
   *
   * @param rootElement top-level container or a single element
   * @return passed or new List of found PsiElements
   */
  public static @NotNull List<PsiElement> collectListElements(@Nullable PsiElement rootElement) {
    return collectListElements(rootElement, new ArrayList<>());
  }

  /**
   * @return list of all children of {@code parentElement} with flattened sequence expressions
   */
  public static @NotNull List<PsiElement> collectChildrenList(@Nullable PsiElement parentElement) {
    if (parentElement == null) {
      return Collections.emptyList();
    }
    PsiElement[] children = parentElement.getChildren();
    if (children.length == 0) {
      return Collections.emptyList();
    }
    List<PsiElement> result = new ArrayList<>();
    for (PsiElement child : children) {
      PerlArrayUtil.collectListElements(child, result);
    }
    return result;
  }

  public static @NotNull List<PsiElement> collectListElements(@Nullable PsiElement rootElement, @NotNull List<PsiElement> result) {
    if (rootElement == null || PerlParserDefinition.COMMENTS.contains(PsiUtilCore.getElementType(rootElement))) {
      return result;
    }

    if (rootElement instanceof PsiPerlParenthesisedExpr ||
        rootElement instanceof PsiPerlCommaSequenceExpr ||
        rootElement instanceof PerlStringList) {
      for (PsiElement childElement : rootElement.getChildren()) {
        collectListElements(childElement, result);
      }
    }
    else if (rootElement.getNode() instanceof CompositeElement) {
      result.add(rootElement);
    }
    return result;
  }
}
