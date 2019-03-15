/*
 * Copyright 2015-2018 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.execution;

import com.intellij.execution.filters.ConsoleDependentFilterProvider;
import com.intellij.execution.filters.ConsoleFilterProvider;
import com.intellij.execution.filters.ConsoleFilterProviderEx;
import com.intellij.execution.filters.Filter;
import com.intellij.execution.impl.ConsoleViewImpl;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.openapi.project.Project;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.terminal.TerminalExecutionConsole;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.lang.perl.idea.sdk.host.PerlHostData;
import com.perl5.lang.perl.idea.sdk.host.PerlHostDataContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PerlTerminalExecutionConsole extends TerminalExecutionConsole implements PerlHostDataContainer<PerlTerminalExecutionConsole> {
  @Nullable
  private PerlHostData myHostData;

  public PerlTerminalExecutionConsole(@NotNull Project project) {
    super(project, null);
    computeConsoleFilters(project, GlobalSearchScope.allScope(project), this).forEach(this::addMessageFilter);
  }

  @Nullable
  public PerlHostData getHostData() {
    return myHostData;
  }

  @Override
  public PerlTerminalExecutionConsole withHostData(@Nullable PerlHostData hostData) {
    myHostData = hostData;
    return this;
  }

  /**
   * This is a copy-paste of {@link ConsoleViewImpl#computeConsoleFilters(com.intellij.openapi.project.Project, com.intellij.psi.search.GlobalSearchScope)}
   * Must be fixed in the platform
   */
  @NotNull
  public static List<Filter> computeConsoleFilters(@NotNull Project project,
                                                   @NotNull GlobalSearchScope searchScope,
                                                   @NotNull ConsoleView console) {
    List<Filter> result = new ArrayList<>();
    for (ConsoleFilterProvider eachProvider : ConsoleFilterProvider.FILTER_PROVIDERS.getExtensions()) {
      Filter[] filters;
      if (eachProvider instanceof ConsoleDependentFilterProvider) {
        filters = ((ConsoleDependentFilterProvider)eachProvider).getDefaultFilters(console, project, searchScope);
      }
      else if (eachProvider instanceof ConsoleFilterProviderEx) {
        filters = ((ConsoleFilterProviderEx)eachProvider).getDefaultFilters(project, searchScope);
      }
      else {
        filters = eachProvider.getDefaultFilters(project);
      }
      ContainerUtil.addAll(result, filters);
    }
    return result;
  }
}
