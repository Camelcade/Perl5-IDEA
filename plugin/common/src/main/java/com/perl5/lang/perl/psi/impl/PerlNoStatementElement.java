/*
 * Copyright 2015-2025 Alexandr Evstigneev
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

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.stubs.IStubElementType;
import com.perl5.lang.perl.psi.PerlVisitor;
import com.perl5.lang.perl.psi.light.PerlDelegatingLightNamedElement;
import com.perl5.lang.perl.psi.stubs.imports.PerlUseStatementStub;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;


public class PerlNoStatementElement extends PerlUseStatementElementBase {
  public PerlNoStatementElement(ASTNode node) {
    super(node);
  }

  public PerlNoStatementElement(PerlUseStatementStub stub, IStubElementType nodeType) {
    super(stub, nodeType);
  }

  @Override
  public final @NotNull List<PerlDelegatingLightNamedElement<?>> computeLightElementsFromPsi() {
    return Collections.emptyList();
  }

  @Override
  protected final @NotNull List<PerlDelegatingLightNamedElement<?>> computeLightElementsFromStubs(@NotNull PerlUseStatementStub stub) {
    return Collections.emptyList();
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PerlVisitor perlVisitor) {
      perlVisitor.visitNoStatement(this);
    }
    else {
      super.accept(visitor);
    }
  }
}
