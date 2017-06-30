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

package com.perl5.lang.perl.parser.moose.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.ElementManipulators;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.util.PairFunction;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.lang.perl.psi.PsiPerlAnonArray;
import com.perl5.lang.perl.psi.impl.PerlPolyNamedElementBase;
import com.perl5.lang.perl.psi.light.PerlDelegatingLightNamedElement;
import com.perl5.lang.perl.psi.light.PerlLightMethodDefinitionElement;
import com.perl5.lang.perl.psi.stubs.PerlPolyNamedElementStub;
import com.perl5.lang.perl.psi.stubs.subsdefinitions.PerlSubDefinitionStub;
import com.perl5.lang.perl.psi.utils.PerlSubAnnotations;
import com.perl5.lang.perl.psi.utils.PerlSubArgument;
import com.perl5.lang.perl.util.PerlArrayUtil;
import com.perl5.lang.perl.util.PerlHashEntry;
import com.perl5.lang.perl.util.PerlHashUtil;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

import static com.perl5.lang.perl.psi.stubs.PerlStubElementTypes.LIGHT_ATTRIBUTE_DEFINITION;
import static com.perl5.lang.perl.psi.stubs.PerlStubElementTypes.LIGHT_METHOD_DEFINITION;

public class PerlMooseAttributeWrapper extends PerlPolyNamedElementBase<PerlPolyNamedElementStub> {
  private static final String MUTATOR_KEY = "writer";
  private static final String ACCESSOR_KEY = "accessor";
  private static final String READER_KEY = "reader";
  private static final List<String> MOOSE_SUB_NAMES_KEYS = Arrays.asList(
    READER_KEY, MUTATOR_KEY, ACCESSOR_KEY, "predicate", "clearer"
  );

  public PerlMooseAttributeWrapper(@NotNull PerlPolyNamedElementStub stub,
                                   @NotNull IStubElementType nodeType) {
    super(stub, nodeType);
  }

  public PerlMooseAttributeWrapper(@NotNull ASTNode node) {
    super(node);
  }

  @NotNull
  @Override
  public List<PerlDelegatingLightNamedElement> calcLightElementsFromStubs(@NotNull PerlPolyNamedElementStub stub) {
    return stub.getLightNamedElementsStubs().stream()
      .map(childStub -> {
        IStubElementType stubType = childStub.getStubType();
        if (stubType == LIGHT_METHOD_DEFINITION) {
          return setMojoReturnsComputation(new PerlLightMethodDefinitionElement<>(this, (PerlSubDefinitionStub)childStub));
        }
        else if (stubType == LIGHT_ATTRIBUTE_DEFINITION) {
          return new PerlAttributeDefinition(this, (PerlSubDefinitionStub)childStub);
        }
        throw new IllegalArgumentException("Unexpected stub type: " + stubType);
      })
      .collect(Collectors.toList());
  }

  @NotNull
  @Override
  public List<PerlDelegatingLightNamedElement> calcLightElementsFromPsi() {
    PsiElement firstChild = getFirstChild();
    List<PsiElement> listElements = PerlArrayUtil.collectListElements(firstChild);
    if (listElements.isEmpty()) {
      return Collections.emptyList();
    }
    PsiElement namesContainer = listElements.get(0);
    if (namesContainer instanceof PsiPerlAnonArray) {
      namesContainer = ((PsiPerlAnonArray)namesContainer).getExpr();
    }
    List<PsiElement> identifiers = ContainerUtil.filter(PerlArrayUtil.collectListElements(namesContainer),
                                                        this::isAcceptableIdentifierElement);
    if (identifiers.isEmpty()) {
      return Collections.emptyList();
    }

    return listElements.size() < 3 ? createMojoAttributes(identifiers) : createMooseAttributes(identifiers, listElements);
  }

  @NotNull
  private List<PerlDelegatingLightNamedElement> createMojoAttributes(@NotNull List<PsiElement> identifiers) {
    List<PerlDelegatingLightNamedElement> result = new ArrayList<>();
    String packageName = PerlPackageUtil.getContextPackageName(this);
    for (PsiElement identifier : identifiers) {
      PerlLightMethodDefinitionElement<PerlMooseAttributeWrapper> newMethod = new PerlLightMethodDefinitionElement<>(
        this,
        ElementManipulators.getValueText(identifier),
        LIGHT_METHOD_DEFINITION,
        identifier,
        packageName,
        Arrays.asList(PerlSubArgument.self(), PerlSubArgument.optionalScalar("new_value")),
        PerlSubAnnotations.tryToFindAnnotations(identifier, getParent())
      );
      result.add(setMojoReturnsComputation(newMethod));
    }

    return result;
  }

  @NotNull
  private PerlLightMethodDefinitionElement setMojoReturnsComputation(
    @NotNull PerlLightMethodDefinitionElement<PerlMooseAttributeWrapper> newMethod) {
    PairFunction<String, List<PsiElement>, String> defaultComputation = newMethod.getReturnsComputation();
    newMethod.setReturnsComputation(
      (context, args) -> args.isEmpty() ? defaultComputation.fun(context, args) : newMethod.getPackageName()
    );
    return newMethod;
  }

  @NotNull
  private List<PerlDelegatingLightNamedElement> createMooseAttributes(@NotNull List<PsiElement> identifiers,
                                                                      @NotNull List<PsiElement> listElements) {

    List<PerlDelegatingLightNamedElement> result = new ArrayList<>();
    String packageName = PerlPackageUtil.getContextPackageName(this);

    Map<String, PerlHashEntry> parameters = PerlHashUtil.packToHash(listElements.subList(1, listElements.size()));
    PerlHashEntry isParameter = parameters.get("is");
    boolean isWritable = isParameter != null && StringUtil.equals("rw", isParameter.getValueString());
    PsiElement forcedIdentifier = null;

    for (String key : MOOSE_SUB_NAMES_KEYS) {
      PerlHashEntry entry = parameters.get(key);
      if (entry == null || !isAcceptableIdentifierElement(entry.valueElement)) {
        continue;
      }

      String methodName = entry.getValueString();
      if (StringUtil.isEmpty(methodName)) {
        continue;
      }

      if (!isWritable && key.equals(READER_KEY) || key.equals(ACCESSOR_KEY)) {
        forcedIdentifier = entry.valueElement;
        continue;
      }

      PsiElement identifier = entry.getNonNullValueElement();
      result.add(
        new PerlLightMethodDefinitionElement<>(
          this,
          ElementManipulators.getValueText(identifier),
          LIGHT_METHOD_DEFINITION,
          identifier,
          packageName,
          key.equals(MUTATOR_KEY)
          ? Arrays.asList(PerlSubArgument.self(), PerlSubArgument.optionalScalar("new_value"))
          : Collections.emptyList(),
          PerlSubAnnotations.tryToFindAnnotations(identifier, getParent())
        )
      );
    }

    // handle isa
    // handle does
    // handle required
    // handle handles ARRAY
    // handle handles HASH

    for (PsiElement identifier : identifiers) {
      if (forcedIdentifier != null) {
        identifier = forcedIdentifier;
      }
      result.add(new PerlAttributeDefinition(
        this,
        ElementManipulators.getValueText(identifier),
        LIGHT_ATTRIBUTE_DEFINITION,
        identifier,
        packageName,
        isWritable ? Arrays.asList(PerlSubArgument.self(), PerlSubArgument.optionalScalar("new_value")) : Collections.emptyList(),
        PerlSubAnnotations.tryToFindAnnotations(identifier, getParent())
      ));
    }

    return result;
  }
}
