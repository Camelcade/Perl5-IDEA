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

package com.perl5.lang.perl.util;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.NullableLazyValue;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.PerlBundle;
import com.perl5.compat.PerlStubIndex;
import com.perl5.lang.ea.fileTypes.PerlExternalAnnotationsFileType;
import com.perl5.lang.ea.psi.PerlExternalAnnotationDeclaration;
import com.perl5.lang.ea.psi.PerlExternalAnnotationNamespace;
import com.perl5.lang.ea.psi.stubs.PerlExternalAnnotationDeclarationStubIndex;
import com.perl5.lang.ea.psi.stubs.PerlExternalAnnotationNamespaceStubIndex;
import com.perl5.lang.perl.PerlScopes;
import com.perl5.lang.perl.idea.configuration.settings.PerlApplicationSettings;
import com.perl5.lang.perl.idea.configuration.settings.PerlSharedSettings;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.impl.PerlFileImpl;
import com.perl5.lang.perl.psi.utils.PerlElementFactory;
import com.perl5.lang.perl.psi.utils.PerlNamespaceAnnotations;
import com.perl5.lang.perl.psi.utils.PerlSubAnnotations;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by hurricup on 09.08.2016.
 */
public class PerlAnnotationsUtil implements PerlExternalAnnotationsLevels
{
	private static NullableLazyValue<VirtualFile> myPluginAnnotationsLazyRoot = new NullableLazyValue<VirtualFile>()
	{
		@Nullable
		@Override
		protected VirtualFile compute()
		{
			String annotaionsRoot = getPluginAnnotationsPath();
			return annotaionsRoot == null ? null : VfsUtil.findFileByIoFile(new File(annotaionsRoot), true);
		}
	};

	@Nullable
	public static String getPluginAnnotationsPath()
	{
		String pluginRoot = PerlPluginUtil.getPluginRoot();
		return pluginRoot == null ? null : pluginRoot + "/plugin.annotations";
	}

	@Nullable
	public static VirtualFile getPluginAnnotationsRoot()
	{
		return myPluginAnnotationsLazyRoot.getValue();
	}

	@Nullable
	public static VirtualFile getApplicationAnnotationsRoot()
	{
		return PerlApplicationSettings.getInstance().getAnnotationsRoot();
	}

	@Nullable
	public static VirtualFile getProjectAnnotationsRoot(Project project)
	{
		return PerlSharedSettings.getInstance(project).getAnnotationsRoot();
	}

	@Nullable
	public static PerlNamespaceAnnotations getExternalAnnotations(@NotNull PerlNamespaceDefinition namespaceDefinition)
	{
		String canonicalName = namespaceDefinition.getPackageName();

		if (StringUtil.isNotEmpty(canonicalName))
		{
			Project project = namespaceDefinition.getProject();
			PerlExternalAnnotationNamespace lowestLevelPsiElement = getLowestLevelPsiElement(
					getExternalAnnotationsNamespaces(project, canonicalName)
			);
			if (lowestLevelPsiElement != null)
			{
				return lowestLevelPsiElement.getAnnotations();
			}
		}
		return null;
	}

	@NotNull
	public static Collection<PerlExternalAnnotationNamespace> getExternalAnnotationsNamespaces(
			@NotNull Project project, @NotNull String canonicalName
	)
	{
		return PerlStubIndex.getElements(
				PerlExternalAnnotationNamespaceStubIndex.KEY,
				canonicalName,
				project,
				PerlScopes.getProjectAndLibrariesScope(project),
				PerlExternalAnnotationNamespace.class
		);
	}

	@Nullable
	public static Collection<PerlExternalAnnotationNamespace> getExternalAnnotationsNamespaces(
			@Nullable PerlNamespaceElement element, int desiredLevel
	)
	{
		if (element == null)
		{
			return null;
		}
		return getExternalAnnotationsNamespaces(element.getProject(), element.getCanonicalName(), desiredLevel);
	}


	@Nullable
	public static Collection<PerlExternalAnnotationNamespace> getExternalAnnotationsNamespaces(
			@NotNull Project project, @Nullable String canonicalName, int desiredLevel
	)
	{
		if (StringUtil.isEmpty(canonicalName))
		{
			return null;
		}

		List<PerlExternalAnnotationNamespace> result = null;

		for (PerlExternalAnnotationNamespace declaration : getExternalAnnotationsNamespaces(project, canonicalName))
		{
			int currentElementLevel = getPsiElementLevel(declaration);
			if (currentElementLevel == desiredLevel)
			{
				if (result == null)
				{
					result = new ArrayList<PerlExternalAnnotationNamespace>();
				}
				result.add(declaration);
			}
		}
		return result;
	}

