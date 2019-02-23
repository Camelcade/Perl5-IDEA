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

import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.ElementManipulators;
import com.intellij.psi.PsiElement;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.stubs.StubIndex;
import com.intellij.psi.stubs.StubIndexKey;
import com.intellij.util.Processor;
import com.intellij.util.SmartList;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.PerlString;
import com.perl5.lang.perl.psi.PerlStringContentElement;
import com.perl5.lang.perl.psi.PerlVariableDeclarationElement;
import com.perl5.lang.perl.psi.references.PerlImplicitDeclarationsService;
import com.perl5.lang.perl.psi.stubs.variables.PerlVariablesStubIndex;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by hurricup on 19.04.2015.
 */
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

  public static Collection<PerlVariableDeclarationElement> getGlobalScalarDefinitions(Project project,
                                                                                      @Nullable String canonicalName,
                                                                                      GlobalSearchScope scope) {
    if (canonicalName == null) {
      return Collections.emptyList();
    }
    List<PerlVariableDeclarationElement> result = new SmartList<>();
    processDefinedGlobalScalars(project, scope, it -> {
      if (canonicalName.equals(it.getCanonicalName())) {
        result.add(it);
      }
      return true;
    });
    return result;
  }


  /**
   * Returns list of defined global scalars
   *
   * @param project project to search in
   * @return collection of variable canonical names
   */
  public static Collection<String> getDefinedGlobalScalarNames(Project project) {
    return PerlUtil.getIndexKeysWithoutInternals(PerlVariablesStubIndex.KEY_SCALAR, project);
  }

  /**
   * Processes all global scalars with specific processor
   *
   * @param project   project to search in
   * @param processor string processor for suitable strings
   * @return collection of constants names
   */
  public static boolean processDefinedGlobalScalars(@NotNull Project project,
                                                    @NotNull GlobalSearchScope scope,
                                                    @NotNull Processor<PerlVariableDeclarationElement> processor) {
    return PerlImplicitDeclarationsService.getInstance(project).processScalars(processor) &&
           processDefinedGlobalVariables(PerlVariablesStubIndex.KEY_SCALAR, project, scope, processor);
  }

  /**
   * Method for processing global indexed variables
   *
   * @param key       stub index key
   * @param project   project
   * @param scope     scope to search
   * @param processor process to process
   * @return false if we should stop processing
   */
  public static boolean processDefinedGlobalVariables(
    @NotNull StubIndexKey<String, PerlVariableDeclarationElement> key,
    @NotNull Project project,
    @NotNull GlobalSearchScope scope,
    @NotNull Processor<PerlVariableDeclarationElement> processor) {
    StubIndex stubIndex = StubIndex.getInstance();
    for (String variableName : stubIndex.getAllKeys(key, project)) {
      if (variableName.length() == 0) {
        return true;
      }

      char firstChar = variableName.charAt(0);
      if (firstChar == '_' || Character.isLetterOrDigit(firstChar)) {
        if (!stubIndex.processElements(key, variableName, project, scope, PerlVariableDeclarationElement.class, element -> {
          ProgressManager.checkCanceled();
          return processor.process(element);
        })) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * Extracts value from the string element
   *
   * @param string psi element that may be StringElement or stringcontentElement
   * @return string content or null
   */
  @Nullable
  public static String getStringContent(@Nullable PsiElement string) {
    return string instanceof PerlString || string instanceof PerlStringContentElement ? ElementManipulators.getValueText(string) : null;
  }
}
