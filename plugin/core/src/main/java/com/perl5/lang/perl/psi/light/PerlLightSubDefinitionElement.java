/*
 * Copyright 2015-2022 Alexandr Evstigneev
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

package com.perl5.lang.perl.psi.light;

import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.util.AtomicNotNullLazyValue;
import com.intellij.openapi.util.NotNullFactory;
import com.intellij.openapi.util.NotNullLazyValue;
import com.intellij.openapi.util.NullableLazyValue;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.stubs.IStubElementType;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValue;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValuesManager;
import com.perl5.lang.perl.idea.presentations.PerlItemPresentationSimpleDynamicLocation;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.impl.PerlPolyNamedElement;
import com.perl5.lang.perl.psi.stubs.subsdefinitions.PerlSubDefinitionStub;
import com.perl5.lang.perl.psi.utils.PerlResolveUtil;
import com.perl5.lang.perl.psi.utils.PerlSubAnnotations;
import com.perl5.lang.perl.psi.utils.PerlSubArgument;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.List;

import static com.intellij.openapi.util.NullableLazyValue.atomicLazyNullable;

public class PerlLightSubDefinitionElement<Delegate extends PerlPolyNamedElement<?>> extends PerlDelegatingLightNamedElement<Delegate>
  implements PerlSubDefinitionElement {
  private final @Nullable String myNamespaceName;
  private final @NotNull NullableLazyValue<PerlSubAnnotations> myAnnotationsProvider;
  private @NotNull NotNullLazyValue<List<PerlSubArgument>> mySubArgumentsProvider;
  private @NotNull NotNullLazyValue<PerlValue> myReturnValueFromCodeProvider;
  // fixme should we actualize this on fly, like identifier?
  private @Nullable PsiPerlBlock mySubDefinitionBody;

  public PerlLightSubDefinitionElement(@NotNull Delegate delegate,
                                       @NotNull String name,
                                       @NotNull IStubElementType<?, ?> elementType,
                                       @Nullable PsiElement nameIdentifier,
                                       @Nullable String namespaceName,
                                       @NotNull List<PerlSubArgument> subArguments,
                                       @Nullable PerlSubAnnotations annotations,
                                       @NotNull NotNullLazyValue<PerlValue> returnValueFromCodeProvider,
                                       @Nullable PsiPerlBlock subDefinitionBody) {
    super(delegate, name, elementType, nameIdentifier);
    myNamespaceName = namespaceName;
    myAnnotationsProvider = atomicLazyNullable(() -> annotations);
    mySubArgumentsProvider = AtomicNotNullLazyValue.createValue(() -> subArguments);
    myReturnValueFromCodeProvider = returnValueFromCodeProvider;
    mySubDefinitionBody = subDefinitionBody;
  }

  public PerlLightSubDefinitionElement(@NotNull Delegate delegate,
                                       @NotNull String name,
                                       @NotNull IStubElementType<?, ?> elementType,
                                       @NotNull PsiElement nameIdentifier,
                                       @Nullable String namespaceName,
                                       @NotNull PerlSubExpr elementSub) {
    super(delegate, name, elementType, nameIdentifier);
    myNamespaceName = namespaceName;
    mySubDefinitionBody = elementSub.getBlock();
    myAnnotationsProvider = atomicLazyNullable(
      () -> PerlSubAnnotations.computeForLightElement(delegate, nameIdentifier));
    mySubArgumentsProvider = AtomicNotNullLazyValue.createValue(
      () -> PerlSubDefinitionElement.getPerlSubArgumentsFromBody(mySubDefinitionBody));
    myReturnValueFromCodeProvider = PerlResolveUtil.computeReturnValueFromControlFlowLazy(elementSub);
  }

  public PerlLightSubDefinitionElement(@NotNull Delegate delegate, @NotNull PerlSubDefinitionStub stub) {
    super(delegate, stub.getSubName(), stub.getStubType());
    myNamespaceName = stub.getNamespaceName();
    mySubArgumentsProvider = AtomicNotNullLazyValue.createValue(stub::getSubArgumentsList);
    myAnnotationsProvider = atomicLazyNullable(stub::getAnnotations);
    myReturnValueFromCodeProvider = AtomicNotNullLazyValue.createValue(stub::getReturnValueFromCode);
  }

  protected final void setSubArgumentsProvider(@NotNull NotNullFactory<List<PerlSubArgument>> provider) {
    mySubArgumentsProvider = AtomicNotNullLazyValue.createValue(provider);
  }

  @Override
  public @Nullable String getNamespaceName() {
    return myNamespaceName;
  }

  @Override
  public @NotNull List<PerlSubArgument> getSubArgumentsList() {
    return mySubArgumentsProvider.getValue();
  }

  @Override
  public String getSubName() {
    return myName;
  }

  @Override
  public @Nullable PerlSubAnnotations getAnnotations() {
    return myAnnotationsProvider.getValue();
  }

  @Override
  public @Nullable String getExplicitNamespaceName() {
    return myNamespaceName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof PerlLightSubDefinitionElement<?>)) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }

    PerlLightSubDefinitionElement<?> element = (PerlLightSubDefinitionElement<?>)o;

    if (getNamespaceName() != null ? !getNamespaceName().equals(element.getNamespaceName()) : element.getNamespaceName() != null) {
      return false;
    }
    if (!getSubArgumentsList().equals(element.getSubArgumentsList())) {
      return false;
    }
    return getAnnotations() != null ? getAnnotations().equals(element.getAnnotations()) : element.getAnnotations() == null;
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (getNamespaceName() != null ? getNamespaceName().hashCode() : 0);
    result = 31 * result + getSubArgumentsList().hashCode();
    result = 31 * result + (getAnnotations() != null ? getAnnotations().hashCode() : 0);
    return result;
  }

  @Override
  public @Nullable Icon getIcon(int flags) {
    if (isMethod()) {
      return PerlIcons.METHOD_GUTTER_ICON;
    }
    else {
      return PerlIcons.SUB_GUTTER_ICON;
    }
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PerlVisitor) {
      ((PerlVisitor)visitor).visitPerlSubDefinitionElement(this);
    }
    else {
      super.accept(visitor);
    }
  }

  @Override
  public ItemPresentation getPresentation() {
    return new PerlItemPresentationSimpleDynamicLocation(this, getSubName());
  }

  @Override
  public @Nullable PsiPerlBlock getSubDefinitionBody() {
    return mySubDefinitionBody;
  }

  @Override
  public String toString() {
    return super.toString() + "@" + getCanonicalName();
  }

  @Override
  public @NotNull PerlValue getReturnValueFromCode() {
    return myReturnValueFromCodeProvider.getValue();
  }

  public void setReturnValueFromCode(@NotNull PerlValue returnValueFromCode) {
    myReturnValueFromCodeProvider = PerlValuesManager.lazy(returnValueFromCode);
  }

  @Override
  public @Nullable PsiElement getControlFlowElement() {
    return getSubDefinitionBody();
  }

  @Override
  public @Nullable PsiPerlSignatureContent getSignatureContent() {
    return null;
  }
}
