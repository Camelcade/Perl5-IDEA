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

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.perl5.lang.perl.psi.PerlMethod;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 24.05.2015.
 */
public abstract class PerlMethodImplMixin extends ASTWrapperPsiElement implements PerlMethod
{
	public PerlMethodImplMixin(@NotNull ASTNode node){
		super(node);
	}

	// Attempt to detect namespace from direct specification, object or traversing to the namespace
	public String getNamespaceName()
	{
		String result = null;
		if( getNamespace() != null )
		{
			// got directly specified namespace
			result = getNamespace().getText();
		}
		else if( getObject() != null )
		{
			result = ((PerlObjectImpl)getObject()).getNamespaceName();
		}

		if( result == null )
		{
			// could not resolve package name, traversing up
			result = "main";
		}

		return result;
	}
}
