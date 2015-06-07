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

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.psi.stubs.StubIndex;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.idea.refactoring.RenameRefactoringQueue;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.PsiPerlNamespaceBlock;
import com.perl5.lang.perl.psi.PsiPerlNamespaceDefinition;
import com.perl5.lang.perl.idea.stubs.namespaces.PerlNamespaceDefinitionStubIndex;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Paths;
import java.util.*;

/**
 * Created by hurricup on 24.04.2015.
 */
public class PerlPackageUtil implements PerlElementTypes, PerlPackageUtilBuiltIn
{
    public static final String PACKAGE_SEPARATOR = "::";

    /**
	 * Checks if package is built in
	 * @param pacakgeName package name
	 * @return result
	 */
	public static boolean isBuiltIn(String pacakgeName)
	{
		String canonicalPcakageName = getCanonicalPackageName(pacakgeName);
		return BUILT_IN.contains(canonicalPcakageName) || BUILT_IN_DEPRECATED.contains(canonicalPcakageName) || BUILT_IN_PRAGMA.contains(canonicalPcakageName);
	}

	/**
	 * Make canonical package name, atm crude, jut chop off :: from end and begining
	 * @param name package name
	 * @return canonical package name
	 */
	public static String getCanonicalPackageName(String name)
	{
		if( PACKAGE_SEPARATOR.equals(name))
			return "main";
		else
			return name.replaceFirst("::$", "").replaceFirst("^::", "");
	}

	/**
	 * Searching of namespace element is in. If no explicit namespaces defined, main is returned
	 * @param element psi element to find definition for
	 * @return canonical package name
	 */
	@NotNull
	public static String getContextPackageName(PsiElement element)
	{
		PsiPerlNamespaceBlock namespaceBlock = PsiTreeUtil.getParentOfType(element, PsiPerlNamespaceBlock.class);

		if (namespaceBlock != null)
		{
			PsiPerlNamespaceDefinition namespaceDefinition = namespaceBlock.getNamespaceDefinition();

			if( namespaceDefinition != null ) // checking that definition is valid and got namespace
			{
				String name = namespaceDefinition.getNamespaceElement().getName();
				assert name != null;
				return name;
			}
		}
		// default value
		return "main";
	}

	/**
	 * Searching project files for namespace definitions by specific package name
	 * @param project	project to search in
	 * @param packageName	canonical package name (without tailing ::)
	 * @return	collection of found definitions
	 */
	public static Collection<PsiPerlNamespaceDefinition> findNamespaceDefinitions(Project project, String packageName)
	{
		assert packageName != null;

		return StubIndex.getElements(PerlNamespaceDefinitionStubIndex.KEY, packageName, project, GlobalSearchScope.allScope(project), PsiPerlNamespaceDefinition.class);
	}

	/**
	 * Returns list of defined package names
	 * @param project project to search in
	 * @return collection of package names
	 */
	public static Collection<String> listDefinedPackageNames(Project project)
	{
		return StubIndex.getInstance().getAllKeys(PerlNamespaceDefinitionStubIndex.KEY, project);
	}

	/**
	 * Builds package path from packageName Foo::Bar => Foo/Bar.pm
	 * @param packageName canonical package name
	 * @return package path
	 */
	public static String getPackagePathByName(String packageName)
	{
		return StringUtils.join(packageName.split(":+"), '/') + ".pm";
	}

	/**
	 * Translates package relative name to the package name Foo/Bar.pm => Foo::Bar
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
	 * @param queue - RenameRefactoringQueue
	 * @param file - file has been moved
	 * @param oldPath - previous filepath
	 */
	public static void handleMovedPackageNamespaces(@NotNull RenameRefactoringQueue queue, VirtualFile file, String oldPath)
	{
		Project project = queue.getProject();
		VirtualFile newInnermostRoot = PerlUtil.findInnermostSourceRoot(project, file);

		if (newInnermostRoot != null)
		{
			String newRelativePath = VfsUtil.getRelativePath(file, newInnermostRoot);
			String newPackageName = PerlPackageUtil.getPackageNameByPath(newRelativePath);

			VirtualFile oldInnermostRoot = PerlUtil.findInnermostSourceRoot(project, oldPath);

			if( oldInnermostRoot != null )
			{
				String oldRelativePath = Paths.get(oldInnermostRoot.getPath()).relativize(Paths.get(oldPath)).toString();
				String oldPackageName = PerlPackageUtil.getPackageNameByPath(oldRelativePath);

				if( !oldPackageName.equals(newPackageName))
				{
					PsiFile psiFile = PsiManager.getInstance(project).findFile(file);
					if( psiFile != null)
						for( PsiPerlNamespaceDefinition namespaceDefinition: PsiTreeUtil.findChildrenOfType(psiFile, PsiPerlNamespaceDefinition.class) )
							if( oldPackageName.equals(namespaceDefinition.getPackageName()))
								queue.addElement(namespaceDefinition,newPackageName);
				}
			}
		}
	}

	/**
	 * Searches for all pm files and add renaming of nested package definitions to the queue. Invoked after renaming
	 * @param queue	RenameRefactoringQueue object
	 * @param directory	VirtualFile of renamed directory
	 * @param oldPath old directory path
	 */
	public static void handlePackagePathChange(RenameRefactoringQueue queue, VirtualFile directory, String oldPath)
	{
		Project project = queue.getProject();
		VirtualFile directorySourceRoot = PerlUtil.findInnermostSourceRoot(project, directory);

		if (directorySourceRoot != null)
			for( VirtualFile file: VfsUtil.collectChildrenRecursively(directory))
				if( !file.isDirectory() && "pm".equals(file.getExtension()) && directorySourceRoot.equals(PerlUtil.findInnermostSourceRoot(project, file)) )
				{
					String relativePath = VfsUtil.getRelativePath(file, directory);
					String oldFilePath = oldPath + "/" + relativePath;
					handleMovedPackageNamespaces(queue, file, oldFilePath);
				}
	}

	/**
	 * Searches for all pm files in directory to be renamed/moved, searches for references to those packages and add them to the renaming queue
	 * @param queue RenameRefactoringQueue object
	 * @param directory VirtualFile of renamed directory
	 * @param newPath new directory path
	 */
	public static void handlePackagePathChangeReferences(RenameRefactoringQueue queue, VirtualFile directory, String newPath)
	{
		Project project = queue.getProject();
		VirtualFile oldDirectorySourceRoot = PerlUtil.findInnermostSourceRoot(project, directory);
		PsiManager psiManager = PsiManager.getInstance(project);

		if (oldDirectorySourceRoot != null)
		{
			for( VirtualFile file: VfsUtil.collectChildrenRecursively(directory))
				if( !file.isDirectory() && "pm".equals(file.getExtension()) && oldDirectorySourceRoot.equals(PerlUtil.findInnermostSourceRoot(project, file)) )
				{
					PsiFile psiFile = psiManager.findFile(file);

					if( psiFile != null )
						for( PsiReference inboundReference: ReferencesSearch.search(psiFile) )
						{
							String newPackagePath = newPath + "/" + VfsUtil.getRelativePath(file, directory);
							VirtualFile newInnermostRoot = PerlUtil.findInnermostSourceRoot(project, newPackagePath);
							String newRelativePath = Paths.get(newInnermostRoot.getPath()).relativize(Paths.get(newPackagePath)).toString();
							String newPackageName = PerlPackageUtil.getPackageNameByPath(newRelativePath);

							queue.addElement(inboundReference.getElement(), newPackageName);
						}
				}
		}
	}
}
