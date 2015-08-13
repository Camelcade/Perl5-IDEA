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

package com.perl5.lang.perl.psi.references;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.ResolveResult;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.mro.PerlMroDfs;
import com.perl5.lang.perl.psi.properties.PerlNamedElement;
import com.perl5.lang.perl.util.PerlGlobUtil;
import com.perl5.lang.perl.util.PerlSubUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PerlSubReference extends PerlReferencePoly
{
	protected boolean myIsAutoLoaded = false;
	protected boolean myIsConstant = false;
	protected boolean myIsDeclared = false;
	protected boolean myIsDefined = false;
	protected boolean myIsAliased = false;
	PerlSubNameElement mySubNameElement;

	public PerlSubReference(@NotNull PsiElement element, TextRange textRange)
	{
		super(element, textRange);
		assert element instanceof PerlSubNameElement;
		mySubNameElement = (PerlSubNameElement) element;
	}

	@NotNull
	@Override
	public ResolveResult[] multiResolve(boolean incompleteCode)
	{
		List<PsiElement> relatedItems = new ArrayList<PsiElement>();

		PsiElement parent = mySubNameElement.getParent();

		String packageName = mySubNameElement.getPackageName();
		String subName = mySubNameElement.getName();
		Project project = mySubNameElement.getProject();

		if (subName != null)
		{
			if (parent instanceof PerlMethod && ((PerlMethod) parent).isObjectMethod())
				relatedItems.addAll(PerlMroDfs.resolveSub(project, packageName, subName, false));
			else if (parent instanceof PerlMethod && "SUPER".equals(packageName))
				relatedItems.addAll(PerlMroDfs.resolveSub(project, ((PerlMethod) parent).getContextPackageName(), subName, true));
			else    // static resolution
			{
				String canonicalName = packageName + "::" + subName;
				for (PsiPerlSubDefinition target : PerlSubUtil.getSubDefinitions(project, canonicalName))
					if (!target.isEquivalentTo(parent))
						relatedItems.add(target);
				for (PsiPerlSubDeclaration target : PerlSubUtil.getSubDeclarations(project, canonicalName))
					if (!target.isEquivalentTo(parent))
						relatedItems.add(target);
				for (PerlGlobVariable target : PerlGlobUtil.getGlobsDefinitions(project, canonicalName))
					if (!target.isEquivalentTo(parent))
						relatedItems.add(target);
				for (PerlConstant target : PerlSubUtil.getConstantsDefinitions(project, canonicalName))
					if (!target.isEquivalentTo(parent))
						relatedItems.add(target);

				if (!"UNIVERSAL".equals(packageName)    // don't check for UNIVERSAL::AUTOLOAD
						&& relatedItems.size() == 0 && !(
						parent instanceof PerlSubDeclaration
								|| parent instanceof PerlSubDefinition
				))
				{
					canonicalName = packageName + "::AUTOLOAD";
					for (PsiPerlSubDefinition target : PerlSubUtil.getSubDefinitions(project, canonicalName))
						if (!target.isEquivalentTo(parent))
							relatedItems.add(target);
					for (PsiPerlSubDeclaration target : PerlSubUtil.getSubDeclarations(project, canonicalName))
						if (!target.isEquivalentTo(parent))
							relatedItems.add(target);
					for (PerlGlobVariable target : PerlGlobUtil.getGlobsDefinitions(project, canonicalName))
						if (!target.isEquivalentTo(parent))
							relatedItems.add(target);
					for (PerlConstant target : PerlSubUtil.getConstantsDefinitions(project, canonicalName))
						if (!target.isEquivalentTo(parent))
							relatedItems.add(target);
				}
			}
		}


		List<ResolveResult> result = new ArrayList<ResolveResult>();

		myIsAutoLoaded = myIsConstant = myIsAliased = myIsDeclared = myIsDefined = false;

		for (PsiElement element : relatedItems)
		{
			if (!myIsAutoLoaded && element instanceof PerlNamedElement && "AUTOLOAD".equals(((PerlNamedElement) element).getName()))
				myIsAutoLoaded = true;

			if (!myIsConstant && element instanceof PerlConstant)
				myIsConstant = true;

			if (!myIsDeclared && element instanceof PerlSubDeclaration)
				myIsDeclared = true;

			if (!myIsDefined && element instanceof PerlSubDefinition)
				myIsDefined = true;

			if (!myIsAliased && element instanceof PerlGlobVariable)
				myIsAliased = true;

			result.add(new PsiElementResolveResult(element));
		}

		return result.toArray(new ResolveResult[result.size()]);
	}


	@Override
	public boolean isReferenceTo(PsiElement element)
	{
		if (element instanceof PerlGlobVariable || element instanceof PerlConstant || element instanceof PerlSubDeclaration || element instanceof PerlSubDefinition)
			return super.isReferenceTo(element);

		PsiElement parent = element.getParent();
		if (parent instanceof PerlGlobVariable || parent instanceof PerlConstant || parent instanceof PerlSubDeclaration || parent instanceof PerlSubDefinition)
			return isReferenceTo(parent);

		PsiElement grandParent = parent.getParent();
		if (grandParent instanceof PerlConstant)
			return isReferenceTo(grandParent);

		return false;
	}


	@Nullable
	@Override
	public PsiElement resolve()
	{
		ResolveResult[] resolveResults = multiResolve(false);
		if (resolveResults.length == 0)
			return null;

		for (ResolveResult resolveResult : resolveResults)
			if (resolveResult.getElement() instanceof PerlGlobVariable)
				return resolveResult.getElement();

		return resolveResults[0].getElement();
	}

	public boolean isAutoloaded()
	{
		return myIsAutoLoaded;
	}

	public boolean isDefined()
	{
		return myIsDefined;
	}

	public boolean isDeclared()
	{
		return myIsDeclared;
	}

	public boolean isAliased()
	{
		return myIsAliased;
	}

	public boolean isConstant()
	{
		return myIsConstant;
	}
}
