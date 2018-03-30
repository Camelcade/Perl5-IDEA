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
import com.intellij.codeInsight.controlflow.impl.ConditionalInstructionImpl;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;

import java.util.Objects;

/**
 * Allows to use partial condition expression, e.g.
 * $a && $b && $c => $a && $b
 */
public class PartialConditionalInstructionImpl extends ConditionalInstructionImpl {
  private final PsiElement myLastConditionElement;

  public PartialConditionalInstructionImpl(ControlFlowBuilder builder,
                                           @Nullable PsiElement element,
                                           @Nullable PsiElement condition,
                                           @NotNull PsiElement lastConditionElement,
                                           boolean result) {
    super(builder, element, condition, result);
    myLastConditionElement = lastConditionElement;
  }

  @NotNull
  @TestOnly
  public String getConditionText() {
    return Objects.requireNonNull(getCondition()).getText()
      .substring(0, myLastConditionElement.getStartOffsetInParent() + myLastConditionElement.getTextLength());
  }

  @NotNull
  @Override
  public String toString() {
    return super.toString() + "; Partial condition: " + getConditionText();
  }
}
