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
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.TokenType;
import com.intellij.psi.impl.source.tree.TreeUtil;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.perl.lexer.PerlTokenSets;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.perl5.lang.perl.parser.PerlElementTypesGenerated.*;

public class PerlStringContentTokenType extends PerlReparseableTokenType {
  private static final TokenSet CONTAINERS_TO_RELEX_WITH_GRANDPARENT = TokenSet.create(
    TR_SEARCHLIST, TR_REPLACEMENTLIST, REGEX_REPLACEMENT
  );

  public PerlStringContentTokenType(@NotNull String debugName) {
    super(debugName);
  }

  @Override
  protected @NotNull TextRange getLexerConfirmationRange(@NotNull ASTNode leaf) {
    ASTNode parent = leaf.getTreeParent();
    IElementType parentType = PsiUtilCore.getElementType(parent);

    if (parentType == STRING_BARE) {
      ASTNode grandParent = parent.getTreeParent();
      IElementType grandParentType = PsiUtilCore.getElementType(grandParent);
      if (grandParentType == STRING_LIST) {
        return grandParent.getTextRange();
      }
      if (grandParentType == COMMA_SEQUENCE_EXPR) {
        // we need to include =>
        PsiElement nextSibling = PerlPsiUtil.getNextSignificantSibling(parent.getPsi());
        int endOffset = nextSibling == null ? grandParent.getTextRange().getEndOffset() :
                        nextSibling.getTextRange().getEndOffset();

        return TextRange.create(parent.getTextRange().getStartOffset(), endOffset);
      }
      if (grandParentType == HASH_INDEX) {
        ASTNode hashIndexOwner = grandParent.getTreeParent();
        if (hashIndexOwner != null) {
          return TextRange.create(hashIndexOwner.getTextRange().getStartOffset(),
                                  grandParent.getTextRange().getEndOffset());
        }
      }
    }
    else if (PerlTokenSets.QUOTED_STRINGS.contains(parentType)) {
      if (PsiUtilCore.getElementType(parent.getTreeParent()) != HEREDOC_OPENER) {
        return parent.getTextRange();
      }
    }
    else if (CONTAINERS_TO_RELEX_WITH_GRANDPARENT.contains(parentType)) {
      ASTNode grandParent = parent.getTreeParent();
      if (grandParent != null) {
        return grandParent.getTextRange();
      }
    }
    else if (PerlTokenSets.HEREDOC_BODIES_TOKENSET.contains(parentType)) {
      ASTNode heredocOperator = findHeredocOperator(parent);
      if (heredocOperator != null) {
        return TextRange.create(heredocOperator.getStartOffset(), leaf.getTextRange().getEndOffset());
      }
    }
    else if (parentType == ATTRIBUTE) {
      return PerlAttributeIdentifierTokenType.getAttributeConfirmationRange(leaf, parent);
    }
    return TextRange.EMPTY_RANGE;
  }

  private static @Nullable ASTNode findHeredocOperator(@NotNull ASTNode heredocBody) {
    ASTNode run = TreeUtil.prevLeaf(heredocBody);
    if (PsiUtilCore.getElementType(run) != TokenType.WHITE_SPACE || !StringUtil.containsLineBreak(run.getChars())) {
      return null;
    }

    ASTNode firstOpener = null;
    while (true) {
      run = TreeUtil.prevLeaf(run);
      if (PsiUtilCore.getElementType(run) == OPERATOR_HEREDOC) {
        firstOpener = run;
      }
      else if (run == null || StringUtil.containsLineBreak(run.getChars())) {
        break;
      }
    }
    return firstOpener;
  }
}
