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
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.impl.source.tree.TreeUtil;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.perl.psi.impl.PerlNamespaceElementImpl;
import org.jetbrains.annotations.NotNull;

import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.*;
import static com.perl5.lang.perl.psi.stubs.PerlStubElementTypes.NO_STATEMENT;
import static com.perl5.lang.perl.psi.stubs.PerlStubElementTypes.USE_STATEMENT;

public class PerlNamespaceNameTokenType extends PerlReparseableTokenType {
  private static final TokenSet SUFFICIENT_CONTAINERS = TokenSet.create(
    NAMESPACE_DEFINITION,
    VARIABLE_DECLARATION_LEXICAL,
    VARIABLE_DECLARATION_GLOBAL,
    ANNOTATION_RETURNS,
    ANNOTATION_TYPE,
    USE_STATEMENT,
    NO_STATEMENT,
    REQUIRE_EXPR,
    PACKAGE_EXPR
  );

  public PerlNamespaceNameTokenType(@NotNull String debugName) {
    super(debugName, PerlNamespaceElementImpl.class);
  }

  @Override
  protected @NotNull TextRange getLexerConfirmationRange(@NotNull ASTNode leaf) {
    ASTNode parent = leaf.getTreeParent();
    IElementType parentType = PsiUtilCore.getElementType(parent);
    if (parentType == SUB_DEFINITION || parentType == SUB_DECLARATION) {
      ASTNode nextLeaf = TreeUtil.nextLeaf(leaf);
      int endOffset = nextLeaf == null ? leaf.getTextRange().getEndOffset() :
                      nextLeaf.getTextRange().getEndOffset();
      return TextRange.create(parent.getStartOffset(), endOffset);
    }
    if (SUFFICIENT_CONTAINERS.contains(parentType)) {
      return TextRange.create(parent.getStartOffset(), leaf.getTextRange().getEndOffset());
    }
    if (parentType == METHOD) {
      ASTNode callElement = parent.getTreeParent();
      IElementType callType = PsiUtilCore.getElementType(callElement);
      if (callType == SUB_CALL || callType == CODE_VARIABLE) {
        ASTNode derefExpr = callElement.getTreeParent();
        if (PsiUtilCore.getElementType(derefExpr) == DEREF_EXPR) {
          return TextRange.create(derefExpr.getStartOffset(), callElement.getTextRange().getEndOffset());
        }
        return TextRange.create(callElement.getStartOffset(), callElement.getTextRange().getEndOffset());
      }
    }
    return TextRange.EMPTY_RANGE;
  }
}
