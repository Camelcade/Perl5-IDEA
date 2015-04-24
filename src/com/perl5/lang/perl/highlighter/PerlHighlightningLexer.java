package com.perl5.lang.perl.highlighter;

import com.intellij.lexer.LayeredLexer;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.lexer.PerlLexerAdapter;
import com.perl5.lang.perl.lexer.PerlTokenTypes;
import com.perl5.lang.pod.highlighter.PodHighlightingLexer;

/**
 * Created by hurricup on 24.04.2015.
 */
public class PerlHighlightningLexer extends LayeredLexer
{
	protected boolean hasNestedPodLexer = true;

	public PerlHighlightningLexer(boolean nestedPod)
	{
		super(new PerlLexerAdapter());
		hasNestedPodLexer = nestedPod;
		registerSublexers();
	}


	public PerlHighlightningLexer()
	{
		super(new PerlLexerAdapter());
		registerSublexers();
	}

	protected void registerSublexers()
	{
		if( hasNestedPodLexer )
		{
			registerSelfStoppingLayer(
				new PodHighlightingLexer(),
				new IElementType[]{PerlTokenTypes.PERL_POD},
				IElementType.EMPTY_ARRAY
			);
		}
/*
		registerSelfStoppingLayer(
				new FlexAdapter(new _HtmlLexer()),
				new IElementType[]{PerlTokenTypes.PERL_MULTILINE_HTML},
				IElementType.EMPTY_ARRAY
		);
		registerSelfStoppingLayer(
				new XmlHighlightingLexer(),
				new IElementType[]{PerlTokenTypes.PERL_MULTILINE_XML},
				IElementType.EMPTY_ARRAY
		);
*/
	}

}
