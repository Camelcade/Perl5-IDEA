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
import com.intellij.psi.stubs.IStubElementType;
import com.perl5.lang.perl.psi.PerlNamespace;
import com.perl5.lang.perl.psi.PsiPerlNamespaceDefinition;
import com.perl5.lang.perl.psi.stubs.namespaces.PerlNamespaceDefinitionStub;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 28.05.2015.
 */
public abstract class PerlNamespaceDefinitionImpl extends StubBasedPsiElementBase<PerlNamespaceDefinitionStub>  implements PsiPerlNamespaceDefinition
{
	public PerlNamespaceDefinitionImpl(@NotNull ASTNode node){
		super(node);
	}

	public PerlNamespaceDefinitionImpl(@NotNull PerlNamespaceDefinitionStub stub, @NotNull IStubElementType nodeType) {
		super(stub,nodeType);
	}

	@Override
	public PerlNamespace getNamespaceElement()
	{
		return findChildByClass(PerlNamespace.class);
	}

}
