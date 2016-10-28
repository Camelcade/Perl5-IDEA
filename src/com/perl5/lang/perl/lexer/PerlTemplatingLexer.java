package com.perl5.lang.perl.lexer;

import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.COMMENT_ANNOTATION;
import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.COMMENT_LINE;

public abstract class PerlTemplatingLexer extends PerlProtoLexer
{
	protected final PerlLexer myPerlLexer = new PerlLexer(null);
	private final CommentEndCalculator myCommentEndCalculator;

	public PerlTemplatingLexer()
	{
		myCommentEndCalculator = getCommentEndCalculator();
	}

	/**
	 * syncronizes position of perl lexer with main one
	 */
	private void syncPerlLexer()
	{
		myPerlLexer.setTokenEnd(getTokenEnd());
	}

	/**
	 * syncronizes position of the main lexer with perl one
	 */
	private void syncMainLexer()
	{
		setTokenEnd(myPerlLexer.getTokenEnd());
	}

	@Override
	public IElementType advance() throws IOException
	{
		if (myPerlLexer.hasPreparsedTokens())
		{
			IElementType result = myPerlLexer.advance();
			syncMainLexer();
			return result;
		}
		return super.advance();
	}

	@Override
	protected void resetInternals()
	{
		super.resetInternals();
		myPerlLexer.reset(getBuffer(), getBufferStart(), getBufferEnd(), 0);
		assert yystate() == 0;
	}

	/**
	 * Delegating current position to the perl lexer
	 */
	protected IElementType delegateLexing()
	{
		yypushback(yylength());
		syncPerlLexer();
		try
		{
			IElementType result = myPerlLexer.advance();
			if (myCommentEndCalculator != null && (result == COMMENT_LINE || result == COMMENT_ANNOTATION))
			{
				int endIndex = myCommentEndCalculator.getCommentEndOffset(myPerlLexer.yytext());
				if (endIndex > -1)
				{
					myPerlLexer.setTokenEnd(myPerlLexer.getTokenStart() + endIndex);
				}
			}
			syncMainLexer();
			return result;
		}
		catch (Exception ignore)
		{
		}
		throw new RuntimeException("Something bad happened");
	}

	@Nullable
	protected CommentEndCalculator getCommentEndCalculator()
	{
		return null;
	}

	@Override
	public boolean isInitialState()
	{
		return super.isInitialState() && myPerlLexer.yystate() == 0;
	}

	public interface CommentEndCalculator
	{
		/**
		 * Finds real comment end offset
		 *
		 * @param commentText comment text, found by perl lexer
		 * @return comment end offset or -1 if comment is ok
		 */
		int getCommentEndOffset(CharSequence commentText);
	}
}
