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
import com.intellij.psi.ElementManipulators;
import com.intellij.psi.PsiElement;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.Processor;
import com.intellij.util.SmartList;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.PerlString;
import com.perl5.lang.perl.psi.PerlStringContentElement;
import com.perl5.lang.perl.psi.PerlVariableDeclarationElement;
import com.perl5.lang.perl.psi.references.PerlImplicitDeclarationsService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.perl5.lang.perl.psi.stubs.variables.PerlVariablesStubIndex.KEY_SCALAR;


public class PerlScalarUtil implements PerlElementTypes, PerlBuiltInScalars {
  public static final String DEFAULT_SELF_NAME = "self";
  public static final String DEFAULT_SELF_SCALAR_NAME = "$" + DEFAULT_SELF_NAME;

  /**
   * Searching project files for global scalar definitions by specific package and variable name
   *
   * @param project       project to search in
   * @param canonicalName canonical variable name package::name
   * @return Collection of found definitions
   */
  public static Collection<PerlVariableDeclarationElement> getGlobalScalarDefinitions(Project project, String canonicalName) {
    return getGlobalScalarDefinitions(project, canonicalName, GlobalSearchScope.allScope(project));
  }

  public static @NotNull Collection<PerlVariableDeclarationElement> getGlobalScalarDefinitions(@NotNull Project project,
                                                                                               @Nullable String canonicalName,
                                                                                               @NotNull GlobalSearchScope scope) {
    if (canonicalName == null) {
      return Collections.emptyList();
    }
    List<PerlVariableDeclarationElement> result = new SmartList<>();
    processDefinedGlobalScalars(project, scope, it -> {
      if (canonicalName.equals(it.getCanonicalName())) {
        result.add(it);
      }
      return true;
    }, true, null);
    return result;
  }


  /**
   * Returns list of defined global scalars
   *
   * @param project project to search in
   * @return collection of variable canonical names
   */
  public static Collection<String> getDefinedGlobalScalarNames(Project project) {
    return PerlStubUtil.getIndexKeysWithoutInternals(KEY_SCALAR, project);
  }

  /**
   * Processes all global scalars with specific processor
   *
   * @param processAll    if false, only one entry per name going to be processed. May be need when filling completion
   * @param namespaceName optional namespace filter
   */
  public static boolean processDefinedGlobalScalars(@NotNull Project project,
                                                    @NotNull GlobalSearchScope scope,
                                                    @NotNull Processor<PerlVariableDeclarationElement> processor,
                                                    boolean processAll,
                                                    @Nullable String namespaceName) {
    if (!PerlImplicitDeclarationsService.getInstance(project).processScalars(processor)) {
      return false;
    }
    return namespaceName == null ?
           PerlVariableUtil.processGlobalVariables(KEY_SCALAR, project, scope, processor, processAll) :
           PerlVariableUtil.processGlobalVariables(KEY_SCALAR, project, scope, processor, "*" + namespaceName, !processAll);
  }

  /**
   * Extracts value from the string element
   *
   * @param string psi element that may be StringElement or stringcontentElement
   * @return string content or null
   */
  public static @Nullable String getStringContent(@Nullable PsiElement string) {
    return string instanceof PerlString || string instanceof PerlStringContentElement ? ElementManipulators.getValueText(string) : null;
  }
}
