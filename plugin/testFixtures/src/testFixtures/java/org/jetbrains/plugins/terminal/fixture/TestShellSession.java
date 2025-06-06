// Copyright 2000-2020 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package org.jetbrains.plugins.terminal.fixture;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.project.Project;
import com.intellij.terminal.pty.PtyProcessTtyConnector;
import com.jediterm.terminal.TtyConnector;
import com.pty4j.PtyProcess;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.terminal.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;
import java.util.function.BooleanSupplier;

public class TestShellSession {

  public final ShellTerminalWidget myWidget;
  public final TestTerminalBufferWatcher myWatcher;

  public TestShellSession(@NotNull Project project, @NotNull Disposable parentDisposable) throws ExecutionException {
    JBTerminalSystemSettingsProvider settingsProvider = new JBTerminalSystemSettingsProvider();
    myWidget = new ShellTerminalWidget(project, settingsProvider, parentDisposable);

    LocalTerminalDirectRunner runner = LocalTerminalDirectRunner.createTerminalRunner(project);
    ShellStartupOptions baseOptions = ShellStartupOptionsKt.shellStartupOptions(project.getBasePath());
    ShellStartupOptions configuredOptions = runner.configureStartupOptions(baseOptions);
    PtyProcess process = runner.createProcess(configuredOptions);
    TtyConnector connector = new PtyProcessTtyConnector(process, StandardCharsets.UTF_8);
    myWidget.start(connector);
    myWatcher = new TestTerminalBufferWatcher(myWidget.getTerminalTextBuffer());
  }

  public void executeCommand(@NotNull @NonNls String shellCommand) throws IOException {
    myWidget.executeCommand(shellCommand);
  }

  public void awaitBufferCondition(@NonNls BooleanSupplier condition, int timeoutMillis) {
    myWatcher.awaitBuffer(condition, timeoutMillis);
  }

  public String getScreenLines() {
    return myWatcher.getScreenLines();
  }
}
