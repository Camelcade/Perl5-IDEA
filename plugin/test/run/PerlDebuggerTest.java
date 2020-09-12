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
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.executors.DefaultDebugExecutor;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.ui.RunContentDescriptor;
import com.intellij.openapi.util.Trinity;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.testFramework.UsefulTestCase;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.xdebugger.XDebugSession;
import com.intellij.xdebugger.XDebuggerManager;
import com.intellij.xdebugger.XDebuggerUtil;
import com.intellij.xdebugger.XSourcePosition;
import com.intellij.xdebugger.breakpoints.XBreakpointManager;
import com.intellij.xdebugger.breakpoints.XLineBreakpoint;
import com.intellij.xdebugger.frame.XExecutionStack;
import com.intellij.xdebugger.frame.XStackFrame;
import com.intellij.xdebugger.impl.XSourcePositionImpl;
import com.perl5.lang.perl.idea.debugger.PerlStackFrame;
import com.perl5.lang.perl.idea.debugger.breakpoints.PerlLineBreakpointProperties;
import com.perl5.lang.perl.idea.debugger.breakpoints.PerlLineBreakpointType;
import com.perl5.lang.perl.idea.debugger.protocol.PerlLoadedFileDescriptor;
import com.perl5.lang.perl.idea.debugger.protocol.PerlStackFrameDescriptor;
import com.perl5.lang.perl.idea.debugger.protocol.PerlValueDescriptor;
import com.perl5.lang.perl.idea.run.GenericPerlRunConfiguration;
import com.perl5.lang.perl.idea.run.debugger.PerlDebugOptionsSets;
import com.perl5.lang.perl.idea.run.prove.PerlTestRunConfiguration;
import com.pty4j.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.File;
import java.util.*;
import java.util.function.Consumer;

import static base.PerlLightTestCaseBase.SEPARATOR_NEWLINES;
import static base.PerlLightTestCaseBase.SEPARATOR_NEW_LINE_AFTER;

@Category(Heavy.class)
public class PerlDebuggerTest extends PerlPlatformTestCase {

  @Override
  protected String getBaseDataPath() {
    return "testData/run/debugger";
  }

  @Override
  protected String getResultsTestDataPath() {
    return super.getResultsTestDataPath() + "/answers";
  }

  @Test
  public void testTestsDebugging() {
    copyDirToModule("../run/testMore");
    PerlTestRunConfiguration testRunConfiguration = createTestRunConfiguration("t/subtest_is_passed_named.t");
    testRunConfiguration.setStartMode(PerlDebugOptionsSets.DEBUGGER_STARTUP_BREAKPOINT);

    setBreakPoint(getModuleFile("t/subtest_is_passed_named.t"), 5);

    Trinity<ExecutionEnvironment, RunContentDescriptor, XDebugSession> trinity = runConfigurationWithDebugger(testRunConfiguration);
    XDebugSession debugSession = trinity.third;
    assertStoppedAtLine(debugSession, 93);
    debugSession.resume();
    assertStoppedAtLine(debugSession, 5);
    debugSession.resume();
    ProcessHandler processHandler = trinity.second.getProcessHandler();
    assertNotNull(processHandler);
    waitForProcessFinish(processHandler);
    checkTestRunResultsWithFile(trinity.second);
  }

  @Test
  public void testStopAtBreakPoint() {
    XDebugSession debugSession = debugTestScript(it -> {
      setBreakPoint(it.getScriptPath(), 5);
      it.setStartMode(PerlDebugOptionsSets.DEBUGGER_STARTUP_BREAKPOINT);
    });
    assertStoppedAtLine(debugSession, 5);
  }

  @Test
  public void testStopAfterCompilation() {
    XDebugSession debugSession = debugTestScript();
    assertStoppedAtLine(debugSession, 15);
  }

