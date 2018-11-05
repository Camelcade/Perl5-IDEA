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

import com.intellij.execution.filters.ConsoleDependentFilterProvider;
import com.intellij.execution.filters.Filter;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.openapi.project.Project;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.lang.perl.idea.execution.PerlRunConsole;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * Created by ELI-HOME on 21-Sep-15.
 * this filter provider allows us to add our own perl filter
 */
public class PerlConsoleFilterProvider extends ConsoleDependentFilterProvider {

  @NotNull
  @Override
  public Filter[] getDefaultFilters(@NotNull ConsoleView consoleView, @NotNull Project project, @NotNull GlobalSearchScope scope) {
    ArrayList<Filter> filters = ContainerUtil.newArrayList(new PerlConsoleFileLinkFilter(project));
    if (consoleView instanceof PerlRunConsole) {
      filters.add(new PerlAbsolutePathConsoleFilter(project, ((PerlRunConsole)consoleView)));
    }
    return filters.toArray(Filter.EMPTY_ARRAY);
  }
}
