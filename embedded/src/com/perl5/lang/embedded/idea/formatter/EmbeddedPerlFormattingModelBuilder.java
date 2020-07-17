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

package com.perl5.lang.embedded.idea.formatter;

import com.intellij.formatting.FormattingMode;
import com.intellij.formatting.FormattingModel;
import com.intellij.formatting.FormattingModelProvider;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.perl5.lang.perl.idea.formatter.PerlFormattingModelBuilder;
import com.perl5.lang.perl.idea.formatter.blocks.PerlFormattingBlock;
import org.jetbrains.annotations.NotNull;


public class EmbeddedPerlFormattingModelBuilder extends PerlFormattingModelBuilder {
  @Override
  public @NotNull FormattingModel createModel(@NotNull PsiElement element,
                                              @NotNull TextRange range,
                                              @NotNull CodeStyleSettings settings,
                                              @NotNull FormattingMode mode) {
    PerlFormattingBlock block = new EmbeddedPerlFormattingBlock(
      element.getNode(), new EmbeddedPerlFormattingContext(element, range, settings, mode));
    return FormattingModelProvider.createFormattingModelForPsiFile(element.getContainingFile(), block, settings);
  }
}
