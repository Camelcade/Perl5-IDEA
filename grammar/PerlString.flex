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

%class PerlStringLexerGenerated
%extends PerlBaseLexer
%abstract
%unicode
%public


%function perlAdvance
%type IElementType


%{
    public abstract IElementType parseEscape();
    public abstract IElementType parseSimpleVariable(IElementType sigilTokenType);
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
CAPPED_VARIABLE_NAME = "^"{PERL_XIDC}+

// fixme this plus is hacky for proper handling of single quote inside the string. Unsure that we should check this at all
PACKAGE = ("::"+ "'" ?) ? {IDENTIFIER} (("::"+ "'" ? | "::"* "'" ) {IDENTIFIER} )*  "::" *
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

SIMPLE_SCALAR = "${" "^"? {BAREWORD_MINUS} "}"
SIMPLE_ARRAY = "@{" "^"? {BAREWORD_MINUS} "}"

%state LEX_PREPARSED_ITEMS

%%

{NEW_LINE}   {return TokenType.NEW_LINE_INDENT;}
{WHITE_SPACE}+   {return TokenType.WHITE_SPACE;}
"<=>" {return OPERATOR_CMP_NUMERIC;}
"=>" {return FAT_COMMA;}
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
"," {return COMMA;}
"." {return OPERATOR_CONCAT;}
"!" {return OPERATOR_NOT;}
"?"  {return QUESTION;}
":"  {return COLON;}
";"  {return SEMICOLON;}
"+" {return OPERATOR_PLUS;}
"-" {return OPERATOR_MINUS;}
"//"  {return OPERATOR_OR_DEFINED;}
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
"("     {return LEFT_PAREN;}
")"     {return RIGHT_PAREN;}

"`"     {return QUOTE_TICK;}
"'"     {return QUOTE_SINGLE;}
"\""     {return QUOTE_DOUBLE;}
"\\"    {return parseEscape();}

{NUMBER_INT_SIMPLE} {return NUMBER_SIMPLE;}
{NUMBER} {return NUMBER;}

{SIMPLE_SCALAR} {return parseSimpleVariable(SIGIL_SCALAR);}
{SIMPLE_ARRAY} {return parseSimpleVariable(SIGIL_ARRAY);}

{CAPPED_VARIABLE_NAME} {return IDENTIFIER;}

{BAREWORD_MINUS} {return IDENTIFIER;}
{PACKAGE_SHORT} {return IDENTIFIER;}			// only ::
{PACKAGE} {return IDENTIFIER;}

[^]    { return TokenType.BAD_CHARACTER; }
