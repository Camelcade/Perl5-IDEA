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

package com.perl5.lang.perl.psi.impl;

import com.intellij.extapi.psi.StubBasedPsiElementBase;
import com.intellij.lang.ASTNode;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.lang.perl.psi.PerlDelegatingLightNamedElement;
import com.perl5.lang.perl.psi.PerlPolyNamedElement;
import com.perl5.lang.perl.psi.stubs.PerlPolyNamedElementStub;
import com.perl5.lang.perl.psi.utils.PerlLightStubUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class PerlPolyNamedElementBase extends StubBasedPsiElementBase<PerlPolyNamedElementStub> implements PerlPolyNamedElement {
  public PerlPolyNamedElementBase(@NotNull PerlPolyNamedElementStub stub, @NotNull IStubElementType nodeType) {
    super(stub, nodeType);
  }

  public PerlPolyNamedElementBase(@NotNull ASTNode node) {
    super(node);
  }

  public PerlPolyNamedElementBase(PerlPolyNamedElementStub stub, IElementType nodeType, ASTNode node) {
    super(stub, nodeType, node);
  }

  @NotNull
  @Override
  public List<PerlDelegatingLightNamedElement> calcLightElements() {
    if (getGreenStub() != null) {
      return ContainerUtil.map(getGreenStub().getLightNamedElementsStubs(), stub -> PerlLightStubUtil.createPsiElement(stub, this));
    }
    else if (getStub() != null) {
      return ContainerUtil.map(getStub().getLightNamedElementsStubs(), stub -> PerlLightStubUtil.createPsiElement(stub, this));
    }
    return calcLightElementsFromPsi();
  }

  @NotNull
  protected abstract List<PerlDelegatingLightNamedElement> calcLightElementsFromPsi();
}
