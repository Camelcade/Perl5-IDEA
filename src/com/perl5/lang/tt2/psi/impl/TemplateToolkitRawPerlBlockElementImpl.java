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

package com.perl5.lang.tt2.psi.impl;

import com.intellij.lang.ASTNode;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.psi.PerlVariableDeclarationWrapper;
import com.perl5.lang.perl.psi.impl.PerlVariableLightImpl;
import com.perl5.lang.tt2.psi.TemplateToolkitRawPerlBlockElement;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hurricup on 11.06.2016.
 */
public class TemplateToolkitRawPerlBlockElementImpl extends TemplateToolkitPerlBlockElementImpl implements TemplateToolkitRawPerlBlockElement
{
	private List<PerlVariableDeclarationWrapper> myImplicitVariables = null;

	public TemplateToolkitRawPerlBlockElementImpl(@NotNull ASTNode node)
	{
		super(node);
	}

	@NotNull
	@Override
	public List<PerlVariableDeclarationWrapper> getImplicitVariables()
	{
		if (myImplicitVariables == null)
		{
			List<PerlVariableDeclarationWrapper> variables = new ArrayList<PerlVariableDeclarationWrapper>(super.getImplicitVariables());
			variables.add(new PerlVariableLightImpl(
					getManager(),
					PerlLanguage.INSTANCE,
					"$output",
					true,
					false,
					false,
					this
			));
			variables.add(new PerlVariableLightImpl(
					getManager(),
					PerlLanguage.INSTANCE,
					"$error",
					true,
					false,
					false,
					this
			));
			myImplicitVariables = variables;
		}
		return myImplicitVariables;
	}
}

