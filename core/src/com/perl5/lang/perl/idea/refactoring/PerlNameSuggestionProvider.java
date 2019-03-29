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

package com.perl5.lang.perl.idea.refactoring;

import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.ElementManipulators;
import com.intellij.psi.PsiElement;
import com.intellij.psi.codeStyle.SuggestedNameInfo;
import com.intellij.refactoring.rename.NameSuggestionProvider;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.idea.PerlNamesValidator;
import com.perl5.lang.perl.idea.intellilang.PerlInjectionMarkersService;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.*;

/**
 * Created by hurricup on 12.06.2015.
 */
public class PerlNameSuggestionProvider implements NameSuggestionProvider {
  private static final String SCALAR = "scalar";
  private static final String VALUE = "value";
  private static final List<String> SCALAR_BASE_NAMES = Arrays.asList(SCALAR, VALUE);
  private static final String ARRAY = "array";
  private static final String LIST = "list";
  private static final List<String> ARRAY_BASE_NAMES = Arrays.asList(ARRAY, LIST);
  private static final String HASH = "hash";
  private static final String MAP = "map";
  private static final String DICT = "dict";
  private static final List<String> HASH_BASE_NAMES = Arrays.asList(HASH, MAP, DICT);
  private static final String NUMBER = "number";
  private static final String STRING = "string";
  private static final String COMMAND_OUTPUT = "command_output";
  private static final String PATTERN = "pattern";
  private static final String REGEX = "regex";
  private static final List<String> REGEX_BASE_NAMES = Arrays.asList(PATTERN, REGEX);
  private static final String PACKAGE = "package";
  private static final String NAMESPACE = "namespace";
  private static final String MODULE = "module";
  private static final List<String> PACKAGE_BASE_NAMES = Arrays.asList(PACKAGE, NAMESPACE, MODULE);
  private static final int PACKAGE_CHUNKS_TO_USE = 3;
  private static final String PATH = "path";
  private static final String PATH_TO = "path_to";
  private static final String ABSOLUTE_PATH_TO = "absolute_path_to";
  private static final String RELATIVE_PATH_TO = "relative_path_to";
  private static final String PATH_NAME = "pathname";
  private static final List<String> BASE_PATH_NAMES = Arrays.asList(PATH, PATH_NAME);
  private static final int FILE_CHUNKS_TO_USE = 2;

  @Nullable
  @Override
  public SuggestedNameInfo getSuggestedNames(@NotNull PsiElement targetElement,
                                             @Nullable PsiElement contextElement,
                                             @NotNull Set<String> result) {
    suggestAndAddRecommendedName(targetElement, contextElement, result);
    return SuggestedNameInfo.NULL_INFO;
  }

  @Nullable
  private String suggestAndAddRecommendedName(@NotNull PsiElement targetElement,
                                              @Nullable PsiElement contextElement,
                                              @NotNull Set<String> result) {
    if (!targetElement.getLanguage().isKindOf(PerlLanguage.INSTANCE)) {
      return null;
    }

    String recommendedName = null;
    Set<String> suggestedNames = new LinkedHashSet<>();

    if (targetElement instanceof PerlHeredocOpener) {
      result.addAll(PerlInjectionMarkersService.getInstance(targetElement.getProject()).getSupportedMarkers());
    }
    else if (targetElement instanceof PerlVariableDeclarationElement) {
      PerlVariable declaredVariable = ((PerlVariableDeclarationElement)targetElement).getVariable();
      if (declaredVariable instanceof PsiPerlScalarVariable) {
        suggestedNames.addAll(SCALAR_BASE_NAMES);
      }
      else if (declaredVariable instanceof PsiPerlArrayVariable) {
        suggestedNames.addAll(ARRAY_BASE_NAMES);
      }
      else if (declaredVariable instanceof PsiPerlHashVariable) {
        suggestedNames.addAll(HASH_BASE_NAMES);
      }
      recommendedName = suggestAndAddRecommendedName((PerlVariableDeclarationElement)targetElement, contextElement, suggestedNames);
    }

    result.addAll(suggestedNames);
    return recommendedName == null ? suggestedNames.isEmpty() ? null : suggestedNames.iterator().next() : recommendedName;
  }

  /**
   * Attempts for figure out a variable name from {@code declaration} and/or {@code contextElement}. Adds suggestions to the {@code result}
   * and return recommended name if any
   */
  @Nullable
  private String suggestAndAddRecommendedName(@NotNull PerlVariableDeclarationElement declaration,
                                              @Nullable PsiElement contextElement,
                                              @NotNull Set<String> result) {
    PsiElement declarationWrapper = declaration.getParent();
    if (declarationWrapper == null) {
      return null;
    }
    PsiElement assignment = declarationWrapper.getParent();
    if (!(assignment instanceof PerlAssignExpression)) {
      return null;
    }
    PsiElement assignee = ((PerlAssignExpression)assignment).getRightSide();
    return suggestAndAddRecommendedName(assignee, result);
  }

