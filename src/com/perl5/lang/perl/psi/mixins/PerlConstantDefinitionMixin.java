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

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.perl5.lang.perl.psi.PsiPerlConstantDefinition;
import com.perl5.lang.perl.psi.impl.PerlCompositeElementImpl;

/**
 * Created by hurricup on 29.08.2015.
 */
public abstract class PerlConstantDefinitionMixin extends PerlCompositeElementImpl implements PsiPerlConstantDefinition
{
	public PerlConstantDefinitionMixin(ASTNode node)
	{
		super(node);
	}

	@Override
	public PsiElement getValueExpression()
	{
		PsiElement[] children = getChildren();
		if (children.length > 1)
		{
			return children[children.length - 1];
		}
		return null;
	}
}
