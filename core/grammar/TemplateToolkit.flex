package com.perl5.lang.tt2.lexer;

import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.lexer.PerlProtoLexer;
import com.perl5.lang.tt2.elementTypes.TemplateToolkitElementTypes;

%%

%class TemplateToolkitLexerGenerated
%extends PerlProtoLexer
%implements TemplateToolkitElementTypes
%abstract
%unicode
%public


%function perlAdvance
%type IElementType


%{
    public abstract IElementType parseIdentifier();
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
PERL_XIDS = [_a-zA-Z]
PERL_XIDC = [_a-zA-Z0-9]

IDENTIFIER = {PERL_XIDS} {PERL_XIDC}*

NUMBER_EXP = [eE][+-]?[0-9_]+
NUMBER_FLOAT = "." ([0-9][0-9_]*) // ? // fixme this causes incorrect lexing of foo.10.bar
NUMBER_INT_SIMPLE = [0-9]+
NUMBER_INT = [0-9][0-9_] *  {NUMBER_FLOAT}? {NUMBER_EXP}?
//NUMBER_SMALL = {NUMBER_FLOAT}{NUMBER_EXP}?
//NUMBER_HEX = "0x" [0-9a-fA-F]+
//NUMBER_BIN = "0b"[01]+
NUMBER = {NUMBER_INT} //| {NUMBER_HEX} | {NUMBER_BIN}
 // | {NUMBER_SMALL} // fixme this causes wrong lexing of foo.bar.10.smth

%state LEX_PREPARSED_ITEMS, LEX_HTML, LEX_TEMPLATE_BLOCK, LEX_TEMPLATE_LINE
%state LEX_DQ_STRING, LEX_SQ_STRING

%%

{NEW_LINE}   {return TokenType.WHITE_SPACE;}
{WHITE_SPACE}+   {return TokenType.WHITE_SPACE;}

"," {return TT2_COMMA;}
"." {return TT2_PERIOD;}
".." {return TT2_RANGE;}
";" {return TT2_SEMI;}

"==" {return TT2_EQUAL;}
"!=" {return TT2_NOT_EQUAL;}
"=" {return TT2_ASSIGN;}
"=>" {return TT2_ASSIGN;}
"<" {return TT2_LT;}
"<=" {return TT2_LE;}
">" {return TT2_GT;}
">=" {return TT2_GE;}
"&&" {return TT2_AND;}
"||" {return TT2_OR;}
"!" {return TT2_NOT;}
"?"  {return TT2_QUESTION;}
":"  {return TT2_COLON;}
"+" {return TT2_PLUS;}
"-" {return TT2_MINUS;}
"/"  {return TT2_DIV;}
"*" {return TT2_MUL;}
"%" {return TT2_MOD;}
"_" {return TT2_CONCAT;}
"|" {return TT2_FILTER;}
"~" {return TT2_TILDA;}

"'"     {return TT2_SQ;}
"\""    {return TT2_DQ;}
"\\"    {return TT2_ESCAPE;}

"$"     {return TT2_SIGIL_SCALAR; }
"{"     {return TT2_LEFT_BRACE;}
"}"     {return TT2_RIGHT_BRACE;}
"["     {return TT2_LEFT_BRACKET;}
"]"     {return TT2_RIGHT_BRACKET;}
"("     {return TT2_LEFT_PAREN;}
")"     {return TT2_RIGHT_PAREN;}

{NUMBER_INT_SIMPLE} {return TT2_NUMBER_SIMPLE;}
{NUMBER} {return TT2_NUMBER;}
{IDENTIFIER} {return parseIdentifier();}

[^]    { return TT2_SYMBOL; }
