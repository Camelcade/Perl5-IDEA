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

package com.perl5.lang.htmlmason.idea.formatter;

import com.intellij.formatting.Alignment;
import com.intellij.formatting.FormattingContext;
import com.intellij.formatting.Indent;
import com.intellij.formatting.Wrap;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.formatter.xml.XmlFormattingPolicy;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.htmlmason.HTMLMasonLanguage;
import com.perl5.lang.htmlmason.elementType.HTMLMasonElementTypes;
import com.perl5.lang.perl.idea.formatter.PerlXmlTemplateFormattingModelBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class HTMLMasonFormattingModelBuilder
  extends PerlXmlTemplateFormattingModelBuilder<HTMLMasonFormattingContext, HTMLMasonFormattingBlock> {
  @Override
  protected HTMLMasonFormattingBlock createTemplateLanguageBlock(ASTNode node,
                                                                 CodeStyleSettings settings,
                                                                 XmlFormattingPolicy xmlFormattingPolicy,
                                                                 Indent indent,
                                                                 @Nullable Alignment alignment,
                                                                 @Nullable Wrap wrap,
                                                                 @NotNull HTMLMasonFormattingContext context) {
    return new HTMLMasonFormattingBlock(this, node, wrap, alignment, settings, xmlFormattingPolicy, indent, context);
  }

  @Override
  protected @NotNull HTMLMasonFormattingContext createContext(@NotNull FormattingContext formattingContext) {
    return new HTMLMasonFormattingContext(formattingContext);
  }

  @Override
  protected boolean isTemplateFile(PsiFile file) {
    return file.getLanguage().isKindOf(HTMLMasonLanguage.INSTANCE);
  }

  @Override
  public boolean isOuterLanguageElement(PsiElement element) {
    return PsiUtilCore.getElementType(element) == HTMLMasonElementTypes.HTML_MASON_OUTER_ELEMENT_TYPE;
  }

  @Override
  public boolean isMarkupLanguageElement(PsiElement element) {
    return PsiUtilCore.getElementType(element) == HTMLMasonElementTypes.HTML_MASON_TEMPLATE_BLOCK_HTML;
  }
}
