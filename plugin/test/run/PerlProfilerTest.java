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

import base.PerlPlatformTestCase;
import categories.Heavy;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.RunProfile;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.ui.RunContentDescriptor;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.profiler.*;
import com.intellij.profiler.ProfilerProcessPanel;
import com.intellij.profiler.api.*;
import com.intellij.profiler.api.configurations.ProfilerConfigurationState;
import com.intellij.profiler.api.configurations.ProfilerRunConfigurationsManager;
import com.intellij.testFramework.UsefulTestCase;
import com.perl5.lang.perl.idea.run.GenericPerlRunConfiguration;
import com.perl5.lang.perl.profiler.configuration.PerlProfilerConfigurationState;
import com.perl5.lang.perl.profiler.run.PerlProfilerProcess;
import com.perl5.lang.perl.profiler.run.PerlProfilerStartupMode;
import com.pty4j.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.jdom.Element;
import org.junit.experimental.categories.Category;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

@Category(Heavy.class)
public class PerlProfilerTest extends PerlPlatformTestCase {
  private Element myConfigurations;

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    myConfigurations = ProfilerRunConfigurationsManager.getInstance().getState();
  }

  @Override
  protected void tearDown() throws Exception {
    try {
      ProfilerRunConfigurationsManager.getInstance().loadState(myConfigurations);
    }
    finally {
      super.tearDown();
    }
  }

  @Override
  protected String getBaseDataPath() {
    return "testData/run/profiler";
  }

  @Override
  protected String getResultsTestDataPath() {
    return super.getResultsTestDataPath() + "/answers";
  }

  @Test
  public void testRunWithProfilerBegin() {
    modifyOnlyConfiguration(it -> it.setStartupMode(PerlProfilerStartupMode.BEGIN));
    checkProfilingResultsWithFile(runScriptWithProfilingAndWait("simple", "testscript.pl"));
  }

  @Test
  public void testRunWithProfilerInit() {
    modifyOnlyConfiguration(it -> it.setStartupMode(PerlProfilerStartupMode.INIT));
    checkProfilingResultsWithFile(runScriptWithProfilingAndWait("simple", "testscript.pl"));
  }

  @Test
  public void testRunWithProfilerEnd() {
    modifyOnlyConfiguration(it -> it.setStartupMode(PerlProfilerStartupMode.END));
    checkProfilingResultsWithFile(runScriptWithProfilingAndWait("simple", "testscript.pl"));
  }

  @Test
  public void testRunWithProfilerNo() {
    modifyOnlyConfiguration(it -> it.setStartupMode(PerlProfilerStartupMode.NO));
    checkProfilingResultsWithFile(runScriptWithProfilingAndWait("simple", "testscript_manual.pl"));
  }

  protected void checkProfilingResultsWithFile(Pair<Pair<ExecutionEnvironment, RunContentDescriptor>, Pair<Executor, PerlProfilerConfigurationState>> executionResult) {
    checkProfilingResultsWithFile(getProfilingResults(executionResult.first.first.getRunProfile(), executionResult.second.second));
  }

  @Test
  public void testProfilingTests() {
    modifyOnlyConfiguration(it -> it.setStartupMode(PerlProfilerStartupMode.INIT));
    copyDirToModule("../run/testMore");
    var execResults = runConfigurationWithProfilingAndWait(createTestRunConfiguration("t"));
    Throwable failure = null;
    try {
      checkTestRunResultsWithFile(execResults.first.second);
    }
    catch (Throwable e) {
      failure = e;
    }
    checkProfilingResultsWithFile(execResults);
    if (failure != null) {
      throw new RuntimeException(failure);
    }
  }


  private Pair<Pair<ExecutionEnvironment, RunContentDescriptor>, Pair<Executor, PerlProfilerConfigurationState>> runScriptWithProfilingAndWait(
    @NotNull String directory,
    @NotNull String script) {
    copyDirToModule(directory);
    return runConfigurationWithProfilingAndWait(createOnlyRunConfiguration(script));
  }

  private void checkProfilingResultsWithFile(@NotNull ProfilerState profilerState) {
    assertInstanceOf(profilerState, DataReady.class);
    var profilerData = ((DataReady)profilerState).getData();
    assertInstanceOf(profilerData, NewCallTreeOnlyProfilerData.class);
    var treeBuilder = ((NewCallTreeOnlyProfilerData)profilerData).getBuilder();
    assertInstanceOf(treeBuilder, DummyCallTreeBuilder.class);
    var stacks = ((DummyCallTreeBuilder)treeBuilder).getAllStacks();
    List<String> serializedFrames = new ArrayList<>();
    for (Object stack : stacks) {
      assertInstanceOf(stack, Stack.class);
      var frames = ((Stack)stack).getFrames();
      List<String> serializedStack = new ArrayList<>();
      for (Object frame : frames) {
        var frameString = frame.toString();
        var prefix = "ANON__[";
        var prefixOffset = frameString.indexOf(prefix);
        if (prefixOffset > -1) {
          frameString = frameString.substring(0, prefixOffset + prefix.length()) +
                        frameString.substring(frameString.lastIndexOf('/'));
        }
        serializedStack.add(frameString);
      }
      serializedFrames.add(String.join("; ", serializedStack));
    }
    Collections.sort(serializedFrames);
    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(".profiling"), String.join("\n", serializedFrames));
  }

  private @NotNull Pair<Pair<ExecutionEnvironment, RunContentDescriptor>, Pair<Executor, PerlProfilerConfigurationState>> runConfigurationWithProfilingAndWait(
    @NotNull GenericPerlRunConfiguration runConfiguration) {
    var executorAndSettings = getExecutorAndSettings();
    Pair<ExecutionEnvironment, RunContentDescriptor> pair;
    try {
      pair = executeConfiguration(runConfiguration, executorAndSettings.first);
    }
    catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    RunContentDescriptor contentDescriptor = pair.second;
    ProcessHandler processHandler = contentDescriptor.getProcessHandler();
    assertNotNull(processHandler);
    waitForProcessFinish(processHandler);
    Integer exitCode = processHandler.getExitCode();
    LOG.debug("Profiling process finished with exit code: ", exitCode);
    return Pair.create(pair, executorAndSettings);
  }

  private @NotNull Pair<Executor, PerlProfilerConfigurationState> getExecutorAndSettings() {
    var executorGroup = DefaultProfilerExecutorGroup.Companion.getInstance();
    assertNotNull(executorGroup);
    var executors = executorGroup.childExecutors();
    Pair<Executor, PerlProfilerConfigurationState> result = null;
    for (Executor executor : executors) {
      var settings = executorGroup.getRegisteredSettings(executor.getId());
      assertNotNull(settings);
      var configurationState = settings.getState();
      if (configurationState instanceof PerlProfilerConfigurationState) {
        assertNull(result);
        result = Pair.create(executor, (PerlProfilerConfigurationState)configurationState);
      }
    }
    assertNotNull(result);
    return result;
  }

  private ProfilerState getProfilingResults(@NotNull RunProfile runConfiguration,
                                            @NotNull PerlProfilerConfigurationState profilerConfiguration) {
    var perlProfilerProcess = getProfilerProcess(runConfiguration, profilerConfiguration);
    waitWithEventsDispatching("Unable to fetch profiling results", () -> !perlProfilerProcess.getState().isActive());
    return perlProfilerProcess.getState();
  }

  private @NotNull PerlProfilerProcess getProfilerProcess(@NotNull RunProfile runConfiguration,
                                                          @NotNull PerlProfilerConfigurationState profilerConfiguration) {
    var toolWindow = getProfilerToolWindow();
    var toolWindowContents = toolWindow.getContentManager().getContents();
    assertSize(1, toolWindowContents);
    var firstComponent = toolWindowContents[0].getComponent();
    assertInstanceOf(firstComponent, ProfilerProcessPanel.class);
    ProfilerProcess<?> profilerProcess = ((ProfilerProcessPanel)firstComponent).getProfilerProcess();
    assertInstanceOf(profilerProcess, PerlProfilerProcess.class);
    assertEquals(profilerConfiguration, ((PerlProfilerProcess)profilerProcess).getProfilerConfiguration());
    assertEquals(runConfiguration, ((PerlProfilerProcess)profilerProcess).getRunProfile());
    return (PerlProfilerProcess)profilerProcess;
  }

  private @NotNull ToolWindow getProfilerToolWindow() {
    var toolWindow = ToolWindowManager.getInstance(myProject).getToolWindow(ProfilerToolWindowManager.TOOLWINDOW_ID);
    assertNotNull(toolWindow);
    return toolWindow;
  }

  private void modifyOnlyConfiguration(@NotNull Consumer<PerlProfilerConfigurationState> stateModifier) {
    var profilerRunConfigurationsManager = ProfilerRunConfigurationsManager.getInstance();
    var configurations = profilerRunConfigurationsManager.getConfigurations();
    for (ProfilerConfigurationState configuration : configurations) {
      if (configuration instanceof PerlProfilerConfigurationState) {
        stateModifier.accept((PerlProfilerConfigurationState)configuration);
        break;
      }
    }
    profilerRunConfigurationsManager.setConfigurations(configurations);
  }
}
