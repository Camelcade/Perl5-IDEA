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

package com.perl5.lang.pod;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.UserDataHolderEx;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.perl5.lang.perl.PerlScopes;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * Created by hurricup on 26.03.2016.
 */
public class PodSearchHelper
{
	public static final String PERL_VAR_FILE_NAME = "perlvar.pod";
	public static final String PERL_FUNC_FILE_NAME = "perlfunc.pod";
	public static final String PERL_OP_FILE_NAME = "perlop.pod";
	public static final String PERL_API_FILE_NAME = "perlapi.pod";

	private static final Key<GlobalSearchScope> PERL_VAR_SCOPE = new Key<GlobalSearchScope>(PERL_VAR_FILE_NAME);
	private static final Key<GlobalSearchScope> PERL_FUNC_SCOPE = new Key<GlobalSearchScope>(PERL_FUNC_FILE_NAME);
	private static final Key<GlobalSearchScope> PERL_OP_SCOPE = new Key<GlobalSearchScope>(PERL_OP_FILE_NAME);
	private static final Key<GlobalSearchScope> PERL_API_SCOPE = new Key<GlobalSearchScope>(PERL_API_FILE_NAME);

	@NotNull
	public static GlobalSearchScope getPerlVarScope(@NotNull Project project)
	{
		GlobalSearchScope cached = project.getUserData(PERL_VAR_SCOPE);
		return cached != null ? cached : ((UserDataHolderEx) project).putUserDataIfAbsent(PERL_VAR_SCOPE, createFileScope(project, PERL_VAR_FILE_NAME));
	}

	@NotNull
	public static GlobalSearchScope getPerlApiScope(@NotNull Project project)
	{
		GlobalSearchScope cached = project.getUserData(PERL_API_SCOPE);
		return cached != null ? cached : ((UserDataHolderEx) project).putUserDataIfAbsent(PERL_API_SCOPE, createFileScope(project, PERL_API_FILE_NAME));
	}

	@NotNull
	public static GlobalSearchScope getPerlFuncScope(@NotNull Project project)
	{
		GlobalSearchScope cached = project.getUserData(PERL_FUNC_SCOPE);
		return cached != null ? cached : ((UserDataHolderEx) project).putUserDataIfAbsent(PERL_FUNC_SCOPE, createFileScope(project, PERL_FUNC_FILE_NAME));
	}

	@NotNull
	public static GlobalSearchScope getPerlOpScope(@NotNull Project project)
	{
		GlobalSearchScope cached = project.getUserData(PERL_FUNC_SCOPE);
		return cached != null ? cached : ((UserDataHolderEx) project).putUserDataIfAbsent(PERL_FUNC_SCOPE, createFileScope(project, PERL_OP_FILE_NAME));
	}

	protected static GlobalSearchScope createFileScope(@NotNull Project project, String fileName)
	{
		Collection<VirtualFile> files = FilenameIndex.getVirtualFilesByName(project, fileName, PerlScopes.getProjectAndLibrariesScope(project));

		if (files.size() > 0)
		{
			return GlobalSearchScope.filesScope(project, files);
		}

		return GlobalSearchScope.EMPTY_SCOPE;
	}


}
