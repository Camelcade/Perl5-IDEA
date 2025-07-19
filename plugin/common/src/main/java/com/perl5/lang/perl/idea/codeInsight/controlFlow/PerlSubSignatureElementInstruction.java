/*
 * Copyright 2015-2025 Alexandr Evstigneev
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
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.*;
import com.perl5.lang.perl.psi.PerlVariable;
import com.perl5.lang.perl.psi.PerlVariableDeclarationElement;
import com.perl5.lang.perl.util.PerlContextUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class PerlSubSignatureElementInstruction extends PerlMutationInstruction {
  private final int myIndex;

  private final @Nullable PsiElement myDefaultValue;

  public PerlSubSignatureElementInstruction(@NotNull ControlFlowBuilder builder,
                                            @NotNull PerlVariable element,
                                            int index,
                                            @Nullable PsiElement defaultValue) {
    super(builder, element);
    myIndex = index;
    myDefaultValue = defaultValue;
  }

  @Override
  public @Nullable PsiElement getLeftSide() {
    return getElement();
  }

  @Override
  public @NotNull PerlValue createValue() {
    var parentElement = Objects.requireNonNull(myElement).getParent();
    if (parentElement instanceof PerlVariableDeclarationElement variableDeclarationElement) {
      var annotations = variableDeclarationElement.getVariableAnnotations();
      if (annotations != null) {
        var annotatedValue = annotations.getAnnotatedValue();
        if (!annotatedValue.isUnknown()) {
          return annotatedValue;
        }
      }
    }

    PerlValue mainValue = PerlContextUtil.isScalar(myElement) ?
                          PerlArrayElementValue.create(PerlValues.ARGUMENTS_VALUE, PerlScalarValue.create(myIndex)) :
                          PerlSublistValue.create(PerlValues.ARGUMENTS_VALUE, myIndex, 0);
    PerlValue defaultValue = PerlValuesManager.from(myDefaultValue);
    return defaultValue.isUnknown() ? mainValue : PerlDefaultArgumentValue.create(mainValue, defaultValue, myIndex);
  }

  @Override
  public @NotNull String getElementPresentation() {
    return "Argument " + myIndex + " " + myElement;
  }
}
