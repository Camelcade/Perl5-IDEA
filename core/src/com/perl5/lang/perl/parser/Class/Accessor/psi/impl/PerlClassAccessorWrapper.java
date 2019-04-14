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

package com.perl5.lang.perl.parser.Class.Accessor.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.ElementManipulators;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.util.Function;
import com.perl5.lang.perl.parser.Class.Accessor.psi.stubs.PerlClassAccessorWrapperStub;
import com.perl5.lang.perl.psi.PsiPerlNamespaceContent;
import com.perl5.lang.perl.psi.impl.PerlPolyNamedNestedCallElementBase;
import com.perl5.lang.perl.psi.light.PerlDelegatingLightNamedElement;
import com.perl5.lang.perl.psi.stubs.PerlPolyNamedElementStub;
import com.perl5.lang.perl.psi.stubs.subsdefinitions.PerlSubDefinitionStub;
import com.perl5.lang.perl.psi.utils.PerlResolveUtil;
import com.perl5.lang.perl.psi.utils.PerlSubAnnotations;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.perl5.lang.perl.parser.Class.Accessor.ClassAccessorElementTypes.*;
import static com.perl5.lang.perl.parser.Class.Accessor.psi.impl.PerlClassAccessorMethod.GETTER_COMPUTATION;
import static com.perl5.lang.perl.parser.Class.Accessor.psi.impl.PerlClassAccessorMethod.SETTER_COMPUTATION;

public class PerlClassAccessorWrapper extends PerlPolyNamedNestedCallElementBase<PerlClassAccessorWrapperStub> {

  public PerlClassAccessorWrapper(@NotNull PerlClassAccessorWrapperStub stub,
                                  @NotNull IStubElementType nodeType) {
    super(stub, nodeType);
  }

  public PerlClassAccessorWrapper(@NotNull ASTNode node) {
    super(node);
  }

  @NotNull
  @Override
  public List<PerlDelegatingLightNamedElement> calcLightElementsFromStubs(@NotNull PerlPolyNamedElementStub stub) {
    return stub.getLightNamedElementsStubs().stream()
      .filter(childStub -> childStub.getStubType() == CLASS_ACCESSOR_METHOD)
      .map(childStub -> new PerlClassAccessorMethod(this, (PerlSubDefinitionStub)childStub))
      .collect(Collectors.toList());
  }

  @NotNull
  @Override
  public List<PerlDelegatingLightNamedElement> calcLightElementsFromPsi() {
    String packageName = PerlPackageUtil.getContextNamespaceName(this);
    if (StringUtil.isEmpty(packageName)) {
      return Collections.emptyList();
    }

    List<PsiElement> listElements = getCallArgumentsList();
    if (listElements.isEmpty()) {
      return Collections.emptyList();
    }

    List<PerlDelegatingLightNamedElement> result = new ArrayList<>();
    for (PsiElement listElement : listElements) {
      if (!isAcceptableIdentifierElement(listElement)) {
        continue;
      }
      String baseName = ElementManipulators.getValueText(listElement);
      PerlSubAnnotations subAnnotations = PerlSubAnnotations.computeForLightElement(this, listElement);
      for (Function<String, String> computation : getNamesComputations()) {
        result.add(new PerlClassAccessorMethod(
          this,
          baseName,
          computation,
          CLASS_ACCESSOR_METHOD,
          listElement,
          packageName,
          subAnnotations
        ));
      }
    }
    return result;
  }

  @NotNull
  private List<Function<String, String>> getNamesComputations() {
    if (!isFollowBestPractice()) {
      return Collections.singletonList(PerlClassAccessorMethod.SIMPLE_COMPUTATION);
    }
    IStubElementType elementType = getElementType();
    if (elementType == CLASS_ACCESSOR_WRAPPER_RO) {
      return Collections.singletonList(GETTER_COMPUTATION);
    }
    else if (elementType == CLASS_ACCESSOR_WRAPPER_WO) {
      return Collections.singletonList(SETTER_COMPUTATION);
    }
    return Arrays.asList(GETTER_COMPUTATION, SETTER_COMPUTATION);
  }

  public boolean isFollowBestPractice() {
    PerlClassAccessorWrapperStub stub = getGreenStub();
    if (stub != null) {
      return stub.isFollowBestPractice();
    }
    boolean[] result = new boolean[]{false};

    // fixme we need a smarter treewalkup here, scopes are not necessary here
    PerlResolveUtil.treeWalkUp(this, (element, state) -> {
      if (element instanceof PsiPerlNamespaceContent) {
        return false;
      }

      if (PsiUtilCore.getElementType(element) == CLASS_ACCESSOR_FBP) {
        result[0] = true;
        return false;
      }

      return true;
    });
    return result[0];
  }
}
