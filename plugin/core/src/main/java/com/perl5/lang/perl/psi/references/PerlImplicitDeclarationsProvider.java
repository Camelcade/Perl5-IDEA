/*
 * Copyright 2015-2024 Alexandr Evstigneev
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

package com.perl5.lang.perl.psi.references;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.extensions.ExtensionPointName;
import com.intellij.openapi.util.JDOMUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlScalarValue;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValue;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValues;
import com.perl5.lang.perl.psi.PerlAnnotationWithValue;
import com.perl5.lang.perl.psi.impl.PerlBuiltInSubDefinition;
import com.perl5.lang.perl.psi.impl.PerlImplicitSubDefinition;
import com.perl5.lang.perl.psi.impl.PerlImplicitVariableDeclaration;
import com.perl5.lang.perl.psi.utils.PerlAnnotations;
import com.perl5.lang.perl.psi.utils.PerlSubArgument;
import com.perl5.lang.perl.psi.utils.PerlVariableType;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jdom.Element;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.perl5.lang.perl.util.PerlPackageUtil.NAMESPACE_ANY_VALUE;

/**
 * Provides information about implicitly defined subs, e.g {@code Types::Standard}
 */
public abstract class PerlImplicitDeclarationsProvider {
  static final ExtensionPointName<PerlImplicitDeclarationsProvider> EP_NAME = ExtensionPointName.create("com.perl5.implicitSubsProvider");
  private static final Logger LOG = Logger.getInstance(PerlImplicitDeclarationsProvider.class);
  private static final @NonNls String PACKAGE = "package";
  private static final @NonNls String SUB_ELEMENT = "sub";
  private static final @NonNls String VARIABLE = "var";
  private static final @NonNls String NAME = "name";
  private static final @NonNls String ARGUMENTS_ELEMENT = "arguments";
  private static final @NonNls String OPTIONAL_ELEMENT = "optional";
  private static final @NonNls String ARGUMENT_ELEMENT = "argument";
  private static final @NonNls String RETURNS_ELEMENT = "returns";

  /**
   * @return path to XML resource with entities description or null if there is no one
   * @see PerlImplicitDeclarationsService#readSubs(java.lang.ClassLoader, java.lang.String)
   */
  protected abstract @NonNls @Nullable String getDataFileName();

  /**
   * Registers implicit entities with project-level {@link PerlImplicitDeclarationsService}
   *
   * @apiNote default implementation reads definitions from the xml file, provided by {@link #getDataFileName()}
   */
  protected void registerDeclarations(@NotNull PerlImplicitDeclarationsService declarationsService) {
    String fileName = getDataFileName();
    if (StringUtil.isEmpty(fileName)) {
      return;
    }

    ClassLoader classLoader = getClass().getClassLoader();
    final Element xmlElement;
    try {
      xmlElement = JDOMUtil.load(classLoader.getResourceAsStream(fileName));
    }
    catch (Exception e) {
      LOG.warn("Error loading resources from " + classLoader + " " + fileName, e);
      return;
    }
    if (xmlElement == null) {
      LOG.warn("Error loading resources from " + classLoader + " " + fileName);
      return;
    }
    for (Element namespaceElement : xmlElement.getChildren(PACKAGE)) {
      String namespaceName = namespaceElement.getAttributeValue("name");
      if (StringUtil.isEmpty(namespaceName)) {
        LOG.warn("Missing or empty package name");
        continue;
      }
      for (Element element : namespaceElement.getChildren()) {
        switch (element.getName()) {
          case SUB_ELEMENT -> readSub(declarationsService, namespaceName, element);
          case VARIABLE -> readVariable(declarationsService, namespaceName, element);
          default -> LOG.warn("Don't know what to do with: " + element.getName());
        }
      }
    }
  }

