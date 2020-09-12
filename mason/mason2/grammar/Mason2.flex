package com.perl5.lang.mason2.lexer;

import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.mason2.lexer.Mason2TemplatingLexerBase;
%%

%class Mason2TemplatingLexer
%extends Mason2TemplatingLexerBase
%unicode
%public


%function perlAdvance
%type IElementType

// http://perldoc.perl.org/perldata.html#Identifier-parsing
PERL_XIDS = [\w && \p{XID_Start}_]
PERL_XIDC = [\w && \p{XID_Continue}]

IDENTIFIER = {PERL_XIDS} {PERL_XIDC}*


WHITE_SPACE = [ \t\f]
WHITE_SPACES = {WHITE_SPACE}+
ANY_SPACE = {WHITE_SPACE}|\R
ANY_SPACES = {ANY_SPACE}+
WHITE_SPACES_WITH_NEW_LINE={WHITE_SPACES}?\R{ANY_SPACES}?

LINE_COMMENT = "#".*

KEYWORD_METHOD="method"
KEYWORD_FILTER="filter"
KEYWORD_AFTER="after"
KEYWORD_AUGMENT="augment"
KEYWORD_AROUND="around"
KEYWORD_BEFORE="before"
KEYWORD_OVERRIDE="override"

KEYWORD_CLASS="class"
KEYWORD_DOC="doc"
KEYWORD_FLAGS="flags"
KEYWORD_INIT="init"
KEYWORD_PERL="perl"
KEYWORD_TEXT="text"

EXPR_BLOCK_CLOSER = " %>"
OPEN_TAG_START = "<%"
CLOSE_TAG_START = "</%"
PERL_LINE_OPENER = "% "
CALL_OPENER = "<& "
CALL_CLOSER = " &>"
CALL_BAREWORD_PATH_START = [\w\/\.]

DOC_CLOSE_TAG = {CLOSE_TAG_START}{KEYWORD_DOC}">"
FLAGS_CLOSE_TAG	= {CLOSE_TAG_START}{KEYWORD_FLAGS}">"
CLASS_CLOSE_TAG	= {CLOSE_TAG_START}{KEYWORD_CLASS}">"
INIT_CLOSE_TAG	= {CLOSE_TAG_START}{KEYWORD_INIT}">"
TEXT_CLOSE_TAG	= {CLOSE_TAG_START}{KEYWORD_TEXT}">"
PERL_CLOSE_TAG	= {CLOSE_TAG_START}{KEYWORD_PERL}">"

EXP_BLOCK_FILTER_SUFFIX = {ANY_SPACE}*{IDENTIFIER}(","{ANY_SPACE}*{IDENTIFIER})*{ANY_SPACE}*{EXPR_BLOCK_CLOSER}

SELF_POINTER = "$."

%state NON_CLEAR_LINE
%state PERL_LINE,PERL_BLOCK
%state PERL_EXPR_BLOCK, PERL_EXPR_BLOCK_FILTER
%state CALL_EXPRESSION, CALL_EXPRESSION_DELEGATED, CALL_PATH
%state AFTER_PERL_LINE,AFTER_PERL_BLOCK
%state SELECT_OPEN_TAG,SELECT_CLOSE_TAG
%state OPENING_TAG
%state DOC,NON_EMPTY_DOC
%state FLAGS
%state CLASS
%state INIT
%state TEXT, NON_EMPTY_TEXT
%state PERL
%state PARAMETRIZED_TAG
%state CHECK_SPACE_CLEAR_LINE, CHECK_SPACE

%%

<PARAMETRIZED_TAG>{
	">"	{yybegin(AFTER_PERL_BLOCK);endPerlExpression();return MASON_TAG_CLOSER;}
	[^]	{return delegateLexing();}
}

<PERL>{
	{PERL_CLOSE_TAG}	{yybegin(AFTER_PERL_BLOCK);return MASON_PERL_CLOSER;}
	[^]					{return delegateLexing();}
}

<NON_EMPTY_TEXT>{
	{TEXT_CLOSE_TAG}	{pushback();yybegin(TEXT);return STRING_CONTENT;}
	[^<]+				{}
	"<"					{}
	<<EOF>>				{yybegin(YYINITIAL);return STRING_CONTENT;}
}

<TEXT>{
	{TEXT_CLOSE_TAG}	{yybegin(AFTER_PERL_BLOCK);return MASON_TEXT_CLOSER;}
	[^]					{yybegin(NON_EMPTY_TEXT);}
}

<CLASS>{
	{CLASS_CLOSE_TAG}	{endPerlExpression();yybegin(AFTER_PERL_BLOCK);return MASON_CLASS_CLOSER;}
	[^]					{return delegateLexing();}
}

<INIT>{
	{INIT_CLOSE_TAG}	{endPerlExpression();yybegin(AFTER_PERL_BLOCK);return MASON_INIT_CLOSER;}
	[^]					{return delegateLexing();}
}

