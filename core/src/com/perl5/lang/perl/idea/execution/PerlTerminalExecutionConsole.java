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

import com.intellij.openapi.project.Project;
import com.intellij.terminal.TerminalExecutionConsole;
import com.perl5.lang.perl.idea.execution.filters.PerlAbsolutePathConsoleFilter;
import com.perl5.lang.perl.idea.execution.filters.PerlConsoleFileLinkFilter;
import com.perl5.lang.perl.idea.sdk.host.PerlHostData;
import com.perl5.lang.perl.idea.sdk.host.PerlHostDataContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PerlTerminalExecutionConsole extends TerminalExecutionConsole implements PerlHostDataContainer<PerlTerminalExecutionConsole> {
  @Nullable
  private PerlHostData myHostData;

  private final Project myProject;

  public PerlTerminalExecutionConsole(@NotNull Project project) {
    super(project, null);
    myProject = project;
  }

  @Nullable
  public PerlHostData getHostData() {
    return myHostData;
  }

  @Override
  public PerlTerminalExecutionConsole withHostData(@Nullable PerlHostData hostData) {
    myHostData = hostData;
    if (myHostData != null) {
      addMessageFilter(new PerlConsoleFileLinkFilter(myProject, myHostData));
      addMessageFilter(new PerlAbsolutePathConsoleFilter(myProject, myHostData));
    }
    return this;
  }
}
