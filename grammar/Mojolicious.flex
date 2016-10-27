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
WHITE_SPACES_WITH_NEW_LINE={WHITE_SPACES}?\R{ANY_SPACES}


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
%state CHECK_SPACE_CLEAR_LINE, CHECK_SPACE
%state AFTER_PERL_BLOCK, AFTER_PERL_LINE

%%

<AFTER_PERL_BLOCK>{
	{WHITE_SPACES}		{yybegin(NON_CLEAR_LINE);return TokenType.WHITE_SPACE;}
	[^]					{yypushback(1);yybegin(NON_CLEAR_LINE);}
}
<AFTER_PERL_LINE>{
	{WHITE_SPACES}		{yybegin(YYINITIAL);return TokenType.WHITE_SPACE;}
	[^]					{yypushback(1);yybegin(YYINITIAL);}
}
<AFTER_PERL_BLOCK,AFTER_PERL_LINE>{
	{WHITE_SPACES_WITH_NEW_LINE}	{yybegin(YYINITIAL);return TokenType.WHITE_SPACE;}
}


<BLOCK_COMMENT>{
	{PERL_BLOCK_CLOSER}	{yybegin(AFTER_PERL_BLOCK);return COMMENT_BLOCK;}
	[^%=]+				{}
	[^]					{}
	<<EOF>>				{yybegin(YYINITIAL);return COMMENT_BLOCK;}
}

<PERL_BLOCK>{
	{PERL_BLOCK_CLOSER}							{yybegin(AFTER_PERL_BLOCK);return MOJO_BLOCK_CLOSER;}
	"begin" / {ANY_SPACES}? {PERL_BLOCK_CLOSER}	{return MOJO_BEGIN;}
	"end" / {ANY_SPACES}? {PERL_BLOCK_CLOSER}	{return MOJO_END;}
	[^]											{return delegateLexing();}
}

<PERL_EXPR_BLOCK>{
	{PERL_BLOCK_CLOSER}			{yybegin(AFTER_PERL_BLOCK);endPerlExpression();return MOJO_BLOCK_EXPR_CLOSER;}
	[^]							{return delegateLexing();}
}

<PERL_LINE>{
	\R								{yybegin(AFTER_PERL_LINE);return TokenType.WHITE_SPACE;}
	"begin" / {WHITE_SPACES}? \R	{return MOJO_BEGIN;}
	"end" / {WHITE_SPACES}? \R		{return MOJO_END;}
	[^]								{return delegateLexing();}
}

<PERL_EXPR_LINE>{
	\R							{yybegin(AFTER_PERL_LINE);endPerlExpression();return SEMICOLON;}
	[^]							{return delegateLexing();}
}

// checking what is behind the spaces
<CHECK_SPACE_CLEAR_LINE>{
	{PERL_EXPR_LINE_OPENER}		{pullback(0);yybegin(YYINITIAL);return TokenType.WHITE_SPACE;}
	{PERL_LINE_OPENER}			{pullback(0);yybegin(YYINITIAL);return TokenType.WHITE_SPACE;}
	{LINE_COMMENT}				{pullback(0);yybegin(YYINITIAL);return TokenType.WHITE_SPACE;}
	<CHECK_SPACE>{
		{BLOCK_COMMENT_START}		{pullback(0);yybegin(YYINITIAL);return TokenType.WHITE_SPACE;}
		{PERL_BLOCK_OPENER}			{pullback(0);yybegin(YYINITIAL);return TokenType.WHITE_SPACE;}
		{PERL_EXPR_BLOCK_OPENER}	{pullback(0);yybegin(YYINITIAL);return TokenType.WHITE_SPACE;}
		[^]							{yypushback(1);yybegin(NON_CLEAR_LINE);return MOJO_TEMPLATE_BLOCK_HTML;}
		<<EOF>>						{yybegin(YYINITIAL);return MOJO_TEMPLATE_BLOCK_HTML;}
	}
}

// initial means we are in HTML block in the beginning of the line
<YYINITIAL>{
	{PERL_EXPR_LINE_OPENER}	{startPerlExpression();yybegin(PERL_EXPR_LINE);return MOJO_LINE_OPENER;}
	{PERL_LINE_OPENER}		{yybegin(PERL_LINE);return MOJO_LINE_OPENER;}
	{LINE_COMMENT}			{yybegin(NON_CLEAR_LINE);return COMMENT_LINE;}
	{ANY_SPACES}			{yybegin(CHECK_SPACE_CLEAR_LINE);}

	// we are on non-clear line
	<NON_CLEAR_LINE>{
		{BLOCK_COMMENT_START}			{yybegin(BLOCK_COMMENT);}
		{PERL_LINE_OPENER_TAG}			{yybegin(NON_CLEAR_LINE);return MOJO_LINE_OPENER_TAG;}
		{PERL_BLOCK_OPENER_TAG}			{yybegin(NON_CLEAR_LINE);return MOJO_BLOCK_OPENER_TAG;}
		{WHITE_SPACES}					{yybegin(CHECK_SPACE);}
		{WHITE_SPACES_WITH_NEW_LINE}	{yybegin(CHECK_SPACE_CLEAR_LINE);}
		{PERL_BLOCK_OPENER}				{yybegin(PERL_BLOCK);return MOJO_BLOCK_OPENER;}
		{PERL_EXPR_BLOCK_OPENER}		{startPerlExpression();yybegin(PERL_EXPR_BLOCK);return MOJO_BLOCK_EXPR_OPENER;}
		\R+								{yybegin(YYINITIAL);return MOJO_TEMPLATE_BLOCK_HTML;}
		[^ \t\f\n\r<%]+					{yybegin(NON_CLEAR_LINE);return MOJO_TEMPLATE_BLOCK_HTML;}
		[<%]							{yybegin(NON_CLEAR_LINE);return MOJO_TEMPLATE_BLOCK_HTML;}
	}
}
