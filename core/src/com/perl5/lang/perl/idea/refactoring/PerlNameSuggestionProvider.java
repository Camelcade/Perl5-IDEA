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

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.ElementManipulator;
import com.intellij.psi.ElementManipulators;
import com.intellij.psi.PsiElement;
import com.intellij.psi.codeStyle.SuggestedNameInfo;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.refactoring.rename.NameSuggestionProvider;
import com.intellij.util.ObjectUtils;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.idea.PerlNamesValidator;
import com.perl5.lang.perl.idea.intellilang.PerlInjectionMarkersService;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.lexer.PerlTokenSets;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.mixins.PerlStatementMixin;
import com.perl5.lang.perl.psi.properties.PerlLexicalScope;
import com.perl5.lang.perl.psi.utils.PerlResolveUtil;
import com.perl5.lang.perl.psi.utils.PerlVariableType;
import com.perl5.lang.perl.util.PerlPackageUtil;
import com.perl5.lang.perl.util.PerlSubUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.*;
import java.util.function.Function;

import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.*;
import static com.perl5.lang.perl.lexer.PerlTokenSets.*;

/**
 * Created by hurricup on 12.06.2015.
 */
public class PerlNameSuggestionProvider implements NameSuggestionProvider {
  private static final Logger LOG = Logger.getInstance(PerlNameSuggestionProvider.class);
  private static final String EXPRESSION = "expression";
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
  private static final String CODE_REF = Objects.requireNonNull(join("code", REF));
  private static final String CODE_REFERENCE = Objects.requireNonNull(join("code", REFERENCE));
  private static final List<String> BASE_ANON_SUB_NAMES = Arrays.asList(ANON_SUB, LAMBDA, CODE_REF, CODE_REFERENCE);
  private static final String RESULT = "result";
  private static final String DO_RESULT = "do_result";
  private static final String EVAL_RESULT = "eval_result";
  private static final String SORTED = "sorted";
  private static final String MAPPED = "mapped";
  private static final String GREPPED = "filtered";
  private static final String STRING_LIST_NAME = "string_list";

  private static final Map<IElementType, String> FIXED_NAMES;
  private static final TokenSet ELEMENTS_WITH_BASE_NAMES;

  static {
    Map<IElementType, String> namesMap = new HashMap<>();
    namesMap.put(STRING_LIST, STRING_LIST_NAME);
    namesMap.put(COMMA_SEQUENCE_EXPR, LIST);
    namesMap.put(SUB_EXPR, CODE_REF);
    namesMap.put(DO_EXPR, DO_RESULT);
    namesMap.put(EVAL_EXPR, EVAL_RESULT);
    namesMap.put(PerlElementTypes.ANON_ARRAY, ANON_ARRAY);
    namesMap.put(PerlElementTypes.ANON_HASH, ANON_HASH);
    namesMap.put(NUMBER_CONSTANT, NUMBER);
    FIXED_NAMES = Collections.unmodifiableMap(namesMap);

    ELEMENTS_WITH_BASE_NAMES = TokenSet.orSet(
      TokenSet.create(FIXED_NAMES.keySet().toArray(IElementType.EMPTY_ARRAY)),
      PerlTokenSets.CAST_EXPRESSIONS,
      PerlTokenSets.SLICES,
      REGEX_OPERATIONS
    );
  }


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
      recommendedName = adjustNamesToBeUniqueFor(
        targetElement,
        ((PerlVariableDeclarationElement)targetElement).getActualType(),
        declaredVariable.getName(),
        suggestAndAddRecommendedName((PerlVariableDeclarationElement)targetElement, contextElement, suggestedNames),
        suggestedNames
      );
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
    if (expression instanceof PsiPerlParenthesisedExpr) {
      return suggestAndAddRecommendedName(((PsiPerlParenthesisedExpr)expression).getExpr(), result);
    }
    String recommendation = null;
    IElementType expressionType = PsiUtilCore.getElementType(expression);
    if (STRINGS.contains(expressionType)) {
      recommendation = suggestAndAddNameForString((PerlString)expression, result);
    }
    else if (expressionType == HASH_ELEMENT) {
      String resultString = Objects.requireNonNull(join(HASH, ELEMENT));
      result.add(resultString);
      result.add(join(HASH, ITEM));
      recommendation = suggestNamesForElements(((PsiPerlHashElement)expression).getExpr(),
                                               false, ((PsiPerlHashElement)expression).getHashIndex().getExpr(),
                                               result,
                                               resultString);
    }
    else if (expressionType == ARRAY_ELEMENT) {
      String resultString = Objects.requireNonNull(join(ARRAY, ELEMENT));
      result.add(resultString);
      result.add(join(ARRAY, ITEM));
      recommendation = suggestNamesForElements(((PsiPerlArrayElement)expression).getExpr(),
                                               false, ((PsiPerlArrayElement)expression).getArrayIndex().getExpr(),
                                               result,
                                               resultString);
    }
    else if (expressionType == DEREF_EXPR) {
      recommendation = suggestAndGetForDereference(expression, result, recommendation);
    }
    else if (expressionType == GREP_EXPR) {
      recommendation = suggestAndGetGrepMapSortNames(expression, GREPPED, result);
    }
    else if (expressionType == MAP_EXPR) {
      recommendation = suggestAndGetGrepMapSortNames(expression, MAPPED, result);
    }
    else if (expressionType == SORT_EXPR) {
      recommendation = suggestAndGetGrepMapSortNames(expression, SORTED, result);
    }
    else if (CALLS.contains(expressionType)) {
      recommendation = suggestAndGetForCall((PsiPerlSubCallExpr)expression, result, recommendation);
    }
    else if (expressionType == SUB_EXPR) {
      result.addAll(BASE_ANON_SUB_NAMES);
    }
    else if (REGEX_OPERATIONS.contains(expressionType)) {
      result.addAll(REGEX_BASE_NAMES);
    }
    else if (expressionType == PerlElementTypes.ANON_HASH) {
      result.addAll(ANON_HASH_BASE_NAMES);
    }
    else if (expressionType == PerlElementTypes.ANON_ARRAY) {
      result.addAll(ANON_ARRAY_BASE_NAMES);
    }