  @Nullable
  private String suggestAndAddRecommendedName(@Nullable PsiElement expression, @NotNull Set<String> result) {
    if (expression instanceof PerlString) {
      result.add(STRING);
      if (expression instanceof PsiPerlStringXq) {
        result.add(COMMAND_OUTPUT);
        return COMMAND_OUTPUT;
      }
      if (expression.getChildren().length == 0) {
        String valueText = ElementManipulators.getValueText(expression);
        if (valueText.length() < 30) {
          String variableNameFromString = spacedToSnakeCase(valueText);
          if (PerlNamesValidator.isIdentifier(variableNameFromString)) {
            result.add(variableNameFromString);
            return variableNameFromString;
          }
        }

        if (PerlString.looksLikePackage(valueText)) {
          result.addAll(PACKAGE_BASE_NAMES);
          List<String> namespaceVariants = getVariantsFromNamespaceName(valueText);
          result.addAll(namespaceVariants);
          if (!namespaceVariants.isEmpty()) {
            return namespaceVariants.get(0);
          }
        }

        // this probably should be the last
        String independentPath = FileUtil.toSystemIndependentName(valueText);
        if (PerlString.looksLikePath(independentPath)) {
          result.addAll(BASE_PATH_NAMES);
          List<String> pathVariants = getVariantsFromPath(independentPath);
          result.addAll(pathVariants);
          if (!pathVariants.isEmpty()) {
            return pathVariants.get(0);
          }
        }
      }

      return STRING;
    }
    else if (expression instanceof PsiPerlNumberConstant) {
      result.add(NUMBER);
      return NUMBER;
    }
    else if (expression instanceof PerlRegexExpression) {
      result.addAll(REGEX_BASE_NAMES);
      return PATTERN;
    }
    return null;
  }

  @NotNull
  private static List<String> getVariantsFromNamespaceName(@NotNull String packageName) {
    List<String> packageChunks = PerlPackageUtil.split(packageName);
    if (packageChunks.isEmpty()) {
      return Collections.emptyList();
    }
    int chunksNumber = Math.min(packageChunks.size(), PACKAGE_CHUNKS_TO_USE);
    List<String> result = new ArrayList<>(chunksNumber);
    for (int i = 1; i <= chunksNumber; i++) {
      String baseName = join(packageChunks.subList(chunksNumber - i, chunksNumber));
      result.add(baseName);
    }
    return ContainerUtil.filter(result, PerlNamesValidator::isIdentifier);
  }

  @NotNull
  private static List<String> getVariantsFromPath(@NotNull String pathName) {
    File file = new File(pathName);
    String fileName = file.getName();
    if (StringUtil.isEmptyOrSpaces(fileName)) {
      return Collections.emptyList();
    }

    List<String> fileNameParts = StringUtil.split(StringUtil.trimLeading(fileName, '.'), ".");
    int chunksNumber = Math.min(fileNameParts.size(), FILE_CHUNKS_TO_USE);
    List<String> result = new ArrayList<>();
    for (int i = 1; i <= chunksNumber; i++) {
      String baseFileName = join(fileNameParts.subList(0, i));
      result.add(baseFileName);
      if (file.isAbsolute()) {
        result.add(join(ABSOLUTE_PATH_TO, baseFileName));
      }
      else {
        result.add(join(RELATIVE_PATH_TO, baseFileName));
      }
      result.add(join(PATH_TO, baseFileName));
    }
    return ContainerUtil.filter(result, PerlNamesValidator::isIdentifier);
  }

  /**
   * Fills {@code result} with names suggested for the {@code targetElement} and returns the recommended name
   */
  @NotNull
  public static String suggestAndGetRecommendedName(@NotNull PsiElement expression, @NotNull Set<String> result) {
    return StringUtil.notNullize(Objects.requireNonNull(EP_NAME.findExtension(PerlNameSuggestionProvider.class))
                                   .suggestAndAddRecommendedName(expression, result), "new_variable_name");
  }

  public static void suggestNames(@NotNull PerlVariableDeclarationElement variableDeclarationElement, @NotNull Set<String> result) {
    Objects.requireNonNull(EP_NAME.findExtension(PerlNameSuggestionProvider.class))
      .suggestAndAddRecommendedName((PsiElement)variableDeclarationElement, null, result);
  }

  @NotNull
  private static String join(@NotNull String... source) {
    return join(Arrays.asList(source));
  }

  @NotNull
  private static String join(@NotNull List<String> source) {
    return StringUtil.join(ContainerUtil.map(source, it -> it.toLowerCase(Locale.getDefault())), "_");
  }

  @NotNull
  private static String spacedToSnakeCase(@NotNull String source) {
    return StringUtil.join(source.toLowerCase(Locale.getDefault()).split("\\s+"), "_");
  }
}
