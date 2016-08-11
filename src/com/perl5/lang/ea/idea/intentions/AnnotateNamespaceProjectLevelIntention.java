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

package com.perl5.lang.ea.idea.intentions;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.perl5.PerlBundle;
import com.perl5.lang.ea.fileTypes.PerlExternalAnnotationsFileType;
import com.perl5.lang.ea.psi.PerlExternalAnnotationNamespace;
import com.perl5.lang.perl.psi.PerlNamespaceDefinition;
import com.perl5.lang.perl.psi.PerlNamespaceElement;
import com.perl5.lang.perl.psi.impl.PerlFileImpl;
import com.perl5.lang.perl.psi.utils.PerlElementFactory;
import com.perl5.lang.perl.psi.utils.PerlNamespaceAnnotations;
import com.perl5.lang.perl.util.PerlAnnotationsUtil;
import com.perl5.lang.perl.util.PerlExternalAnnotationsLevels;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * Created by hurricup on 11.08.2016.
 */
public class AnnotateNamespaceProjectLevelIntention extends AnnotateNamespaceIntentionBase implements PerlExternalAnnotationsLevels
{
	@Nullable
	@Override
	protected PsiElement getElementToAnnotate(PerlNamespaceElement namespaceElement)
	{
		Collection<PerlExternalAnnotationNamespace> externalAnnotationsNamespcaces = PerlAnnotationsUtil.getExternalAnnotationsNamespaces(namespaceElement, getAnnotationsLevel());
		if (externalAnnotationsNamespcaces != null && !externalAnnotationsNamespcaces.isEmpty())
		{
			return externalAnnotationsNamespcaces.iterator().next();
		}

		// no element
		VirtualFile targetVirtualFile = getTargetVirtualFile(namespaceElement);
		if (targetVirtualFile == null)
		{
			return null;
		}

		Project project = namespaceElement.getProject();
		PsiFile targetPsiFile = PsiManager.getInstance(project).findFile(targetVirtualFile);
		if (targetPsiFile == null)
		{
			return null;
		}

		PerlFileImpl dummyFile = PerlElementFactory.createFile(project, "package " + namespaceElement.getCanonicalName() + ";", PerlExternalAnnotationsFileType.INSTANCE);
		PerlExternalAnnotationNamespace namespace = dummyFile.findChildByClass(PerlExternalAnnotationNamespace.class);

		if (namespace == null)
		{
			return null;
		}

		PsiElement newLine = PerlElementFactory.createNewLine(project);
		targetPsiFile.add(newLine);
		targetPsiFile.add(newLine);
		return targetPsiFile.add(namespace);
	}

	@Nullable
	protected VirtualFile getTargetVirtualFile(PerlNamespaceElement namespaceElement)
	{
		PerlNamespaceDefinition namespaceDefinition = getNamespaceDefinition(namespaceElement);
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

		VirtualFile annotationsRoot = getAnnotationsRoot(namespaceElement.getProject());
		if (annotationsRoot == null)
		{
			return null;
		}

		String relativePath = PerlPackageUtil.getPathByNamspaceNameWithoutExtension(filePackageName) + "." + PerlExternalAnnotationsFileType.EXTENSION;
		VirtualFile targetFile = VfsUtil.findRelativeFile(annotationsRoot, relativePath);
		if (targetFile != null)
		{
			return targetFile;
		}

		List<String> pathChunks = StringUtil.split(relativePath, "/");
		String fileName = pathChunks.remove(pathChunks.size() - 1);
		for (String pathChunk : pathChunks)
		{
			VirtualFile subdir = annotationsRoot.findChild(pathChunk);
			if (subdir == null)
			{
				try
				{
					subdir = annotationsRoot.createChildDirectory(this, pathChunk);
				}
				catch (IOException ignore)
				{
					// warn here
					return null;
				}
			}
			else if (!subdir.isDirectory())
			{
				// can't create path, should warn or something
				return null;
			}
			annotationsRoot = subdir;
		}
		try
		{
			return annotationsRoot.findOrCreateChildData(this, fileName);
		}
		catch (IOException ignore)
		{
			return null;
		}
	}

	protected int getAnnotationsLevel()
	{
		return PROJECT_LEVEL;
	}

	@Nullable
	protected VirtualFile getAnnotationsRoot(Project project)
	{
		return PerlAnnotationsUtil.getProjectAnnotationsRoot(project);
	}

	@Override
	public boolean isAvailable(@NotNull Project project, Editor editor, @NotNull PsiElement element)
	{
		if (!super.isAvailable(project, editor, element))
		{
			return false;
		}

		PerlNamespaceAnnotations declarationAnnotations = null;
		if (element.getParent() instanceof PerlNamespaceDefinition)
		{
			declarationAnnotations = ((PerlNamespaceDefinition) element.getParent()).getStubbedOrLocalAnnotations();
		}
		else
		{
			declarationAnnotations = ((PerlNamespaceElement) element).getNamespaceDefinitions().get(0).getStubbedOrLocalAnnotations();
		}

		return declarationAnnotations == null;
	}

	@NotNull
	@Override
	public String getText()
	{
		return PerlBundle.message("perl.annotate.project");
	}
}
