/*
 * Copyright 2016 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.run.debugger;

import com.intellij.execution.ExecutionResult;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.ui.ExecutionConsole;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.xdebugger.XDebugProcess;
import com.intellij.xdebugger.XDebugSession;
import com.intellij.xdebugger.XDebuggerManager;
import com.intellij.xdebugger.XSourcePosition;
import com.intellij.xdebugger.breakpoints.XBreakpointHandler;
import com.intellij.xdebugger.breakpoints.XBreakpointManager;
import com.intellij.xdebugger.breakpoints.XLineBreakpoint;
import com.intellij.xdebugger.evaluation.XDebuggerEditorsProvider;
import com.perl5.lang.perl.idea.run.PerlRunProfileState;
import com.perl5.lang.perl.idea.run.debugger.breakpoints.PerlLineBreakpointHandler;
import com.perl5.lang.perl.idea.run.debugger.breakpoints.PerlLineBreakpointProperties;
import com.perl5.lang.perl.idea.run.debugger.breakpoints.PerlLineBreakpointType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * Created by hurricup on 04.05.2016.
 */
public class PerlDebugProcess extends XDebugProcess
{
	private ExecutionResult myExecutionResult;
	private PerlDebugThread myPerlDebugThread;
	private PerlRunProfileState myRunProfileState;

	public PerlDebugProcess(@NotNull XDebugSession session, PerlDebugProfileState state, ExecutionResult executionResult)
	{
		super(session);
		this.myExecutionResult = executionResult;
		myPerlDebugThread = new PerlDebugThread(session, state);
		myRunProfileState = state;
		myPerlDebugThread.start();
	}

	@NotNull
	@Override
	public XDebuggerEditorsProvider getEditorsProvider()
	{
		return PerlDebuggerEditorsProvider.INSTANCE;
	}

	@Override
	public boolean checkCanInitBreakpoints()
	{
		if (PerlDebugThread.DEV_MODE)
			System.err.println("Check can init breakpoints");
		return true;
	}

	@NotNull
	@Override
	public XBreakpointHandler<?>[] getBreakpointHandlers()
	{
		return new XBreakpointHandler[]{new PerlLineBreakpointHandler(myPerlDebugThread)};
	}

	@Override
	public void sessionInitialized()
	{
		if (PerlDebugThread.DEV_MODE)
			System.err.println("Session initialized");
		super.sessionInitialized();
	}

	@Override
	public void registerAdditionalActions(@NotNull DefaultActionGroup leftToolbar, @NotNull DefaultActionGroup topToolbar, @NotNull DefaultActionGroup settings)
	{
		super.registerAdditionalActions(leftToolbar, topToolbar, settings);
	}

	@Override
	public boolean checkCanPerformCommands()
	{
		return super.checkCanPerformCommands();
	}

	@Override
	public void startStepOver()
	{
		myPerlDebugThread.sendString("o\n");
	}

	@Override
	public void startStepInto()
	{
		myPerlDebugThread.sendString("\n");
	}

	@Override
	public void startStepOut()
	{
		myPerlDebugThread.sendString("u\n");
	}

	@Override
	public void resume()
	{
		myPerlDebugThread.sendString("g\n");
	}

	@Override
	public void stop()
	{
		myPerlDebugThread.setStop();

		ApplicationManager.getApplication().runReadAction(
				new Runnable()
				{
					@Override
					public void run()
					{
						XBreakpointManager breakpointManager = XDebuggerManager.getInstance(getSession().getProject()).getBreakpointManager();
						Collection<? extends XLineBreakpoint<PerlLineBreakpointProperties>> breakpoints = breakpointManager.getBreakpoints(PerlLineBreakpointType.class);
						for (XLineBreakpoint<PerlLineBreakpointProperties> breakpoint : breakpoints)
						{
							breakpointManager.updateBreakpointPresentation(breakpoint, null, null);
						}
					}
				}
		);
	}

	@Override
	public void runToPosition(@NotNull XSourcePosition position)
	{

	}

	@Nullable
	@Override
	protected ProcessHandler doGetProcessHandler()
	{
		return myExecutionResult.getProcessHandler();
	}

	@NotNull
	@Override
	public ExecutionConsole createConsole()
	{
		return myExecutionResult.getExecutionConsole();
	}
}
