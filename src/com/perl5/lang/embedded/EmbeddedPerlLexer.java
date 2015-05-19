package com.perl5.lang.embedded;

import com.intellij.psi.tree.IElementType;
import com.intellij.util.text.ImmutableText;
import com.perl5.lang.perl.lexer.PerlLexer;

import java.io.IOException;

/**
 * Created by hurricup on 19.05.2015.
 */
public class EmbeddedPerlLexer extends PerlLexer
{
	public EmbeddedPerlLexer(java.io.Reader in) {
		super(in);
	}

	@Override
	public void reset(CharSequence buf, int start, int end, int initialState)
	{
		if( start == 0 )
			initialState = LEX_HTML_BLOCK;

		super.reset(buf, start, end, initialState);
	}

	private int preHTMLState = YYINITIAL;

	public IElementType advance() throws IOException
	{
		CharSequence buffer = getBuffer();
		int tokenStart = getNextTokenStart();

		if( yystate() == LEX_HTML_BLOCK )
		{
			setTokenStart(tokenStart);
			if(tokenStart < buffer.length() - 1 && buffer.charAt(tokenStart) == '<' && buffer.charAt(tokenStart+1) == '?') // finishing html block
			{
				setState(preHTMLState);
				setTokenEnd(tokenStart + 2);
				return EMBED_MARKER;
			}
			else
			{
				for (int offset = tokenStart; offset < buffer.length() - 3; offset++)
				{
					if (buffer.charAt(offset + 1) == '<' && buffer.charAt(offset + 2) == '?')
					{
						setTokenEnd(offset + 1);
						return TEMPLATE_BLOCK_HTML;
					}
				}
				setState(preHTMLState);
				setTokenEnd(buffer.length());
				return TEMPLATE_BLOCK_HTML;
			}
		}
		else if( tokenStart < buffer.length() - 2 && buffer.charAt(tokenStart)=='?' && buffer.charAt(tokenStart+1)=='>')
		{
			preHTMLState = yystate();
			yybegin(LEX_HTML_BLOCK);
			setTokenStart(tokenStart);
			setTokenEnd(tokenStart + 2);
			return EMBED_MARKER;
		}

		return super.advance();
	}
}
