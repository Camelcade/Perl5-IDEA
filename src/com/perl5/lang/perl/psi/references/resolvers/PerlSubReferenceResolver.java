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

package com.perl5.lang.perl.psi.references.resolvers;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.PsiFile;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.impl.source.resolve.ResolveCache;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.mro.PerlMroDfs;
import com.perl5.lang.perl.psi.properties.PerlNamedElement;
import com.perl5.lang.perl.psi.properties.PerlNamespaceElementContainer;
import com.perl5.lang.perl.psi.references.PerlSubReference;
import com.perl5.lang.perl.util.PerlGlobUtil;
import com.perl5.lang.perl.util.PerlPackageUtil;
import com.perl5.lang.perl.util.PerlSubUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by hurricup on 27.09.2015.
 */
public class PerlSubReferenceResolver implements ResolveCache.PolyVariantResolver<PerlSubReference>
{
	@NotNull
	@Override
	public ResolveResult[] resolve(@NotNull PerlSubReference reference, boolean incompleteCode)
	{
		PsiElement myElement = reference.getElement();
		assert myElement instanceof PerlSubNameElement;
		PerlSubNameElement subNameElement = (PerlSubNameElement) myElement;

		List<PsiElement> relatedItems = new ArrayList<PsiElement>();

		PsiElement parent = subNameElement.getParent();

		String packageName = subNameElement.getPackageName();
		String subName = subNameElement.getName();
		Project project = subNameElement.getProject();

		PerlNamespaceElement expliclitPackageElement = null;
		if (parent instanceof PerlNamespaceElementContainer)
			expliclitPackageElement = ((PerlNamespaceElementContainer) parent).getNamespaceElement();

		if (subName != null)
		{
			if (parent instanceof PerlMethod && ((PerlMethod) parent).isObjectMethod())
				relatedItems.addAll(PerlMroDfs.resolveSub(project, packageName, subName, false));
			else if (parent instanceof PerlMethod && "SUPER".equals(packageName))
				relatedItems.addAll(PerlMroDfs.resolveSub(project, ((PerlMethod) parent).getContextPackageName(), subName, true));
			else    // static resolution
			{
				if (expliclitPackageElement == null && subNameElement.isBuiltIn())
					return new ResolveResult[0];

				PsiFile file = subNameElement.getContainingFile();

//				if (file instanceof PerlFile)
//					((PerlFile) file).getElementsResolveScope();

//				System.err.println("Checking for " + subName);

				// check indexes for defined subs
				collectRelatedItems(
						packageName + "::" + subName,
						project,
						parent,
						relatedItems
				);

				if (expliclitPackageElement == null)
				{
					// check for imports to the current file
					PerlNamespaceContainer namespaceContainer = PsiTreeUtil.getParentOfType(subNameElement, PerlNamespaceContainer.class);

					assert namespaceContainer != null;

					for (Map.Entry<String, Set<String>> imports : namespaceContainer.getImportedSubsNames().entrySet())
						for (String importedSubName : imports.getValue())
							if (importedSubName.equals(subName))
								collectRelatedItems(
										imports.getKey() + "::" + subName,
										project,
										parent,
										relatedItems
								);
				} else    // check imports to target namespace
				{
					String targetPackageName = expliclitPackageElement.getCanonicalName();
					if (targetPackageName != null)
					{
						// fixme partially not DRY with previous block
						for (PerlNamespaceDefinition namespaceDefinition : PerlPackageUtil.getNamespaceDefinitions(project, targetPackageName))
							for (Map.Entry<String, Set<String>> imports : namespaceDefinition.getImportedSubsNames().entrySet())
								for (String importedSubName : imports.getValue())
									if (importedSubName.equals(subName))
										collectRelatedItems(
												imports.getKey() + "::" + subName,
												project,
												parent,
												relatedItems
										);
					}
				}

				// check for autoload
				if (relatedItems.size() == 0
						&& !"UNIVERSAL".equals(packageName)    // don't check for UNIVERSAL::AUTOLOAD
						&& !(
						parent instanceof PerlSubDeclaration
								|| parent instanceof PerlSubDefinition
				))
					collectRelatedItems(
							packageName + "::AUTOLOAD",
							project,
							parent,
							relatedItems
					);
			}
		}


		List<ResolveResult> result = new ArrayList<ResolveResult>();

		reference.resetFlags();

		for (PsiElement element : relatedItems)
		{
			if (!reference.isAutoloaded() && element instanceof PerlNamedElement && "AUTOLOAD".equals(((PerlNamedElement) element).getName()))
				reference.setAutoloaded();

			if (!reference.isConstant() && element instanceof PerlConstant)
				reference.setConstant();

			if (!reference.isDeclared() && element instanceof PerlSubDeclaration)
				reference.setDeclared();

			if (!reference.isDefined() && element instanceof PerlSubDefinition)
				reference.setDefined();

			if (!reference.isAliased() && element instanceof PerlGlobVariable)
				reference.setAliased();

			result.add(new PsiElementResolveResult(element));
		}

		return result.toArray(new ResolveResult[result.size()]);
	}

	public void collectRelatedItems(String canonicalName, Project project, PsiElement exclusion, List<PsiElement> relatedItems)
	{
		for (PsiPerlSubDefinition target : PerlSubUtil.getSubDefinitions(project, canonicalName))
			if (!target.isEquivalentTo(exclusion))
				relatedItems.add(target);
		for (PsiPerlSubDeclaration target : PerlSubUtil.getSubDeclarations(project, canonicalName))
			if (!target.isEquivalentTo(exclusion))
				relatedItems.add(target);
		for (PerlGlobVariable target : PerlGlobUtil.getGlobsDefinitions(project, canonicalName))
			if (!target.isEquivalentTo(exclusion))
				relatedItems.add(target);
		for (PerlConstant target : PerlSubUtil.getConstantsDefinitions(project, canonicalName))
			if (!target.isEquivalentTo(exclusion))
				relatedItems.add(target);
	}
}
