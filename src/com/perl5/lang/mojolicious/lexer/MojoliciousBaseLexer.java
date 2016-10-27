package com.perl5.lang.mojolicious.lexer;

import com.intellij.openapi.util.text.StringUtil;
import com.perl5.lang.mojolicious.MojoliciousElementTypes;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.lexer.PerlTemplatingLexer;
import org.jetbrains.annotations.Nullable;

public abstract class MojoliciousBaseLexer extends PerlTemplatingLexer implements MojoliciousElementTypes, PerlElementTypes
{
	private static final CommentEndCalculator COMMENT_END_CALCULATOR = commentText -> StringUtil.indexOf(commentText, "%>");

	@Nullable
	@Override
	protected CommentEndCalculator getCommentEndCalculator()
	{
		return COMMENT_END_CALCULATOR;
	}

	protected void startPerlExpression()
	{
		myPerlLexer.pushStateAndBegin(0);
	}

	protected void endPerlExpression()
	{
		myPerlLexer.popState();
	}
}
