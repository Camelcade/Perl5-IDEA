package com.perl5.lang.perl.idea.formatter.blocks;

import com.intellij.formatting.Alignment;
import com.intellij.formatting.Block;
import com.intellij.formatting.SpacingBuilder;
import com.intellij.formatting.Wrap;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import com.intellij.psi.formatter.common.InjectedLanguageBlockBuilder;
import com.perl5.lang.perl.idea.formatter.settings.PerlCodeStyleSettings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

/**
 * Created by hurricup on 20.05.2017.
 */
public class PerlHeredocFormattingBlock extends PerlFormattingBlock {
  public PerlHeredocFormattingBlock(@NotNull ASTNode node,
                                    @Nullable Wrap wrap,
                                    @Nullable Alignment alignment,
                                    @NotNull CommonCodeStyleSettings codeStyleSettings,
                                    @NotNull PerlCodeStyleSettings perlCodeStyleSettings,
                                    @NotNull SpacingBuilder spacingBuilder,
                                    @NotNull InjectedLanguageBlockBuilder injectedLanguageBlockBuilder) {
    super(node, wrap, alignment, codeStyleSettings, perlCodeStyleSettings, spacingBuilder, injectedLanguageBlockBuilder);
  }

  @Override
  public boolean isLeaf() {
    return true;
  }

  @NotNull
  @Override
  public List<Block> getSubBlocks() {
    return Collections.emptyList();
  }

  @NotNull
  @Override
  public TextRange getTextRange() {
    TextRange originalRange = super.getTextRange();
    return originalRange.isEmpty() ? originalRange : TextRange.create(originalRange.getStartOffset(), originalRange.getEndOffset() - 1);
  }
}
