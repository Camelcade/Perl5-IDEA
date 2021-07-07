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

package com.perl5.lang.perl.debugger.run.run.debugger;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.util.Key;
import com.perl5.lang.perl.idea.run.PerlRunProfileState;
import com.perl5.lang.perl.idea.run.debugger.PerlDebugOptions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class PerlDebugProfileStateBase extends PerlRunProfileState {
  private static final Key<Boolean> READY_TO_CONNECT_KEY = Key.create("perl5.process.ready.for.connection");

  private Integer myDebugPort;

  public PerlDebugProfileStateBase(ExecutionEnvironment environment) {
    super(environment);
  }

  public abstract String mapPathToRemote(@NotNull String localPath);

  public abstract @NotNull String mapPathToLocal(@NotNull String remotePath);

  public Integer getDebugPort() throws ExecutionException {
    if (myDebugPort == null) {
      myDebugPort = getDebugOptions().getDebugPort();
    }
    return myDebugPort;
  }

  public PerlDebugOptions getDebugOptions() {
    return (PerlDebugOptions)getEnvironment().getRunProfile();
  }

  @Override
  public boolean isParallelRunAllowed() {
    return false;
  }

  /**
   * Marks passed {@code processHandler} as ready for connecting from IDE side
   *
   * @return passed {@code processHandler}
   */
  public static @NotNull ProcessHandler markAsReady(@NotNull ProcessHandler processHandler) {
    READY_TO_CONNECT_KEY.set(processHandler, Boolean.TRUE);
    return processHandler;
  }

  /**
   * @return true iff {@code processHandler} is null or marked as ready for connection
   * @see #markAsReady(ProcessHandler)
   */
  public static boolean isReadyForConnection(@Nullable ProcessHandler processHandler) {
    return processHandler == null || READY_TO_CONNECT_KEY.get(processHandler) != null;
  }
}
