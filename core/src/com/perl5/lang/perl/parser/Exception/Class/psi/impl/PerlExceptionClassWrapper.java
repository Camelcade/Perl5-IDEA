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
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.ElementManipulators;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.perl5.lang.perl.parser.Exception.Class.psi.light.PerlLightExceptionClassDefinition;
import com.perl5.lang.perl.psi.PsiPerlAnonArray;
import com.perl5.lang.perl.psi.impl.PerlPolyNamedElementBase;
import com.perl5.lang.perl.psi.light.PerlDelegatingLightNamedElement;
import com.perl5.lang.perl.psi.light.PerlLightMethodDefinitionElement;
import com.perl5.lang.perl.psi.light.PerlLightSubDefinitionElement;
import com.perl5.lang.perl.psi.mro.PerlMroType;
import com.perl5.lang.perl.psi.stubs.PerlPolyNamedElementStub;
import com.perl5.lang.perl.psi.stubs.namespaces.PerlNamespaceDefinitionStub;
import com.perl5.lang.perl.psi.stubs.subsdefinitions.PerlSubDefinitionStub;
import com.perl5.lang.perl.psi.utils.PerlNamespaceAnnotations;
import com.perl5.lang.perl.psi.utils.PerlSubAnnotations;
import com.perl5.lang.perl.psi.utils.PerlSubArgument;
import com.perl5.lang.perl.util.*;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

import static com.perl5.lang.perl.psi.stubs.PerlStubElementTypes.*;

public class PerlExceptionClassWrapper extends PerlPolyNamedElementBase<PerlPolyNamedElementStub> {
  public static final String FIELDS_METHOD_NAME = "Fields";

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
      .map(childStub -> {
        IStubElementType stubType = childStub.getStubType();
        if (stubType == LIGHT_NAMESPACE_DEFINITION) {
          return new PerlLightExceptionClassDefinition(this, (PerlNamespaceDefinitionStub)childStub);
        }
        else if (stubType == LIGHT_METHOD_DEFINITION) {
          return new PerlLightMethodDefinitionElement<>(this, (PerlSubDefinitionStub)childStub);
        }
        else if (stubType == LIGHT_SUB_DEFINITION) {
          return new PerlLightSubDefinitionElement<>(this, (PerlSubDefinitionStub)childStub);
        }
        throw new IllegalArgumentException("Unexpected stub type: " + stubType);
      })
      .collect(Collectors.toList());
  }

  @NotNull
  @Override
  public List<PerlDelegatingLightNamedElement> calcLightElementsFromPsi() {
    PsiElement firstChild = getFirstChild();
    List<PerlDelegatingLightNamedElement> result = new ArrayList<>();
    List<PsiElement> listElements = PerlArrayUtil.collectListElements(firstChild);

    for (int i = 0; i < listElements.size(); i++) {
      processExceptionElement(listElements, i, result);
    }

    return result;
  }

  private void processExceptionElement(@NotNull List<PsiElement> listElements,
                                       int currentIndex,
                                       @NotNull List<PerlDelegatingLightNamedElement> result) {
    PsiElement listElement = listElements.get(currentIndex);
    if (!isAcceptableIdentifierElement(listElement)) {
      return;
    }
    String namespaceName = ElementManipulators.getValueText(listElement);
    if (StringUtil.isEmpty(namespaceName)) {
      return;
    }

    Map<String, PerlHashEntry> exceptionSettings =
      listElements.size() > currentIndex + 1
      ? PerlHashUtil.collectHashMap(listElements.get(currentIndex + 1))
      : Collections.emptyMap();

    // Building fields
    Set<PerlSubArgument> throwArguments = Collections.emptySet();
    PerlHashEntry fieldsEntry = exceptionSettings.get("fields");
    if (fieldsEntry != null && fieldsEntry.isComplete()) {
      PsiElement fieldsContainer = fieldsEntry.getNonNullValueElement();
      if (fieldsContainer instanceof PsiPerlAnonArray) {
        fieldsContainer = ((PsiPerlAnonArray)fieldsContainer).getExpr();
      }
      List<PsiElement> elements = PerlArrayUtil.collectListElements(fieldsContainer);
      if (!elements.isEmpty()) {

        // Fields method
        result.add(new PerlLightMethodDefinitionElement<>(
          this,
          FIELDS_METHOD_NAME,
          LIGHT_METHOD_DEFINITION,
          fieldsEntry.keyElement,
          namespaceName,
          Collections.emptyList(),
          null
        ));

        // fields themselves
        throwArguments = new LinkedHashSet<>();
        for (PsiElement fieldElement : elements) {
          if (isAcceptableIdentifierElement(fieldElement)) {
            String fieldName = PerlScalarUtil.getStringContent(fieldElement);
            if (StringUtil.isNotEmpty(fieldName)) {
              throwArguments.add(PerlSubArgument.mandatoryScalar(fieldName));
              result.add(new PerlLightMethodDefinitionElement<>(
                this,
                fieldName,
                LIGHT_METHOD_DEFINITION,
                fieldElement,
                namespaceName,
                Collections.emptyList(),
                null
              ));
            }
          }
        }
      }
    }

    // making exception class
    PerlHashEntry isaEntry = exceptionSettings.get("isa");
    String parentClass = "Exception::Class::Base";
    if (isaEntry != null && isaEntry.isComplete()) {
      String manualIsa = isaEntry.getValueString();
      if (manualIsa != null) {
        parentClass = manualIsa;
      }
    }

    result.add(new PerlLightExceptionClassDefinition(
      this,
      namespaceName,
      LIGHT_NAMESPACE_DEFINITION,
      listElement,
      PerlMroType.DFS,
      Collections.singletonList(parentClass),
      PerlNamespaceAnnotations.tryToFindAnnotations(listElement, getParent()),
      Collections.emptyList(),
      Collections.emptyList(),
      Collections.emptyMap()
    ));

    // making alias
    PerlHashEntry aliasEntry = exceptionSettings.get("alias");
    if (aliasEntry != null && aliasEntry.isComplete()) {
      if (isAcceptableIdentifierElement(aliasEntry.valueElement)) {
        String aliasName = aliasEntry.getValueString();
        if (StringUtils.isNotEmpty(aliasName)) {
          result.add(new PerlLightSubDefinitionElement<>(
            this,
            aliasName,
            LIGHT_SUB_DEFINITION,
            aliasEntry.getNonNullValueElement(),
            PerlPackageUtil.getContextNamespaceName(this),
            new ArrayList<>(throwArguments),
            PerlSubAnnotations.tryToFindAnnotations(aliasEntry.keyElement, aliasEntry.valueElement)
          ));
        }
      }
    }
  }
}
