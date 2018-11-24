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

package com.perl5.lang.perl.idea.run.debugger;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.perl5.lang.perl.idea.run.PerlRunProfileState;
import org.jetbrains.annotations.NotNull;

public abstract class PerlDebugProfileStateBase extends PerlRunProfileState {
  private Integer myDebugPort;

  public PerlDebugProfileStateBase(ExecutionEnvironment environment) {
    super(environment);
  }

  public abstract String mapPathToRemote(@NotNull String localPath);

  @NotNull
  public abstract String mapPathToLocal(@NotNull String remotePath);

  public Integer getDebugPort() throws ExecutionException {
    if (myDebugPort == null) {
      myDebugPort = getDebugOptions().getDebugPort();
    }
    return myDebugPort;
  }

  public PerlDebugOptions getDebugOptions() {
    return (PerlDebugOptions)getEnvironment().getRunProfile();
  }
}
