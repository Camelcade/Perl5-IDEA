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

package com.perl5.lang.tt2.formatter;

import com.intellij.formatting.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.formatter.xml.XmlFormattingPolicy;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.perl.idea.formatter.PerlXmlTemplateFormattingModelBuilder;
import com.perl5.lang.perl.idea.formatter.blocks.PerlFormattingBlock;
import com.perl5.lang.tt2.TemplateToolkitLanguage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.perl5.lang.tt2.formatter.TemplateToolkitFormattingTokenSets.PERL_BLOCKS;
import static com.perl5.lang.tt2.parser.TemplateToolkitElementTypesGenerated.TT2_HTML;
import static com.perl5.lang.tt2.parser.TemplateToolkitElementTypesGenerated.TT2_OUTER;

public class TemplateToolkitFormattingModelBuilder
  extends PerlXmlTemplateFormattingModelBuilder<TemplateToolkitFormattingContext, TemplateToolkitFormattingBlock> {
  @Override
  protected boolean isTemplateFile(PsiFile file) {
    return file.getLanguage().isKindOf(TemplateToolkitLanguage.INSTANCE);
  }

  @Override
  public boolean isOuterLanguageElement(PsiElement element) {
    return PsiUtilCore.getElementType(element) == TT2_OUTER;
  }

  @Override
  public boolean isMarkupLanguageElement(PsiElement element) {
    return PsiUtilCore.getElementType(element) == TT2_HTML;
  }

  @Override
  protected Block createTemplateLanguageBlock(ASTNode node,
                                              CodeStyleSettings settings,
                                              XmlFormattingPolicy xmlFormattingPolicy,
                                              Indent indent,
                                              @Nullable Alignment alignment,
                                              @Nullable Wrap wrap,
                                              @NotNull TemplateToolkitFormattingContext context) {
    var nodeType = PsiUtilCore.getElementType(node);
    if (PERL_BLOCKS.contains(nodeType)) {
      var perlBlock = new PerlFormattingBlock(node, context.getPurePerlContext());
      perlBlock.setIndent(Indent.getNoneIndent());
      return perlBlock;
    }
    return new TemplateToolkitFormattingBlock(this, node, wrap, alignment, settings, xmlFormattingPolicy, indent, context);
  }

  @Override
  protected @NotNull TemplateToolkitFormattingContext createContext(@NotNull FormattingContext formattingContext) {
    return new TemplateToolkitFormattingContext(formattingContext);
  }
}
