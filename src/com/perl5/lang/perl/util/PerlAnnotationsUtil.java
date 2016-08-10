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
import com.perl5.compat.PerlStubIndex;
import com.perl5.lang.ea.psi.PerlExternalAnnotationDeclaration;
import com.perl5.lang.ea.psi.PerlExternalAnnotationNamespace;
import com.perl5.lang.ea.psi.stubs.PerlExternalAnnotationDeclarationStubIndex;
import com.perl5.lang.ea.psi.stubs.PerlExternalAnnotationNamespaceStubIndex;
import com.perl5.lang.perl.PerlScopes;
import com.perl5.lang.perl.idea.configuration.settings.PerlApplicationSettings;
import com.perl5.lang.perl.idea.configuration.settings.PerlSharedSettings;
import com.perl5.lang.perl.psi.PerlNamespaceDefinition;
import com.perl5.lang.perl.psi.PerlSubBase;
import com.perl5.lang.perl.psi.utils.PerlNamespaceAnnotations;
import com.perl5.lang.perl.psi.utils.PerlSubAnnotations;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Collection;

/**
 * Created by hurricup on 09.08.2016.
 */
public class PerlAnnotationsUtil
{
	private static final int PROJECT_LEVEL = 0;
	private static final int APP_LEVEL = 1;
	private static final int PLUGIN_LEVEL = 2;
	private static final int UNKNOWN_LEVEL = 3; // light virtual files and other stuff
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
					getExternalNamespcaceAnnotations(project, canonicalName)
			);
			if (lowestLevelPsiElement != null)
			{
				return lowestLevelPsiElement.getAnnotations();
			}
		}
		return null;
	}

	@NotNull
	public static Collection<PerlExternalAnnotationNamespace> getExternalNamespcaceAnnotations(
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
	public static PerlSubAnnotations getExternalAnnotations(@NotNull PerlSubBase subBase)
	{
		Project project = subBase.getProject();
		String canonicalName = subBase.getCanonicalName();

		if (StringUtil.isNotEmpty(canonicalName))
		{
			PerlExternalAnnotationDeclaration lowestLevelPsiElement = getLowestLevelPsiElement(
					getSubExternalAnnotations(project, canonicalName)
			);
			if (lowestLevelPsiElement != null)
			{
				return lowestLevelPsiElement.getSubAnnotations();
			}
		}
		return null;
	}

	@NotNull
	public static Collection<PerlExternalAnnotationDeclaration> getSubExternalAnnotations(
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

	private static int getPsiElementLevel(PsiElement element)
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

}
