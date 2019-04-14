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
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.ElementManipulators;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlScalarValue;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValue;
import com.perl5.lang.perl.parser.moose.stubs.PerlMooseAttributeWrapperStub;
import com.perl5.lang.perl.psi.PerlSubExpr;
import com.perl5.lang.perl.psi.PerlVisitor;
import com.perl5.lang.perl.psi.PsiPerlAnonArray;
import com.perl5.lang.perl.psi.PsiPerlAnonHash;
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
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

import static com.perl5.lang.perl.psi.stubs.PerlStubElementTypes.LIGHT_ATTRIBUTE_DEFINITION;
import static com.perl5.lang.perl.psi.stubs.PerlStubElementTypes.LIGHT_METHOD_DEFINITION;

public class PerlMooseAttributeWrapper extends PerlPolyNamedElementBase<PerlMooseAttributeWrapperStub> {
  private static final String MUTATOR_KEY = "writer";
  private static final String ACCESSOR_KEY = "accessor";
  private static final String READER_KEY = "reader";
  private static final List<String> MOOSE_SUB_NAMES_KEYS = Arrays.asList(
    READER_KEY, MUTATOR_KEY, ACCESSOR_KEY, "predicate", "clearer"
  );

  public PerlMooseAttributeWrapper(@NotNull PerlMooseAttributeWrapperStub stub,
                                   @NotNull IStubElementType nodeType) {
    super(stub, nodeType);
  }

