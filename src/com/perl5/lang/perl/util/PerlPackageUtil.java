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
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.util.TextRange;
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
import com.intellij.util.Processor;
import com.perl5.lang.perl.idea.fileTypes.PerlFileTypePackage;
import com.perl5.lang.perl.idea.refactoring.rename.RenameRefactoringQueue;
import com.perl5.lang.perl.idea.stubs.imports.PerlUseStatementStubIndex;
import com.perl5.lang.perl.idea.stubs.namespaces.PerlNamespaceDefinitionStubIndex;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.PerlNamespaceDefinition;
import com.perl5.lang.perl.psi.PerlUseStatement;
import com.perl5.lang.perl.psi.PsiPerlNamespaceDefinition;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import gnu.trove.THashSet;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
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

	public static final Set<String> BUILT_IN_ALL = new THashSet<String>();
	public static final ConcurrentHashMap<String, String> CANONICAL_NAMES_CACHE = new ConcurrentHashMap<String, String>();

	public static final String SUPER_PACKAGE = "SUPER";
	public static final String MAIN_PACKAGE = "main";
	public static final String UNIVERSAL_PACKAGE = "UNIVERSAL";
	public static final String CORE_PACKAGE = "CORE";

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
	 * @param packageName package name
	 * @return result
	 */
	public static boolean isDeprecated(Project project, String packageName)
	{
		if (isMain(packageName))
			return false;

		for (PerlNamespaceDefinition definition : PerlPackageUtil.getNamespaceDefinitions(project, packageName))
			if (definition.isDeprecated())
				return true;

		return BUILT_IN_DEPRECATED.contains(getCanonicalPackageName(packageName));
	}

	public static boolean isSUPER(String packageName)
	{
		return PerlPackageUtil.SUPER_PACKAGE.equals(packageName);
	}

	public static boolean isMain(String packageName)
	{
		return PerlPackageUtil.MAIN_PACKAGE.equals(packageName);
	}

	public static boolean isCORE(String packageName)
	{
		return PerlPackageUtil.CORE_PACKAGE.equals(packageName);
	}

	public static boolean isUNIVERSAL(String packageName)
	{
		return PerlPackageUtil.UNIVERSAL_PACKAGE.equals(packageName);
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

		if (chunks.length > 0 && chunks[0].isEmpty())    // implicit main
			chunks[0] = PerlPackageUtil.MAIN_PACKAGE;

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
		return getNamespaceDefinitions(project, packageName, GlobalSearchScope.allScope(project));
	}

	public static Collection<PsiPerlNamespaceDefinition> getNamespaceDefinitions(Project project, String packageName, GlobalSearchScope scope)
	{
		assert packageName != null;

		return StubIndex.getElements(PerlNamespaceDefinitionStubIndex.KEY, packageName, project, scope, PsiPerlNamespaceDefinition.class);
	}

	/**
	 * Returns list of defined package names
	 *
	 * @param project project to search in
	 * @return collection of package names
	 */
	public static Collection<String> getDefinedPackageNames(Project project)
	{
		return PerlUtil.getIndexKeysWithoutInternals(PerlNamespaceDefinitionStubIndex.KEY, project);
	}

	/**
	 * Processes all global packages names with specific processor
	 *
	 * @param scope   search scope
	 * @param processor string processor for suitable strings
	 * @return collection of constants names
	 */
	public static boolean processDefinedPackageNames(GlobalSearchScope scope, Processor<String> processor)
	{
		return StubIndex.getInstance().processAllKeys(PerlNamespaceDefinitionStubIndex.KEY, processor, scope, null);
	}

	/**
	 * Returns list of derived classes
	 *
	 * @param project project to search in
	 * @return collection of definitions
	 */
	public static Collection<PsiPerlNamespaceDefinition> getDerivedNamespaceDefinitions(Project project, String packageName)
	{
		return getDerivedNamespaceDefinitions(project, packageName, GlobalSearchScope.allScope(project));
	}

	public static Collection<PsiPerlNamespaceDefinition> getDerivedNamespaceDefinitions(Project project, String packageName, GlobalSearchScope scope)
	{
		assert packageName != null;

		return StubIndex.getElements(PerlNamespaceDefinitionStubIndex.KEY, "*" + packageName, project, scope, PsiPerlNamespaceDefinition.class);
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

		return getCanonicalPackageName(StringUtils.join(packagePath.replaceFirst("\\.pm$", "").split("/"), PACKAGE_SEPARATOR));
	}

	/**
	 * Adds to queue netsted namespaces, which names should be adjusted to the new package name/path
	 *
	 * @param queue   - RenameRefactoringQueue
	 * @param file    - file has been moved
	 * @param oldPath - previous filepath
	 */
	public static void collectNestedPackageDefinitionsFromFile(@NotNull RenameRefactoringQueue queue, VirtualFile file, String oldPath)
	{
		Project project = queue.getProject();
		VirtualFile newInnermostRoot = PerlUtil.getFileClassRoot(project, file);

		if (newInnermostRoot != null)
		{
			String newRelativePath = VfsUtil.getRelativePath(file, newInnermostRoot);
			String newPackageName = PerlPackageUtil.getPackageNameByPath(newRelativePath);

			VirtualFile oldInnermostRoot = PerlUtil.getFileClassRoot(project, oldPath);

			if (oldInnermostRoot != null)
			{
				String oldRelativePath = oldPath.substring(oldInnermostRoot.getPath().length());
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
	public static void collectNestedPackageDefinitions(RenameRefactoringQueue queue, VirtualFile directory, String oldPath)
	{
		Project project = queue.getProject();
		VirtualFile directorySourceRoot = PerlUtil.getFileClassRoot(project, directory);

		if (directorySourceRoot != null)
			for (VirtualFile file : VfsUtil.collectChildrenRecursively(directory))
				if (!file.isDirectory() && file.getFileType() == PerlFileTypePackage.INSTANCE && directorySourceRoot.equals(PerlUtil.getFileClassRoot(project, file)))
				{
					String relativePath = VfsUtil.getRelativePath(file, directory);
					String oldFilePath = oldPath + "/" + relativePath;
					collectNestedPackageDefinitionsFromFile(queue, file, oldFilePath);
				}
	}

	/**
	 * Searches for all pm files in directory to be renamed/moved, searches for references to those packages and add them to the renaming queue
	 *
	 * @param project   Project to be renamed
	 * @param directory VirtualFile of directory to be renamed
	 * @param newPath   new directory path
	 */
	public static void adjustNestedFiles(Project project, VirtualFile directory, String newPath)
	{
		VirtualFile oldDirectorySourceRoot = PerlUtil.getFileClassRoot(project, directory);
		PsiManager psiManager = PsiManager.getInstance(project);

		if (oldDirectorySourceRoot != null)
		{
			for (VirtualFile file : VfsUtil.collectChildrenRecursively(directory))
				if (!file.isDirectory() && file.getFileType() == PerlFileTypePackage.INSTANCE && oldDirectorySourceRoot.equals(PerlUtil.getFileClassRoot(project, file)))
				{
					PsiFile psiFile = psiManager.findFile(file);

					if (psiFile != null)
						for (PsiReference inboundReference : ReferencesSearch.search(psiFile))
						{
							String newPackagePath = newPath + "/" + VfsUtil.getRelativePath(file, directory);
							VirtualFile newInnermostRoot = PerlUtil.getFileClassRoot(project, newPackagePath);
							if (newInnermostRoot != null)
							{
								String newRelativePath = newPackagePath.substring(newInnermostRoot.getPath().length());
								String newPackageName = PerlPackageUtil.getPackageNameByPath(newRelativePath);

								PerlPsiUtil.renameFileReferencee(inboundReference.getElement(), newPackageName);
							}
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
	public static Collection<String> getPackageFilesForPsiElement(PsiElement element)
	{
		final Set<String> result = new THashSet<String>();

		processPackageFilesForPsiElement(element, new Processor<String>()
		{
			@Override
			public boolean process(String s)
			{
				result.add(s);
				return true;
			}
		});

		return result;
	}


	public static void processPackageFilesForPsiElement(PsiElement element, Processor<String> processor)
	{
		if (element != null)
		{
			VirtualFile[] classRoots;

			classRoots = ProjectRootManager.getInstance(element.getProject()).orderEntries().getClassesRoots();

			for (VirtualFile classRoot : classRoots)
			{
				for (VirtualFile virtualFile : VfsUtil.collectChildrenRecursively(classRoot))
					if (!virtualFile.isDirectory() && "pm".equals(virtualFile.getExtension()))
					{
						String relativePath = VfsUtil.getRelativePath(virtualFile, classRoot);
						String packageName = PerlPackageUtil.getPackageNameByPath(relativePath);
						processor.process(packageName);
					}
			}
		}
	}

	public static Collection<PerlUseStatement> getPackageImports(Project project, String packageName)
	{
		return getPackageImports(project, packageName, GlobalSearchScope.allScope(project));
	}

	public static Collection<PerlUseStatement> getPackageImports(Project project, String packageName, PsiFile file)
	{
		return getPackageImports(project, packageName, GlobalSearchScope.fileScope(project, file.getVirtualFile()));
	}

	public static Collection<PerlUseStatement> getPackageImports(Project project, String packageName, GlobalSearchScope scope)
	{
		assert packageName != null;

		return StubIndex.getElements(PerlUseStatementStubIndex.KEY, packageName, project, scope, PerlUseStatement.class);
	}

	public static TextRange getPackageRangeFromOffset(int startOffset, String text)
	{
		int endOffset = startOffset + text.length();
		if (text.endsWith("::"))
		{
			endOffset -= 2;
		}
		else if (text.endsWith("'"))
		{
			endOffset -= 1;
		}
		return new TextRange(startOffset, endOffset);
	}
}
