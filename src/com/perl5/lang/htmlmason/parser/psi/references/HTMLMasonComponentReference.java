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

package com.perl5.lang.htmlmason.parser.psi.references;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.resolve.ResolveCache;
import com.intellij.util.IncorrectOperationException;
import com.perl5.lang.htmlmason.idea.configuration.HTMLMasonSettings;
import com.perl5.lang.htmlmason.parser.psi.HTMLMasonCompositeElement;
import com.perl5.lang.htmlmason.parser.psi.HTMLMasonNamedElement;
import com.perl5.lang.htmlmason.parser.psi.HTMLMasonSubcomponentDefitnition;
import com.perl5.lang.htmlmason.parser.psi.impl.HTMLMasonFileImpl;
import com.perl5.lang.perl.psi.PerlString;
import com.perl5.lang.perl.psi.references.PerlPolyVariantReference;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hurricup on 19.03.2016.
 */
public class HTMLMasonComponentReference extends PerlPolyVariantReference<PerlString>
{
	protected static final ResolveCache.PolyVariantResolver<HTMLMasonComponentReference> RESOLVER = new HTMLMasonComponentResolver();

	public HTMLMasonComponentReference(@NotNull PerlString element, TextRange textRange)
	{
		super(element, textRange);
	}

	@NotNull
	@Override
	public ResolveResult[] multiResolve(boolean incompleteCode)
	{
		return ResolveCache.getInstance(myElement.getProject()).resolveWithCaching(this, RESOLVER, true, incompleteCode);
	}

	@Override
	public PsiElement handleElementRename(String newElementName) throws IncorrectOperationException
	{
		ResolveResult[] results = multiResolve(false);
		String currentContent = myElement.getStringContent();

		if (results.length == 1 && results[0].getElement() instanceof HTMLMasonFileImpl)
		{
			handleFilePathChange((HTMLMasonFileImpl) results[0].getElement(), currentContent, newElementName);
		}
		else if (HTMLMasonNamedElement.HTML_MASON_IDENTIFIER_PATTERN.matcher(newElementName).matches())
		{
			TextRange range = getRangeInElement();
			String newContent = newElementName + currentContent.substring(range.getLength());
			myElement.setStringContent(newContent);
		}
		return myElement;
	}

	private void handleFilePathChange(HTMLMasonFileImpl target, String currentContent, String newFileName)
	{
		VirtualFile componentFileDir = target.getComponentVirtualFile().getParent();
		VirtualFile componentRoot = null;
		String absPrefix = "";

		if (StringUtil.startsWith(currentContent, "/")) // abs path
		{
			absPrefix = "/";
			componentRoot = target.getComponentRoot();
			;
		}
		else // relative path
		{
			PsiFile psiFile = myElement.getContainingFile();

			if (psiFile instanceof HTMLMasonFileImpl)
			{
				VirtualFile virtualFile = ((HTMLMasonFileImpl) psiFile).getComponentVirtualFile();

				if (virtualFile != null)
				{
					componentRoot = virtualFile.getParent();
				}
			}
		}

		if (componentFileDir != null && componentRoot != null)
		{
			String newContent = null;

			if (componentRoot.equals(componentFileDir))
			{
				newContent = absPrefix + newFileName;
			}
			else
			{
				String relativePath = VfsUtil.getRelativePath(componentFileDir, componentRoot);

				if (relativePath != null)
				{
					newContent = absPrefix + relativePath + '/' + newFileName;
				}
			}

			if (newContent != null)
			{
				myElement.setStringContent(newContent);
			}
		}

	}

	@Override
	public PsiElement bindToElement(@NotNull PsiElement element) throws IncorrectOperationException
	{
		if (element instanceof HTMLMasonFileImpl)
		{
			handleFilePathChange(
					(HTMLMasonFileImpl) element,
					myElement.getStringContent(),
					((HTMLMasonFileImpl) element).getName()
			);
		}
		return myElement;
	}

	private static class HTMLMasonComponentResolver implements ResolveCache.PolyVariantResolver<HTMLMasonComponentReference>
	{
		@NotNull
		@Override
		public ResolveResult[] resolve(@NotNull HTMLMasonComponentReference htmlMasonComponentReference, boolean incompleteCode)
		{
			List<ResolveResult> result = null;

			// looking subcomponents
			String nameOrPath = htmlMasonComponentReference.getRangeInElement().substring(htmlMasonComponentReference.getElement().getText());
			final PsiFile file = htmlMasonComponentReference.getElement().getContainingFile();

			if (file instanceof HTMLMasonFileImpl)
			{
				for (HTMLMasonCompositeElement subcomponentDefitnition : ((HTMLMasonFileImpl) file).getSubComponents())
				{
					assert subcomponentDefitnition instanceof HTMLMasonSubcomponentDefitnition;
					if (StringUtil.equals(((HTMLMasonSubcomponentDefitnition) subcomponentDefitnition).getName(), nameOrPath))
					{
						if (result == null)
							result = new ArrayList<ResolveResult>();
						result.add(new PsiElementResolveResult(subcomponentDefitnition));
					}
				}
			}

			// looking components
			if (result == null)
			{
				final Project project = file.getProject();
				VirtualFile componentVirtualFile = null;

				if (StringUtil.startsWith(nameOrPath, "/"))
				{
					HTMLMasonSettings settings = HTMLMasonSettings.getInstance(project);
					if (settings != null)
					{
						for (VirtualFile componentRoot : settings.getComponentsRootsVirtualFiles())
						{
							componentVirtualFile = componentRoot.findFileByRelativePath(nameOrPath);

							if (componentVirtualFile != null)
							{
								break;
							}
						}
					}
				}
				else // possible relative path
				{
					VirtualFile containingFile = file.getVirtualFile();
					if (containingFile != null)
					{
						VirtualFile containingDir = containingFile.getParent();
						componentVirtualFile = containingDir.findFileByRelativePath(nameOrPath);
					}
				}

				if (componentVirtualFile != null)
				{
					PsiFile componentFile = PsiManager.getInstance(project).findFile(componentVirtualFile);
					if (componentFile instanceof HTMLMasonFileImpl)
					{
						result = new ArrayList<ResolveResult>();
						result.add(new PsiElementResolveResult(componentFile));
					}

				}
			}

			return result == null ? ResolveResult.EMPTY_ARRAY : result.toArray(new ResolveResult[result.size()]);
		}
	}
}
