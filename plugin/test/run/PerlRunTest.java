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

import base.PerlInterpreterConfigurator;
import base.PerlPlatformTestCase;
import categories.Heavy;
import com.intellij.execution.executors.DefaultRunExecutor;
import com.intellij.execution.process.CapturingProcessAdapter;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.ui.RunContentDescriptor;
import com.intellij.notification.Notification;
import com.intellij.openapi.util.Ref;
import com.intellij.testFramework.UsefulTestCase;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.idea.run.GenericPerlRunConfiguration;
import com.pty4j.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@SuppressWarnings("UnconstructableJUnitTestCase")
@Category(Heavy.class)
public class PerlRunTest extends PerlPlatformTestCase {
  public PerlRunTest(@NotNull PerlInterpreterConfigurator interpreterConfigurator) {
    super(interpreterConfigurator);
  }

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

  @Test
  public void testMissingPackageNotification() {
    copyDirToModule("missingpackage");
    Ref<Notification> notificationSink = createNotificationSink();
    runConfigurationAndWait(createOnlyRunConfiguration("simplescript.pl"));
    var notification = notificationSink.get();
    assertNotNull(notification);
    assertEquals(PerlBundle.message("perl.missing.library.notification"), notification.getGroupId());
    assertEquals(PerlBundle.message("perl.missing.library.notification.title", "Some::Missing::Module"), notification.getTitle());
    assertSize(2, notification.getActions());
    assertEquals(PerlBundle.message("perl.missing.library.notification.message"), notification.getContent());
  }

  private void runAndCompareOutput(GenericPerlRunConfiguration runConfiguration) {
    Pair<ExecutionEnvironment, RunContentDescriptor> execResult = runConfigurationAndWait(runConfiguration);
    CapturingProcessAdapter capturingProcessAdapter = getCapturingAdapter(execResult.first);
    assertNotNull(capturingProcessAdapter);
    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(""), serializeOutput(capturingProcessAdapter.getOutput()));
  }
}
