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
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.stubs.StubIndex;
import com.intellij.util.Processor;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.PerlGlobVariable;
import com.perl5.lang.perl.psi.PerlSubDeclarationElement;
import com.perl5.lang.perl.psi.PerlSubDefinitionElement;
import com.perl5.lang.perl.psi.PerlSubElement;
import com.perl5.lang.perl.psi.references.PerlImplicitDeclarationsService;
import com.perl5.lang.perl.psi.stubs.globs.PerlGlobsStubIndex;
import com.perl5.lang.perl.psi.stubs.subsdeclarations.PerlSubDeclarationIndex;
import com.perl5.lang.perl.psi.stubs.subsdeclarations.PerlSubDeclarationReverseIndex;
import com.perl5.lang.perl.psi.stubs.subsdefinitions.PerlLightSubDefinitionsIndex;
import com.perl5.lang.perl.psi.stubs.subsdefinitions.PerlLightSubDefinitionsReverseIndex;
import com.perl5.lang.perl.psi.stubs.subsdefinitions.PerlSubDefinitionReverseIndex;
import com.perl5.lang.perl.psi.stubs.subsdefinitions.PerlSubDefinitionsIndex;
import com.perl5.lang.perl.psi.utils.PerlSubArgument;
import gnu.trove.THashSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;


public class PerlSubUtil implements PerlElementTypes {
  public static final String SUB_AUTOLOAD = "AUTOLOAD";
  public static final String SUB_DESTROY = "DESTROY";
  public static final String SUB_AUTOLOAD_WITH_PREFIX = PerlPackageUtil.NAMESPACE_SEPARATOR + SUB_AUTOLOAD;


  /**
   * Checks if sub defined as unary with ($) proto
   *
   * @param packageName package name
   * @param subName     sub name
   * @return check result
   */
  public static boolean isUnary(@Nullable String packageName, @NotNull String subName) {
    // todo implement checking
    return false;
  }

  /**
   * Checks if sub defined as unary with () proto
   *
   * @param packageName package name
   * @param subName     sub name
   * @return check result
   */
  public static boolean isArgumentless(@Nullable String packageName, @NotNull String subName) {
    // todo implement checking
    return false;
  }

  /**
   * Searching project files for sub definitions by specific package and function name
   *
   * @param project       project to search in
   * @param canonicalName canonical function name package::name
   * @return Collection of found definitions
   */
  public static Collection<PerlSubDefinitionElement> getSubDefinitions(@NotNull Project project,
                                                                       @Nullable String canonicalName) {
    return getSubDefinitions(project, canonicalName, GlobalSearchScope.allScope(project));
  }

  public static Collection<PerlSubDefinitionElement> getSubDefinitions(@NotNull Project project,
                                                                       @Nullable String canonicalName,
                                                                       @NotNull GlobalSearchScope scope) {
    if (canonicalName == null) {
      return Collections.emptyList();
    }
    List<PerlSubDefinitionElement> result = new ArrayList<>();
    processSubDefinitions(project, canonicalName, scope, result::add);
    return result;
  }

  public static Collection<PerlSubDefinitionElement> getSubDefinitionsInPackage(@NotNull Project project,
                                                                                @NotNull String packageName) {
    return getSubDefinitionsInPackage(project, packageName, GlobalSearchScope.allScope(project));
  }

  public static Collection<PerlSubDefinitionElement> getSubDefinitionsInPackage(@NotNull Project project,
                                                                                @NotNull String packageName,
                                                                                @NotNull GlobalSearchScope scope) {
    List<PerlSubDefinitionElement> result = new ArrayList<>();
    processSubDefinitionsInPackage(project, packageName, scope, result::add);
    return result;
  }

  public static boolean processSubDefinitionsInPackage(@NotNull Project project,
                                                       @NotNull String packageName,
                                                       @NotNull GlobalSearchScope scope,
                                                       @NotNull Processor<PerlSubDefinitionElement> processor) {
    return PerlImplicitDeclarationsService.getInstance(project).processSubsInPackage(packageName, processor) &&
           PerlSubDefinitionReverseIndex.getInstance().processElements(project, packageName, scope, processor) &&
           PerlLightSubDefinitionsReverseIndex.getInstance().processLightElements(project, packageName, scope, processor);
  }

  public static boolean processSubDeclarationsInPackage(@NotNull Project project,
                                                        @NotNull String packageName,
                                                        @NotNull GlobalSearchScope scope,
                                                        @NotNull Processor<PerlSubDeclarationElement> processor) {
    return PerlSubDeclarationReverseIndex.getInstance().processElements(project, packageName, scope, processor);
  }

  public static boolean processSubDefinitions(@NotNull Project project,
                                              @NotNull String canonicalName,
                                              @NotNull GlobalSearchScope scope,
                                              @NotNull Processor<PerlSubDefinitionElement> processor) {
    return PerlImplicitDeclarationsService.getInstance(project).processSubs(canonicalName, processor) &&
           PerlSubDefinitionsIndex.getInstance().processElements(project, canonicalName, scope, processor) &&
           PerlLightSubDefinitionsIndex.getInstance().processLightElements(project, canonicalName, scope, processor);
  }

  public static boolean processSubDeclarations(@NotNull Project project,
                                               @NotNull String canonicalName,
                                               @NotNull GlobalSearchScope scope,
                                               @NotNull Processor<PerlSubDeclarationElement> processor) {
    return PerlSubDeclarationIndex.getInstance().processElements(project, canonicalName, scope, processor);
  }


