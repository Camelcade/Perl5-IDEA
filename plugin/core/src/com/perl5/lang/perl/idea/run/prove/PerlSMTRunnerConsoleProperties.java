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

import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.testframework.sm.runner.SMTRunnerConsoleProperties;
import com.intellij.execution.testframework.sm.runner.SMTestLocator;
import com.perl5.lang.perl.idea.run.GenericPerlRunConfiguration;
import com.perl5.lang.perl.idea.sdk.host.PerlHostData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class PerlSMTRunnerConsoleProperties extends SMTRunnerConsoleProperties {
  public PerlSMTRunnerConsoleProperties(@NotNull RunConfiguration config,
                                        @NotNull String testFrameworkName,
                                        @NotNull Executor executor) {
    super(config, testFrameworkName, executor);
    setIdBasedTestTree(true);
  }

  @Override
  public @Nullable SMTestLocator getTestLocator() {
    try {
      return new PerlSMTestLocator(
        PerlHostData.notNullFrom(((GenericPerlRunConfiguration)getConfiguration()).getEffectiveSdk()));
    }
    catch (ExecutionException e) {
      return null;
    }
  }
}
