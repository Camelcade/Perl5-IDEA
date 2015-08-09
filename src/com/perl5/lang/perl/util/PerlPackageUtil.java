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
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.psi.stubs.StubIndex;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.idea.refactoring.RenameRefactoringQueue;
import com.perl5.lang.perl.idea.stubs.namespaces.PerlNamespaceDefinitionStubIndex;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.PerlNamespaceDefinition;
import com.perl5.lang.perl.psi.PsiPerlNamespaceDefinition;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * Created by hurricup on 24.04.2015.
 */
public class PerlPackageUtil implements PerlElementTypes, PerlPackageUtilBuiltIn
{
	public static final String PACKAGE_SEPARATOR = "::";
	public static final String PACKAGE_SEPARATOR_LEGACY = "'";
	public static final Pattern PACKAGE_SEPARATOR_RE = Pattern.compile(PACKAGE_SEPARATOR + "|" + PACKAGE_SEPARATOR_LEGACY);
	public static final Pattern PACKAGE_SEPARATOR_TAIL_RE = Pattern.compile("(" + PACKAGE_SEPARATOR + "|" + PACKAGE_SEPARATOR_LEGACY + ")$");

	public static final HashSet<String> BUILT_IN_ALL = new HashSet<String>();
	public static final ConcurrentHashMap<String, String> CANONICAL_NAMES_CACHE = new ConcurrentHashMap<String, String>();

	static
	{
		BUILT_IN_ALL.addAll(BUILT_IN);
		BUILT_IN_ALL.addAll(BUILT_IN_PRAGMA);
		BUILT_IN_ALL.addAll(BUILT_IN_DEPRECATED);
	}

	/**
	 * Checks if package is built in
	 *
	 * @param pacakgeName package name
	 * @return result
	 */
	public static boolean isBuiltIn(String pacakgeName)
	{
		return BUILT_IN_ALL.contains(getCanonicalPackageName(pacakgeName));
	}

	/**
	 * Checks if package is pragma
	 *
	 * @param pacakgeName package name
	 * @return result
	 */
	public static boolean isPragma(String pacakgeName)
	{
		return BUILT_IN_PRAGMA.contains(getCanonicalPackageName(pacakgeName));
	}

	/**
	 * Checks if package is deprecated
	 *
	 * @param pacakgeName package name
	 * @return result
	 */
	public static boolean isDeprecated(String pacakgeName)
	{
		return BUILT_IN_DEPRECATED.contains(getCanonicalPackageName(pacakgeName));
	}

	/**
	 * Make canonical package name.
	 *
	 * @param name package name
	 * @return canonical package name
	 */
	public static String getCanonicalPackageName(String name)
	{
		String newName;

		if ((newName = CANONICAL_NAMES_CACHE.get(name)) != null)
			return newName;

		String originalName = name;

//		System.out.println("Source: " + name);
		// removing trailing separator if any
		name = PACKAGE_SEPARATOR_TAIL_RE.matcher(name).replaceFirst("");
//		System.out.println("Notail: " + name);

		ArrayList<String> canonicalChunks = new ArrayList<String>();
		String[] chunks = PACKAGE_SEPARATOR_RE.split(name, -1);

//		System.out.println("Chunks: " + chunks.length);

		if (chunks.length > 0 && chunks[0].equals(""))    // implicit main
			chunks[0] = "main";

		for (String chunk : chunks)
			if (!(canonicalChunks.size() == 0 && chunk.equals("main")))
				canonicalChunks.add(chunk);

//		System.out.println("Canonical chunks: " + chunks.length);

		if (canonicalChunks.size() == 0)
			newName = "main";
		else
			newName = StringUtils.join(canonicalChunks, "::");

//		System.out.println("Canonical: " + newName + "\n");
		CANONICAL_NAMES_CACHE.put(originalName, newName);

		return newName;
	}

