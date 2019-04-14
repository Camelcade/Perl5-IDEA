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

import com.intellij.codeInsight.controlflow.ControlFlowUtil;
import com.intellij.codeInsight.controlflow.Instruction;
import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.stubs.IStubElementType;
import com.perl5.lang.perl.idea.codeInsight.controlFlow.PerlControlFlowBuilder;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlOneOfValue;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValue;
import com.perl5.lang.perl.idea.presentations.PerlItemPresentationSimple;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.properties.PerlBlockOwner;
import com.perl5.lang.perl.psi.properties.PerlLexicalScope;
import com.perl5.lang.perl.psi.stubs.subsdefinitions.PerlSubDefinitionStub;
import com.perl5.lang.perl.psi.utils.PerlSubArgument;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hurricup on 11.11.2015.
 */
public abstract class PerlSubDefinitionBase extends PerlSubBase<PerlSubDefinitionStub> implements PerlSubDefinitionElement,
                                                                                                  PerlLexicalScope,
                                                                                                  PerlElementTypes {
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

  @NotNull
  @Override
  public List<PerlSubArgument> getSubArgumentsList() {
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

  @NotNull
  @Override
  public PerlValue getReturnValueFromCode() {
    PerlValue returnValue = PerlSubDefinitionElement.super.getReturnValueFromCode();
    if (!returnValue.isEmpty()) {
      return returnValue;
    }
    PerlSubDefinitionStub greenStub = getGreenStub();
    return greenStub != null ? greenStub.getReturnValueFromCode() : computeReturnValueFromCode();
  }

  @NotNull
  private PerlValue computeReturnValueFromCode() {
    PerlOneOfValue.Builder valueBuilder = PerlOneOfValue.builder();
    Instruction[] instructions = PerlControlFlowBuilder.getFor(this).getInstructions();
    Instruction exitInstruction = instructions[instructions.length - 1];
    PerlControlFlowBuilder.iteratePrev(instructions, it -> {
      if (it == exitInstruction || it.num() == 0) {
        return ControlFlowUtil.Operation.NEXT;
      }
      PsiElement element = it.getElement();
      if (element == null) {
        return ControlFlowUtil.Operation.NEXT;
      }
      valueBuilder.addVariant(PerlValue.from(element));
      return ControlFlowUtil.Operation.CONTINUE;
    });

    return valueBuilder.build();
  }

  @Override
  public ItemPresentation getPresentation() {
    return new PerlItemPresentationSimple(this, getPresentableName());
  }


  /**
   * Returns list of arguments defined in signature
   *
   * @return list of arguments or null if there is no signature
   */
  @Nullable
  private List<PerlSubArgument> getPerlSubArgumentsFromSignature() {
    List<PerlSubArgument> arguments = null;
    PsiElement signatureContainer = getSignatureContainer();

    if (signatureContainer != null) {
      arguments = new ArrayList<>();
      //noinspection unchecked

      PsiElement signatureElement = signatureContainer.getFirstChild();

      while (signatureElement != null) {
        processSignatureElement(signatureElement, arguments);
        signatureElement = signatureElement.getNextSibling();
      }
    }

    return arguments;
  }

  protected boolean processSignatureElement(PsiElement signatureElement, List<PerlSubArgument> arguments) {
    if (signatureElement instanceof PerlVariableDeclarationElement) {
      PerlVariable variable = ((PerlVariableDeclarationElement)signatureElement).getVariable();
      arguments.add(PerlSubArgument.mandatory(variable.getActualType(), variable.getName()));
      return true;
    }
    return false;
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PerlVisitor) {
      ((PerlVisitor)visitor).visitPerlSubDefinitionElement(this);
    }
    else {
      super.accept(visitor);
    }
  }

  @Nullable
  public PsiPerlBlock getSubDefinitionBody() {
    return PerlBlockOwner.findBlock(this);
  }
}
