/*
 * Copyright 2015-2020 Alexandr Evstigneev
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

package com.perl5.lang.perl.lexer.adapters;

import com.intellij.lexer.MergingLexerAdapter;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.lexer.PerlLexingContext;
import org.jetbrains.annotations.NotNull;

/**
 * Second level of lexer adapter merges necessary tokens
 *
 * @see PerlSublexingLexerAdapter
 */
public class PerlMergingLexerAdapter extends MergingLexerAdapter implements PerlElementTypes {
  public static final TokenSet TOKENS_TO_MERGE = TokenSet.create(
    STRING_CONTENT, REGEX_TOKEN, STRING_CONTENT_QQ, STRING_CONTENT_XQ, TokenType.WHITE_SPACE, COMMENT_BLOCK
  );

  public PerlMergingLexerAdapter(@NotNull PerlLexingContext lexingContext) {
    super(new PerlSublexingLexerAdapter(lexingContext), TOKENS_TO_MERGE);
  }

  @Override
  public void advance() {
    ProgressManager.checkCanceled();
    super.advance();
  }
}
