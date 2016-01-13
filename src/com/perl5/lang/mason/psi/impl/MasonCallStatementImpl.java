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

package com.perl5.lang.mason.psi.impl;

import com.intellij.lang.ASTNode;
import com.perl5.lang.mason.psi.MasonCallStatement;
import com.perl5.lang.perl.psi.impl.PsiPerlStatementImpl;

/**
 * Created by hurricup on 13.01.2016.
 */
public class MasonCallStatementImpl extends PsiPerlStatementImpl implements MasonCallStatement
{
	public MasonCallStatementImpl(ASTNode node)
	{
		super(node);
	}
}
