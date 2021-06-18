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

package run;

import base.PerlPlatformTestCase;
import categories.Heavy;
import com.intellij.execution.executors.DefaultRunExecutor;
import com.intellij.execution.process.CapturingProcessAdapter;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.ui.RunContentDescriptor;
import com.intellij.testFramework.UsefulTestCase;
import com.perl5.lang.perl.idea.run.GenericPerlRunConfiguration;
import com.pty4j.util.Pair;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(Heavy.class)
public class PerlRunTest extends PerlPlatformTestCase {
  @Override
  protected String getBaseDataPath() {
    return "testData/run/run";
  }

  @Override
  protected String getResultsTestDataPath() {
    return super.getResultsTestDataPath() + "/answers";
  }

  @Test
  public void testArgsPassing() {
    copyDirToModule("argsInspector");
    var runConfiguration = createOnlyRunConfiguration("script.pl");
    assertNotNull(runConfiguration);
    assertInstanceOf(runConfiguration, GenericPerlRunConfiguration.class);
    runConfiguration.setProgramParameters(
      "arg1 \"arg2 arg3\" \"arg4\\\"arg5\" \"arg6'arg7\" 'arg8 arg9' \"'arg10 arg11'\" \"arg12 arg13\" \\\"arg14 arg15\\\"");
    runAndCompareOutput(runConfiguration);
  }

  @Test
  public void testRunTestDir() {
    copyDirToModule("testMore");
    runTestConfigurationWithExecutorAndCheckResultsWithFile(createTestRunConfiguration("t"), DefaultRunExecutor.EXECUTOR_ID);
  }

  @Test
  public void testRunSimpleScript() {
    copyDirToModule("simple");
    runAndCompareOutput(createOnlyRunConfiguration("simplescript.pl"));
  }

  private void runAndCompareOutput(GenericPerlRunConfiguration runConfiguration) {
    Pair<ExecutionEnvironment, RunContentDescriptor> execResult;
    try {
      execResult = executeConfiguration(runConfiguration, DefaultRunExecutor.EXECUTOR_ID);
    }
    catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    RunContentDescriptor contentDescriptor = execResult.second;
    ProcessHandler processHandler = contentDescriptor.getProcessHandler();
    assertNotNull(processHandler);
    waitForProcessFinish(processHandler);
    CapturingProcessAdapter capturingProcessAdapter = getCapturingAdapter(execResult.first);
    assertNotNull(capturingProcessAdapter);
    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(""), serializeOutput(capturingProcessAdapter.getOutput()));
  }
}
