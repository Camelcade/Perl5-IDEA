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

import com.intellij.navigation.ChooseByNameContributor;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Condition;
import com.intellij.util.ArrayUtilRt;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.lang.perl.psi.impl.PerlImplicitElement;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

abstract class PerlChooseByNameContributor implements ChooseByNameContributor {
  private static final Condition<NavigationItem> FILTER = it -> !(it instanceof PerlImplicitElement);

  @Override
  public final String @NotNull [] getNames(Project project, boolean includeNonProjectItems) {
    return ArrayUtilRt.toStringArray(getNamesCollection(project, includeNonProjectItems));
  }

  protected abstract @NotNull Collection<String> getNamesCollection(Project project, boolean includeNonProjectItems);

  @Override
  public final NavigationItem @NotNull [] getItemsByName(String name, String pattern, Project project, boolean includeNonProjectItems) {
    return ContainerUtil.filter(getItemsCollectionByName(name, pattern, project, includeNonProjectItems), FILTER)
      .toArray(NavigationItem.EMPTY_NAVIGATION_ITEM_ARRAY);
  }

  protected abstract @NotNull Collection<? extends NavigationItem> getItemsCollectionByName(String name,
                                                                                            String pattern,
                                                                                            Project project,
                                                                                            boolean includeNonProjectItems);
}
