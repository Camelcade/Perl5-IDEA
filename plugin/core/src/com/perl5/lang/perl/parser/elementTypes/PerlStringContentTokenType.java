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
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.perl.psi.impl.PerlStringContentElementImpl;
import org.jetbrains.annotations.NotNull;

import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.HEREDOC_OPENER;
import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.STRING_BARE;

public class PerlStringContentTokenType extends PerlReparseableTokenType {
  public PerlStringContentTokenType(@NotNull String debugName) {
    super(debugName, PerlStringContentElementImpl.class);
  }

  @Override
  protected boolean isReparseable(@NotNull ASTNode leaf, @NotNull CharSequence newText) {
    if (newText.length() == 0) {
      return false;
    }
    ASTNode parent = leaf.getTreeParent();
    if (PsiUtilCore.getElementType(parent) != STRING_BARE ||
        PsiUtilCore.getElementType(parent.getTreeParent()) == HEREDOC_OPENER) {
      return false;
    }

    if (!Character.isUnicodeIdentifierStart(newText.charAt(0))) {
      return false;
    }

    for (int i = 1; i < newText.length(); i++) {
      char currentChar = newText.charAt(i);
      if (!Character.isUnicodeIdentifierPart(currentChar)) {
        return false;
      }
    }

    return true;
  }
}
