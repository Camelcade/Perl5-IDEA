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

package com.perl5.lang.perl.idea.editor;

import com.intellij.openapi.editor.IndentStrategy;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.perl.idea.formatter.PerlIndentProcessor;
import com.perl5.lang.perl.psi.impl.PerlHeredocElementImpl;
import org.jetbrains.annotations.NotNull;


public class PerlIndentStrategy implements IndentStrategy {
  @Override
  public boolean canIndent(int indentationStartOffset, int indentationEndOffset, @NotNull PsiElement element) {
    if (element instanceof PerlHeredocElementImpl heredocElement) {
      return heredocElement.isIndentable();
    }

    PsiElement parentElement = element.getParent();
    if (parentElement instanceof PerlHeredocElementImpl heredocElement) {
      return heredocElement.isIndentable();
    }

    return !PerlIndentProcessor.INSTANCE.getAbsoluteUnindentableTokens().contains(PsiUtilCore.getElementType(element));
  }
}
