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
import com.intellij.openapi.projectRoots.ProjectJdkTable;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.PlatformUtils;
import com.perl5.lang.perl.idea.modules.JpsPerlLibrarySourceRootType;
import com.perl5.lang.perl.idea.sdk.PerlSdkType;
import com.perl5.lang.perl.idea.settings.Perl5Settings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.model.serialization.PathMacroUtil;

import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

		String scriptPath = runProfile.getScriptPath();
		if (scriptPath == null)
		{
			throw new ExecutionException("Please setup script file");
		}

		VirtualFile scriptFile = LocalFileSystem.getInstance().findFileByPath(scriptPath);
		if (scriptFile == null)
		{
			throw new ExecutionException("Script file: " + scriptPath + " is not exists");
		}

		String perlSdkPath = null;
		List<String> includePaths = Collections.emptyList();
		if (PlatformUtils.isIntelliJ())
		{
			Module moduleForFile = ModuleUtilCore.findModuleForFile(scriptFile, getEnvironment().getProject());
			if (moduleForFile == null)
			{
				perlSdkPath = null;
			}
			else
			{
				Sdk sdk = ModuleRootManager.getInstance(moduleForFile).getSdk();
				if (sdk == null)
				{
					perlSdkPath = null;
				}
				else
				{
					perlSdkPath = sdk.getHomePath();
					List<VirtualFile> sourceRoots = ModuleRootManager.getInstance(moduleForFile).getSourceRoots(JpsPerlLibrarySourceRootType.INSTANCE);
					if(!sourceRoots.isEmpty())
					{
						includePaths = new ArrayList<String>(sourceRoots.size());
						for (VirtualFile sourceRoot : sourceRoots)
						{
							includePaths.add(sourceRoot.getPath());
						}
					}
				}
			}
		}
		else
		{
			Perl5Settings perl5Settings = Perl5Settings.getInstance(getEnvironment().getProject());
			perlSdkPath = perl5Settings.perlPath;
			includePaths = perl5Settings.libRoots;
		}

		String alternativeSdkPath = runProfile.ALTERNATIVE_SDK_PATH;
		if(runProfile.isUseAlternativeSdk() && !StringUtil.isEmpty(alternativeSdkPath))
		{
			Sdk sdk = ProjectJdkTable.getInstance().findJdk(alternativeSdkPath);
			if(sdk != null)
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
			throw new ExecutionException("Perl SDK is not set");
		}

		String homePath = runProfile.WORKING_DIRECTORY;
		if (StringUtil.isEmpty(homePath))
		{
			Module moduleForFile = ModuleUtilCore.findModuleForFile(scriptFile, getEnvironment().getProject());
			if (moduleForFile != null)
			{
				homePath = PathMacroUtil.getModuleDir(moduleForFile.getModuleFilePath());
			}
		}

		assert homePath != null;

		GeneralCommandLine commandLine = new GeneralCommandLine();
		commandLine.setExePath(PerlSdkType.getInstance().getExecutablePath(perlSdkPath));
		for (String includePath : includePaths)
		{
			commandLine.addParameter("-I" + FileUtil.toSystemIndependentName(includePath));
		}
		commandLine.addParameter(scriptPath);
		String programParameters = runProfile.getProgramParameters();
		if (programParameters != null)
		{
			commandLine.addParameters(StringUtil.split(programParameters, " "));
		}

		String charsetName = runProfile.getCharset();
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
		commandLine.withEnvironment(runProfile.getEnvs());
		commandLine.setPassParentEnvironment(runProfile.isPassParentEnvs());
		OSProcessHandler handler = new OSProcessHandler(commandLine.createProcess(), commandLine.getCommandLineString(), charset);
		ProcessTerminatedListener.attach(handler, getEnvironment().getProject());
		return handler;
	}
}