  public PerlMooseAttributeWrapper(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  protected void visitWrapper(@NotNull PerlVisitor visitor) {
    visitor.visitMooseAttributeWrapper(this);
  }

  @NotNull
  public List<String> getAttributesNames() {
    PerlMooseAttributeWrapperStub stub = getGreenStub();
    if (stub != null) {
      return stub.getAttributesNames();
    }
    Pair<List<PsiElement>, List<PsiElement>> lists = getIdentifiersAndListElements();
    if (lists == null || lists.first.isEmpty()) {
      return Collections.emptyList();
    }
    return lists.first.stream().map(ElementManipulators::getValueText).collect(Collectors.toList());
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

  /**
   * @return pair of lists: identifiers lists and list of has arguments
   * null if something went wrong
   */
  @Nullable
  private Pair<List<PsiElement>, List<PsiElement>> getIdentifiersAndListElements() {
    PsiElement firstChild = getFirstChild();
    List<PsiElement> listElements = PerlArrayUtil.collectListElements(firstChild);
    if (listElements.isEmpty()) {
      return null;
    }
    PsiElement namesContainer = listElements.get(0);
    if (namesContainer instanceof PsiPerlAnonArray) {
      namesContainer = ((PsiPerlAnonArray)namesContainer).getExpr();
    }
    List<PsiElement> identifiers = ContainerUtil.filter(PerlArrayUtil.collectListElements(namesContainer),
                                                        this::isAcceptableIdentifierElement);
    if (identifiers.isEmpty()) {
      return null;
    }
    return Pair.create(identifiers, listElements);
  }


  @NotNull
  @Override
  public List<PerlDelegatingLightNamedElement> calcLightElementsFromPsi() {
    Pair<List<PsiElement>, List<PsiElement>> lists = getIdentifiersAndListElements();
    if (lists == null) {
      return Collections.emptyList();
    }
    return lists.second.size() < 3 ? createMojoAttributes(lists) : createMooseAttributes(lists.first, lists.second);
  }

  @NotNull
  private List<PerlDelegatingLightNamedElement> createMojoAttributes(@NotNull Pair<List<PsiElement>, List<PsiElement>> lists) {
    List<PsiElement> arguments = lists.second.subList(1, lists.second.size());
    PerlSubExpr bodyExpr = arguments.size() == 1 && arguments.get(0) instanceof PerlSubExpr ? (PerlSubExpr)arguments.get(0) : null;

    List<PerlDelegatingLightNamedElement> result = new ArrayList<>();
    String packageName = PerlPackageUtil.getContextNamespaceName(this);
    for (PsiElement identifier : lists.first) {
      PerlLightMethodDefinitionElement<PerlMooseAttributeWrapper> newMethod = new PerlLightMethodDefinitionElement<>(
        this,
        ElementManipulators.getValueText(identifier),
        LIGHT_METHOD_DEFINITION,
        identifier,
        packageName,
        Arrays.asList(PerlSubArgument.self(), PerlSubArgument.optionalScalar("new_value")),
        PerlSubAnnotations.tryToFindAnnotations(identifier, getParent()),
        bodyExpr == null ? null : bodyExpr.getBlock()
      );
      result.add(setMojoReturnsComputation(newMethod));
    }

    return result;
  }

  @NotNull
  private PerlLightMethodDefinitionElement setMojoReturnsComputation(
    @NotNull PerlLightMethodDefinitionElement<PerlMooseAttributeWrapper> newMethod) {
    newMethod.setReturnValue(PerlScalarValue.create(newMethod.getNamespaceName()));
    return newMethod;
  }

  @NotNull
  private List<PerlDelegatingLightNamedElement> createMooseAttributes(@NotNull List<PsiElement> identifiers,
                                                                      @NotNull List<PsiElement> listElements) {

    List<PerlDelegatingLightNamedElement> result = new ArrayList<>();
    String packageName = PerlPackageUtil.getContextNamespaceName(this);

    Map<String, PerlHashEntry> parameters = PerlHashUtil.packToHash(listElements.subList(1, listElements.size()));
    // handling is
    PerlHashEntry isParameter = parameters.get("is");
    boolean isWritable = isParameter != null && StringUtil.equals("rw", isParameter.getValueString());
    PsiElement forcedIdentifier = null;

    // handling isa and does
    PerlHashEntry isaEntry = parameters.get("isa");
    String valueClass = null;
    if (isaEntry == null) {
      isaEntry = parameters.get("does");
    }

    if (isaEntry != null && isaEntry.valueElement != null && isAcceptableIdentifierElement(isaEntry.valueElement)) {
      valueClass = isaEntry.getValueString();
      if (StringUtil.isEmpty(valueClass)) {
        valueClass = null;
      }
    }

    // handling accessor, reader, etc.
    List<PerlLightMethodDefinitionElement> secondaryResult = new ArrayList<>();
    for (String key : MOOSE_SUB_NAMES_KEYS) {
      PerlHashEntry entry = parameters.get(key);
      if (entry == null) {
        continue;
      }

      String methodName = entry.getValueString();
      if (StringUtil.isEmpty(methodName)) {
        continue;
      }

      if (!isWritable && key.equals(MUTATOR_KEY)) {
        continue;
      }

      if (!isWritable && key.equals(READER_KEY) || key.equals(ACCESSOR_KEY)) {
        forcedIdentifier = entry.valueElement;
        continue;
      }

      if (!isAcceptableIdentifierElement(entry.valueElement)) {
        continue;
      }

      // fixme we could optimize new_value with subclassing and hardcoding of signature
      PsiElement identifier = entry.getNonNullValueElement();
      PerlLightMethodDefinitionElement<PerlMooseAttributeWrapper> secondaryElement = new PerlLightMethodDefinitionElement<>(
        this,
        ElementManipulators.getValueText(identifier),
        LIGHT_METHOD_DEFINITION,
        identifier,
        packageName,
        key.equals(MUTATOR_KEY) ? Arrays.asList(PerlSubArgument.self(), PerlSubArgument.optionalScalar("new_value", valueClass))
                                : Collections.emptyList(),
        PerlSubAnnotations.tryToFindAnnotations(identifier, entry.keyElement, getParent())
      );

      if (key.equals(READER_KEY) && valueClass != null) {
        secondaryElement.setReturnValue(PerlScalarValue.create(valueClass));
      }

      secondaryResult.add(
        secondaryElement
      );
    }

    // handle handles
    PerlHashEntry handlesEntry = parameters.get("handles");
    if (handlesEntry != null) {
      // to show proper signatures, we need an access to delegates, what requires indexes; we should do this in runtime, not indexing, but store delegation target
      if (handlesEntry.valueElement instanceof PsiPerlAnonHash) {
        // handle handles HASH
        Map<String, PerlHashEntry> delegatesMap = PerlHashUtil.collectHashMap(handlesEntry.valueElement);
        for (PerlHashEntry delegateEntry : delegatesMap.values()) {
          if (!isAcceptableIdentifierElement(delegateEntry.keyElement)) {
            continue;
          }
          secondaryResult.add(new PerlLightMethodDefinitionElement<>(
            this,
            ElementManipulators.getValueText(delegateEntry.keyElement),
            LIGHT_METHOD_DEFINITION,
            delegateEntry.keyElement,
            packageName,
            Collections.emptyList(),
            PerlSubAnnotations.tryToFindAnnotations(delegateEntry.keyElement, handlesEntry.keyElement, getParent())
          ));
        }
      }
      else if (handlesEntry.valueElement instanceof PsiPerlAnonArray) {
        // handle handles ARRAY
        List<PsiElement> delegatesIdentifiers = PerlArrayUtil.collectListElements(((PsiPerlAnonArray)handlesEntry.valueElement).getExpr());
        for (PsiElement identifier : delegatesIdentifiers) {
          if (!isAcceptableIdentifierElement(identifier)) {
            continue;
          }
          secondaryResult.add(new PerlLightMethodDefinitionElement<>(
            this,
            ElementManipulators.getValueText(identifier),
            LIGHT_METHOD_DEFINITION,
            identifier,
            packageName,
            Collections.emptyList(),
            PerlSubAnnotations.tryToFindAnnotations(identifier, handlesEntry.keyElement, getParent())
          ));
        }
      }
    }

    // handle required this should alter contstructor, no idea how for now
    // handle HANDLES ROLE OR ROLETYPE requires index
    // handle HANDLES DUCKTYPE requires index
    // handle HANDLES REGEXP this requires regexp & resolve

    for (PsiElement identifier : identifiers) {
      if (forcedIdentifier != null) {
        identifier = forcedIdentifier;
      }
      if (!isAcceptableIdentifierElement(identifier)) {
        continue;
      }
      PerlAttributeDefinition newElement = new PerlAttributeDefinition(
        this,
        PerlAttributeDefinition.DEFAULT_NAME_COMPUTATION.fun(ElementManipulators.getValueText(identifier)),
        LIGHT_ATTRIBUTE_DEFINITION,
        identifier,
        packageName,
        isWritable
        ? Arrays.asList(PerlSubArgument.self(), PerlSubArgument.optionalScalar("new_value", valueClass))
        : Collections.emptyList(),
        PerlSubAnnotations.tryToFindAnnotations(identifier, getParent())
      );
      if (valueClass != null) {
        PerlValue returnValue = PerlScalarValue.create(valueClass);
        newElement.setReturnValue(returnValue);
      }
      result.add(newElement);
      result.addAll(secondaryResult);
      secondaryResult.clear();
    }

    return result;
  }
}
