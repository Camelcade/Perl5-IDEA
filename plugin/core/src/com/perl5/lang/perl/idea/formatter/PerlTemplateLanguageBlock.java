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
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.formatter.xml.XmlFormattingPolicy;
import com.intellij.xml.template.formatter.TemplateLanguageBlock;
import com.perl5.lang.perl.idea.formatter.blocks.PerlAstBlock;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class PerlTemplateLanguageBlock<Ctx extends PerlBaseFormattingContext> extends TemplateLanguageBlock
  implements PerlAstBlock {
  private final @NotNull Ctx myFormattingContext;

  public PerlTemplateLanguageBlock(PerlXmlTemplateFormattingModelBuilder<Ctx, ?> builder,
                                   @NotNull ASTNode node,
                                   @Nullable Wrap wrap,
                                   @Nullable Alignment alignment,
                                   CodeStyleSettings settings,
                                   XmlFormattingPolicy xmlFormattingPolicy,
                                   @Nullable Indent indent,
                                   @NotNull Ctx formattingContext) {
    super(builder, node, wrap, alignment, settings, xmlFormattingPolicy, indent);
    myFormattingContext = formattingContext;
  }

  @Override
  protected final @NotNull PerlXmlTemplateFormattingModelBuilder<Ctx, ?> getBuilder() {
    //noinspection unchecked
    return (PerlXmlTemplateFormattingModelBuilder<Ctx, ?>)super.getBuilder();
  }

  @Override
  protected final Block createTemplateLanguageBlock(ASTNode child) {
    return getBuilder().createTemplateLanguageBlock(
      child, getSettings(), getXmlFormattingPolicy(), getChildIndent(child), getChildAlignment(child), getChildWrap(child),
      myFormattingContext);
  }

  @Override
  public @NotNull ChildAttributes getChildAttributes(int newChildIndex) {
    return myFormattingContext.getChildAttributes(this, newChildIndex);
  }

  @Override
  protected @Nullable Wrap getChildWrap(ASTNode child) {
    return myFormattingContext.getWrap(child);
  }

  @Override
  protected @Nullable Alignment getChildAlignment(ASTNode child) {
    return myFormattingContext.getAlignment(child);
  }

  @Override
  protected @NotNull Indent getChildIndent(@NotNull ASTNode node) {
    return myFormattingContext.getNodeIndent(node);
  }

  @Override
  protected Spacing getSpacing(TemplateLanguageBlock adjacentBlock) {
    return myFormattingContext.getSpacing(null, this, adjacentBlock);
  }

  @Override
  public @Nullable Spacing getSpacing(@Nullable Block child1, @NotNull Block child2) {
    return myFormattingContext.getSpacing(this, child1, child2);
  }
}
