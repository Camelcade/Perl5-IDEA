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
import com.perl5.lang.perl.psi.PerlDelegatingLightNamedElement;
import com.perl5.lang.perl.psi.PerlDelegatingSubElement;
import com.perl5.lang.perl.psi.PerlString;
import com.perl5.lang.perl.psi.PsiPerlAnonHash;
import com.perl5.lang.perl.psi.impl.PerlPolyNamedElementBase;
import com.perl5.lang.perl.psi.stubs.PerlPolyNamedElementStub;
import com.perl5.lang.perl.util.PerlArrayUtil;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PerlConstantsWrapperMixin extends PerlPolyNamedElementBase {

  public PerlConstantsWrapperMixin(@NotNull PerlPolyNamedElementStub stub, @NotNull IStubElementType nodeType) {
    super(stub, nodeType);
  }

  public PerlConstantsWrapperMixin(@NotNull ASTNode node) {
    super(node);
  }

  @NotNull
  @Override
  protected List<PerlDelegatingLightNamedElement> calcLightElementsFromPsi() {
    PsiElement firstChild = getFirstChild();
    if (firstChild instanceof PsiPerlAnonHash) {
      firstChild = ((PsiPerlAnonHash)firstChild).getExpr();
    }

    boolean isKey = true;
    List<PerlDelegatingLightNamedElement> result = new ArrayList<>();
    for (PsiElement listElement : PerlArrayUtil.getElementsAsPlainList(firstChild, null)) {
      if (listElement instanceof PerlString) {
        result.add(new PerlDelegatingSubElement(
          this,
          ElementManipulators.getValueText(listElement),
          PerlPackageUtil.getContextPackageName(this),
          Collections.emptyList(),
          null,
          listElement
        ));
      }
      isKey = !isKey;
    }

    return result;
  }
}
