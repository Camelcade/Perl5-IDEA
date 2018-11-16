package com.perl5.lang.htmlmason.lexer;

import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.htmlmason.lexer.HTMLMasonBaseLexer;

%%

%class HTMLMasonLexer
%extends HTMLMasonBaseLexer
%unicode
%public


%function perlAdvance
%type IElementType

// http://perldoc.perl.org/perldata.html#Identifier-parsing
PERL_XIDS = [\w && \p{XID_Start}_]
PERL_XIDC = [\w && \p{XID_Continue}]

IDENTIFIER = {PERL_XIDS} {PERL_XIDC}*


WHITE_SPACE = [ \t\f]
ANY_SPACE = {WHITE_SPACE}|\R
ANY_SPACES = {ANY_SPACE}+
WHITE_SPACES_ENDING_NEW_LINE={ANY_SPACES}?\R

EXPR_BLOCK_CLOSER = "%>"
OPEN_TAG_START = "<%"
CLOSE_TAG_START = "</%"
PERL_LINE_OPENER = "%"
CALL_OPEN_TAG_START = "<&"
CALL_OPEN_TAG_START_FILTERING = "<&|"
CALL_OPEN_TAG_END = "&>"
CALL_CLOSE_TAG_START = "</&"
CALL_BAREWORD_PATH_START = [\w\/\.]

SUBCOMPONENT_NAME = [\w\.\_\-]+

EXP_BLOCK_FILTER_SUFFIX = {ANY_SPACE}*{IDENTIFIER}(","{ANY_SPACE}*{IDENTIFIER})*{ANY_SPACE}*{EXPR_BLOCK_CLOSER}

KEYWORD_DEF="def"
KEYWORD_METHOD="method"

CUSTOM_OPEN_TAG_CLOSED = {IDENTIFIER}">"
CUSTOM_CLOSE_TAG = {CLOSE_TAG_START}{CUSTOM_OPEN_TAG_CLOSED}

KEYWORD_INIT="init"
INIT_CLOSE_TAG = {CLOSE_TAG_START}{KEYWORD_INIT}">"

KEYWORD_CLEANUP="cleanup"
CLEANUP_CLOSE_TAG = {CLOSE_TAG_START}{KEYWORD_CLEANUP}">"

KEYWORD_ONCE="once"
ONCE_CLOSE_TAG = {CLOSE_TAG_START}{KEYWORD_ONCE}">"

KEYWORD_SHARED="shared"
SHARED_CLOSE_TAG = {CLOSE_TAG_START}{KEYWORD_SHARED}">"

KEYWORD_FLAGS="flags"
FLAGS_CLOSE_TAG = {CLOSE_TAG_START}{KEYWORD_FLAGS}">"

KEYWORD_ARGS="args"
ARGS_CLOSE_TAG = {CLOSE_TAG_START}{KEYWORD_ARGS}">"

KEYWORD_ATTR="attr"
ATTR_CLOSE_TAG = {CLOSE_TAG_START}{KEYWORD_ATTR}">"

KEYWORD_FILTER="filter"
FILTER_CLOSE_TAG = {CLOSE_TAG_START}{KEYWORD_FILTER}">"

KEYWORD_DOC="doc"
DOC_CLOSE_TAG = {CLOSE_TAG_START}{KEYWORD_DOC}">"

KEYWORD_TEXT="text"
TEXT_CLOSE_TAG = {CLOSE_TAG_START}{KEYWORD_TEXT}">"

KEYWORD_PERL="perl"
PERL_CLOSE_TAG = {CLOSE_TAG_START}{KEYWORD_PERL}">"

%state NON_CLEAR_LINE
%state CHECK_SPACE_CLEAR_LINE, CHECK_SPACE
%state PARAMETRIZED_TAG
%state PERL_LINE
%state PERL_EXPR, PERL_EXPR_FILTER
%state SELECT_OPEN_TAG,SELECT_CLOSE_TAG,SELECT_OPEN_TAG_WITH_CUSTOM_TAGS,SELECT_CLOSE_TAG_WITH_CUSTOM_TAGS
%state CALL_OPENER, CALL_PATH, CALL_OPENER_DELEGATED
%state CALL_OPENER_FILTERING, CALL_OPENER_COMMON
%state CALL_CLOSER, CALL_CLOSER_NAME
%state AFTER_PERL_LINE, AFTER_PERL_BLOCK

%state INIT,CLEANUP,ONCE,SHARED,PARAMETRIZED_OPENER,FLAGS,ATTR,FILTER,
%state PERL,PERL_WITH_CUSTOM_CLOSER
%state ARGS,ARGS_WITH_CUSTOM_CLOSER
%state DOC, NON_EMPTY_DOC
%state TEXT, NON_EMPTY_TEXT

