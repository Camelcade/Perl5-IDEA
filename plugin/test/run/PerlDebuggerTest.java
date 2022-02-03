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
import base.PerlLightTestCaseBase;
import base.PerlPlatformTestCase;
import categories.Heavy;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.executors.DefaultDebugExecutor;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.ui.RunContentDescriptor;
import com.intellij.openapi.util.Trinity;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.testFramework.PlatformTestUtil;
import com.intellij.testFramework.UsefulTestCase;
import com.intellij.xdebugger.*;
import com.intellij.xdebugger.breakpoints.XBreakpointManager;
import com.intellij.xdebugger.breakpoints.XLineBreakpoint;
import com.intellij.xdebugger.frame.XExecutionStack;
import com.intellij.xdebugger.frame.XStackFrame;
import com.intellij.xdebugger.impl.XDebugSessionImpl;
import com.intellij.xdebugger.impl.XSourcePositionImpl;
import com.intellij.xdebugger.impl.frame.XWatchesView;
import com.intellij.xdebugger.impl.frame.XWatchesViewImpl;
import com.intellij.xdebugger.impl.ui.XDebugSessionData;
import com.intellij.xdebugger.impl.ui.XDebugSessionTab;
import com.intellij.xdebugger.impl.ui.tree.XDebuggerTree;
import com.intellij.xdebugger.impl.ui.tree.nodes.XDebuggerTreeNode;
import com.intellij.xdebugger.impl.ui.tree.nodes.XValueGroupNodeImpl;
import com.intellij.xdebugger.impl.ui.tree.nodes.XValueNodeImpl;
import com.intellij.xdebugger.impl.ui.tree.nodes.XValuePresentationUtil;
import com.perl5.lang.perl.debugger.PerlStackFrame;
import com.perl5.lang.perl.debugger.breakpoints.PerlLineBreakpointProperties;
import com.perl5.lang.perl.debugger.breakpoints.PerlLineBreakpointType;
import com.perl5.lang.perl.debugger.protocol.PerlLoadedFileDescriptor;
import com.perl5.lang.perl.debugger.protocol.PerlStackFrameDescriptor;
import com.perl5.lang.perl.idea.run.GenericPerlRunConfiguration;
import com.perl5.lang.perl.idea.run.debugger.PerlDebugOptionsSets;
import com.perl5.lang.perl.idea.run.prove.PerlTestRunConfiguration;
import com.pty4j.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import javax.swing.tree.TreeNode;
import java.io.File;
import java.util.*;
import java.util.function.Consumer;

import static base.PerlLightTestCaseBase.SEPARATOR_NEWLINES;

@SuppressWarnings("UnconstructableJUnitTestCase")
@Category(Heavy.class)
public class PerlDebuggerTest extends PerlPlatformTestCase {

  public PerlDebuggerTest(@NotNull PerlInterpreterConfigurator interpreterConfigurator) {
    super(interpreterConfigurator);
  }

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
    assertInstanceOf(debugSession, XDebugSessionImpl.class);
    int line = 27;
    runToLine(debugSession, line, false);
    assertStoppedAtLine(debugSession, line);
    compareSessionWithFile((XDebugSessionImpl)debugSession);
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
      disposeOnPerlTearDown(() -> {
        if (!debugSession.isStopped()) {
          debugSession.stop();
        }
      });
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

