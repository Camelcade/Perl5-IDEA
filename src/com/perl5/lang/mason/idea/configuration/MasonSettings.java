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
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.intellij.util.xmlb.annotations.Transient;
import com.perl5.lang.perl.psi.PerlVariable;
import com.perl5.lang.perl.psi.utils.PerlVariableType;
import gnu.trove.THashSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Created by hurricup on 03.01.2016.
 */
@State(
		name = "Perl5MasonSettings",
		storages = {
				@Storage(file = StoragePathMacros.PROJECT_FILE),
				@Storage(file = StoragePathMacros.PROJECT_CONFIG_DIR + "/perl5.xml", scheme = StorageScheme.DIRECTORY_BASED)
		}
)


public class MasonSettings implements PersistentStateComponent<MasonSettings>
{
	public List<String> componentRoots = new ArrayList<String>();
	public List<String> autobaseNames = new ArrayList<String>(Arrays.asList("Base.mp", "Base.mc"));
	public List<String> globalVariables = new ArrayList<String>();

	@Transient
	private Set<String> myGLobalVariables;
	@Transient
	private Set<String> myGlobalScalarNames;
	@Transient
	private Set<String> myGlobalArrayNames;
	@Transient
	private Set<String> myGlobalHashNames;

	@Transient
	private Project myProject;
	@Transient
	private List<VirtualFile> componentsRootsVirtualFiles = null;

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
		initGlobalVariablesSets();
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

	private void initGlobalVariablesSets()
	{
		myGLobalVariables = new THashSet<String>(globalVariables);
		myGLobalVariables.add("$m");

		myGlobalScalarNames = new THashSet<String>();
		myGlobalScalarNames.add("m");

		myGlobalArrayNames = new THashSet<String>();
		myGlobalHashNames = new THashSet<String>();

		for (String var : globalVariables)
		{
			if (StringUtil.isNotEmpty(var))
			{
				if (var.charAt(0) == '$')
				{
					myGlobalScalarNames.add(var.substring(1));
				}
				else if (var.charAt(0) == '@')
				{
					myGlobalArrayNames.add(var.substring(1));
				}
				else if (var.charAt(0) == '%')
				{
					myGlobalHashNames.add(var.substring(1));
				}
			}
		}
	}

	public boolean isGlobalVariable(@NotNull PerlVariable variable)
	{
		PerlVariableType variableType = variable.getActualType();

		if (variableType == PerlVariableType.SCALAR)
		{
			return isGlobalScalar(variable.getName());
		}
		else if (variableType == PerlVariableType.ARRAY)
		{
			return isGlobalArray(variable.getName());
		}
		else if (variableType == PerlVariableType.HASH)
		{
			return isGlobalHash(variable.getName());
		}

		return false;

	}

	public boolean isGlobalScalar(String variableName)
	{
		return myGlobalScalarNames.contains(variableName);
	}

	public boolean isGlobalArray(String variableName)
	{
		return myGlobalArrayNames.contains(variableName);
	}

	public boolean isGlobalHash(String variableName)
	{
		return myGlobalHashNames.contains(variableName);
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
		initGlobalVariablesSets();
	}

	public List<String> getGlobalVariables()
	{
		return new ArrayList<String>(myGLobalVariables);
	}
}
