package com.perl5.lang.pod.idea.highlighter;

import com.intellij.lexer.LayeredLexer;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.idea.highlighter.PerlHighlightningLexer;
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
			new PerlHighlightningLexer(false),
			new IElementType[]{PodElementTypes.POD_CODE},
			IElementType.EMPTY_ARRAY
		);
	}

}
