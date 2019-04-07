/*
 * Copyright 2015-2018 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.codeInsight.controlFlow;

import com.intellij.codeInsight.controlflow.ControlFlowBuilder;
import com.intellij.codeInsight.controlflow.impl.InstructionImpl;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.perl.psi.PerlAssignExpression.PerlAssignValueDescriptor;
import org.jetbrains.annotations.NotNull;

public class PerlAssignInstruction extends InstructionImpl {
  @NotNull
  private final PerlAssignValueDescriptor myRightSide;
  @NotNull
  private final PsiElement myOperation;

  public PerlAssignInstruction(@NotNull ControlFlowBuilder builder,
                               @NotNull PsiElement leftSide,
                               @NotNull PerlAssignValueDescriptor rightSide,
                               @NotNull PsiElement operationElement) {
    super(builder, leftSide);
    myRightSide = rightSide;
    myOperation = operationElement;
  }

  @NotNull
  public PsiElement getLeftSide() {
    // you can't create this instruction with nullable element
    //noinspection ConstantConditions
    return myElement;
  }

  @NotNull
  public PerlAssignValueDescriptor getRightSide() {
    return myRightSide;
  }

  @NotNull
  public IElementType getOperationType() {
    return PsiUtilCore.getElementType(myOperation);
  }

  @NotNull
  public PsiElement getOperation() {
    return myOperation;
  }

  @NotNull
  @Override
  public String getElementPresentation() {
    return "assign " + myElement + " " + getOperationType() + " " + myRightSide;
  }
}
