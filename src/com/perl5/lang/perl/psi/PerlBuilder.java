package com.perl5.lang.perl.psi;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.lang.impl.PsiBuilderAdapter;
import com.intellij.lang.impl.PsiBuilderImpl;
import com.intellij.lang.parser.GeneratedParserUtilBase;
import com.intellij.lexer.Lexer;
import com.intellij.util.containers.Stack;
import com.perl5.lang.perl.parser.PerlCodeBlockState;

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

}
