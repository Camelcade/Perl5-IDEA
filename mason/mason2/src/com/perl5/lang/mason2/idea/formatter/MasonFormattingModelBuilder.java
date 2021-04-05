/*
 * Copyright 2015-2021 Alexandr Evstigneev
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

import com.intellij.formatting.FormattingContext;
import com.intellij.formatting.FormattingModel;
import com.intellij.formatting.FormattingModelProvider;
import com.perl5.lang.perl.idea.formatter.PerlTemplatingFormattingModelBuilder;
import com.perl5.lang.perl.idea.formatter.blocks.PerlFormattingBlock;
import org.jetbrains.annotations.NotNull;


public class MasonFormattingModelBuilder extends PerlTemplatingFormattingModelBuilder {
  @Override
  public @NotNull FormattingModel createModel(@NotNull FormattingContext formattingContext) {
    var element = formattingContext.getPsiElement();
    var settings = formattingContext.getCodeStyleSettings();
    var mode = formattingContext.getFormattingMode();
    PerlFormattingBlock block = new MasonFormattingBlock(
      element.getNode(), new MasonFormattingContext(element, element.getTextRange(), settings, mode));
    return FormattingModelProvider.createFormattingModelForPsiFile(element.getContainingFile(), block, settings);
  }
}
