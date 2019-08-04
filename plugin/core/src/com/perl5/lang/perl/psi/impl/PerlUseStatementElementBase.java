/*
 * Copyright 2015-2019 Alexandr Evstigneev
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
import com.intellij.psi.stubs.IStubElementType;
import com.perl5.lang.perl.extensions.packageprocessor.PerlPackageProcessor;
import com.perl5.lang.perl.extensions.packageprocessor.PerlPackageProcessorDefault;
import com.perl5.lang.perl.extensions.packageprocessor.PerlVersionProcessor;
import com.perl5.lang.perl.idea.EP.PerlPackageProcessorEP;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.properties.PerlNamespaceElementContainer;
import com.perl5.lang.perl.psi.stubs.imports.PerlUseStatementStub;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public abstract class PerlUseStatementElementBase extends PerlPolyNamedElement<PerlUseStatementStub>
  implements PerlNamespaceElementContainer,
             PerlCompositeElement,
             PsiPerlStatement,
             PerlUseStatement {
  public PerlUseStatementElementBase(@NotNull PerlUseStatementStub stub,
                                     @NotNull IStubElementType nodeType) {
    super(stub, nodeType);
  }

  public PerlUseStatementElementBase(@NotNull ASTNode node) {
    super(node);
  }

  public boolean isPragma() {
    return getPackageProcessor().isPragma();
  }

  public boolean isVersion() {
    return getNamespaceElement() == null && getVersionElement() != null;
  }

  public boolean isPragmaOrVersion() {
    return isPragma() || isVersion();
  }

  @Nullable
  @Override
  public String getPackageName() {
    PerlUseStatementStub stub = getGreenStub();
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

  public PerlVersionElement getVersionElement() {
    return findChildByClass(PerlVersionElement.class);
  }

  @Override
  @Nullable
  public List<String> getImportParameters() {
    PerlUseStatementStub stub = getGreenStub();
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
  public PerlPackageProcessor getPackageProcessor() {
    PerlPackageProcessor packageProcessor = null;

    // package name processor
    String packageName = getPackageName();
    if (packageName != null) {
      packageProcessor = PerlPackageProcessorEP.EP.findSingle(packageName);
    }
    else if (getVersionElement() != null) {
      packageProcessor = PerlVersionProcessor.getProcessor(this);
    }

    return packageProcessor == null ? PerlPackageProcessorDefault.INSTANCE : packageProcessor;
  }

  @NotNull
  @Override
  public String getNamespaceName() {
    PerlUseStatementStub stub = getGreenStub();
    if (stub != null) {
      return stub.getNamespaceName();
    }

    return PerlPackageUtil.getContextNamespaceName(this);
  }

  @Nullable
  public PsiPerlExpr getExpr() {
    return findChildByClass(PsiPerlExpr.class);
  }

  /**
   * @return text that should be shown in folded block of use arguments
   */
  @Nullable
  public String getArgumentsFoldingText() {
    return getPackageProcessor().getArgumentsFoldingText(this);
  }

  /**
   * @return true iff arguments of this use statement should be collapesed by default, e.g. group of constants definitions
   */
  public boolean isFoldedByDefault() {
    return getPackageProcessor().isFoldedByDefault(this);
  }
}
