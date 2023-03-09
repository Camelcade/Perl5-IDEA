/*
 * Copyright 2015-2023 Alexandr Evstigneev
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

package com.perl5.lang.tt2.formatter;

import com.intellij.formatting.Alignment;
import com.intellij.formatting.Indent;
import com.intellij.formatting.Wrap;
import com.intellij.lang.ASTNode;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.formatter.xml.XmlFormattingPolicy;
import com.perl5.lang.perl.idea.formatter.PerlTemplateLanguageBlock;
import com.perl5.lang.perl.idea.formatter.PerlXmlTemplateFormattingModelBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TemplateToolkitFormattingBlock extends PerlTemplateLanguageBlock<TemplateToolkitFormattingContext> {
  public TemplateToolkitFormattingBlock(PerlXmlTemplateFormattingModelBuilder<TemplateToolkitFormattingContext, ?> builder,
                                        @NotNull ASTNode node,
                                        @Nullable Wrap wrap,
                                        @Nullable Alignment alignment,
                                        CodeStyleSettings settings,
                                        XmlFormattingPolicy xmlFormattingPolicy,
                                        @Nullable Indent indent,
                                        @NotNull TemplateToolkitFormattingContext formattingContext) {
    super(builder, node, wrap, alignment, settings, xmlFormattingPolicy, indent, formattingContext);
  }
}
