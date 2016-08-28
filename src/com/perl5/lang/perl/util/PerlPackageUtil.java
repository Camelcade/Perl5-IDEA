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

import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.*;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.NonClasspathDirectoriesScope;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.PlatformUtils;
import com.intellij.util.Processor;
import com.intellij.util.containers.HashSet;
import com.perl5.compat.PerlStubIndex;
import com.perl5.lang.perl.PerlScopes;
import com.perl5.lang.perl.extensions.packageprocessor.PerlLibProvider;
import com.perl5.lang.perl.extensions.packageprocessor.PerlPackageProcessor;
import com.perl5.lang.perl.fileTypes.PerlFileTypePackage;
import com.perl5.lang.perl.idea.modules.JpsPerlLibrarySourceRootType;
import com.perl5.lang.perl.idea.refactoring.rename.RenameRefactoringQueue;
import com.perl5.lang.perl.idea.sdk.PerlSdkType;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.impl.PerlFileImpl;
import com.perl5.lang.perl.psi.stubs.PerlSubBaseStub;
import com.perl5.lang.perl.psi.stubs.namespaces.PerlNamespaceDefinitionStubIndex;
import com.perl5.lang.perl.psi.stubs.namespaces.PerlParentNamespaceDefinitionStubIndex;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import gnu.trove.THashSet;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by hurricup on 24.04.2015.
 */
public class PerlPackageUtil implements PerlElementTypes, PerlBuiltInNamespaces
{
	// perl using idea class roots plus some inferred classroots not presenting in order entries
	public static final Key<List<VirtualFile>> PERL_CLASS_ROOTS = Key.create("perl.class.roots");
	public static final Set<String> BUILT_IN_ALL = new THashSet<String>();

	private static final Set<String> INTERNAL_PACKAGES = new HashSet<String>();

	private static final Map<String, String> CANONICAL_NAMES_CACHE = new ConcurrentHashMap<String, String>();
	private static final Map<String, String> myFilePathsToPackageNameMap = new ConcurrentHashMap<String, String>();