%%

<CALL_PATH>{
	{ANY_SPACES}?","					{pushback();yybegin(CALL_OPENER_DELEGATED);return STRING_CONTENT;}
	{ANY_SPACES}?{CALL_OPEN_TAG_END}	{pushback();yybegin(CALL_OPENER_DELEGATED);return STRING_CONTENT;}
	[^ \t\f\n\r,&]+						{}
	{ANY_SPACES}						{}
	<<EOF>>								{popState();yybegin(YYINITIAL);return STRING_CONTENT;}
}

<CALL_OPENER_COMMON>{
	{CALL_BAREWORD_PATH_START}	{yybegin(CALL_PATH);}
	{ANY_SPACES}				{return TokenType.WHITE_SPACE;}
	[^]							{pushback();yybegin(CALL_OPENER_DELEGATED);}
	<CALL_OPENER_DELEGATED>{
		{CALL_OPEN_TAG_END}		{pushback();popState();}
		[^]						{return delegateLexing();}
	}
}

<CALL_OPENER>{
	{CALL_OPEN_TAG_END}			{endPerlExpression();yybegin(AFTER_PERL_BLOCK);return HTML_MASON_CALL_CLOSER;}
	[^]							{pushback();pushStateAndBegin(CALL_OPENER_COMMON);}
}

<CALL_OPENER_FILTERING>{
	{CALL_OPEN_TAG_END}			{endPerlExpression();yybegin(AFTER_PERL_BLOCK);return HTML_MASON_CALL_CLOSER_UNMATCHED;}
	[^]							{pushback();pushStateAndBegin(CALL_OPENER_COMMON);}
}

<CALL_CLOSER_NAME>{
	{ANY_SPACES}?">"	{pushback();yybegin(CALL_CLOSER);return STRING_CONTENT;}
	[^ \t\f\n\r\>]+		{}
	{ANY_SPACES}		{}
	<<EOF>>				{yybegin(YYINITIAL);return STRING_CONTENT;}
}

<CALL_CLOSER>{
	">"				{yybegin(AFTER_PERL_BLOCK);return HTML_MASON_TAG_CLOSER;}
	{ANY_SPACES}	{return TokenType.WHITE_SPACE;}
	[^]				{pushback();yybegin(CALL_CLOSER_NAME);}
}

<PARAMETRIZED_OPENER>{
	">"					{yybegin(AFTER_PERL_BLOCK);setPerlToInitial();return HTML_MASON_TAG_CLOSER;}
	{ANY_SPACES}		{return TokenType.WHITE_SPACE;}
	{SUBCOMPONENT_NAME}	{return IDENTIFIER;}
	[^]					{return TokenType.BAD_CHARACTER;}
}

<INIT>{
	{INIT_CLOSE_TAG}	{endPerlExpression();yybegin(AFTER_PERL_BLOCK);return HTML_MASON_INIT_CLOSER;}
	[^]					{return delegateLexing();}
}
<CLEANUP>{
	{CLEANUP_CLOSE_TAG}	{endPerlExpression();yybegin(AFTER_PERL_BLOCK);return HTML_MASON_CLEANUP_CLOSER;}
	[^]					{return delegateLexing();}
}
<ONCE>{
	{ONCE_CLOSE_TAG}	{endPerlExpression();yybegin(AFTER_PERL_BLOCK);return HTML_MASON_ONCE_CLOSER;}
	[^]					{return delegateLexing();}
}
<SHARED>{
	{SHARED_CLOSE_TAG}	{endPerlExpression();yybegin(AFTER_PERL_BLOCK);return HTML_MASON_SHARED_CLOSER;}
	[^]					{return delegateLexing();}
}
<FLAGS>{
	{FLAGS_CLOSE_TAG}	{endPerlExpression();yybegin(AFTER_PERL_BLOCK);return HTML_MASON_FLAGS_CLOSER;}
	\R					{setPerlToInitial();return TokenType.WHITE_SPACE;}
	[^]					{return delegateLexing();}
}

<ARGS> 						{ARGS_CLOSE_TAG}	{return processArgsCloser();}
<ARGS_WITH_CUSTOM_CLOSER>	{CUSTOM_CLOSE_TAG}	{return processCustomArgsCloser();}

<ARGS_WITH_CUSTOM_CLOSER,ARGS>{
	\R					{setPerlToInitial();return HTML_MASON_HARD_NEWLINE;}
	[^]					{return delegateLexing();}
}

