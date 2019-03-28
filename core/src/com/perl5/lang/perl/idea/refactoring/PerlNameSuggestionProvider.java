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
import com.intellij.psi.PsiElement;
import com.intellij.psi.codeStyle.SuggestedNameInfo;
import com.intellij.refactoring.rename.NameSuggestionProvider;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.idea.intellilang.PerlInjectionMarkersService;
import com.perl5.lang.perl.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Set;

/**
 * Created by hurricup on 12.06.2015.
 */
public class PerlNameSuggestionProvider implements NameSuggestionProvider {
  private static final String SCALAR = "scalar";
  private static final String ARRAY = "array";
  private static final String LIST = "list";
  private static final String HASH = "hash";

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

    if (targetElement instanceof PerlHeredocOpener) {
      result.addAll(PerlInjectionMarkersService.getInstance(targetElement.getProject()).getSupportedMarkers());
    }
    if (targetElement instanceof PerlVariableDeclarationElement) {
      PerlVariable declaredVariable = ((PerlVariableDeclarationElement)targetElement).getVariable();
      if (declaredVariable instanceof PsiPerlScalarVariable) {
        return suggestAndAddRecommendedName((PsiPerlScalarVariable)declaredVariable, contextElement, result);
      }
      else if (declaredVariable instanceof PsiPerlArrayVariable) {
        return suggestAndAddRecommendedName((PsiPerlArrayVariable)declaredVariable, contextElement, result);
      }
      else if (declaredVariable instanceof PsiPerlHashVariable) {
        return suggestAndAddRecommendedName((PsiPerlHashVariable)declaredVariable, contextElement, result);
      }
    }

    return recommendedName;
  }

  @NotNull
  private String suggestAndAddRecommendedName(@NotNull PsiPerlScalarVariable scalarVariable,
                                              @Nullable PsiElement contextElement,
                                              @NotNull Set<String> result) {
    result.add(SCALAR);
    return SCALAR;
  }

  @NotNull
  private String suggestAndAddRecommendedName(@NotNull PsiPerlArrayVariable arrayVariable,
                                              @Nullable PsiElement contextElement,
                                              @NotNull Set<String> result) {
    result.add(ARRAY);
    return ARRAY;
  }

  @NotNull
  private String suggestAndAddRecommendedName(@NotNull PsiPerlHashVariable hashVariable,
                                              @Nullable PsiElement contextElement,
                                              @NotNull Set<String> result) {
    result.add(HASH);
    return HASH;
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
