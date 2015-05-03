package com.perl5.lang.perl.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

/**
 * Created by hurricup on 03.05.2015.
 */
public interface PerlLexerBase extends FlexLexer, PerlElementTypes
{
	// @todo should be parseString
	public abstract IElementType processStringOpener();

	public abstract IElementType processDiv();
	public abstract IElementType parseRegex();
	public abstract IElementType getParsedToken();

	public abstract void popState();

	public abstract IElementType processSemicolon();
	public abstract IElementType processNewLine();

	public abstract void processPodOpener();
	public abstract IElementType endPodBlock();

	public abstract void processDataOpener();
	public abstract IElementType endDataBlock();

	public abstract boolean isMultilineEnd();
	public abstract IElementType processMultilineOpener();
	public abstract IElementType endMultiline();

	public abstract IElementType processOpenerWhiteSpace();

	public abstract IElementType processTransOpener();
	public abstract IElementType processTransQuote();
	public abstract IElementType processTransChar();
	public abstract IElementType processTransCloser();

	public abstract IElementType processRegexOpener();

	public abstract IElementType processQuoteLikeListQuote();
	public abstract IElementType processQuoteLikeListOpener();

	public abstract IElementType processQuoteLikeStringOpener();
	public abstract IElementType processQuoteLikeChar();

	public abstract IElementType processQuoteLikeQuote();
	public abstract IElementType processQuoteLikeWord();

}

