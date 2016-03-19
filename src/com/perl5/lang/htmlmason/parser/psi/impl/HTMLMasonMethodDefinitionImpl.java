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

package com.perl5.lang.htmlmason.parser.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.stubs.IStubElementType;
import com.perl5.lang.htmlmason.parser.psi.HTMLMasonMethodDefinition;
import com.perl5.lang.htmlmason.parser.stubs.HTMLMasonMethodDefinitionStub;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 09.03.2016.
 */
public class HTMLMasonMethodDefinitionImpl extends HTMLMasonStubBasedNamedElement<HTMLMasonMethodDefinitionStub> implements HTMLMasonMethodDefinition
{
	public HTMLMasonMethodDefinitionImpl(@NotNull ASTNode node)
	{
		super(node);
	}

	public HTMLMasonMethodDefinitionImpl(@NotNull HTMLMasonMethodDefinitionStub stub, @NotNull IStubElementType nodeType)
	{
		super(stub, nodeType);
	}

	@Nullable
	@Override
	protected String getNameFromStub()
	{
		HTMLMasonMethodDefinitionStub stub = getStub();
		return stub == null ? null : stub.getName();
	}
}