	/**
	 * Searching of namespace element is in. If no explicit namespaces defined, main is returned
	 *
	 * @param element psi element to find definition for
	 * @return canonical package name
	 */
	@NotNull
	public static String getContextPackageName(PsiElement element)
	{
		PerlNamespaceDefinition namespaceDefinition = PsiTreeUtil.getParentOfType(element, PerlNamespaceDefinition.class);

		if (namespaceDefinition != null && namespaceDefinition.getNamespaceElement() != null) // checking that definition is valid and got namespace
		{
			String name = namespaceDefinition.getNamespaceElement().getCanonicalName();
			assert name != null;
			return name;
		}

		// default value
		return "main";
	}

	/**
	 * Searching project files for namespace definitions by specific package name
	 *
	 * @param project     project to search in
	 * @param packageName canonical package name (without tailing ::)
	 * @return collection of found definitions
	 */
	public static Collection<PsiPerlNamespaceDefinition> getNamespaceDefinitions(Project project, String packageName)
	{
		assert packageName != null;

		return StubIndex.getElements(PerlNamespaceDefinitionStubIndex.KEY, packageName, project, GlobalSearchScope.allScope(project), PsiPerlNamespaceDefinition.class);
	}

	/**
	 * Returns list of defined package names
	 *
	 * @param project project to search in
	 * @return collection of package names
	 */
	public static Collection<String> getDefinedPackageNames(Project project)
	{
		return StubIndex.getInstance().getAllKeys(PerlNamespaceDefinitionStubIndex.KEY, project);
	}

	/**
	 * Builds package path from packageName Foo::Bar => Foo/Bar.pm
	 *
	 * @param packageName canonical package name
	 * @return package path
	 */
	public static String getPackagePathByName(String packageName)
	{
		return StringUtils.join(packageName.split(":+"), '/') + ".pm";
	}

	/**
	 * Translates package relative name to the package name Foo/Bar.pm => Foo::Bar
	 *
	 * @param packagePath package relative path
	 * @return canonical package name
	 */
	public static String getPackageNameByPath(String packagePath)
	{
		packagePath = packagePath.replaceAll("\\\\", "/");

		return StringUtils.join(packagePath.replaceFirst("\\.pm$", "").split("/"), PACKAGE_SEPARATOR);
	}

	/**
	 * Adds to queue netsted namespaces, which names should be adjusted to the new package name/path
	 *
	 * @param queue   - RenameRefactoringQueue
	 * @param file    - file has been moved
	 * @param oldPath - previous filepath
	 */
	public static void handleMovedPackageNamespaces(@NotNull RenameRefactoringQueue queue, VirtualFile file, String oldPath)
	{
		Project project = queue.getProject();
		VirtualFile newInnermostRoot = PerlUtil.getFileClassRoot(project, file);

		if (newInnermostRoot != null)
		{
			String newRelativePath = VfsUtil.getRelativePath(file, newInnermostRoot);
			String newPackageName = PerlPackageUtil.getPackageNameByPath(newRelativePath);

			VirtualFile oldInnermostRoot = PerlUtil.getFileClassRoot(ModuleUtil.findModuleForFile(file, project), oldPath);

			if (oldInnermostRoot != null)
			{
				String oldRelativePath = Paths.get(oldInnermostRoot.getPath()).relativize(Paths.get(oldPath)).toString();
				String oldPackageName = PerlPackageUtil.getPackageNameByPath(oldRelativePath);

				if (!oldPackageName.equals(newPackageName))
				{
					PsiFile psiFile = PsiManager.getInstance(project).findFile(file);
					if (psiFile != null)
						for (PsiPerlNamespaceDefinition namespaceDefinition : PsiTreeUtil.findChildrenOfType(psiFile, PsiPerlNamespaceDefinition.class))
							if (oldPackageName.equals(namespaceDefinition.getPackageName()))
								queue.addElement(namespaceDefinition, newPackageName);
				}
			}
		}
	}