    if (ELEMENTS_WITH_BASE_NAMES.contains(expressionType)) {
      recommendation = getBaseName(expression);
    }
    /*
    qw/list/
    (1,2,3)
    for my $var (@array){
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
    else if (element instanceof PsiPerlNestedCall) {
      recommendation = suggestAndGetForCall((PsiPerlNestedCall)element, result, recommendation);
    }
    else if (element instanceof PsiPerlParenthesisedCallArguments) {
      recommendation = join(getBaseName(baseElement), RESULT);
    }
    return recommendation;
  }

  private static String suggestAndGetForCall(@NotNull PsiPerlSubCallExpr expression, @NotNull Set<String> result, String recommendation) {
    PsiPerlMethod method = expression.getMethod();
    if (method != null) {
      PerlSubNameElement subNameElement = method.getSubNameElement();
      if (subNameElement != null) {
        String subName = subNameElement.getName();
        if ("new".equals(subName)) {
          List<String> variantsFromObjectClass = getVariantsFromNamespaceName(subNameElement.getPackageName());
          result.addAll(variantsFromObjectClass);
          if (!variantsFromObjectClass.isEmpty()) {
            recommendation = variantsFromObjectClass.get(0);
          }
        }
        else {
          String normalizedName = join(subName.replaceAll("^(_+|get_*|set_*)", ""));
          if (StringUtil.isNotEmpty(normalizedName)) {
            result.add(normalizedName);
            recommendation = normalizedName;
          }

          result.addAll(getVariantsFromNamespaceName(PerlSubUtil.getMethodReturnValue(expression)));
        }
      }
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

    IElementType elementType = PsiUtilCore.getElementType(element);

    String fixedName = FIXED_NAMES.get(elementType);
    if (fixedName != null) {
      return fixedName;
    }
    else if (elementType == GREP_EXPR) {
      return join(GREPPED, getBaseName(((PerlGrepExpr)element).getExpr()));
    }
    else if (elementType == MAP_EXPR) {
      return join(MAPPED, getBaseName(((PerlMapExpr)element).getExpr()));
    }
    else if (elementType == SORT_EXPR) {
      return join(SORTED, getBaseName(((PerlSortExpr)element).getTarget()));
    }
    else if (elementType == ARRAY_SLICE) {
      return join(getBaseName(((PsiPerlArraySlice)element).getExpr()), SLICE);
    }
    else if (elementType == HASH_SLICE) {
      return join(getBaseName(((PsiPerlHashSlice)element).getExpr()), SLICE);
    }
    else if (elementType == BLOCK) {
      PsiElement[] blockChildren = element.getChildren();
      if (blockChildren.length == 1) {
        PsiElement perlStatement = blockChildren[0];
        if (perlStatement instanceof PerlStatementMixin &&
            !((PerlStatementMixin)perlStatement).hasModifier()) {
          return getBaseName(((PsiPerlStatement)perlStatement).getExpr());
        }
      }
    }
    else if (REGEX_OPERATIONS.contains(elementType)) {
      return PATTERN;
    }
    else if (VARIABLES.contains(elementType)) {
      return join(((PerlVariable)element).getName());
    }
    else if (CAST_EXPRESSIONS.contains(elementType)) {
      PsiPerlExpr targetExpr = ((PerlCastExpression)element).getExpr();
      PsiPerlBlock targetBlock = ((PerlCastExpression)element).getBlock();
      return derefName(getBaseName(targetExpr == null ? targetBlock : targetExpr));
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
  private static List<String> getVariantsFromNamespaceName(@Nullable String packageName) {
    if (StringUtil.isEmptyOrSpaces(packageName)) {
      return Collections.emptyList();
    }
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
  public static String getRecommendedName(@NotNull PsiElement expression,
                                          @NotNull PsiElement contextElement,
                                          @NotNull PerlVariableType variableType) {
    String suggestedName = getInstance().suggestAndAddRecommendedName(expression, new LinkedHashSet<>());
    return StringUtil.notNullize(
      adjustNamesToBeUniqueFor(contextElement, variableType, null, suggestedName, Collections.emptySet()),
      EXPRESSION);
  }

  @NotNull
  private static PerlNameSuggestionProvider getInstance() {
    return Objects.requireNonNull(EP_NAME.findExtension(PerlNameSuggestionProvider.class));
  }

  public static void suggestNames(@NotNull PerlVariableDeclarationElement variableDeclarationElement, @NotNull Set<String> result) {
    getInstance().suggestAndAddRecommendedName((PsiElement)variableDeclarationElement, null, result);
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

  /**
   * Collects existing variables names for the {@code contextElement} and processing names from the {@code names} by adding
   * a number if variable of {@code variableType} with original name exists in scope. Modifying passed names.
   *
   * @return potentially adjusted {@code recommendedName}
   */
  @Nullable
  @Contract("_,_,_,null,_->null")
  public static String adjustNamesToBeUniqueFor(@NotNull PsiElement contextElement,
                                                @NotNull PerlVariableType variableType,
                                                @Nullable String currentName,
                                                @Nullable String recommendedName,
                                                @NotNull Set<String> names) {
    if (names.isEmpty() && recommendedName == null) {
      return recommendedName;
    }
    Set<String> existingNames = collectExistingNames(contextElement, variableType);
    Function<String, String> fun = originalName -> {
      if (originalName == null) {
        return null;
      }
      if (!existingNames.contains(originalName) || originalName.equals(currentName)) {
        return originalName;
      }
      else {
        for (int i = 1; i < Integer.MAX_VALUE; i++) {
          String adjustedName = originalName + i;
          if (!existingNames.contains(adjustedName)) {
            return adjustedName;
          }
        }
        return null;
      }
    };
    if (!names.isEmpty()) {
      LinkedHashSet<String> result = new LinkedHashSet<>();
      names.forEach(it -> result.add(fun.apply(it)));
      names.clear();
      names.addAll(result);
    }
    return fun.apply(recommendedName);
  }

