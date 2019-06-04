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
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlPushValue;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValue;
import com.perl5.lang.perl.psi.PsiPerlArrayPushExpr;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PerlPushInstruction extends PerlMutationInstruction {
  public PerlPushInstruction(@NotNull ControlFlowBuilder builder,
                             @NotNull PsiPerlArrayPushExpr pushExpr) {
    super(builder, pushExpr);
  }

  @Nullable
  @Override
  public PsiElement getLeftSide() {
    assert myElement != null;
    return ((PsiPerlArrayPushExpr)myElement).getTarget();
  }

  @NotNull
  @Override
  public PerlValue createValue() {
    assert myElement != null;
    return PerlPushValue.create((PsiPerlArrayPushExpr)myElement);
  }

  @NotNull
  @Override
  public String getElementPresentation() {
    return "push " + myElement;
  }
}
