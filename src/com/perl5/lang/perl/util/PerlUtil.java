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

package com.perl5.lang.perl.util;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.stubs.StubIndex;
import com.intellij.psi.stubs.StubIndexKey;
import com.intellij.util.IncorrectOperationException;
import com.intellij.util.Processor;
import com.perl5.lang.perl.psi.PerlUseStatement;
import gnu.trove.THashSet;

import java.io.File;
import java.util.*;

/**
 * Created by hurricup on 27.05.2015.
 * Misc helper methods
 */
public class PerlUtil
{

	/**
	 * Searches for innermost source root for a file
	 *
	 * @param project project to search in
	 * @param file    containing file
	 * @return innermost root
	 */
	public static VirtualFile getFileClassRoot(Project project, VirtualFile file)
	{
		Module module = ModuleUtil.findModuleForFile(file, project);

		if (module != null)
		{
			for (VirtualFile classRoot : ModuleRootManager.getInstance(module).orderEntries().classes().getRoots())
				if (VfsUtil.isAncestor(classRoot, file, false))
					return classRoot;
		} else
			throw new IncorrectOperationException("Unable to find class root for file outside of the modules");

		return null;
	}

	/**
	 * Searches for innermost source root for a file by it's absolute path
	 *
	 * @param module   module to search in
	 * @param filePath containing filename
	 * @return innermost root
	 */
	public static VirtualFile getFileClassRoot(Module module, String filePath)
	{
		if (module != null)
		{
			File file = new File(filePath);

			for (VirtualFile classRoot : ModuleRootManager.getInstance(module).orderEntries().classes().getRoots())
			{
				File sourceRootFile = new File(classRoot.getPath());
				if (VfsUtil.isAncestor(sourceRootFile, file, false))
					return classRoot;
			}
		}

		return null;
	}

	public static Collection<String> getIndexKeysWithoutInternals(StubIndexKey<String, ?> key, Project project)
	{
		final Set<String> result = new THashSet<String>();

		StubIndex.getInstance().processAllKeys(key, project, new
				PerlInternalIndexKeysProcessor()
				{
					@Override
					public boolean process(String name)
					{
						if (super.process(name))
							result.add(name);
						return true;
					}
				});

		return result;
	}

	/**
	 * Returns a map of imported names, filtered by specific processor
	 *
	 * @param project   Project to search in
	 * @param namespace namespace to search in
	 * @param file      PsiFile to search in
	 * @return result map
	 */
	public static Map<String, Set<String>> getImportedNames(Project project, String namespace, PsiFile file, Processor<String> processor)
	{
		Map<String, Set<String>> result = new HashMap<String, Set<String>>();

		for (PerlUseStatement useStatement : PerlPackageUtil.getPackageImports(project, namespace, file))
		{
			String packageName = useStatement.getPackageName();

			if (packageName != null)
			{
				List<String> imports = useStatement.getPackageProcessor().getImportedSubs(useStatement);

				Set<String> currentSet = result.get(packageName);
				if (imports != null)
					for (String item : imports)
						if (processor.process(item))
						{
							if (!result.containsKey(packageName))
								result.put(packageName, currentSet = new HashSet<String>());

							currentSet.add(item);
						}
			}
		}

		return result;
	}


}
