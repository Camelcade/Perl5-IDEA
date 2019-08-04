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

import com.intellij.codeInsight.daemon.impl.Divider;
import com.intellij.codeInsight.daemon.impl.LocalInspectionsPass;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.ResolveState;
import com.intellij.psi.StubBasedPsiElement;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.perl.psi.PerlStubBasedPsiElementBase;
import com.perl5.lang.perl.psi.PerlVisitor;
import com.perl5.lang.perl.psi.light.PerlDelegatingLightNamedElement;
import com.perl5.lang.perl.psi.stubs.PerlPolyNamedElementStub;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.*;

public abstract class PerlPolyNamedElement<Stub extends PerlPolyNamedElementStub> extends PerlStubBasedPsiElementBase<Stub>
  implements StubBasedPsiElement<Stub> {
  public PerlPolyNamedElement(@NotNull Stub stub, @NotNull IStubElementType nodeType) {
    super(stub, nodeType);
  }

  public PerlPolyNamedElement(@NotNull ASTNode node) {
    super(node);
  }

  @NotNull
  public final List<PerlDelegatingLightNamedElement> getLightElements() {
    return CachedValuesManager.getCachedValue(this, () -> CachedValueProvider.Result.create(computeLightElements(), this)
    );
  }

  /**
   * Calculates light elements from stubs or psi
   */
  @NotNull
  public final List<PerlDelegatingLightNamedElement> computeLightElements() {
    Stub stub = getGreenStub();
    if (stub != null) {
      return computeLightElementsFromStubs(stub);
    }
    return computeLightElementsFromPsi();
  }

  /**
   * Internal sub for calculating light elements from psi
   */
  @NotNull
  public abstract List<PerlDelegatingLightNamedElement> computeLightElementsFromPsi();

  /**
   * Internal sub for calculating light elements from stubs
   */
  @NotNull
  protected abstract List<PerlDelegatingLightNamedElement> computeLightElementsFromStubs(@NotNull Stub stub);

  /**
   * @implNote we need to accept light elements here, because in non-recursive visitors case, platform collects elements by itself by
   * traversing the tree
   * @see LocalInspectionsPass#inspect(java.util.List, com.intellij.codeInspection.InspectionManager, boolean, com.intellij.openapi.progress.ProgressIndicator)
   * @see Divider#divideInsideAndOutsideInOneRoot(com.intellij.psi.PsiFile, com.intellij.openapi.util.TextRange, com.intellij.openapi.util.TextRange, java.util.List, java.util.List, java.util.List, java.util.List, java.util.List, java.util.List, boolean)
   */
  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PerlVisitor) {
      ((PerlVisitor)visitor).visitPolyNamedElement(this);
    }
    else {
      super.accept(visitor);
    }
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + "(" + getElementType().toString() + ")";
  }

  @Override
  public boolean processDeclarations(@NotNull PsiScopeProcessor processor,
                                     @NotNull ResolveState state,
                                     PsiElement lastParent,
                                     @NotNull PsiElement place) {
    for (PerlDelegatingLightNamedElement namedElement : getLightElements()) {
      if (!processor.execute(namedElement, state)) {
        return false;
      }
    }

    return super.processDeclarations(processor, state, lastParent, place);
  }

  /**
   * Checks if we can treat this element as text identifier. Atm: no interpolation, no XQ strings
   */
  public boolean isAcceptableIdentifierElement(@Nullable PsiElement identifierElement) {
    if (identifierElement == null) {
      return false;
    }
    IElementType elementType = PsiUtilCore.getElementType(identifierElement);
    if (elementType == STRING_BARE || elementType == STRING_SQ || elementType == STRING_CONTENT) {
      return true;
    }
    else if (elementType == STRING_XQ) {
      return false;
    }
    else if (elementType == STRING_DQ) {
      PsiElement[] children = identifierElement.getChildren();
      return children.length == 0 ||
             children.length == 1 && PsiUtilCore.getElementType(children[0]) == LP_STRING_QQ && children[0].getChildren().length == 0;
    }
    return false;
  }
}