  @Test
  public void testStepOver() {
    XDebugSession debugSession = debugTestScript();
    assertStoppedAtLine(debugSession, 15);
    debugSession.stepOver(false);
    assertStoppedAtLine(debugSession, 16);
    debugSession.stepOver(false);
    assertStoppedAtLine(debugSession, 17);
    setBreakPointInCurrentFile(debugSession, 4);
    debugSession.stepOver(false);
    assertStoppedAtLine(debugSession, 4);
  }

  @Test
  public void testForceStepOver() {
    XDebugSession debugSession = debugTestScript();
    assertStoppedAtLine(debugSession, 15);
    debugSession.stepOver(true);
    assertStoppedAtLine(debugSession, 16);
    debugSession.stepOver(true);
    assertStoppedAtLine(debugSession, 17);
    setBreakPointInCurrentFile(debugSession, 4);
    debugSession.stepOver(true);
    assertStoppedAtLine(debugSession, 18);
  }

  @Test
  public void testConditionalBreakpoint() {
    XDebugSession debugSession = debugTestScript();
    setBreakPointInCurrentFile(debugSession, 7);
    setBreakPointInCurrentFile(debugSession, 8);
    setBreakPointCondition(7, "$scalar == 42");
    setBreakPointCondition(8, "$scalar == 1");
    debugSession.resume();
    assertStoppedAtLine(debugSession, 8);
  }

  @Test
  public void testFrameVariables() {
    XDebugSession debugSession = debugScript("variables", "testscript.pl", null);
    int line = 21;
    runToLine(debugSession, line, false);
    assertStoppedAtLine(debugSession, line);
    compareFrameWithFile(debugSession.getCurrentStackFrame());
  }

  @Test
  public void testStackFrames() {
    XDebugSession debugSession = debugTestScript();
    runToLine(debugSession, 8, false);
    assertStoppedAtLine(debugSession, 8);
    List<XExecutionStack> stacks = Arrays.asList(debugSession.getSuspendContext().getExecutionStacks());
    assertEquals("Expected 1 stack, got: " + stacks, 1, stacks.size());
    XExecutionStack mainStack = stacks.get(0);
    List<XStackFrame> frames = new ArrayList<>();
    mainStack.computeStackFrames(0, new XExecutionStack.XStackFrameContainer() {
      @Override
      public void addStackFrames(@NotNull List<? extends XStackFrame> stackFrames, boolean last) {
        frames.addAll(stackFrames);
      }

      @Override
      public void errorOccurred(@NotNull String errorMessage) {
        fail("Unable to compte frames: " + errorMessage);
      }
    });
    assertEquals("Got: " + frames, 2, frames.size());
  }

  @Test
  public void testStepInto() {
    XDebugSession debugSession = debugTestScript();
    assertStoppedAtLine(debugSession, 15);
    debugSession.stepOver(false);
    assertStoppedAtLine(debugSession, 16);
    debugSession.stepInto();
    assertStoppedAtLine(debugSession, 1);
  }

  @Test
  public void testStepOut() {
    XDebugSession debugSession = debugTestScript();
    assertStoppedAtLine(debugSession, 15);
    debugSession.stepOver(false);
    assertStoppedAtLine(debugSession, 16);
    debugSession.stepInto();
    assertStoppedAtLine(debugSession, 1);
    setBreakPointInCurrentFile(debugSession, 7);
    debugSession.stepOut();
    assertStoppedAtLine(debugSession, 7);
    debugSession.stepOut();
    assertStoppedAtLine(debugSession, 17);
  }

  @Test
  public void testRunToCursor() {
    XDebugSession debugSession = debugTestScript();
    assertStoppedAtLine(debugSession, 15);
    debugSession.stepOver(false);
    assertStoppedAtLine(debugSession, 16);
    debugSession.stepInto();
    assertStoppedAtLine(debugSession, 1);
    setBreakPointInCurrentFile(debugSession, 5);
    runToLine(debugSession, 17, false);
    assertStoppedAtLine(debugSession, 5);
    runToLine(debugSession, 17, false);
    assertStoppedAtLine(debugSession, 17);
  }

