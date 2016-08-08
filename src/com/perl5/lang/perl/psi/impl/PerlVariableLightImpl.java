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

package com.perl5.lang.perl.psi.impl;

import com.intellij.lang.Language;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import com.intellij.psi.impl.light.LightElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.perl5.lang.perl.idea.configuration.settings.PerlSharedSettings;
import com.perl5.lang.perl.idea.stubs.variables.PerlVariableStub;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.properties.PerlLexicalScope;
import com.perl5.lang.perl.psi.utils.PerlVariableAnnotations;
import com.perl5.lang.perl.psi.utils.PerlVariableType;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by hurricup on 17.01.2016.
 */
public class PerlVariableLightImpl extends LightElement implements PerlVariableLight
{
	protected final PerlVariableType myVariableType;
	protected final String myVariableName;
	protected final String myVariableClass;
	protected final PsiElement myParent;
	protected final boolean myIsLexical;
	protected final boolean myIsLocal;
	protected final boolean myIsInvocant;

	public PerlVariableLightImpl(@NotNull PsiManager manager,
								 @NotNull Language language,
								 @NotNull String variableName,
								 boolean isLexical,
								 boolean isLocal,
								 boolean isInvocant,
								 @NotNull PsiElement parent)
	{
		this(manager, language, variableName, null, isLexical, isLocal, isInvocant, parent);
	}

	public PerlVariableLightImpl(@NotNull PsiManager manager,
								 @NotNull Language language,
								 @NotNull String variableName,
								 @Nullable String variableClass,
								 boolean isLexical,
								 boolean isLocal,
								 boolean isInvocant,
								 @NotNull PsiElement parent)
	{
		super(manager, language);

		PerlVariableType type = null;

		if (variableName.startsWith("$"))
		{
			type = PerlVariableType.SCALAR;
		}
		else if (variableName.startsWith("@"))
		{
			type = PerlVariableType.ARRAY;
		}
		else if (variableName.startsWith("%"))
		{
			type = PerlVariableType.HASH;
		}

		if (type != null)
		{
			myVariableType = type;
			myVariableName = variableName.substring(1);
			myVariableClass = variableClass;
			myParent = parent;
			myIsLexical = isLexical;
			myIsLocal = isLocal;
			myIsInvocant = isInvocant;
		}
		else
		{
			throw new RuntimeException("Incorrect variable name, should start from sigil: " + variableName);
		}
	}

	@Override
	public String toString()
	{
		return getVariableType().getSigil() + getVariableName() + '@' + getVariableClass();
	}


	@Nullable
	@Override
	public String getDeclaredType()
	{
		return getVariableClass();
	}

	@Nullable
	@Override
	public String guessVariableType()
	{
		return getDeclaredType();
	}

	@Nullable
	@Override
	public String getVariableTypeHeavy()
	{
		return getDeclaredType();
	}

	@Override
	public PerlVariableType getActualType()
	{
		return getVariableType();
	}

	@Override
	public PerlVariableDeclarationWrapper getLexicalDeclaration()
	{
		return null;
	}

	@Override
	public List<PerlVariableDeclarationWrapper> getGlobalDeclarations()
	{
		return null;
	}

	@Override
	public List<PerlGlobVariable> getRelatedGlobs()
	{
		return null;
	}


	@Override
	public int getLineNumber()
	{
		Document document = PsiDocumentManager.getInstance(getProject()).getCachedDocument(getParent().getContainingFile());
		return document == null ? 0 : document.getLineNumber(getTextOffset()) + 1;
	}

	@Override
	public boolean isSelf()
	{
		return getActualType() == PerlVariableType.SCALAR && PerlSharedSettings.getInstance(getProject()).isSelfName(getName());
	}

	@Override
	public PerlLexicalScope getLexicalScope()
	{
		return PsiTreeUtil.getParentOfType(getParent(), PerlLexicalScope.class, false);
	}

	@Override
	public String getExplicitPackageName()
	{
		return null;
	}

	@Nullable
	@Override
	public String getContextPackageName()
	{
		return PerlPackageUtil.getContextPackageName(getParent());
	}

	@Nullable
	@Override
	public String getPackageName()
	{
		return getContextPackageName();
	}

	@Nullable
	@Override
	public String getCanonicalName()
	{
		String packageName = getPackageName();
		if (packageName == null)
		{
			return null;
		}
		return packageName + PerlPackageUtil.PACKAGE_SEPARATOR + getName();
	}

	@Override
	public PerlVariableNameElement getVariableNameElement()
	{
		return this;
	}

	@Override
	public boolean isBuiltIn()
	{
		return false;
	}

	@Override
	public boolean isDeprecated()
	{
		return false;
	}

	public PerlVariableType getVariableType()
	{
		return myVariableType;
	}

	public String getVariableName()
	{
		return myVariableName;
	}

	public String getVariableClass()
	{
		return myVariableClass;
	}

	public PsiElement getParent()
	{
		return myParent;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (!(o instanceof PerlVariable))
		{
			return false;
		}

		PerlVariable that = (PerlVariable) o;

		if (myVariableType != that.getActualType())
		{
			return false;
		}
		if (!myVariableName.equals(that.getName()))
		{
			return false;
		}
		return StringUtil.equals(getPackageName(), that.getPackageName());

	}

	@Override
	public int hashCode()
	{
		int result = myVariableType.hashCode();
		result = 31 * result + myVariableName.hashCode();
		return result;
	}

	@Override
	public PerlVariable getVariable()
	{
		return this;
	}

	@Override
	public boolean isLexicalDeclaration()
	{
		return myIsLexical;
	}

	@Override
	public boolean isLocalDeclaration()
	{
		return myIsLocal;
	}

	@Override
	public boolean isGlobalDeclaration()
	{
		return !myIsLexical;
	}

	@Override
	public boolean isInvocantDeclaration()
	{
		return myIsInvocant;
	}

	@Override
	public String getPresentableName()
	{
		return getCanonicalName();
	}

	@Nullable
	@Override
	public PsiElement getNameIdentifier()
	{
		return this;
	}

	@Override
	public PsiElement setName(@NonNls @NotNull String name) throws IncorrectOperationException
	{
		return this;
	}

	@Override
	public IStubElementType getElementType()
	{
		return null;
	}

	@Override
	public PerlVariableStub getStub()
	{
		return null;
	}

	@Override
	public int getTextOffset()
	{
		return getParent().getTextOffset();
	}

	@NotNull
	@Override
	public String getName()
	{
		return getVariableName();
	}

	@Override
	public boolean isDeclaration()
	{
		return true;
	}

	@Nullable
	@Override
	public PerlVariableAnnotations getVariableAnnotations()
	{
		return null;
	}

	@Nullable
	@Override
	public PerlVariableAnnotations getLocalVariableAnnotations()
	{
		return null;
	}

	@Nullable
	@Override
	public PerlVariableAnnotations getExternalVariableAnnotations()
	{
		return null;
	}
}

