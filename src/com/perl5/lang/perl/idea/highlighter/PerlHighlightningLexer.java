package com.perl5.lang.perl.idea.highlighter;

import com.intellij.lexer.*;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.lexer.LayeredLexerAdaptive;
import com.perl5.lang.perl.lexer.PerlLexerAdapter;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.pod.idea.highlighter.PodHighlightingLexer;

/**
 * Created by hurricup on 24.04.2015.
 */
public class PerlHighlightningLexer extends LayeredLexerAdaptive implements PerlElementTypes
{
	protected boolean hasNestedPodLexer = true;

	/**
	 * Lexers to use in multiline
	 */
	protected final Lexer htmlLexer = new HtmlHighlightingLexer();
	protected final Lexer xhtmlLexer = new XHtmlHighlightingLexer();
	protected final Lexer xmlLexer = new XmlHighlightingLexer();

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

	protected Lexer nextMultilineLexer = null;

	@Override
	protected Lexer getLexerByTokenType(IElementType tokenType)
	{
		Lexer lexer = null;

		/**
		 * This is logic of choosing lexer for later tokens, could be in other method
		 */
		// @todo should be done using map
		if( tokenType == PERL_MULTILINE_MARKER_HTML)
			nextMultilineLexer = htmlLexer;
		else if( tokenType == PERL_MULTILINE_MARKER_XHTML)
			nextMultilineLexer = xhtmlLexer;
		else if( tokenType == PERL_MULTILINE_MARKER_XML)
			nextMultilineLexer = xmlLexer;

		/**
		 *  Here we choose lexer
		 */
		if( tokenType == PERL_STRING_MULTILINE)
		{
			lexer = nextMultilineLexer;
			nextMultilineLexer = null;
		}

		if( lexer == null )
			lexer = super.getLexerByTokenType(tokenType);

		return lexer;
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

		registerSelfStoppingLexer(htmlLexer, IElementType.EMPTY_ARRAY);
		registerSelfStoppingLexer(xhtmlLexer, IElementType.EMPTY_ARRAY);
		registerSelfStoppingLexer(xmlLexer, IElementType.EMPTY_ARRAY);

	}

}
