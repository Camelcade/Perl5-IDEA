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

	public IElementType advance() throws IOException
	{
		CharSequence buffer = getBuffer();
		int tokenStart = getNextTokenStart();
		if( tokenStart == 0 || tokenStart < buffer.length() - 2 && buffer.charAt(tokenStart)=='?' && buffer.charAt(tokenStart+1)=='>')
		{
			setTokenStart(tokenStart);
			for( int offset = tokenStart; offset < buffer.length()-2; offset++)
			{
				if( buffer.charAt(offset) == '<' && buffer.charAt(offset+1) == '?' )
				{
					setTokenEnd(offset+2);
					return TEMPLATE_BLOCK_HTML;
				}
			}
			setTokenEnd(buffer.length());
			return TEMPLATE_BLOCK_HTML;
		}

		return super.advance();
	}
}