  @Test
  public void testForceRunToCursor() {
    XDebugSession debugSession = debugTestScript();
    assertStoppedAtLine(debugSession, 15);
    debugSession.stepOver(false);
    assertStoppedAtLine(debugSession, 16);
    debugSession.stepInto();
    assertStoppedAtLine(debugSession, 1);
    setBreakPointInCurrentFile(debugSession, 5);
    runToLine(debugSession, 17, true);
    assertStoppedAtLine(debugSession, 17);
  }

  @Test
  public void testStepInEval() {
    XDebugSession debugSession = debugScript("eval", "testscript.pl", null);
    assertStoppedAtLine(debugSession, 4);
    runToLine(debugSession, 13, false);
    assertStoppedAtLine(debugSession, 13);
    debugSession.stepInto();
    assertStoppedAtLine(debugSession, 2);
    assertTrue(getCurrentVirtualFile(debugSession).getName().contains("eval"));
    debugSession.stepOver(false);
    assertStoppedAtLine(debugSession, 3);
    debugSession.stepOut();
    assertStoppedAtLine(debugSession, 14);
    assertTrue(getCurrentVirtualFile(debugSession).getName().contains("testscript"));
  }

  private @NotNull VirtualFile getCurrentVirtualFile(XDebugSession debugSession) {
    waitForDebugger(debugSession);
    return Objects.requireNonNull(debugSession.getCurrentPosition()).getFile();
  }

  @Test
  public void testStopAsap() {
    XDebugSession debugSession = debugTestScript(it -> it.setStartMode(PerlDebugOptionsSets.DEBUGGER_STARTUP_COMPILE));
    assertStoppedAtLine(debugSession, 12);
  }

  private void runToLine(@NotNull XDebugSession debugSession, int line, boolean force) {
    debugSession.runToPosition(currentFilePosition(debugSession, line), force);
  }

  private @NotNull XSourcePositionImpl currentFilePosition(@NotNull XDebugSession debugSession, int line) {
    return XSourcePositionImpl.create(getCurrentVirtualFile(debugSession), line);
  }

  private @NotNull XDebugSession debugTestScript() {
    return debugTestScript(it -> it.setStartMode(PerlDebugOptionsSets.DEBUGGER_STARTUP_RUN));
  }

  private @NotNull XDebugSession debugTestScript(@Nullable Consumer<GenericPerlRunConfiguration> configurator) {
    return debugScript("simple", "testscript.pl", configurator);
  }

  private @NotNull XDebugSession debugScript(@NotNull String directoryName,
                                             @NotNull String scriptName,
                                             @Nullable Consumer<GenericPerlRunConfiguration> configurator) {
    copyDirToModule(directoryName);
    GenericPerlRunConfiguration runConfiguration = createOnlyRunConfiguration(scriptName);
    if (configurator != null) {
      configurator.accept(runConfiguration);
    }
    return runConfigurationWithDebugger(runConfiguration).third;
  }

  private void assertStoppedAtLine(XDebugSession debugSession, int expected) {
    waitForDebugger(debugSession);
    XSourcePosition currentPosition = debugSession.getCurrentPosition();
    assertNotNull(currentPosition);
    assertEquals("Stopped at wrong line: ", expected, currentPosition.getLine());
  }

