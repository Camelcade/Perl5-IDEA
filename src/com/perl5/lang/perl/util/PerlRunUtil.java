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
import com.intellij.execution.util.ExecUtil;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationListener;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.roots.ui.configuration.ProjectSettingsService;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.PlatformUtils;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.idea.configuration.settings.PerlLocalSettings;
import com.perl5.lang.perl.idea.configuration.settings.PerlSettingsConfigurable;
import com.perl5.lang.perl.idea.configuration.settings.PerlSharedSettings;
import com.perl5.lang.perl.idea.sdk.PerlSdkType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.event.HyperlinkEvent;
import java.util.Collections;
import java.util.List;

/**
 * Created by hurricup on 26.04.2016.
 */
public class PerlRunUtil
{
	public static final String PERL_RUN_ERROR_GROUP = "PERL_RUN_ERROR_GROUP";

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
		commandLine.setExePath(FileUtil.toSystemDependentName(executablePath));
		for (String libRoot : PerlSharedSettings.getInstance(project).libRootUrls)
		{
			String includePath = VfsUtil.urlToPath(libRoot);
			commandLine.addParameter("-I" + FileUtil.toSystemDependentName(includePath));
		}

		commandLine.addParameters(perlParameters);

		if (scriptFile != null)
		{
			commandLine.addParameter(FileUtil.toSystemDependentName(scriptFile.getPath()));
		}
		return commandLine;
	}

	@Nullable
	public static String getPerlPath(@NotNull Project project, @Nullable VirtualFile scriptFile)
	{
		if (PlatformUtils.isIntelliJ())
		{
			Module moduleForFile = scriptFile == null ? null : ModuleUtilCore.findModuleForFile(scriptFile, project);

			// found in file module
			String perlPath = getModuleSdkPath(moduleForFile);
			if (perlPath != null)
			{
				return perlPath;
			}

			// found in project
			Sdk projectSdk = ProjectRootManager.getInstance(project).getProjectSdk();
			if (projectSdk != null && projectSdk.getSdkType() == PerlSdkType.getInstance())
			{
				return projectSdk.getHomePath();
			}

			// looking for any perl module in project
			for (Module module : ModuleManager.getInstance(project).getModules())
			{
				perlPath = getModuleSdkPath(module);
				if (perlPath != null)
				{
					return perlPath;
				}
			}
			showSdkConfigurationError(PerlBundle.message("perl.error.idea.project.or.module.sdk"), project);
		}
		else
		{
			String perlPath = PerlLocalSettings.getInstance(project).PERL_PATH;
			if (StringUtil.isNotEmpty(perlPath))
			{
				return perlPath;
			}
			else
			{
				showSdkConfigurationError(PerlBundle.message("perl.error.micro.project.or.module.sdk"), project);
			}
		}
		return null;
	}

	@Nullable
	private static String getModuleSdkPath(Module module)
	{
		if (module == null)
		{
			return null;
		}

		Sdk sdk = ModuleRootManager.getInstance(module).getSdk();
		if (sdk != null && sdk.getSdkType() == PerlSdkType.getInstance())
		{
			return sdk.getHomePath();
		}
		return null;
	}

	private static void showSdkConfigurationError(String message, final Project project)
	{
		Notifications.Bus.notify(new Notification(
				PERL_RUN_ERROR_GROUP,
				"SDK Configuration Error",
				"<p>" + message + "</p>" +
						"<br/>" +
						"<p><a href=\"configure\">Configure...</a></p>" +
						"<br/>"
				,
				NotificationType.ERROR,
				new NotificationListener.UrlOpeningListener(false)
				{
					@Override
					protected void hyperlinkActivated(@NotNull Notification notification, @NotNull HyperlinkEvent event)
					{
						if (PlatformUtils.isIntelliJ())
						{
							ProjectSettingsService.getInstance(project).openProjectSettings();
						}
						else
						{
							ShowSettingsUtil.getInstance().editConfigurable(project, new PerlSettingsConfigurable(project));
						}
						notification.expire();
					}
				}
		));
	}

	@Nullable
	public static String getPathFromPerl()
	{
		List<String> perlPathLines = getDataFromProgram("perl", "-le", "print $^X");

		if (perlPathLines.size() == 1)
		{
			int perlIndex = perlPathLines.get(0).lastIndexOf("perl");
			if (perlIndex > 0)
			{
				return perlPathLines.get(0).substring(0, perlIndex);
			}

		}
		return null;
	}

	@NotNull
	public static List<String> getDataFromProgram(String... command)
	{
		try
		{
			GeneralCommandLine commandLine = new GeneralCommandLine(command);
			return ExecUtil.execAndGetOutput(commandLine).getStdoutLines();

		}
		catch (Exception e)
		{
//			throw new IncorrectOperationException("Error executing external perl, please report to plugin developers: " + e.getMessage());
			return Collections.emptyList();
		}
	}


}
