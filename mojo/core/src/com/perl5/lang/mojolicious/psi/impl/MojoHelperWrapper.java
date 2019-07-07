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

package com.perl5.lang.mojolicious.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.ElementManipulators;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.perl5.lang.mojolicious.psi.stubs.MojoHelperWrapperStub;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlScalarValue;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValue;
import com.perl5.lang.perl.psi.PerlSelfHinter;
import com.perl5.lang.perl.psi.PerlSubExpr;
import com.perl5.lang.perl.psi.impl.PerlPolyNamedNestedCallElementBase;
import com.perl5.lang.perl.psi.light.PerlDelegatingLightNamedElement;
import com.perl5.lang.perl.psi.stubs.subsdefinitions.PerlSubDefinitionStub;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.perl5.lang.mojolicious.psi.impl.MojoliciousFile.MOJO_CONTROLLER_NS;
import static com.perl5.lang.perl.psi.stubs.PerlStubElementTypes.LIGHT_METHOD_DEFINITION;

public class MojoHelperWrapper extends PerlPolyNamedNestedCallElementBase<MojoHelperWrapperStub> implements PerlSelfHinter {

  public MojoHelperWrapper(@NotNull MojoHelperWrapperStub stub,
                           @NotNull IStubElementType nodeType) {
    super(stub, nodeType);
  }

  public MojoHelperWrapper(@NotNull ASTNode node) {
    super(node);
  }

  @NotNull
  @Override
  protected List<PerlDelegatingLightNamedElement> computeLightElementsFromStubs(@NotNull MojoHelperWrapperStub stub) {
    return stub.getLightNamedElementsStubs().stream()
      .filter(childStub -> childStub.getStubType() == LIGHT_METHOD_DEFINITION)
      .map(childStub -> new MojoHelperDefinition(this, (PerlSubDefinitionStub)childStub))
      .collect(Collectors.toList());
  }

  @NotNull
  @Override
  public List<PerlDelegatingLightNamedElement> computeLightElementsFromPsi() {
    List<PsiElement> listElements = getCallArgumentsList();
    if (listElements.size() != 2) {
      return Collections.emptyList();
    }

    PsiElement identifierElement = listElements.get(0);
    if (!isAcceptableIdentifierElement(identifierElement)) {
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
      this, subName, LIGHT_METHOD_DEFINITION, identifierElement, MOJO_CONTROLLER_NS, (PerlSubExpr)bodyElement));
  }

  @NotNull
  @Override
  public PerlValue getSelfType() {
    return PerlScalarValue.create(MOJO_CONTROLLER_NS);
  }
}