  private @NotNull Trinity<ExecutionEnvironment, RunContentDescriptor, XDebugSession> runConfigurationWithDebugger(@NotNull RunConfiguration runConfiguration) {
    try {
      Pair<ExecutionEnvironment, RunContentDescriptor> pair = executeConfiguration(runConfiguration, DefaultDebugExecutor.EXECUTOR_ID);
      XDebugSession debugSession = XDebuggerManager.getInstance(getProject()).getDebugSession(pair.second.getExecutionConsole());
      assertNotNull(debugSession);
      return Trinity.create(pair.first, pair.second, debugSession);
    }
    catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  private void setBreakPointInCurrentFile(@NotNull XDebugSession debugSession, int line) {
    waitForDebugger(debugSession);
    setBreakPoint(getCurrentVirtualFile(debugSession), line);
  }

  private void setBreakPoint(@NotNull String scriptPath, int line) {
    VirtualFile scriptVirtualFile = refreshAndFindFile(new File(scriptPath));
    assertNotNull(scriptVirtualFile);
    setBreakPoint(scriptVirtualFile, line);
  }

  private void setBreakPoint(@NotNull VirtualFile scriptVirtualFile, int line) {
    XDebuggerUtil.getInstance().toggleLineBreakpoint(getProject(), scriptVirtualFile, line);
  }

  private void waitForDebugger(@NotNull XDebugSession debugSession) {
    waitWithEventsDispatching(
      "Timeout waiting for debugger", () -> debugSession.isPaused() || debugSession.isSuspended() || debugSession.isStopped()
    );
  }

  private @NotNull Collection<? extends XLineBreakpoint<PerlLineBreakpointProperties>> getLineBreakpoints() {
    return getBreakPointManager().getBreakpoints(PerlLineBreakpointType.class);
  }

  private @NotNull XBreakpointManager getBreakPointManager() {
    return XDebuggerManager.getInstance(getProject()).getBreakpointManager();
  }

  private void setBreakPointCondition(int line, String condition) {
    for (XLineBreakpoint<PerlLineBreakpointProperties> breakpoint : getLineBreakpoints()) {
      if (breakpoint.getLine() == line) {
        breakpoint.setCondition(condition);
        return;
      }
    }
    fail("There is no breakpoint at line " + line);
  }

  private void compareFrameWithFile(XStackFrame currentFrame) {
    assertInstanceOf(currentFrame, PerlStackFrame.class);
    PerlStackFrameDescriptor frameDescriptor = ((PerlStackFrame)currentFrame).getFrameDescriptor();
    assertNotNull(frameDescriptor);
    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(""), serializeFrameDescriptor(frameDescriptor));
  }

  private String serializeFrameDescriptor(@NotNull PerlStackFrameDescriptor frameDescriptor) {
    StringBuilder sb = new StringBuilder();
    PerlLoadedFileDescriptor fileDescriptor = frameDescriptor.getFileDescriptor();
    sb.append("Name: ").append(fileDescriptor.getName()).append("; line: ").append(frameDescriptor.getLine()).append("\n");
    sb.append("Main size: ").append(frameDescriptor.getMainSize()).append(SEPARATOR_NEWLINES);
    sb.append("Args:").append(SEPARATOR_NEWLINES).append(serializePerlValueDescriptors(frameDescriptor.getArgs()));
    sb.append("Lexicals:").append(SEPARATOR_NEWLINES).append(serializePerlValueDescriptors(frameDescriptor.getLexicals()));
    sb.append("Globals:").append(SEPARATOR_NEWLINES).append(serializePerlValueDescriptors(frameDescriptor.getGlobals()));
    return sb.toString();
  }

  private String serializePerlValueDescriptors(PerlValueDescriptor @NotNull [] descriptorsArray) {
    if (descriptorsArray.length == 0) {
      return "";
    }
    List<PerlValueDescriptor> descriptors = new ArrayList<>();
    ContainerUtil.addAll(descriptors, descriptorsArray);
    descriptors.sort(Comparator.comparing(PerlValueDescriptor::getName));
    StringBuilder sb = new StringBuilder();
    for (PerlValueDescriptor descriptor : descriptors) {
      sb.append(descriptor.testDebugString()).append("\n");
    }
    sb.append(SEPARATOR_NEW_LINE_AFTER);
    return sb.toString();
  }
}
