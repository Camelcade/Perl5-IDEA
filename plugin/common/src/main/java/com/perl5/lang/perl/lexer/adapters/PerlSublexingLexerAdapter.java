/*
 * Copyright 2015-2025 Alexandr Evstigneev
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
 * First level adapter, relexes lazy blocks if necessary
 *
 * @see com.perl5.lang.perl.lexer.adapters.PerlMergingLexerAdapter
 */

import com.intellij.lexer.FlexAdapter;
import com.intellij.lexer.Lexer;
import com.intellij.lexer.LexerBase;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.lexer.PerlLexer;
import com.perl5.lang.perl.lexer.PerlLexingContext;
import com.perl5.lang.perl.lexer.PerlProtoLexer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class PerlSublexingLexerAdapter extends LexerBase implements PerlElementTypes {
  private static final Logger LOG = Logger.getInstance(FlexAdapter.class);
  private static final Map<IElementType, Integer> SUBLEXINGS_MAP = new HashMap<>();
  private static final Map<IElementType, Integer> ENFORCED_SUBLEXINGS_MAP = new HashMap<>();

  static {
    SUBLEXINGS_MAP.put(LP_STRING_Q, PerlLexer.STRING_Q);
    SUBLEXINGS_MAP.put(LP_STRING_QQ, PerlLexer.STRING_QQ);
    SUBLEXINGS_MAP.put(LP_STRING_TR, PerlLexer.STRING_TR_BEGIN);
    SUBLEXINGS_MAP.put(LP_STRING_RE, PerlLexer.STRING_RE);
    SUBLEXINGS_MAP.put(LP_STRING_QX, PerlLexer.STRING_QX);
    SUBLEXINGS_MAP.put(LP_STRING_QQ_RESTRICTED, PerlLexer.STRING_QQ_RESTRICTED);
    SUBLEXINGS_MAP.put(LP_STRING_QX_RESTRICTED, PerlLexer.STRING_QX_RESTRICTED);
    SUBLEXINGS_MAP.put(LP_CODE_BLOCK, PerlLexer.YYINITIAL);
    SUBLEXINGS_MAP.put(LP_CODE_BLOCK_WITH_TRYCATCH, PerlLexer.YYINITIAL);
    SUBLEXINGS_MAP.put(LP_STRING_QW, PerlLexer.STRING_LIST);
    SUBLEXINGS_MAP.put(LP_REGEX, PerlLexer.MATCH_REGEX);
    SUBLEXINGS_MAP.put(LP_REGEX_X, PerlLexer.MATCH_REGEX_X);
    SUBLEXINGS_MAP.put(LP_REGEX_XX, PerlLexer.MATCH_REGEX_XX);
    SUBLEXINGS_MAP.put(LP_REGEX_SQ, PerlLexer.MATCH_REGEX_SQ);
    SUBLEXINGS_MAP.put(LP_REGEX_X_SQ, PerlLexer.MATCH_REGEX_X_SQ);
    SUBLEXINGS_MAP.put(LP_REGEX_XX_SQ, PerlLexer.MATCH_REGEX_XX_SQ);

    ENFORCED_SUBLEXINGS_MAP.put(COMMENT_ANNOTATION, PerlLexer.ANNOTATION);
    ENFORCED_SUBLEXINGS_MAP.put(HEREDOC, PerlLexer.STRING_Q);
    ENFORCED_SUBLEXINGS_MAP.put(HEREDOC_QQ, PerlLexer.STRING_QQ);
    ENFORCED_SUBLEXINGS_MAP.put(HEREDOC_QX, PerlLexer.STRING_QX);
  }

  private final @NotNull PerlProtoLexer myPerlLexer;
  private final @NotNull FlexAdapter myFlexAdapter;
  private final @NotNull PerlLexingContext myLexingContext;
  private boolean myIsSublexing = false;
  private PerlSublexingLexerAdapter mySubLexer;
  private int myTokenStart;
  private int myTokenEnd;
  private int myState;
  private IElementType myTokenType;
  private char mySingleOpenQuoteChar = 0;
  private int myAdvancesCounter = 0;

  public PerlSublexingLexerAdapter(@NotNull PerlLexingContext perlLexingContext) {
    this(new PerlLexer(null).withProject(perlLexingContext.getProject()), perlLexingContext);
  }

  public PerlSublexingLexerAdapter(@NotNull PerlProtoLexer perlLexer, @NotNull PerlLexingContext lexingContext) {
    myPerlLexer = perlLexer;
    myFlexAdapter = new FlexAdapter(perlLexer);
    myLexingContext = lexingContext;
  }

  /**
   * Starts lexing of single quoted string content
   */
  public void start(@NotNull CharSequence buffer,
                    int startOffset,
                    int endOffset,
                    int subLexingState,
                    char openQuoteChar) {
    LOG.assertTrue(subLexingState == PerlLexer.STRING_Q || subLexingState == PerlLexer.STRING_LIST);
    start(buffer, startOffset, endOffset, subLexingState);
    PerlLexer perlLexer = getPerlLexer();
    if (perlLexer == null) {
      LOG.error("Expected to get perl lexer, got " + myPerlLexer);
      return;
    }
    perlLexer.setSingleOpenQuoteChar(openQuoteChar);
  }

  @Override
  public void start(@NotNull CharSequence buffer, int startOffset, int endOffset, int initialState) {
    PerlLexingContext lexingContext = getLexingContext();
    getFlexAdapter().start(buffer, startOffset, endOffset, initialState);
    PerlLexer perlLexer = getPerlLexer();
    if (perlLexer != null) {
      perlLexer.setSingleOpenQuoteChar(lexingContext.getOpenChar());
      perlLexer.setHasTryCatch(lexingContext.isWithTryCatch());
    }
    myTokenStart = myTokenEnd = startOffset;
    myTokenType = null;
    myIsSublexing = false;
  }

  private @Nullable PerlLexer getPerlLexer() {
    return myPerlLexer instanceof PerlLexer perlLexer ? perlLexer : null;
  }

  private @NotNull FlexAdapter getFlexAdapter() {
    return myFlexAdapter;
  }

  private @NotNull PerlLexingContext getLexingContext() {
    return myLexingContext;
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
    if (++myAdvancesCounter % PerlProgressAwareAdapter.CHECK_CANCEL_EACH_TOKEN == 0) {
      ProgressManager.checkCanceled();
    }
    locateToken();
    myTokenType = null;
  }

  @Override
  public @NotNull CharSequence getBufferSequence() {
    return getFlexAdapter().getBufferSequence();
  }

  @Override
  public int getBufferEnd() {
    return getFlexAdapter().getBufferEnd();
  }

  private @NotNull PerlSublexingLexerAdapter getSubLexer() {
    if (mySubLexer == null) {
      mySubLexer = new PerlSublexingLexerAdapter(getLexingContext().withOpenChar((char)0));
    }
    return mySubLexer;
  }

  protected void locateToken() {
    if (myTokenType != null) {
      return;
    }

    if (myIsSublexing) {
      lexToken(mySubLexer);

      if (myTokenType != null) {
        myState = PerlLexer.PREPARSED_ITEMS;
        return;
      }

      // sublexing finished
      myIsSublexing = false;
    }

    lexToken(getFlexAdapter());

    Integer subLexingState = SUBLEXINGS_MAP.get(myTokenType);

    boolean enforceSubLexing = getLexingContext().isEnforceSubLexing();
    if (subLexingState != null) {
      subLexCurrentToken(subLexingState);
    }
    else if (enforceSubLexing) {
      Integer initialState = ENFORCED_SUBLEXINGS_MAP.get(myTokenType);
      if (initialState != null) {
        subLexCurrentToken(initialState);
      }
    }
  }

  private void subLexCurrentToken(Integer subLexingState) {
    PerlSublexingLexerAdapter subLexer = getSubLexer();
    if (subLexingState == PerlLexer.STRING_Q || subLexingState == PerlLexer.STRING_LIST) {
      subLexer.start(getBufferSequence(), myTokenStart, myTokenEnd, subLexingState, mySingleOpenQuoteChar);
    }
    else {
      subLexer.start(getBufferSequence(), myTokenStart, myTokenEnd, subLexingState);
    }
    myIsSublexing = true;
    myTokenType = null;
    locateToken();
  }

  private void lexToken(Lexer lexer) {
    myTokenType = lexer.getTokenType();
    if (myTokenType == QUOTE_SINGLE_OPEN) {
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