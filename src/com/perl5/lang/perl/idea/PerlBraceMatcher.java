package com.perl5.lang.perl.idea;

import com.intellij.lang.BracePair;
import com.intellij.lang.PairedBraceMatcher;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 23.05.2015.
 */
public class PerlBraceMatcher implements PairedBraceMatcher, PerlElementTypes
{
	private static final BracePair[] PAIRS = new BracePair[]{
			new BracePair(PERL_LPAREN, PERL_RPAREN, false),
			new BracePair(PERL_LBRACK, PERL_RBRACK, false),
			new BracePair(PERL_LBRACE, PERL_LBRACE, false),
//			new BracePair(PERL_LANGLE, PERL_RANGLE, false),
	};

	@Override
	public BracePair[] getPairs()
	{
		return PAIRS;
	}

	@Override
	public boolean isPairedBracesAllowedBeforeType(@NotNull IElementType lbraceType, @Nullable IElementType contextType)
	{
		return true;
	}

	@Override
	public int getCodeConstructStart(PsiFile file, int openingBraceOffset)
	{
		return openingBraceOffset;
	}
}
