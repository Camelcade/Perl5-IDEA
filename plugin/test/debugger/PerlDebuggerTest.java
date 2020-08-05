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

package debugger;

import base.PerlPlatformTestCase;
import categories.Heavy;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.executors.DefaultDebugExecutor;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.ui.RunContentDescriptor;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.testFramework.PlatformTestUtil;
import com.intellij.xdebugger.XDebugSession;
import com.intellij.xdebugger.XDebuggerManager;
import com.intellij.xdebugger.XDebuggerUtil;
import com.intellij.xdebugger.XSourcePosition;
import com.intellij.xdebugger.impl.XSourcePositionImpl;
import com.perl5.lang.perl.idea.run.GenericPerlRunConfiguration;
import com.perl5.lang.perl.idea.run.debugger.PerlDebugOptionsSets;
import com.pty4j.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.File;
import java.util.Objects;
import java.util.function.Consumer;

@Category(Heavy.class)
public class PerlDebuggerTest extends PerlPlatformTestCase {
  private static final int WAIT_TIMEOUT = 10_000;

  @Override
  protected String getBaseDataPath() {
    return "testData/debugger";
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    addPerlBrewSdk(getPerl526DistibutionId("test_debug"));
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
    debugSession.stepOver(false);
    debugSession.stepOver(false);
    assertStoppedAtLine(debugSession, 3);
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
    debugSession.stepOver(false);
    debugSession.stepOver(false);
    assertStoppedAtLine(debugSession, 3);
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
    debugSession.stepOver(false);
    debugSession.stepOver(false);
    assertStoppedAtLine(debugSession, 3);
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
    GenericPerlRunConfiguration runConfiguration = createRunConfiguration(scriptName);
    if (configurator != null) {
      configurator.accept(runConfiguration);
    }
    return startConfigurationDebugging(runConfiguration);
  }

  private void assertStoppedAtLine(XDebugSession debugSession, int expected) {
    waitForDebugger(debugSession);
    XSourcePosition currentPosition = debugSession.getCurrentPosition();
    assertNotNull(currentPosition);
    assertEquals("Stopped at wrong line: ", expected, currentPosition.getLine());
  }

  private @NotNull XDebugSession startConfigurationDebugging(@NotNull RunConfiguration runConfiguration) {
    try {
      Pair<ExecutionEnvironment, RunContentDescriptor> pair = executeConfiguration(runConfiguration, DefaultDebugExecutor.EXECUTOR_ID);
      RunContentDescriptor contentDescriptor = pair.second;
      return Objects.requireNonNull(
        XDebuggerManager.getInstance(getProject()).getDebugSession(contentDescriptor.getExecutionConsole()));
    }
    catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  private void setBreakPointInCurrentFile(@NotNull XDebugSession debugSession, int line) {
    XDebuggerUtil.getInstance().toggleLineBreakpoint(getProject(), getCurrentVirtualFile(debugSession), line);
  }

  private void setBreakPoint(String scriptPath, int line) {
    VirtualFile scriptVirtualFile = refreshAndFindFile(new File(scriptPath));
    assertNotNull(scriptVirtualFile);
    XDebuggerUtil.getInstance().toggleLineBreakpoint(getProject(), scriptVirtualFile, line);
  }

  private void waitForDebugger(@NotNull XDebugSession debugSession) {
    long start = System.currentTimeMillis();
    while (true) {
      try {
        if (System.currentTimeMillis() - start > WAIT_TIMEOUT) {
          fail("Timeout waiting");
        }
        if (debugSession.isPaused() || debugSession.isSuspended() || debugSession.isStopped()) {
          break;
        }
        PlatformTestUtil.dispatchAllEventsInIdeEventQueue();
        Thread.sleep(100);
      }
      catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
