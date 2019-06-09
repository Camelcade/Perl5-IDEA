/*
 * Copyright 2015-2019 Alexandr Evstigneev
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

package com.perl5.lang.perl.psi.utils;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.perl.psi.PerlVariableDeclarationExpr;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.*;
import static com.perl5.lang.perl.lexer.PerlTokenSets.VARIABLE_DECLARATIONS;


public enum PerlContextType {
  VOID,
  SCALAR,
  LIST;

  private static final Logger LOG = Logger.getInstance(PerlContextType.class);

  private static final TokenSet LIST_CONTEXT_ELEMENTS = TokenSet.create(
    ARRAY_VARIABLE, HASH_VARIABLE, ARRAY_CAST_EXPR, HASH_CAST_EXPR, COMMA_SEQUENCE_EXPR, STRING_LIST, PARENTHESISED_EXPR, ARRAY_SLICE,
    HASH_SLICE
  );

  public static boolean isVoid(@Nullable PsiElement element) {
    return from(element) == VOID;
  }

  public static boolean isList(@Nullable PsiElement element) {
    return from(element) == LIST;
  }

  public static boolean isScalar(@Nullable PsiElement element) {
    return from(element) == SCALAR;
  }

  /**
   * @return context of the {@code element}
   */
  @NotNull
  public static PerlContextType from(@Nullable PsiElement element) {
    if (element == null) {
      return VOID;
    }
    IElementType elementType = PsiUtilCore.getElementType(element);

    if (VARIABLE_DECLARATIONS.contains(elementType)) {
      if (((PerlVariableDeclarationExpr)element).isParenthesized()) {
        return LIST;
      }
      PsiElement[] children = element.getChildren();
      return children.length == 0 ? VOID : from(children[0]);
    }
    else if (LIST_CONTEXT_ELEMENTS.contains(elementType)) {
      return LIST;
    }
    else if (elementType == VARIABLE_DECLARATION_ELEMENT) {
      PsiElement[] children = element.getChildren();
      if (children.length > 2) {
        LOG.error("Don't know how to unwrap multiple children: " + element.getText());
        return LIST;
      }
      if (children.length == 0) {
        return VOID;
      }
      return from(children[0]);
    }

    return SCALAR;
  }
}
