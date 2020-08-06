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

package run;

import base.PerlLightTestCaseBase;
import base.PerlPlatformTestCase;
import categories.Heavy;
import com.intellij.execution.executors.DefaultRunExecutor;
import com.intellij.execution.process.CapturingProcessAdapter;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.process.ProcessOutput;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.ui.RunContentDescriptor;
import com.intellij.openapi.util.Disposer;
import com.intellij.testFramework.UsefulTestCase;
import com.perl5.lang.perl.idea.run.GenericPerlRunConfiguration;
import com.pty4j.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(Heavy.class)
public class PerlRunTest extends PerlPlatformTestCase {
  private static final int MAX_RUNNING_TIME = 10_000;

  @Override
  protected String getBaseDataPath() {
    return "testData/run/run";
  }

  @Override
  protected String getResultsTestDataPath() {
    return super.getResultsTestDataPath() + "/answers";
  }

  @Test
  public void testRunSimpleScript() {
    copyDirToModule("simple");
    GenericPerlRunConfiguration runConfiguration = createRunConfiguration("simplescript.pl");
    Pair<ExecutionEnvironment, RunContentDescriptor> execResult;
    try {
      execResult = executeConfiguration(runConfiguration, DefaultRunExecutor.EXECUTOR_ID);
    }
    catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    RunContentDescriptor contentDescriptor = execResult.second;
    Disposer.register(myPerlLightTestCaseDisposable, contentDescriptor);
    CapturingProcessAdapter capturingProcessAdapter = new CapturingProcessAdapter();
    ProcessHandler processHandler = contentDescriptor.getProcessHandler();
    assertNotNull(processHandler);
    processHandler.addProcessListener(capturingProcessAdapter);
    if (!processHandler.waitFor(MAX_RUNNING_TIME)) {
      fail("Process failed to finish in time");
    }
    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(""), serializeOutput(capturingProcessAdapter.getOutput()));
  }

  private @NotNull String serializeOutput(@Nullable ProcessOutput processOutput) {
    if (processOutput == null) {
      return "null";
    }
    return "Exit code: " + processOutput.getExitCode() + PerlLightTestCaseBase.SEPARATOR_NEWLINES +
           "Stdout: " + processOutput.getStdout() + PerlLightTestCaseBase.SEPARATOR_NEWLINES +
           "Stderr: " + processOutput.getStderr();
  }
}