	@Nullable
	public static PerlSubAnnotations getExternalAnnotations(@NotNull PerlSubBase subBase)
	{
		Project project = subBase.getProject();
		String canonicalName = subBase.getCanonicalName();

		if (StringUtil.isNotEmpty(canonicalName))
		{
			PerlExternalAnnotationDeclaration lowestLevelPsiElement = getLowestLevelPsiElement(
					getExternalAnnotationsSubDeclarations(project, canonicalName)
			);
			if (lowestLevelPsiElement != null)
			{
				return lowestLevelPsiElement.getAnnotations();
			}
		}
		return null;
	}

	@NotNull
	public static Collection<PerlExternalAnnotationDeclaration> getExternalAnnotationsSubDeclarations(
			@NotNull Project project, @NotNull String canonicalName
	)
	{
		return PerlStubIndex.getElements(
				PerlExternalAnnotationDeclarationStubIndex.KEY,
				canonicalName,
				project,
				PerlScopes.getProjectAndLibrariesScope(project),
				PerlExternalAnnotationDeclaration.class
		);
	}

	@Nullable
	public static Collection<PerlExternalAnnotationDeclaration> getExternalAnnotationsSubDeclarations(
			@Nullable PerlSubNameElement subNameElement, int desiredLevel
	)
	{
		if (subNameElement == null)
		{
			return null;
		}
		return getExternalAnnotationsSubDeclarations(subNameElement.getProject(), subNameElement.getCanonicalName(), desiredLevel);
	}

	@Nullable
	public static Collection<PerlExternalAnnotationDeclaration> getExternalAnnotationsSubDeclarations(
			@NotNull Project project, @Nullable String canonicalName, int desiredLevel
	)
	{
		if (StringUtil.isEmpty(canonicalName))
		{
			return null;
		}

		List<PerlExternalAnnotationDeclaration> result = null;

		for (PerlExternalAnnotationDeclaration declaration : getExternalAnnotationsSubDeclarations(project, canonicalName))
		{
			int currentElementLevel = getPsiElementLevel(declaration);
			if (currentElementLevel == desiredLevel)
			{
				if (result == null)
				{
					result = new ArrayList<PerlExternalAnnotationDeclaration>();
				}
				result.add(declaration);
			}
		}
		return result;
	}

	@Nullable
	private static <T extends PsiElement> T getLowestLevelPsiElement(Collection<T> elements)
	{
		int currentLevel = UNKNOWN_LEVEL;
		T result = null;
		for (T element : elements)
		{
			int elementLevel = getPsiElementLevel(element);
			if (elementLevel == PROJECT_LEVEL)
			{
				return element;
			}
			if (elementLevel < currentLevel)
			{
				currentLevel = elementLevel;
				result = element;
			}
		}
		return result;
	}

	public static int getPsiElementLevel(@NotNull PsiElement element)
	{
		VirtualFile virtualFile = element.getContainingFile().getVirtualFile();
		VirtualFile annotationsRoot = getPluginAnnotationsRoot();
		if (annotationsRoot != null && VfsUtil.isAncestor(annotationsRoot, virtualFile, true))
		{
			return PLUGIN_LEVEL;
		}
		annotationsRoot = getApplicationAnnotationsRoot();
		if (annotationsRoot != null && VfsUtil.isAncestor(annotationsRoot, virtualFile, true))
		{
			return APP_LEVEL;
		}
		annotationsRoot = element.getProject().getBaseDir();
		if (annotationsRoot != null && VfsUtil.isAncestor(annotationsRoot, virtualFile, true))
		{
			return PROJECT_LEVEL;
		}
		return UNKNOWN_LEVEL;
	}

	@Nullable
	public static VirtualFile getAnnotationsLevelRoot(@NotNull Project project, int level)
	{
		if (level == PLUGIN_LEVEL)
		{
			return getPluginAnnotationsRoot();
		}
		else if (level == APP_LEVEL)
		{
			return getApplicationAnnotationsRoot();
		}
		else if (level == PROJECT_LEVEL)
		{
			return getProjectAnnotationsRoot(project);
		}
		return null;
	}

	@NotNull
	public static String getPsiElementLevelName(@NotNull PsiElement element)
	{
		return getAnnotationLevelName(getPsiElementLevel(element));
	}

	@NotNull
	public static String getAnnotationLevelName(int psiElementLevel)
	{
		if (psiElementLevel == PROJECT_LEVEL)
		{
			return PerlBundle.message("perl.ea.project.level");
		}
		if (psiElementLevel == APP_LEVEL)
		{
			return PerlBundle.message("perl.ea.application.level");
		}
		if (psiElementLevel == PLUGIN_LEVEL)
		{
			return PerlBundle.message("perl.ea.plugin.level");
		}
		return PerlBundle.message("perl.ea.unknown.level");

	}


