/*
 * Copyright 2015-2020 Alexandr Evstigneev
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
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValue;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValues;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.light.PerlDelegatingLightNamedElement;
import com.perl5.lang.perl.psi.stubs.calls.EmptyCallData;
import com.perl5.lang.perl.psi.stubs.calls.PerlSubCallElementData;
import com.perl5.lang.perl.psi.stubs.calls.PerlSubCallElementStub;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public abstract class PerlSubCallElement extends PerlPolyNamedElement<PerlSubCallElementStub>
  implements PerlMethodContainer, PerlSelfHinterElement, PsiPerlExpr {
  private static final Logger LOG = Logger.getInstance(PerlSubCallElement.class);

  public PerlSubCallElement(@NotNull PerlSubCallElementStub stub,
                            @NotNull IStubElementType nodeType) {
    super(stub, nodeType);
  }

  public PerlSubCallElement(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public final @NotNull List<? extends PerlDelegatingLightNamedElement<?>> computeLightElementsFromPsi() {
    PerlSubCallHandler<?> lightElementProvider = getHandler();
    return lightElementProvider == null ? Collections.emptyList() : lightElementProvider.computeLightElementsFromPsi(this);
  }

  @Override
  protected @NotNull List<? extends PerlDelegatingLightNamedElement<?>> computeLightElementsFromStubs(@NotNull PerlSubCallElementStub stub) {
    PerlSubCallHandler<?> lightElementProvider = getHandler();
    return lightElementProvider == null ? Collections.emptyList() : lightElementProvider.computeLightElementsFromStubs(this, stub);
  }

  @Override
  public @Nullable PsiPerlMethod getMethod() {
    return findChildByClass(PsiPerlMethod.class);
  }

  public @Nullable String getSubName() {
    PerlSubCallElementStub stub = getGreenStub();
    if (stub != null) {
      return stub.getSubName();
    }
    PsiPerlMethod method = getMethod();
    if (method == null) {
      return null;
    }
    PerlSubNameElement subNameElement = method.getSubNameElement();
    return subNameElement == null ? null : subNameElement.getName();
  }

  public @NotNull PerlSubCallElementData getCallData() {
    PerlSubCallHandler<?> lightElementProvider = getHandler();
    if (lightElementProvider == null) {
      LOG.error("Trying to get call data for: " + getText());
      return EmptyCallData.INSTANCE;
    }
    return lightElementProvider.getCallData(this);
  }

  @Override
  public @NotNull PerlValue getSelfType() {
    PerlSubCallHandler<?> callHandler = getHandler();
    return callHandler == null ? PerlValues.UNKNOWN_VALUE : callHandler.getSelfType();
  }

  public @Nullable PerlSubCallHandler<?> getHandler() {
    return PerlSubCallHandler.getHandler(getSubName());
  }

  public static boolean isNestedCall(@Nullable PsiElement psiElement) {
    return psiElement instanceof PerlSubCallElement && psiElement.getParent() instanceof PsiPerlDerefExpr;
  }
}
