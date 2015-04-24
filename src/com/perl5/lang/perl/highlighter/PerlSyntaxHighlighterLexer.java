package com.perl5.lang.perl.highlighter;

import com.intellij.lexer.FlexAdapter;
import com.intellij.lexer.LayeredLexer;
import com.intellij.lexer.XmlHighlightingLexer;
import com.intellij.lexer._HtmlLexer;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.lexer.PerlLexerAdapter;
import com.perl5.lang.perl.lexer.PerlTokenTypes;
import com.perl5.lang.pod.highlighter.PodHighlightingLexer;
import com.perl5.lang.pod.lexer.PodLexerAdapter;

/**
 * Created by hurricup on 24.04.2015.
 */
public class PerlSyntaxHighlighterLexer extends LayeredLexer
{
	protected boolean hasNestedPodLexer = true;

	public PerlSyntaxHighlighterLexer(boolean nestedPod)
	{
		super(new PerlLexerAdapter());
		hasNestedPodLexer = nestedPod;
		registerSublexers();
	}


	public PerlSyntaxHighlighterLexer()
	{
		super(new PerlLexerAdapter());

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
	}

}
