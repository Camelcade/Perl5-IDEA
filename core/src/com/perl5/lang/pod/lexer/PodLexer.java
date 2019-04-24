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

package com.perl5.lang.pod.lexer;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.containers.IntStack;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Reader;

/**
 * Created by hurricup on 22.03.2016.
 */
@SuppressWarnings("ALL")
public class PodLexer extends PodLexerGenerated {
  private static final Logger LOG = Logger.getInstance(PodLexer.class);
  private final IntStack myOpenedAngles = new IntStack();

  public PodLexer(Reader in) {
    super(in);
  }

  @Override
  public void reset(CharSequence buffer, int start, int end, int initialState) {
    super.reset(buffer, start, end, initialState);
    if (start == 0 || start > 0 && buffer.charAt(start - 1) == '\n') {
      yybegin(LEX_COMMAND_WAITING);
    }
  }

  @Override
  protected void resetInternals() {
    super.resetInternals();
    myOpenedAngles.clear();
  }

  @Override
  public IElementType advance() throws IOException {
    IElementType result = super.advance();
    if (result == POD_NEWLINE) {
      myOpenedAngles.clear();
    }
    return result;
  }

  @Override
  public int yystate() {
    return preparsedTokensList.isEmpty() && myOpenedAngles.empty() ? super.yystate() : LEX_PREPARSED_ITEMS;
  }

  @Override
  public IElementType perlAdvance() throws IOException {
    IElementType result = super.perlAdvance();
    int state = getRealLexicalState();

    if (state == LEX_COMMAND_WAITING &&
        result != TokenType.WHITE_SPACE &&
        result != POD_NEWLINE &&
        result != POD_CODE &&
        result != POD_CUT ||
        state == LEX_COMMAND_READY &&
        result != TokenType.WHITE_SPACE &&
        result != POD_NEWLINE &&
        result != POD_CODE) {
      yybegin(YYINITIAL);
    }

    return result;
  }

  protected IElementType parseFallback() {
    int tokenStart = getTokenStart();
    int bufferEnd = getBufferEnd();
    CharSequence buffer = getBuffer();

    if (tokenStart < bufferEnd) {
      char currentChar = buffer.charAt(tokenStart);
      if (isIdentifierCharacter(currentChar)) {
        int tokenEnd = getTokenEnd();

        while (tokenEnd < bufferEnd && isIdentifierCharacter(buffer.charAt(tokenEnd)) && !isPodTag(buffer, tokenEnd, bufferEnd)) {
          tokenEnd++;
        }

        setTokenEnd(tokenEnd);
        return POD_IDENTIFIER;
      }
      else {
        return POD_SYMBOL;
      }
    }
    throw new RuntimeException("Can't be");
  }

  protected boolean isIdentifierCharacter(char myChar) {
    return myChar == '_' || Character.isLetterOrDigit(myChar);
  }

  protected boolean isPodTag(CharSequence buffer, int offset, int bufferEnd) {
    if (offset + 1 < bufferEnd) {
      return StringUtil.containsChar("IBCLEFSXZ", buffer.charAt(offset)) && buffer.charAt(offset + 1) == '<';
    }
    return false;
  }

  protected IElementType parseExample() {
    int offset = getTokenEnd();
    int tokenEnd = offset;
    int bufferEnd = getBufferEnd();
    CharSequence buffer = getBuffer();

    boolean newLine = false;
    boolean clearLine = false;

    while (offset < bufferEnd) {
      char currentChar = buffer.charAt(offset);

      if (currentChar == '\n') {
        newLine = true;

        if (!clearLine) {
          tokenEnd = offset + 1;
        }
        clearLine = true;
      }
      else {
        if (newLine && !Character.isWhitespace(currentChar)) {
          break;
        }

        newLine = false;
      }

      clearLine = clearLine && Character.isWhitespace(currentChar);

      offset++;
    }

    setTokenEnd(tokenEnd);
    yybegin(LEX_COMMAND_WAITING);

    return POD_CODE;
  }

  @Override
  protected IElementType parseCutToken() {
    int tokenEnd = getTokenEnd();
    int bufferEnd = getBufferEnd();
    CharSequence buffer = getBuffer();

    // this is a hack for joined tags from second psi tree, we may do this in separate lexer, to use it only in MultiPsi docs, but it's ok for now
    if (!(tokenEnd < bufferEnd && buffer.charAt(tokenEnd) == '=')) {
      yybegin(YYINITIAL);
    }
    return POD_CUT;
  }

  /**
   * Invoked on opening angle and angles / space after formatter key
   * Pushes angles length to the stack of angles to watch for the closing one
   */
  protected void pushAngle() {
    myOpenedAngles.push(yylength());
  }

  /**
   * Invoked on spaces following spaced angle.
   */
  protected void checkPendingSpacedAngles() {
    if (!myOpenedAngles.empty() && myOpenedAngles.peek() > 1) {
      yybegin(CLOSING_SPACED_ANGLE);
    }
  }

  /**
   * Invoked on close angle of any size.
   *
   * @return if it's matching close angle - returns angle. Symbol otherwise
   */
  protected IElementType popAngle() {
    int tokenLength = yylength();
    if (myOpenedAngles.empty()) {
      // not counting
      return processAsSingleSymbol(tokenLength);
    }
    int openerLength = myOpenedAngles.peek();
    if (openerLength > tokenLength) {
      // unbalanced closing angle, seems we can handle this
      return processAsSingleSymbol(tokenLength);
    }
    myOpenedAngles.pop();
    if (openerLength < tokenLength) {
      yypushback(tokenLength - openerLength);
    }
    return POD_ANGLE_RIGHT;
  }

  @NotNull
  private IElementType processAsSingleSymbol(int tokenLength) {
    if (tokenLength > 1) {
      yypushback(tokenLength - 1);
    }
    return POD_SYMBOL;
  }

  /**
   * Lexes curent token as target element if we are  inside formatter and as SYMBOL otherwise.
   */
  protected IElementType lexOptional(@NotNull IElementType type) {
    return myOpenedAngles.empty() ? POD_SYMBOL : type;
  }
}
