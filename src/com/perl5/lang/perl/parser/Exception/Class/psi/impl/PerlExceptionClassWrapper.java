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

package com.perl5.lang.perl.parser.Exception.Class.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.ElementManipulators;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.stubs.IStubElementType;
import com.perl5.lang.perl.parser.Exception.Class.psi.light.PerlLightExceptionClassDefinition;
import com.perl5.lang.perl.psi.PerlString;
import com.perl5.lang.perl.psi.PerlStringContentElement;
import com.perl5.lang.perl.psi.PerlVisitor;
import com.perl5.lang.perl.psi.impl.PerlPolyNamedElementBase;
import com.perl5.lang.perl.psi.light.PerlDelegatingLightNamedElement;
import com.perl5.lang.perl.psi.mro.PerlMroType;
import com.perl5.lang.perl.psi.stubs.PerlPolyNamedElementStub;
import com.perl5.lang.perl.psi.stubs.namespaces.PerlNamespaceDefinitionStub;
import com.perl5.lang.perl.psi.utils.PerlNamespaceAnnotations;
import com.perl5.lang.perl.util.PerlArrayUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.perl5.lang.perl.psi.stubs.PerlStubElementTypes.LIGHT_NAMESPACE_DEFINITION;

public class PerlExceptionClassWrapper extends PerlPolyNamedElementBase {

  public PerlExceptionClassWrapper(@NotNull PerlPolyNamedElementStub stub,
                                   @NotNull IStubElementType nodeType) {
    super(stub, nodeType);
  }

  public PerlExceptionClassWrapper(@NotNull ASTNode node) {
    super(node);
  }

  @NotNull
  @Override
  public List<PerlDelegatingLightNamedElement> calcLightElementsFromStubs(@NotNull PerlPolyNamedElementStub stub) {
    return stub.getLightNamedElementsStubs().stream()
      .filter(childStub -> childStub.getStubType() == LIGHT_NAMESPACE_DEFINITION)
      .map(childStub -> new PerlLightExceptionClassDefinition(this, (PerlNamespaceDefinitionStub)childStub))
      .collect(Collectors.toList());
  }

  @NotNull
  @Override
  public List<PerlDelegatingLightNamedElement> calcLightElementsFromPsi() {
    PsiElement firstChild = getFirstChild();
    List<PerlDelegatingLightNamedElement> result = new ArrayList<>();
    for (PsiElement listElement : PerlArrayUtil.collectListElements(firstChild, null)) {
      if (listElement instanceof PerlString || listElement instanceof PerlStringContentElement) {
        result.add(new PerlLightExceptionClassDefinition(
          this,
          ElementManipulators.getValueText(listElement),
          LIGHT_NAMESPACE_DEFINITION,
          listElement,
          PerlMroType.DFS,
          Collections.singletonList("Exception::Class::Base"), // fixme NYI
          PerlNamespaceAnnotations.tryToFindAnnotations(listElement, getParent()),
          Collections.emptyList(),
          Collections.emptyList(),
          Collections.emptyMap()
        ));
      }
    }

    return result;
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PerlVisitor) {
      ((PerlVisitor)visitor).visitExceptionClassWrapper(this);
    }
    else {
      super.accept(visitor);
    }
  }
}
