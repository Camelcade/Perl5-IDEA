/*
 * Copyright 2015-2020 Alexandr Evstigneev
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

package com.perl5.lang.perl.parser.elementTypes;

import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.lexer.FlexAdapter;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.perl.lexer.PerlLexer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.*;
import static com.perl5.lang.perl.psi.stubs.PerlStubElementTypes.USE_STATEMENT;

public class PerlReparseableStringListElementType extends PerlReparseableQuoteLikeElementType {
  public PerlReparseableStringListElementType(@NotNull String debugName,
                                              @NotNull Class<? extends PsiElement> clazz) {
    super(debugName, clazz);
  }

  @Override
  public boolean isParsable(@Nullable ASTNode parent,
                            @NotNull CharSequence buffer,
                            @NotNull Language fileLanguage,
                            @NotNull Project project) {
    if (PsiUtilCore.getElementType(parent) == USE_STATEMENT) {
      @NotNull ASTNode[] children = parent.getChildren(TokenSet.create(PACKAGE));
      if (children.length == 1 && StringUtil.equals("vars", children[0].getChars())) {
        return false;
      }
    }

    FlexAdapter flexAdapter = new FlexAdapter(new PerlLexer(null).withProject(project));
    flexAdapter.start(buffer);

    IElementType firstTokenType = flexAdapter.getTokenType();
    if (firstTokenType != RESERVED_QW) {
      return false;
    }

    flexAdapter.advance();
    skipSpaces(flexAdapter);
    if (flexAdapter.getTokenType() != QUOTE_SINGLE_OPEN) {
      return false;
    }
    flexAdapter.advance();
    if (flexAdapter.getTokenType() == LP_STRING_QW) {
      flexAdapter.advance();
    }
    if (flexAdapter.getTokenType() != QUOTE_SINGLE_CLOSE) {
      return false;
    }
    flexAdapter.advance();
    return flexAdapter.getTokenType() == null;
  }
}
