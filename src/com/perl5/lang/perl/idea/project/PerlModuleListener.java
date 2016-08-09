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

package com.perl5.lang.perl.idea.project;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.ModuleListener;
import com.intellij.openapi.project.Project;
import com.intellij.util.Function;
import com.perl5.lang.perl.idea.configuration.settings.PerlSharedSettings;

import java.util.List;

/**
 * Created by hurricup on 09.08.2016.
 */
public class PerlModuleListener implements ModuleListener
{
	@Override
	public void moduleAdded(Project project, Module module)
	{

	}

	@Override
	public void beforeModuleRemoved(Project project, Module module)
	{

	}

	@Override
	public void moduleRemoved(Project project, Module module)
	{

	}

	@Override
	public void modulesRenamed(Project project, List<Module> modules, Function<Module, String> oldNameProvider)
	{
		PerlSharedSettings perlSharedSettings = PerlSharedSettings.getInstance(project);
		for (Module module : modules)
		{
			String oldName = oldNameProvider.fun(module);
			List<String> libRootUrlsForModule = perlSharedSettings.deleteLibRootUrlsForModule(oldName);
			if (libRootUrlsForModule != null)
			{
				perlSharedSettings.setLibRootUrlsForModule(module, libRootUrlsForModule);
			}
		}
	}
}
