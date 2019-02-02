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
import com.perl5.lang.perl.psi.impl.PerlBuiltInSubDefinition;
import com.perl5.lang.perl.psi.impl.PerlImplicitSubDefinition;
import com.perl5.lang.perl.psi.utils.PerlSubArgument;
import com.perl5.lang.perl.psi.utils.PerlVariableType;
import com.perl5.lang.perl.util.PerlPackageUtil;
import gnu.trove.THashMap;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static com.perl5.lang.perl.util.PerlPackageUtil.CORE_PACKAGE;
import static com.perl5.lang.perl.util.PerlPackageUtil.PACKAGE_SEPARATOR;

/**
 * Service for a common tricky-defined subs:<ul>
 * <li>Built-in subs</li>
 * <li>Subs from extensions</li>
 * </ul>
 */
public class PerlImplicitSubsService {
  private static final Logger LOG = Logger.getInstance(PerlImplicitSubsService.class);
  private static final String PACKAGE = "package";
  private static final String SUB_ELEMENT = "sub";
  private static final String ARGUMENTS_ELEMENT = "arguments";
  private static final String OPTIONAL_ELEMENT = "optional";
  private static final String ARGUMENT_ELEMENT = "argument";
  private final Map<String, PerlImplicitSubDefinition> mySubsMap = new THashMap<>();
  @NotNull
  private final PsiManager myPsiManager;

  public PerlImplicitSubsService(@NotNull Project project) {
    myPsiManager = PsiManager.getInstance(project);

    PerlImplicitSubsProvider.EP_NAME.extensions().forEach(it -> readSubs(it, it.getSubsFileName()));
  }

  /**
   * Reads subs from xml file.
   * @param classLoaderProvider source of the classLoader for loading a resource
   * @param fileName resource file name
   */
  private void readSubs(@NotNull Object classLoaderProvider, @NotNull String fileName) {
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
      for (Element subElement : namespaceElement.getChildren(SUB_ELEMENT)) {
        String subName = subElement.getAttribute("name").getValue();
        if (StringUtil.isEmpty(subName)) {
          LOG.warn("Missing or empty name attribute for sub");
        }
        else {
          PerlImplicitSubDefinition subDefinition;
          if (PerlPackageUtil.CORE_PACKAGE.equals(namespaceName)) {
            subDefinition = new PerlBuiltInSubDefinition(
              myPsiManager,
              subName,
              PerlPackageUtil.CORE_PACKAGE,
              readArguments(subElement.getChild(ARGUMENTS_ELEMENT), subName),
              null
            );
          }
          else {
            subDefinition = new PerlImplicitSubDefinition(
              myPsiManager,
              subName,
              namespaceName,
              readArguments(subElement.getChild(ARGUMENTS_ELEMENT), subName),
              null
            );
          }
          mySubsMap.put(subDefinition.getCanonicalName(), subDefinition);
        }
      }
    }
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

  @Nullable
  public PerlSubDefinitionElement findSub(@Nullable String canonicalName) {
    return mySubsMap.get(canonicalName);
  }

  public boolean processSubsInPackage(@NotNull String packageName, @NotNull Processor<? super PerlImplicitSubDefinition> processor) {
    return processSubs(it -> !packageName.equals(it.getPackageName()) || processor.process(it));
  }

  public boolean processSubs(@NotNull String subFqn, @NotNull Processor<? super PerlImplicitSubDefinition> processor) {
    return processSubs(it -> !subFqn.equals(it.getCanonicalName()) || processor.process(it));
  }

  public boolean processSubs(@NotNull Processor<PerlImplicitSubDefinition> processor) {
    for (PerlImplicitSubDefinition subDefinition : mySubsMap.values()) {
      ProgressManager.checkCanceled();
      if (!processor.process(subDefinition)) {
        return false;
      }
    }
    return true;
  }

  @NotNull
  public static PerlImplicitSubsService getInstance(@NotNull Project project) {
    return ServiceManager.getService(project, PerlImplicitSubsService.class);
  }
}
