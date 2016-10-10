/*
    Copyright 2015 Alexandr Evstigneev

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/
package com.perl5.lang.perl.lexer;

import com.perl5.lang.perl.util.PerlSubUtil;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;


%%

%class PerlLexerGenerated
%extends PerlBaseLexer
%abstract
%unicode
%public


%function perlAdvance
%type IElementType


%{

    protected int trenarCounter = 0;

	public abstract IElementType guessDiv();
	public abstract IElementType getIdentifierToken();
	public abstract IElementType checkOperatorXSticked();
	public abstract IElementType parseVersion();
	public abstract IElementType parseNumber();
	public abstract IElementType parseOperatorDereference();
	public abstract IElementType parseHeredocOpener();
	public abstract IElementType parseHeredocOpenerBackref();
	public abstract IElementType guessLtNumeric();
%}


/*
// Char classes
*/
NEW_LINE = \r?\n
WHITE_SPACE     = [ \t\f]

// http://perldoc.perl.org/perldata.html#Identifier-parsing
PERL_XIDS = [\w && \p{XID_Start}\d_] // seems in java \d does not matches XID_Start
PERL_XIDC = [\w && \p{XID_Continue}]

IDENTIFIER = {PERL_XIDS} {PERL_XIDC}*

BAREWORD_MINUS = "-" * {IDENTIFIER}

QAULIFIED_IDENTIFIER = ("::"* "'" ?) ? {IDENTIFIER} (("::"+ "'" ? | "::"* "'" ) {IDENTIFIER} )*  "::" *
PACKAGE_SHORT = "::"+ "'" ?

//DQ_STRING = "\"" ([^\"]|"\\\\"|"\\\"" )* "\""	// for lpe
//SQ_STRING = "\'" ([^\']|"\\\\"|"\\\'" )* "\'"
//XQ_STRING = "\`" ([^\`]|"\\\\"|"\\\`" )* "\`"

PERL_VERSION_CHUNK = [0-9][0-9_]*
PERL_VERSION = "v"?{PERL_VERSION_CHUNK}("." {PERL_VERSION_CHUNK})*
// heading _ removed to avoid @_ parsing as sigil-number

// duplicated in Pod lexer
NUMBER_EXP = [eE][+-]?[0-9_]+
NUMBER_FLOAT = "." ([0-9][0-9_]*)?
NUMBER_INT_SIMPLE = [0-9]+
NUMBER_INT = [0-9][0-9_] *  {NUMBER_FLOAT}? {NUMBER_EXP}?
NUMBER_SMALL = {NUMBER_FLOAT}{NUMBER_EXP}?
NUMBER_HEX = "0"[xX][0-9a-fA-F_]+
NUMBER_BIN = "0"[bB][01_]+
NUMBER = {NUMBER_HEX} | {NUMBER_BIN}| {NUMBER_INT} | {NUMBER_SMALL}

