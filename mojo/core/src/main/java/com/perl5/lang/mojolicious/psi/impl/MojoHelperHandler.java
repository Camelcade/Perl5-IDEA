/*
 * Copyright 2015-2020 Alexandr Evstigneev
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

package com.perl5.lang.mojolicious.psi.impl;

import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.ElementManipulators;
import com.intellij.psi.PsiElement;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlScalarValue;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValue;
import com.perl5.lang.perl.psi.PerlSubCallHandlerWithEmptyData;
import com.perl5.lang.perl.psi.PerlSubExpr;
import com.perl5.lang.perl.psi.impl.PerlSubCallElement;
import com.perl5.lang.perl.psi.light.PerlDelegatingLightNamedElement;
import com.perl5.lang.perl.psi.stubs.calls.PerlSubCallElementStub;
import com.perl5.lang.perl.psi.stubs.subsdefinitions.PerlSubDefinitionStub;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.perl5.lang.mojolicious.psi.impl.MojoliciousFile.MOJO_CONTROLLER_NS;
import static com.perl5.lang.perl.psi.stubs.PerlStubElementTypes.LIGHT_METHOD_DEFINITION;

public class MojoHelperHandler extends PerlSubCallHandlerWithEmptyData {
  @Override
  public @NotNull List<? extends PerlDelegatingLightNamedElement<?>> computeLightElementsFromPsi(@NotNull PerlSubCallElement psiElement) {
    List<PsiElement> listElements = psiElement.getCallArgumentsList();
    if (listElements.size() != 2) {
      return Collections.emptyList();
    }

    PsiElement identifierElement = listElements.get(0);
    if (!psiElement.isAcceptableIdentifierElement(identifierElement)) {
      return Collections.emptyList();
    }

    PsiElement bodyElement = listElements.get(1);
    if (!(bodyElement instanceof PerlSubExpr)) {
      return Collections.emptyList();
    }

    String subName = ElementManipulators.getValueText(identifierElement);
    if (StringUtil.isEmpty(subName)) {
      return Collections.emptyList();
    }

    return Collections.singletonList(new MojoHelperDefinition(
      psiElement, subName, LIGHT_METHOD_DEFINITION, identifierElement, MOJO_CONTROLLER_NS, (PerlSubExpr)bodyElement));
  }

  @Override
  public @NotNull List<? extends PerlDelegatingLightNamedElement<?>> computeLightElementsFromStubs(@NotNull PerlSubCallElement psiElement,
                                                                                                   @NotNull PerlSubCallElementStub stubElement) {
    return stubElement.getLightNamedElementsStubs().stream()
      .filter(childStub -> childStub.getStubType() == LIGHT_METHOD_DEFINITION)
      .map(childStub -> new MojoHelperDefinition(psiElement, (PerlSubDefinitionStub)childStub))
      .collect(Collectors.toList());
  }

  @Override
  public @NotNull PerlValue getSelfType() {
    return PerlScalarValue.create(MOJO_CONTROLLER_NS);
  }
}
