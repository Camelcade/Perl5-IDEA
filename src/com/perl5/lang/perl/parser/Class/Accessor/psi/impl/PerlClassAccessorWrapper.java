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

package com.perl5.lang.perl.parser.Class.Accessor.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.BaseScopeProcessor;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.perl.parser.Class.Accessor.psi.stubs.PerlClassAccessorWrapperStub;
import com.perl5.lang.perl.psi.PsiPerlNamespaceContent;
import com.perl5.lang.perl.psi.impl.PerlPolyNamedNestedCallElementBase;
import com.perl5.lang.perl.psi.light.PerlDelegatingLightNamedElement;
import com.perl5.lang.perl.psi.stubs.PerlPolyNamedElementStub;
import com.perl5.lang.perl.psi.utils.PerlResolveUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

import static com.perl5.lang.perl.parser.Class.Accessor.ClassAccessorElementTypes.CLASS_ACCESSOR_FBP;

public class PerlClassAccessorWrapper extends PerlPolyNamedNestedCallElementBase<PerlClassAccessorWrapperStub> {
  public PerlClassAccessorWrapper(@NotNull PerlClassAccessorWrapperStub stub,
                                  @NotNull IStubElementType nodeType) {
    super(stub, nodeType);
  }

  public PerlClassAccessorWrapper(@NotNull ASTNode node) {
    super(node);
  }

  @NotNull
  @Override
  public List<PerlDelegatingLightNamedElement> calcLightElementsFromStubs(@NotNull PerlPolyNamedElementStub stub) {
    return Collections.emptyList();
  }

  @NotNull
  @Override
  public List<PerlDelegatingLightNamedElement> calcLightElementsFromPsi() {
    return Collections.emptyList();
  }

  public boolean isFollowBestPractice() {
    PerlPolyNamedElementStub stub = getStub();
    if (stub != null) {
      return ((PerlClassAccessorWrapperStub)stub).isFollowBestPractice();
    }
    PerlClassAccessorWrapperStub greenStub = getGreenStub();
    if (greenStub != null) {
      return greenStub.isFollowBestPractice();
    }

    boolean[] result = new boolean[]{false};

    // fixme we need a smarter treewalkup here, scopes are not necessary here
    PerlResolveUtil.treeWalkUp(this, new BaseScopeProcessor() {
      @Override
      public boolean execute(@NotNull PsiElement element, @NotNull ResolveState state) {
        if (element instanceof PsiPerlNamespaceContent) {
          return false;
        }

        if (PsiUtilCore.getElementType(element) == CLASS_ACCESSOR_FBP) {
          result[0] = true;
          return false;
        }

        return true;
      }
    });
    return result[0];
  }

}
