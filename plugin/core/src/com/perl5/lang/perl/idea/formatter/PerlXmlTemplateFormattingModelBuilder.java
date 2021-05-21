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

package com.perl5.lang.perl.idea.formatter;

import com.intellij.formatting.*;
import com.intellij.lang.ASTNode;
import com.intellij.lang.xml.XMLLanguage;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.formatter.xml.XmlFormattingPolicy;
import com.intellij.psi.templateLanguages.SimpleTemplateLanguageFormattingModelBuilder;
import com.intellij.xml.template.formatter.AbstractXmlTemplateFormattingModelBuilder;
import com.perl5.lang.perl.psi.PerlMultiplePsiFilesPerDocumentFileViewProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class PerlXmlTemplateFormattingModelBuilder<Ctx extends PerlBaseFormattingContext, Blk extends PerlTemplateLanguageBlock<Ctx>>
  extends AbstractXmlTemplateFormattingModelBuilder {
  private static final Logger LOG = Logger.getInstance(PerlXmlTemplateFormattingModelBuilder.class);

  @Override
  public @NotNull FormattingModel createModel(@NotNull FormattingContext formattingContext) {
    LOG.debug("Creating model for ", formattingContext);
    return useXmlFormattingModelBuilder(formattingContext.getPsiElement()) ?
           super.createModel(formattingContext) :
           new SimpleTemplateLanguageFormattingModelBuilder().createModel(formattingContext);
  }


  @Override
  protected final Block createTemplateLanguageBlock(@NotNull ASTNode node,
                                                    CodeStyleSettings settings,
                                                    XmlFormattingPolicy xmlFormattingPolicy,
                                                    Indent indent,
                                                    @Nullable Alignment alignment,
                                                    @Nullable Wrap wrap) {
    var perlFormattingContext = createContext(FormattingContext.create(node.getPsi(), settings));
    return createTemplateLanguageBlock(node,
                                       settings,
                                       xmlFormattingPolicy,
                                       perlFormattingContext.getNodeIndent(node),
                                       perlFormattingContext.getAlignment(node),
                                       perlFormattingContext.getWrap(node),
                                       perlFormattingContext);
  }

  protected abstract Blk createTemplateLanguageBlock(ASTNode node,
                                                     CodeStyleSettings settings,
                                                     XmlFormattingPolicy xmlFormattingPolicy,
                                                     Indent indent,
                                                     @Nullable Alignment alignment,
                                                     @Nullable Wrap wrap,
                                                     @NotNull Ctx context);

  protected abstract @NotNull Ctx createContext(@NotNull FormattingContext formattingContext);

  protected boolean useXmlFormattingModelBuilder(@NotNull PsiElement element) {
    FileViewProvider viewProvider = element.getContainingFile().getViewProvider();
    LOG.assertTrue(viewProvider instanceof PerlMultiplePsiFilesPerDocumentFileViewProvider, "Got " + viewProvider);
    return ((PerlMultiplePsiFilesPerDocumentFileViewProvider)viewProvider).getTemplateDataLanguage().isKindOf(XMLLanguage.INSTANCE);
  }
}
