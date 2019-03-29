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

import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.ElementManipulators;
import com.intellij.psi.PsiElement;
import com.intellij.psi.codeStyle.SuggestedNameInfo;
import com.intellij.refactoring.rename.NameSuggestionProvider;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.idea.PerlNamesValidator;
import com.perl5.lang.perl.idea.intellilang.PerlInjectionMarkersService;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
    if (assignee instanceof PerlString) {
      result.add(STRING);
      if (assignee instanceof PsiPerlStringXq) {
        result.add(COMMAND_OUTPUT);
        return COMMAND_OUTPUT;
      }
      if (assignee.getChildren().length == 0) {
        String valueText = ElementManipulators.getValueText(assignee);
        if (valueText.length() < 30) {
          String variableNameFromString = PerlPsiUtil.toSnakeCase(valueText);
          if (PerlNamesValidator.isIdentifier(variableNameFromString)) {
            result.add(variableNameFromString);
            return variableNameFromString;
          }
        }
      }

      return STRING;
    }
    else if (assignee instanceof PsiPerlNumberConstant) {
      result.add(NUMBER);
      return NUMBER;
    }
    return null;
  }


  /**
   * Fills {@code result} with names suggested for the {@code targetElement} and returns the recommended name
   */
  @NotNull
  public static String suggestAndGetRecommendedName(@NotNull PsiElement targetElement, @NotNull Set<String> result) {
    return StringUtil.notNullize(Objects.requireNonNull(EP_NAME.findExtension(PerlNameSuggestionProvider.class))
                                   .suggestAndAddRecommendedName(targetElement, null, result), "new_variable_name");
  }

}
