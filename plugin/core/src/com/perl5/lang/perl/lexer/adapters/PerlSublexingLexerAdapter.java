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

/**
 * Second level adapter, relexes lazy blocks if necessary
 *
 * @see com.perl5.lang.perl.lexer.adapters.PerlMergingLexerAdapter
 * @see com.perl5.lang.perl.lexer.adapters.PerlCodeMergingLexerAdapter
 */

import com.intellij.lexer.FlexAdapter;
import com.intellij.lexer.Lexer;
import com.intellij.lexer.LexerBase;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.lexer.LexerWithContext;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.lexer.PerlLexer;
import com.perl5.lang.perl.lexer.PerlLexingContext;
import gnu.trove.THashMap;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class PerlSublexingLexerAdapter extends LexerBase implements PerlElementTypes {
  private static final Logger LOG = Logger.getInstance(FlexAdapter.class);
  private static final int LAZY_BLOCK_MINIMAL_SIZE = 140;
  private static final Map<IElementType, Integer> SUBLEXINGS_MAP = new THashMap<>();

  static {
    SUBLEXINGS_MAP.put(LP_STRING_QW, PerlLexer.STRING_LIST);
    SUBLEXINGS_MAP.put(LP_STRING_Q, PerlLexer.STRING_Q);
    SUBLEXINGS_MAP.put(LP_STRING_QQ, PerlLexer.STRING_QQ);
    SUBLEXINGS_MAP.put(LP_STRING_TR, PerlLexer.STRING_TR_BEGIN);
    SUBLEXINGS_MAP.put(LP_STRING_RE, PerlLexer.STRING_RE);
    SUBLEXINGS_MAP.put(LP_STRING_QX, PerlLexer.STRING_QX);
    SUBLEXINGS_MAP.put(LP_STRING_QQ_RESTRICTED, PerlLexer.STRING_QQ_RESTRICTED);
    SUBLEXINGS_MAP.put(LP_STRING_QX_RESTRICTED, PerlLexer.STRING_QX_RESTRICTED);

    SUBLEXINGS_MAP.put(LP_REGEX, PerlLexer.MATCH_REGEX);
    SUBLEXINGS_MAP.put(LP_REGEX_X, PerlLexer.MATCH_REGEX_X);
    SUBLEXINGS_MAP.put(LP_REGEX_XX, PerlLexer.MATCH_REGEX_XX);
  }

  private final LexerWithContext myMainLexerWithContext;
  private boolean myIsSublexing = false;
  private PerlSublexingLexerAdapter mySubLexer;
  private int myTokenStart;
  private int myTokenEnd;
  private int myState;
  private IElementType myTokenType;
  private char mySingleOpenQuoteChar = 0;

  public PerlSublexingLexerAdapter(@NotNull PerlLexingContext perlLexingContext) {
    this(LexerWithContext.create(new PerlCodeMergingLexerAdapter(perlLexingContext), perlLexingContext));
  }

  public PerlSublexingLexerAdapter(@NotNull LexerWithContext lexerWithContext) {
    myMainLexerWithContext = lexerWithContext;
  }

  /**
   * Starts lexing of single quoted string content
   */
  public void start(@NotNull CharSequence buffer,
                    int startOffset,
                    int endOffset,
                    int subLexingState,
                    char openQuoteChar) {
    LOG.assertTrue(subLexingState == PerlLexer.STRING_Q);
    start(buffer, startOffset, endOffset, subLexingState);
    Lexer mainLexer = getMainLexer();
    LOG.assertTrue(mainLexer instanceof PerlCodeMergingLexerAdapter, "Got: " + myMainLexerWithContext);
    ((PerlCodeMergingLexerAdapter)mainLexer).getPerlLexer().setSingleOpenQuoteChar(openQuoteChar);
  }

  @Override
  public void start(@NotNull CharSequence buffer, int startOffset, int endOffset, int initialState) {
    getMainLexer().start(buffer, startOffset, endOffset, initialState);
    myTokenStart = myTokenEnd = startOffset;
    myTokenType = null;
    myIsSublexing = false;
  }

  private @NotNull Lexer getMainLexer() {
    return myMainLexerWithContext.getLexer();
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
    return getMainLexer().getBufferSequence();
  }

  @Override
  public int getBufferEnd() {
    return getMainLexer().getBufferEnd();
  }

  private @NotNull PerlSublexingLexerAdapter getSubLexer() {
    if (mySubLexer == null) {
      mySubLexer = new PerlSublexingLexerAdapter(myMainLexerWithContext.getLexingContext());
    }
    return mySubLexer;
  }

  protected void locateToken() {
    if (myTokenType != null) {
      return;
    }

    try {
      if (myIsSublexing) {
        lexToken(mySubLexer);

        if (myTokenType != null) {
          myState = PerlLexer.PREPARSED_ITEMS;
          return;
        }

        // sublexing finished
        myIsSublexing = false;
      }

      lexToken(getMainLexer());

      Integer subLexingState = SUBLEXINGS_MAP.get(myTokenType);

      if (subLexingState == null ||
          (myTokenEnd - myTokenStart > LAZY_BLOCK_MINIMAL_SIZE && !myMainLexerWithContext.getLexingContext().isEnforceSubLexing())) {
        return;
      }

      // need to sublex
      PerlSublexingLexerAdapter subLexer = getSubLexer();
      if (subLexingState == PerlLexer.STRING_Q) {
        subLexer.start(getBufferSequence(), myTokenStart, myTokenEnd, subLexingState, mySingleOpenQuoteChar);
      }
      else {
        subLexer.start(getBufferSequence(), myTokenStart, myTokenEnd, subLexingState);
      }
      myIsSublexing = true;
      myTokenType = null;
      locateToken();
    }
    catch (Exception | Error e) {
      LOG.error(myMainLexerWithContext.getClass().getName(), e);
      myTokenType = TokenType.WHITE_SPACE;
      myTokenEnd = getBufferEnd();
    }
  }

  private void lexToken(Lexer lexer) {
    myTokenType = lexer.getTokenType();
    if (myTokenType == LEFT_BRACE_CODE_START) {
      myTokenType = LEFT_BRACE;
    }
    else if (myTokenType == QUOTE_SINGLE_OPEN) {
      CharSequence tokenSequence = lexer.getTokenSequence();
      if (tokenSequence.length() != 1) {
        LOG.error("Got: " + tokenSequence);
      }
      mySingleOpenQuoteChar = tokenSequence.charAt(0);
    }
    else if (myTokenType == QUOTE_SINGLE_CLOSE) {
      mySingleOpenQuoteChar = 0;
    }
    myTokenStart = lexer.getTokenStart();
    myState = lexer.getState();
    myTokenEnd = lexer.getTokenEnd();
    lexer.advance();
  }
}