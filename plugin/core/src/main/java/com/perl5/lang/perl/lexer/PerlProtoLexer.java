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

package com.perl5.lang.perl.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.LinkedList;


public abstract class PerlProtoLexer implements FlexLexer {
  private static final Logger LOG = Logger.getInstance(PerlProtoLexer.class);
  protected final LinkedList<CustomToken> myPreparsedTokensList = new LinkedList<>();
  protected final IntArrayList myStateStack = new IntArrayList();
  private IElementType myLastTokenType = null;

  @SuppressWarnings("override")
  public abstract void setTokenStart(int position);

  @SuppressWarnings("override")
  public abstract void setTokenEnd(int position);

  @SuppressWarnings("override")
  public abstract CharSequence getBuffer();

  @SuppressWarnings("override")
  public abstract int getBufferStart();

  @SuppressWarnings("override")
  public abstract int getBufferEnd();

  @SuppressWarnings("override")
  public abstract int getNextTokenStart();

  protected void pushback() {
    yypushback(yylength());
  }

  @SuppressWarnings("override")
  public abstract void yypushback(int number);

  @SuppressWarnings("override")
  public abstract int yylength();

  @SuppressWarnings("override")
  public abstract IElementType perlAdvance() throws IOException;

  @SuppressWarnings("override")
  public abstract int getRealLexicalState();

  @SuppressWarnings("override")
  public abstract CharSequence yytext();

  public boolean hasPreparsedTokens() {
    return !myPreparsedTokensList.isEmpty();
  }

  /**
   * Checks internal lexer state aside of flex states
   *
   * @return true if it's safe to return YYINITIAL from yystate
   */
  public boolean isInitialState() {
    return myPreparsedTokensList.isEmpty() && myStateStack.isEmpty();
  }

  @Override
  public IElementType advance() throws IOException {
    IElementType tokenType;

    if (!myPreparsedTokensList.isEmpty()) {
      tokenType = getPreParsedToken();
    }
    else {
      tokenType = perlAdvance();
    }

    if (tokenType != null) {
      myLastTokenType = tokenType;
    }

    return tokenType;
  }

  protected @Nullable IElementType getLastTokenType() {
    return myLastTokenType;
  }

  /**
   * Reading tokens from parsed queue, setting start and end and returns them one by one
   *
   * @return token type or null if queue is empty
   */
  public IElementType getPreParsedToken() {
    return restoreToken(myPreparsedTokensList.removeFirst());
  }

  private IElementType restoreToken(CustomToken token) {
    setTokenStart(token.getTokenStart());
    setTokenEnd(token.getTokenEnd());
    return token.getTokenType();
  }

  public void pushStateAndBegin(int newState) {
    pushState();
    yybegin(newState);
  }

  public void pushStateAndBegin(int stateToPush, int newState) {
    yybegin(stateToPush);
    pushStateAndBegin(newState);
  }

  public void pushState() {
    myStateStack.push(getRealLexicalState());
  }

  public void popState() {
    if (myStateStack.isEmpty()) {
      LOG.error("Empty stack at " + getRealLexicalState() + "-" + yystate() +
                "; tokenText: '" + yytext() + "'"
      );
      return;
    }
    yybegin(myStateStack.popInt());
  }

  /**
   * Adds preparsed token to the queue with consistency control
   *
   * @param start     token start
   * @param end       token end
   * @param tokenType token type
   */
  protected void pushPreparsedToken(int start, int end, IElementType tokenType) {
    pushPreparsedToken(getCustomToken(start, end, tokenType));
  }

  /**
   * Checks if range contains only whitespace chars and pushes whitespace or passed tokentype
   *
   * @param start     start offset
   * @param end       end offset
   * @param tokenType tokentype to push if there are non-space chars
   */
  protected void pushPreparsedSpaceOrToken(int start, int end, IElementType tokenType) {
    pushPreparsedToken(start, end, isWhiteSpacesOnly(start, end) ? TokenType.WHITE_SPACE : tokenType);
  }

  /**
   * Checks if specified range contains only spaces
   *
   * @param start start offset
   * @param end   end offset
   * @return check result
   */
  protected boolean isWhiteSpacesOnly(int start, int end) {
    LOG.assertTrue(end <= getBufferEnd());
    LOG.assertTrue(start >= 0);
    CharSequence buffer = getBuffer();

    while (start < end) {
      if (!Character.isWhitespace(buffer.charAt(start))) {
        return false;
      }
      start++;
    }

    return true;
  }


  /**
   * Helper for creating custom token object
   *
   * @param start     token start
   * @param end       token end
   * @param tokenType token type
   * @return custom token object
   */
  protected CustomToken getCustomToken(int start, int end, IElementType tokenType) {
    return new CustomToken(start, end, tokenType);
  }

  /**
   * Adds preparsed token to the queue with consistency control
   *
   * @param token token to add
   */
  protected void pushPreparsedToken(CustomToken token) {
    if (!myPreparsedTokensList.isEmpty() && myPreparsedTokensList.getLast().getTokenEnd() != token.getTokenStart()) {
      LOG.error("Tokens list size=" + myPreparsedTokensList.size() +
                "; new token start=" + token.getTokenStart() +
                (myPreparsedTokensList.isEmpty() ? "" : " last token end=" + myPreparsedTokensList.getLast().getTokenEnd()));
    }

    myPreparsedTokensList.add(token);
  }

  protected void resetInternals() {
    myLastTokenType = null;
    myPreparsedTokensList.clear();
    myStateStack.clear();
  }

  /**
   * Checks if buffer at current offset contains specific string
   *
   * @param buffer  CharSequence buffer
   * @param offset  offset
   * @param pattern string to search
   * @return search result
   */
  public boolean isBufferAtString(CharSequence buffer, int offset, CharSequence pattern) {
    int patternEnd = offset + pattern.length();
    return getBufferEnd() >= patternEnd && StringUtil.equals(buffer.subSequence(offset, patternEnd), pattern);
  }
}
