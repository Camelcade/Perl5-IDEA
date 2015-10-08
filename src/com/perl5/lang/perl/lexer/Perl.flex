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
    // fixme this must be in skeleton
    public void setTokenStart(int position){zzCurrentPos = zzStartRead = position;}
    public void setTokenEnd(int position){zzMarkedPos = position;}
    public CharSequence getBuffer(){ return zzBuffer;}
    public char[] getBufferArray(){ return zzBufferArray;}
    public int getBufferEnd() {return zzEndRead;}
    public int getNextTokenStart(){ return zzMarkedPos;}
    public boolean isLastToken(){ return zzMarkedPos == zzEndRead; }

    protected int trenarCounter = 0;

	public abstract IElementType guessDiv();
	public abstract IElementType getIdentifierToken();
	public abstract IElementType checkOperatorXSticked();
	public abstract IElementType parseVersion();
	public abstract IElementType parseNumber();
	public abstract IElementType parseOperatorDereference();
	public abstract IElementType parseCappedVariableName();
	public abstract IElementType parseHeredocOpener();
	public abstract IElementType guessLtNumeric();
%}


/*
// Char classes
*/
NEW_LINE = \r?\n
WHITE_SPACE     = [ \t\f]

// http://perldoc.perl.org/perldata.html#Identifier-parsing
//PERL_XIDS = [\w && \p{XID_Start}]
//PERL_XIDC = [\w && \p{XID_Continue}]

// added digits to opener for package Encode::KR::2022_KR;
PERL_XIDS = [_a-zA-Z0-9]
PERL_XIDC = [_a-zA-Z0-9]

IDENTIFIER = {PERL_XIDS} {PERL_XIDC}*

BAREWORD_MINUS = "-" * {IDENTIFIER}

CAPPED_VARIABLE_NAME = "^"{PERL_XIDC}+

PACKAGE = ("::" + "'" ?) ? ({IDENTIFIER} ("::"+ "'" ? | "::"* "'" )) * {IDENTIFIER} "::" +
PACKAGE_PARSABLE = ("::" + "'" ?) ? ({IDENTIFIER} ("::"+ "'" ? | "::"* "'" )) + {IDENTIFIER}
PACKAGE_SHORT = "::"+ "'" ?

PERL_VERSION_CHUNK = [0-9][0-9_]*
PERL_VERSION = "v"?{PERL_VERSION_CHUNK}("." {PERL_VERSION_CHUNK})*
// heading _ removed to avoid @_ parsing as sigil-number

NUMBER_EXP = [eE][+-]?[0-9_]+
NUMBER_FLOAT = "." ([0-9][0-9_]*)?
NUMBER_INT_SIMPLE = [0-9]+
NUMBER_INT = [0-9][0-9_] *  {NUMBER_FLOAT}? {NUMBER_EXP}?
NUMBER_SMALL = {NUMBER_FLOAT}{NUMBER_EXP}?
NUMBER_HEX = "0x" [0-9a-fA-F_]+
NUMBER_BIN = "0b"[01_]+
NUMBER = {NUMBER_HEX} | {NUMBER_BIN}| {NUMBER_INT} | {NUMBER_SMALL}

// fixme this can be done in bareword parser
X_OP_STICKED = "x"[0-9]+[^a-zA-Z]

// atm making the same, but seems unary are different
PERL_OPERATORS_FILETEST = "-" [rwxoRWXOezsfdlpSbctugkTBMAC][^a-zA-Z0-9_]