	@Nullable
	public static PsiElement findOrCreateSubAnnotationTarget(
			@NotNull Project project,
			@Nullable String packageName,
			@Nullable String subName,
			int desiredLevel
	)
	{
		if (StringUtil.isEmpty(packageName) || StringUtil.isEmpty(subName))
		{
			return null;
		}
		String canonicalName = packageName + PerlPackageUtil.PACKAGE_SEPARATOR + subName;

		Collection<PerlExternalAnnotationDeclaration> externalAnnotationsSubDeclarations = getExternalAnnotationsSubDeclarations(project, canonicalName, desiredLevel);

		if (externalAnnotationsSubDeclarations != null && !externalAnnotationsSubDeclarations.isEmpty())
		{
			return externalAnnotationsSubDeclarations.iterator().next();
		}

		PsiElement namespaceAnnotationTarget = findOrCreateNamespaceAnnotationTarget(project, packageName, desiredLevel);
		if (namespaceAnnotationTarget == null)
		{
			return null;
		}

		PerlFileImpl dummyFile = PerlElementFactory.createFile(project, "package Dummy;\n\nsub " + subName + ";\n", PerlExternalAnnotationsFileType.INSTANCE);
		PsiPerlNamespaceContent namespaceContent = PsiTreeUtil.findChildOfType(dummyFile, PsiPerlNamespaceContent.class);
		if (namespaceContent == null)
		{
			return null;
		}

		PsiElement targetElement = ((PerlExternalAnnotationNamespace) namespaceAnnotationTarget).getNamespaceContent();
		if (targetElement != null)
		{
			namespaceAnnotationTarget = targetElement;
		}

		namespaceAnnotationTarget.addRange(namespaceContent.getFirstChild(), namespaceContent.getLastChild());

		for (PerlExternalAnnotationDeclaration declaration : PsiTreeUtil.findChildrenOfType(namespaceAnnotationTarget, PerlExternalAnnotationDeclaration.class))
		{
			if (StringUtil.equals(declaration.getSubName(), subName))
			{
				return declaration;
			}
		}
		return null;
	}

	@Nullable
	public static PerlExternalAnnotationNamespace findOrCreateNamespaceAnnotationTarget(
			@NotNull Project project,
			@Nullable String canonicalName,
			int desiredLevel
	)
	{
		if (StringUtil.isEmpty(canonicalName))
		{
			return null;
		}

		Collection<PerlExternalAnnotationNamespace> externalAnnotationsNamespaces = PerlAnnotationsUtil.getExternalAnnotationsNamespaces(
				project,
				canonicalName,
				desiredLevel
		);
		if (externalAnnotationsNamespaces != null && !externalAnnotationsNamespaces.isEmpty())
		{
			return externalAnnotationsNamespaces.iterator().next();
		}

		// no element
		VirtualFile targetVirtualFile = findOrCreateAnnotationsVirtualFile(project, canonicalName, desiredLevel);
		if (targetVirtualFile == null)
		{
			return null;
		}

		PsiFile targetPsiFile = PsiManager.getInstance(project).findFile(targetVirtualFile);
		if (targetPsiFile == null)
		{
			return null;
		}

		PerlFileImpl dummyFile = PerlElementFactory.createFile(project, "\n\npackage " + canonicalName + ";\n", PerlExternalAnnotationsFileType.INSTANCE);
		targetPsiFile.addRange(dummyFile.getFirstChild(), dummyFile.getLastChild());

		for (PerlExternalAnnotationNamespace namespace : PsiTreeUtil.findChildrenOfType(targetPsiFile, PerlExternalAnnotationNamespace.class))
		{
			if (StringUtil.equals(namespace.getPackageName(), canonicalName))
			{
				return namespace;
			}
		}
		return null;
	}

	@Nullable
	public static VirtualFile findOrCreateAnnotationsVirtualFile(
			@NotNull Project project,
			@NotNull String packageName,
			int desiredLevel
	)
	{
		Collection<PerlNamespaceDefinition> namespaceDefinitions = PerlPackageUtil.getNamespaceDefinitions(project, packageName);
		if (namespaceDefinitions.isEmpty())
		{
			return null;
		}
		PerlNamespaceDefinition namespaceDefinition = namespaceDefinitions.iterator().next();
		if (namespaceDefinition == null)
		{
			return null;
		}

		PsiFile containingFile = namespaceDefinition.getContainingFile();
		if (!(containingFile instanceof PerlFileImpl))
		{
			return null;
		}
		String filePackageName = ((PerlFileImpl) containingFile).getFilePackageName();
		if (filePackageName == null)
		{
			filePackageName = namespaceDefinition.getPackageName();
		}

		if (filePackageName == null)
		{
			return null;
		}

		VirtualFile annotationsRoot = getAnnotationsLevelRoot(project, desiredLevel);
		if (annotationsRoot == null)
		{
			return null;
		}

		String relativePath = PerlPackageUtil.getPathByNamspaceNameWithoutExtension(filePackageName) + "." + PerlExternalAnnotationsFileType.EXTENSION;

		try
		{
			return PerlFileUtil.findOrCreateRelativeFile(annotationsRoot, relativePath);
		}
		catch (IOException e)
		{
			return null;
		}
	}

}
