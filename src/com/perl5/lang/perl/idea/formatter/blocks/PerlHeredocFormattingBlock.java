package com.perl5.lang.perl.idea.formatter.blocks;

import com.intellij.formatting.Alignment;
import com.intellij.formatting.Block;
import com.intellij.formatting.SpacingBuilder;
import com.intellij.formatting.Wrap;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import com.intellij.psi.formatter.common.InjectedLanguageBlockBuilder;
import com.perl5.lang.perl.idea.formatter.settings.PerlCodeStyleSettings;
import com.perl5.lang.perl.psi.PerlHeredocTerminatorElement;
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

  @NotNull
  private PerlHeredocElementImpl getPsi() {
    return (PerlHeredocElementImpl)myNode.getPsi();
  }

  @Override
  public boolean isLeaf() {
    return !getPsi().isValidHost();
  }

  @NotNull
  @Override
  protected List<Block> buildChildren() {
    if (isLeaf()) {
      return Collections.emptyList();
    }

    List<Block> blocks = new ArrayList<>();
    //getInjectedLanguageBlockBuilder().addInjectedBlocks(blocks, getNode(), null, null, Indent.getAbsoluteNoneIndent());
    return blocks;
  }

  @NotNull
  @Override
  public TextRange getTextRange() {
    TextRange originalRange = super.getTextRange();
    if (originalRange.isEmpty()) {
      return originalRange;
    }

    TextRange trimmedRange = TextRange.create(originalRange.getStartOffset(), originalRange.getEndOffset() - 1);

    PerlHeredocElementImpl heredocElement = getPsi();
    if (!heredocElement.isIndentable()) {
      return trimmedRange;
    }

    PsiElement run = heredocElement.getNextSibling();
    while (run instanceof PsiWhiteSpace) {
      run = run.getNextSibling();
    }

    if (!(run instanceof PerlHeredocTerminatorElement)) {
      return trimmedRange;
    }

    // perlop: Tabs and spaces can be mixed, but are matched exactly. One tab will not be equal to 8 spaces!
    int spacesBeforeTerminator = run.getTextRange().getStartOffset() - trimmedRange.getEndOffset() - 1;
    if (spacesBeforeTerminator == 0) {
      return trimmedRange;
    }

    int realSpaces;
    for (realSpaces = 0; realSpaces < spacesBeforeTerminator; realSpaces++) {
      if (!Character.isWhitespace(myNode.getChars().charAt(realSpaces))) {
        break;
      }
    }

    return TextRange.create(trimmedRange.getStartOffset() + realSpaces, trimmedRange.getEndOffset());
  }
}
