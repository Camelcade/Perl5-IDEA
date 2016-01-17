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

package com.perl5.lang.mason.idea.configuration;

import com.intellij.openapi.components.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.intellij.util.xmlb.annotations.Transient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by hurricup on 03.01.2016.
 */
@State(
		name = "Perl5MasonSettings",
		storages = {
				@Storage(file = StoragePathMacros.PROJECT_CONFIG_DIR + "/perl5.xml", scheme = StorageScheme.DIRECTORY_BASED)
		}
)


public class MasonSettings implements PersistentStateComponent<MasonSettings>
{
	public List<String> componentRoots = new ArrayList<String>();
	public List<String> autobaseNames = new ArrayList<String>(Arrays.asList("Base.mp", "Base.mc"));
	public List<VariableDescription> globalVariables = new ArrayList<VariableDescription>();

	@Transient
	private int changeCounter = 0;

	@Transient
	private Project myProject;
	@Transient
	private List<VirtualFile> componentsRootsVirtualFiles = null;

	public MasonSettings()
	{
		globalVariables.add(new VariableDescription("$m", "Mason::Request"));
		changeCounter++;
	}

	public static MasonSettings getInstance(@NotNull Project project)
	{
		MasonSettings persisted = ServiceManager.getService(project, MasonSettings.class);
		if (persisted == null)
			persisted = new MasonSettings();

		return persisted.setProject(project);
	}

	private MasonSettings setProject(Project project)
	{
		myProject = project;
		return this;
	}

	public void settingsUpdated()
	{
		componentsRootsVirtualFiles = null;
		changeCounter++;
	}

	@NotNull
	public List<VirtualFile> getComponentsRootsVirtualFiles()
	{
		if (componentsRootsVirtualFiles == null)
		{
			componentsRootsVirtualFiles = new ArrayList<VirtualFile>();
			for (String relativeRoot : componentRoots)
			{
				VirtualFile rootFile = VfsUtil.findRelativeFile(myProject.getBaseDir(), relativeRoot);
				if (rootFile != null && rootFile.exists())
				{
					componentsRootsVirtualFiles.add(rootFile);
				}
			}
		}
		return componentsRootsVirtualFiles;
	}


	@Nullable
	@Override
	public MasonSettings getState()
	{
		return this;
	}

	@Override
	public void loadState(MasonSettings state)
	{
		XmlSerializerUtil.copyBean(state, this);
		changeCounter++;
	}

	public int getChangeCounter()
	{
		return changeCounter;
	}
}
