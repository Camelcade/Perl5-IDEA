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
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValue;
import com.perl5.lang.perl.psi.PsiPerlArrayPopExpr;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValues.UNKNOWN_VALUE;

public class PerlPopInstruction extends PerlMutationInstruction {
  public PerlPopInstruction(@NotNull ControlFlowBuilder builder,
                            @Nullable PsiPerlArrayPopExpr element) {
    super(builder, element);
  }

  @Nullable
  @Override
  public PsiElement getLeftSide() {
    assert myElement instanceof PsiPerlArrayPopExpr;
    return ((PsiPerlArrayPopExpr)myElement).getTarget();
  }

  @NotNull
  @Override
  public PerlValue createValue() {
    return UNKNOWN_VALUE;
  }

  @NotNull
  @Override
  public String getElementPresentation() {
    return "pop " + myElement;
  }
}
