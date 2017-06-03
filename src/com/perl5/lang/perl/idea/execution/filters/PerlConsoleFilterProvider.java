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

package com.perl5.lang.perl.idea.execution.filters;

import com.intellij.execution.filters.ConsoleFilterProviderEx;
import com.intellij.execution.filters.Filter;
import com.intellij.openapi.project.Project;
import com.intellij.psi.search.GlobalSearchScope;
import org.jetbrains.annotations.NotNull;

/**
 * Created by ELI-HOME on 21-Sep-15.
 * this filter provider allows us to add our own perl filter
 */
public class PerlConsoleFilterProvider implements ConsoleFilterProviderEx {
  @NotNull
  @Override
  public Filter[] getDefaultFilters(@NotNull Project project) {
    Filter filter = new PerlConsoleFileLinkFilter(project);
    return new Filter[]{filter};
  }

  @Override
  public Filter[] getDefaultFilters(@NotNull Project project, @NotNull GlobalSearchScope globalSearchScope) {
    return getDefaultFilters(project);
  }
}
