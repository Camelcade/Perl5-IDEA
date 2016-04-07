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

package com.perl5.lang.pod.parser.psi.references;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.PsiFile;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.impl.source.resolve.ResolveCache;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.IncorrectOperationException;
import com.perl5.lang.perl.PerlScopes;
import com.perl5.lang.perl.psi.references.resolvers.PerlSubReferenceResolver;
import com.perl5.lang.perl.util.PerlPackageUtil;
import com.perl5.lang.pod.PodLanguage;
import com.perl5.lang.pod.parser.psi.PodFile;
import com.perl5.lang.pod.parser.psi.impl.PodIdentifierImpl;
import com.perl5.lang.pod.parser.psi.util.PodFileUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hurricup on 05.04.2016.
 */
public class PodSubReference extends PodReferenceBase<PodIdentifierImpl>
{
	protected static final ResolveCache.PolyVariantResolver<PodSubReference> RESOLVER = new PodSubReferenceResolver();

	public PodSubReference(PodIdentifierImpl element)
	{
		super(element, new TextRange(0, element.getTextLength()), true);
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
		return (PsiElement) myElement.replaceWithText(newElementName);
	}

	@Override
	public PsiElement bindToElement(@NotNull PsiElement element) throws IncorrectOperationException
	{
		return super.bindToElement(element);
	}

	private static class PodSubReferenceResolver implements ResolveCache.PolyVariantResolver<PodSubReference>
	{
		@NotNull
		@Override
		public ResolveResult[] resolve(@NotNull PodSubReference podSubReference, boolean incompleteCode)
		{
			PsiElement element = podSubReference.getElement();
			if (element != null)
			{
				String subName = element.getText();
				if (StringUtil.isNotEmpty(subName))
				{
					PsiFile podFile = element.getContainingFile().getViewProvider().getPsi(PodLanguage.INSTANCE);
					if (podFile != null)
					{
						String className = PodFileUtil.getPackageName((PodFile) podFile);
						if (className == null)
						{
							className = "main";
						}

						String canonicalName = className + PerlPackageUtil.PACKAGE_SEPARATOR + subName;

						List<PsiElement> targets = new ArrayList<PsiElement>();
						final Project project = element.getProject();
						PerlSubReferenceResolver.collectRelatedItems(canonicalName, project, null, targets, GlobalSearchScope.projectScope(project));

						if (targets.isEmpty())
						{
							PerlSubReferenceResolver.collectRelatedItems(canonicalName, project, null, targets, PerlScopes.getProjectAndLibrariesScope(project));
						}

						if (!targets.isEmpty())
						{
							List<ResolveResult> results = new ArrayList<ResolveResult>();
							for (PsiElement target : targets)
							{
								results.add(new PsiElementResolveResult(target));
							}
							return results.toArray(new ResolveResult[results.size()]);
						}
					}
				}
			}

			return ResolveResult.EMPTY_ARRAY;
		}
	}
}
