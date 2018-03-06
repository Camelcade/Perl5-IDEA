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

package com.perl5.lang.mason2.idea.formatter;

import com.intellij.psi.PsiElement;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.htmlmason.idea.formatter.AbstractMasonFormattingContext;
import com.perl5.lang.perl.idea.formatter.PerlIndentProcessor;
import org.jetbrains.annotations.NotNull;

import static com.perl5.lang.mason2.elementType.Mason2ElementTypes.MASON_LINE_OPENER;

public class MasonFormattingContext extends AbstractMasonFormattingContext {
  public MasonFormattingContext(@NotNull PsiElement element, @NotNull CodeStyleSettings settings) {
    super(element, settings);
  }

  @Override
  public PerlIndentProcessor getIndentProcessor() {
    return MasonIndentProcessor.INSTANCE;
  }

  @Override
  protected IElementType getLineOpenerToken() {
    return MASON_LINE_OPENER;
  }
}
