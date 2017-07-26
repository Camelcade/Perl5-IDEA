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
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.stubs.StubIndex;
import com.intellij.util.Processor;
import com.perl5.lang.perl.extensions.packageprocessor.PerlExportDescriptor;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.references.PerlSubReference;
import com.perl5.lang.perl.psi.stubs.subsdeclarations.PerlSubDeclarationIndex;
import com.perl5.lang.perl.psi.stubs.subsdeclarations.PerlSubDeclarationReverseIndex;
import com.perl5.lang.perl.psi.stubs.subsdefinitions.PerlLightSubDefinitionsIndex;
import com.perl5.lang.perl.psi.stubs.subsdefinitions.PerlLightSubDefinitionsReverseIndex;
import com.perl5.lang.perl.psi.stubs.subsdefinitions.PerlSubDefinitionReverseIndex;
import com.perl5.lang.perl.psi.stubs.subsdefinitions.PerlSubDefinitionsIndex;
import com.perl5.lang.perl.psi.utils.PerlSubArgument;
import com.perl5.lang.perl.util.processors.PerlImportsCollector;
import com.perl5.lang.perl.util.processors.PerlSubImportsCollector;
import gnu.trove.THashSet;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Created by hurricup on 19.04.2015.
 */
public class PerlSubUtil implements PerlElementTypes, PerlBuiltInSubs {
  public static final String SUB_AUTOLOAD = "AUTOLOAD";
  public static final String SUB_AUTOLOAD_WITH_PREFIX = PerlPackageUtil.PACKAGE_SEPARATOR + SUB_AUTOLOAD;

