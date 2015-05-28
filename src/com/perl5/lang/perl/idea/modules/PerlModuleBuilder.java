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

package com.perl5.lang.perl.idea.modules;

import com.intellij.ide.util.projectWizard.JavaModuleBuilder;
import com.intellij.ide.util.projectWizard.ModuleBuilder;
import com.intellij.ide.util.projectWizard.ModuleBuilderListener;
import com.intellij.ide.util.projectWizard.SourcePathsBuilder;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.util.Pair;
import org.jetbrains.annotations.NonNls;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hurricup on 28.05.2015.
 */
public class PerlModuleBuilder extends JavaModuleBuilder
{
	private List<Pair<String, String>> mySourcePaths = new ArrayList<>();

	@Override
	public ModuleType getModuleType()
	{
		return PerlModuleType.INSTANCE;
	}

//	@Override
//	public List<Pair<String, String>> getSourcePaths()
//	{
//		return mySourcePaths;
//	}
//
//	@Override
//	public void setSourcePaths(List<Pair<String, String>> sourcePaths)
//	{
//		mySourcePaths = sourcePaths != null ? new ArrayList<>(sourcePaths) : null;
//	}
//
//	@Override
//	public void addSourcePath(Pair<String, String> sourcePathInfo)
//	{
//		if (mySourcePaths == null) {
//			mySourcePaths = new ArrayList<>();
//		}
//		mySourcePaths.add(sourcePathInfo);
//	}

}
