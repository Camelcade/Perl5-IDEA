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

package com.perl5.lang.perl.idea.run.remote;

import com.intellij.execution.DefaultExecutionResult;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.ExecutionResult;
import com.intellij.execution.Executor;
import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.ProgramRunner;
import com.intellij.openapi.project.Project;
import com.intellij.xdebugger.DefaultDebugProcessHandler;
import com.perl5.lang.perl.idea.run.debugger.PerlDebugProfileState;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 09.05.2016.
 */
public class PerlRemoteDebuggingRunProfileState extends PerlDebugProfileState
{
	private final PerlRemoteDebuggingConfiguration runProfile;
	private final Project myProject;

	public PerlRemoteDebuggingRunProfileState(ExecutionEnvironment environment)
	{
		super(environment);
		runProfile = (PerlRemoteDebuggingConfiguration) environment.getRunProfile();
		myProject = environment.getProject();
	}

	@NotNull
	@Override
	public ExecutionResult execute(@NotNull Executor executor, @NotNull ProgramRunner runner) throws ExecutionException
	{
		return new DefaultExecutionResult(TextConsoleBuilderFactory.getInstance().createBuilder(myProject).getConsole(), new DefaultDebugProcessHandler());
	}

	@Override
	public String getDebugHost()
	{
		return runProfile.getDebugHost();
	}

	@Override
	public int getDebugPort() throws ExecutionException
	{
		return Integer.parseInt(runProfile.getDebugPort());
	}

	@Override
	public boolean isPerlServer()
	{
		return runProfile.isPerlServer();
	}
}
