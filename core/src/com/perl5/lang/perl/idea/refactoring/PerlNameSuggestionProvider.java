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
import com.intellij.psi.ElementManipulator;
import com.intellij.psi.ElementManipulators;
import com.intellij.psi.PsiElement;
import com.intellij.psi.codeStyle.SuggestedNameInfo;
import com.intellij.refactoring.rename.NameSuggestionProvider;
import com.intellij.util.ObjectUtils;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.idea.PerlNamesValidator;
import com.perl5.lang.perl.idea.intellilang.PerlInjectionMarkersService;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.impl.PsiPerlBlockImpl;
import com.perl5.lang.perl.psi.mixins.PerlStatementMixin;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.*;

/**
 * Created by hurricup on 12.06.2015.
 */
public class PerlNameSuggestionProvider implements NameSuggestionProvider {
  private static final String ANON = "anon";
  private static final String REFERENCE = "reference";
  private static final String REF = "ref";
  private static final String SCALAR = "scalar";
  private static final String VALUE = "value";
  private static final List<String> SCALAR_BASE_NAMES = Arrays.asList(SCALAR, VALUE);
  private static final String ARRAY = "array";
  private static final String ANON_ARRAY = Objects.requireNonNull(join(ANON, ARRAY));
  private static final String ARRAY_REF = Objects.requireNonNull(join(ARRAY, REF));
  private static final String LIST = "list";
  private static final List<String> ARRAY_BASE_NAMES = Arrays.asList(ARRAY, LIST);
  private static final List<String> ANON_ARRAY_BASE_NAMES = ContainerUtil.append(ARRAY_BASE_NAMES, ANON_ARRAY, ARRAY_REF);
  private static final String HASH = "hash";
  private static final String ANON_HASH = Objects.requireNonNull(join(ANON, HASH));
  private static final String HASH_REF = Objects.requireNonNull(join(HASH, REF));
  private static final String MAP = "map";
  private static final String DICT = "dict";
  private static final List<String> HASH_BASE_NAMES = Arrays.asList(HASH, MAP, DICT);
  private static final List<String> ANON_HASH_BASE_NAMES = ContainerUtil.append(HASH_BASE_NAMES, ANON_HASH, HASH_REF);
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
  private static final String ELEMENT = "element";
  private static final String ITEM = "item";
  private static final String SLICE = "slice";
  private static final int MAX_GENERATED_NAME_LENGTH = 40;
  private static final String SUB = "sub";
  private static final String ANON_SUB = Objects.requireNonNull(join(ANON, SUB));
  private static final String LAMBDA = "lambda";
  private static final List<String> BASE_ANON_SUB_NAMES = Arrays.asList(ANON_SUB, LAMBDA);
  private static final String RESULT = "result";
  private static final String DO_RESULT = "do_result";
  private static final String EVAL_RESULT = "eval_result";
  private static final String SORTED = "sorted";
  private static final String MAPPED = "mapped";
  private static final String GREPPED = "filtered";

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
    String recommendation = null;
    if (expression instanceof PerlString) {
      recommendation = suggestAndAddNameForString((PerlString)expression, result);
    }
    else if (expression instanceof PsiPerlHashElement) {
      String resultString = Objects.requireNonNull(join(HASH, ELEMENT));
      result.add(resultString);
      result.add(join(HASH, ITEM));
      recommendation = suggestNamesForElements(((PsiPerlHashElement)expression).getExpr(),
                                               false, ((PsiPerlHashElement)expression).getHashIndex().getExpr(),
                                               result,
                                               resultString);
    }
    else if (expression instanceof PsiPerlArrayElement) {
      String resultString = Objects.requireNonNull(join(ARRAY, ELEMENT));
      result.add(resultString);
      result.add(join(ARRAY, ITEM));
      recommendation = suggestNamesForElements(((PsiPerlArrayElement)expression).getExpr(),
                                               false, ((PsiPerlArrayElement)expression).getArrayIndex().getExpr(),
                                               result,
                                               resultString);
    }
    else if (expression instanceof PerlDerefExpression) {
      recommendation = suggestAndGetForDereference(expression, result, recommendation);
    }
    else if (expression instanceof PerlGrepExpr) {
      recommendation = suggestAndGetGrepMapSortNames(expression, GREPPED, result);
    }
    else if (expression instanceof PerlMapExpr) {
      recommendation = suggestAndGetGrepMapSortNames(expression, MAPPED, result);
    }
    else if (expression instanceof PerlSortExpr) {
      recommendation = suggestAndGetGrepMapSortNames(expression, SORTED, result);
    }
    else if (expression instanceof PerlSubExpr) {
      result.addAll(BASE_ANON_SUB_NAMES);
      recommendation = getBaseName(expression);
    }
    else if (expression instanceof PerlRegexExpression) {
      result.addAll(REGEX_BASE_NAMES);
      recommendation = getBaseName(expression);
    }
    else if (expression instanceof PerlDoExpr) {
      recommendation = getBaseName(expression);
    }
    else if (expression instanceof PerlEvalExpr) {
      recommendation = getBaseName(expression);
    }
    else if (expression instanceof PsiPerlAnonArray) {
      result.addAll(ANON_HASH_BASE_NAMES);
      recommendation = getBaseName(expression);
    }
    else if (expression instanceof PsiPerlAnonHash) {
      result.addAll(ANON_ARRAY_BASE_NAMES);
      recommendation = getBaseName(expression);
    }
    else if (expression instanceof PsiPerlNumberConstant) {
      recommendation = getBaseName(expression);
    }
    else if (expression instanceof PerlCastExpression) {
      recommendation = getBaseName(expression);
    }
    else if( expression instanceof PsiPerlArraySlice){
      recommendation = getBaseName(expression);
    }
    else if( expression instanceof PsiPerlHashSlice){
      recommendation = getBaseName(expression);
    }
    /*
    else if( expression instanceof PsiPerlArrayIndexVariable){

    }
    else if( expression instanceof SubCallExpr){

    }
    */
    ContainerUtil.addIfNotNull(result, recommendation);
    return recommendation;
  }

  private String suggestAndGetForDereference(@NotNull PsiElement expression,
                                             @NotNull Set<String> result, String recommendation) {
    PsiElement[] children = expression.getChildren();
    PsiElement element = children[children.length - 1];
    PsiElement baseElement = children[children.length - 2];

    if (element instanceof PsiPerlHashIndex) {
      recommendation = Objects.requireNonNull(join(HASH, ELEMENT));
      result.add(recommendation);
      result.add(join(HASH, ITEM));

      recommendation = suggestNamesForElements(baseElement, true, ((PsiPerlHashIndex)element).getExpr(), result, recommendation);
    }
    else if (element instanceof PsiPerlArrayIndex) {
      recommendation = Objects.requireNonNull(join(ARRAY, ELEMENT));
      result.add(recommendation);
      result.add(join(ARRAY, ITEM));

      recommendation = suggestNamesForElements(baseElement, true, ((PsiPerlArrayIndex)element).getExpr(), result, recommendation);
    }
    else if (element instanceof PsiPerlParenthesisedCallArguments) {
      recommendation = join(getBaseName(baseElement), RESULT);
    }
    return recommendation;
  }

  private String suggestAndGetGrepMapSortNames(@Nullable PsiElement expression,
                                               @NotNull String prefix,
                                               @NotNull Set<String> result) {
    String recommendedName = join(prefix, VALUE);
    result.add(recommendedName);
    String fullName = getBaseName(expression);
    if (StringUtil.isNotEmpty(fullName)) {
      recommendedName = fullName;
      result.add(fullName);
    }
    return recommendedName;
  }

  private static String suggestAndAddNameForString(@NotNull PerlString expression, @NotNull Set<String> result) {
    result.add(STRING);
    if (expression instanceof PsiPerlStringXq) {
      result.add(COMMAND_OUTPUT);
      return COMMAND_OUTPUT;
    }
    if (expression.getChildren().length == 0) {
      String nameFromManipulator = getNameFromManipulator(expression);
      if (nameFromManipulator != null) {
        result.add(nameFromManipulator);
        return nameFromManipulator;
      }
      String valueText = ElementManipulators.getValueText(expression);

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

  @Nullable
  @Contract("null->null")
  private static String getBaseName(@Nullable PsiElement element) {
    if (element == null) {
      return null;
    }
    String nameFromKey = getNameFromManipulator(element);
    if (StringUtil.isNotEmpty(nameFromKey)) {
      return nameFromKey;
    }
    if (element instanceof PerlVariable) {
      return join(((PerlVariable)element).getName());
    }
    else if (element instanceof PerlSubExpr) {
      return ANON_SUB;
    }
    else if (element instanceof PerlDoExpr) {
      return DO_RESULT;
    }
    else if (element instanceof PerlEvalExpr) {
      return EVAL_RESULT;
    }
    else if (element instanceof PerlGrepExpr) {
      return join(GREPPED, getBaseName(((PerlGrepExpr)element).getExpr()));
    }
    else if (element instanceof PerlMapExpr) {
      return join(MAPPED, getBaseName(((PerlMapExpr)element).getExpr()));
    }
    else if (element instanceof PerlSortExpr) {
      return join(SORTED, getBaseName(((PerlSortExpr)element).getTarget()));
    }
    else if (element instanceof PsiPerlAnonArray) {
      return ANON_ARRAY;
    }
    else if (element instanceof PsiPerlAnonHash) {
      return ANON_HASH;
    }
    else if (element instanceof PsiPerlNumberConstant) {
      return NUMBER;
    }
    else if (element instanceof PerlRegexExpression) {
      return PATTERN;
    }
    else if (element instanceof PerlCastExpression) {
      PsiPerlExpr targetExpr = ((PerlCastExpression)element).getExpr();
      PsiPerlBlock targetBlock = ((PerlCastExpression)element).getBlock();
      return derefName(getBaseName(targetExpr == null ? targetBlock : targetExpr));
    }
    else if (element instanceof PsiPerlArraySlice) {
      return join(getBaseName(((PsiPerlArraySlice)element).getExpr()), SLICE);
    }
    else if (element instanceof PsiPerlHashSlice) {
      return join(getBaseName(((PsiPerlHashSlice)element).getExpr()), SLICE);
    }
    else if (element instanceof PsiPerlBlockImpl) {
      PsiElement[] blockChildren = element.getChildren();
      if (blockChildren.length == 1) {
        PsiElement perlStatement = blockChildren[0];
        if (perlStatement instanceof PerlStatementMixin &&
            !((PerlStatementMixin)perlStatement).hasModifier()) {
          return getBaseName(((PsiPerlStatement)perlStatement).getExpr());
        }
      }
    }

    return null;
  }

  /**
   * Suggests names for hash/array elements
   *
   * @param baseExpr       variable from index expression
   * @param derefBase      if true, base name going to be stripped from ref/reference
   * @param indexExpr      expressions from index expression
   * @param result         result to add recommendations to
   * @param recommendation base recommendation name
   * @return recommended name
   */
  @NotNull
  private static String suggestNamesForElements(@Nullable PsiElement baseExpr,
                                                boolean derefBase,
                                                @Nullable PsiElement indexExpr,
                                                @NotNull Set<String> result,
                                                @NotNull String recommendation) {
    String singularName = null;
    String baseExprName = getBaseName(baseExpr);
    if (derefBase) {
      baseExprName = derefName(baseExprName);
    }

    if (StringUtil.isNotEmpty(baseExprName)) {
      singularName = ObjectUtils.notNull(StringUtil.unpluralize(baseExprName), baseExprName);
      String variableNameElement = join(singularName, ELEMENT);
      if (StringUtil.isNotEmpty(variableNameElement)) {
        result.add(variableNameElement);
        recommendation = variableNameElement;
      }
      ContainerUtil.addIfNotNull(result, join(singularName, ITEM));
    }

    String nameFromKey = getBaseName(indexExpr);
    if (indexExpr instanceof PerlString) {
      recommendation = nameFromKey;
    }

    if (StringUtil.isNotEmpty(nameFromKey) && !(indexExpr instanceof PsiPerlNumberConstant)) {
      result.add(nameFromKey);
      if (StringUtil.isNotEmpty(singularName)) {
        String join = join(singularName, nameFromKey);
        ContainerUtil.addIfNotNull(result, join);
      }
    }
    return recommendation;
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
      ContainerUtil.addIfNotNull(result, baseName);
    }
    return result;
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
      if (baseFileName != null) {
        result.add(baseFileName);
        if (file.isAbsolute()) {
          ContainerUtil.addIfNotNull(result, join(ABSOLUTE_PATH_TO, baseFileName));
        }
        else {
          ContainerUtil.addIfNotNull(result, join(RELATIVE_PATH_TO, baseFileName));
        }
        ContainerUtil.addIfNotNull(result, join(PATH_TO, baseFileName));
      }
    }
    return result;
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

  @Nullable
  private static String join(@NotNull String... source) {
    return join(Arrays.asList(source));
  }

  @Nullable
  private static String join(@NotNull List<String> source) {
    for (String element : source) {
      if (StringUtil.isEmptyOrSpaces(element)) {
        return null;
      }
    }
    return validateName(StringUtil.join(ContainerUtil.map(source, it -> it.toLowerCase(Locale.getDefault())), "_"));
  }

  @Nullable
  private static String spacedToSnakeCase(@NotNull String source) {
    return join(source.toLowerCase(Locale.getDefault()).split("\\s+"));
  }

  @Contract("null->null")
  @Nullable
  private static String validateName(@Nullable String name) {
    return StringUtil.isNotEmpty(name) && name.length() <= MAX_GENERATED_NAME_LENGTH && PerlNamesValidator.isIdentifier(name) ? name : null;
  }

  @Contract("null->null")
  @Nullable
  private static String getNameFromManipulator(@Nullable PsiElement element) {
    if (element == null) {
      return null;
    }
    ElementManipulator<PsiElement> manipulator = ElementManipulators.getManipulator(element);
    if (manipulator == null) {
      return null;
    }
    return spacedToSnakeCase(ElementManipulators.getValueText(element));
  }

  @Nullable
  @Contract("null->null")
  private static String derefName(@Nullable String name) {
    return removeChunk(removeChunk(name, REFERENCE + "s?"), REF + "s?");
  }

  /**
   * @return original {@code name} with {@code chunk} removed. Chunk removed with underscore prefix
   */
  @Nullable
  @Contract("null,_->null")
  private static String removeChunk(@Nullable String name, @NotNull String chunk) {
    if (name == null) {
      return null;
    }
    return validateName(name.replaceAll("_+" + chunk + "_+", "_").replaceAll("(_+" + chunk + "$|^" + chunk + "_+)", ""));
  }
}
