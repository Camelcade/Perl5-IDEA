package com.perl5.lang.perl.idea.formatter;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.formatter.AbstractWhiteSpaceFormattingStrategy;
import com.perl5.lang.perl.psi.impl.PerlHeredocElementImpl;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 21.05.2017.
 */
public class PerlWhiteSpaceFormattingStrategy extends AbstractWhiteSpaceFormattingStrategy {

  @NotNull
  @Override
  public CharSequence adjustWhiteSpaceIfNecessary(@NotNull CharSequence whiteSpaceText,
                                                  @NotNull CharSequence text,
                                                  int startOffset,
                                                  int endOffset,
                                                  CodeStyleSettings codeStyleSettings,
                                                  ASTNode nodeAfter) {
    if (nodeAfter == null) {
      return super.adjustWhiteSpaceIfNecessary(whiteSpaceText, text, startOffset, endOffset, codeStyleSettings, nodeAfter);
    }

    PsiElement psi = nodeAfter.getPsi();
    if (!(psi instanceof PerlHeredocElementImpl && ((PerlHeredocElementImpl)psi).isIndentable())) {
      return super.adjustWhiteSpaceIfNecessary(whiteSpaceText, text, startOffset, endOffset, codeStyleSettings, nodeAfter);
    }


    return super.adjustWhiteSpaceIfNecessary(whiteSpaceText, text, startOffset, endOffset, codeStyleSettings, nodeAfter);
    //return whiteSpaceText;
  }

  @Override
  public int check(@NotNull CharSequence text, int start, int end) {
    for (int i = start; i < end; i++) {
      if (!Character.isWhitespace(text.charAt(i))) {
        return i;
      }
    }
    return end;
  }
}
