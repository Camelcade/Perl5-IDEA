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

package com.perl5.lang.perl.idea.sdk.host;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.process.ProcessOutput;
import org.jetbrains.annotations.NotNull;

public class PerlExecutionException extends ExecutionException {
  @NotNull
  private final ProcessOutput myProcessOutput;

  public PerlExecutionException(@NotNull ProcessOutput processOutput) {
    super("");
    myProcessOutput = processOutput;
  }

  @NotNull
  public ProcessOutput getProcessOutput() {
    return myProcessOutput;
  }

  @Override
  public String getMessage() {
    return "Non-zero exit code: " + myProcessOutput.getExitCode() +
           "; stderr: " + myProcessOutput.getStderr() +
           ": stdout: " + myProcessOutput.getStdout();
  }
}