  /**
   * Returns list of defined subs names
   *
   * @param project project to search in
   * @return collection of sub names
   */
  public static Collection<String> getDefinedSubsNames(Project project) {
    // fixme honor scope
    Collection<String> result = PerlSubDefinitionsIndex.getInstance().getAllNames(project);
    result.addAll(PerlLightSubDefinitionsIndex.getInstance().getAllNames(project));
    return result;
  }

  /**
   * Searching project files for sub declarations by specific package and function name
   *
   * @param project       project to search in
   * @param canonicalName canonical function name package::name
   * @return Collection of found definitions
   */
  public static Collection<PerlSubDeclarationElement> getSubDeclarations(Project project, String canonicalName) {
    return getSubDeclarations(project, canonicalName, GlobalSearchScope.allScope(project));
  }

  public static Collection<PerlSubDeclarationElement> getSubDeclarations(Project project, String canonicalName, GlobalSearchScope scope) {
    if (canonicalName == null) {
      return Collections.emptyList();
    }
    return StubIndex.getElements(PerlSubDeclarationIndex.KEY, canonicalName, project, scope, PerlSubDeclarationElement.class);
  }

  /**
   * Processes all declared subs names with given processor
   *
   * @param project   project to search in
   * @param processor string processor for suitable strings
   * @return collection of constants names
   */
  public static boolean processDeclaredSubsNames(Project project, Processor<String> processor) {
    return StubIndex.getInstance().processAllKeys(PerlSubDeclarationIndex.KEY, project, processor);
  }

  /**
   * Builds arguments string for presentation
   *
   * @param subArguments list of arguments
   * @return stringified prototype
   */
  public static String getArgumentsListAsString(List<PerlSubArgument> subArguments) {
    int argumentsNumber = subArguments.size();

    List<String> argumentsList = new ArrayList<>();
    List<String> optionalAargumentsList = new ArrayList<>();

    for (PerlSubArgument argument : subArguments) {
      if (!optionalAargumentsList.isEmpty() || argument.isOptional()) {
        optionalAargumentsList.add(argument.toStringShort());
      }
      else {
        argumentsList.add(argument.toStringShort());
      }

      int compiledListSize = argumentsList.size() + optionalAargumentsList.size();
      if (compiledListSize > 5 && argumentsNumber > compiledListSize) {
        if (!optionalAargumentsList.isEmpty()) {
          optionalAargumentsList.add("...");
        }
        else {
          argumentsList.add("...");
        }
        break;
      }
    }

    if (argumentsList.isEmpty() && optionalAargumentsList.isEmpty()) {
      return "";
    }

    String argumentListString = StringUtil.join(argumentsList, ", ");
    String optionalArgumentsString = StringUtil.join(optionalAargumentsList, ", ");

    if (argumentListString.isEmpty()) {
      return "([" + optionalArgumentsString + "])";
    }
    if (optionalAargumentsList.isEmpty()) {
      return "(" + argumentListString + ")";
    }
    else {
      return "(" + argumentListString + " [, " + optionalArgumentsString + "])";
    }
  }


  public static @NotNull List<PerlSubElement> collectOverridingSubs(PerlSubElement subBase) {
    return collectOverridingSubs(subBase, new THashSet<>());
  }

  public static @NotNull List<PerlSubElement> collectOverridingSubs(@NotNull PerlSubElement subBase, @NotNull Set<String> recursionSet) {
    List<PerlSubElement> result;
    result = new ArrayList<>();
    for (PerlSubElement directDescendant : subBase.getDirectOverridingSubs()) {
      String packageName = directDescendant.getNamespaceName();
      if (StringUtil.isNotEmpty(packageName) && !recursionSet.contains(packageName)) {
        recursionSet.add(packageName);
        result.add(directDescendant);
        result.addAll(collectOverridingSubs(directDescendant, recursionSet));
      }
    }

    return result;
  }

  public static boolean processRelatedItems(@NotNull Project project,
                                            @NotNull GlobalSearchScope searchScope,
                                            @NotNull String canonicalName,
                                            @NotNull Processor<? super PsiNamedElement> processor) {
    if (!PerlSubUtil.processSubDefinitions(project, canonicalName, searchScope, processor::process)) {
      return false;
    }
    if (!PerlSubUtil.processSubDeclarations(project, canonicalName, searchScope, processor::process)) {
      return false;
    }
    for (PerlGlobVariable target : PerlGlobUtil.getGlobsDefinitions(project, canonicalName, searchScope)) {
      if (!processor.process(target)) {
        return false;
      }
    }
    return true;
  }

  public static boolean processRelatedSubsInPackage(@NotNull Project project,
                                                    @NotNull GlobalSearchScope searchScope,
                                                    @NotNull String packageName,
                                                    @NotNull Processor<? super PsiNamedElement> processor) {
    return processSubDefinitionsInPackage(project, packageName, searchScope, processor::process) &&
           processSubDeclarationsInPackage(project, packageName, searchScope, processor::process);
  }

  public static boolean processRelatedItemsInPackage(@NotNull Project project,
                                                     @NotNull GlobalSearchScope searchScope,
                                                     @NotNull String packageName,
                                                     @NotNull Processor<? super PsiNamedElement> processor) {
    return processRelatedSubsInPackage(project, searchScope, packageName, processor) &&
           PerlGlobsStubIndex.getInstance().processElements(project, "*" + packageName, searchScope, processor);
  }
}
