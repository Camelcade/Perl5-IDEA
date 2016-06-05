package com.perl5.lang.tt2.lexer;

import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.lexer.PerlProtoLexer;
import com.perl5.lang.tt2.elementTypes.TemplateToolkitElementTypes;

%%

%abstract
%class TemplateToolkitLexerGenerated
%extends PerlProtoLexer
%implements TemplateToolkitElementTypes
%abstract
%unicode
%public


%function perlAdvance
%type IElementType


%{
//    public abstract IElementType parseEscape();
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
NUMBER_FLOAT = "." ([0-9][0-9_]*)?
NUMBER_INT_SIMPLE = [0-9]+
NUMBER_INT = [0-9][0-9_] *  {NUMBER_FLOAT}? {NUMBER_EXP}?
NUMBER_SMALL = {NUMBER_FLOAT}{NUMBER_EXP}?
NUMBER_HEX = "0x" [0-9a-fA-F]+
NUMBER_BIN = "0b"[01]+
NUMBER = {NUMBER_HEX} | {NUMBER_BIN}| {NUMBER_INT} | {NUMBER_SMALL}

%state LEX_PREPARSED_ITEMS, LEX_HTML, LEX_TEMPLATE_BLOCK, LEX_TEMPLATE_LINE

%%

{NEW_LINE}   {return TokenType.NEW_LINE_INDENT;}
{WHITE_SPACE}+   {return TokenType.WHITE_SPACE;}

//"," {return OPERATOR_COMMA;}
//"." {return OPERATOR_CONCAT;}
//"!" {return OPERATOR_NOT;}
//"?"  {return QUESTION;}
//":"  {return COLON;}
//";"  {return SEMICOLON;}
//"+" {return OPERATOR_PLUS;}
//"-" {return OPERATOR_MINUS;}
//"/"  {return OPERATOR_DIV;}
//"*" {return OPERATOR_MUL;}
//"%" {return OPERATOR_MOD;}
//"&" {return OPERATOR_BITWISE_AND;}
//
//"|" {return OPERATOR_BITWISE_OR;}
//"^" {return OPERATOR_BITWISE_XOR;}
//"~" {return OPERATOR_BITWISE_NOT;}
//
//"=" {return OPERATOR_ASSIGN;}
//
//"$"     {return SIGIL_SCALAR; }
//"{"     {return LEFT_BRACE;}
//"}"     {return RIGHT_BRACE;}
//"["     {return LEFT_BRACKET;}
//"]"     {return RIGHT_BRACKET;}
//"("     {return LEFT_PAREN;}
//")"     {return RIGHT_PAREN;}
//
//"`"     {return QUOTE_TICK;}
//"'"     {return QUOTE_SINGLE;}
//"\""     {return QUOTE_DOUBLE;}
//"\\"    {return parseEscape();}

//{NUMBER_INT_SIMPLE} {return NUMBER_SIMPLE;}

{NUMBER} {return TT2_NUMBER;}
{IDENTIFIER} {return TT2_IDENTIFIER;}

[^]    { return TT2_SYMBOL; }