SPECIAL_VARIABLE_NAME = [\"\'\[\]\`\\!\%&\(\)\+,-./\;<=>|~?:*\^@]
CAPPED_SINGLE_LETTER_VARIABLE_NAME = "^"[\]\[A-Z\^_?\\]
VARIABLE_NAME = {QAULIFIED_IDENTIFIER} | {CAPPED_SINGLE_LETTER_VARIABLE_NAME} | {SPECIAL_VARIABLE_NAME}

CAPPED_BRACED_VARIABLE = {CAPPED_SINGLE_LETTER_VARIABLE_NAME}[\w_]*
BRACED_VARIABLE_NAME = "{" ({VARIABLE_NAME}|"$"|{CAPPED_BRACED_VARIABLE}) "}"

// fixme this can be done in bareword parser
X_OP_STICKED = "x"[0-9]+

// atm making the same, but seems unary are different
PERL_OPERATORS_FILETEST = "-" [rwxoRWXOezsfdlpSbctugkTBMAC][^a-zA-Z0-9_]

HEREDOC_MARKER = [a-zA-Z0-9_]+
HEREDOC_MARKER_DQ = [^\"\n\r]*
HEREDOC_MARKER_SQ = [^\'\n\r]*
HEREDOC_MARKER_XQ = [^\`\n\r]*
HEREDOC_OPENER = "<<"({WHITE_SPACE}* \'{HEREDOC_MARKER_SQ}\' | {WHITE_SPACE}* \"{HEREDOC_MARKER_DQ}\" | {WHITE_SPACE}* \`{HEREDOC_MARKER_XQ}\` | {HEREDOC_MARKER})
HEREDOC_OPENER_BACKREF = "<<\\"[a-zA-Z0-9_]+

%state LEX_OPERATOR

%state LEX_QUOTE_LIKE_OPENER_Q, LEX_QUOTE_LIKE_OPENER_QQ, LEX_QUOTE_LIKE_OPENER_QX, LEX_QUOTE_LIKE_OPENER_QW
%state LEX_TRANS_OPENER, LEX_REGEX_OPENER

// custom lexical states to avoid crossing with navie ones
%state LEX_CUSTOM1, LEX_CUSTOM2, LEX_CUSTOM3, LEX_CUSTOM4, LEX_CUSTOM5, LEX_CUSTOM6, LEX_CUSTOM7, LEX_CUSTOM8, LEX_CUSTOM9, LEX_CUSTOM10
%state LEX_PREPARSED_ITEMS

%state LEX_VARIABLE_NAME, LEX_BRACED_VARIABLE_NAME

%%

// inclusive states
{NEW_LINE}   {return TokenType.NEW_LINE_INDENT;}
{WHITE_SPACE}+   {return TokenType.WHITE_SPACE;}

///////////////////////// package definition ///////////////////////////////////////////////////////////////////////////
<LEX_VARIABLE_NAME>{
	"$"										{yybegin(YYINITIAL); return VARIABLE_NAME;}
	{CAPPED_SINGLE_LETTER_VARIABLE_NAME}	{yybegin(YYINITIAL); return VARIABLE_NAME;}
	{SPECIAL_VARIABLE_NAME} 				{yybegin(YYINITIAL); return VARIABLE_NAME;}
	{IDENTIFIER} 							{yybegin(YYINITIAL); return VARIABLE_NAME;}
	{QAULIFIED_IDENTIFIER} 					{yybegin(YYINITIAL); return parsePackage(VARIABLE_NAME);}
}

<LEX_BRACED_VARIABLE_NAME>{
	"{" 							{return LEFT_BRACE;}
	"$"								{yybegin(YYINITIAL); return VARIABLE_NAME;}
	{CAPPED_BRACED_VARIABLE} / "}"	{yybegin(YYINITIAL); return VARIABLE_NAME;}
	{SPECIAL_VARIABLE_NAME} / "}" 	{yybegin(YYINITIAL); return VARIABLE_NAME;}
	{IDENTIFIER} / "}" 				{yybegin(YYINITIAL); return VARIABLE_NAME;}
	{QAULIFIED_IDENTIFIER} / "}"  	{yybegin(YYINITIAL); return parsePackage(VARIABLE_NAME);}
}

<LEX_OPERATOR>{
	"**"	{return OPERATOR_POW;}
	"%=" 	{return OPERATOR_MOD_ASSIGN;}
	"*=" 	{return OPERATOR_MUL_ASSIGN;}
	"&&" 	{return OPERATOR_AND;}
	"&=" 	{return OPERATOR_BITWISE_AND_ASSIGN;}
	"**=" 	{return OPERATOR_POW_ASSIGN;}
	"&&="	{return OPERATOR_AND_ASSIGN;}
	"*" 	{return OPERATOR_MUL;}
	"%" 	{return OPERATOR_MOD;}
	"&" 	{return OPERATOR_BITWISE_AND;}
	">="	{return OPERATOR_GE_NUMERIC;}
	"<="	{return OPERATOR_LE_NUMERIC;}
	"=="	{return OPERATOR_EQ_NUMERIC;}
	"!="	{return OPERATOR_NE_NUMERIC;}
	"~~"	{return OPERATOR_SMARTMATCH;}
	"+="	{return OPERATOR_PLUS_ASSIGN;}
	"-="	{return OPERATOR_MINUS_ASSIGN;}
	"/=" 	{return OPERATOR_DIV_ASSIGN;}
	".=" 	{return OPERATOR_CONCAT_ASSIGN;}
	"x=" 	{return OPERATOR_X_ASSIGN;}
	"|=" 	{return OPERATOR_BITWISE_OR_ASSIGN;}
	"^=" 	{return OPERATOR_BITWISE_XOR_ASSIGN;}
	"<<=" 	{return OPERATOR_SHIFT_LEFT_ASSIGN;}
	">>=" 	{return OPERATOR_SHIFT_RIGHT_ASSIGN;}
	"||=" 	{return OPERATOR_OR_ASSIGN;}
	"//=" 	{return OPERATOR_OR_DEFINED_ASSIGN;}
	"<=>" 	{return OPERATOR_CMP_NUMERIC;}
	"<" 	{return guessLtNumeric();}
	">" 	{return OPERATOR_GT_NUMERIC;}

	"->" 	{return parseOperatorDereference();}
	"=>" 	{return OPERATOR_COMMA_ARROW;}
	"," 	{return OPERATOR_COMMA;}

	"..." 	{return OPERATOR_HELLIP;}
	".." 	{return OPERATOR_FLIP_FLOP;}
	"." 	{return OPERATOR_CONCAT;}
	"=~" 	{return OPERATOR_RE;}
	"!~" 	{return OPERATOR_NOT_RE;}

	"<<" 	{return OPERATOR_SHIFT_LEFT;}
	">>" 	{return OPERATOR_SHIFT_RIGHT;}

	"||" 	{return OPERATOR_OR;}
	"?"  	{return QUESTION;}
	":"  	{return COLON;}
	"|" 	{return OPERATOR_BITWISE_OR;}
	"^" 	{return OPERATOR_BITWISE_XOR;}
	"=" 	{return OPERATOR_ASSIGN;}
}

<YYINITIAL>{
	"$$" / [^$\{\w] {return startVariableLexing(1,SIGIL_SCALAR);}
	"$"{VARIABLE_NAME} {return startVariableLexing(1,SIGIL_SCALAR);}

	"@$" / [^$\{\w] {return startVariableLexing(1,SIGIL_ARRAY);}
	"@"{VARIABLE_NAME} {return startVariableLexing(1, SIGIL_ARRAY);}

	"$#$" / [^$\{\w] {return startVariableLexing(2,SIGIL_SCALAR_INDEX);}
	"$#"{VARIABLE_NAME} {return startVariableLexing(2, SIGIL_SCALAR_INDEX);}

	"%$" / [^$\{\w] {return startVariableLexing(1,SIGIL_HASH);}
	"%"{VARIABLE_NAME} {return startVariableLexing(1, SIGIL_HASH);}

	"&$" / [^$\{\w] {return startVariableLexing(1,SIGIL_CODE);}
	"&"{VARIABLE_NAME} {return startVariableLexing(1, SIGIL_CODE);}

	"*$" / [^$\{\w] {return startVariableLexing(1,SIGIL_GLOB);}
	"*"{VARIABLE_NAME} {return startVariableLexing(1, SIGIL_GLOB);}

	"$"{BRACED_VARIABLE_NAME} {return startBracedVariableLexing(1, SIGIL_SCALAR);}
	"@"{BRACED_VARIABLE_NAME} {return startBracedVariableLexing(1, SIGIL_ARRAY);}
	"$#"{BRACED_VARIABLE_NAME} {return startBracedVariableLexing(2, SIGIL_SCALAR_INDEX);}
	"%"{BRACED_VARIABLE_NAME} {return startBracedVariableLexing(1, SIGIL_HASH);}
	"&"{BRACED_VARIABLE_NAME} {return startBracedVariableLexing(1, SIGIL_CODE);}
	"*"{BRACED_VARIABLE_NAME} {return startBracedVariableLexing(1, SIGIL_GLOB);}
}

//	{DQ_STRING}	{return STRING_IDENTIFIER;}
//	{SQ_STRING} {return STRING_IDENTIFIER;}
//	{XQ_STRING} {return STRING_IDENTIFIER;}

{HEREDOC_OPENER}   {return parseHeredocOpener();}
{HEREDOC_OPENER_BACKREF} {return parseHeredocOpenerBackref();}


"\"" 	{return QUOTE_DOUBLE;}
"`" 	{return QUOTE_TICK;}
"'" 	{return QUOTE_SINGLE;}


"++" 	{return OPERATOR_PLUS_PLUS;}
"--" 	{return OPERATOR_MINUS_MINUS;}

"!" 	{return OPERATOR_NOT;}


"\\" 	{return OPERATOR_REFERENCE;}

"+" 	{return OPERATOR_PLUS;}
"-" 	{return OPERATOR_MINUS;}
"/"   	{return guessDiv(); }

"~" 	{return OPERATOR_BITWISE_NOT;}
";"     {return SEMICOLON;}


"@" 	{return SIGIL_ARRAY;}
"$#" 	{return SIGIL_SCALAR_INDEX;}
"$" 	{ return SIGIL_SCALAR; }
"%"		{ return SIGIL_HASH;}
"*"		{return SIGIL_GLOB;}
"&"		{return SIGIL_CODE;}

"{"     {return LEFT_BRACE;}
"}"     {return RIGHT_BRACE;}
"["     {return LEFT_BRACKET;}
"]"     {return RIGHT_BRACKET;}
"("     {return LEFT_PAREN;}
")"     {return RIGHT_PAREN;}

{NUMBER_INT_SIMPLE} {return NUMBER_SIMPLE;}
{NUMBER}        {return parseNumber();}
{PERL_VERSION}  {return parseVersion();}

///////////////////////////////// PERL VARIABLE ////////////////////////////////////////////////////////////////////////


{PERL_OPERATORS_FILETEST} {yypushback(1);return OPERATOR_FILETEST;}

{X_OP_STICKED} {return checkOperatorXSticked();} // for x100
{BAREWORD_MINUS} {return parseBarewordMinus();}
{PACKAGE_SHORT} {return PACKAGE_IDENTIFIER;}			// only ::
{QAULIFIED_IDENTIFIER} {return parsePackage(IDENTIFIER);}

/* error fallback [^] */
[^]    { return TokenType.BAD_CHARACTER; }
