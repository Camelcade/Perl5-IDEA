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

package com.perl5.lang.perl.idea.project;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.perl5.lang.perl.idea.configuration.settings.PerlSharedSettings;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 30.08.2015.
 */
public class PerlMicroIdeSettingsLoader implements ProjectComponent
{
	protected Project myProject;
	protected PerlSharedSettings perl5Settings;

	public PerlMicroIdeSettingsLoader(Project project)
	{
		myProject = project;
		perl5Settings = PerlSharedSettings.getInstance(project);
	}

	public void initComponent()
	{
		// TODO: insert component initialization logic here
	}

	public void disposeComponent()
	{
		// TODO: insert component disposal logic here
	}

	@NotNull
	public String getComponentName()
	{
		return "PerlMicroIdeSettingsLoader";
	}

	public void projectOpened()
	{
		// called when project is opened
		final Module[] modules = ModuleManager.getInstance(myProject).getModules();
//		if (!PlatformUtils.isIntelliJ() && modules.length > 0)
//		{
//			ApplicationManager.getApplication().runWriteAction(new Runnable()
//			{
//				@Override
//				public void run()
//				{
//					for (Module module : modules)
//					{
//						ModifiableRootModel rootModel = ModuleRootManager.getInstance(module).getModifiableModel();
//						ContentEntry[] entries = rootModel.getContentEntries();
//
//						if (entries.length > 0)
//						{
//							ContentEntry entry = entries[0];
//							Set<String> libPaths = new HashSet<String>(perl5Settings.getLibRootUrlsForModule(module));
//
//							for (SourceFolder folder : entry.getSourceFolders())
//							{
//								if (libPaths.contains(folder.getUrl()))
//								{
//									entry.removeSourceFolder(folder);
//								}
//							}
//
//							final String rootPath = VfsUtilCore.urlToPath(entry.getUrl());
//							for (String path : libPaths)
//							{
//								if (FileUtil.isAncestor(rootPath, VfsUtilCore.urlToPath(path), true))
//								{
//									entry.addSourceFolder(path, JpsPerlLibrarySourceRootType.INSTANCE);
//								}
//							}
//							rootModel.commit();
//						}
//					}
//					PerlLibUtil.applyClassPaths(myProject);
//				}
//			});
//		}

	}

	public void projectClosed()
	{
		// called when project is being closed
	}

}
