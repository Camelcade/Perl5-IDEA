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

import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.intellij.lexer.FlexLexer;

%%

%abstract
%class PerlStringLexerGenerated
%extends PerlBaseLexer
%abstract
%unicode
%public


%function advance
%type IElementType


%{
    //fixme this must be in skeleton
    public void setTokenStart(int position){zzCurrentPos = zzStartRead = position;}
    public void setTokenEnd(int position){zzMarkedPos = position;}
    public CharSequence getBuffer(){ return zzBuffer;}
    public char[] getBufferArray(){ return zzBufferArray;}
    public int getBufferEnd() {return zzEndRead;}
    public int getNextTokenStart(){ return zzMarkedPos;}
    public boolean isLastToken(){ return zzMarkedPos == zzEndRead; }
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
CAPPED_VARIABLE_NAME = "^"{PERL_XIDC}+

PACKAGE = ("::" + "'" ?) ? ({IDENTIFIER} ("::"+ "'" ? | "::"* "'" )) * {IDENTIFIER} "::" +
PACKAGE_PARSABLE = ("::" + "'" ?) ? ({IDENTIFIER} ("::"+ "'" ? | "::"* "'" )) + {IDENTIFIER}
PACKAGE_SHORT = "::"+ "'" ?

NUMBER_EXP = [eE][+-]?[0-9_]+
NUMBER_FLOAT = "." ([0-9][0-9_]*)?
NUMBER_INT_SIMPLE = [0-9]+
NUMBER_INT = [0-9][0-9_] *  {NUMBER_FLOAT}? {NUMBER_EXP}?
NUMBER_SMALL = {NUMBER_FLOAT}{NUMBER_EXP}?
NUMBER_HEX = "0x" [0-9a-fA-F]+
NUMBER_BIN = "0b"[01]+
NUMBER = {NUMBER_HEX} | {NUMBER_BIN}| {NUMBER_INT} | {NUMBER_SMALL}

BAREWORD_MINUS = "-" * {IDENTIFIER}


%xstate LEX_ESCAPED

%%

<LEX_ESCAPED> {
    [^]   {
        yybegin(YYINITIAL);
        return STRING_CONTENT;
    }
}

{NEW_LINE}   {return TokenType.NEW_LINE_INDENT;}
{WHITE_SPACE}+   {return TokenType.WHITE_SPACE;}
"<=>" {return OPERATOR_CMP_NUMERIC;}
"=>" {return OPERATOR_COMMA_ARROW;}
"..." {return OPERATOR_HELLIP;}
".." {return OPERATOR_FLIP_FLOP;}
"++" {return OPERATOR_PLUS_PLUS;}
"--" {return OPERATOR_MINUS_MINUS;}
"=~" {return OPERATOR_RE;}
"!~" {return OPERATOR_NOT_RE;}
"<<" {return OPERATOR_SHIFT_LEFT;}
">>" {return OPERATOR_SHIFT_RIGHT;}
"&&" {return OPERATOR_AND;}
"||" {return OPERATOR_OR;}
"->" {return OPERATOR_DEREFERENCE;}
"<" {return OPERATOR_LT_NUMERIC;}
">" {return OPERATOR_GT_NUMERIC;}
"," {return OPERATOR_COMMA;}
"." {return OPERATOR_CONCAT;}
"!" {return OPERATOR_NOT;}
"?"  {return QUESTION;}
":"  {return COLON;}
";"  {return SEMICOLON;}
"+" {return OPERATOR_PLUS;}
"-" {return OPERATOR_MINUS;}
"/"  {return OPERATOR_DIV;}
"*" {return OPERATOR_MUL;}
"%" {return OPERATOR_MOD;}
"&" {return OPERATOR_BITWISE_AND;}

"|" {return OPERATOR_BITWISE_OR;}
"^" {return OPERATOR_BITWISE_XOR;}
"~" {return OPERATOR_BITWISE_NOT;}

"=" {return OPERATOR_ASSIGN;}

"@"     {return SIGIL_ARRAY;}
"$#"    {return SIGIL_SCALAR_INDEX;}
"$"     {return SIGIL_SCALAR; }
"{"     {return LEFT_BRACE;}
"}"     {return RIGHT_BRACE;}
"["     {return LEFT_BRACKET;}
"]"     {return RIGHT_BRACKET;}

"`"     {return QUOTE_TICK;}
"'"     {return QUOTE_SINGLE;}
"\""     {return QUOTE_DOUBLE;}
"\\"    {
    yybegin(LEX_ESCAPED);
    return OPERATOR_REFERENCE;
}

{NUMBER_INT_SIMPLE} {return NUMBER_SIMPLE;}
{NUMBER} {return NUMBER;}

{BAREWORD_MINUS} {return parseBarewordMinus();}
{CAPPED_VARIABLE_NAME} {return IDENTIFIER;}
{PACKAGE_PARSABLE} {return parsePackage(); }
{PACKAGE_SHORT} {return PACKAGE_IDENTIFIER;}
{PACKAGE} {return parsePackageCanonical();}

[^]    { return STRING_CONTENT; }
