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
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.perl.lexer.PerlTokenSets;
import com.perl5.lang.perl.psi.impl.PerlSubNameElementImpl;
import org.jetbrains.annotations.NotNull;

import static com.perl5.lang.perl.lexer.PerlElementTypes.SUB_DECLARATION;
import static com.perl5.lang.perl.parser.PerlElementTypesGenerated.*;

public class PerlSubNameTokenType extends PerlReparseableTokenType {
  public PerlSubNameTokenType(@NotNull String debugName) {
    super(debugName, PerlSubNameElementImpl.class);
  }

  @Override
  protected @NotNull TextRange getLexerConfirmationRange(@NotNull ASTNode leaf) {
    ASTNode parent = leaf.getTreeParent();
    IElementType parentType = PsiUtilCore.getElementType(parent);
    if (parentType == SUB_DECLARATION ||
        PerlTokenSets.SUB_DEFINITIONS_TOKENSET.contains(parentType) ||
        PerlTokenSets.MODIFIER_DECLARATIONS_TOKENSET.contains(parentType)) {
      return TextRange.create(parent.getStartOffset(), leaf.getTextRange().getEndOffset());
    }
    if (parentType == METHOD) {
      ASTNode callElement = parent.getTreeParent();
      IElementType callType = PsiUtilCore.getElementType(callElement);
      if (callType == SUB_CALL || callType == CODE_VARIABLE) {
        ASTNode derefExpr = callElement.getTreeParent();
        if (PsiUtilCore.getElementType(derefExpr) == DEREF_EXPR) {
          return TextRange.create(derefExpr.getStartOffset(), leaf.getTextRange().getEndOffset());
        }
        return TextRange.create(callElement.getStartOffset(), leaf.getTextRange().getEndOffset());
      }
    }
    return TextRange.EMPTY_RANGE;
  }
}
