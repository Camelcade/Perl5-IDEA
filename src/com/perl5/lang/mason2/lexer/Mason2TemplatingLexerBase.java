/*
 * Copyright 2015-2017 Alexandr Evstigneev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.perl5.lang.mason2.lexer;

import com.intellij.openapi.util.text.StringUtil;
import com.perl5.lang.mason2.elementType.Mason2ElementTypes;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.lexer.PerlTemplatingLexer;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 28.10.2016.
 */
public abstract class Mason2TemplatingLexerBase extends PerlTemplatingLexer implements Mason2ElementTypes, PerlElementTypes {
  private final CommentEndCalculator COMMENT_END_CALCULATOR = commentText ->
  {
    int realLexicalState = getRealLexicalState();
    if (realLexicalState == Mason2TemplatingLexer.PERL_EXPR_BLOCK || realLexicalState == Mason2TemplatingLexer.PERL_EXPR_BLOCK_FILTER) {
      return StringUtil.indexOf(commentText, KEYWORD_BLOCK_CLOSER);
    }
    return -1;
  };

  @Nullable
  @Override
  protected CommentEndCalculator getCommentEndCalculator() {
    return COMMENT_END_CALCULATOR;
  }
}
