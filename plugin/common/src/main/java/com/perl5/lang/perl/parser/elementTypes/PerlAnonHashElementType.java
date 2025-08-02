/*
 * Copyright 2015-2025 Alexandr Evstigneev
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
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.perl.lexer.PerlLexer;
import com.perl5.lang.perl.lexer.PerlTemplatingLexer;
import org.jetbrains.annotations.NotNull;

import static com.perl5.lang.perl.parser.PerlElementTypesGenerated.*;

public class PerlAnonHashElementType extends PerlBracedBlockElementType {
  public PerlAnonHashElementType() {
    super("ANON_HASH");
  }

  @Override
  protected @NotNull IElementType getOpeningBraceType() {
    return LEFT_BRACE;
  }

  @Override
  protected boolean isLexerStateOk(int lexerState) {
    return PerlTemplatingLexer.getPerlLexerState(lexerState) == PerlLexer.AFTER_RIGHT_BRACE;
  }

  @Override
  protected boolean isParentOk(@NotNull ASTNode parent) {
    if (!super.isParentOk(parent)) {
      return false;
    }

    IElementType parentType = PsiUtilCore.getElementType(parent);

    // explicitly, like +{}
    if (parentType == PREFIX_UNARY_EXPR) {
      return true;
    }

    // checking we are first in call args, comma sequence or statement
    if (PsiUtilCore.getElementType(parent.getFirstChildNode()) != this) {
      return true;
    }

    // block and file level anon hash
    if (parentType == STATEMENT) {
      return false;
    }

    // moving up to call arguments
    if (parentType == COMMA_SEQUENCE_EXPR) {
      parent = parent.getTreeParent();
      parentType = PsiUtilCore.getElementType(parent);
    }

    return parentType != CALL_ARGUMENTS;
  }
}
