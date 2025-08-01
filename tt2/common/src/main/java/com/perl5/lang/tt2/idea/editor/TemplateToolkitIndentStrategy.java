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

package com.perl5.lang.tt2.idea.editor;

import com.intellij.openapi.editor.IndentStrategy;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

import static com.perl5.lang.tt2.parser.TemplateToolkitElementTypesGenerated.TT2_OUTLINE_TAG;


public class TemplateToolkitIndentStrategy implements IndentStrategy {
  @Override
  public boolean canIndent(int indentationStartOffset, int indentationEndOffset, @NotNull PsiElement element) {
    return element.getNode().getElementType() != TT2_OUTLINE_TAG;
  }
}
