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

package com.perl5.lang.perl.psi.mro;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.search.GlobalSearchScope;
import com.perl5.lang.perl.psi.PerlGlobVariable;
import com.perl5.lang.perl.psi.PerlNamespaceDefinitionElement;
import com.perl5.lang.perl.util.PerlGlobUtil;
import com.perl5.lang.perl.util.PerlPackageUtil;
import com.perl5.lang.perl.util.PerlSubUtil;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Created by hurricup on 08.08.2015.
 */
public abstract class PerlMro {
  protected PerlMro() {
  }

  /**
   * Method should return a sequence of packages. See http://perldoc.perl.org/mro.html#mro%3a%3aget_linear_isa(%24classname%5b%2c-%24type%5d)
   * Method should not add package itself or UNIVERSAL, only parents structure. Package itself and UNIVERSAL being added by calee
   *
   * @param project      current project
   * @param packageNames list of namespaces to check
   * @param recursionMap map for controlling recursive inheritance
   * @param result       list of package names to populate
   */
  public abstract void getLinearISA(Project project,
                                    List<PerlNamespaceDefinitionElement> packageNames,
                                    HashSet<String> recursionMap,
                                    ArrayList<String> result);

  /**
   * Resolving method with current MRO;
   *
   * @param project     current Project
   * @param packageName package name
   * @param subName     sub name
   * @param isSuper     super flag
   * @return collection of first encountered super subs declarations, definitions, constants and typeglobs
   */
  @NotNull
  public static Collection<PsiElement> resolveSub(@NotNull Project project, String packageName, String subName, boolean isSuper) {
    Collection<PsiElement> result = new ArrayList<>();
    if (subName == null) {
      return result;
    }

    if (packageName == null) {
      packageName = PerlPackageUtil.UNIVERSAL_PACKAGE;
    }

    Collection<String> linearISA = getLinearISA(project, packageName, isSuper);

    for (String currentPackageName : linearISA) {
      collectEntities(result, project, currentPackageName + PerlPackageUtil.PACKAGE_SEPARATOR + subName);

      if (!result.isEmpty()) {
        break;
      }
    }


    if (result.isEmpty()) {
      for (String currentPackageName : linearISA) {
        if (!PerlPackageUtil.isUNIVERSAL(currentPackageName)) // ignoring UNIVERSAL::AUTOLOAD
        {
          collectEntities(result, project, currentPackageName + PerlPackageUtil.PACKAGE_SEPARATOR + "AUTOLOAD");

          if (!result.isEmpty()) {
            break;
          }
        }
      }
    }

    return result;
  }

  protected static void collectEntities(Collection<PsiElement> result, Project project, String fullName) {
    result.addAll(PerlSubUtil.getSubDefinitions(project, fullName));
    result.addAll(PerlSubUtil.getSubDeclarations(project, fullName));
    result.addAll(PerlGlobUtil.getGlobsDefinitions(project, fullName));
  }

  /**
   * Returns collection of Sub Definitions of class and it's superclasses according perl's default MRO
   *
   * @param psiElement      anchorElement
   * @param basePackageName base project
   * @param isSuper         flag for SUPER resolutions
   * @return collection of definitions
   */
  public static Collection<PsiElement> getVariants(@NotNull PsiElement psiElement, String basePackageName, boolean isSuper) {
    Project project = psiElement.getProject();
    HashMap<String, PsiElement> methods = new HashMap<>();

    if (basePackageName != null) {
      GlobalSearchScope searchScope = psiElement.getResolveScope();
      for (String packageName : getLinearISA(project, basePackageName, isSuper)) {
        PerlSubUtil.processSubDefinitionsInPackage(project, packageName, searchScope, subDefinition -> {
          if (!methods.containsKey(subDefinition.getSubName())) {
            methods.put(subDefinition.getSubName(), subDefinition);
          }
          return true;
        });
        PerlSubUtil.processSubDeclarationsInPackage(project, packageName, searchScope, subDeclaration -> {
          if (!methods.containsKey(subDeclaration.getSubName())) {
            methods.put(subDeclaration.getSubName(), subDeclaration);
          }
          return true;
        });

        for (PerlGlobVariable globVariable : PerlGlobUtil.getGlobsDefinitions(project, "*" + packageName)) {
          if (globVariable.isLeftSideOfAssignment() && !methods.containsKey(globVariable.getName())) {
            methods.put(globVariable.getName(), globVariable);
          }
        }
      }
    }

    return new ArrayList<>(methods.values());
  }

  /**
   * Building linear @ISA list
   *
   * @param project     current project
   * @param packageName current package name
   * @param isSuper     if false - we include current package into the list, true - otherwise
   * @return list of linear @ISA
   */
  public static ArrayList<String> getLinearISA(Project project, @NotNull String packageName, boolean isSuper) {
    HashSet<String> recursionMap = new HashSet<>();
    ArrayList<String> result = new ArrayList<>();

    if (!isSuper) {
      recursionMap.add(packageName);
      result.add(packageName);
    }

    getPackageParents(project, packageName, recursionMap, result);

    if (!recursionMap.contains(PerlPackageUtil.UNIVERSAL_PACKAGE)) {
      result.add(PerlPackageUtil.UNIVERSAL_PACKAGE);
    }

    return result;
  }

  public static void getPackageParents(Project project,
                                       @NotNull String packageName,
                                       HashSet<String> recursionMap,
                                       ArrayList<String> result) {
    // at the moment we are checking all definitions available
    // fixme we should check only those, which are used in currrent file
    for (PerlNamespaceDefinitionElement namespaceDefinition : PerlPackageUtil.getNamespaceDefinitions(project, packageName)) {
      namespaceDefinition.getLinearISA(recursionMap, result);
    }
  }
}
