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

package com.perl5.lang.perl.psi.impl;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiReference;
import com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistry;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.IncorrectOperationException;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.mro.PerlDefaultMro;
import com.perl5.lang.perl.psi.properties.PerlPackageMember;
import com.perl5.lang.perl.psi.utils.PerlElementFactory;
import com.perl5.lang.perl.util.PerlGlobUtil;
import com.perl5.lang.perl.util.PerlPackageUtil;
import com.perl5.lang.perl.util.PerlSubUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hurricup on 24.05.2015.
 *
 */
public class PerlSubNameElementImpl extends LeafPsiElement implements PerlSubNameElement
{
	public PerlSubNameElementImpl(@NotNull IElementType type, CharSequence text) {
		super(type, text);
	}

	@Override
	public void accept(@NotNull PsiElementVisitor visitor) {
		if (visitor instanceof PerlVisitor) ((PerlVisitor)visitor).visitSubNameElement(this);
		else super.accept(visitor);
	}

	@Override
	public PsiElement setName(@NotNull String name) throws IncorrectOperationException
	{
		PerlSubNameElement newFunction = PerlElementFactory.createUserFunction(getProject(), name);
		if( newFunction != null )
			replace(newFunction);
		else
			throw new IncorrectOperationException("Unable to create function from: "+ name);
		return this;
	}


	@Override
	public String getPackageName()
	{
		PsiElement parent = getParent();

		if( parent instanceof PerlPackageMember)
			return ((PerlPackageMember) parent).getPackageName();
		else
			return PerlPackageUtil.getContextPackageName(this);
	}

	@Override
	@NotNull
	public String getCanonicalName()
	{
		return getPackageName() + "::" + getName();
	}

	@Nullable
	@Override
	public PsiElement getNameIdentifier()
	{
		return this;
	}

	@Override
	public String getName()
	{
		return this.getText();
	}

	@NotNull
	@Override
	public PsiReference[] getReferences()
	{
		return ReferenceProvidersRegistry.getReferencesFromProviders(this);
	}

	@Override
	public List<PerlSubDefinition> getSubDefinitions()
	{
		List<PerlSubDefinition> result = new ArrayList<>();
		PsiElement parent = getParent();

		String packageName = getPackageName();
		String subName = getName();

		if( subName != null  )
		{
			if (parent instanceof PerlMethod && ((PerlMethod) parent).isObjectMethod())
				result.addAll(PerlDefaultMro.getSubDefinitions(getProject(), packageName, subName));
			else if (parent instanceof PerlMethod && "SUPER".equals(packageName))
				result.addAll(PerlDefaultMro.getSuperSubDefinitions(getProject(), ((PerlMethod) parent).getContextPackageName(), subName));
			else
				for (PsiPerlSubDefinition subDefinition : PerlSubUtil.findSubDefinitions(getProject(), packageName + "::" + subName))
					if (!subDefinition.isEquivalentTo(parent))
						result.add(subDefinition);
		}
		return result;
	}

	@Override
	public List<PerlSubDeclaration> getSubDeclarations()
	{
		List<PerlSubDeclaration> result = new ArrayList<>();
		PsiElement parent = getParent();

		String packageName = getPackageName();
		String subName = getName();

		if( subName != null  )
		{
			if (parent instanceof PerlMethod && ((PerlMethod) parent).isObjectMethod())
				result.addAll(PerlDefaultMro.getSubDeclarations(getProject(), packageName, subName));
			else if (parent instanceof PerlMethod &&  "SUPER".equals(packageName))
				result.addAll(PerlDefaultMro.getSuperSubDeclarations(getProject(), ((PerlMethod) parent).getContextPackageName(), subName));
			else
				for (PsiPerlSubDeclaration subDeclaration : PerlSubUtil.findSubDeclarations(getProject(), packageName + "::" + subName))
					if (!subDeclaration.isEquivalentTo(parent))
						result.add(subDeclaration);
		}

		return result;
	}

	// fixme getRelatedGlobs => getSubAliases
	// fixme not dry with declarations and definitions
	@Override
	public List<PerlGlobVariable> getRelatedGlobs()
	{
		List<PerlGlobVariable> result = new ArrayList<>();
		PsiElement parent = getParent();

		String packageName = getPackageName();
		String subName = getName();

		if( subName != null  )
		{
			if ( parent instanceof PerlMethod && ((PerlMethod) parent).isObjectMethod())
				result.addAll(PerlDefaultMro.getSubAliases(getProject(), packageName, subName));
			else if (parent instanceof PerlMethod && "SUPER".equals(packageName))
				result.addAll(PerlDefaultMro.getSuperSubAliases(getProject(), ((PerlMethod) parent).getContextPackageName(), subName));
			else
				for (PerlGlobVariable glob : PerlGlobUtil.findGlobsDefinitions(getProject(), packageName + "::" + subName))
					if (!glob.isEquivalentTo(parent))
						result.add(glob);
		}

		return result;
	}

	@Override
	public boolean isBuiltIn()
	{
		return PerlSubUtil.isBuiltIn(getText());
	}
}


