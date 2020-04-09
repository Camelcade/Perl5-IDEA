/*
 * Copyright 2015-2020 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.run.prove;

import com.intellij.execution.testframework.sm.runner.ui.SMTRunnerConsoleView;
import com.intellij.openapi.project.Project;
import com.perl5.lang.perl.idea.execution.PerlTerminalExecutionConsole;
import com.perl5.lang.perl.idea.sdk.host.PerlConsoleView;
import com.perl5.lang.perl.idea.sdk.host.PerlHostData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class PerlSMTRunnerConsoleView extends SMTRunnerConsoleView implements PerlConsoleView {
  private PerlHostData<?, ?> myHostData;

  public PerlSMTRunnerConsoleView(@NotNull Project project,
                                  @NotNull PerlSMTRunnerConsoleProperties consoleProperties,
                                  @Nullable String splitterProperty) {
    super(consoleProperties, splitterProperty);
    PerlTerminalExecutionConsole.addFiltersToConsole(project, this);
  }

  @NotNull
  public PerlSMTRunnerConsoleView withHostData(@Nullable PerlHostData<?, ?> hostData) {
    myHostData = hostData;
    return this;
  }

  @Nullable
  @Override
  public PerlHostData<?, ?> getHostData() {
    return myHostData;
  }
}
