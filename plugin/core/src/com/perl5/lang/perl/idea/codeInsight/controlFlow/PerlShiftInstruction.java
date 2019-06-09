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

package com.perl5.lang.perl.idea.codeInsight.controlFlow;

import com.intellij.codeInsight.controlflow.ControlFlowBuilder;
import com.intellij.psi.PsiElement;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlSublistValue;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValue;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValuesManager;
import com.perl5.lang.perl.psi.PsiPerlArrayShiftExpr;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PerlShiftInstruction extends PerlMutationInstruction {
  public PerlShiftInstruction(@NotNull ControlFlowBuilder builder,
                              @NotNull PsiPerlArrayShiftExpr element) {
    super(builder, element);
  }

  @Nullable
  @Override
  public PsiElement getLeftSide() {
    assert myElement instanceof PsiPerlArrayShiftExpr;
    return ((PsiPerlArrayShiftExpr)myElement).getTarget();
  }

  @NotNull
  @Override
  public PerlValue createValue() {
    assert myElement != null;
    return PerlSublistValue.createShiftValue(PerlValuesManager.createShiftPopArgumentValue((PsiPerlArrayShiftExpr)myElement));
  }

  @NotNull
  @Override
  public String getElementPresentation() {
    return "shift " + myElement;
  }
}

