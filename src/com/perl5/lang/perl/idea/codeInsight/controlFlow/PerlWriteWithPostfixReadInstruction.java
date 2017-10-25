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

package com.perl5.lang.perl.idea.codeInsight.controlFlow;

import com.intellij.codeInsight.controlflow.ControlFlowBuilder;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents $var++, bless argument
 */
public class PerlWriteWithPostfixReadInstruction extends PerlWriteInstruction {
  public PerlWriteWithPostfixReadInstruction(@NotNull ControlFlowBuilder builder,
                                             @NotNull PsiElement element,
                                             @Nullable PsiElement newValueElement, int newValueIndex) {
    super(builder, element, newValueElement, newValueIndex);
  }

  @Override
  public String toString() {
    return super.toString() + " & READ FROM";
  }

  @Override
  public boolean isRead() {
    return true;
  }
}
