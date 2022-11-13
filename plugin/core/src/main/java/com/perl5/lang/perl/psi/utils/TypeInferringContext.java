/*
 * Copyright 2015-2022 Alexandr Evstigneev
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

package com.perl5.lang.perl.psi.utils;

import com.intellij.psi.PsiElement;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlDuckValue;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlOneOfValue;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValue;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValueWithFallback;
import com.perl5.lang.perl.psi.PerlVariable;
import com.perl5.lang.perl.psi.PerlVariableDeclarationElement;
import com.perl5.lang.perl.psi.impl.PerlBuiltInVariable;
import com.perl5.lang.perl.psi.impl.PerlImplicitVariableDeclaration;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlDuckValue.isDuckTypingEnabled;
import static com.perl5.lang.perl.psi.utils.PerlResolveUtil.computeStopElement;

class TypeInferringContext {
  private final @NotNull PsiElement myContextElement;
  private final @Nullable String myNamespaceName;
  private final @NotNull String myVariableName;
  private final @NotNull PerlVariableType myActualType;
  private final @Nullable PerlVariableDeclarationElement myLexicalDeclaration;
  private final @Nullable PsiElement myStopElement;
  private final @NotNull PerlOneOfValue.Builder myValueBuilder;
  private final @NotNull PerlDuckValue.Builder myDuckValueBuilder;

  private TypeInferringContext(@NotNull PsiElement contextElement,
                               @Nullable String namespaceName,
                               @NotNull String variableName,
                               @NotNull PerlVariableType actualType,
                               @Nullable PerlVariableDeclarationElement lexicalDeclaration,
                               @Nullable PsiElement stopElement,
                               @NotNull PerlOneOfValue.Builder valueBuilder,
                               @NotNull PerlDuckValue.Builder duckValueBuilder) {
    myContextElement = contextElement;
    myNamespaceName = namespaceName;
    myVariableName = variableName;
    myActualType = actualType;
    myLexicalDeclaration = lexicalDeclaration;
    myStopElement = stopElement;
    myValueBuilder = valueBuilder;
    myDuckValueBuilder = duckValueBuilder;
  }

  public TypeInferringContext(@NotNull PerlBuiltInVariable variable, @NotNull PsiElement contextElement) {
    this(contextElement,
         null,
         variable.getVariableName(),
         variable.getActualType(),
         variable,
         computeStopElement(variable, contextElement),
         PerlOneOfValue.builder(),
         PerlDuckValue.builder()
    );
  }

  public TypeInferringContext(@NotNull PerlVariable variable) {
    myContextElement = variable;
    myLexicalDeclaration = PerlResolveUtil.getLexicalDeclaration(variable);

    PsiElement stopElement = myLexicalDeclaration;
    if (myLexicalDeclaration instanceof PerlImplicitVariableDeclaration) {
      stopElement = myLexicalDeclaration.getContext();
    }
    myStopElement = stopElement;

    myVariableName = variable.getName();
    myNamespaceName = variable.getExplicitNamespaceName();
    myActualType = variable.getActualType();
    myValueBuilder = PerlOneOfValue.builder();
    myDuckValueBuilder = PerlDuckValue.builder();
  }

  public @NotNull PsiElement getContextElement() {
    return myContextElement;
  }

  public @Nullable String getNamespaceName() {
    return myNamespaceName;
  }

  public @NotNull String getVariableName() {
    return myVariableName;
  }

  public @NotNull PerlVariableType getActualType() {
    return myActualType;
  }

  @Contract("null->false")
  public boolean isMyVariable(@Nullable PsiElement psiElement) {
    return psiElement instanceof PerlVariable perlVariable &&
           perlVariable.getActualType() == myActualType &&
           Objects.equals(perlVariable.getName(), myVariableName);
  }

  public @Nullable PerlVariableDeclarationElement getLexicalDeclaration() {
    return myLexicalDeclaration;
  }

  public @Nullable PsiElement getStopElement() {
    return myStopElement;
  }

  public void addVariant(@NotNull PerlValue perlValue) {
    myValueBuilder.addVariant(perlValue);
  }

  public void addDuckCall(@NotNull String callableName) {
    myDuckValueBuilder.addElement(callableName);
  }

  public @NotNull PerlValue buildValue() {
    return isDuckTypingEnabled() ? PerlValueWithFallback.create(myValueBuilder.build(), myDuckValueBuilder.build())
                                 : myValueBuilder.build();
  }

  @NotNull TypeInferringContext withContext(@NotNull PsiElement newContextElement) {
    return new TypeInferringContext(newContextElement, myNamespaceName, myVariableName, myActualType, myLexicalDeclaration, myStopElement,
                                    myValueBuilder, myDuckValueBuilder);
  }
}
