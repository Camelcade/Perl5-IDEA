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
import com.perl5.lang.perl.psi.impl.PerlHeredocElementImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
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
    assert node.getPsi() instanceof PerlHeredocElementImpl : "Got " + node + "instead of heredoc.";
  }

  @Override
  public boolean isLeaf() {
    return !((PerlHeredocElementImpl)myNode.getPsi()).isValidHost();
  }

  @NotNull
  @Override
  protected List<Block> buildChildren() {
    if (!isLeaf()) {
      return Collections.emptyList();
    }

    List<Block> blocks = new ArrayList<>();
    getInjectedLanguageBlockBuilder().addInjectedBlocks(blocks, getNode(), null, null, getIndent());
    return blocks;
  }

  @NotNull
  @Override
  public TextRange getTextRange() {
    TextRange originalRange = super.getTextRange();
    return originalRange.isEmpty() ? originalRange : TextRange.create(originalRange.getStartOffset(), originalRange.getEndOffset() - 1);
  }
}
