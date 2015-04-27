package com.perl5.lang.perl.lexer;

import com.intellij.lexer.LexerPosition;

/**
 * Created by hurricup on 27.04.2015.
 * This is modified clone of LexerPositionImpl
 */
class AdaptiveLexerPositionImpl implements LexerPosition
{
	private final int myOffset;
	private final int myState;

	public AdaptiveLexerPositionImpl(final int offset, final int state) {
		myOffset = offset;
		myState = state;
	}

	@Override
	public int getOffset() {
		return myOffset;
	}

	@Override
	public int getState() {
		return myState;
	}
}
