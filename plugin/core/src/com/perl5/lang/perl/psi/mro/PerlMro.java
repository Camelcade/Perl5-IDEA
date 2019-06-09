/*
 * Copyright 2015-2019 Alexandr Evstigneev
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

package com.perl5.lang.perl.psi.mro;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Ref;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.Processor;
import com.perl5.lang.perl.psi.PerlGlobVariable;
import com.perl5.lang.perl.psi.PerlNamespaceDefinitionElement;
import com.perl5.lang.perl.util.PerlGlobUtil;
import com.perl5.lang.perl.util.PerlPackageUtil;
import com.perl5.lang.perl.util.PerlSubUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static com.perl5.lang.perl.util.PerlSubUtil.SUB_AUTOLOAD;


public abstract class PerlMro {
  protected PerlMro() {
  }

  /**
   * Method should return a sequence of packages. See http://perldoc.perl.org/mro.html#mro%3a%3aget_linear_isa(%24classname%5b%2c-%24type%5d)
   * Method should not add package itself or UNIVERSAL, only parents structure. Package itself and UNIVERSAL being added by calee
   *
   * @param project      current project
   * @param namespaceDefinitions list of namespaces to check
   * @param recursionMap map for controlling recursive inheritance
   * @param result       list of package names to populate
   */
  public abstract void getLinearISA(@NotNull Project project,
                                    @NotNull List<PerlNamespaceDefinitionElement> namespaceDefinitions,
                                    @NotNull Set<String> recursionMap,
                                    @NotNull List<String> result);

  /**
   * Resolving method with current MRO;
   *
   * @return collection of first encountered super subs declarations, definitions, constants and typeglobs
   * @deprecated use {@link #processTargets(Project, GlobalSearchScope, String, Set, boolean, Processor)}
   */
  @NotNull
  public static Collection<PsiElement> resolveSub(@NotNull Project project,
                                                  @NotNull GlobalSearchScope searchScope,
                                                  @Nullable String namespaceName,
                                                  @Nullable String subName,
                                                  boolean isSuper) {
    if (subName == null) {
      return Collections.emptyList();
    }
    if (namespaceName == null) {
      namespaceName = PerlPackageUtil.NAMESPACE_PACKAGE;
    }
    Collection<PsiElement> result = new ArrayList<>();
    processTargets(project, searchScope, namespaceName, Collections.singleton(subName), isSuper, result::add);
    return result;
  }

  public static boolean processTargets(@NotNull Project project,
                                       @NotNull GlobalSearchScope searchScope,
                                       @NotNull String baseNamespaceName,
                                       @NotNull Set<String> subNames,
                                       boolean isSuper,
                                       @NotNull Processor<? super PsiNamedElement> processor) {
    Collection<String> linearISA = getLinearISA(project, searchScope, baseNamespaceName, isSuper);

    Ref<String> stopFlag = Ref.create();
    for (String currentNamespaceName : linearISA) {
      for (String subName : subNames) {
        if (!PerlSubUtil.processRelatedItems(project, searchScope, PerlPackageUtil.join(currentNamespaceName, subName), it -> {
          stopFlag.set("");
          return processor.process(it);
        })) {
          return false;
        }
      }
      if (!stopFlag.isNull()) {
        return true;
      }
    }

    for (String currentNamespaceName : linearISA) {
      if (PerlPackageUtil.isUNIVERSAL(currentNamespaceName)) {
        continue;
      }
      if (!PerlSubUtil.processRelatedItems(project, searchScope, PerlPackageUtil.join(currentNamespaceName, SUB_AUTOLOAD), it -> {
        processor.process(it);
        return false;
      })) {
        return false;
      }
    }

    return true;
  }

  /**
   * Returns collection of Sub Definitions of class and it's superclasses according perl's default MRO
   *
   * @param psiElement      anchorElement
   * @param baseNamespaceName base project
   * @param isSuper         flag for SUPER resolutions
   * @return collection of definitions
   */
  public static Collection<PsiElement> getVariants(@NotNull PsiElement psiElement,
                                                   @Nullable String baseNamespaceName,
                                                   boolean isSuper) {
    if (baseNamespaceName == null) {
      return Collections.emptyList();
    }
    Project project = psiElement.getProject();
    Map<String, PsiElement> methods = new HashMap<>();

    GlobalSearchScope searchScope = psiElement.getResolveScope();
    for (String packageName : getLinearISA(project, psiElement.getResolveScope(), baseNamespaceName, isSuper)) {
      PerlSubUtil.processRelatedSubsInPackage(project, searchScope, packageName, it -> {
        methods.putIfAbsent(it.getName(), it);
        return true;
      });
      for (PerlGlobVariable globVariable : PerlGlobUtil.getGlobsDefinitions(project, "*" + packageName)) {
        if (globVariable.isLeftSideOfAssignment() && !methods.containsKey(globVariable.getName())) {
          methods.putIfAbsent(globVariable.getName(), globVariable);
        }
      }
    }

    return new ArrayList<>(methods.values());
  }

  /**
   * Building linear @ISA list
   *
   * @param packageName current package name
   * @param isSuper     if false - we include current package into the list, true - otherwise
   * @return list of linear @ISA
   */
  public static ArrayList<String> getLinearISA(@NotNull Project project,
                                               @NotNull GlobalSearchScope searchScope,
                                               @NotNull String packageName,
                                               boolean isSuper) {
    HashSet<String> recursionMap = new HashSet<>();
    ArrayList<String> result = new ArrayList<>();

    if (!isSuper) {
      recursionMap.add(packageName);
      result.add(packageName);
    }

    getPackageParents(project, searchScope, packageName, recursionMap, result);

    if (!recursionMap.contains(PerlPackageUtil.NAMESPACE_PACKAGE)) {
      result.add(PerlPackageUtil.NAMESPACE_PACKAGE);
    }

    return result;
  }

  public static void getPackageParents(@NotNull Project project,
                                       @NotNull GlobalSearchScope searchScope,
                                       @NotNull String packageName,
                                       @NotNull Set<String> recursionMap,
                                       @NotNull List<String> result) {
    // at the moment we are checking all definitions available
    // fixme we should check only those, which are used in currrent file
    for (PerlNamespaceDefinitionElement namespaceDefinition : PerlPackageUtil.getNamespaceDefinitions(project, searchScope, packageName)) {
      namespaceDefinition.getLinearISA(recursionMap, result);
    }
  }
}
