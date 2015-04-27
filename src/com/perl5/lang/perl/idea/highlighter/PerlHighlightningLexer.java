package com.perl5.lang.perl.idea.highlighter;

import com.intellij.lexer.*;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.lexer.PerlLexerAdapter;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.pod.idea.highlighter.PodHighlightingLexer;

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
				new IElementType[]{PerlElementTypes.PERL_POD},
				IElementType.EMPTY_ARRAY
			);
		}

		registerSelfStoppingLayer(
				new HtmlHighlightingLexer(),
				new IElementType[]{PerlElementTypes.PERL_STRING_MULTILINE_HTML},
				IElementType.EMPTY_ARRAY
		);


		registerSelfStoppingLayer(
				new XmlHighlightingLexer(),
				new IElementType[]{PerlElementTypes.PERL_STRING_MULTILINE_XML},
				IElementType.EMPTY_ARRAY
		);

		registerSelfStoppingLayer(
				new XHtmlHighlightingLexer(),
				new IElementType[]{PerlElementTypes.PERL_STRING_MULTILINE_XHTML},
				IElementType.EMPTY_ARRAY
		);

	}

}
