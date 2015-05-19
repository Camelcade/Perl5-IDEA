package com.perl5.lang.embedded;

import com.intellij.lexer.FlexAdapter;

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
