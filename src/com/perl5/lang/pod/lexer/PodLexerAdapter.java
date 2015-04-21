package com.perl5.lang.pod.lexer;

import com.intellij.lexer.FlexAdapter;

import java.io.Reader;

/**
 * Created by hurricup on 21.04.2015.
 */
public class PodLexerAdapter extends FlexAdapter
{
	public PodLexerAdapter() {
		super(new PodLexer((Reader) null));
	}
}
