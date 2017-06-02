package com.perl5.lang.mojolicious.idea.formatter;

import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.perl5.lang.perl.idea.formatter.PerlFormattingContext;
import com.perl5.lang.perl.idea.formatter.PerlIndentProcessor;
import org.jetbrains.annotations.NotNull;

public class MojoliciousFormattingContext extends PerlFormattingContext {
  public MojoliciousFormattingContext(@NotNull CodeStyleSettings settings) {
    super(settings);
  }

  @Override
  public PerlIndentProcessor getIndentProcessor() {
    return MojoliciousIndentProcessor.INSTANCE;
  }
}
