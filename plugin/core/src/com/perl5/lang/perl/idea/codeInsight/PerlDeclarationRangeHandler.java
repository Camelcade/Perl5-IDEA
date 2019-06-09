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

package com.perl5.lang.perl.idea.codeInsight;

import com.intellij.codeInsight.hint.DeclarationRangeHandler;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.perl5.lang.perl.psi.PerlNamespaceDefinitionWithIdentifier;
import com.perl5.lang.perl.psi.PerlSubDefinitionElement;
import com.perl5.lang.perl.psi.PsiPerlBlock;
import com.perl5.lang.perl.psi.PsiPerlNamespaceContent;
import org.jetbrains.annotations.NotNull;

public class PerlDeclarationRangeHandler implements DeclarationRangeHandler<PsiElement> {
  @NotNull
  @Override
  public TextRange getDeclarationRange(@NotNull PsiElement container) {
    if (container instanceof PerlSubDefinitionElement) {
      PsiPerlBlock body = ((PerlSubDefinitionElement)container).getSubDefinitionBody();
      if (body == null) {
        return TextRange.EMPTY_RANGE;
      }
      return TextRange.create(container.getNode().getStartOffset(), body.getTextOffset());
    }
    else if (container instanceof PerlNamespaceDefinitionWithIdentifier) {
      PsiPerlBlock block = ((PerlNamespaceDefinitionWithIdentifier)container).getBlock();
      if (block != null) {
        return TextRange.create(container.getNode().getStartOffset(), block.getTextOffset());
      }
      PsiPerlNamespaceContent content = ((PerlNamespaceDefinitionWithIdentifier)container).getNamespaceContent();
      if (content != null) {
        return TextRange.create(container.getNode().getStartOffset(), content.getTextOffset());
      }
      return TextRange.EMPTY_RANGE;
    }

    throw new IllegalArgumentException("Unhandled container: " + container);
  }
}
