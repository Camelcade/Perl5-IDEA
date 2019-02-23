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
import com.perl5.lang.perl.extensions.packageprocessor.PerlPackageProcessor;
import com.perl5.lang.perl.extensions.packageprocessor.PerlPackageProcessorDefault;
import com.perl5.lang.perl.extensions.packageprocessor.PerlVersionProcessor;
import com.perl5.lang.perl.idea.EP.PerlPackageProcessorEP;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.stubs.imports.PerlUseStatementStub;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

/**
 * Created by hurricup on 31.05.2015.
 */
public abstract class PerlUseStatementMixin extends PerlStubBasedPsiElementBase<PerlUseStatementStub> implements PsiPerlUseStatement {
  public PerlUseStatementMixin(ASTNode node) {
    super(node);
  }

  public PerlUseStatementMixin(PerlUseStatementStub stub, IStubElementType nodeType) {
    super(stub, nodeType);
  }

  @Override
  public boolean isPragma() {
    return getPackageProcessor().isPragma();
  }

  @Override
  public boolean isVersion() {
    return getNamespaceElement() == null && getVersionElement() != null;
  }

  @Override
  public boolean isPragmaOrVersion() {
    return isPragma() || isVersion();
  }

  @Override
  public String getPackageName() {
    PerlUseStatementStub stub = getStub();
    if (stub != null) {
      return stub.getPackageName();
    }

    PerlNamespaceElement ns = getNamespaceElement();
    if (ns != null) {
      return ns.getCanonicalName();
    }
    return null;
  }

  @Nullable
  @Override
  public PerlNamespaceElement getNamespaceElement() {
    return findChildByClass(PerlNamespaceElement.class);
  }

  @Override
  public PerlVersionElement getVersionElement() {
    return findChildByClass(PerlVersionElement.class);
  }

  @Override
  @Nullable
  public List<String> getImportParameters() {
    PerlUseStatementStub stub = getStub();
    if (stub != null) {
      return stub.getImportParameters();
    }

    if (getExpr() == null) {
      return null;
    }


    PerlNamespaceElement namespaceElement = getNamespaceElement();
    if (namespaceElement != null) {
      return PerlPsiUtil.collectStringContents(namespaceElement.getNextSibling());
    }
    else {
      return Collections.emptyList();
    }
  }

  @NotNull
  @Override
  public PerlPackageProcessor getPackageProcessor() {
    PerlPackageProcessor packageProcessor = null;

    // package name processor
    String packageName = getPackageName();
    if (packageName != null) {
      packageProcessor = PerlPackageProcessorEP.EP.findSingle(packageName);
    }
    else if (getVersionElement() != null) {
      packageProcessor = PerlVersionProcessor.getProcessor(PerlUseStatementMixin.this);
    }

    return packageProcessor == null ? PerlPackageProcessorDefault.INSTANCE : packageProcessor;
  }

  @Nullable
  @Override
  public String getOuterPackageName() {
    PerlUseStatementStub stub = getStub();
    if (stub != null) {
      return stub.getOuterPackageName();
    }

    return PerlPackageUtil.getContextNamespaceName(this);
  }

  @Override
  @Nullable
  public PsiPerlExpr getExpr() {
    return findChildByClass(PsiPerlExpr.class);
  }
}
