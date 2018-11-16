/*
 * Copyright 2015-2017 Alexandr Evstigneev
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
import com.intellij.psi.stubs.IStubElementType;
import com.perl5.lang.perl.extensions.PerlImplicitVariablesProvider;
import com.perl5.lang.perl.psi.PerlVariableDeclarationElement;
import com.perl5.lang.perl.psi.impl.PsiPerlMethodDefinitionImpl;
import com.perl5.lang.perl.psi.stubs.subsdefinitions.PerlSubDefinitionStub;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by hurricup on 08.01.2016.
 */
public class MasonMethodDefinition extends PsiPerlMethodDefinitionImpl implements PerlImplicitVariablesProvider {
  public MasonMethodDefinition(ASTNode node) {
    super(node);
  }

  public MasonMethodDefinition(PerlSubDefinitionStub stub, IStubElementType nodeType) {
    super(stub, nodeType);
  }

  @NotNull
  @Override
  public List<PerlVariableDeclarationElement> getImplicitVariables() {
    if (myImplicitVariables == null) {
      myImplicitVariables = buildImplicitVariables();
    }
    return myImplicitVariables;
  }
}
