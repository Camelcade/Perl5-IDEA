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

package com.perl5.lang.perl.psi.references;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.JDOMUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiManager;
import com.intellij.util.Processor;
import com.perl5.lang.perl.psi.PerlSubDefinitionElement;
import com.perl5.lang.perl.psi.PerlVariableDeclarationElement;
import com.perl5.lang.perl.psi.impl.PerlBuiltInSubDefinition;
import com.perl5.lang.perl.psi.impl.PerlImplicitSubDefinition;
import com.perl5.lang.perl.psi.impl.PerlImplicitVariableDeclaration;
import com.perl5.lang.perl.psi.utils.PerlSubArgument;
import com.perl5.lang.perl.psi.utils.PerlVariableType;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jdom.Element;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static com.perl5.lang.perl.util.PerlPackageUtil.CORE_PACKAGE;
import static com.perl5.lang.perl.util.PerlPackageUtil.PACKAGE_SEPARATOR;

/**
 * Service for a common tricky-defined entities:<ul>
 * <li>Built-in subs</li>
 * <li>Subs from extensions</li>
 * <li>Global scalars, arrays and hashes defined by modules code</li>
 * </ul>
 */
public class PerlImplicitDeclarationsService {
  private static final Logger LOG = Logger.getInstance(PerlImplicitDeclarationsService.class);
  private static final String PACKAGE = "package";
  private static final String SUB_ELEMENT = "sub";
  private static final String VARIABLE = "var";
  private static final String NAME = "name";
  private static final String ARGUMENTS_ELEMENT = "arguments";
  private static final String OPTIONAL_ELEMENT = "optional";
  private static final String ARGUMENT_ELEMENT = "argument";
  private final Map<String, PerlImplicitSubDefinition> mySubsMap = new HashMap<>();
  private final Map<String, PerlImplicitVariableDeclaration> myScalarsMap = new HashMap<>();
  private final Map<String, PerlImplicitVariableDeclaration> myArraysMap = new HashMap<>();
  private final Map<String, PerlImplicitVariableDeclaration> myHashesMap = new HashMap<>();
  @NotNull
  private final PsiManager myPsiManager;

  public PerlImplicitDeclarationsService(@NotNull Project project) {
    myPsiManager = PsiManager.getInstance(project);

    PerlImplicitDeclarationsProvider.EP_NAME.extensions().forEach(it -> readDefinitions(it, it.getSubsFileName()));
  }

