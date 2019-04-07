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
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.util.PairFunction;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValue;
import com.perl5.lang.perl.idea.presentations.PerlItemPresentationSimple;
import com.perl5.lang.perl.psi.PerlPolyNamedElement;
import com.perl5.lang.perl.psi.PerlSubDefinitionElement;
import com.perl5.lang.perl.psi.PerlVisitor;
import com.perl5.lang.perl.psi.PsiPerlBlock;
import com.perl5.lang.perl.psi.stubs.subsdefinitions.PerlSubDefinitionStub;
import com.perl5.lang.perl.psi.utils.PerlSubAnnotations;
import com.perl5.lang.perl.psi.utils.PerlSubArgument;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.List;

public class PerlLightSubDefinitionElement<Delegate extends PerlPolyNamedElement> extends PerlDelegatingLightNamedElement<Delegate>
  implements PerlSubDefinitionElement {
  @Nullable
  private final String myPackageName;
  @Nullable
  private final PerlSubAnnotations myAnnotations;
  @NotNull
  private List<PerlSubArgument> mySubArguments;
  @NotNull // fixme should we make a static TrioFunction to save memory? or even inherit?
  private PairFunction<String, List<PerlValue>, PerlValue> myReturnValueComputation = (context, arguments) ->
    PerlSubDefinitionElement.super.getReturnValue(context, arguments);

  // fixme should we actualize this on fly, like identifier?
  @Nullable
  private PsiPerlBlock mySubDefinitionBody;

  public PerlLightSubDefinitionElement(@NotNull Delegate delegate,
                                       @NotNull String subName,
                                       @NotNull IStubElementType elementType,
                                       @NotNull PsiElement nameIdentifier,
                                       @Nullable String packageName,
                                       @NotNull List<PerlSubArgument> subArguments,
                                       @Nullable PerlSubAnnotations annotations) {
    super(delegate, subName, elementType, nameIdentifier);
    myPackageName = packageName;
    mySubArguments = subArguments;
    myAnnotations = annotations;
  }

  public PerlLightSubDefinitionElement(@NotNull Delegate delegate,
                                       @NotNull String subName,
                                       @NotNull IStubElementType elementType,
                                       @NotNull PsiElement nameIdentifier,
                                       @Nullable String packageName,
                                       @NotNull List<PerlSubArgument> subArguments,
                                       @Nullable PerlSubAnnotations annotations,
                                       @Nullable PsiPerlBlock subDefinitionBody) {
    this(delegate, subName, elementType, nameIdentifier, packageName, subArguments, annotations);
    mySubDefinitionBody = subDefinitionBody;
  }

  public PerlLightSubDefinitionElement(@NotNull Delegate delegate, @NotNull PerlSubDefinitionStub stub) {
    super(delegate, stub.getSubName(), stub.getStubType());
    myPackageName = stub.getNamespaceName();
    mySubArguments = stub.getSubArgumentsList();
    myAnnotations = stub.getAnnotations();
  }

  protected void setSubArguments(@NotNull List<PerlSubArgument> subArguments) {
    mySubArguments = subArguments;
  }

  @Nullable
  @Override
  public String getNamespaceName() {
    return myPackageName;
  }

  @NotNull
  @Override
  public List<PerlSubArgument> getSubArgumentsList() {
    return mySubArguments;
  }

  @Override
  public String getSubName() {
    return myName;
  }

  @Nullable
  @Override
  public PerlSubAnnotations getAnnotations() {
    return myAnnotations;
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
    if (!mySubArguments.equals(element.mySubArguments)) return false;
    return getAnnotations() != null ? getAnnotations().equals(element.getAnnotations()) : element.getAnnotations() == null;
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (getNamespaceName() != null ? getNamespaceName().hashCode() : 0);
    result = 31 * result + mySubArguments.hashCode();
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
  public PerlValue getReturnValue(@Nullable String contextPackage, @NotNull List<PerlValue> arguments) {
    return myReturnValueComputation.fun(contextPackage, arguments);
  }

  @NotNull
  public PairFunction<String, List<PerlValue>, PerlValue> getReturnValueComputation() {
    return myReturnValueComputation;
  }

  public void setReturnValueComputation(@NotNull PairFunction<String, List<PerlValue>, PerlValue> returnValueComputation) {
    myReturnValueComputation = returnValueComputation;
  }

  @Nullable
  @Override
  public PsiElement getControlFlowElement() {
    return getSubDefinitionBody();
  }
}
