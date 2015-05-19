package com.perl5.lang.embedded.idea;

import com.intellij.lexer.Lexer;
import com.perl5.lang.embedded.EmbeddedPerlLexerAdapter;
import com.perl5.lang.perl.idea.highlighter.PerlSyntaxHighlighter;
import com.perl5.lang.pod.idea.highlighter.PodHighlightingLexer;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 19.05.2015.
 */
public class EmbeddedPerlSyntaxHighlighter extends PerlSyntaxHighlighter
{
	@NotNull
	@Override
	public Lexer getHighlightingLexer()
	{
		return new EmbeddedPerlLexerAdapter();
	}
}
