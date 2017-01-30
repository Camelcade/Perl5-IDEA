/*
 * Copyright 2015 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.run;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.CommandLineState;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.OSProcessHandler;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.process.ProcessTerminatedListener;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.ProjectJdkTable;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.perl5.lang.perl.util.PerlRunUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.model.serialization.PathMacroUtil;

import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.List;
import java.util.Map;

/**
 * @author VISTALL
 * @since 16-Sep-15
 */
public class PerlRunProfileState extends CommandLineState
{
	public PerlRunProfileState(ExecutionEnvironment environment)
	{
		super(environment);
	}

	@NotNull
	@Override
	protected ProcessHandler startProcess() throws ExecutionException
	{
		PerlConfiguration runProfile = (PerlConfiguration) getEnvironment().getRunProfile();

		VirtualFile scriptFile = runProfile.getScriptFile();
		if (scriptFile == null)
		{
			throw new ExecutionException("Script file: " + runProfile.getScriptPath() + " is not exists");
		}

		Project project = getEnvironment().getProject();
		String perlSdkPath = PerlRunUtil.getPerlPath(project, scriptFile);

		String alternativeSdkPath = runProfile.getAlternativeSdkPath();
		if (runProfile.isUseAlternativeSdk() && !StringUtil.isEmpty(alternativeSdkPath))
		{
			Sdk sdk = ProjectJdkTable.getInstance().findJdk(alternativeSdkPath);
			if (sdk != null)
			{
				perlSdkPath = sdk.getHomePath();
			}
			else
			{
				perlSdkPath = alternativeSdkPath;
			}
		}

		if (perlSdkPath == null)
		{
			throw new ExecutionException("Unable to locate Perl Interpreter");
		}

		String homePath = runProfile.getWorkingDirectory();
		if (StringUtil.isEmpty(homePath))
		{
			Module moduleForFile = ModuleUtilCore.findModuleForFile(scriptFile, project);
			if (moduleForFile != null)
			{
				homePath = PathMacroUtil.getModuleDir(moduleForFile.getModuleFilePath());
			}
			else
			{
				homePath = project.getBasePath();
			}
		}

		assert homePath != null;


		GeneralCommandLine commandLine = PerlRunUtil.getPerlCommandLine(project, runProfile, perlSdkPath, scriptFile, getPerlArguments(runProfile));

		String programParameters = runProfile.getProgramParameters();

		if (programParameters != null)
		{
			commandLine.addParameters(StringUtil.split(programParameters, " "));
		}

		String charsetName = runProfile.getConsoleCharset();
		Charset charset = null;
		if (!StringUtil.isEmpty(charsetName))
		{
			try
			{
				charset = Charset.forName(charsetName);
			}
			catch (UnsupportedCharsetException e)
			{
				throw new ExecutionException("Unknown charset: " + charsetName);
			}
		}
		else
		{
			charset = scriptFile.getCharset();
		}

		commandLine.setCharset(charset);
		commandLine.withWorkDirectory(homePath);
		commandLine.withEnvironment(calcEnv(runProfile));
		commandLine.setPassParentEnvironment(runProfile.isPassParentEnvs());
		OSProcessHandler handler = new OSProcessHandler(commandLine.createProcess(), commandLine.getCommandLineString(), charset);
		ProcessTerminatedListener.attach(handler, project);
		return handler;
	}

	@NotNull
	protected String[] getPerlArguments(PerlConfiguration runProfile)
	{

		String perlParameters = runProfile.getPerlParameters();
		if (perlParameters == null)
		{
			return new String[0];
		}
		List<String> result = StringUtil.split(perlParameters, " ");
		return result.toArray(new String[result.size()]);
	}

	protected Map<String, String> calcEnv(PerlConfiguration runProfile) throws ExecutionException
	{
		return runProfile.getEnvs();
	}

}
