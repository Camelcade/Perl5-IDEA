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

import com.intellij.extapi.psi.StubBasedPsiElementBase;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.references.PerlVariableNameReference;
import com.perl5.lang.perl.psi.stubs.variables.PerlVariableStub;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by hurricup on 24.05.2015.
 *
 */
public abstract class PerlVariableImplMixin extends StubBasedPsiElementBase<PerlVariableStub> implements PerlElementTypes, PerlVariable //
{
	public final static Set<String> THIS_NAMES = new HashSet<>();
	static{
		THIS_NAMES.add("this");
		THIS_NAMES.add("self");
		THIS_NAMES.add("proto");
	}

	public PerlVariableImplMixin(PerlVariableStub stub, IStubElementType nodeType)
	{
		super(stub, nodeType);
	}


	public PerlVariableImplMixin(ASTNode node)
	{
		super(node);
	}

	@Override
	public PerlLexicalScope getLexicalScope()
	{
		return PsiTreeUtil.getParentOfType(this, PerlLexicalScope.class);
	}

	@Override
	public String getPackageName()
	{
		PerlVariableStub stub = getStub();
		if( stub != null)
			return stub.getPackageName();

		String namespace = getExplicitPackageName();

		if( namespace == null )
			namespace = getContextPackageName();

		return namespace;
	}

	@Override
	public String getContextPackageName()
	{
		return PerlPackageUtil.getContextPackageName(this);
	}

	@Override
	public String getExplicitPackageName()
	{
		PerlNamespace namespace = getNamespace();
		return namespace != null ? namespace.getName(): null;
	}

	@Override
	public PerlNamespace getNamespace()
	{
		return findChildByClass(PerlNamespace.class);
	}

	@Nullable
	@Override
	public PerlVariableName getVariableName()
	{
		return findChildByClass(PerlVariableName.class);
	}

	@Nullable
	@Override
	public String guessVariableType()
	{
		PerlVariableName variableNameObject = getVariableName();

		if( variableNameObject != null )
		{
			String variableName = variableNameObject.getName();

			if( this instanceof PerlPerlScalar && THIS_NAMES.contains(variableName))
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

			// todo check assignment expression with this variable in the left and guess from there (constructors, other vars that can have known type
		}

		return null;
	}
}