  /**
   * Checks if provided function is built in
   *
   * @param function function name
   * @return checking result
   */
  public static boolean isBuiltIn(String function) {
    return BUILT_IN.contains(function);
  }


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
    return PerlSubDefinitionReverseIndex.processSubDefinitionsInPackage(project, packageName, scope, processor) &&
           PerlLightSubDefinitionsReverseIndex.processSubDefinitionsInPackage(project, packageName, scope, processor);
  }

  public static boolean processSubDeclarationsInPackage(@NotNull Project project,
                                                        @NotNull String packageName,
                                                        @NotNull GlobalSearchScope scope,
                                                        @NotNull Processor<PerlSubDeclarationElement> processor) {
    return PerlSubDeclarationReverseIndex.processSubDeclarationsInPackage(project, packageName, scope, processor);
  }

  public static boolean processSubDefinitions(@NotNull Project project,
                                              @NotNull String canonicalName,
                                              @NotNull GlobalSearchScope scope,
                                              @NotNull Processor<PerlSubDefinitionElement> processor) {
    return PerlSubDefinitionsIndex.processSubDefinitions(project, canonicalName, scope, processor) &&
           PerlLightSubDefinitionsIndex.processSubDefinitions(project, canonicalName, scope, processor);
  }

  public static boolean processSubDeclarations(@NotNull Project project,
                                               @NotNull String canonicalName,
                                               @NotNull GlobalSearchScope scope,
                                               @NotNull Processor<PerlSubDeclarationElement> processor) {
    return PerlSubDeclarationIndex.processSubDeclarations(project, canonicalName, scope, processor);
  }


  /**
   * Returns list of defined subs names
   *
   * @param project project to search in
   * @return collection of sub names
   */
  public static Collection<String> getDefinedSubsNames(Project project) {
    // fixme honor scope
    Collection<String> result = StubIndex.getInstance().getAllKeys(PerlSubDefinitionsIndex.KEY, project);
    result.addAll(StubIndex.getInstance().getAllKeys(PerlLightSubDefinitionsIndex.KEY, project));
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
   * Returns list of declared subs names
   *
   * @param project project to search in
   * @return collection of sub names
   */
  public static Collection<String> getDeclaredSubsNames(Project project) {
    // fixme honor scope
    return StubIndex.getInstance().getAllKeys(PerlSubDeclarationIndex.KEY, project);
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
   * Detects return value of method container
   *
   * @param methodContainer method container inspected
   * @return package name or null
   */
  @Nullable
  public static String getMethodReturnValue(PerlMethodContainer methodContainer) {
    if (methodContainer instanceof PerlSmartMethodContainer) {
      return ((PerlSmartMethodContainer)methodContainer).getReturnPackageName();
    }
    PerlMethod methodElement = methodContainer.getMethod();
    if (methodElement == null) {
      return null;
    }
    PerlSubNameElement subNameElement = methodElement.getSubNameElement();
    if (subNameElement == null) {
      return null;
    }

    // fixme this should be moved to a method

    if ("new".equals(subNameElement.getName())) {
      return methodElement.getPackageName();
    }

    PsiReference reference = subNameElement.getReference();

    if (reference instanceof PerlSubReference) {
      for (ResolveResult resolveResult : ((PerlSubReference)reference).multiResolve(false)) {
        PsiElement targetElement = resolveResult.getElement();
        if (targetElement instanceof PerlSub) {
          String returnType = ((PerlSub)targetElement).getReturns(subNameElement.getPackageName(),
                                                                  methodContainer.getCallArgumentsList());
          if (returnType != null) {
            return returnType;
          }
        }
      }
    }

    return null;
  }

  /**
   * Returns a list of imported descriptors
   *
   * @param namespaceDefinitionElement element to start looking from
   * @return result map
   */
  @NotNull
  public static List<PerlExportDescriptor> getImportedSubsDescriptors(@NotNull PerlNamespaceDefinitionElement namespaceDefinitionElement) {
    PerlImportsCollector collector = new PerlSubImportsCollector();
    PerlUtil.processImportedEntities(namespaceDefinitionElement, collector);
    return collector.getResult();
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

    String argumentListString = StringUtils.join(argumentsList, ", ");
    String optionalArgumentsString = StringUtils.join(optionalAargumentsList, ", ");

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


  @NotNull
  public static List<PerlSubElement> collectOverridingSubs(PerlSubElement subBase) {
    return collectOverridingSubs(subBase, new THashSet<>());
  }

  @NotNull
  public static List<PerlSubElement> collectOverridingSubs(@NotNull PerlSubElement subBase, @NotNull Set<String> recursionSet) {
    List<PerlSubElement> result;
    result = new ArrayList<>();
    for (PerlSubElement directDescendant : subBase.getDirectOverridingSubs()) {
      String packageName = directDescendant.getPackageName();
      if (StringUtil.isNotEmpty(packageName) && !recursionSet.contains(packageName)) {
        recursionSet.add(packageName);
        result.add(directDescendant);
        result.addAll(collectOverridingSubs(directDescendant, recursionSet));
      }
    }

    return result;
  }

  @NotNull
  public static List<PsiElement> collectRelatedItems(@NotNull String canonicalName, @NotNull Project project) {
    final List<PsiElement> result = new ArrayList<>();
    processRelatedItems(canonicalName, project, element -> {
      result.add(element);
      return true;
    });
    return result;
  }

  public static void processRelatedItems(@NotNull String canonicalName,
                                         @NotNull Project project,
                                         @NotNull Processor<PsiElement> processor) {
    processRelatedItems(canonicalName, project, GlobalSearchScope.allScope(project), processor);
  }

  // fixme this should replace PerlSubReferenceResolver#collectRelatedItems
  public static void processRelatedItems(@NotNull String canonicalName,
                                         @NotNull Project project,
                                         @NotNull GlobalSearchScope searchScope,
                                         @NotNull Processor<PsiElement> processor) {
    for (PerlSubDefinitionElement target : PerlSubUtil.getSubDefinitions(project, canonicalName, searchScope)) {
      if (!processor.process(target)) {
        return;
      }
    }
    for (PerlSubDeclarationElement target : PerlSubUtil.getSubDeclarations(project, canonicalName, searchScope)) {
      if (!processor.process(target)) {
        return;
      }
    }
    for (PerlGlobVariable target : PerlGlobUtil.getGlobsDefinitions(project, canonicalName, searchScope)) {
      if (!processor.process(target)) {
        return;
      }
    }
  }
}
