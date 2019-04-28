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

package com.perl5.lang.perl.idea.folding;

import com.intellij.lang.folding.FoldingBuilderEx;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.perl5.lang.perl.psi.PerlNamespaceDefinitionWithIdentifier;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by hurricup on 03.04.2016.
 */
public abstract class PerlFoldingBuilderBase extends FoldingBuilderEx {
  /**
   * Checks if {@code element}'s range adjusted by {@code endMargin} and {@code startMargin} covers more than {@code minLines}. If it does,
   * creates a folding descriptor and putting it into {@code result} list.
   */
  protected static void addDescriptorFor(@NotNull List<FoldingDescriptor> result,
                                         @NotNull Document document,
                                         @NotNull PsiElement element,
                                         int startMargin,
                                         int endMargin,
                                         int minLines
  ) {
    if (element.getParent() instanceof PerlNamespaceDefinitionWithIdentifier) {
      return;
    }

    TextRange range = element.getTextRange();
    int startOffset = range.getStartOffset() + startMargin;
    int endOffset = range.getEndOffset() - endMargin;
    int startLine = document.getLineNumber(startOffset);
    int endLine = document.getLineNumber(endOffset);

    if (endLine - startLine > minLines) {
      result.add(new FoldingDescriptor(element.getNode(), new TextRange(startOffset, endOffset)));
    }
  }
}
