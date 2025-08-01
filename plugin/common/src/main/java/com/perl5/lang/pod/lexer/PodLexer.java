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

package com.perl5.lang.pod.lexer;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Reader;

import static com.perl5.lang.pod.parser.PodElementTypesGenerated.*;


@SuppressWarnings("ALL")
public class PodLexer extends PodLexerGenerated {
  private static final Logger LOG = Logger.getInstance(PodLexer.class);
  private final IntArrayList myOpenedAngles = new IntArrayList();

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
    return myPreparsedTokensList.isEmpty() && myOpenedAngles.isEmpty() ? super.yystate() : LEX_PREPARSED_ITEMS;
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
    if (!myOpenedAngles.isEmpty() && myOpenedAngles.getInt(myOpenedAngles.size() - 1) > 1) {
      yybegin(CLOSING_SPACED_ANGLE);
    }
  }

  /**
   * Invoked on close angle of any size.
   *
   * @return if it's matching close angle - returns angle. Symbol otherwise
   */
  @NotNull
  protected IElementType popAngle() {
    int tokenLength = yylength();
    if (myOpenedAngles.isEmpty()) {
      // not counting
      return processAsSingleSymbol(tokenLength);
    }
    int openerLength = myOpenedAngles.getInt(myOpenedAngles.size() - 1);
    if (openerLength > tokenLength) {
      // unbalanced closing angle, seems we can handle this
      return processAsSingleSymbol(tokenLength);
    }
    myOpenedAngles.popInt();
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
  @NotNull
  protected IElementType lexOptional(@NotNull IElementType type) {
    return myOpenedAngles.isEmpty() ? POD_SYMBOL : type;
  }
}
