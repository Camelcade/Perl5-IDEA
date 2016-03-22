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

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.pod.lexer.PodElementTypes;
import org.jetbrains.annotations.NotNull;

%%

%class PodLexerGenerated
%extends PodBaseLexer
%implements FlexLexer, PodElementTypes
%unicode
%public

%function advance
%type IElementType

%{

%}

NEW_LINE = \r?\n
WHITE_SPACE     = [ \t\f]

NONSPACE = [^ \t\f\r\n]+

%state LEX_NEWLINE, LEX_HARD_NEWLINE
%%

<LEX_NEWLINE>{
	"=pod"	{yybegin(LEX_HARD_NEWLINE); return POD_POD;}
	"=head1"	{yybegin(LEX_HARD_NEWLINE);return POD_HEAD1;}
	"=head2"	{yybegin(LEX_HARD_NEWLINE);return POD_HEAD2;}
	"=head3"	{yybegin(LEX_HARD_NEWLINE);return POD_HEAD3;}
	"=head4"	{yybegin(LEX_HARD_NEWLINE);return POD_HEAD4;}
	"=over"	{yybegin(LEX_HARD_NEWLINE);return POD_OVER;}
	"=item"	{yybegin(LEX_HARD_NEWLINE);return POD_ITEM;}
	"=back"	{yybegin(LEX_HARD_NEWLINE);return POD_BACK;}
	"=begin"	{yybegin(LEX_HARD_NEWLINE);return POD_BEGIN;}
	"=end"	{yybegin(LEX_HARD_NEWLINE);return POD_END;}
	"=for"	{yybegin(LEX_HARD_NEWLINE);return POD_FOR;}
	"=encoding"	{yybegin(LEX_HARD_NEWLINE);return POD_ENCODING;}
	"=cut"	{yybegin(LEX_HARD_NEWLINE);return POD_CUT;}
}

<LEX_HARD_NEWLINE>{
	{NEW_LINE} {yybegin(LEX_NEWLINE); return POD_NEWLINE;}
}

{NEW_LINE} {yybegin(LEX_NEWLINE);return TokenType.NEW_LINE_INDENT;}
{WHITE_SPACE} {return TokenType.WHITE_SPACE;}
{NONSPACE} {return POD_IDENTIFIER;}

[^] {return TokenType.BAD_CHARACTER;}