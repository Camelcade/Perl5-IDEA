package com.perl5.lang.embedded.lexer;

import com.intellij.openapi.util.text.StringUtil;
import com.perl5.lang.perl.lexer.PerlTemplatingLexer;
import org.jetbrains.annotations.Nullable;

public abstract class EmbeddedPerlBaseLexer extends PerlTemplatingLexer {
  private static final CommentEndCalculator COMMENT_END_CALCULATOR = commentText -> StringUtil.indexOf(commentText, "?>");

  @Nullable
  @Override
  protected CommentEndCalculator getCommentEndCalculator() {
    return COMMENT_END_CALCULATOR;
  }
}
