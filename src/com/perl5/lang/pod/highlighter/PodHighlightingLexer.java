package com.perl5.lang.pod.highlighter;

import com.intellij.lexer.LayeredLexer;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.highlighter.PerlSyntaxHighlighterLexer;
import com.perl5.lang.perl.lexer.PerlLexerAdapter;
import com.perl5.lang.pod.PodParserDefinition;
import com.perl5.lang.pod.lexer.PodElementTypes;
import com.perl5.lang.pod.lexer.PodLexerAdapter;

/**
 * Created by hurricup on 24.04.2015.
 */
public class PodHighlightingLexer extends LayeredLexer
{
	public PodHighlightingLexer()
	{
		super(new PodLexerAdapter());

		registerSelfStoppingLayer(
			new PerlSyntaxHighlighterLexer(false),
			new IElementType[]{PodElementTypes.POD_CODE},
			IElementType.EMPTY_ARRAY
		);
	}

}
