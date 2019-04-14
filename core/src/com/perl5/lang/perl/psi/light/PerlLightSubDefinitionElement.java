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

package com.perl5.lang.perl.psi.light;

import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.util.AtomicNotNullLazyValue;
import com.intellij.openapi.util.AtomicNullableLazyValue;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.stubs.IStubElementType;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValue;
import com.perl5.lang.perl.idea.presentations.PerlItemPresentationSimple;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.impl.PerlPolyNamedNestedCallElementBase;
import com.perl5.lang.perl.psi.stubs.subsdefinitions.PerlSubDefinitionStub;
import com.perl5.lang.perl.psi.utils.PerlResolveUtil;
import com.perl5.lang.perl.psi.utils.PerlSubAnnotations;
import com.perl5.lang.perl.psi.utils.PerlSubArgument;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.List;

import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlUnknownValue.UNKNOWN_PROVIDER;

public class PerlLightSubDefinitionElement<Delegate extends PerlPolyNamedElement> extends PerlDelegatingLightNamedElement<Delegate>
  implements PerlSubDefinitionElement {
  @Nullable
  private final String myPackageName;
  @NotNull
  private final AtomicNullableLazyValue<PerlSubAnnotations> myAnnotationsProvider;
  @NotNull
  private AtomicNotNullLazyValue<List<PerlSubArgument>> mySubArgumentsProvider;
  @NotNull
  private AtomicNotNullLazyValue<PerlValue> myReturnValueFromCodeProfider;
  // fixme should we actualize this on fly, like identifier?
  @Nullable
  private PsiPerlBlock mySubDefinitionBody;

  public PerlLightSubDefinitionElement(@NotNull Delegate delegate,
                                       @NotNull String name,
                                       @NotNull IStubElementType elementType,
                                       @Nullable PsiElement nameIdentifier,
                                       @Nullable String packageName,
                                       @Nullable PerlSubAnnotations annotations,
                                       @NotNull List<PerlSubArgument> subArguments,
                                       @NotNull AtomicNotNullLazyValue<PerlValue> returnValueFromCodeProfider,
                                       @Nullable PsiPerlBlock subDefinitionBody) {
    super(delegate, name, elementType, nameIdentifier);
    myPackageName = packageName;
    myAnnotationsProvider = AtomicNullableLazyValue.createValue(() -> annotations);
    mySubArgumentsProvider = AtomicNotNullLazyValue.createValue(() -> subArguments);
    myReturnValueFromCodeProfider = returnValueFromCodeProfider;
    mySubDefinitionBody = subDefinitionBody;
  }

  public PerlLightSubDefinitionElement(@NotNull Delegate delegate,
                                       @NotNull String name,
                                       @NotNull IStubElementType elementType,
                                       @NotNull PsiElement nameIdentifier,
                                       @Nullable String packageName,
                                       @NotNull PerlSubExpr elementSub) {
    super(delegate, name, elementType, nameIdentifier);
    myPackageName = packageName;
    mySubDefinitionBody = elementSub.getBlock();
    myAnnotationsProvider = AtomicNullableLazyValue.createValue(
      () -> PerlPolyNamedNestedCallElementBase.computeSubAnnotations(delegate, nameIdentifier));
    mySubArgumentsProvider = AtomicNotNullLazyValue.createValue(
      () -> PerlSubDefinitionElement.getPerlSubArgumentsFromBody(mySubDefinitionBody));
    myReturnValueFromCodeProfider = AtomicNotNullLazyValue.createValue(
      () -> PerlResolveUtil.computeReturnValueFromControlFlow(elementSub)
    );
  }

  @Deprecated
  public PerlLightSubDefinitionElement(@NotNull Delegate delegate,
                                       @NotNull String subName,
                                       @NotNull IStubElementType elementType,
                                       @NotNull PsiElement nameIdentifier,
                                       @Nullable String packageName,
                                       @NotNull List<PerlSubArgument> subArguments,
                                       @Nullable PerlSubAnnotations annotations,
                                       @NotNull AtomicNotNullLazyValue<PerlValue> returnValueFromCodeProvider) {
    this(delegate, subName, elementType, nameIdentifier, packageName, annotations, subArguments, returnValueFromCodeProvider, null);
  }

  @Deprecated
  public PerlLightSubDefinitionElement(@NotNull Delegate delegate,
                                       @NotNull String subName,
                                       @NotNull IStubElementType elementType,
                                       @NotNull PsiElement nameIdentifier,
                                       @Nullable String packageName,
                                       @NotNull List<PerlSubArgument> subArguments,
                                       @Nullable PerlSubAnnotations annotations) {
    this(delegate, subName, elementType, nameIdentifier, packageName, subArguments, annotations, UNKNOWN_PROVIDER);
  }

  @Deprecated
  public PerlLightSubDefinitionElement(@NotNull Delegate delegate,
                                       @NotNull String subName,
                                       @NotNull IStubElementType elementType,
                                       @NotNull PsiElement nameIdentifier,
                                       @Nullable String packageName,
                                       @NotNull List<PerlSubArgument> subArguments,
                                       @Nullable PerlSubAnnotations annotations,
                                       @Nullable PsiPerlBlock subDefinitionBody) {
    this(delegate, subName, elementType, nameIdentifier, packageName, annotations, subArguments, UNKNOWN_PROVIDER, subDefinitionBody);
  }

  public PerlLightSubDefinitionElement(@NotNull Delegate delegate, @NotNull PerlSubDefinitionStub stub) {
    super(delegate, stub.getSubName(), stub.getStubType());
    myPackageName = stub.getNamespaceName();
    mySubArgumentsProvider = AtomicNotNullLazyValue.createValue(stub::getSubArgumentsList);
    myAnnotationsProvider = AtomicNullableLazyValue.createValue(stub::getAnnotations);
    myReturnValueFromCodeProfider = AtomicNotNullLazyValue.createValue(stub::getReturnValueFromCode);
  }

  @Deprecated
  protected void setSubArguments(@NotNull List<PerlSubArgument> subArguments) {
    mySubArgumentsProvider = AtomicNotNullLazyValue.createValue(() -> subArguments);
  }

  @Nullable
  @Override
  public String getNamespaceName() {
    return myPackageName;
  }

  @NotNull
  @Override
  public List<PerlSubArgument> getSubArgumentsList() {
    return mySubArgumentsProvider.getValue();
  }

  @Override
  public String getSubName() {
    return myName;
  }

  @Nullable
  @Override
  public PerlSubAnnotations getAnnotations() {
    return myAnnotationsProvider.getValue();
  }

  @Nullable
  @Override
  public String getExplicitNamespaceName() {
    return myPackageName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof PerlLightSubDefinitionElement)) return false;
    if (!super.equals(o)) return false;

    PerlLightSubDefinitionElement element = (PerlLightSubDefinitionElement)o;

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

  @Nullable
  @Override
  public Icon getIcon(int flags) {
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
    return new PerlItemPresentationSimple(this, getSubName());
  }

  @Override
  @Nullable
  public PsiPerlBlock getSubDefinitionBody() {
    return mySubDefinitionBody;
  }

  @Override
  public String toString() {
    return super.toString() + "@" + getCanonicalName();
  }

  @NotNull
  @Override
  public PerlValue getReturnValueFromCode() {
    return myReturnValueFromCodeProfider.getValue();
  }

  public void setReturnValueFromCode(@NotNull PerlValue returnValueFromCode) {
    myReturnValueFromCodeProfider = AtomicNotNullLazyValue.createValue(() -> returnValueFromCode);
  }

  @Nullable
  @Override
  public PsiElement getControlFlowElement() {
    return getSubDefinitionBody();
  }
}