<ATTR>{
	{ATTR_CLOSE_TAG}	{endPerlExpression();yybegin(AFTER_PERL_BLOCK);return HTML_MASON_ATTR_CLOSER;}
	\R					{setPerlToInitial();return HTML_MASON_HARD_NEWLINE;}
	[^]					{return delegateLexing();}
}
<FILTER>{
	{FILTER_CLOSE_TAG}	{endPerlExpression();yybegin(AFTER_PERL_BLOCK);return HTML_MASON_FILTER_CLOSER;}
	[^]					{return delegateLexing();}
}

<NON_EMPTY_DOC>{
	{DOC_CLOSE_TAG}		{yybegin(DOC);pushback();return COMMENT_BLOCK;}
	[^<]+				{}
	"<"					{}
	<<EOF>>				{yybegin(YYINITIAL);return COMMENT_BLOCK;}
}

<DOC>{
	{DOC_CLOSE_TAG}		{yybegin(AFTER_PERL_BLOCK);return HTML_MASON_DOC_CLOSER;}
	[^]					{yybegin(NON_EMPTY_DOC);}
}

<NON_EMPTY_TEXT>{
	{TEXT_CLOSE_TAG}	{yybegin(TEXT);pushback();return STRING_CONTENT;}
	[^<]+				{}
	"<"					{}
	<<EOF>>				{yybegin(YYINITIAL);return STRING_CONTENT;}
}

<TEXT>{
	{TEXT_CLOSE_TAG}	{yybegin(AFTER_PERL_BLOCK);return HTML_MASON_TEXT_CLOSER;}
	[^]					{yybegin(NON_EMPTY_TEXT);}
}

<PERL> 							{PERL_CLOSE_TAG}	{return processPerlCloser();}
<PERL_WITH_CUSTOM_CLOSER>		{CUSTOM_CLOSE_TAG}	{return processCustomPerlCloser();}
<PERL_WITH_CUSTOM_CLOSER,PERL> 	[^]					{return delegateLexing();}

<PERL_EXPR_FILTER>{
	{ANY_SPACES}			{return TokenType.WHITE_SPACE;}
	","						{return COMMA;}
	{IDENTIFIER}			{return HTML_MASON_ESCAPER_NAME;}
	{EXPR_BLOCK_CLOSER}		{yybegin(AFTER_PERL_BLOCK);endPerlExpression();return HTML_MASON_BLOCK_CLOSER;}
	[^]						{return TokenType.BAD_CHARACTER;}
}

<PERL_EXPR>{
	"|" / {EXP_BLOCK_FILTER_SUFFIX}	{yybegin(PERL_EXPR_FILTER);return HTML_MASON_EXPR_FILTER_PIPE;}
	{EXPR_BLOCK_CLOSER}				{yybegin(AFTER_PERL_BLOCK);endPerlExpression();return HTML_MASON_BLOCK_CLOSER;}
	[^]								{return delegateLexing();}
}

<AFTER_PERL_LINE,AFTER_PERL_BLOCK>{
	{WHITE_SPACES_ENDING_NEW_LINE}	{yybegin(YYINITIAL);return TokenType.WHITE_SPACE;}
	{ANY_SPACES}					{yybegin(NON_CLEAR_LINE);return TokenType.WHITE_SPACE;}
}

<AFTER_PERL_LINE>	[^]	{pushback();yybegin(YYINITIAL);}
<AFTER_PERL_BLOCK>	[^]	{pushback();yybegin(NON_CLEAR_LINE);}

<SELECT_OPEN_TAG_WITH_CUSTOM_TAGS>{
	<SELECT_OPEN_TAG>{
		{KEYWORD_METHOD}	{return processMethodOpenTag();}
		{KEYWORD_DEF}		{return processDefOpenTag();}

		{KEYWORD_INIT}">"		{yybegin(INIT);startPerlExpression();return HTML_MASON_INIT_OPENER;}
		{KEYWORD_CLEANUP}">"	{yybegin(CLEANUP);startPerlExpression();return HTML_MASON_CLEANUP_OPENER;}
		{KEYWORD_ONCE}">"		{yybegin(ONCE);startPerlExpression();return HTML_MASON_ONCE_OPENER;}
		{KEYWORD_SHARED}">"		{yybegin(SHARED);startPerlExpression();return HTML_MASON_SHARED_OPENER;}
		{KEYWORD_FLAGS}">"		{yybegin(FLAGS);startPerlExpression();return HTML_MASON_FLAGS_OPENER;}
		{KEYWORD_ATTR}">"		{yybegin(ATTR);startPerlExpression();return HTML_MASON_ATTR_OPENER;}
		{KEYWORD_ARGS}">"		{return processArgsOpenTag(ARGS);}
		{KEYWORD_FILTER}">"		{yybegin(FILTER);startPerlExpression();return HTML_MASON_FILTER_OPENER;}
		{KEYWORD_DOC}">"		{yybegin(DOC);return HTML_MASON_DOC_OPENER;}
		{KEYWORD_TEXT}">"		{yybegin(TEXT);return HTML_MASON_TEXT_OPENER;}

		{KEYWORD_PERL}">"		{return processPerlOpenTag(PERL);}
	}

	{IDENTIFIER}">"				{return processCustomSimpleOpenTag();}
	{IDENTIFIER}				{return processCustomComplexOpenTag();}

	<SELECT_OPEN_TAG>{
		[^]						{return processOpenTagFallback();}
		<<EOF>>					{yybegin(YYINITIAL);return HTML_MASON_BLOCK_OPENER;}
	}
}

