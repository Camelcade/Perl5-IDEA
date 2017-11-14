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
import com.perl5.lang.perl.psi.utils.PerlSubArgument;
import com.perl5.lang.perl.psi.utils.PerlVariableType;
import com.perl5.lang.perl.util.PerlPackageUtil;
import gnu.trove.THashMap;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class PerlBuiltInSubsService {
  private static final Logger LOG = Logger.getInstance(PerlBuiltInSubsService.class);
  private static final String SUB_ELEMENT = "sub";
  private static final String ARGUMENTS_ELEMENT = "arguments";
  private static final String OPTIONAL_ELEMENT = "optional";
  private static final String ARGUMENT_ELEMENT = "argument";
  private static String XML_PATH = "perlData/subs.xml";
  private final Map<String, PerlBuiltInSubDefinition> mySubsMap = new THashMap<>();
  @NotNull
  private final PsiManager myPsiManager;

  public PerlBuiltInSubsService(@NotNull Project project) {
    myPsiManager = PsiManager.getInstance(project);

    readExternal();
  }

  private void readExternal() {
    try {
      Element xmlElement = JDOMUtil.load(this.getClass().getClassLoader().getResourceAsStream(XML_PATH));
      readSubs(xmlElement);
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private void readSubs(@NotNull Element xmlElement) {
    for (Element subElement : xmlElement.getChildren(SUB_ELEMENT)) {
      String subName = subElement.getAttribute("name").getValue();
      if (StringUtil.isEmpty(subName)) {
        LOG.warn("Missing or empty name attribute for sub");
      }
      else {
        mySubsMap.put(subName, new PerlBuiltInSubDefinition(
          myPsiManager,
          subName,
          PerlPackageUtil.CORE_PACKAGE,
          readArguments(subElement.getChild(ARGUMENTS_ELEMENT), subName),
          null
        ));
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
  public PerlSubDefinitionElement findSub(@Nullable String subName) {
    return mySubsMap.get(subName);
  }

  public boolean processSubs(@NotNull Processor<PerlSubDefinitionElement> processor) {
    for (PerlBuiltInSubDefinition subDefinition : mySubsMap.values()) {
      ProgressManager.checkCanceled();
      if (!processor.process(subDefinition)) {
        return false;
      }
    }
    return true;
  }


  @NotNull
  public static PerlBuiltInSubsService getInstance(@NotNull Project project) {
    return ServiceManager.getService(project, PerlBuiltInSubsService.class);
  }
}
