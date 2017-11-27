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

package com.perl5.lang.perl.idea.completion;

import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.perl.idea.PerlElementPatterns;
import com.perl5.lang.perl.idea.completion.providers.*;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.PsiPerlStringList;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 25.04.2015.
 */
public class PerlCompletionContributor extends CompletionContributor implements PerlElementTypes, PerlElementPatterns {
  private static final TokenSet AUTO_OPENED_TOKENS = TokenSet.create(
    RESERVED_USE,
    RESERVED_NO,
    RESERVED_PACKAGE,
    ANNOTATION_RETURNS_KEY,
    ANNOTATION_TYPE_KEY
  );

  public PerlCompletionContributor() {
    extend(
      CompletionType.BASIC,
      STRING_CONTENT_PATTERN,
      new PerlStringContentCompletionProvider()
    );

    extend(
      CompletionType.BASIC,
      LABEL_PATTERN,
      new PerlLabelCompletionProvider()
    );

    extend(
      CompletionType.BASIC,
      NAMESPACE_NAME_PATTERN,
      new PerlPackageCompletionProvider()
    );

    extend(
      CompletionType.BASIC,
      SUB_NAME_PATTERN,
      new PerlSubNameElementCompletionProvider()
    );

    extend(
      CompletionType.BASIC,
      VARIABLE_NAME_PATTERN,
      new PerlVariableNameCompletionProvider()
    );

    // refactored
    extend(
      CompletionType.BASIC,
      SUB_NAME_PATTERN.and(IN_STATIC_METHOD_PATTERN),
      new PerlSubBuiltInCompletionProvider()
    );

    extend(
      CompletionType.BASIC,
      SUB_NAME_PATTERN.and(IN_STATIC_METHOD_PATTERN),
      new PerlImportedSubsCompletionProvider()
    );

    // refactored
    extend(
      CompletionType.BASIC,
      SUB_NAME_PATTERN.and(IN_STATIC_METHOD_PATTERN),
      new PerlSubStaticCompletionProvider()
    );

    // refactored
    extend(
      CompletionType.BASIC,
      SUB_NAME_PATTERN.and(IN_OBJECT_METHOD_PATTERN),
      new PerlSubMethodCompletionProvider()
    );

    // refactored, adds packages when it's appropriate
    extend(
      CompletionType.BASIC,
      SUB_NAME_PATTERN.inside(METHOD_PATTERN),
      new PerlPackageSubCompletionProvider()
    );

    // refactored
    extend(
      CompletionType.BASIC,
      UNKNOWN_ANNOTATION_PATTERN,
      new PerlAnnotationCompletionProvider()
    );
  }

  @Override
  public void fillCompletionVariants(@NotNull CompletionParameters parameters, @NotNull CompletionResultSet result) {
    super.fillCompletionVariants(parameters, result);
  }

  @Override
  public boolean invokeAutoPopup(@NotNull PsiElement element, char typedChar) {
    IElementType elementType = PsiUtilCore.getElementType(element);

    if (typedChar == '>' && elementType == OPERATOR_MINUS) {
      return true;
    }
    else if (typedChar == ':' && elementType == COLON) {
      return true;
    }
    else if (typedChar == ' ' && (
      AUTO_OPENED_TOKENS.contains(elementType)) || element.getParent() instanceof PsiPerlStringList) {
      return true;
    }
    else if (StringUtil.containsChar("$@%#", typedChar)) {
      return true;
    }

    return super.invokeAutoPopup(element, typedChar);
  }
}