	static
	{
		BUILT_IN_ALL.addAll(BUILT_IN);
		BUILT_IN_ALL.addAll(BUILT_IN_PRAGMA);

		INTERNAL_PACKAGES.add(SUPER_PACKAGE);
		INTERNAL_PACKAGES.add(MAIN_PACKAGE);
		INTERNAL_PACKAGES.add(UNIVERSAL_PACKAGE);
		INTERNAL_PACKAGES.add(CORE_PACKAGE);
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
		if (INTERNAL_PACKAGES.contains(packageName))
		{
			return false;
		}

		for (PerlNamespaceDefinition definition : PerlPackageUtil.getNamespaceDefinitions(project, packageName))
		{
			if (definition.isDeprecated())
			{
				return true;
			}
		}

		return false;
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
		{
			return newName;
		}

		String originalName = name;

//		System.out.println("Source: " + name);
		// removing trailing separator if any
		name = PACKAGE_SEPARATOR_TAIL_RE.matcher(name).replaceFirst("");
//		System.out.println("Notail: " + name);

		ArrayList<String> canonicalChunks = new ArrayList<String>();
		String[] chunks = PACKAGE_SEPARATOR_RE.split(name, -1);

//		System.out.println("Chunks: " + chunks.length);

		if (chunks.length > 0 && chunks[0].isEmpty())    // implicit main
		{
			chunks[0] = PerlPackageUtil.MAIN_PACKAGE;
		}

		for (String chunk : chunks)
		{
			if (!(canonicalChunks.isEmpty() && chunk.equals("main")))
			{
				canonicalChunks.add(chunk);
			}
		}

//		System.out.println("Canonical chunks: " + chunks.length);

		if (canonicalChunks.isEmpty())
		{
			newName = "main";
		}
		else
		{
			newName = StringUtils.join(canonicalChunks, "::");
		}

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
	@Nullable
	public static String getContextPackageName(PsiElement element)
	{
		PerlNamespaceDefinition namespaceDefinition = getContainingNamespace(element);

		if (namespaceDefinition != null && namespaceDefinition.getPackageName() != null) // checking that definition is valid and got namespace
		{
			String name = namespaceDefinition.getPackageName();
			assert name != null;
			return name;
		}

		// default value
		PsiFile file = element.getContainingFile();
		if (file instanceof PerlFileImpl)
		{
			PsiElement contextParent = file.getContext();
			PsiElement realParent = file.getParent();

			if (contextParent != null && !contextParent.equals(realParent))
			{
				return getContextPackageName(contextParent);
			}

			return ((PerlFileImpl) file).getPackageName();
		}
		else
		{
			return PerlPackageUtil.MAIN_PACKAGE;
		}
	}

	// fixme shouldn't we have recursion protection here?
	@Nullable
	public static PerlNamespaceContainer getNamespaceContainerForElement(@Nullable PsiElement element)
	{
		if (element == null)
		{
			return null;
		}

		PerlNamespaceContainer namespaceContainer = PsiTreeUtil.getParentOfType(element, PerlNamespaceContainer.class);

		if (namespaceContainer instanceof PerlFileImpl)
		{
			PsiElement contextParent = namespaceContainer.getContext();
			if (contextParent != null && !contextParent.equals(namespaceContainer.getParent()))
			{
				return getNamespaceContainerForElement(contextParent);
			}
		}
		return namespaceContainer;
	}

	// fixme take fileContext in account?
	public static PerlNamespaceDefinition getContainingNamespace(PsiElement element)
	{
		return PsiTreeUtil.getStubOrPsiParentOfType(element, PerlNamespaceDefinition.class);
	}

	@NotNull
	public static List<PerlNamespaceDefinition> collectNamespaceDefinitions(@NotNull Project project, @NotNull List<String> packageNames)
	{
		ArrayList<PerlNamespaceDefinition> namespaceDefinitions = new ArrayList<PerlNamespaceDefinition>();
		for (String packageName : packageNames)
		{
			Collection<PerlNamespaceDefinition> list = getNamespaceDefinitions(project, packageName, GlobalSearchScope.projectScope(project));

			if (list.isEmpty())
			{
				list = getNamespaceDefinitions(project, packageName, PerlScopes.getProjectAndLibrariesScope(project));
			}

			namespaceDefinitions.addAll(list);
		}
		return namespaceDefinitions;
	}

	/**
	 * Searching project files for namespace definitions by specific package name
	 *
	 * @param project     project to search in
	 * @param packageName canonical package name (without tailing ::)
	 * @return collection of found definitions
	 */
	@Deprecated // must use element's resolve scope here
	public static Collection<PerlNamespaceDefinition> getNamespaceDefinitions(Project project, @NotNull String packageName)
	{
		return getNamespaceDefinitions(project, packageName, PerlScopes.getProjectAndLibrariesScope(project));
	}

	public static Collection<PerlNamespaceDefinition> getNamespaceDefinitions(Project project, @NotNull String packageName, GlobalSearchScope scope)
	{
		return PerlStubIndex.getElements(PerlNamespaceDefinitionStubIndex.KEY, packageName, project, scope, PerlNamespaceDefinition.class);
	}

	/**
	 * Returns list of defined package names
	 *
	 * @param project project to search in
	 * @return collection of package names
	 */
	public static Collection<String> getDefinedPackageNames(Project project)
	{
		return PerlStubIndex.getInstance().getAllKeys(PerlNamespaceDefinitionStubIndex.KEY, project);
	}

	/**
	 * Finds and processes all namespace definitions for psi element, according to it's resolve scope
	 * @param element element in question
	 * @param processor processor
	 */
	public static void processDefinedPackagesForPsiElement(PsiElement element, Processor<Collection<PerlNamespaceDefinition>> processor)
	{
		if (element == null)
		{
			return;
		}

		final Project project = element.getProject();
		final GlobalSearchScope elementResolveScope = element.getResolveScope();

		for (String packageName : PerlPackageUtil.getDefinedPackageNames(project))
		{
			if (packageName.length() > 0 && Character.isLetterOrDigit(packageName.charAt(0)))
			{
				Collection<PerlNamespaceDefinition> namespaceDefinitions = PerlPackageUtil.getNamespaceDefinitions(project, packageName, elementResolveScope);

				if (namespaceDefinitions.size() > 0)
				{
					processor.process(namespaceDefinitions);
				}
			}
		}
	}

	/**
	 * Returns list of derived classes
	 *
	 * @param project project to search in
	 * @return collection of definitions
	 */
	public static List<PerlNamespaceDefinition> getDerivedNamespaceDefinitions(@NotNull Project project, @NotNull String packageName)
	{
		List<PerlNamespaceDefinition> list = getDerivedNamespaceDefinitions(project, packageName, GlobalSearchScope.projectScope(project));
		if (list.isEmpty())
		{
			list = getDerivedNamespaceDefinitions(project, packageName, PerlScopes.getProjectAndLibrariesScope(project));
		}
		return list;
	}

	public static List<PerlNamespaceDefinition> getDerivedNamespaceDefinitions(@NotNull Project project, @NotNull String packageName, @NotNull GlobalSearchScope scope)
	{
		return new ArrayList<PerlNamespaceDefinition>(PerlStubIndex.getElements(PerlParentNamespaceDefinitionStubIndex.KEY, packageName, project, scope, PerlNamespaceDefinition.class));
	}

	/**
	 * Builds package path from packageName Foo::Bar => Foo/Bar.pm
	 *
	 * @param packageName canonical package name
	 * @return package path
	 */
	@NotNull
	public static String getPathByNamespaceName(String packageName)
	{
		return getPathByNamspaceNameWithoutExtension(packageName) + ".pm";
	}

	@NotNull
	public static String getPathByNamspaceNameWithoutExtension(String packageName)
	{
		return StringUtils.join(packageName.split(":+"), '/');
	}

	/**
	 * Returns package name by virtual file and project
	 *
	 * @param virtualFile file in question
	 * @param project     project
	 * @return canonical package name
	 */
	@Nullable
	public static String getPackageNameByFile(VirtualFile virtualFile, @NotNull Project project)
	{
		if (virtualFile == null)
		{
			return null;
		}

		VirtualFile innermostSourceRoot = PerlUtil.getFileClassRoot(project, virtualFile);
		if (innermostSourceRoot != null)
		{
			String relativePath = VfsUtil.getRelativePath(virtualFile, innermostSourceRoot);
			return PerlPackageUtil.getPackageNameByPath(relativePath);
		}
		return null;
	}

	/**
	 * Translates package relative name to the package name Foo/Bar.pm => Foo::Bar
	 *
	 * @param packagePath package relative path
	 * @return canonical package name
	 */
	public static String getPackageNameByPath(final String packagePath)
	{
		String result = myFilePathsToPackageNameMap.get(packagePath);

		if (result == null)
		{
			String path = packagePath.replaceAll("\\\\", "/");
			result = getCanonicalPackageName(StringUtils.join(path.replaceFirst("\\.[^.]+$", "").split("/"), PACKAGE_SEPARATOR));
			myFilePathsToPackageNameMap.put(packagePath, result);
		}
		return result;
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
					{
						for (PerlNamespaceDefinition namespaceDefinition : PsiTreeUtil.findChildrenOfType(psiFile, PerlNamespaceDefinition.class))
						{
							if (oldPackageName.equals(namespaceDefinition.getPackageName()))
							{
								queue.addElement(namespaceDefinition, newPackageName);
							}
						}
					}
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
		{
			for (VirtualFile file : VfsUtil.collectChildrenRecursively(directory))
			{
				if (!file.isDirectory() && file.getFileType() == PerlFileTypePackage.INSTANCE && directorySourceRoot.equals(PerlUtil.getFileClassRoot(project, file)))
				{
					String relativePath = VfsUtil.getRelativePath(file, directory);
					String oldFilePath = oldPath + "/" + relativePath;
					collectNestedPackageDefinitionsFromFile(queue, file, oldFilePath);
				}
			}
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
			{
				if (!file.isDirectory() && file.getFileType() == PerlFileTypePackage.INSTANCE && oldDirectorySourceRoot.equals(PerlUtil.getFileClassRoot(project, file)))
				{
					PsiFile psiFile = psiManager.findFile(file);

					if (psiFile != null)
					{
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


	public static void processPackageFilesForPsiElement(PsiElement element, final Processor<String> processor)
	{
		processClassrootFilesForPsiElement(
				element,
				new ClassRootVirtualFileProcessor()
				{
					@Override
					public boolean process(VirtualFile file, VirtualFile classRoot)
					{
						String relativePath = VfsUtil.getRelativePath(file, classRoot);
						String packageName = PerlPackageUtil.getPackageNameByPath(relativePath);
						return processor.process(packageName);
					}
				},
				PerlFileTypePackage.INSTANCE)
		;
	}

	public static void processClassrootFilesForPsiElement(PsiElement element, ClassRootVirtualFileProcessor processor, FileType fileType)
	{
		if (element != null)
		{
			List<VirtualFile> psiElementPerlClassRoots = getPsiElementPerlClassRoots(element);
			if (psiElementPerlClassRoots.isEmpty())
			{
				return;
			}
			for (VirtualFile file : FileTypeIndex.getFiles(fileType, new NonClasspathDirectoriesScope(psiElementPerlClassRoots)))
			{
				for (VirtualFile classRoot : psiElementPerlClassRoots)
				{
					if (VfsUtil.isAncestor(classRoot, file, true))
					{
						if (!processor.process(file, classRoot))
						{
							return;
						}
					}
				}
			}
		}
	}

	private static List<VirtualFile> getPsiElementPerlClassRoots(PsiElement element)
	{
		if (element == null)
		{
			return Collections.emptyList();
		}
		Module moduleForPsiElement = ModuleUtilCore.findModuleForPsiElement(element);
		if (moduleForPsiElement == null)
		{
			return Collections.emptyList();
		}
		List<VirtualFile> classRoots = moduleForPsiElement.getUserData(PERL_CLASS_ROOTS);
		if (classRoots != null)
		{
			return classRoots;
		}

		classRoots = new ArrayList<VirtualFile>();
		recursivelyCollectPerlClassRoots(moduleForPsiElement, classRoots, true, new HashSet<Module>());
		moduleForPsiElement.putUserData(PERL_CLASS_ROOTS, classRoots);
		return classRoots;
	}

	private static void recursivelyCollectPerlClassRoots(Module module, List<VirtualFile> result, boolean includeUnexported, Set<Module> moduleRecursionControl)
	{
		if (moduleRecursionControl.contains(module))
		{
			return;
		}
		moduleRecursionControl.add(module);

		ModuleRootManager moduleRootManager = ModuleRootManager.getInstance(module);

		addIfMissing(result, moduleRootManager.getSourceRoots(JpsPerlLibrarySourceRootType.INSTANCE));

		for (OrderEntry orderEntry : moduleRootManager.getOrderEntries())
		{
			if (includeUnexported && PlatformUtils.isIntelliJ() &&
					orderEntry instanceof JdkOrderEntry &&
					((JdkOrderEntry) orderEntry).getJdk().getSdkType() == PerlSdkType.getInstance())
			{
				addIfMissing(result, Arrays.asList(orderEntry.getFiles(OrderRootType.CLASSES)));
			}

			if (!(orderEntry instanceof ExportableOrderEntry))
			{
				continue;
			}
			if (!includeUnexported && !((ExportableOrderEntry) orderEntry).isExported())
			{
				continue;
			}

			if (orderEntry instanceof LibraryOrderEntry)
			{
				addIfMissing(result, Arrays.asList(orderEntry.getFiles(OrderRootType.CLASSES)));
			}
			else if (orderEntry instanceof ModuleOrderEntry)
			{
				recursivelyCollectPerlClassRoots(((ModuleOrderEntry) orderEntry).getModule(), result, false, moduleRecursionControl);
			}
		}

	}

	private static <E> void addIfMissing(Collection<E> result, Iterable<E> addition)
	{
		if (addition == null)
		{
			return;
		}

		for (E e : addition)
		{
			if (!result.contains(e))
			{
				result.add(e);
			}
		}
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

	public static void processNotOverridedMethods(final PerlNamespaceDefinition namespaceDefinition, Processor<PerlSubBase> processor)
	{
		if (namespaceDefinition != null)
		{
			PsiFile containingFile = namespaceDefinition.getContainingFile();
			String packageName = namespaceDefinition.getPackageName();
			if (packageName == null)
			{
				return;
			}

			Set<String> namesSet = new THashSet<String>();
			// collecting overrided
			for (PerlSubDefinitionBase subDefinitionBase : PsiTreeUtil.findChildrenOfType(containingFile, PerlSubDefinitionBase.class))
			{
				if (subDefinitionBase.isValid() && StringUtil.equals(packageName, subDefinitionBase.getPackageName()))
				{
					namesSet.add(subDefinitionBase.getSubName());
				}
			}

			processParentClassesSubs(
					namespaceDefinition,
					namesSet,
					new THashSet<PerlNamespaceDefinition>(),
					processor
			);
		}

	}

	public static void processParentClassesSubs(PerlNamespaceDefinition childClass,
												Set<String> processedSubsNames,
												Set<PerlNamespaceDefinition> recursionMap,
												Processor<PerlSubBase> processor
	)
	{
		if (childClass == null || recursionMap.contains(childClass))
		{
			return;
		}
		recursionMap.add(childClass);

		for (PerlNamespaceDefinition parentNamespace : childClass.getParentNamespaceDefinitions())
		{
			for (PsiElement subDefinitionBase : PerlPsiUtil.collectNamespaceMembers(parentNamespace, PerlSubBaseStub.class, PerlSubBase.class))
			{
				String subName = ((PerlSubBase) subDefinitionBase).getSubName();
				if (subDefinitionBase.isValid() &&
						((PerlSubBase) subDefinitionBase).isMethod() &&
						!processedSubsNames.contains(subName)
						)
				{
					processedSubsNames.add(subName);
					processor.process(((PerlSubBase) subDefinitionBase));
				}
			}
			processParentClassesSubs(
					parentNamespace,
					processedSubsNames,
					recursionMap,
					processor
			);
		}
	}

	public static void processChildNamespacesSubs(@NotNull PerlNamespaceDefinition namespaceDefinition,
												  @Nullable Set<PerlNamespaceDefinition> recursionSet,
												  Processor<PerlSubBase> processor)
	{
		if (recursionSet == null)
		{
			recursionSet = new THashSet<PerlNamespaceDefinition>();
		}

		recursionSet.add(namespaceDefinition);

		for (PerlNamespaceDefinition childNamespace : namespaceDefinition.getChildNamespaceDefinitions())
		{
			if (!recursionSet.contains(childNamespace))
			{
				boolean processSubclasses = true;

				for (PsiElement subBase : PerlPsiUtil.collectNamespaceMembers(childNamespace, PerlSubBaseStub.class, PerlSubBase.class))
				{
					processSubclasses = processor.process((PerlSubBase) subBase);
				}

				if (processSubclasses)
				{
					processChildNamespacesSubs(childNamespace, recursionSet, processor);
				}
			}
		}
	}

	@Nullable
	public static PsiFile getPackagePsiFileByPackageName(Project project, String packageName)
	{
		VirtualFile packageVirtualFile = getPackageVirtualFileByPackageName(project, packageName);

		if (packageVirtualFile != null)
		{
			return PsiManager.getInstance(project).findFile(packageVirtualFile);
		}

		return null;
	}

	@Nullable
	public static VirtualFile getPackageVirtualFileByPackageName(Project project, String packageName)
	{
		if (StringUtil.isEmpty(packageName))
		{
			return null;
		}

		String packagePath = getPathByNamespaceName(packageName);
		VirtualFile[] classRoots = ProjectRootManager.getInstance(project).orderEntries().getClassesRoots();

		for (VirtualFile classRoot : classRoots)
		{
			VirtualFile targetFile = classRoot.findFileByRelativePath(packagePath);
			if (targetFile != null)
			{
				return targetFile;
			}
		}
		return null;
	}

	/**
	 * Resolving canonical package name to a psi file
	 *
	 * @param psiFile              base file
	 * @param canonicalPackageName package name in canonical form
	 * @return vartual file
	 */
	@Nullable
	public static PsiFile resolvePackageNameToPsi(@NotNull PsiFile psiFile, String canonicalPackageName)
	{
		// resolves to a psi file
		return resolveRelativePathToPsi(psiFile, PerlPackageUtil.getPathByNamespaceName(canonicalPackageName));
	}

	/**
	 * Resolving canonical package to a virtual file
	 *
	 * @param psiFile              base file
	 * @param canonicalPackageName package name in canonical form
	 * @return vartual file
	 */
	@Nullable
	public static VirtualFile resolvePackageNameToVirtualFile(@NotNull PsiFile psiFile, String canonicalPackageName)
	{
		// resolves to a psi file
		return resolveRelativePathToVirtualFile(psiFile, PerlPackageUtil.getPathByNamespaceName(canonicalPackageName));
	}

	/**
	 * Resolving relative path to a psi file
	 *
	 * @param psiFile      base file
	 * @param relativePath relative path
	 * @return vartual file
	 */
	@Nullable
	public static PsiFile resolveRelativePathToPsi(@NotNull PsiFile psiFile, String relativePath)
	{
		VirtualFile targetFile = resolveRelativePathToVirtualFile(psiFile, relativePath);

		if (targetFile != null && targetFile.exists())
		{
			PsiFile targetPsiFile = PsiManager.getInstance(psiFile.getProject()).findFile(targetFile);
			if (targetPsiFile != null)
			{
				return targetPsiFile;
			}
		}

		return null;
	}

	/**
	 * Resolving relative path to a virtual file
	 *
	 * @param psiFile      base file
	 * @param relativePath relative path
	 * @return vartual file
	 */
	@Nullable
	public static VirtualFile resolveRelativePathToVirtualFile(@NotNull PsiFile psiFile, String relativePath)
	{
		if (relativePath != null)
		{
			for (VirtualFile classRoot : getLibPathsForPsiFile(psiFile))
			{
				if (classRoot != null)
				{
					VirtualFile targetFile = classRoot.findFileByRelativePath(relativePath);
					if (targetFile != null)
					{
						String foundRelativePath = VfsUtil.getRelativePath(targetFile, classRoot);

						if (StringUtil.isNotEmpty(foundRelativePath) && StringUtil.equals(foundRelativePath, relativePath))
						{
							return targetFile;
						}
					}
				}
			}
		}

		return null;
	}

	/**
	 * Returns List of lib directories including class roots, current directory and use lib ones
	 *
	 * @param psiFile psiFile to resolve from
	 * @return list of lib dirs
	 */
	@NotNull
	public static List<VirtualFile> getLibPathsForPsiFile(@NotNull PsiFile psiFile)
	{
		List<VirtualFile> result = new ArrayList<VirtualFile>();

		// libdirs providers fixme we need to use stubs or psi here
		for (PerlUseStatement useStatement : PsiTreeUtil.findChildrenOfType(psiFile, PerlUseStatement.class))
		{
			PerlPackageProcessor packageProcessor = useStatement.getPackageProcessor();
			if (packageProcessor instanceof PerlLibProvider)
			{
				((PerlLibProvider) packageProcessor).addLibDirs(useStatement, result);
			}
		}

		// classpath
		result.addAll(getPsiElementPerlClassRoots(psiFile));

		// current dir
		if (psiFile instanceof PerlFileImpl)
		{
			VirtualFile virtualFile = psiFile.getVirtualFile();
			if (virtualFile != null)
			{
				result.add(virtualFile.getParent());
			}
		}

		return result;
	}

	public interface ClassRootVirtualFileProcessor
	{
		boolean process(VirtualFile file, VirtualFile classRoot);
	}

}
