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

package com.perl5.lang.perl.psi.utils;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.util.PsiUtilCore;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.*;

/**
 * Created by hurricup on 27.06.2016.
 */
public enum PerlContextType {
  VOID,
  SCALAR,
  LIST;

  private static final Logger LOG = Logger.getInstance(PerlContextType.class);

  private static final TokenSet ELEMENTS_TO_UNWRAP = TokenSet.create(
    VARIABLE_DECLARATION_ELEMENT
  );

  private static final TokenSet LIST_CONTEXT_ELEMENTS = TokenSet.create(
    ARRAY_VARIABLE, HASH_VARIABLE, ARRAY_CAST_EXPR, HASH_CAST_EXPR, COMMA_SEQUENCE_EXPR
  );

  /**
   * @return context of the {@code element}
   */
  @NotNull
  public static PerlContextType from(@Nullable PsiElement element) {
    if (element == null) {
      return VOID;
    }
    IElementType elementType = PsiUtilCore.getElementType(element);

    if (LIST_CONTEXT_ELEMENTS.contains(elementType)) {
      return LIST;
    }
    else if (ELEMENTS_TO_UNWRAP.contains(elementType)) {
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
