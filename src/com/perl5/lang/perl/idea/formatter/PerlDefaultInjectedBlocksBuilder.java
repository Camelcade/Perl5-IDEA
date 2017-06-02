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

  public void addInjectedBlocks(List<Block> result, ASTNode injectionHost, @NotNull TextRange parentBlockRange) {
    final PsiFile[] injectedFile = new PsiFile[1];
    final Ref<TextRange> injectedRangeInsideHost = new Ref<>();

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

      if ((injectionHost.getTextLength() >= textRange.getEndOffset() && textRange.getStartOffset() >= 0)) {
        injectedFile[0] = injectedPsi;
        injectedRangeInsideHost.set(textRange);
      }
    };
    final PsiElement injectionHostPsi = injectionHost.getPsi();
    InjectedLanguageUtil.enumerate(injectionHostPsi, injectionHostPsi.getContainingFile(), false, injectedPsiVisitor);

    if (injectedFile[0] != null) {
      final Language childLanguage = injectedFile[0].getLanguage();
      final FormattingModelBuilder builder = LanguageFormatting.INSTANCE.forContext(childLanguage, injectionHostPsi);

      if (builder != null) {
        TextRange range = injectionHost.getTextRange();

        int childOffset = range.getStartOffset();
        TextRange nodeRange = new TextRange(0, injectedFile[0].getTextLength());
        TextRange croppedNodeRange = parentBlockRange.shiftRight(-childOffset).intersection(nodeRange);

        addInjectedLanguageBlockWrapper(result, injectedFile[0].getNode(), Indent.getNoneIndent(), childOffset,
                                        croppedNodeRange);
      }
    }
  }
}
