package com.perl5.lang.perl.lexer;

import com.intellij.openapi.project.Project;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.COMMENT_ANNOTATION;
import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.COMMENT_LINE;

public abstract class PerlTemplatingLexer extends PerlProtoLexer {
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
      return result;
    }
    return super.advance();
  }

  @Override
  protected void resetInternals() {
    super.resetInternals();
    myPerlLexer.reset(getBuffer(), getBufferStart(), getBufferEnd(), 0);
    assert yystate() == 0 : "Got yystate: " + yystate() + " instead of 0";
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

  @Nullable
  protected CommentEndCalculator getCommentEndCalculator() {
    return null;
  }

  @Override
  public boolean isInitialState() {
    return super.isInitialState() && myPerlLexer.yystate() == 0;
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
