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
import com.perl5.lang.perl.PerlScopes;
import com.perl5.lang.perl.extensions.packageprocessor.PerlExportDescriptor;
import com.perl5.lang.perl.idea.stubs.subsdeclarations.PerlSubDeclarationStubIndex;
import com.perl5.lang.perl.idea.stubs.subsdefinitions.PerlSubDefinitionsStubIndex;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.mro.PerlMro;
import com.perl5.lang.perl.psi.references.PerlSubReference;
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
  public static Collection<PerlSubDefinitionBase> getSubDefinitions(Project project, String canonicalName) {
    return getSubDefinitions(project, canonicalName, PerlScopes.getProjectAndLibrariesScope(project));
  }

  public static Collection<PerlSubDefinitionBase> getSubDefinitions(Project project, String canonicalName, GlobalSearchScope scope) {
    if (canonicalName == null) {
      return Collections.emptyList();
    }
    return StubIndex.getElements(PerlSubDefinitionsStubIndex.KEY, canonicalName, project, scope, PerlSubDefinitionBase.class);
  }

  /**
   * Returns list of defined subs names
   *
   * @param project project to search in
   * @return collection of sub names
   */
  public static Collection<String> getDefinedSubsNames(Project project) {
    return PerlUtil.getIndexKeysWithoutInternals(PerlSubDefinitionsStubIndex.KEY, project);
  }

  /**
   * Processes all defined subs names with given processor
   *
   * @param project   project to search in
   * @param processor string processor for suitable strings
   * @return collection of constants names
   */
  public static boolean processDefinedSubsNames(Project project, Processor<String> processor) {
    return StubIndex.getInstance().processAllKeys(PerlSubDefinitionsStubIndex.KEY, project, processor);
  }

  /**
   * Searching project files for sub declarations by specific package and function name
   *
   * @param project       project to search in
   * @param canonicalName canonical function name package::name
   * @return Collection of found definitions
   */
  public static Collection<PsiPerlSubDeclaration> getSubDeclarations(Project project, String canonicalName) {
    return getSubDeclarations(project, canonicalName, PerlScopes.getProjectAndLibrariesScope(project));
  }

  public static Collection<PsiPerlSubDeclaration> getSubDeclarations(Project project, String canonicalName, GlobalSearchScope scope) {
    if (canonicalName == null) {
      return Collections.emptyList();
    }
    return StubIndex.getElements(PerlSubDeclarationStubIndex.KEY, canonicalName, project, scope, PsiPerlSubDeclaration.class);
  }

  /**
   * Returns list of declared subs names
   *
   * @param project project to search in
   * @return collection of sub names
   */
  public static Collection<String> getDeclaredSubsNames(Project project) {
    return PerlUtil.getIndexKeysWithoutInternals(PerlSubDeclarationStubIndex.KEY, project);
  }

  /**
   * Processes all declared subs names with given processor
   *
   * @param project   project to search in
   * @param processor string processor for suitable strings
   * @return collection of constants names
   */
  public static boolean processDeclaredSubsNames(Project project, Processor<String> processor) {
    return StubIndex.getInstance().processAllKeys(PerlSubDeclarationStubIndex.KEY, project, processor);
  }

  /**
   * Detects return value of method container
   *
   * @param methodContainer method container inspected
   * @return package name or null
   */
  @Nullable
  public static String getMethodReturnValue(PerlMethodContainer methodContainer) {
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
        if (targetElement instanceof PerlSubDefinitionBase && ((PerlSubDefinitionBase)targetElement).getReturns() != null) {
          return ((PerlSubDefinitionBase)targetElement).getReturns();
        }
        else if (targetElement instanceof PerlSubDeclaration && ((PerlSubDeclaration)targetElement).getReturns() != null) {
          return ((PerlSubDeclaration)targetElement).getReturns();
        }
      }
    }

    return null;
  }

  /**
   * Returns a list of imported descriptors
   *
   * @param rootElement element to start looking from
   * @return result map
   */
  @NotNull
  public static List<PerlExportDescriptor> getImportedSubsDescriptors(@NotNull PsiElement rootElement) {
    PerlImportsCollector collector = new PerlSubImportsCollector();
    PerlUtil.processImportedEntities(rootElement, collector);
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

    List<String> argumentsList = new ArrayList<String>();
    List<String> optionalAargumentsList = new ArrayList<String>();

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
  public static PerlSubBase getTopLevelSuperMethod(@NotNull PerlSubBase subBase) {
    return getTopLevelSuperMethod(subBase, new THashSet<String>());
  }

  /**
   * Recursively collecting superMethods
   *
   * @param subBase        base method to search
   * @param classRecursion class recursion set
   * @return empty list if we've already been in this class, or list of topmost methods
   */
  @NotNull
  private static PerlSubBase getTopLevelSuperMethod(@NotNull PerlSubBase subBase, Set<String> classRecursion) {
    String packageName = subBase.getPackageName();

    if (packageName == null || classRecursion.contains(packageName)) {
      return subBase;
    }

    classRecursion.add(packageName);

    PerlSubBase directSuperMethod = getDirectSuperMethod(subBase);
    return directSuperMethod == null ? subBase : getTopLevelSuperMethod(directSuperMethod, classRecursion);
  }

  @Nullable
  public static PerlSubBase getDirectSuperMethod(PerlSubBase subBase) {
    if (!subBase.isMethod()) {
      return null;
    }

    Collection<PsiElement> resolveTargets = PerlMro.resolveSub(
      subBase.getProject(),
      subBase.getPackageName(),
      subBase.getSubName(),
      true
    );

    for (PsiElement resolveTarget : resolveTargets) {
      if (resolveTarget instanceof PerlSubBase) {
        return (PerlSubBase)resolveTarget;
      }
    }
    return null;
  }

  @NotNull
  public static List<PerlSubBase> collectOverridingSubs(PerlSubBase subBase) {
    return collectOverridingSubs(subBase, new THashSet<String>());
  }

  @NotNull
  public static List<PerlSubBase> collectOverridingSubs(@NotNull PerlSubBase subBase, @NotNull Set<String> recursionSet) {
    List<PerlSubBase> result = new ArrayList<PerlSubBase>();
    for (PerlSubBase directDescendant : getDirectOverridingSubs(subBase)) {
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
  public static List<PerlSubBase> getDirectOverridingSubs(@NotNull PerlSubBase subBase) {
    PerlNamespaceDefinition containingNamespace = PerlPackageUtil.getContainingNamespace(subBase);

    return containingNamespace == null ? Collections.<PerlSubBase>emptyList() : getDirectOverridingSubs(subBase, containingNamespace);
  }

  @NotNull
  public static List<PerlSubBase> getDirectOverridingSubs(@NotNull final PerlSubBase subBase,
                                                          @NotNull PerlNamespaceDefinition containingNamespace) {
    final List<PerlSubBase> overridingSubs = new ArrayList<PerlSubBase>();
    final String subName = subBase.getSubName();

    PerlPackageUtil.processChildNamespacesSubs(containingNamespace, null, new Processor<PerlSubBase>() {
      @Override
      public boolean process(PerlSubBase overridingSub) {
        String overridingSubName = overridingSub.getSubName();
        if (StringUtil.equals(overridingSubName, subName) && subBase == getDirectSuperMethod(overridingSub)) {
          overridingSubs.add(overridingSub);
          return false;
        }
        return true;
      }
    });

    return overridingSubs;
  }

  @NotNull
  public static List<PsiElement> collectRelatedItems(@NotNull String canonicalName, @NotNull Project project) {
    final List<PsiElement> result = new ArrayList<PsiElement>();
    processRelatedItems(canonicalName, project, new Processor<PsiElement>() {
      @Override
      public boolean process(PsiElement element) {
        result.add(element);
        return true;
      }
    });
    return result;
  }

  public static void processRelatedItems(@NotNull String canonicalName,
                                         @NotNull Project project,
                                         @NotNull Processor<PsiElement> processor) {
    processRelatedItems(canonicalName, project, PerlScopes.getProjectAndLibrariesScope(project), processor);
  }

  // fixme this should replace PerlSubReferenceResolver#collectRelatedItems
  public static void processRelatedItems(@NotNull String canonicalName,
                                         @NotNull Project project,
                                         @NotNull GlobalSearchScope searchScope,
                                         @NotNull Processor<PsiElement> processor) {
    for (PerlSubDefinitionBase target : PerlSubUtil.getSubDefinitions(project, canonicalName, searchScope)) {
      if (!processor.process(target)) {
        return;
      }
    }
    for (PsiPerlSubDeclaration target : PerlSubUtil.getSubDeclarations(project, canonicalName, searchScope)) {
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
