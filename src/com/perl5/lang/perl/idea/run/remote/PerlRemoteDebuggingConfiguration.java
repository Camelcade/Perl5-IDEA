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
import com.intellij.openapi.util.text.StringUtil;
import com.perl5.lang.perl.idea.run.debugger.PerlDebugOptions;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 09.05.2016.
 */
public class PerlRemoteDebuggingConfiguration extends RunConfigurationBase implements RunConfigurationWithSuppressedDefaultRunAction, PerlDebugOptions
{
	public String debugHost = "localhost";
	public int debugPort = 12345;
	public String remoteProjectRoot = "/home/";
	public String scriptCharset = "utf8";
	public String perlRole = "client";
	public String startMode = "RUN";
	public boolean isNonInteractiveModeEnabled = false;
	public boolean isCompileTimeBreakpointsEnabled = false;

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

	public String getDebugHost()
	{
		return debugHost;
	}

	public void setDebugHost(String debugHost)
	{
		this.debugHost = debugHost;
	}

	public int getDebugPort()
	{
		return debugPort;
	}

	public void setDebugPort(int debugPort)
	{
		this.debugPort = debugPort;
	}

	public String getRemoteProjectRoot()
	{
		return remoteProjectRoot;
	}

	public void setRemoteProjectRoot(String remoteWorkingDirectory)
	{
		while (StringUtil.endsWith(remoteWorkingDirectory, "/"))
		{
			remoteWorkingDirectory = remoteWorkingDirectory.substring(0, remoteWorkingDirectory.length() - 1);
		}
		this.remoteProjectRoot = remoteWorkingDirectory;
	}

	public String getScriptCharset()
	{
		return scriptCharset;
	}

	public void setScriptCharset(String scriptCharset)
	{
		this.scriptCharset = scriptCharset;
	}

	@Override
	public String getStartMode()
	{
		return startMode;
	}

	public void setStartMode(String startMode)
	{
		this.startMode = startMode;
	}

	@Override
	public String getPerlRole()
	{
		return perlRole;
	}

	public void setPerlRole(String perlRole)
	{
		this.perlRole = perlRole;
	}

	@Override
	public boolean isNonInteractiveModeEnabled()
	{
		return isNonInteractiveModeEnabled;
	}

	public void setNonInteractiveModeEnabled(boolean nonInteractiveModeEnabled)
	{
		isNonInteractiveModeEnabled = nonInteractiveModeEnabled;
	}

	@Override
	public boolean isCompileTimeBreakpointsEnabled()
	{
		return isCompileTimeBreakpointsEnabled;
	}

	public void setCompileTimeBreakpointsEnabled(boolean compileTimeBreakpointsEnabled)
	{
		isCompileTimeBreakpointsEnabled = compileTimeBreakpointsEnabled;
	}
}
