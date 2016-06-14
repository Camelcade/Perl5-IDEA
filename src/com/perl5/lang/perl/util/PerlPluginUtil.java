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
import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManager;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;

/**
 * Created by hurricup on 16.04.2016.
 */
public class PerlPluginUtil
{
	public static final String PLUGIN_ID = "com.perl5";

	@Nullable
	public static IdeaPluginDescriptor getPlugin()
	{
		return PluginManager.getPlugin(PluginId.getId(PLUGIN_ID));
	}

	@Nullable
	public static String getPluginVersion()
	{
		IdeaPluginDescriptor plugin = getPlugin();
		return plugin == null ? null : plugin.getVersion();
	}

	@Nullable
	public static String getPluginRoot()
	{
		IdeaPluginDescriptor plugin = PerlPluginUtil.getPlugin();
		if (plugin != null)
		{
			try
			{
				return FileUtil.toSystemIndependentName(plugin.getPath().getCanonicalPath());
			}
			catch (IOException e)
			{
				return null;
			}
		}
		return null;
	}


	@Nullable
	public static String getPluginPerlScriptsRoot()
	{
		String pluginRoot = getPluginRoot();
		return pluginRoot == null ? null : pluginRoot + "/perl";
	}

	@Nullable
	public static VirtualFile getPluginScriptVirtualFile(String scriptName)
	{
		String scriptsRoot = getPluginPerlScriptsRoot();

		if (scriptsRoot != null)
		{
			String scriptPath = scriptsRoot + "/" + scriptName;
			return VfsUtil.findFileByIoFile(new File(scriptPath), true);
		}
		return null;
	}

	@Nullable
	public static GeneralCommandLine getPluginScriptCommandLine(Project project, String script, String... params)
	{
		VirtualFile scriptVirtualFile = getPluginScriptVirtualFile(script);
		if (scriptVirtualFile != null)
		{
			return PerlRunUtil.getPerlCommandLine(project, scriptVirtualFile, params);
		}
		return null;
	}

}