  /**
   * Traverses a subtree after an baseElement and walk ups from the baseElement, collecting all variable names.
   */
  @NotNull
  public static Set<String> collectExistingNames(@Nullable PsiElement baseElement, @NotNull PerlVariableType variableType) {
    if (baseElement == null) {
      return Collections.emptySet();
    }
    Set<String> names = new HashSet<>();
    PsiElement closestScope = PsiTreeUtil.getParentOfType(baseElement, PerlLexicalScope.class, false);
    if (closestScope == null) {
      LOG.error("Unable to find scope for an " + baseElement);
    }
    else {
      PsiElement[] children = closestScope.getChildren();
      PerlRecursiveVisitor recursiveVisitor = new PerlRecursiveVisitor() {
        @Override
        public void visitPerlVariable(@NotNull PerlVariable o) {
          if (o.getActualType() == variableType) {
            ContainerUtil.addIfNotNull(names, o.getName());
          }
        }
      };
      for (int i = children.length - 1; i >= 0; i--) {
        PsiElement child = children[i];
        child.accept(recursiveVisitor);
        if (PsiTreeUtil.isAncestor(child, baseElement, false)) {
          break;
        }
      }
    }

    PerlResolveUtil.treeWalkUp(baseElement, (element, __) -> {
      if (element instanceof PerlVariable && ((PerlVariable)element).getActualType() == variableType) {
        ContainerUtil.addIfNotNull(names, ((PerlVariable)element).getName());
      }
      return true;
    });

    return names;
  }
}