  private void compareSessionWithFile(XDebugSessionImpl debugSession) {
    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(""), serializeSession(debugSession));
  }

  private String serializeSession(@NotNull XDebugSessionImpl debugSession) {
    StringBuilder sb = new StringBuilder();
    sb.append("Name: ").append(debugSession.getSessionName()).append("\n");
    var currentFrame = debugSession.getCurrentStackFrame();
    assertNotNull(currentFrame);
    sb.append(serializeFrame(currentFrame)).append(SEPARATOR_NEWLINES);

    sb.append(serializeSessionData(debugSession.getSessionData()))
      .append(SEPARATOR_NEWLINES)
      .append(serializeSessionTab(debugSession.getSessionTab()));

    return sb.toString().replaceAll("(REF|IO|CODE|FORMAT)\\([^)]+\\)", "$1(...)");
  }

  private @NotNull String serializeSessionData(@Nullable XDebugSessionData sessionData) {
    if (sessionData == null) {
      return "No session data";
    }
    return String.join(
      "\n",
      "Session data:",
      "Configuration name: " + sessionData.getConfigurationName(),
      "Watch expressions: " + serializeExpressions(sessionData.getWatchExpressions())
    );
  }

  private @NotNull String serializeExpressions(@NotNull List<XExpression> expressions) {
    return expressions.toString();
  }

  private @NotNull String serializeSessionTab(@Nullable XDebugSessionTab sessionTab) {
    if (sessionTab == null) {
      return "No session tab";
    }
    XWatchesView watchesView = sessionTab.getWatchesView();
    assertInstanceOf(watchesView, XWatchesViewImpl.class);
    return "Variables view:\n" + serializeDebuggerTree(((XWatchesViewImpl)watchesView).getTree());
  }

  private @NotNull String serializeDebuggerTree(@NotNull XDebuggerTree debuggerTree) {
    PlatformTestUtil.waitWhileBusy(debuggerTree);
    XDebuggerTreeNode rootNode = debuggerTree.getRoot();
    StringBuilder result = new StringBuilder();
    serializeDebuggerNodeRecursively(rootNode, result, "-");
    return result.toString();
  }

  /**
   * @implNote we need to serialize presentation better. Ideally with style and icon
   */
  private void serializeDebuggerNodeRecursively(@NotNull XDebuggerTreeNode node,
                                                @NotNull StringBuilder result,
                                                @NotNull String prefix) {
    result.append(prefix).append(node);
    if (node instanceof XValueNodeImpl) {
      result.append("-").append(XValuePresentationUtil.computeValueText(((XValueNodeImpl)node).getValuePresentation()));
      var valueContainer = ((XValueNodeImpl)node).getValueContainer();
      if (valueContainer.canNavigateToSource()) {
        result.append("; navigates to source");
      }
      if (valueContainer.canNavigateToTypeSource()) {
        result.append("; navigates to type source");
      }
      valueContainer.computeSourcePosition(sourcePosition -> {
        if (sourcePosition != null) {
          result.append("; source position: ")
            .append(sourcePosition.getFile().getName())
            .append(":").append(sourcePosition.getLine())
            .append(":").append(sourcePosition.getOffset());
        }
      });
    }
    result.append("; icon: ").append(PerlLightTestCaseBase.getIconText(node.getIcon()));
    result.append("\n");

    if (!node.isLeaf() && !isMainGroup(node)) {
      loadAllChildren(node);
      if (prefix.length() < 10) {
        List<? extends TreeNode> childNodes = node.getChildren();
        for (TreeNode childNode : childNodes) {
          assertInstanceOf(childNode, XDebuggerTreeNode.class);
          serializeDebuggerNodeRecursively((XDebuggerTreeNode)childNode, result, " " + prefix);
        }
      }
      else {
        result.append(prefix).append("Has more children: ").append(node.getChildCount());
      }
    }
  }

  private boolean isMainGroup(@NotNull XDebuggerTreeNode node) {
    return node instanceof XValueGroupNodeImpl && StringUtil.contains(node.toString(), "%main");
  }

  /**
   * @implNote this method is lame. We should check last kid of the children for ...
   */
  private void loadAllChildren(@NotNull XDebuggerTreeNode node) {
    if (node.isLeaf()) {
      return;
    }
    node.getChildren();
    PlatformTestUtil.dispatchAllEventsInIdeEventQueue();
    try {
      Thread.sleep(100);
    }
    catch (InterruptedException e) {
      fail(e.getMessage());
    }
    PlatformTestUtil.dispatchAllEventsInIdeEventQueue();
  }

  private @NotNull String serializeFrame(@Nullable XStackFrame stackFrame) {
    if (stackFrame == null) {
      return "No stack frame";
    }
    var sourcePosition = stackFrame.getSourcePosition();
    if (sourcePosition == null) {
      return "No source position";
    }

    assertInstanceOf(stackFrame, PerlStackFrame.class);
    PerlStackFrameDescriptor frameDescriptor = ((PerlStackFrame)stackFrame).getFrameDescriptor();
    assertNotNull(frameDescriptor);
    PerlLoadedFileDescriptor fileDescriptor = frameDescriptor.getFileDescriptor();
    assertNotNull(fileDescriptor);

    return String.join(
      "\n",
      "Name: " + fileDescriptor.getName(),
      "File name: " + sourcePosition.getFile().getName(),
      "Line: " + sourcePosition.getLine(),
      "Offset: " + sourcePosition.getOffset(),
      "Main is not empty: " + (frameDescriptor.getMainSize() > 0));
  }
}
