package com.perl5.lang.mojolicious.lexer;

import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.mojolicious.lexer.MojoliciousBaseLexer;

%%

%class MojoliciousLexer
%extends MojoliciousBaseLexer
//%abstract
%unicode
%public


%function perlAdvance
%type IElementType

WHITE_SPACE = [ \t\f]
WHITE_SPACES = {WHITE_SPACE}+
ANY_SPACE = {WHITE_SPACE}|\R
ANY_SPACES = {ANY_SPACE}+

PERL_LINE_OPENER = "%"
PERL_EXPR_LINE_OPENER = "%=" "="?

PERL_BLOCK_OPENER = "<%"
PERL_EXPR_BLOCK_OPENER = "<%=" "="?
PERL_BLOCK_CLOSER = "="?"%>"

PERL_LINE_OPENER_TAG = "%%"
PERL_BLOCK_OPENER_TAG = "<%%"

LINE_COMMENT = "%#" .*
BLOCK_COMMENT_START = "<%#"

%state PERL_BLOCK
%state PERL_EXPR_BLOCK
%state PERL_LINE
%state PERL_EXPR_LINE
%state NON_CLEAR_LINE
%xstate BLOCK_COMMENT

%%

<BLOCK_COMMENT>{
	{PERL_BLOCK_CLOSER}	{yybegin(NON_CLEAR_LINE);return COMMENT_BLOCK;}
	[^%=]+				{}
	[^]					{}
	<<EOF>>				{yybegin(YYINITIAL);return COMMENT_BLOCK;}
}

<PERL_BLOCK>{
	{PERL_BLOCK_CLOSER}			{yybegin(NON_CLEAR_LINE);return MOJO_BLOCK_CLOSER;}
	[^]							{return delegateLexing();}
}

<PERL_EXPR_BLOCK>{
	{PERL_BLOCK_CLOSER}			{yybegin(NON_CLEAR_LINE);endPerlExpression();return MOJO_BLOCK_EXPR_CLOSER;}
	[^]							{return delegateLexing();}
}

<PERL_LINE>{
	\R							{yybegin(YYINITIAL);return TokenType.WHITE_SPACE;}
	[^]							{return delegateLexing();}
}

<PERL_EXPR_LINE>{
	\R							{yybegin(YYINITIAL);endPerlExpression();return SEMICOLON;}
	[^]							{return delegateLexing();}
}

// initial means we are in HTML block in the beginning of the line
<YYINITIAL>{
	{PERL_EXPR_LINE_OPENER}	{startPerlExpression();yybegin(PERL_EXPR_LINE);return MOJO_LINE_OPENER;}
	{PERL_LINE_OPENER}		{yybegin(PERL_LINE);return MOJO_LINE_OPENER;}
	{LINE_COMMENT}			{yybegin(NON_CLEAR_LINE);return COMMENT_LINE;}
	{ANY_SPACES}			{return TokenType.WHITE_SPACE;}

	// we are on non-clear line
	<NON_CLEAR_LINE>{
		{BLOCK_COMMENT_START}		{yybegin(BLOCK_COMMENT);}
		{PERL_LINE_OPENER_TAG}		{yybegin(NON_CLEAR_LINE);return MOJO_LINE_OPENER_TAG;}
		{PERL_BLOCK_OPENER_TAG}		{yybegin(NON_CLEAR_LINE);return MOJO_BLOCK_OPENER_TAG;}
		{WHITE_SPACES}				{return TokenType.WHITE_SPACE;}
		{PERL_BLOCK_OPENER}			{yybegin(PERL_BLOCK);return MOJO_BLOCK_OPENER;}
		{PERL_EXPR_BLOCK_OPENER}	{startPerlExpression();yybegin(PERL_EXPR_BLOCK);return MOJO_BLOCK_EXPR_OPENER;}
		\R+							{yybegin(YYINITIAL);return TokenType.WHITE_SPACE;}
		[^] 						{yybegin(NON_CLEAR_LINE);return MOJO_TEMPLATE_BLOCK_HTML;}
	}
}
