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
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.xdebugger.XDebugProcess;
import com.intellij.xdebugger.XDebugSession;
import com.intellij.xdebugger.XSourcePosition;
import com.intellij.xdebugger.evaluation.EvaluationMode;
import com.intellij.xdebugger.evaluation.XDebuggerEditorsProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 04.05.2016.
 */
public class PerlDebugProcess extends XDebugProcess
{
	private ExecutionResult myExecutionResult;
	private PerlDebugThread myPerlDebugThread;

	public PerlDebugProcess(@NotNull XDebugSession session, ExecutionResult executionResult)
	{
		super(session);
		this.myExecutionResult = executionResult;
		myPerlDebugThread = new PerlDebugThread(session);
		myPerlDebugThread.start();
	}

	@NotNull
	@Override
	public XDebuggerEditorsProvider getEditorsProvider()
	{
		return new XDebuggerEditorsProvider()
		{
			@NotNull
			@Override
			public FileType getFileType()
			{
				return null;
			}

			@NotNull
			@Override
			public Document createDocument(@NotNull Project project, @NotNull String text, @Nullable XSourcePosition sourcePosition, @NotNull EvaluationMode mode)
			{
				return null;
			}
		};
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
		myPerlDebugThread.sendString("q\n");
		myPerlDebugThread.setStop();
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
