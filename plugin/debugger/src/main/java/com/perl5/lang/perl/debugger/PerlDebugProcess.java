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

package com.perl5.lang.perl.debugger;

import com.intellij.execution.ExecutionResult;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.ui.ExecutionConsole;
import com.intellij.execution.ui.RunnerLayoutUi;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.ui.content.Content;
import com.intellij.xdebugger.XDebugProcess;
import com.intellij.xdebugger.XDebugSession;
import com.intellij.xdebugger.XDebuggerManager;
import com.intellij.xdebugger.XSourcePosition;
import com.intellij.xdebugger.breakpoints.XBreakpointHandler;
import com.intellij.xdebugger.breakpoints.XBreakpointManager;
import com.intellij.xdebugger.breakpoints.XLineBreakpoint;
import com.intellij.xdebugger.evaluation.XDebuggerEditorsProvider;
import com.intellij.xdebugger.frame.XSuspendContext;
import com.intellij.xdebugger.ui.XDebugTabLayouter;
import com.perl5.PerlBundle;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.debugger.breakpoints.PerlLineBreakPointDescriptor;
import com.perl5.lang.perl.debugger.breakpoints.PerlLineBreakpointHandler;
import com.perl5.lang.perl.debugger.breakpoints.PerlLineBreakpointProperties;
import com.perl5.lang.perl.debugger.breakpoints.PerlLineBreakpointType;
import com.perl5.lang.perl.debugger.run.run.debugger.PerlDebugProfileStateBase;
import com.perl5.lang.perl.idea.run.PerlRunProfileState;
import com.perl5.lang.perl.idea.run.debugger.PerlDebugOptions;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;


public class PerlDebugProcess extends XDebugProcess {
  private static final Logger LOG = Logger.getInstance(PerlDebugProcess.class);
  private final ExecutionResult myExecutionResult;
  private final PerlDebugThread myDebugThread;
  private final PerlRunProfileState myDebugProfileState;
  @NonNls static final String PERL_DEBUGGER_NOTIFICATION_GROUP_ID = "PERL_DEBUGGER";

  public PerlDebugProcess(@NotNull XDebugSession session, PerlDebugProfileStateBase state, ExecutionResult executionResult) {
    super(session);
    this.myExecutionResult = executionResult;
    myDebugThread = new PerlDebugThread(session, state, executionResult);
    myDebugProfileState = state;
    myDebugThread.start();
  }

  @Override
  public @NotNull XDebuggerEditorsProvider getEditorsProvider() {
    return PerlDebuggerEditorsProvider.INSTANCE;
  }

  @Override
  public boolean checkCanInitBreakpoints() {
    LOG.debug("Check can init breakpoints");
    return true;
  }

  @Override
  public @NotNull XBreakpointHandler<?>[] getBreakpointHandlers() {
    return new XBreakpointHandler[]{new PerlLineBreakpointHandler(myDebugThread)};
  }

  @Override
  public void sessionInitialized() {
    LOG.debug("Session initialized");
    super.sessionInitialized();
  }

  @Override
  public void registerAdditionalActions(@NotNull DefaultActionGroup leftToolbar,
                                        @NotNull DefaultActionGroup topToolbar,
                                        @NotNull DefaultActionGroup settings) {
    super.registerAdditionalActions(leftToolbar, topToolbar, settings);
  }

  @Override
  public boolean checkCanPerformCommands() {
    return super.checkCanPerformCommands();
  }

  @Override
  public void startStepOver(@Nullable XSuspendContext context) {
    myDebugThread.sendString("o");
  }

  @Override
  public void startStepInto(@Nullable XSuspendContext context) {
    myDebugThread.sendString("");
  }

  @Override
  public void startStepOut(@Nullable XSuspendContext context) {
    myDebugThread.sendString("u");
  }

  @Override
  public void startPausing() {
    if (((PerlDebugOptions)myDebugProfileState.getEnvironment().getRunProfile()).isNonInteractiveModeEnabled()) {
      myDebugThread.sendString("pause");
    }
    else {
      Notifications.Bus.notify(new Notification(
        PERL_DEBUGGER_NOTIFICATION_GROUP_ID,
        PerlBundle.message("perl.run.pause.unavailable.title"),
        PerlBundle.message("perl.run.pause.unavailable.content"),
        NotificationType.INFORMATION
      ));
    }
  }

  @Override
  public void resume(@Nullable XSuspendContext context) {
    myDebugThread.sendString("g");
  }

  @Override
  public void stop() {
    myDebugThread.setStop();

    ApplicationManager.getApplication().runReadAction(
      () -> {
        XBreakpointManager breakpointManager = XDebuggerManager.getInstance(getSession().getProject()).getBreakpointManager();
        Collection<? extends XLineBreakpoint<PerlLineBreakpointProperties>> breakpoints =
          breakpointManager.getBreakpoints(PerlLineBreakpointType.class);
        for (XLineBreakpoint<PerlLineBreakpointProperties> breakpoint : breakpoints) {
          breakpointManager.updateBreakpointPresentation(breakpoint, null, null);
        }
      }
    );
  }

  @Override
  public void runToPosition(@NotNull XSourcePosition position, @Nullable XSuspendContext context) {
    PerlLineBreakPointDescriptor descriptor = PerlLineBreakPointDescriptor.createFromSourcePosition(position, myDebugThread);
    if (descriptor != null) {
      myDebugThread.sendCommand("p", descriptor);
    }
  }

  @Override
  protected @Nullable ProcessHandler doGetProcessHandler() {
    return myExecutionResult.getProcessHandler();
  }

  @Override
  public @NotNull ExecutionConsole createConsole() {
    return myExecutionResult.getExecutionConsole();
  }

  @Override
  public @NotNull XDebugTabLayouter createTabLayouter() {
    return new XDebugTabLayouter() {
      @Override
      public void registerAdditionalContent(@NotNull RunnerLayoutUi ui) {
        Content content =
          ui.createContent("PerlSourceLIst", myDebugThread.getScriptListPanel(), "Loaded Sources", PerlIcons.PERL_SCRIPT_FILE_ICON, null);
        content.setCloseable(false);
        ui.addContent(content);

        content =
          ui.createContent("PerlEvalsList", myDebugThread.getEvalsListPanel(), "Compiled evals", PerlIcons.PERL_LANGUAGE_ICON, null);
        content.setCloseable(false);
        ui.addContent(content);
      }
    };
  }
}
