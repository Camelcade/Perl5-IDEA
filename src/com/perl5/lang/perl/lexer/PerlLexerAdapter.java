package com.perl5.lang.perl.lexer;

/**
 * Created by hurricup on 12.04.2015.
 */
import com.intellij.lexer.FlexAdapter;

import java.io.Reader;

import com.intellij.lexer.LayeredLexer;

public class PerlLexerAdapter extends FlexAdapter {
	public PerlLexerAdapter() {
		super(new PerlLexer((Reader) null));
	}
}