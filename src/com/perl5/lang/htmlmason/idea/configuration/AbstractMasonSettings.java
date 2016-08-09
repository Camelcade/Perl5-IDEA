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

package com.perl5.lang.htmlmason.idea.configuration;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.EditorNotifications;
import com.intellij.util.xmlb.annotations.Transient;
import com.perl5.lang.mason2.idea.configuration.VariableDescription;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hurricup on 05.03.2016.
 */
public abstract class AbstractMasonSettings
{
	public List<String> componentRoots = new ArrayList<String>();
	public List<VariableDescription> globalVariables = new ArrayList<VariableDescription>();

	@Transient
	protected int changeCounter = 0;

	@Transient
	protected Project myProject;
	@Transient
	private List<VirtualFile> componentsRootsVirtualFiles = null;

	public void settingsUpdated()
	{
		componentsRootsVirtualFiles = null;
		changeCounter++;
		EditorNotifications.getInstance(myProject).updateAllNotifications();
	}

	@NotNull
	public List<VirtualFile> getComponentsRootsVirtualFiles()
	{
		if (componentsRootsVirtualFiles == null)
		{
			componentsRootsVirtualFiles = new ArrayList<VirtualFile>();
			for (String relativeRoot : componentRoots)
			{
				VirtualFile rootFile = VfsUtil.findRelativeFile(relativeRoot, myProject.getBaseDir());
				if (rootFile != null && rootFile.exists())
				{
					componentsRootsVirtualFiles.add(rootFile);
				}
			}
		}
		return componentsRootsVirtualFiles;
	}

	public int getChangeCounter()
	{
		return changeCounter;
	}


}
