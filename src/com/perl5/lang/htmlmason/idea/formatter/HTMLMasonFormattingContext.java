package com.perl5.lang.htmlmason.idea.formatter;

import com.intellij.formatting.SpacingBuilder;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.htmlmason.elementType.HTMLMasonElementTypes;
import com.perl5.lang.perl.idea.formatter.PerlFormattingContext;
import com.perl5.lang.perl.idea.formatter.PerlIndentProcessor;
import org.jetbrains.annotations.NotNull;

public class HTMLMasonFormattingContext extends PerlFormattingContext implements HTMLMasonElementTypes {
  private static final TokenSet SIMPLE_OPENERS = TokenSet.create(
    HTML_MASON_BLOCK_OPENER,
    HTML_MASON_CALL_OPENER,
    HTML_MASON_CALL_FILTERING_OPENER
  );

  public HTMLMasonFormattingContext(@NotNull CodeStyleSettings settings) {
    super(settings);
  }

  @Override
  protected SpacingBuilder createSpacingBuilder() {
    return super.createSpacingBuilder()
      .after(SIMPLE_OPENERS).spaces(1)
      .before(HTML_MASON_BLOCK_CLOSER).spaces(1)
      .before(HTML_MASON_CALL_CLOSER).spaces(1)
      .before(HTML_MASON_CALL_CLOSER_UNMATCHED).spaces(1)
      .between(HTML_MASON_CALL_CLOSE_TAG_START, HTML_MASON_TAG_CLOSER).spaces(0)
      .after(HTML_MASON_CALL_CLOSE_TAG_START).spaces(1)
      .beforeInside(HTML_MASON_TAG_CLOSER, HTML_MASON_CALL_CLOSE_TAG).spaces(1)
      ;
  }

  @Override
  public PerlIndentProcessor getIndentProcessor() {
    return HTMLMasonIndentProcessor.INSTANCE;
  }
}
