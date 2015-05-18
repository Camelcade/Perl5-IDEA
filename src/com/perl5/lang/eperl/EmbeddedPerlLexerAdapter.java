package com.perl5.lang.eperl;

import com.intellij.lexer.FlexAdapter;
import com.perl5.lang.eperl.lexer.EmbeddedPerlLexer;
import com.perl5.lang.perl.lexer.PerlLexer;

import java.io.Reader;

/**
 * Created by hurricup on 18.05.2015.
 */
public class EmbeddedPerlLexerAdapter extends FlexAdapter
{
	public EmbeddedPerlLexerAdapter() {
		super(new EmbeddedPerlLexer((Reader) null));
	}
}
