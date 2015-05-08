package com.perl5.lang.perl.psi;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.lang.impl.PsiBuilderAdapter;
import com.intellij.lang.impl.PsiBuilderImpl;
import com.intellij.lang.parser.GeneratedParserUtilBase;
import com.intellij.lexer.Lexer;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.containers.Stack;
import com.perl5.lang.perl.PerlParserDefinition;
import com.perl5.lang.perl.PerlTokenType;
import com.perl5.lang.perl.parser.PerlCodeBlockState;
import com.perl5.lang.perl.parser.PerlTokenData;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by hurricup on 04.05.2015.
 * This wrapper created to be able to store per-parsing data like pragmas, warnings and variables ?
 */
public class PerlBuilder extends GeneratedParserUtilBase.Builder
{
	protected final Stack<PerlCodeBlockState> blockState = new Stack<PerlCodeBlockState>();

	public PerlBuilder(PsiBuilder builder, GeneratedParserUtilBase.ErrorState state, PsiParser parser) {
		super(builder, state, parser);
//		System.out.println("Created builder object");
	}

	public void initCodeBlockStateStack()
	{
		assert blockState.size() == 0;
		blockState.clear();
		blockState.push(new PerlCodeBlockState());
//		System.out.println("Inited states stack");
	}

	public void pushCodeBlockState(String debugText)
	{
		blockState.push(new PerlCodeBlockState(getCurrentBlockState()));
//		System.out.println("Pushed codeblock state on: " + debugText);
	}

	public void popCodeBlockState(String debugText)
	{
		assert blockState.size() > 0;
		blockState.pop();
//		System.out.println("Popped codeblock state" + debugText);
	}

	public PerlCodeBlockState getCurrentBlockState()
	{
		int stackSize = blockState.size();
		assert stackSize > 0;
		return blockState.get(stackSize - 1);
	}

	/**
	 * Return token ahead of current, skips spaces and comments (@todo same as look behind, need to merge)
	 * @param steps
	 * @return
	 */
	public PerlTokenData getAheadToken(int steps)
	{
		assert steps != 0;
		int rawStep = 0;
		int step = steps / Math.abs(steps);

		IElementType rawTokenType = null;

		while( steps != 0 )
		{
			rawStep += step;
			rawTokenType = rawLookup(rawStep);

			// reached end
			if( rawTokenType == null )
				return null;

			if( !PerlParserDefinition.WHITE_SPACE_AND_COMMENTS.contains(rawTokenType))
			{
				steps-=step;
			}
		}

		return new PerlTokenData(rawTokenType, getOriginalText().subSequence(rawTokenTypeStart(rawStep), rawTokenTypeStart(rawStep+1)).toString());
	}



}
