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

package com.perl5.lang.perl.idea.run.remote;

import com.intellij.execution.impl.ConsoleViewImpl;
import com.intellij.openapi.project.Project;
import com.perl5.lang.perl.idea.sdk.host.PerlHostData;
import com.perl5.lang.perl.idea.sdk.host.PerlHostDataProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PerlRunConsole extends ConsoleViewImpl implements PerlHostDataProvider {
  @Nullable
  private PerlHostData<?, ?> myHostData;

  public PerlRunConsole(@NotNull Project project) {
    super(project, true);
  }

  @Nullable
  @Override
  public PerlHostData<?, ?> getHostData() {
    return myHostData;
  }

  public void setHostData(@Nullable PerlHostData<?, ?> hostData) {
    myHostData = hostData;
  }
}
