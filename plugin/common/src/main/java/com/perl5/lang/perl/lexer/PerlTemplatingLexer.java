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

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

import static com.perl5.lang.perl.parser.PerlElementTypesGenerated.COMMENT_ANNOTATION;
import static com.perl5.lang.perl.parser.PerlElementTypesGenerated.COMMENT_LINE;

public abstract class PerlTemplatingLexer extends PerlProtoLexer {
  private static final Logger LOG = Logger.getInstance(PerlTemplatingLexer.class);
  protected final PerlLexer myPerlLexer = new PerlLexer(null);

  public PerlTemplatingLexer withProject(@Nullable Project project) {
    myPerlLexer.withProject(project);
    return this;
  }

  /**
   * syncronizes position of perl lexer with main one
   */
  private void syncPerlLexer() {
    myPerlLexer.setTokenEnd(getTokenEnd());
  }

  /**
   * syncronizes position of the main lexer with perl one
   */
  private void syncMainLexer() {
    setTokenEnd(myPerlLexer.getTokenEnd());
  }

  @Override
  public IElementType advance() throws IOException {
    if (myPerlLexer.hasPreparsedTokens()) {
      IElementType result = myPerlLexer.advance();
      syncMainLexer();
      if (LOG.isTraceEnabled()) {
        LOG.debug("Preparsed by perl lexer: '", myPerlLexer.yytext(),
                  "'; state: ", myPerlLexer.yystate(),
                  "; real state: ", myPerlLexer.getRealLexicalState(),
                  "; tokenType: ", result,
                  "; start: ", myPerlLexer.getTokenStart(),
                  "; end: ", myPerlLexer.getTokenEnd()
        );
      }
      return result;
    }

    IElementType result = super.advance();
    if (LOG.isTraceEnabled()) {
      LOG.debug("From template lexer: '", yytext(),
                "'; state: ", yystate(),
                "; real state: ", getRealLexicalState(),
                "; tokenType: ", result,
                "; start: ", getTokenStart(),
                "; end: ", getTokenEnd()
      );
    }
    return result;
  }

  public int getPerlLexerState() {
    return myPerlLexer.yystate();
  }

  @Override
  protected void resetInternals() {
    super.resetInternals();
    int packedLexicalState = getRealLexicalState();
    yybegin(getTemplateLexerState(packedLexicalState));
    myPerlLexer.reset(getBuffer(), getBufferStart(), getBufferEnd(), getPerlLexerState(packedLexicalState));
  }

  public static int packState(int perlLexerState, int templateLexerState) {
    return (templateLexerState << 16) + perlLexerState;
  }

  public static int getPerlLexerState(int packedState) {
    return packedState & 0xFFFF;
  }

  public static int getTemplateLexerState(int packedState) {
    return packedState >> 16;
  }

  /**
   * Delegating current position to the perl lexer
   */
  protected IElementType delegateLexing() {
    yypushback(yylength());
    syncPerlLexer();
    try {
      IElementType result = myPerlLexer.advance();
      CommentEndCalculator commentEndCalculator = getCommentEndCalculator();
      if (commentEndCalculator != null && (result == COMMENT_LINE || result == COMMENT_ANNOTATION)) {
        int endIndex = commentEndCalculator.getCommentEndOffset(myPerlLexer.yytext());
        if (endIndex > -1) {
          myPerlLexer.setTokenEnd(myPerlLexer.getTokenStart() + endIndex);
        }
      }
      syncMainLexer();
      return result;
    }
    catch (Exception ignore) {
    }
    throw new RuntimeException("Something bad happened");
  }

  protected @Nullable CommentEndCalculator getCommentEndCalculator() {
    return null;
  }

  protected void startPerlExpression() {
    myPerlLexer.pushStateAndBegin(0);
  }

  protected void endPerlExpression() {
    myPerlLexer.popState();
  }

  protected void setPerlToInitial() {
    myPerlLexer.yybegin(0);
  }

  public interface CommentEndCalculator {
    /**
     * Finds real comment end offset
     *
     * @param commentText comment text, found by perl lexer
     * @return comment end offset or -1 if comment is ok
     */
    int getCommentEndOffset(CharSequence commentText);
  }
}
