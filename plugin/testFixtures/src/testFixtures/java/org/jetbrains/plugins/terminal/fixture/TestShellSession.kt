// Copyright 2000-2020 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package org.jetbrains.plugins.terminal.fixture

import com.intellij.openapi.Disposable
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.util.SystemInfo
import com.intellij.terminal.JBTerminalWidget
import com.intellij.terminal.pty.PtyProcessTtyConnector
import com.intellij.util.io.delete
import com.jediterm.core.util.TermSize
import com.jediterm.terminal.TtyConnector
import com.pty4j.windows.conpty.WinConPtyProcess
import org.jetbrains.plugins.terminal.JBTerminalSystemSettingsProvider
import org.jetbrains.plugins.terminal.LocalTerminalDirectRunner
import org.jetbrains.plugins.terminal.ShellTerminalWidget
import org.jetbrains.plugins.terminal.shellStartupOptions
import org.junit.Assume
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.util.function.BooleanSupplier

/***
 * Copy-paste of org.jetbrains.plugins.terminal.classic.fixture.ClassicTerminalTestShellSessioni
 * See: https://youtrack.jetbrains.com/issue/IJPL-107274/
 */
class TestShellSession(shellCommand: List<String>?, val widget: ShellTerminalWidget) {

  constructor(project: Project, parentDisposable: Disposable) : this(
    null,
    ShellTerminalWidget(
      project,
      JBTerminalSystemSettingsProvider(),
      parentDisposable
    )
  )

  private val watcher: TestTerminalBufferWatcher = TestTerminalBufferWatcher(widget.terminalTextBuffer, widget.terminal)

  init {
    start(shellCommand, widget)
  }

  @Throws(IOException::class)
  fun executeCommand(shellCommand: String) {
    widget.executeCommand(shellCommand)
  }

  @Suppress("unused")
  fun awaitScreenLinesEndWith(expectedScreenLines: List<String>, timeoutMillis: Int) {
    watcher.awaitScreenLinesEndWith(expectedScreenLines, timeoutMillis.toLong())
  }

  @Suppress("unused")
  fun awaitScreenLinesAre(expectedScreenLines: List<String>, timeoutMillis: Int) {
    watcher.awaitScreenLinesAre(expectedScreenLines, timeoutMillis.toLong())
  }

  fun awaitBufferCondition(condition: BooleanSupplier, timeoutMillis: Int) {
    watcher.awaitBuffer(condition, timeoutMillis.toLong())
  }

  val screenLines: String
    get() = watcher.screenLines

  companion object {
    fun start(shellCommand: List<String>?, terminalWidget: JBTerminalWidget) {
      val runner = LocalTerminalDirectRunner.createTerminalRunner(terminalWidget.project)
      val baseOptions = shellStartupOptions(terminalWidget.project.basePath) {
        it.shellCommand = shellCommand
      }
      val initialTermSize = TermSize(80, 50)
      val workingDirectory = Files.createTempDirectory("intellij-terminal-working-dir")
      val configuredOptions = runner.configureStartupOptions(baseOptions).builder().modify {
        it.initialTermSize = initialTermSize
        it.workingDirectory = workingDirectory.toString()
      }.build()
      val process = runner.createProcess(configuredOptions)
      val connector: TtyConnector = PtyProcessTtyConnector(process, StandardCharsets.UTF_8)
      terminalWidget.asNewWidget().connectToTty(connector, initialTermSize)

      Disposer.register(terminalWidget) {
        try {
          connector.close()
        }
        catch (t: Throwable) {
          logger<TestTerminalBufferWatcher>().error("Error closing TtyConnector", t)
        }
        workingDirectory.delete()
      }

      if (SystemInfo.isWindows) {
        val msg = "On Windows, the bundled ConPTY in required for test stability"
        Assume.assumeTrue(msg + ", but got " + process::class.java, process is WinConPtyProcess)
        Assume.assumeTrue(msg, (process as WinConPtyProcess).isBundledConPtyLibrary)
      }
    }
  }

}
