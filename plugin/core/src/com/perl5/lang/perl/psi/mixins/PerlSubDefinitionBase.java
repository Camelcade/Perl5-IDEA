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

package com.perl5.lang.perl.psi.mixins;

import com.intellij.codeInsight.controlflow.Instruction;
import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.util.ClearableLazyValue;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.stubs.IStubElementType;
import com.perl5.lang.perl.idea.codeInsight.controlFlow.PerlControlFlowBuilder;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValue;
import com.perl5.lang.perl.idea.presentations.PerlItemPresentationSimpleDynamicLocation;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.properties.PerlLexicalScope;
import com.perl5.lang.perl.psi.stubs.subsdefinitions.PerlSubDefinitionStub;
import com.perl5.lang.perl.psi.utils.PerlResolveUtil;
import com.perl5.lang.perl.psi.utils.PerlSubArgument;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;


public abstract class PerlSubDefinitionBase extends PerlSubBase<PerlSubDefinitionStub> implements PerlSubDefinitionElement,
                                                                                                  PerlLexicalScope,
                                                                                                  PerlElementTypes,
                                                                                                  PerlControlFlowOwner {
  private final ClearableLazyValue<Instruction[]> myControlFlow = PerlControlFlowBuilder.createLazy(this);
  private final ClearableLazyValue<PerlValue> myReturnValueFromCode = ClearableLazyValue.create(
    () -> PerlResolveUtil.computeReturnValueFromControlFlow(this));

  public PerlSubDefinitionBase(@NotNull ASTNode node) {
    super(node);
  }

  public PerlSubDefinitionBase(@NotNull PerlSubDefinitionStub stub, @NotNull IStubElementType nodeType) {
    super(stub, nodeType);
  }

  protected abstract PsiElement getSignatureContainer();

  @Override
  public boolean isMethod() {
    if (super.isMethod()) {
      return true;
    }

    List<PerlSubArgument> arguments = getSubArgumentsList();
    return !arguments.isEmpty() && arguments.get(0).isSelf(getProject());
  }

  @Override
  public @NotNull List<PerlSubArgument> getSubArgumentsList() {
    PerlSubDefinitionStub stub = getGreenStub();
    if (stub != null) {
      return new ArrayList<>(stub.getSubArgumentsList());
    }

    List<PerlSubArgument> arguments = getPerlSubArgumentsFromSignature();

    if (arguments == null) {
      arguments = getPerlSubArgumentsFromBody();
    }

    return arguments;
  }

  @Override
  public @NotNull PerlValue getReturnValueFromCode() {
    PerlValue returnValue = PerlSubDefinitionElement.super.getReturnValueFromCode();
    if (!returnValue.isUnknown()) {
      return returnValue;
    }
    PerlSubDefinitionStub greenStub = getGreenStub();
    return greenStub != null ?
           greenStub.getReturnValueFromCode() : myReturnValueFromCode.getValue();
  }

  @Override
  public ItemPresentation getPresentation() {
    return new PerlItemPresentationSimpleDynamicLocation(this, getPresentableName());
  }

  /**
   * Returns list of arguments defined in signature
   *
   * @return list of arguments or null if there is no signature
   */
  private @Nullable List<PerlSubArgument> getPerlSubArgumentsFromSignature() {
    List<PerlSubArgument> arguments = null;
    PsiElement signatureContainer = getSignatureContainer();

    if (signatureContainer != null) {
      arguments = new ArrayList<>();
      //noinspection unchecked

      PsiElement signatureElement = signatureContainer.getFirstChild();

      while (signatureElement != null) {
        if (signatureElement instanceof PerlSignatureElement) {
          processSignatureElement(((PerlSignatureElement)signatureElement).getDeclarationElement(), arguments);
        }
        else {
          processSignatureElement(signatureElement, arguments);
        }
        signatureElement = signatureElement.getNextSibling();
      }
    }

    return arguments;
  }

  protected boolean processSignatureElement(PsiElement signatureElement, List<PerlSubArgument> arguments) {
    if (signatureElement instanceof PerlVariableDeclarationElement) {
      PerlVariable variable = ((PerlVariableDeclarationElement)signatureElement).getVariable();
      PerlSubArgument newArgument = PerlSubArgument.mandatory(variable.getActualType(), variable.getName());
      newArgument.setOptional(signatureElement.getNextSibling() != null);
      arguments.add(newArgument);
      return true;
    }
    return false;
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
  public @Nullable PsiPerlBlock getSubDefinitionBody() {
    return getBlockSmart();
  }

  @Override
  public @NotNull Instruction[] getControlFlow() {
    return myControlFlow.getValue();
  }

  @Override
  public void subtreeChanged() {
    myControlFlow.drop();
    myReturnValueFromCode.drop();
  }
}