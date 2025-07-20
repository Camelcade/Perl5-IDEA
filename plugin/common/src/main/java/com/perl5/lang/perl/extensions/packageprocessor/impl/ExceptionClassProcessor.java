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

package com.perl5.lang.perl.extensions.packageprocessor.impl;

import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.ElementManipulators;
import com.intellij.psi.PsiElement;
import com.perl5.lang.perl.extensions.packageprocessor.PerlPackageProcessorBase;
import com.perl5.lang.perl.parser.Exception.Class.psi.light.PerlLightExceptionClassDefinition;
import com.perl5.lang.perl.psi.PsiPerlAnonArray;
import com.perl5.lang.perl.psi.PsiPerlExpr;
import com.perl5.lang.perl.psi.impl.PerlUseStatementElement;
import com.perl5.lang.perl.psi.light.PerlDelegatingLightNamedElement;
import com.perl5.lang.perl.psi.light.PerlLightMethodDefinitionElement;
import com.perl5.lang.perl.psi.light.PerlLightSubDefinitionElement;
import com.perl5.lang.perl.psi.mro.PerlMroType;
import com.perl5.lang.perl.psi.stubs.imports.PerlUseStatementStub;
import com.perl5.lang.perl.psi.stubs.namespaces.PerlNamespaceDefinitionStub;
import com.perl5.lang.perl.psi.stubs.subsdefinitions.PerlSubDefinitionStub;
import com.perl5.lang.perl.psi.utils.PerlNamespaceAnnotations;
import com.perl5.lang.perl.psi.utils.PerlSubAnnotations;
import com.perl5.lang.perl.psi.utils.PerlSubArgument;
import com.perl5.lang.perl.util.*;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValues.UNKNOWN_VALUE_PROVIDER;
import static com.perl5.lang.perl.psi.stubs.PerlStubElementTypes.*;

public class ExceptionClassProcessor extends PerlPackageProcessorBase {
  public static final String FIELDS_METHOD_NAME = "Fields";

  @Override
  public @NotNull List<PerlDelegatingLightNamedElement<?>> computeLightElementsFromPsi(@NotNull PerlUseStatementElement useStatementElement) {
    PsiPerlExpr expr = useStatementElement.getExpr();
    if (expr == null) {
      return Collections.emptyList();
    }
    List<PerlDelegatingLightNamedElement<?>> result = new ArrayList<>();
    List<PsiElement> listElements = PerlArrayUtilCore.collectListElements(expr);

    for (int i = 0; i < listElements.size(); i++) {
      processExceptionElement(listElements, i, result, useStatementElement);
    }

    return result;
  }

  @Override
  public @NotNull List<PerlDelegatingLightNamedElement<?>> computeLightElementsFromStubs(@NotNull PerlUseStatementElement useStatementElement,
                                                                                         @NotNull PerlUseStatementStub useStatementStub) {
    return useStatementStub.getLightNamedElementsStubs().stream()
      .map(childStub -> {
        var stubType = childStub.getElementType();
        if (stubType == LIGHT_NAMESPACE_DEFINITION) {
          return new PerlLightExceptionClassDefinition(useStatementElement, (PerlNamespaceDefinitionStub)childStub);
        }
        else if (stubType == LIGHT_METHOD_DEFINITION) {
          return new PerlLightMethodDefinitionElement<>(useStatementElement, (PerlSubDefinitionStub)childStub);
        }
        else if (stubType == LIGHT_SUB_DEFINITION) {
          return new PerlLightSubDefinitionElement<>(useStatementElement, (PerlSubDefinitionStub)childStub);
        }
        throw new IllegalArgumentException("Unexpected stub type: " + stubType);
      })
      .collect(Collectors.toList());
  }

  private void processExceptionElement(@NotNull List<? extends PsiElement> listElements,
                                       int currentIndex,
                                       @NotNull List<? super PerlDelegatingLightNamedElement<?>> result,
                                       @NotNull PerlUseStatementElement useStatementElement) {
    PsiElement listElement = listElements.get(currentIndex);
    if (!useStatementElement.isAcceptableIdentifierElement(listElement)) {
      return;
    }
    String namespaceName = ElementManipulators.getValueText(listElement);
    if (StringUtil.isEmpty(namespaceName)) {
      return;
    }

    Map<String, PerlHashEntry> exceptionSettings =
      listElements.size() > currentIndex + 1
        ? PerlHashUtilCore.collectHashMap(listElements.get(currentIndex + 1))
      : Collections.emptyMap();

    // Building fields
    Set<PerlSubArgument> throwArguments = Collections.emptySet();
    PerlHashEntry fieldsEntry = exceptionSettings.get("fields");
    if (fieldsEntry != null && fieldsEntry.isComplete()) {
      PsiElement fieldsContainer = fieldsEntry.getNonNullValueElement();
      if (fieldsContainer instanceof PsiPerlAnonArray anonArray) {
        fieldsContainer = anonArray.getExpr();
      }
      List<PsiElement> elements = PerlArrayUtilCore.collectListElements(fieldsContainer);
      if (!elements.isEmpty()) {

        // Fields method
        result.add(new PerlLightMethodDefinitionElement<>(
          useStatementElement,
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
          if (useStatementElement.isAcceptableIdentifierElement(fieldElement)) {
            String fieldName = PerlScalarUtilCore.getStringContent(fieldElement);
            if (StringUtil.isNotEmpty(fieldName)) {
              throwArguments.add(PerlSubArgument.mandatoryScalar(fieldName));
              result.add(new PerlLightMethodDefinitionElement<>(
                useStatementElement,
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
      useStatementElement,
      namespaceName,
      LIGHT_NAMESPACE_DEFINITION,
      listElement,
      PerlMroType.DFS,
      Collections.singletonList(parentClass),
      PerlNamespaceAnnotations.tryToFindAnnotations(listElement, useStatementElement),
      Collections.emptyList(),
      Collections.emptyList(),
      Collections.emptyMap()
    ));

    // making alias
    PerlHashEntry aliasEntry = exceptionSettings.get("alias");
    if (aliasEntry != null && aliasEntry.isComplete()) {
      if (useStatementElement.isAcceptableIdentifierElement(aliasEntry.valueElement)) {
        String aliasName = aliasEntry.getValueString();
        if (StringUtil.isNotEmpty(aliasName)) {
          result.add(new PerlLightSubDefinitionElement<>(
            useStatementElement,
            aliasName,
            LIGHT_SUB_DEFINITION,
            aliasEntry.getNonNullValueElement(),
            PerlPackageUtilCore.getContextNamespaceName(useStatementElement),
            new ArrayList<>(throwArguments),
            PerlSubAnnotations.tryToFindAnnotations(aliasEntry.keyElement, aliasEntry.valueElement),
            UNKNOWN_VALUE_PROVIDER,
            null
          ));
        }
      }
    }
  }
}