HEREDOC_MARKER = [a-zA-Z0-9_]+
HEREDOC_MARKER_DQ = [^\"\n\r]+
HEREDOC_MARKER_SQ = [^\'\n\r]+
HEREDOC_MARKER_XQ = [^\`\n\r]+
HEREDOC_OPENER = "<<"({WHITE_SPACE}* \'{HEREDOC_MARKER_SQ}\' | {WHITE_SPACE}* \"{HEREDOC_MARKER_DQ}\" | {WHITE_SPACE}* \`{HEREDOC_MARKER_XQ}\` | {HEREDOC_MARKER})

%state LEX_CODE

%state LEX_HEREDOC_WAITING, LEX_HEREDOC_WAITING_QQ, LEX_HEREDOC_WAITING_QX
%state LEX_FORMAT_WAITING

%state LEX_QUOTE_LIKE_OPENER_Q, LEX_QUOTE_LIKE_OPENER_QQ, LEX_QUOTE_LIKE_OPENER_QX, LEX_QUOTE_LIKE_OPENER_QW
%state LEX_TRANS_OPENER, LEX_REGEX_OPENER

%state LEX_HTML_BLOCK
%state LEX_MOJO_PERL_LINE, LEX_MOJO_PERL_BLOCK, LEX_MOJO_PERL_LINE_SEMI, LEX_MOJO_PERL_BLOCK_SEMI
%state LEX_PREPARSED_ITEMS

%%

// inclusive states
{NEW_LINE}   {return TokenType.NEW_LINE_INDENT;}
{WHITE_SPACE}+   {return TokenType.WHITE_SPACE;}
";"     {return SEMICOLON;}

///////////////////////// package definition ///////////////////////////////////////////////////////////////////////////

{HEREDOC_OPENER}   {return parseHeredocOpener();}

"\"" {return QUOTE_DOUBLE;}
"`" {return QUOTE_TICK;}
"'" {return QUOTE_SINGLE;}

"<=>" {return OPERATOR_CMP_NUMERIC;}
"<" {return guessLtNumeric();}
">" {return OPERATOR_GT_NUMERIC;}

"->" {return parseOperatorDereference();}
"=>" {return OPERATOR_COMMA_ARROW;}
"," {return OPERATOR_COMMA;}

"..." {return OPERATOR_HELLIP;}
".." {return OPERATOR_FLIP_FLOP;}
"." {return OPERATOR_CONCAT;}

"++" {return OPERATOR_PLUS_PLUS;}
"--" {return OPERATOR_MINUS_MINUS;}

"=~" {return OPERATOR_RE;}
"!~" {return OPERATOR_NOT_RE;}

"<<" {return OPERATOR_SHIFT_LEFT;}
">>" {return OPERATOR_SHIFT_RIGHT;}

"&&" {return OPERATOR_AND;}
"||" {return OPERATOR_OR;}
"!" {return OPERATOR_NOT;}

{X_OP_STICKED} {return checkOperatorXSticked();} // fixme split this ?


"?"  {return QUESTION;}
":"  {return COLON;}

"\\" {return OPERATOR_REFERENCE;}

"+" {return OPERATOR_PLUS;}
"-" {return OPERATOR_MINUS;}
"/"   { return guessDiv(); }
"*" {return OPERATOR_MUL;}
"%" {return OPERATOR_MOD;}
"&" {return OPERATOR_BITWISE_AND;}

"|" {return OPERATOR_BITWISE_OR;}
"^" {return OPERATOR_BITWISE_XOR;}
"~" {return OPERATOR_BITWISE_NOT;}

"=" {return OPERATOR_ASSIGN;}

"@" {return SIGIL_ARRAY;}
"$#" {return SIGIL_SCALAR_INDEX;}
"$" { return SIGIL_SCALAR; }

"{"             {return LEFT_BRACE;}
"}"             {return RIGHT_BRACE;}
"["             {return LEFT_BRACKET;}
"]"             {return RIGHT_BRACKET;}
"("             {return LEFT_PAREN;}
")"             {return RIGHT_PAREN;}

{NUMBER_INT_SIMPLE} {return NUMBER_SIMPLE;}
{NUMBER}        {return parseNumber();}
{PERL_VERSION}  {return parseVersion();}

///////////////////////////////// PERL VARIABLE ////////////////////////////////////////////////////////////////////////


{PERL_OPERATORS_FILETEST} {yypushback(1);return OPERATOR_FILETEST;}

{CAPPED_VARIABLE_NAME} {return parseCappedVariableName();}

// fixme refactor
{BAREWORD_MINUS} {return parseBarewordMinus();}
{PACKAGE_PARSABLE} {return parsePackage(); }
{PACKAGE_SHORT} {return PACKAGE_IDENTIFIER;}
{PACKAGE} {return parsePackageCanonical();}

/* error fallback [^] */
[^]    { return TokenType.BAD_CHARACTER; }
