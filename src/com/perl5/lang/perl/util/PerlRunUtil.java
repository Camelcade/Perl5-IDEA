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

package com.perl5.lang.perl.util;

import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.PlatformUtils;
import com.perl5.lang.perl.idea.configuration.settings.Perl5Settings;
import com.perl5.lang.perl.idea.sdk.PerlSdkType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 26.04.2016.
 */
public class PerlRunUtil
{
	@Nullable
	public static GeneralCommandLine getPerlCommandLine(@NotNull Project project, @Nullable VirtualFile scriptFile, String... perlParameters)
	{
		String perlPath = getPerlPath(project, scriptFile);
		return perlPath == null ? null : getPerlCommandLine(project, perlPath, scriptFile, perlParameters);
	}

	@NotNull
	public static GeneralCommandLine getPerlCommandLine(@NotNull Project project, @NotNull String perlDirectory, @Nullable VirtualFile scriptFile, String... perlParameters)
	{
		GeneralCommandLine commandLine = new GeneralCommandLine();
		String executablePath = PerlSdkType.getInstance().getExecutablePath(perlDirectory);
		commandLine.setExePath(FileUtil.toSystemIndependentName(executablePath));
		for (String libRoot : Perl5Settings.getInstance(project).libRootUrls)
		{
			String includePath = VfsUtil.urlToPath(libRoot);
			commandLine.addParameter("-I" + FileUtil.toSystemIndependentName(includePath));
		}

		commandLine.addParameters(perlParameters);

		if (scriptFile != null)
		{
			commandLine.addParameter(scriptFile.getPath());
		}
		return commandLine;
	}

	@Nullable
	public static String getPerlPath(@NotNull Project project, @Nullable VirtualFile scriptFile)
	{
		String perlSdkPath = null;
		if (PlatformUtils.isIntelliJ())
		{
			Module moduleForFile = scriptFile == null ? null : ModuleUtilCore.findModuleForFile(scriptFile, project);
			if (moduleForFile == null)
			{
				Sdk projectSdk = ProjectRootManager.getInstance(project).getProjectSdk();
				if (projectSdk != null)
				{
					perlSdkPath = projectSdk.getHomePath();
				}
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
				}
			}
		}
		else
		{
			perlSdkPath = Perl5Settings.getInstance(project).perlPath;
		}

		return perlSdkPath;
	}
}
