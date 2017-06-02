package com.perl5.lang.perl.idea.formatter;

import com.intellij.formatting.Block;
import com.intellij.formatting.FormattingModelBuilder;
import com.intellij.formatting.Indent;
import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.lang.LanguageFormatting;
import com.intellij.openapi.util.Ref;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.formatter.common.DefaultInjectedLanguageBlockBuilder;
import com.intellij.psi.impl.source.tree.injected.InjectedLanguageUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PerlDefaultInjectedBlocksBuilder extends DefaultInjectedLanguageBlockBuilder {
  public PerlDefaultInjectedBlocksBuilder(@NotNull CodeStyleSettings settings) {
    super(settings);
  }

  public void addInjectedBlocks(List<Block> result, ASTNode injectionHost) {
    final PsiFile[] injectedFile = new PsiFile[1];
    final Ref<TextRange> injectedRangeInsideHost = new Ref<>();
    final Ref<Integer> prefixLength = new Ref<>();
    final Ref<Integer> suffixLength = new Ref<>();
    final Ref<ASTNode> injectionHostToUse = new Ref<>(injectionHost);

    final PsiLanguageInjectionHost.InjectedPsiVisitor injectedPsiVisitor = (injectedPsi, places) -> {
      if (places.size() != 1) {
        return;
      }
      final PsiLanguageInjectionHost.Shred shred = places.get(0);
      TextRange textRange = shred.getRangeInsideHost();
      PsiLanguageInjectionHost shredHost = shred.getHost();
      if (shredHost == null) {
        return;
      }
      ASTNode node = shredHost.getNode();
      if (node == null) {
        return;
      }
      if (node != injectionHost) {
        int shift = 0;
        boolean canProcess = false;
        for (ASTNode n = injectionHost.getTreeParent(), prev = injectionHost; n != null; prev = n, n = n.getTreeParent()) {
          shift += n.getStartOffset() - prev.getStartOffset();
          if (n == node) {
            textRange = textRange.shiftRight(shift);
            canProcess = true;
            break;
          }
        }
        if (!canProcess) {
          return;
        }
      }

      String childText;
      if ((injectionHost.getTextLength() == textRange.getEndOffset() && textRange.getStartOffset() == 0) ||
          (canProcessFragment((childText = injectionHost.getText()).substring(0, textRange.getStartOffset()), injectionHost) &&
           canProcessFragment(childText.substring(textRange.getEndOffset()), injectionHost))) {
        injectedFile[0] = injectedPsi;
        injectedRangeInsideHost.set(textRange);
        prefixLength.set(shred.getPrefix().length());
        suffixLength.set(shred.getSuffix().length());
      }
    };
    final PsiElement injectionHostPsi = injectionHost.getPsi();
    InjectedLanguageUtil.enumerate(injectionHostPsi, injectionHostPsi.getContainingFile(), false, injectedPsiVisitor);

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
}
