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
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.util.PsiUtilCore;
import org.jetbrains.annotations.NotNull;

import static com.perl5.lang.perl.parser.PerlElementTypesGenerated.ATTRIBUTE;
import static com.perl5.lang.perl.parser.PerlElementTypesGenerated.ATTRIBUTES;

public class PerlAttributeIdentifierTokenType extends PerlReparseableTokenType {
  public PerlAttributeIdentifierTokenType(@NotNull String debugName) {
    super(debugName, LeafPsiElement.class);
  }

  @Override
  protected @NotNull TextRange getLexerConfirmationRange(@NotNull ASTNode leaf) {
    ASTNode attribute = leaf.getTreeParent();
    if (PsiUtilCore.getElementType(attribute) != ATTRIBUTE) {
      return TextRange.EMPTY_RANGE;
    }

    return getAttributeConfirmationRange(leaf, attribute);
  }

  static @NotNull TextRange getAttributeConfirmationRange(@NotNull ASTNode leaf, ASTNode attribute) {
    ASTNode attributes = attribute.getTreeParent();
    if (PsiUtilCore.getElementType(attributes) != ATTRIBUTES) {
      return TextRange.EMPTY_RANGE;
    }

    ASTNode attributesContainer = attributes.getTreeParent();
    if (attributesContainer == null) {
      return TextRange.EMPTY_RANGE;
    }

    return TextRange.create(attributesContainer.getStartOffset(), leaf.getTextRange().getEndOffset());
  }
}