  /**
   * Reads subs from xml file.
   * @param classLoaderProvider source of the classLoader for loading a resource
   * @param fileName resource file name
   */
  private void readDefinitions(@NotNull Object classLoaderProvider, @NotNull String fileName) {
    ClassLoader classLoader = classLoaderProvider.getClass().getClassLoader();
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
      String namespaceName = namespaceElement.getAttribute("name").getValue();
      if (StringUtil.isEmpty(namespaceName)) {
        LOG.warn("Missing or empty package name");
        continue;
      }
      for (Element element : namespaceElement.getChildren()) {
        switch (element.getName()) {
          case SUB_ELEMENT:
            readSub(namespaceName, element);
            break;
          case VARIABLE:
            readVariable(namespaceName, element);
            break;
          default:
            LOG.warn("Don't know what to do with: " + element.getName());
        }
      }
    }
  }

  private void readVariable(@NotNull String namespaceName, @NotNull Element element) {
    String varName = element.getAttribute(NAME).getValue();
    if (StringUtil.isEmpty(varName)) {
      LOG.warn("Missing or empty variable name");
      return;
    }
    PerlImplicitVariableDeclaration implicitVariable = PerlImplicitVariableDeclaration.createGlobal(myPsiManager, varName, namespaceName);
    String canonicalName = implicitVariable.getCanonicalName();
    switch (implicitVariable.getVariableType()) {
      case SCALAR:
        myScalarsMap.put(canonicalName, implicitVariable);
        break;
      case ARRAY:
        myArraysMap.put(canonicalName, implicitVariable);
        break;
      case HASH:
        myHashesMap.put(canonicalName, implicitVariable);
        break;
      default:
        LOG.warn("Can handle only SCALAR, ARRAY or HASH at the moment, got: " + implicitVariable);
    }
  }

  private void readSub(@NotNull String namespaceName, @NotNull Element element) {
    String subName = element.getAttribute(NAME).getValue();
    if (StringUtil.isEmpty(subName)) {
      LOG.warn("Missing or empty name attribute for sub");
      return;
    }
    PerlImplicitSubDefinition subDefinition;
    if (PerlPackageUtil.CORE_PACKAGE.equals(namespaceName)) {
      subDefinition = new PerlBuiltInSubDefinition(
        myPsiManager,
        subName,
        PerlPackageUtil.CORE_PACKAGE,
        readArguments(element.getChild(ARGUMENTS_ELEMENT), subName),
        null
      );
    }
    else {
      subDefinition = new PerlImplicitSubDefinition(
        myPsiManager,
        subName,
        namespaceName,
        readArguments(element.getChild(ARGUMENTS_ELEMENT), subName),
        null
      );
    }
    mySubsMap.put(subDefinition.getCanonicalName(), subDefinition);
  }


  @NotNull
  private List<PerlSubArgument> readArguments(@Nullable Element argumentsElement, @NotNull String subName) {
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

  @Nullable
  private PerlSubArgument readArgument(@NotNull Element element, boolean isOptional, String subName) {
    String variableName = element.getAttribute("name").getValue();
    if (StringUtil.isEmpty(variableName)) {
      LOG.warn("Missing argument name for " + subName);
      return null;
    }

    PerlVariableType variableType;
    String type = element.getAttribute("type").getValue();
    if (type == null || type.length() != 1 || (variableType = PerlVariableType.bySigil(type.charAt(0))) == null) {
      LOG.warn("Unknown type modifier for argument: " + variableName + " in " + subName);
      return null;
    }
    return PerlSubArgument.create(variableType, variableName, isOptional);
  }

  @Nullable
  public PerlSubDefinitionElement findCoreSub(@Nullable String subName) {
    return findSub(CORE_PACKAGE, subName);
  }

  @Nullable
  public PerlSubDefinitionElement findSub(@Nullable String packageName, @Nullable String subName) {
    return findSub(packageName + PACKAGE_SEPARATOR + subName);
  }

  @Contract("null->null")
  @Nullable
  public PerlSubDefinitionElement findSub(@Nullable String canonicalName) {
    return mySubsMap.get(canonicalName);
  }

  public boolean processSubsInPackage(@NotNull String packageName, @NotNull Processor<? super PerlSubDefinitionElement> processor) {
    return processSubs(it -> !packageName.equals(it.getPackageName()) || processor.process(it));
  }

  public boolean processSubs(@NotNull String canonicalName, @NotNull Processor<? super PerlSubDefinitionElement> processor) {
    PerlSubDefinitionElement subDefinitionElement = findSub(canonicalName);
    return subDefinitionElement == null || processor.process(subDefinitionElement);
  }

  public boolean processSubs(@NotNull Processor<? super PerlSubDefinitionElement> processor) {
    for (PerlImplicitSubDefinition subDefinition : mySubsMap.values()) {
      ProgressManager.checkCanceled();
      if (!processor.process(subDefinition)) {
        return false;
      }
    }
    return true;
  }

  @Contract("null->null")
  @Nullable
  public PerlVariableDeclarationElement getScalar(@Nullable String canonicalName) {
    return myScalarsMap.get(canonicalName);
  }

  @Contract("null->null")
  @Nullable
  public PerlVariableDeclarationElement getArray(@Nullable String canonicalName) {
    return myArraysMap.get(canonicalName);
  }

  @Contract("null->null")
  @Nullable
  public PerlVariableDeclarationElement getHash(@Nullable String canonicalName) {
    return myHashesMap.get(canonicalName);
  }

  public boolean processScalars(@NotNull String canonicalName, @NotNull Processor<? super PerlVariableDeclarationElement> processor) {
    PerlVariableDeclarationElement scalar = getScalar(canonicalName);
    return scalar == null || processor.process(scalar);
  }

  public boolean processArrays(@NotNull String canonicalName, @NotNull Processor<? super PerlVariableDeclarationElement> processor) {
    PerlVariableDeclarationElement array = getArray(canonicalName);
    return array == null || processor.process(array);
  }

  public boolean processHashes(@NotNull String canonicalName, @NotNull Processor<? super PerlVariableDeclarationElement> processor) {
    PerlVariableDeclarationElement hash = getHash(canonicalName);
    return hash == null || processor.process(hash);
  }

  public boolean processScalarsInPackage(@Nullable String packageName,
                                         @NotNull Processor<? super PerlVariableDeclarationElement> processor) {
    return packageName != null && processScalars(it -> !packageName.equals(it.getPackageName()) || processor.process(it));
  }

  public boolean processArraysInPackage(@Nullable String packageName,
                                        @NotNull Processor<? super PerlVariableDeclarationElement> processor) {
    return packageName != null && processArrays(it -> !packageName.equals(it.getPackageName()) || processor.process(it));
  }

  public boolean processHashesInPackage(@Nullable String packageName,
                                        @NotNull Processor<? super PerlVariableDeclarationElement> processor) {
    return packageName != null && processHashes(it -> !packageName.equals(it.getPackageName()) || processor.process(it));
  }

  public boolean processScalars(@NotNull Processor<? super PerlVariableDeclarationElement> processor) {
    return processVariables(myScalarsMap, processor);
  }

  public boolean processArrays(@NotNull Processor<? super PerlVariableDeclarationElement> processor) {
    return processVariables(myArraysMap, processor);
  }

  public boolean processHashes(@NotNull Processor<? super PerlVariableDeclarationElement> processor) {
    return processVariables(myHashesMap, processor);
  }

  private static boolean processVariables(@NotNull Map<String, PerlImplicitVariableDeclaration> variablesMap,
                                          @NotNull Processor<? super PerlVariableDeclarationElement> processor) {
    for (PerlImplicitVariableDeclaration variableDeclaration : variablesMap.values()) {
      ProgressManager.checkCanceled();
      if (!processor.process(variableDeclaration)) {
        return false;
      }
    }
    return true;
  }

  @NotNull
  public static PerlImplicitDeclarationsService getInstance(@NotNull Project project) {
    return ServiceManager.getService(project, PerlImplicitDeclarationsService.class);
  }
}
