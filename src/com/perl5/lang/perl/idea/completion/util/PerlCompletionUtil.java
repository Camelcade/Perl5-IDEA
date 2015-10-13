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

package com.perl5.lang.perl.idea.completion.util;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.psi.search.GlobalSearchScope;

/**
 * Created by evstigneev on 13.10.2015.
 */
public class PerlCompletionUtil
{
	public static GlobalSearchScope getCompletionScope(CompletionParameters parameters)
	{
		Project project = parameters.getPosition().getProject();

		Module currentModule = ProjectRootManager.getInstance(project).getFileIndex().getModuleForFile(parameters.getOriginalFile().getVirtualFile());

		if (currentModule != null)
		{
			return GlobalSearchScope.moduleWithDependenciesAndLibrariesScope(currentModule);
		}
		else
		{
			return GlobalSearchScope.projectScope(project);
		}
	}
}