<FLAGS>{
	\R					{setPerlToInitial();return TokenType.WHITE_SPACE;}
	{FLAGS_CLOSE_TAG}	{endPerlExpression();yybegin(AFTER_PERL_BLOCK);return MASON_FLAGS_CLOSER;}
	[^]					{return delegateLexing();}
}

<NON_EMPTY_DOC>{
	{DOC_CLOSE_TAG}	{pushback();yybegin(DOC);return COMMENT_BLOCK;}
	[^<]+			{}
	"<"				{}
	<<EOF>>			{yybegin(YYINITIAL);return COMMENT_BLOCK;}
}

<DOC>{
	{DOC_CLOSE_TAG}	{yybegin(AFTER_PERL_BLOCK);return MASON_DOC_CLOSER;}
	[^]				{yybegin(NON_EMPTY_DOC);}
}

<OPENING_TAG>{
	">"		{yybegin(AFTER_PERL_BLOCK);return MASON_TAG_CLOSER;}
	[^]		{return delegateLexing();}
}

<PERL_EXPR_BLOCK_FILTER>{
	{ANY_SPACES}			{return TokenType.WHITE_SPACE;}
	","						{return COMMA;}
	{IDENTIFIER}			{return IDENTIFIER;}
}

<PERL_EXPR_BLOCK>{
	<PERL_EXPR_BLOCK_FILTER>{
		{EXPR_BLOCK_CLOSER}					{yybegin(AFTER_PERL_BLOCK);endPerlExpression();return MASON_BLOCK_CLOSER;}
		{ANY_SPACES} / {EXPR_BLOCK_CLOSER}	{return TokenType.WHITE_SPACE;}
	}
	"|" / {EXP_BLOCK_FILTER_SUFFIX}	{yybegin(PERL_EXPR_BLOCK_FILTER);return MASON_EXPR_FILTER_PIPE;}
	[^]								{return delegateLexing();}
}


<SELECT_OPEN_TAG>{
	{KEYWORD_METHOD}	{yybegin(PARAMETRIZED_TAG);startPerlExpression();return MASON_METHOD_OPENER;}
	{KEYWORD_FILTER}	{yybegin(PARAMETRIZED_TAG);startPerlExpression();return MASON_FILTER_OPENER;}
	{KEYWORD_AFTER}		{yybegin(PARAMETRIZED_TAG);startPerlExpression();return MASON_AFTER_OPENER;}
	{KEYWORD_AUGMENT}	{yybegin(PARAMETRIZED_TAG);startPerlExpression();return MASON_AUGMENT_OPENER;}
	{KEYWORD_AROUND}	{yybegin(PARAMETRIZED_TAG);startPerlExpression();return MASON_AROUND_OPENER;}
	{KEYWORD_BEFORE}	{yybegin(PARAMETRIZED_TAG);startPerlExpression();return MASON_BEFORE_OPENER;}
	{KEYWORD_OVERRIDE}	{yybegin(PARAMETRIZED_TAG);startPerlExpression();return MASON_OVERRIDE_OPENER;}

	{KEYWORD_CLASS}">"		{yybegin(CLASS);startPerlExpression();return MASON_CLASS_OPENER;}
	{KEYWORD_DOC}">"		{yybegin(DOC);return MASON_DOC_OPENER;}
	{KEYWORD_FLAGS}">"		{yybegin(FLAGS);startPerlExpression();return MASON_FLAGS_OPENER;}
	{KEYWORD_INIT}">"		{yybegin(INIT);startPerlExpression();return MASON_INIT_OPENER;}
	{KEYWORD_PERL}">"		{yybegin(PERL);return MASON_PERL_OPENER;}
	{KEYWORD_TEXT}">"		{yybegin(TEXT);return MASON_TEXT_OPENER;}
	" "						{yybegin(PERL_EXPR_BLOCK); startPerlExpression(); return MASON_BLOCK_OPENER;}
	[^]						{yypushback(1);yybegin(NON_CLEAR_LINE);return MASON_TEMPLATE_BLOCK_HTML;}
	<<EOF>>					{yybegin(YYINITIAL);return MASON_TEMPLATE_BLOCK_HTML;}
}

<SELECT_CLOSE_TAG>{
	{KEYWORD_METHOD}">"		{yybegin(AFTER_PERL_BLOCK);return MASON_METHOD_CLOSER;}
	{KEYWORD_FILTER}">"		{yybegin(AFTER_PERL_BLOCK);return MASON_FILTER_CLOSER;}
	{KEYWORD_AFTER}">"		{yybegin(AFTER_PERL_BLOCK);return MASON_AFTER_CLOSER;}
	{KEYWORD_AUGMENT}">"	{yybegin(AFTER_PERL_BLOCK);return MASON_AUGMENT_CLOSER;}
	{KEYWORD_AROUND}">"		{yybegin(AFTER_PERL_BLOCK);return MASON_AROUND_CLOSER;}
	{KEYWORD_BEFORE}">"		{yybegin(AFTER_PERL_BLOCK);return MASON_BEFORE_CLOSER;}
	{KEYWORD_OVERRIDE}">"	{yybegin(AFTER_PERL_BLOCK);return MASON_OVERRIDE_CLOSER;}
	[^]						{yypushback(1);yybegin(NON_CLEAR_LINE);return MASON_TEMPLATE_BLOCK_HTML;}
	<<EOF>>					{yybegin(YYINITIAL);return MASON_TEMPLATE_BLOCK_HTML;}
}

