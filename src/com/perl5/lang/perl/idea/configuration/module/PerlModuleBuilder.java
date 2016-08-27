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

package com.perl5.lang.perl.idea.configuration.module;

import com.intellij.ide.util.projectWizard.ModuleBuilder;
import com.intellij.ide.util.projectWizard.SourcePathsBuilder;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.projectRoots.SdkTypeId;
import com.intellij.openapi.roots.CompilerModuleExtension;
import com.intellij.openapi.roots.ContentEntry;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.roots.libraries.Library;
import com.intellij.openapi.roots.libraries.LibraryTable;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.perl5.lang.perl.idea.modules.PerlModuleType;
import com.perl5.lang.perl.idea.sdk.PerlSdkType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hurricup on 28.05.2015.
 * Partially cloned from JavaModuleBuilder
 */
public class PerlModuleBuilder extends ModuleBuilder implements SourcePathsBuilder
{
	private final List<Pair<String, String>> myModuleLibraries = new ArrayList<Pair<String, String>>();
	private List<Pair<String, String>> mySourcePaths;

	private static String getUrlByPath(final String path)
	{
		return VfsUtil.getUrlForLibraryRoot(new File(path));
	}

	@Override
	public void setupRootModel(ModifiableRootModel rootModel) throws ConfigurationException
	{
		final CompilerModuleExtension compilerModuleExtension = rootModel.getModuleExtension(CompilerModuleExtension.class);
		compilerModuleExtension.setExcludeOutput(true);
		if (myJdk != null)
		{
			rootModel.setSdk(myJdk);
		}
		else
		{
			rootModel.inheritSdk();
		}

		ContentEntry contentEntry = doAddContentEntry(rootModel);
		if (contentEntry != null)
		{
			final List<Pair<String, String>> sourcePaths = getSourcePaths();

			if (sourcePaths != null)
			{
				for (final Pair<String, String> sourcePath : sourcePaths)
				{
					String first = sourcePath.first;
					new File(first).mkdirs();
					final VirtualFile sourceRoot = LocalFileSystem.getInstance()
							.refreshAndFindFileByPath(FileUtil.toSystemIndependentName(first));
					if (sourceRoot != null)
					{
						contentEntry.addSourceFolder(sourceRoot, false, sourcePath.second);
					}
				}
			}
		}

		LibraryTable libraryTable = rootModel.getModuleLibraryTable();
		for (Pair<String, String> libInfo : myModuleLibraries)
		{
			final String moduleLibraryPath = libInfo.first;
			final String sourceLibraryPath = libInfo.second;
			Library library = libraryTable.createLibrary();
			Library.ModifiableModel modifiableModel = library.getModifiableModel();
			modifiableModel.addRoot(getUrlByPath(moduleLibraryPath), OrderRootType.CLASSES);
			if (sourceLibraryPath != null)
			{
				modifiableModel.addRoot(getUrlByPath(sourceLibraryPath), OrderRootType.SOURCES);
			}
			modifiableModel.commit();
		}
	}

	@Override
	public ModuleType getModuleType()
	{
		return PerlModuleType.getInstance();
	}

	@Override
	public boolean isSuitableSdkType(SdkTypeId sdkType)
	{
		return sdkType == PerlSdkType.getInstance();
	}

	@Override
	public List<Pair<String, String>> getSourcePaths()
	{
		if (mySourcePaths == null)
		{
			final List<Pair<String, String>> paths = new ArrayList<Pair<String, String>>();
//			paths.add(Pair.create(getContentEntryPath(), ""));
			return paths;
		}
		return mySourcePaths;
	}

	@Override
	public void setSourcePaths(List<Pair<String, String>> sourcePaths)
	{
		mySourcePaths = sourcePaths != null ? new ArrayList<Pair<String, String>>(sourcePaths) : null;
	}

	@Override
	public void addSourcePath(Pair<String, String> sourcePathInfo)
	{
		if (mySourcePaths == null)
		{
			mySourcePaths = new ArrayList<Pair<String, String>>();
		}

		mySourcePaths.add(sourcePathInfo);
	}

	public void addModuleLibrary(String moduleLibraryPath, String sourcePath)
	{
		myModuleLibraries.add(Pair.create(moduleLibraryPath, sourcePath));
	}

//	@Nullable
//	@Override
//	public ModuleWizardStep modifySettingsStep(@NotNull SettingsStep settingsStep) {
//		return PerlModuleType.getInstance().modifySettingsStep(settingsStep, this);
//	}

}
