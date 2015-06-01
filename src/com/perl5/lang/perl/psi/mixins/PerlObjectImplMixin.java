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

package com.perl5.lang.perl.psi.mixins;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.references.PerlVariableNameReference;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by hurricup on 24.05.2015.
 * This class represents an object, which method being invoked; At the moment it may be represented with scalar variable only
 */
public abstract class PerlObjectImplMixin extends ASTWrapperPsiElement implements PerlObject
{
	public final static Set<String> THIS_NAMES = new HashSet<>();
	static{
		THIS_NAMES.add("this");
		THIS_NAMES.add("self");
		THIS_NAMES.add("proto");
	}

	public PerlObjectImplMixin(@NotNull ASTNode node){
		super(node);
	}

	// todo probably, this should be moved to variable, scalar specifically
	@Nullable
	@Override
	public String guessNamespace()
	{
		// at the moment object is only wrapper for scalar variable, nothing else
		PerlVariable scalar = getPerlScalar();

		if( scalar != null )
		{
			PerlVariableName variableNameObject = scalar.getVariableName();

			if( variableNameObject != null )
			{
				String variableName = variableNameObject.getName();

				if( THIS_NAMES.contains(variableName))
					return PerlPackageUtil.getContextPackageName(this);

				// find declaration and check type
				PsiReference[] references = variableNameObject.getReferences();

				for( PsiReference reference: references)
				{
					assert reference instanceof PerlVariableNameReference;
					ResolveResult[] results = ((PerlVariableNameReference) reference).multiResolve(false);

					for( ResolveResult result: results)
					{
						PsiElement decalarationVariableName = result.getElement();
						IPerlVariableDeclaration declaration = PsiTreeUtil.getParentOfType(decalarationVariableName, IPerlVariableDeclaration.class);
						if( declaration != null )
						{
							PerlNamespace declarationNamespace = declaration.getNamespace();
							if( declarationNamespace != null )
							{
								return declarationNamespace.getName();
							}
						}
					}
				}
			}
		}

		return null;
	}
}
