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

package com.perl5.lang.mason2.psi.impl;

import com.intellij.lang.ASTNode;
import com.perl5.lang.mason2.psi.MasonAroundMethodModifier;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.psi.impl.PerlVariableLightImpl;

/**
 * Created by hurricup on 08.01.2016.
 */
public class MasonAroundMethodModifierImpl extends MasonMethodModifierImpl implements MasonAroundMethodModifier
{
	protected static final String ORIG_VARIABLE_NAME = "$orig";

	public MasonAroundMethodModifierImpl(ASTNode node)
	{
		super(node);
	}

	@Override
	protected void fillImplicitVariables()
	{
		super.fillImplicitVariables();
		if (IMPLICIT_VARIABLES != null)
		{
			IMPLICIT_VARIABLES.add(new PerlVariableLightImpl(
					getManager(),
					PerlLanguage.INSTANCE,
					ORIG_VARIABLE_NAME,
					true,
					false,
					false,
					this
			));
		}
	}
}
