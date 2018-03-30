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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PerlAssignInstuction extends InstructionImpl {
  @NotNull
  private final PsiElement myLeftSide;
  @NotNull
  private final PsiElement myRightSide;
  @NotNull
  private final PsiElement myOperation;

  public PerlAssignInstuction(@NotNull ControlFlowBuilder builder,
                              @Nullable PsiElement element,
                              @NotNull PsiElement leftSide,
                              @NotNull PsiElement rightSide,
                              @NotNull PsiElement operationElement) {
    super(builder, element);
    myLeftSide = leftSide;
    myRightSide = rightSide;
    myOperation = operationElement;
  }

  @NotNull
  public PsiElement getLeftSide() {
    return myLeftSide;
  }

  @NotNull
  public PsiElement getRightSide() {
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
    return "assign " + myLeftSide + " " + getOperationType() + " " + myRightSide;
  }
}
