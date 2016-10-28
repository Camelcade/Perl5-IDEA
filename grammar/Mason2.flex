package com.perl5.lang.mason2.lexer;

import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.lexer.PerlProtoLexer;
import com.perl5.lang.mason2.elementType.Mason2ElementTypes;

%%

%class Mason2LexerGenerated
%extends PerlProtoLexer
%implements Mason2ElementTypes
//%abstract
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
WHITE_SPACES_WITH_NEW_LINE={WHITE_SPACES}?\R{ANY_SPACES}

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
CLOSE_TAG_START = "</%"

DOC_CLOSE_TAG = {CLOSE_TAG_START}{KEYWORD_DOC}">"
FLAGS_CLOSE_TAG	= {CLOSE_TAG_START}{KEYWORD_FLAGS}">"

EXP_BLOCK_FILTER_SUFFIX = {ANY_SPACE}*{IDENTIFIER}(","{ANY_SPACE}*{IDENTIFIER})*{ANY_SPACE}*{EXPR_BLOCK_CLOSER}

SELF_POINTER = "$."

%state NON_CLEAR_LINE
%state PERL_LINE,PERL_BLOCK
%state PERL_EXPR_BLOCK, PERL_EXPR_BLOCK_FILTER
%state CALL_EXPRESSION
%state AFTER_PERL_LINE,AFTER_PERL_BLOCK
%state SELECT_OPEN_TAG,SELECT_CLOSE_TAG
%state OPENING_TAG
%state DOC,NON_EMPTY_DOC
%state FLAGS

%%

<FLAGS>{
	\R					{setPerlToInitial();return TokenType.WHITE_SPACE;}
	{FLAGS_CLOSE_TAG}	{yybegin(AFTER_PERL_BLOCK);return MASON_FLAGS_CLOSER;}
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
	{IDENTIFIER}			{return IDENTIFIER;}
}

<PERL_EXPR_BLOCK>{
	"|"	{yybegin(PERL_EXPR_BLOCK_FILTER);return MASON_EXPR_FILTER_PIPE;}
	<PERL_EXPR_BLOCK_FILTER>{
		{EXPR_BLOCK_CLOSER}		{yybegin(AFTER_PERL_BLOCK);return MASON_BLOCK_CLOSER;}
	}
	[^]	{return delegateLexing();}
}


<SELECT_OPEN_TAG>{
	{KEYWORD_METHOD}	{yybegin(OPENING_TAG);return MASON_METHOD_OPENER;}
	{KEYWORD_FILTER}	{yybegin(OPENING_TAG);return MASON_FILTER_OPENER;}
	{KEYWORD_AFTER}		{yybegin(OPENING_TAG);return MASON_AFTER_OPENER;}
	{KEYWORD_AUGMENT}	{yybegin(OPENING_TAG);return MASON_AUGMENT_OPENER;}
	{KEYWORD_AROUND}	{yybegin(OPENING_TAG);return MASON_AROUND_OPENER;}
	{KEYWORD_BEFORE}	{yybegin(OPENING_TAG);return MASON_BEFORE_OPENER;}
	{KEYWORD_OVERRIDE}	{yybegin(OPENING_TAG);return MASON_OVERRIDE_OPENER;}

	{KEYWORD_CLASS}">"		{yybegin(AFTER_PERL_BLOCK);return MASON_CLASS_OPENER;}
	{KEYWORD_DOC}">"		{yybegin(DOC);return MASON_DOC_OPENER;}
	{KEYWORD_FLAGS}">"		{yybegin(FLAGS);startPerlExpression();return MASON_FLAGS_OPENER;}
	{KEYWORD_INIT}">"		{yybegin(AFTER_PERL_BLOCK);return MASON_INIT_OPENER;}
	{KEYWORD_PERL}">"		{yybegin(AFTER_PERL_BLOCK);return MASON_PERL_OPENER;}
	{KEYWORD_TEXT}">"		{yybegin(AFTER_PERL_BLOCK);return MASON_TEXT_OPENER;}
	" "						{yybegin(PERL_EXPR_BLOCK); return MASON_BLOCK_OPENER;}
	[^]						{yypushback(1);yybegin(NON_CLEAR_LINE);return MASON_TEMPLATE_BLOCK_HTML;}
	<<EOF>>					{yybegin(YYINITIAL);return MASON_TEMPLATE_BLOCK_HTML;}
}

<SELECT_CLOSE_TAG>{
	{KEYWORD_METHOD}
	{KEYWORD_FILTER}
	{KEYWORD_AFTER}
	{KEYWORD_AUGMENT}
	{KEYWORD_AROUND}
	{KEYWORD_BEFORE}
	{KEYWORD_OVERRIDE}

	{KEYWORD_CLASS}
	{KEYWORD_DOC}
	{KEYWORD_FLAGS}
	{KEYWORD_INIT}
	{KEYWORD_PERL}
	{KEYWORD_TEXT}
}

<CALL_EXPRESSION>{
	" &>"	{yybegin(AFTER_PERL_BLOCK;);return MASON_CALL_CLOSER;}
	[^]		{return delegateLexing();}
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
	"{{" / {WHITE_SPACES}?{LINE_COMMENT}?\R?	{return MASON_FILTERED_BLOCK_OPENER;}
	"}}" / {WHITE_SPACES}?{LINE_COMMENT}?\R?	{return MASON_FILTERED_BLOCK_CLOSER;}
	[^]											{return delegateLexing();}
}


// html part, clear line
<YYINITIAL>{
	"% "	{yybegin(PERL_LINE);return MASON_LINE_OPENER;}

	// html part, non-clear line
	<NON_CLEAR_LINE>{
		"<& "	{yybegin(CALL_EXPRESSION);return MASON_CALL_OPENER;}
		"<%"	{yybegin(SELECT_OPEN_TAG);}
		{CLOSE_TAG_START}	{yybegin(SELECT_CLOSE_TAG);}
	}
	[^]	{retury TokenType.BAD_CHARACTER;}
}

