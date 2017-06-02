package com.perl5.lang.perl.idea.formatter;

import com.intellij.formatting.Block;
import com.intellij.formatting.FormattingModel;
import com.intellij.formatting.FormattingModelBuilder;
import com.intellij.formatting.Indent;
import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.lang.LanguageFormatting;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.Ref;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.formatter.common.InjectedLanguageBlockWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

// this was cloned from DefaultInjectedLanguageBlockBuilder
public class PerlHeredocInjectedBlocksBuilder {
  private static final Logger LOG = Logger.getInstance("#com.perl5.lang.perl.idea.formatter.PerlHeredocInjectedBlocksBuilder");

  @NotNull
  private final CodeStyleSettings mySettings;

  public PerlHeredocInjectedBlocksBuilder(@NotNull CodeStyleSettings settings) {
    mySettings = settings;
  }

  public Block createInjectedBlock(ASTNode node,
                                   Block originalBlock,
                                   Indent indent,
                                   int offset,
                                   TextRange range,
                                   @Nullable Language language) {
    return new InjectedLanguageBlockWrapper(originalBlock, offset, range, indent, language);
  }

  public void addInjectedBlocks(List<Block> result, final ASTNode injectionHost) {
    final PsiFile[] injectedFile = new PsiFile[1];
    final Ref<TextRange> injectedRangeInsideHost = new Ref<>();
    final Ref<Integer> prefixLength = new Ref<>();
    final Ref<Integer> suffixLength = new Ref<>();
    final Ref<ASTNode> injectionHostToUse = new Ref<>(injectionHost);

    final PsiElement injectionHostPsi = injectionHost.getPsi();

    if (injectedFile[0] != null) {
      final Language childLanguage = injectedFile[0].getLanguage();
      final FormattingModelBuilder builder = LanguageFormatting.INSTANCE.forContext(childLanguage, injectionHostPsi);

      if (builder != null) {
        final int startOffset = injectedRangeInsideHost.get().getStartOffset();
        final int endOffset = injectedRangeInsideHost.get().getEndOffset();
        TextRange range = injectionHostToUse.get().getTextRange();

        int childOffset = range.getStartOffset();

        addInjectedLanguageBlockWrapper(result, injectedFile[0].getNode(), Indent.getNoneIndent(), childOffset + startOffset,
                                        new TextRange(prefixLength.get(), injectedFile[0].getTextLength() - suffixLength.get()));
      }
    }
  }

  public void addInjectedLanguageBlockWrapper(final List<Block> result, final ASTNode injectedNode,
                                              final Indent indent, int offset, @Nullable TextRange range) {

    //
    // Do not create a block for an empty range
    //
    if (range != null) {
      if (range.getLength() == 0) return;
      if (StringUtil.isEmptyOrSpaces(range.substring(injectedNode.getText()))) {
        return;
      }
    }

    final PsiElement childPsi = injectedNode.getPsi();
    final Language childLanguage = childPsi.getLanguage();
    final FormattingModelBuilder builder = LanguageFormatting.INSTANCE.forContext(childLanguage, childPsi);
    LOG.assertTrue(builder != null);
    final FormattingModel childModel = builder.createModel(childPsi, mySettings);
    Block original = childModel.getRootBlock();

    if ((original.isLeaf() && !injectedNode.getText().trim().isEmpty()) || !original.getSubBlocks().isEmpty()) {
      result.add(createInjectedBlock(injectedNode, original, indent, offset, range, childLanguage));
    }
  }
}
