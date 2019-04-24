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
package com.perl5.lang.pod.lexer;

import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.pod.lexer.PodElementTypes;
import com.perl5.lang.perl.lexer.PerlProtoLexer;
import org.jetbrains.annotations.NotNull;

%%

%class PodLexerGenerated
%extends PerlProtoLexer
%implements PodElementTypes
%abstract
%unicode
%public

%function perlAdvance
%type IElementType

%{
	protected abstract IElementType parseFallback();
        protected abstract IElementType parseExample();
        protected abstract IElementType parseCutToken();
        protected abstract void pushAngle();
        @NotNull
        protected abstract IElementType popAngle();
        protected abstract void checkPendingSpacedAngles();
        @NotNull
        protected abstract IElementType lexOptional(@NotNull IElementType type);
%}

NEW_LINE = \R
WHITE_SPACE     = [ \t\f]
COMPLEX_ANGLE_PREFIX = ({WHITE_SPACE}+\R?|\R)
HARD_NEW_LINE = {NEW_LINE}({WHITE_SPACE}*{NEW_LINE})+
NONSPACE = [^ \t\f\r\n]
EXAMPLE = {WHITE_SPACE}+{NONSPACE}+
TAG_NAME = "=" [:jletterdigit:]+ 

NUMBER_EXP = [eE][+-]?[0-9_]+
NUMBER_FLOAT = "." ([0-9][0-9_]*)?
NUMBER_INT = [0-9][0-9_] *  {NUMBER_FLOAT}? {NUMBER_EXP}?
NUMBER_SMALL = {NUMBER_FLOAT}{NUMBER_EXP}?
NUMBER_HEX = "0"[xX][0-9a-fA-F_]+
NUMBER_BIN = "0"[bB][01_]+
NUMBER = {NUMBER_HEX} | {NUMBER_BIN}| {NUMBER_INT} | {NUMBER_SMALL}
IDENTIFIER = [\w_]+
IDENTIFIER_SUFFIX = [IBCLEFSXZ] "<"

HEAD2_TOKEN= "=head2" | "=method" | "=func" | "=attr"

%state LEX_PREPARSED_ITEMS
%state LEX_COMMAND_READY, LEX_COMMAND_WAITING
%state OPENING_ANGLE
%state CLOSING_SPACED_ANGLE
%%

<OPENING_ANGLE>{
  "<""<"+ / [\s]  {pushAngle();yybegin(YYINITIAL);return POD_ANGLE_LEFT;}
  // this rule should supersed << in C<<manpage()>> case
  "<" / "<"[^<]   {pushAngle();yybegin(YYINITIAL);return POD_ANGLE_LEFT;}
  "<"             {pushAngle();yybegin(YYINITIAL);return POD_ANGLE_LEFT;}
}

<CLOSING_SPACED_ANGLE>{
  ">" +        {yybegin(YYINITIAL);return popAngle();}
}

<LEX_COMMAND_WAITING>{
	"=pod"			{yybegin(YYINITIAL);return POD_POD;}
	"=head1"		{yybegin(YYINITIAL);return POD_HEAD1;}
	{HEAD2_TOKEN}		{yybegin(YYINITIAL);return POD_HEAD2;}
	"=head3"		{yybegin(YYINITIAL);return POD_HEAD3;}
	"=head4"		{yybegin(YYINITIAL);return POD_HEAD4;}
	"=item"			{yybegin(YYINITIAL);return POD_ITEM;}
	"=back"			{yybegin(YYINITIAL);return POD_BACK;}
	"=over"			{yybegin(YYINITIAL);return POD_OVER;}
	"=begin"		{yybegin(YYINITIAL);return POD_BEGIN;}
	"=end"			{yybegin(YYINITIAL);return POD_END;}
	"=for"			{yybegin(YYINITIAL);return POD_FOR;}
	"=encoding"		{yybegin(YYINITIAL);return POD_ENCODING;}
	"=cut"			{return parseCutToken();}
	{TAG_NAME} 		{yybegin(YYINITIAL);return POD_UNKNOWN;}
	{EXAMPLE}		{return parseExample();}
	{WHITE_SPACE}+ 	{yybegin(LEX_COMMAND_READY);return TokenType.WHITE_SPACE;}
	{NONSPACE}		{yybegin(YYINITIAL);yypushback(yylength());}
}

<LEX_COMMAND_READY>{
	{NEW_LINE} {yybegin(LEX_COMMAND_WAITING); return TokenType.WHITE_SPACE;}
}

<YYINITIAL>{
	{HARD_NEW_LINE} {yypushback(yylength()-1);yybegin(LEX_COMMAND_READY);return POD_NEWLINE;}
}
{NEW_LINE} {return TokenType.WHITE_SPACE;}
{WHITE_SPACE}+ {return TokenType.WHITE_SPACE;}
{COMPLEX_ANGLE_PREFIX} / ">" {checkPendingSpacedAngles();return TokenType.WHITE_SPACE;}

">" {return popAngle();}
"<" {return POD_SYMBOL;}
"(" {return POD_PAREN_LEFT;}
")" {return POD_PAREN_RIGHT;}
"{" {return POD_BRACE_LEFT;}
"}" {return POD_BRACE_RIGHT;}
"[" {return POD_BRACKET_LEFT;}
"]" {return POD_BRACKET_RIGHT;}
"/" {return lexOptional(POD_DIV);}
"\\" {return POD_BACKREF;}
"*" {return POD_ASTERISK;}
"|" {return lexOptional(POD_PIPE);}
":" {return POD_COLON;}
"\"" {return POD_QUOTE_DOUBLE;}
"'" {return POD_QUOTE_SINGLE;}
"`" {return POD_QUOTE_TICK;}
"I" / "<" {yybegin(OPENING_ANGLE);return POD_I;}
"B" / "<" {yybegin(OPENING_ANGLE);return POD_B;}
"C" / "<" {yybegin(OPENING_ANGLE);return POD_C;}
"L" / "<" {yybegin(OPENING_ANGLE);return POD_L;}
"E" / "<" {yybegin(OPENING_ANGLE);return POD_E;}
"F" / "<" {yybegin(OPENING_ANGLE);return POD_F;}
"S" / "<" {yybegin(OPENING_ANGLE);return POD_S;}
"X" / "<" {yybegin(OPENING_ANGLE);return POD_X;}
"Z" / "<" {yybegin(OPENING_ANGLE);return POD_Z;}

{NUMBER} 	{return POD_NUMBER;}
{IDENTIFIER} / {IDENTIFIER_SUFFIX}   {return POD_IDENTIFIER;}
{IDENTIFIER}    {return POD_IDENTIFIER;}

[^] {return parseFallback();}