<CALL_EXPRESSION_DELEGATED>{
	[^]		{return delegateLexing();}
}

<CALL_PATH>{
	{ANY_SPACES}?{CALL_CLOSER}		{pushback();yybegin(CALL_EXPRESSION);return STRING_CONTENT;}
	{ANY_SPACES}?","				{pushback();yybegin(CALL_EXPRESSION);return STRING_CONTENT;}
	[^]								{}
	<<EOF>>							{yybegin(YYINITIAL);return STRING_CONTENT;}
}

<CALL_EXPRESSION>{
	<CALL_EXPRESSION_DELEGATED>{
		{ANY_SPACES} / {CALL_CLOSER}	{return TokenType.WHITE_SPACE;}
		{CALL_CLOSER}					{yybegin(AFTER_PERL_BLOCK);endPerlExpression();return MASON_CALL_CLOSER;}
	}
	{ANY_SPACES} 					{return TokenType.WHITE_SPACE;}
	{CALL_BAREWORD_PATH_START}		{yybegin(CALL_PATH);}
	[^]								{pushback();yybegin(CALL_EXPRESSION_DELEGATED);}
}

<AFTER_PERL_BLOCK>{
	{WHITE_SPACES_WITH_NEW_LINE}	{yybegin(YYINITIAL);return TokenType.WHITE_SPACE;}
	{WHITE_SPACES}					{yybegin(NON_CLEAR_LINE);return TokenType.WHITE_SPACE;}
	[^]								{yypushback(1);yybegin(NON_CLEAR_LINE);}
}

<AFTER_PERL_LINE>{
	{ANY_SPACES}	{yybegin(YYINITIAL);return TokenType.WHITE_SPACE;}
	[^]				{yypushback(1);yybegin(YYINITIAL);}
}

<PERL_LINE,PERL_BLOCK>{
	{SELF_POINTER} / {IDENTIFIER} {return MASON_SELF_POINTER;}
}

<PERL_LINE>{
	\R											{yybegin(AFTER_PERL_LINE);return SEMICOLON;}
	// we should probably use start/end perl expression instead just resetting to initial
	"{{" / {WHITE_SPACES}?{LINE_COMMENT}?\R?	{setPerlToInitial();return MASON_FILTERED_BLOCK_OPENER;}
	"}}" / {WHITE_SPACES}?{LINE_COMMENT}?\R?	{setPerlToInitial();return MASON_FILTERED_BLOCK_CLOSER;}
	[^]											{return delegateLexing();}
}

// checking what is behind the spaces
<CHECK_SPACE_CLEAR_LINE>{
	{PERL_LINE_OPENER}			{pushback();yybegin(YYINITIAL);return TokenType.WHITE_SPACE;}
	<CHECK_SPACE>{
		// unsure if this right, we are missing incorrect <% and <&
		{CALL_OPENER}				{pushback();yybegin(YYINITIAL);return TokenType.WHITE_SPACE;}
		{OPEN_TAG_START}			{pushback();yybegin(YYINITIAL);return TokenType.WHITE_SPACE;}
		{CLOSE_TAG_START}			{pushback();yybegin(YYINITIAL);return TokenType.WHITE_SPACE;}
		[^]							{yypushback(1);yybegin(NON_CLEAR_LINE);return MASON_TEMPLATE_BLOCK_HTML;}
		<<EOF>>						{yybegin(YYINITIAL);return MASON_TEMPLATE_BLOCK_HTML;}
	}
}

// html part, clear line
<YYINITIAL>{
	{PERL_LINE_OPENER}		{yybegin(PERL_LINE);return MASON_LINE_OPENER;}
	{ANY_SPACES}			{yybegin(CHECK_SPACE_CLEAR_LINE);}

	// html part, non-clear line
	<NON_CLEAR_LINE>{
		{CALL_OPENER}			{yybegin(CALL_EXPRESSION);startPerlExpression();return MASON_CALL_OPENER;}
		{OPEN_TAG_START}		{yybegin(SELECT_OPEN_TAG);}
		{CLOSE_TAG_START}		{yybegin(SELECT_CLOSE_TAG);}
		{WHITE_SPACES}					{yybegin(CHECK_SPACE);}
		{WHITE_SPACES_WITH_NEW_LINE}	{yybegin(CHECK_SPACE_CLEAR_LINE);}
		[^ \t\f\n\r<%]+					{yybegin(NON_CLEAR_LINE);return MASON_TEMPLATE_BLOCK_HTML;}
		[<%]							{yybegin(NON_CLEAR_LINE);return MASON_TEMPLATE_BLOCK_HTML;}
	}
}

