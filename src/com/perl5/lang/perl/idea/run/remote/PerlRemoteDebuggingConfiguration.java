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

import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.*;
import com.intellij.execution.executors.DefaultDebugExecutor;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.RunConfigurationWithSuppressedDefaultRunAction;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.DefaultJDOMExternalizer;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.WriteExternalException;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 09.05.2016.
 */
public class PerlRemoteDebuggingConfiguration extends RunConfigurationBase implements RunConfigurationWithSuppressedDefaultRunAction
{
	public boolean isPerlServer = true;
	public String debugHost = "localhost";
	public String debugPort = "12345";
	public String remoteWorkingDirectory = "/home/";

	public PerlRemoteDebuggingConfiguration(Project project, @NotNull ConfigurationFactory factory, String name)
	{
		super(project, factory, name);
	}

	@NotNull
	@Override
	public SettingsEditor<? extends RunConfiguration> getConfigurationEditor()
	{
		return new PerlRemoteDebuggingConfigurationEditor(getProject());
	}

	@Override
	public void checkConfiguration() throws RuntimeConfigurationException
	{

	}

	@Override
	public void readExternal(Element element) throws InvalidDataException
	{
		super.readExternal(element);
		DefaultJDOMExternalizer.readExternal(this, element);
	}

	@Override
	public void writeExternal(Element element) throws WriteExternalException
	{
		super.writeExternal(element);
		DefaultJDOMExternalizer.writeExternal(this, element);
	}

	@Nullable
	@Override
	public RunProfileState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment environment) throws ExecutionException
	{
		if (executor instanceof DefaultDebugExecutor)
		{
			return new PerlRemoteDebuggingRunProfileState(environment);
		}
		return null;
	}

	public boolean isPerlServer()
	{
		return isPerlServer;
	}

	public void setPerlServer(boolean perlServer)
	{
		this.isPerlServer = perlServer;
	}

	public String getDebugHost()
	{
		return debugHost;
	}

	public void setDebugHost(String debugHost)
	{
		this.debugHost = debugHost;
	}

	public String getDebugPort()
	{
		return debugPort;
	}

	public void setDebugPort(String debugPort)
	{
		this.debugPort = debugPort;
	}

	public String getRemoteWorkingDirectory()
	{
		return remoteWorkingDirectory;
	}

	public void setRemoteWorkingDirectory(String remoteWorkingDirectory)
	{
		this.remoteWorkingDirectory = remoteWorkingDirectory;
	}
}
