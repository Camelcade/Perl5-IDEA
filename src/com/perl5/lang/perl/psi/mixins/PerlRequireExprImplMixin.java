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
import com.intellij.psi.stubs.IStubElementType;
import com.perl5.lang.perl.psi.PerlNamespaceElement;
import com.perl5.lang.perl.psi.PerlRequireExpr;
import com.perl5.lang.perl.psi.stubs.imports.runtime.PerlRuntimeImportStub;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 31.05.2015.
 */
public abstract class PerlRequireExprImplMixin extends PerlDoExprImplMixin implements PerlRequireExpr {
  public PerlRequireExprImplMixin(ASTNode node) {
    super(node);
  }

  public PerlRequireExprImplMixin(PerlRuntimeImportStub stub, IStubElementType nodeType) {
    super(stub, nodeType);
  }

  @Override
  public PerlNamespaceElement getNamespaceElement() {
    return findChildByClass(PerlNamespaceElement.class);
  }

  @Nullable
  @Override
  protected String findImportPath() {
    if (getNamespaceElement() != null) {
      return PerlPackageUtil.getPackagePathByName(getNamespaceElement().getCanonicalName());
    }

    return super.findImportPath();
  }
}
