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

package com.perl5.lang.perl.idea.gotosearch;

import com.intellij.navigation.ChooseByNameContributor;
import com.intellij.navigation.GotoClassContributor;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.project.Project;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.ArrayUtil;
import com.perl5.lang.perl.psi.PerlNamespaceDefinitionElement;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * GoToClassContributor looks up namespaces (packages names) - windows shortcut Ctrl+N
 */
public class PerlGoToClassContributor implements ChooseByNameContributor, GotoClassContributor {
  @Nullable
  @Override
  public String getQualifiedName(NavigationItem navigationItem) {
    return navigationItem.getName();
  }

  @Nullable
  @Override
  public String getQualifiedNameSeparator() {
    return PerlPackageUtil.PACKAGE_SEPARATOR;
  }

  @NotNull
  @Override
  public String[] getNames(Project project, boolean b) {
    return ArrayUtil.toStringArray(PerlPackageUtil.getDefinedPackageNames(project));
  }

  @NotNull
  @Override
  public NavigationItem[] getItemsByName(String packageName, String searchTerm, Project project, boolean includeNonProjectItems) {
    Collection<PerlNamespaceDefinitionElement> result = PerlPackageUtil.getNamespaceDefinitions(
      project,
      packageName,
      (includeNonProjectItems ? GlobalSearchScope.allScope(project) : GlobalSearchScope.projectScope(project))
    );
    //noinspection SuspiciousToArrayCall
    return result.toArray(new NavigationItem[result.size()]);
  }
}
