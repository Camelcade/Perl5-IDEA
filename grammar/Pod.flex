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
	protected abstract IElementType parseFormatter(IElementType tokenType);
	protected abstract IElementType parseCloseAngle();
%}

NEW_LINE = \r?\n
WHITE_SPACE     = [ \t\f]
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

%state LEX_PREPARSED_ITEMS
%state LEX_COMMAND_READY, LEX_COMMAND_WAITING
%%

<LEX_COMMAND_WAITING>{
	"=pod"			{yybegin(YYINITIAL);return POD_POD;}
	"=head1"		{yybegin(YYINITIAL);return POD_HEAD1;}
	"=head2"		{yybegin(YYINITIAL);return POD_HEAD2;}
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
	{NEW_LINE} {yybegin(LEX_COMMAND_WAITING); return TokenType.NEW_LINE_INDENT;}
}

<YYINITIAL>
{
	{HARD_NEW_LINE} {yypushback(yylength()-1);yybegin(LEX_COMMAND_READY);return POD_NEWLINE;}
}
{NEW_LINE} {return TokenType.NEW_LINE_INDENT;}
{WHITE_SPACE}+ {return TokenType.WHITE_SPACE;}
">" {return parseCloseAngle();}
"(" {return POD_PAREN_LEFT;}
")" {return POD_PAREN_RIGHT;}
"{" {return POD_BRACE_LEFT;}
"}" {return POD_BRACE_RIGHT;}
"[" {return POD_BRACKET_LEFT;}
"]" {return POD_BRACKET_RIGHT;}
"/" {return POD_DIV;}
"\\" {return POD_BACKREF;}
"*" {return POD_ASTERISK;}
"|" {return POD_PIPE;}
":" {return POD_COLON;}
"\"" {return POD_QUOTE_DOUBLE;}
"'" {return POD_QUOTE_SINGLE;}
"`" {return POD_QUOTE_TICK;}
"I<" {return parseFormatter(POD_I);}
"B<" {return parseFormatter(POD_B);}
"C<" {return parseFormatter(POD_C);}
"L<" {return parseFormatter(POD_L);}
"E<" {return parseFormatter(POD_E);}
"F<" {return parseFormatter(POD_F);}
"S<" {return parseFormatter(POD_S);}
"X<" {return parseFormatter(POD_X);}
"Z<" {return parseFormatter(POD_Z);}

{NUMBER} 	{return POD_NUMBER;}

[^] {return parseFallback();}