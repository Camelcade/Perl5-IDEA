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

package com.perl5.lang.perl.psi.mixins;

import com.intellij.lang.ASTNode;
import com.intellij.psi.ElementManipulators;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.perl5.lang.perl.psi.PerlDoExpr;
import com.perl5.lang.perl.psi.PerlString;
import com.perl5.lang.perl.psi.PerlStubBasedPsiElementBase;
import com.perl5.lang.perl.psi.stubs.imports.runtime.PerlRuntimeImportStub;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 19.09.2015.
 */
public abstract class PerlDoExprMixin extends PerlStubBasedPsiElementBase<PerlRuntimeImportStub> implements PerlDoExpr {
  public PerlDoExprMixin(ASTNode node) {
    super(node);
  }

  public PerlDoExprMixin(PerlRuntimeImportStub stub, IStubElementType nodeType) {
    super(stub, nodeType);
  }

  @Nullable
  @Override
  public String getImportPath() {
    PerlRuntimeImportStub stub = getStub();
    if (stub != null) {
      return stub.getImportPath();
    }

    return findImportPath();
  }

  @Nullable
  protected String findImportPath() {
    PsiElement lastChild = getLastChild();
    if (lastChild instanceof PerlString)    // seems we've got require "...";
    {
      return ElementManipulators.getValueText(lastChild);
    }
    return null;
  }
}
