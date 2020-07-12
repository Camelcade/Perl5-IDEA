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

import com.intellij.lexer.FlexAdapter;
import com.intellij.lexer.LexerBase;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.lexer.PerlBaseLexer;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.lexer.PerlLexer;
import com.perl5.lang.perl.lexer.PerlLexingContext;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * First level adapter, working above Flex lexer. Merges code blocks into LP_CODE_BLOCK tokens
 *
 * @see PerlSublexingLexerAdapter
 */
class PerlCodeMergingLexerAdapter extends LexerBase implements PerlElementTypes {
  private static final Logger LOG = Logger.getInstance(FlexAdapter.class);
  private final PerlBaseLexer myPerlLexer;
  private int myBufferStart;
  private IElementType myTokenType;
  private CharSequence myText;

  private int myTokenStart;
  private int myTokenEnd;

  private int myBufferEnd;
  private int myState;

  private final @NotNull PerlLexingContext myPerlLexingContext;

  public PerlCodeMergingLexerAdapter(@NotNull PerlLexingContext perlLexingContext) {
    myPerlLexingContext = perlLexingContext;
    myPerlLexer = new PerlLexer(null).withProject(perlLexingContext.getProject());
  }

  @Override
  public void start(final @NotNull CharSequence buffer, int startOffset, int endOffset, final int initialState) {
    myText = buffer;
    myTokenStart = myTokenEnd = myBufferStart = startOffset;
    myBufferEnd = endOffset;
    int enforcedInitialState = myPerlLexingContext.getEnforcedInitialState();
    myPerlLexer.reset(myText, startOffset, endOffset, enforcedInitialState >= 0 ? enforcedInitialState : initialState);
    myPerlLexer.setSingleOpenQuoteChar(myPerlLexingContext.getOpenChar());
    myPerlLexer.setHasTryCatch(myPerlLexingContext.isWithTryCatch());
    myTokenType = null;
  }

  @Override
  public int getState() {
    locateToken();
    return myState;
  }

  @Override
  public IElementType getTokenType() {
    locateToken();
    return myTokenType;
  }

  @Override
  public int getTokenStart() {
    locateToken();
    return myTokenStart;
  }

  @Override
  public int getTokenEnd() {
    locateToken();
    return myTokenEnd;
  }

  @Override
  public void advance() {
    locateToken();
    myTokenType = null;
  }

  @Override
  public @NotNull CharSequence getBufferSequence() {
    return myText;
  }

  @Override
  public int getBufferEnd() {
    return myBufferEnd;
  }

  protected void locateToken() {
    if (myTokenType != null) {
      return;
    }

    try {
      myTokenStart = myPerlLexer.getTokenEnd();
      myState = myPerlLexer.yystate();
      myTokenType = myPerlLexer.advance();
      myTokenEnd = myPerlLexer.getTokenEnd();
      mergeCode();
    }
    catch (Exception | Error e) {
      LOG.error(myPerlLexer.getClass().getName(), e);
      myTokenType = TokenType.WHITE_SPACE;
      myTokenEnd = myBufferEnd;
    }
  }

  protected void mergeCode() throws IOException {
    if (myTokenType != LEFT_BRACE_CODE_START) {
      return;
    }
    if (myTokenStart == myBufferStart || !myPerlLexingContext.isAllowToMergeCode() || myPerlLexingContext.isEnforceSubLexing()) {
      myTokenType = LEFT_BRACE;
      return;
    }

    int bracesDepth = 0;
    while (true) {
      IElementType nextTokenType = myPerlLexer.advance();
      if (nextTokenType == null) {
        break;
      }
      else if (nextTokenType == LEFT_BRACE || nextTokenType == LEFT_BRACE_CODE_START) {
        bracesDepth++;
      }
      else if (nextTokenType == RIGHT_BRACE) {
        if (bracesDepth == 0) {
          break;
        }
        bracesDepth--;
      }
    }
    myTokenEnd = myPerlLexer.getTokenEnd();
    myTokenType = myPerlLexer.getLPCodeBlockElementType();
    myState = PerlLexer.YYINITIAL;
  }

  @NotNull PerlBaseLexer getPerlLexer() {
    return myPerlLexer;
  }
}
