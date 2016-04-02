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

%function advance
%type IElementType

%{
	protected abstract IElementType parseFallback();
	protected abstract IElementType parseExample();
	protected abstract IElementType parseCutToken();
%}

NEW_LINE = \r?\n
WHITE_SPACE     = [ \t\f]
HARD_NEW_LINE = {NEW_LINE}({WHITE_SPACE}*{NEW_LINE})+
NONSPACE = [^ \t\f\r\n]
EXAMPLE = {WHITE_SPACE}+{NONSPACE}+
TAG_NAME = [\w]

NUMBER_EXP = [eE][+-]?[0-9_]+
NUMBER_FLOAT = "." ([0-9][0-9_]*)?
NUMBER_INT_SIMPLE = [0-9]+
NUMBER_INT = [0-9][0-9_] *  {NUMBER_FLOAT}? {NUMBER_EXP}?
NUMBER_SMALL = {NUMBER_FLOAT}{NUMBER_EXP}?
NUMBER_HEX = "0"[xX][0-9a-fA-F_]+
NUMBER_BIN = "0"[bB][01_]+
NUMBER = {NUMBER_HEX} | {NUMBER_BIN}| {NUMBER_INT} | {NUMBER_SMALL}
NUMBER_INDENT = {NUMBER_INT_SIMPLE}("."{NUMBER_INT_SIMPLE})?

%state LEX_COMMAND_READY, LEX_COMMAND_WAITING
%xstate LEX_OVER, LEX_ENCODING
%xstate LEX_FORMAT_BEGIN, LEX_FORMAT_END, LEX_FORMAT_LINE
%state LEX_CAPTURE_LINE, LEX_CAPTURE_BLOCK
%%

<LEX_COMMAND_WAITING>{
	"=pod"			{yybegin(YYINITIAL);return POD_POD;}
	"=head1"		{yybegin(YYINITIAL);return POD_HEAD1;}
	"=head2"		{yybegin(YYINITIAL);return POD_HEAD2;}
	"=head3"		{yybegin(YYINITIAL);return POD_HEAD3;}
	"=head4"		{yybegin(YYINITIAL);return POD_HEAD4;}
	"=item"			{yybegin(YYINITIAL);return POD_ITEM;}
	"=back"			{yybegin(YYINITIAL);return POD_BACK;}

	"=over"			{yybegin(LEX_OVER);return POD_OVER;}
	"=begin"		{yybegin(LEX_FORMAT_BEGIN);return POD_BEGIN;}
	"=end"			{yybegin(LEX_FORMAT_END);return POD_END;}
	"=for"			{yybegin(LEX_FORMAT_LINE);return POD_FOR;}
	"=encoding"		{yybegin(LEX_ENCODING);return POD_ENCODING;}
	"=cut"			{return parseCutToken();}
	"="{TAG_NAME}+ 	{yybegin(YYINITIAL);return POD_UNKNOWN;}
	{EXAMPLE}		{return parseExample();}
	{WHITE_SPACE}+ 	{yybegin(LEX_COMMAND_READY);return TokenType.WHITE_SPACE;}
	{NONSPACE}		{yybegin(YYINITIAL);yypushback(yylength());}
}

<LEX_OVER>{
	{WHITE_SPACE}+ 	{return TokenType.WHITE_SPACE;}
	{NUMBER_INDENT} {yybegin(YYINITIAL);return POD_INDENT_LEVEL;}
	[^]				{yybegin(YYINITIAL);yypushback(yylength());}
}

<LEX_ENCODING>{
	{WHITE_SPACE}+ 	{return TokenType.WHITE_SPACE;}
	{NONSPACE}+ {yybegin(YYINITIAL);return POD_ENCODING_NAME;}
	[^]				{yybegin(YYINITIAL);yypushback(yylength());}
}

<LEX_FORMAT_BEGIN,LEX_FORMAT_END,LEX_FORMAT_LINE>{
	{WHITE_SPACE}+ 	{return TokenType.WHITE_SPACE;}
	":"{NONSPACE}+	{yypushback(yylength()-1);yybegin(LEX_FORMAT_END);return POD_COLON;}
	[^]				{yybegin(YYINITIAL);yypushback(yylength());}
}

<LEX_FORMAT_BEGIN>{
	{NONSPACE}+		{yybegin(LEX_CAPTURE_BLOCK);return POD_FORMAT_NAME;}
}
<LEX_FORMAT_END>{
	{NONSPACE}+		{yybegin(YYINITIAL);return POD_FORMAT_NAME;}
}
<LEX_FORMAT_LINE>{
	{NONSPACE}+		{yybegin(LEX_CAPTURE_LINE);return POD_FORMAT_NAME;}
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
"<" {return POD_ANGLE_LEFT;}
">" {return POD_ANGLE_RIGHT;}
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
"I<" {yypushback(1);return POD_I;}
"B<" {yypushback(1);return POD_B;}
"C<" {yypushback(1);return POD_C;}
"L<" {yypushback(1);return POD_L;}
"E<" {yypushback(1);return POD_E;}
"F<" {yypushback(1);return POD_F;}
"S<" {yypushback(1);return POD_S;}
"X<" {yypushback(1);return POD_X;}
"Z<" {yypushback(1);return POD_Z;}

{NUMBER} 	{return POD_NUMBER;}

[^] {return parseFallback();}