  private void readVariable(@NotNull PerlImplicitDeclarationsService declarationsService,
                            @NotNull String namespaceName,
                            @NotNull Element element) {
    String varName = element.getAttributeValue(NAME);
    if (StringUtil.isEmpty(varName)) {
      LOG.warn("Missing or empty variable name");
      return;
    }
    declarationsService.registerVariable(
      PerlImplicitVariableDeclaration.createGlobal(declarationsService.getPsiManager(), varName, namespaceName));
  }

  private void readSub(@NotNull PerlImplicitDeclarationsService declarationsService,
                       @NotNull String namespaceName,
                       @NotNull Element element) {
    String subName = element.getAttributeValue(NAME);
    if (StringUtil.isEmpty(subName)) {
      LOG.warn("Missing or empty name attribute for sub");
      return;
    }
    PerlImplicitSubDefinition subDefinition;
    if (PerlPackageUtil.CORE_NAMESPACE.equals(namespaceName)) {
      subDefinition = new PerlBuiltInSubDefinition(
        declarationsService.getPsiManager(),
        subName,
        PerlPackageUtil.CORE_NAMESPACE,
        readArguments(element.getChild(ARGUMENTS_ELEMENT), subName),
        readReturnValue(element)
      );
    }
    else {
      subDefinition = new PerlImplicitSubDefinition(
        declarationsService.getPsiManager(),
        subName,
        namespaceName,
        readArguments(element.getChild(ARGUMENTS_ELEMENT), subName),
        readReturnValue(element)
      );
    }
    declarationsService.registerSub(subDefinition);
  }

  /**
   * It is unclear how to generify reading the value, but this should be some generic logic for this. Probably we should attempt to lex
   * the value as an {@code @returns} annotation argument and process it via some method. But this is good enough for now.
   *
   * @see PerlAnnotations#getReturnClass(PerlAnnotationWithValue)
   */
  private @NotNull PerlValue readReturnValue(@NotNull Element subElement) {
    var returnsAttribute = subElement.getAttributeValue(RETURNS_ELEMENT);
    if (returnsAttribute == null) {
      return PerlValues.UNKNOWN_VALUE;
    }
    if (returnsAttribute.equals("*")) {
      return NAMESPACE_ANY_VALUE;
    }
    return PerlScalarValue.create(PerlPackageUtil.getCanonicalName(returnsAttribute));
  }

  private @NotNull List<PerlSubArgument> readArguments(@Nullable Element argumentsElement, @NotNull String subName) {
    if (argumentsElement == null) {
      return Collections.emptyList();
    }
    List<Element> mandatoryElements = argumentsElement.getChildren(ARGUMENT_ELEMENT);

    List<Element> optionalElements;
    Element optionalElement = argumentsElement.getChild(OPTIONAL_ELEMENT);
    if (optionalElement == null) {
      optionalElements = Collections.emptyList();
    }
    else {
      optionalElements = optionalElement.getChildren(ARGUMENT_ELEMENT);
    }

    if (mandatoryElements.isEmpty() && optionalElements.isEmpty()) {
      return Collections.emptyList();
    }

    List<PerlSubArgument> result = new ArrayList<>();
    mandatoryElements.stream().map(element -> readArgument(element, false, subName)).filter(Objects::nonNull).forEach(result::add);
    optionalElements.stream().map(element -> readArgument(element, true, subName)).filter(Objects::nonNull).forEach(result::add);

    return result;
  }

  private @Nullable PerlSubArgument readArgument(@NotNull Element element, boolean isOptional, String subName) {
    String variableName = element.getAttributeValue("name");
    if (StringUtil.isEmpty(variableName)) {
      LOG.warn("Missing argument name for " + subName);
      return null;
    }

    String type = element.getAttributeValue("type");
    if (type == null || type.length() != 1) {
      LOG.warn("Unknown type modifier for argument: " + variableName + " in " + subName);
      return null;
    }
    return PerlSubArgument.create(PerlVariableType.bySigil(type.charAt(0)), variableName, isOptional);
  }
}
