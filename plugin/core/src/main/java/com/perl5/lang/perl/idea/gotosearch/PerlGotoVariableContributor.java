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

import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.project.Project;
import com.intellij.psi.search.GlobalSearchScope;
import com.perl5.lang.perl.util.PerlArrayUtil;
import com.perl5.lang.perl.util.PerlGlobUtil;
import com.perl5.lang.perl.util.PerlHashUtil;
import com.perl5.lang.perl.util.PerlScalarUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;


public class PerlGotoVariableContributor extends PerlChooseByNameContributor {
  @Override
  protected @NotNull Collection<String> getNamesCollection(Project project, boolean includeNonProjectItems) {
    Collection<String> result = new ArrayList<>();

    for (String name : PerlScalarUtil.getDefinedGlobalScalarNames(project)) {
      result.add("$" + name);
    }

    for (String name : PerlArrayUtil.getDefinedGlobalArrayNames(project)) {
      result.add("@" + name);
    }

    for (String name : PerlHashUtil.getDefinedGlobalHashNames(project)) {
      result.add("%" + name);
    }

    for (String name : PerlGlobUtil.getDefinedGlobsNames(project)) {
      result.add("*" + name);
    }

    return result;
  }

  @Override
  protected @NotNull Collection<? extends NavigationItem> getItemsCollectionByName(String name,
                                                                                   String pattern,
                                                                                   Project project,
                                                                                   boolean includeNonProjectItems) {
    if (name.length() <= 0) {
      return Collections.emptyList();
    }
    GlobalSearchScope scope =
      includeNonProjectItems ? GlobalSearchScope.allScope(project) : GlobalSearchScope.projectScope(project);

    char firstChar = name.charAt(0);

    if (firstChar == '$') {
      return PerlScalarUtil.getGlobalScalarDefinitions(project, name.substring(1), scope);
    }
    else if (firstChar == '@') {
      return PerlArrayUtil.getGlobalArrayDefinitions(project, name.substring(1), scope);
    }
    else if (firstChar == '%') {
      return PerlHashUtil.getGlobalHashDefinitions(project, name.substring(1), scope);
    }
    else if (firstChar == '*') {
      return PerlGlobUtil.getGlobsDefinitions(project, name.substring(1), scope);
    }
    return Collections.emptyList();
  }
}