<SELECT_CLOSE_TAG_WITH_CUSTOM_TAGS>{
	<SELECT_CLOSE_TAG>{
		{KEYWORD_DEF}">"	{return processDefCloseTag();}
		{KEYWORD_METHOD}">"	{return processMethodCloseTag();}
	}
	{IDENTIFIER}">"			{return processCustomCloseTag();}
	<SELECT_CLOSE_TAG>{
		[^]					{return processCloseTagFallback();}
		<<EOF>>				{return processCloseTagFallback();}
	}
}

<PERL_LINE>{
	\R	{yybegin(AFTER_PERL_LINE);return TokenType.WHITE_SPACE;}
	[^]	{return delegateLexing();}
}

<CHECK_SPACE_CLEAR_LINE>{
	{PERL_LINE_OPENER}				{pushback();yybegin(YYINITIAL);return TokenType.WHITE_SPACE;}
	<CHECK_SPACE>
	{
		{OPEN_TAG_START}				{pushback();yybegin(NON_CLEAR_LINE);return TokenType.WHITE_SPACE;}
		{CLOSE_TAG_START}				{pushback();yybegin(NON_CLEAR_LINE);return TokenType.WHITE_SPACE;}
		{CALL_OPEN_TAG_START}			{pushback();yybegin(NON_CLEAR_LINE);return TokenType.WHITE_SPACE;}
		{CALL_OPEN_TAG_START_FILTERING}	{pushback();yybegin(NON_CLEAR_LINE);return TokenType.WHITE_SPACE;}
		{CALL_CLOSE_TAG_START}			{pushback();yybegin(NON_CLEAR_LINE);return TokenType.WHITE_SPACE;}
		[^]								{pushback();yybegin(NON_CLEAR_LINE);return HTML_MASON_TEMPLATE_BLOCK_HTML;}
		<<EOF>>							{yybegin(YYINITIAL);return HTML_MASON_TEMPLATE_BLOCK_HTML;}
	}
}

<YYINITIAL>{
	{PERL_LINE_OPENER}	{yybegin(PERL_LINE);return HTML_MASON_LINE_OPENER;}
	<NON_CLEAR_LINE>{
		{OPEN_TAG_START}				{
			if( myCustomTagsMap == null ){
				yybegin(SELECT_OPEN_TAG);
			}
			else
			{
				yybegin(SELECT_OPEN_TAG_WITH_CUSTOM_TAGS);
			}
		}
		{CLOSE_TAG_START}				{
			if( myCustomTagsMap == null ){
				yybegin(SELECT_CLOSE_TAG);
			}
			else
			{
				yybegin(SELECT_CLOSE_TAG_WITH_CUSTOM_TAGS);
			}
		}
		{CALL_OPEN_TAG_START}			{yybegin(CALL_OPENER);startPerlExpression();return HTML_MASON_CALL_OPENER;}
		{CALL_OPEN_TAG_START_FILTERING}	{yybegin(CALL_OPENER_FILTERING);startPerlExpression();return HTML_MASON_CALL_FILTERING_OPENER;}
		{CALL_CLOSE_TAG_START}			{yybegin(CALL_CLOSER); return HTML_MASON_CALL_CLOSE_TAG_START;}
		{WHITE_SPACES_ENDING_NEW_LINE}	{yybegin(CHECK_SPACE_CLEAR_LINE);}
		{ANY_SPACES}					{yybegin(CHECK_SPACE);}
		[^ \t\f\n\r<%]+					{yybegin(NON_CLEAR_LINE);return HTML_MASON_TEMPLATE_BLOCK_HTML;}
		[<%]							{yybegin(NON_CLEAR_LINE);return HTML_MASON_TEMPLATE_BLOCK_HTML;}
	}
}
