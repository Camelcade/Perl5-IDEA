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

package com.perl5.lang.perl.idea.formatter;

import com.intellij.formatting.*;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.util.NotNullLazyValue;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.perl5.lang.perl.idea.formatter.blocks.PerlAstBlock;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class PerlBaseFormattingContext {
  private final @NotNull FormattingContext myFormattingContext;

  private final @Nullable Document myDocument;
  private final @NotNull NotNullLazyValue<SpacingBuilder> mySpacingBuilderProvider =
    NotNullLazyValue.createValue(this::createSpacingBuilder);

  public PerlBaseFormattingContext(@NotNull FormattingContext formattingContext) {
    myFormattingContext = formattingContext;
    myDocument = myFormattingContext.getContainingFile().getViewProvider().getDocument();
  }

  protected final @NotNull FormattingContext getFormattingContext() {
    return myFormattingContext;
  }

  public final @NotNull SpacingBuilder getSpacingBuilder() {
    return mySpacingBuilderProvider.getValue();
  }

  protected final @NotNull CodeStyleSettings getCodeStyleSettings() {
    return getFormattingContext().getCodeStyleSettings();
  }

  protected abstract @NotNull SpacingBuilder createSpacingBuilder();

  protected final @Nullable Document getDocument() {
    return myDocument;
  }

  public @NotNull FormattingMode getFormattingMode() {
    return myFormattingContext.getFormattingMode();
  }

  public @NotNull TextRange getTextRange() {
    return myFormattingContext.getFormattingRange();
  }

  public abstract @NotNull Indent getNodeIndent(@NotNull ASTNode node);

  public abstract @Nullable Indent getChildIndent(@NotNull PerlAstBlock block, int newChildIndex);

  public abstract @Nullable Wrap getWrap(@NotNull ASTNode childNode);

  public abstract @Nullable Alignment getAlignment(@NotNull ASTNode childNode);

  public abstract @Nullable Alignment getChildAlignment(@NotNull PerlAstBlock block, int newChildIndex);

  public @Nullable Spacing getSpacing(@Nullable ASTBlock parent, @Nullable Block child1, @NotNull Block child2) {
    return getSpacingBuilder().getSpacing(parent, child1, child2);
  }

  public final @NotNull ChildAttributes getChildAttributes(@NotNull PerlAstBlock block, int newChildIndex) {
    return new ChildAttributes(getChildIndent(block, newChildIndex), getChildAlignment(block, newChildIndex));
  }
}