package com.perl5.lang.perl.lexer;

import com.intellij.openapi.project.Project;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.COMMENT_LINE;

public abstract class PerlTemplatingLexer extends PerlProtoLexer
{
	private final PerlLexer myPerlLexer = new PerlLexer(null);

	/**
	 * syncronizes position of perl lexer with main one
	 */
	private void syncPerlLexer()
	{
		myPerlLexer.setTokenStart(getTokenStart());
		myPerlLexer.setTokenEnd(getTokenEnd());
	}

	/**
	 * syncronizes position the main lexer with perl one
	 */
	private void syncMainLexer()
	{
		setTokenStart(myPerlLexer.getTokenStart());
		setTokenEnd(myPerlLexer.getTokenEnd());
	}

	@Override
	public IElementType advance() throws IOException
	{
		if (myPerlLexer.hasPreparsedTokens())
		{
			IElementType result = myPerlLexer.advance();
			if (!myPerlLexer.hasPreparsedTokens())
			{
				syncMainLexer();
			}
			return result;
		}
		return super.advance();
	}

	@Override
	protected void resetInternals()
	{
		super.resetInternals();
		assert yystate() == 0;
		myPerlLexer.reset(getBuffer(), getBufferStart(), getBufferEnd(), 0);
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
			if (result == COMMENT_LINE)
			{
				adjustCommentToken();
			}
			syncMainLexer();
			return result;
		}
		catch (Exception ignore)
		{
		}
		throw new RuntimeException("Something bad happened");
	}

	/**
	 * Implemented for templating languages, which can seek through the comment token (current one) and adjust end
	 * position with setTokenEnd() method
	 */
	public void adjustCommentToken()
	{
	}

	@Override
	public boolean isInitialState()
	{
		return super.isInitialState() && myPerlLexer.yystate() == 0;
	}
}
