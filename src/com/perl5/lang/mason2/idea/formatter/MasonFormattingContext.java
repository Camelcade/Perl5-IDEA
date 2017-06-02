package com.perl5.lang.mason2.idea.formatter;

import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.perl5.lang.mason2.elementType.Mason2ElementTypes;
import com.perl5.lang.perl.idea.formatter.PerlFormattingContext;
import com.perl5.lang.perl.idea.formatter.PerlIndentProcessor;
import org.jetbrains.annotations.NotNull;

public class MasonFormattingContext extends PerlFormattingContext implements Mason2ElementTypes {
  public MasonFormattingContext(@NotNull CodeStyleSettings settings) {
    super(settings);
  }

  @Override
  public PerlIndentProcessor getIndentProcessor() {
    return MasonIndentProcessor.INSTANCE;
  }
}