	/**
	 * Searches for all pm files and add renaming of nested package definitions to the queue. Invoked after renaming
	 *
	 * @param queue     RenameRefactoringQueue object
	 * @param directory VirtualFile of renamed directory
	 * @param oldPath   old directory path
	 */
	public static void handlePackagePathChange(RenameRefactoringQueue queue, VirtualFile directory, String oldPath)
	{
		Project project = queue.getProject();
		VirtualFile directorySourceRoot = PerlUtil.getFileClassRoot(project, directory);

		if (directorySourceRoot != null)
			for (VirtualFile file : VfsUtil.collectChildrenRecursively(directory))
				if (!file.isDirectory() && "pm".equals(file.getExtension()) && directorySourceRoot.equals(PerlUtil.getFileClassRoot(project, file)))
				{
					String relativePath = VfsUtil.getRelativePath(file, directory);
					String oldFilePath = oldPath + "/" + relativePath;
					handleMovedPackageNamespaces(queue, file, oldFilePath);
				}
	}

	/**
	 * Searches for all pm files in directory to be renamed/moved, searches for references to those packages and add them to the renaming queue
	 *
	 * @param queue     RenameRefactoringQueue object
	 * @param directory VirtualFile of directory to be renamed
	 * @param newPath   new directory path
	 */
	public static void handlePackagePathChangeReferences(RenameRefactoringQueue queue, VirtualFile directory, String newPath)
	{
		Project project = queue.getProject();
		VirtualFile oldDirectorySourceRoot = PerlUtil.getFileClassRoot(project, directory);
		PsiManager psiManager = PsiManager.getInstance(project);

		if (oldDirectorySourceRoot != null)
		{
			for (VirtualFile file : VfsUtil.collectChildrenRecursively(directory))
				if (!file.isDirectory() && "pm".equals(file.getExtension()) && oldDirectorySourceRoot.equals(PerlUtil.getFileClassRoot(project, file)))
				{
					PsiFile psiFile = psiManager.findFile(file);

					if (psiFile != null)
						for (PsiReference inboundReference : ReferencesSearch.search(psiFile))
						{
							String newPackagePath = newPath + "/" + VfsUtil.getRelativePath(file, directory);
							VirtualFile newInnermostRoot = PerlUtil.getFileClassRoot(ModuleUtil.findModuleForPsiElement(psiFile), newPackagePath);
							String newRelativePath = Paths.get(newInnermostRoot.getPath()).relativize(Paths.get(newPackagePath)).toString();
							String newPackageName = PerlPackageUtil.getPackageNameByPath(newRelativePath);

							queue.addElement(inboundReference.getElement(), newPackageName);
						}
				}
		}
	}


	/**
	 * Returns list of Package names available as pm files for specific psi element
	 * todo we could cache this with invalidating on file event
	 *
	 * @param element base PsiElement
	 * @return list of distinct strings
	 */
	public static List<String> getPackageFilesForPsiElement(PsiElement element)
	{
		HashSet<String> result = new HashSet<String>();

		if (element != null)
		{
			Module module = ModuleUtil.findModuleForPsiElement(element);
			VirtualFile[] classRoots;

			if (module != null)
				classRoots = ModuleRootManager.getInstance(module).orderEntries().classes().getRoots();
			else
				classRoots = ProjectRootManager.getInstance(element.getProject()).orderEntries().getClassesRoots();

			for (VirtualFile classRoot : classRoots)
			{
				for (VirtualFile virtualFile : VfsUtil.collectChildrenRecursively(classRoot))
					if (!virtualFile.isDirectory() && "pm".equals(virtualFile.getExtension()))
					{
						String relativePath = VfsUtil.getRelativePath(virtualFile, classRoot);
						String packageName = PerlPackageUtil.getPackageNameByPath(relativePath);

						result.add(packageName);
					}
			}
		}
		return new ArrayList<String>(result);
	}

}
