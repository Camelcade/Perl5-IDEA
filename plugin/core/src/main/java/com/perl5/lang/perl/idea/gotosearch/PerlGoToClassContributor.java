/*
 * Copyright 2015-2021 Alexandr Evstigneev
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

import com.intellij.navigation.GotoClassContributor;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.project.Project;
import com.intellij.psi.search.GlobalSearchScope;
import com.perl5.lang.perl.psi.properties.PerlIdentifierOwner;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;

/**
 * GoToClassContributor looks up namespaces (packages names) - windows shortcut Ctrl+N
 */
public class PerlGoToClassContributor extends PerlChooseByNameContributor implements GotoClassContributor {
  @Override
  public @Nullable String getQualifiedName(NavigationItem navigationItem) {
    return navigationItem instanceof PerlIdentifierOwner ? navigationItem.getName(): null;
  }

  @Override
  public @Nullable String getQualifiedNameSeparator() {
    return PerlPackageUtil.NAMESPACE_SEPARATOR;
  }

  @Override
  protected @NotNull Collection<String> getNamesCollection(Project project, boolean includeNonProjectItems) {
    return PerlPackageUtil.getKnownNamespaceNames(project);
  }

  @Override
  protected @NotNull Collection<? extends NavigationItem> getItemsCollectionByName(String packageName,
                                                                                   String pattern,
                                                                                   Project project,
                                                                                   boolean includeNonProjectItems) {
    if (PerlPackageUtil.MAIN_NAMESPACE_NAME.equals(packageName)) {
      return Collections.emptyList();
    }
    var searchScope = includeNonProjectItems ? GlobalSearchScope.allScope(project) : GlobalSearchScope.projectScope(project);
    return PerlPackageUtil.getNamespaceDefinitions(project, searchScope, packageName);
  }
}
